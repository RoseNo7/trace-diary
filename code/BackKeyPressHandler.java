package com.graduationwork.tracediary;

import android.app.Activity;
import android.widget.Toast;

public class BackKeyPressHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackKeyPressHandler (Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity,
                "종료하려면 한번 더 누르세요.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
