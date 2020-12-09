package com.graduationwork.tracediary.Alarm;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.graduationwork.tracediary.Data.DBhelper;
import com.graduationwork.tracediary.Data.LocationLogFile;
import com.graduationwork.tracediary.Data.SharedPreference;
import com.graduationwork.tracediary.LocationInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;


public class AlarmService extends Service {
    DBhelper helper;
    SQLiteDatabase db;

    /* GPS 변수 */
    Location location;
    String location_place;

    /* SharedPrefercne 값 변수 */
    String SP_Date;
    String SP_Time;
    String SP_PLACE;
    Double SP_Lati;
    Double SP_Longi;
    Float SP_ACCU;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       /* GPS 위치 값과 장소명을 가져옴 */

        LocationInfo locationInfo = new LocationInfo(this);
       try {
           location = locationInfo.getLocation();
           location_place = locationInfo.reverseGeocoding();
       } catch (Exception e) {
           e.printStackTrace();
       }



        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = sdf.format(date);

        StringTokenizer stringTokenizer = new StringTokenizer(datetime);

        /* 날짜,시간 구분 */
        String getDate = stringTokenizer.nextToken();       /* yyyy-MM-dd */
        String getTime = stringTokenizer.nextToken();       /* HH:mm:ss */

        try {
            if (location != null) {
                /* location에 대한 log파일 생성 : "/"는 구분자로 사용 : 주소에 띄어쓰기가 있기에 */
                LocationLogFile writeFile = new LocationLogFile();
                String Log = getDate + "/" + getTime + "/" + location_place + "/" + location.getLatitude() + "/" + location.getLongitude() + "/" + location.getAccuracy();
                writeFile.writeFile(Log, getDate);

                /* 처음 사용하는 경우 */
                if (SharedPreference.getSharedDate(this) == null) {
                    SharedPreference.putShared(this, getDate, getTime, location_place, location);

                } else {
                    /* 거리 계산 */
                    float distance = locationInfo.getDistance();

                    /* 반경 밖에 있는 경우 */
                    if (distance > SharedPreference.getSharedRadius(this)) {
                        SharedPreference.putShared(this, getDate, getTime, location_place, location);

                    /* 반경 안에 존재 : 시간 계산 / 3000 = 30분 */
                    } else {
                        get_SharedPreferences();

                        if (diffenentTime(getDate, getTime) >= 3000) {
                            insertDB();

                            /* 날이 지난 경우 : 구글 맵에도 보여주기 위해 DB에 넣음 */
                            if (getDate.compareTo(SP_Date) >= 1) {
                                SharedPreference.putShared(this, getDate, getTime, location_place, location);
                                get_SharedPreferences();

                                insertDB();
                             }
                        }
                    }
                }
            }

        } catch (Exception e) {
            if (diffenentTime(getDate, getTime) >= 3000) {
                if (getDate.compareTo(SP_Date) >= 1) {
                    SharedPreference.putShared(this, getDate, getTime, location_place, location);
                    get_SharedPreferences();

                    insertDB();
                }
            }
        } finally {

            if(SharedPreference.getSharedAlarmSet(this)) {
                Alarm alarm = new Alarm(this);
                alarm.setAlarm();
            }

            stopSelf();                                 /* 서비스 종료 */

            return super.onStartCommand(intent, flags, startId);
        }
    }


    /* SharedPreference에서 값 가져오기 */
    public void get_SharedPreferences() {
        SP_Date = SharedPreference.getSharedDate(this);
        SP_Time = SharedPreference.getSharedTime(this);
        SP_PLACE = SharedPreference.getSharedPlace(this);
        SP_Lati = Double.longBitsToDouble(SharedPreference.getSharedLatitude(this));
        SP_Longi = Double.longBitsToDouble(SharedPreference.getSharedLongitude(this));
        SP_ACCU = SharedPreference.getSharedAccuracy(this);
    }

    /* 시간 차이 */
    public int diffenentTime(String currentDate, String currentTime) {
        String curDate = currentDate.replace("-", "");
        String curTime =  currentTime.replace(":", "");

        String preDate = SharedPreference.getSharedDate(this).replace("-", "");
        String preTime =  SharedPreference.getSharedTime(this).replace(":", "");

        int gap_Date = Integer.valueOf(curDate) - Integer.valueOf(preDate);
        int gap_Time = Math.abs(Integer.valueOf(curTime) - Integer.valueOf(preTime));

        /* 같은 날 */
        if (gap_Date == 0) {
            return gap_Time;
        } else {
            return ((gap_Date * 236060) - gap_Time);
        }
    }

    /* Database에 값 넣기 */
    public void insertDB() {
        helper = new DBhelper(this);
        db = helper.getWritableDatabase();
        String table_Name = helper.getTableName();

        if (db != null) {
            try {
                String sql = "INSERT INTO " + table_Name + "(DATE, TIME, PLACE, LATI, LONGI, ACCU) values (?, ?, ?, ?, ?, ?)";
                Object[] params = {SP_Date, SP_Time, SP_PLACE, SP_Lati, SP_Longi, SP_ACCU};

                db.execSQL(sql, params);

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            db.close();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
