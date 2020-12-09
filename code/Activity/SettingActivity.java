package com.graduationwork.tracediary.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.graduationwork.tracediary.Alarm.Alarm;
import com.graduationwork.tracediary.Data.SharedPreference;
import com.graduationwork.tracediary.R;


public class SettingActivity extends AppCompatActivity {

    Button btn_set_Alarm;
    Button btn_set_Radius;

    EditText edit_radius;

    TextView  state_set_radius;
    TextView  state_set_alarm;

    Alarm alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        edit_radius = (EditText) findViewById(R.id.insert_radius);

        state_set_alarm  =  (TextView) findViewById(R.id.text_alarm_state);
        state_set_radius = (TextView) findViewById(R.id.text_alarm_radius);

        state_set_alarm.setText(String.valueOf(SharedPreference.getSharedAlarmSet(this)));

        float get_radius = (SharedPreference.getSharedRadius(this));
        state_set_radius.setText(String.valueOf(get_radius));

        alarm = new Alarm(SettingActivity.this);

        /* 툴바 + back key */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }



        /* 알람 ON/OFF */
        btn_set_Alarm = (Button) findViewById(R.id.setting_alarm_OnOff);
        btn_set_Alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* 꺼져있다면 */
                if (!SharedPreference.getSharedAlarmSet(SettingActivity.this)) {
                    SharedPreference.putShared(SettingActivity.this, true);

                    btn_set_Alarm.setText("ON");

                    alarm.setAlarm();

                } else {
                    alarm.stopAlarm();

                    SharedPreference.putShared(SettingActivity.this, false);
                    btn_set_Alarm.setText("OFF");
                }

                state_set_alarm.setText(String.valueOf(SharedPreference.getSharedAlarmSet(SettingActivity.this)));
            }
        });

        if (SharedPreference.getSharedAlarmSet(this)) {
            btn_set_Alarm.setText("ON");
        } else {
            btn_set_Alarm.setText("OFF");
        }

        /* 반경 설정 */
        btn_set_Radius = (Button) findViewById(R.id.setting_radius);
        btn_set_Radius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String inserted_radius = edit_radius.getText().toString();
                    state_set_radius.setText(inserted_radius);

                    float set_radius = Float.parseFloat(inserted_radius);
                    SharedPreference.putShared(SettingActivity.this, set_radius);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* toolbar : back버튼 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

