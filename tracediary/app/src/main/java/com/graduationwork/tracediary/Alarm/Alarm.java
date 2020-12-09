package com.graduationwork.tracediary.Alarm;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.graduationwork.tracediary.Data.SharedPreference;

import static android.content.Context.ALARM_SERVICE;

public class Alarm {
    public static final long ALARM_TIME = 1000 * 60 * 10;       /* 알람 간격 */

    /* 알람 매니저 변수 */
    public static AlarmManager alarmManager;
    public static PendingIntent pendingIntent;
    public static Intent AlarmIntent;
    Context context;

    public Alarm(Context context) {
        this.context = context;
    }

    /* 알람 매니저 생성 */
    public void setAlarm() {
                /* 설정해두면 계속 돌아가는 것으로써 진행중인지 확인을 안해도 됨 */
        if (SharedPreference.getSharedAlarmSet(context)) {
            AlarmIntent = new Intent(context, AlarmReceiver.class);

            /* PendingIntent.FLAG_UPDATE_CURRENT : PendingIntent가 존재하는 경우, Extra Data를 모두 대체 */
            /* PendingIntent.FLAG_NO_CREATE : 이미 생성된 PendingIntent가 없다면 null, 생성된 인텐트가 있다면 PendingIntent 반환 */
            /* PendingIntent.FLAG_CANCEL_CURRENT : 이전에 생성된 PendingIntent를 제거하고 다시 생성 */
            alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            pendingIntent = PendingIntent.getBroadcast(context, 0, AlarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            /* 버전에 따라 ALarmManager의 성능 보장 못함 : 마쉬멜로우에서부터 DOZE모드가 생김 */
            /* alarmManager.RTC_WAKEUP : UTC로 스케줄링을 하고, 디바이스가 잠들면 깨우고 즉시 전달 */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ALARM_TIME, pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ALARM_TIME, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ALARM_TIME, pendingIntent);
            }
        }
    }

    /* 알람 취소 */
    public void stopAlarm() {
        try {
            pendingIntent = PendingIntent.getBroadcast(context, 0, AlarmIntent, PendingIntent.FLAG_NO_CREATE);

            if (alarmManager != null) {
                if (pendingIntent != null) {
                    alarmManager.cancel(pendingIntent);
                    pendingIntent.cancel();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        alarmManager = null;
        pendingIntent = null;
    }
}
