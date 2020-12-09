package com.graduationwork.tracediary.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

public class SharedPreference {
    /* 정보 저장하기 : Overloading */
    /* 위치 값(위도, 경도) 저장하기 */
    public static void putShared(Context context, String _date, String _time, String _place, Location _location) {

        /* 파일 이름은 Alarm_set, MODE_PRIVATE : app내에서만 사용 */
        SharedPreferences info = context.getSharedPreferences("TraceDiary_set", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = info.edit();      /* Editor 설정 */

        /* 정보 등록 */
        editor.putString("Location_Date", _date);
        editor.putString("Location_Time", _time);
        editor.putString("Location_Place", _place);
        editor.putLong("Location_Latitude", Double.doubleToLongBits(_location.getLatitude()));      /* Double형을 저장할 수 없음 */
        editor.putLong("Location_Longitude", Double.doubleToLongBits(_location.getLongitude()));
        editor.putFloat("Location_Accuracy", _location.getAccuracy());

        /* xml에 저장 */
        editor.commit();
    }

    /* 위치 값에 대한 반경 : 사용자 설정 */
    public static void putShared(Context context, float _radius) {
        SharedPreferences info = context.getSharedPreferences("TraceDiary_set", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = info.edit();

        editor.putFloat("Location_Radius", _radius);

        editor.commit();
    }

    /* 알람 상태 저장 : 사용자 설정  */
    public static void putShared(Context context, boolean alarm_OnOFF) {

        /* 파일 이름은 Alarm_set, MODE_PRIVATE : app내에서만 사용 */
        SharedPreferences info = context.getSharedPreferences("TraceDiary_set", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = info.edit();      /* Editor 설정 */

        /* 정보 등록 */
        editor.putBoolean("Alarm_Check", alarm_OnOFF);


        editor.commit();
    }

    /* 애플리케이션을 처음 사용하는가 확인  */
    public static void putShared(Context context) {

        SharedPreferences info = context.getSharedPreferences("TraceDiary_set", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = info.edit();      /* Editor 설정 */

        editor.putBoolean("App_use", true);

        editor.commit();
    }


    /* number  */
    public static void putShared(Context context, int num) {

        /* 파일 이름은 Alarm_set, MODE_PRIVATE : app내에서만 사용 */
        SharedPreferences info = context.getSharedPreferences("TraceDiary_set", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = info.edit();      /* Editor 설정 */

        /* 정보 등록 */
        editor.putInt("NUMBER", num);

        /* xml에 저장 */
        editor.commit();
    }

    /* Overloading을 안한 이유 : 저장 형식과 return 형식이 정해져 있어서 하나의 함수로 복수의 값을 보내기 힘듬 */
    public static String getSharedDate(Context context) {
        SharedPreferences info = context.getSharedPreferences("TraceDiary_set", context.MODE_PRIVATE);
        /* "문자열"과 같은 문자열이 있으면 값을 가져오고, 아닌 경우 default값 */
        return info.getString("Location_Date", null);
    }

    public static String getSharedTime(Context context) {
        SharedPreferences info = context.getSharedPreferences("TraceDiary_set", context.MODE_PRIVATE);
        return info.getString("Location_Time", null);
    }

    public static String getSharedPlace(Context context) {
        SharedPreferences info = context.getSharedPreferences("TraceDiary_set", context.MODE_PRIVATE);
        return info.getString("Location_Place", null);
    }

    public static long getSharedLatitude(Context context) {
        SharedPreferences info = context.getSharedPreferences("TraceDiary_set", context.MODE_PRIVATE);
        return info.getLong("Location_Latitude", 0L);
    }

    public static long getSharedLongitude(Context context) {
        SharedPreferences info = context.getSharedPreferences("TraceDiary_set", context.MODE_PRIVATE);
        return info.getLong("Location_Longitude", 0L);
    }

    public static float getSharedAccuracy(Context context) {
        SharedPreferences info = context.getSharedPreferences("TraceDiary_set", context.MODE_PRIVATE);
        return info.getFloat("Location_Accuracy", 0.0f);
    }

    public static float getSharedRadius(Context context) {
        SharedPreferences info = context.getSharedPreferences("TraceDiary_set", context.MODE_PRIVATE);
        return info.getFloat("Location_Radius", 100.0f);
    }

    public static boolean getSharedAlarmSet(Context context) {
        SharedPreferences info = context.getSharedPreferences("TraceDiary_set", context.MODE_PRIVATE);
        return info.getBoolean("Alarm_Check", false);
    }

    public static boolean getSharedUse(Context context) {
        SharedPreferences info = context.getSharedPreferences("TraceDiary_set", context.MODE_PRIVATE);
        return info.getBoolean("App_use", false);
    }

    /* 확인용 */
    public static int getSharedNum(Context context) {
        SharedPreferences info = context.getSharedPreferences("TraceDiary_set", context.MODE_PRIVATE);
        return info.getInt("NUMBER", 0);
    }
}

