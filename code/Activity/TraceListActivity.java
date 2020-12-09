package com.graduationwork.tracediary.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.graduationwork.tracediary.Adapter.RecyclerViewAdapter;
import com.graduationwork.tracediary.Data.DBhelper;
import com.graduationwork.tracediary.Data.LocationLogFile;
import com.graduationwork.tracediary.LocationDTO;
import com.graduationwork.tracediary.R;
import com.graduationwork.tracediary.SpaceItemDecoration;

import static com.graduationwork.tracediary.Adapter.LogRecyclerViewAdapter.logLocationDTOs;
import static com.graduationwork.tracediary.Adapter.RecyclerViewAdapter.locationDTOs;

public class TraceListActivity extends AppCompatActivity implements OnMapReadyCallback {
    /*GoogleMap.OnMarkerClickListener */

    TextView text_selected;
    String selectedDate;                                    /* 선택된 날짜 */

    SupportMapFragment supportMapFragment;                  /* Google Map */

    /* DB */
    DBhelper helper;
    SQLiteDatabase db;

    LatLng curLatLng = null;
    LatLng preLatLng = null;

    LinearLayout logbtn;

    public static GoogleMap googleMap;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace_list);

        /* 구글 맵 */
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewAdapter = new RecyclerViewAdapter();

        /* 구분선 */
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));

        /* 누른 날짜 */
        Intent dateIntent = getIntent();
        selectedDate = dateIntent.getStringExtra("date");

        text_selected = (TextView) findViewById(R.id.select_date);
        text_selected.setText(selectedDate);

        logbtn = (LinearLayout) findViewById(R.id.btn_showlog);
        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logIntent = new Intent(TraceListActivity.this, LogLocationActivity.class);
                logIntent.putExtra("selectDate", selectedDate);
                startActivity(logIntent);




            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        locationDTOs.clear();
        SelectDB();

        drawRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (googleMap != null) {
            googleMap.clear();
            locationDTOs.clear();
            SelectDB();

            addMaker(googleMap);
            addLine(googleMap);

            drawRecyclerView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /* 맵이 준비가 되었을 때 */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        /* DB에 아무것도 없는 날은 서울 중심으로 지도를 보여준다 */
        LatLng futLatLng = new LatLng(37.551409, 126.990229);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(futLatLng, 9));

        /* 초점 */
        if(!locationDTOs.isEmpty()) {
            LatLng standLatLng = new LatLng(locationDTOs.get(0)._latitude, locationDTOs.get(0)._longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(standLatLng, 12));
        }

        addMaker(googleMap);
        addLine(googleMap);
    }

    /* RecyclerView */
    public void drawRecyclerView() {
            /* RecyclerView */
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    /* Maker 그리기 */
    public void addMaker(GoogleMap googleMap) {
        try {

            for (int i = 0; i < locationDTOs.size(); i++) {
                MarkerOptions makerOptions = new MarkerOptions();
                String _title;



                if (locationDTOs.get(i)._comment != null) {
                    _title = locationDTOs.get(i)._comment;
                } else {
                    _title = locationDTOs.get(i)._time;
                }

                makerOptions
                        .position(new LatLng(locationDTOs.get(i)._latitude, locationDTOs.get(i)._longitude))
                        .title(_title);

                googleMap.addMarker(makerOptions);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /* line 그리기 */
    public void addLine(GoogleMap googleMap){
        LocationLogFile lineAddFile = new LocationLogFile();
        lineAddFile.readFile(selectedDate);

        for(int i = 0; i < logLocationDTOs.size(); i++){
            Float accuracy_ = logLocationDTOs.get(i)._accuracy;

            /* 값이 있고 정확성이 높은 위치는 선으로 잇는다. */

             curLatLng = new LatLng(logLocationDTOs.get(i)._latitude,logLocationDTOs.get(i)._longitude);

            if(i == 0  ){

            }else{
                 googleMap.addPolyline(new PolylineOptions().add(preLatLng,curLatLng).width(5).color(Color.RED));
            }

             preLatLng=curLatLng;
        }
    }


    /* recyclerView에 데이터를 넣음 */
    public void SelectDB() {
        helper = new DBhelper(this);
        db = helper.getReadableDatabase();
        String table_Name = helper.getTableName();

        /* Recyclerview에 데이터를 넣음 */
        if (db != null) {
            String sql = "SELECT * FROM " + table_Name + " WHERE DATE = " + "'" + selectedDate + "'";
            Cursor cursor = db.rawQuery(sql, null);

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                String _Date = cursor.getString(0);
                String _Time = cursor.getString(1);
                String _Place = cursor.getString(2);
                Double _Lati = Double.parseDouble(cursor.getString(3));
                Double _Longi = Double.parseDouble(cursor.getString(4));
                float _Accu = Float.parseFloat(cursor.getString(5));
                String _Comme = cursor.getString(6);

                locationDTOs.add(new LocationDTO(_Date, _Time, _Place, _Lati, _Longi, _Accu, _Comme));

            }
            cursor.close();
        }
    }

    /*
    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }
    */
}
