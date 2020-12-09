package com.graduationwork.tracediary.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Tracediary.db";              /* DB 이름 */
    private static final String DATABASE_DIR = "/mnt/sdcard/TraceDiary/";    /* DB 디렉토리 */
    private static final String TABLE_NAME = "location";                        /* DB 테이블 이름 */
    private static final int    VERSION = 1;

    public DBhelper(Context context) {
        super(context, DATABASE_DIR + DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    "(DATE TEXT, TIME TEXT, PLACE TEXT, LATI DOUBLE, LONGI DOUBLE, ACCU FLOAT, COMME TEXT, PRIMARY KEY(DATE, TIME))";
            db.execSQL(sql);
        } catch (Exception e) {}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion != newVersion) {
            /* db.execSQL("ALTER") */
        }
    }

    public String getTableName() {
        return TABLE_NAME;
    }
}
