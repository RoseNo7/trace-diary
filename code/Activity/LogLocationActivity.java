package com.graduationwork.tracediary.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.graduationwork.tracediary.Adapter.LogRecyclerViewAdapter;
import com.graduationwork.tracediary.Data.LocationLogFile;
import com.graduationwork.tracediary.R;
import com.graduationwork.tracediary.SpaceItemDecoration;

public class LogLocationActivity extends AppCompatActivity {
    TextView text_LogDate;
    String logDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_location);

        Intent log_Intent = getIntent();
        logDate = log_Intent.getStringExtra("selectDate");

        text_LogDate = (TextView) findViewById(R.id.log_date);
        text_LogDate.setText(logDate);

        LocationLogFile readFile = new LocationLogFile();
        readFile.readFile(logDate);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.log_recycler_view);
        RecyclerView.LayoutManager logLayoutManager = new LinearLayoutManager(this);
        LogRecyclerViewAdapter logRecyclerViewAdapter = new LogRecyclerViewAdapter();

        /* 구분선 */
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));

        recyclerView.setLayoutManager(logLayoutManager);
        recyclerView.setAdapter(logRecyclerViewAdapter);
    }
}
