package com.graduationwork.tracediary.DialogActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.graduationwork.tracediary.Data.DBhelper;
import com.graduationwork.tracediary.R;

import static com.graduationwork.tracediary.Adapter.LogRecyclerViewAdapter.logLocationDTOs;
import static com.graduationwork.tracediary.Adapter.RecyclerViewAdapter.locationDTOs;

public class InsertMakerDialog extends Activity {
    LinearLayout insert_maker_button;
    LinearLayout insert_cancle_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_insert_maker_dialog);

        /* Maker 추가 */
        insert_maker_button = (LinearLayout) findViewById(R.id.btn_maker_insert);
        insert_maker_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBhelper helper = new DBhelper(v.getContext());
                SQLiteDatabase db = helper.getWritableDatabase();
                String table_Name = helper.getTableName();

                Intent insertIntent = getIntent();
                int position = insertIntent.getIntExtra("selectAdd", -1);

                if (db != null) {
                    try {
                        String sql = "INSERT INTO " + table_Name + "(DATE, TIME, PLACE, LATI, LONGI, ACCU) values (?, ?, ?, ?, ?, ?)";
                        Object[] params = {logLocationDTOs.get(position)._date, logLocationDTOs.get(position)._time ,logLocationDTOs.get(position)._place,
                                logLocationDTOs.get(position)._latitude, logLocationDTOs.get(position)._longitude, logLocationDTOs.get(position)._accuracy};

                        db.execSQL(sql, params);

                    } catch (Exception e) {
                    }

                    db.close();
                    finish();
                }
            }
        });

        /* 취소 */
        insert_cancle_button = (LinearLayout) findViewById(R.id.btn_maker_insert_cancle);
        insert_cancle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
