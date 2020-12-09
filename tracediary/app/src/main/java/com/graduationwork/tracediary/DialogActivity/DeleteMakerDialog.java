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

import static com.graduationwork.tracediary.Adapter.RecyclerViewAdapter.locationDTOs;

public class DeleteMakerDialog extends Activity {
    LinearLayout delete_maker_button;
    LinearLayout delete_cancle_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_delete_maker_dialog);

        /* Maker 삭제 */
        delete_maker_button = (LinearLayout) findViewById(R.id.btn_maker_delete);
        delete_maker_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBhelper helper = new DBhelper(v.getContext());
                SQLiteDatabase db = helper.getWritableDatabase();
                String table_Name = helper.getTableName();

                Intent deleteIntent = getIntent();
                int position = deleteIntent.getIntExtra("selectDelete", -1);

                if (db != null) {
                    try {
                        String sql = "DELETE FROM " + table_Name +  " WHERE DATE = '" + locationDTOs.get(position)._date + "' AND TIME = '" + locationDTOs.get(position)._time + "'";

                        db.execSQL(sql);

                    } catch (Exception e) {
                    }

                    db.close();
                    finish();
                }
            }
        });

        /* 취소 */
        delete_cancle_button = (LinearLayout) findViewById(R.id.btn_maker_delete_cancle);
        delete_cancle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
