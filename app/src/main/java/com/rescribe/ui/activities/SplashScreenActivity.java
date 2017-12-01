package com.rescribe.ui.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import com.rescribe.R;
import com.rescribe.broadcast_receivers.SnoozeAlarmNotificationReceiver;
import com.rescribe.notification.MQTTServiceAlarmTask;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.RescribeConstants;

public class SplashScreenActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mContext = SplashScreenActivity.this;
        MQTTServiceAlarmTask.cancelAlarm(mContext);
        new MQTTServiceAlarmTask(mContext).run();
        doNext();
    }

    private void doNext() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, mContext).equals(RescribeConstants.YES)) {
                    Intent intentObj = new Intent(SplashScreenActivity.this, HomePageActivity.class);
                    startActivity(intentObj);
                } else {
                    Intent intentObj = new Intent(SplashScreenActivity.this, LoginSignUpActivity.class);
                    startActivity(intentObj);
                }
                finish();
            }
        }, RescribeConstants.TIME_STAMPS.THREE_SECONDS);


        //-----------
        Intent alarm = new Intent(SplashScreenActivity.this, SnoozeAlarmNotificationReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(SplashScreenActivity.this, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if (!alarmRunning) {
            int timeInterval = 1_000; // After every 1 minutes
            PendingIntent pendingIntent = PendingIntent.getBroadcast(SplashScreenActivity.this, 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), timeInterval, pendingIntent);
        }
        //-----------
    }
}
