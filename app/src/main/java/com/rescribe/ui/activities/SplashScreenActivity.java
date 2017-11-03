package com.rescribe.ui.activities;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import com.rescribe.R;
import com.rescribe.broadcast_receivers.SnoozeAlarmNotificationReceiver;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.services.MQTTService;
import com.rescribe.util.RescribeConstants;

public class SplashScreenActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // start mqtt Service
        // use this to start and trigger a service
        Intent serviceIntent = new Intent(this, MQTTService.class);
        // potentially add data to the serviceIntent
        serviceIntent.putExtra(MQTTService.IS_MESSAGE, false);
        startService(serviceIntent);

        mContext = SplashScreenActivity.this;
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
        if (alarmRunning == false) {
            int timeInterval = 1000 * 1; // After every 1 minutes
            PendingIntent pendingIntent = PendingIntent.getBroadcast(SplashScreenActivity.this, 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), timeInterval, pendingIntent);
        }
        //-----------
    }
}
