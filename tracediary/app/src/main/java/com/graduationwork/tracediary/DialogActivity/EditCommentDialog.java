package com.graduationwork.tracediary.DialogActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.graduationwork.tracediary.Data.DBhelper;
import com.graduationwork.tracediary.R;

import static com.graduationwork.tracediary.Adapter.RecyclerViewAdapter.locationDTOs;

public class EditCommentDialog extends Activity {
    EditText comment_edit;

    Button comment_commit_button;
    Button comment_cancle_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comment_dialog);

        comment_edit = (EditText) findViewById(R.id.edit_comment);

        /* Maker Comment 추가 */
        comment_commit_button = (Button) findViewById(R.id.btn_comment_commit);
        comment_commit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBhelper helper = new DBhelper(v.getContext());
                SQLiteDatabase db = helper.getWritableDatabase();
                String table_Name = helper.getTableName();

                Intent editIntent = getIntent();
                int position = editIntent.getIntExtra("selectMaker", -1);

                String comment = comment_edit.getText().toString();

                if (db != null) {
                    try {
                        String sql = "UPDATE " + table_Name + " SET COMME = '" + comment + "' WHERE DATE = '" +
                                locationDTOs.get(position)._date + "' AND TIME = '" + locationDTOs.get(position)._time +  "'";

                        db.execSQL(sql);

                    } catch (Exception e) {
                    }
                    db.close();
                }


                finish();

            }
        });

        /* 취소 */
        comment_cancle_button = (Button) findViewById(R.id.btn_comment_cancel);
        comment_cancle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
