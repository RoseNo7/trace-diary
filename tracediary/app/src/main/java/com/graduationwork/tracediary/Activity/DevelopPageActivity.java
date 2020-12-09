package com.graduationwork.tracediary.Activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.graduationwork.tracediary.Alarm.AlarmReceiver;
import com.graduationwork.tracediary.Data.SharedPreference;
import com.graduationwork.tracediary.LocationInfo;
import com.graduationwork.tracediary.R;

import static com.graduationwork.tracediary.Alarm.Alarm.AlarmIntent;
import static com.graduationwork.tracediary.Alarm.Alarm.alarmManager;
import static com.graduationwork.tracediary.Alarm.Alarm.pendingIntent;

/* 값 확인 하려고 만듬 */
public class DevelopPageActivity extends AppCompatActivity {
    Button A;
    TextView B;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_develop_page);

        B = (TextView) findViewById(R.id.textView);

        A = (Button) findViewById(R.id.btn);
        A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                B.setText(SharedPreference.getSharedDate(DevelopPageActivity.this) + "\n");
                B.append(SharedPreference.getSharedTime(DevelopPageActivity.this) + "\n");
                B.append(SharedPreference.getSharedPlace(DevelopPageActivity.this) + "\n");
                B.append(SharedPreference.getSharedLatitude(DevelopPageActivity.this) + "\n");
                B.append(SharedPreference.getSharedLongitude(DevelopPageActivity.this) + "\n" );
                B.append(SharedPreference.getSharedAccuracy(DevelopPageActivity.this) + "\n" );
                B.append(SharedPreference.getSharedRadius(DevelopPageActivity.this) + "\n");
                //B.append(SharedPreference.getSharedNum(DevelopPageActivity.this) + "\n");
                B.append(SharedPreference.getSharedAlarmSet(DevelopPageActivity.this) + "\n");


                B.append(String.valueOf(alarmManager)+"\n");

                AlarmIntent = new Intent(DevelopPageActivity.this, AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(DevelopPageActivity.this, 0, AlarmIntent, PendingIntent.FLAG_NO_CREATE);

                B.append(String.valueOf(pendingIntent) + "\n");

                /*
                B.append(String.valueOf(dis) + "\n");
                B.append(String.valueOf(G) + "\n");
                B.append(String.valueOf(H) + "\n");
                */
            }
        });
    }
}