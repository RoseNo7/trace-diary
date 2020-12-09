package com.graduationwork.tracediary.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.graduationwork.tracediary.AudioRecordThread;
import com.graduationwork.tracediary.R;

public class RecordingActivity extends AppCompatActivity {
    Button btn;
    boolean isR = false;
    AudioRecordThread audioRecodeThread;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        /* 툴바 + back key */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_sub);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        audioRecodeThread = new AudioRecordThread();

        btn = (Button) findViewById(R.id.playBtn);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isR) {
                    audioRecodeThread.start();
                    isR = true;
                    btn.setText("녹음 중지");
                } else {
                    audioRecodeThread.stopRecording();
                    isR = false;
                    btn.setText("녹음 하기");
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        audioRecodeThread.stopRecording();
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
