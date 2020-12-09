package com.graduationwork.tracediary;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.graduationwork.tracediary.Activity.DevelopPageActivity;
import com.graduationwork.tracediary.Activity.RecordingActivity;
import com.graduationwork.tracediary.Activity.SettingActivity;
import com.graduationwork.tracediary.Activity.TraceListActivity;
import com.graduationwork.tracediary.Alarm.Alarm;
import com.graduationwork.tracediary.Data.SharedPreference;

import java.util.ArrayList;

import static com.graduationwork.tracediary.Adapter.RecyclerViewAdapter.locationDTOs;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    private BackKeyPressHandler backKeyPressHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 처음 사용하는 경우 : 알람을 시작 */
        boolean app_use = SharedPreference.getSharedUse(this);

        if (!app_use) {
            SharedPreference.putShared(this);

            Alarm alarm = new Alarm(this);
            alarm.setAlarm();
        }


        /* 권한 확인 */
        requirePermission();

        /* 화면 */
        draw_main();

        /* 달력 */
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String day;

                if (dayOfMonth < 10) {
                    day = "0" + Integer.toString(dayOfMonth);
                } else {
                    day = Integer.toString(dayOfMonth);
                }

                String selectDate = year + "-" + (month+1) + "-" + day;

                locationDTOs.clear();

                Intent intent = new Intent(MainActivity.this, TraceListActivity.class);
                intent.putExtra("date", selectDate);
                startActivity(intent);
            }
        });
    }


    void draw_main() {
        /* toolbar 셋팅 */
        Toolbar main_toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(main_toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);            /* actionbar의 이름 지움 */

        main_toolbar.findViewById(R.id.toolbar_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, main_toolbar, 0, 0);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        /* NavigationView */
        NavigationView navigationView = (NavigationView) findViewById(R.id.main_navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.first) {
                    Intent intent = new Intent (MainActivity.this, RecordingActivity.class);
                    startActivity(intent);

                } else if (item.getItemId() == R.id.second) {
                   Intent intent = new Intent (MainActivity.this, SettingActivity.class);
                   startActivity(intent);

                } else if (item.getItemId() == R.id.third) {
                    Intent intent = new Intent (MainActivity.this, DevelopPageActivity.class);
                    startActivity(intent);
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        backKeyPressHandler = new BackKeyPressHandler(this);
    }

    /* 권한 확인 */
    void requirePermission() {
        String[] permissions = new String[] {Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO};
        ArrayList<String> listPermissionNeeded = new ArrayList<>();

        for (String permission : permissions) {

            /* 권한 허가 확인 */
            if (ContextCompat.checkSelfPermission((this), permission) == PackageManager.PERMISSION_DENIED) {
                listPermissionNeeded.add(permission);
            }
        }

        /* 권한 요청 */
        if (!listPermissionNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]), 1);
        }
    }


    @Override
    public void onBackPressed() {
        backKeyPressHandler.onBackPressed();
    }
}
