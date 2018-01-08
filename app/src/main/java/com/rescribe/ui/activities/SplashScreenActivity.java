package com.rescribe.ui.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.broadcast_receivers.SnoozeAlarmNotificationReceiver;
import com.rescribe.model.token.FCMTokenData;
import com.rescribe.notification.MQTTServiceAlarmTask;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.services.fcm.FCMService;
import com.rescribe.util.RescribeConstants;

import static com.rescribe.services.fcm.FCMService.TOKEN_DATA;
import static com.rescribe.services.fcm.FCMService.TOKEN_DATA_ACTION;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "Splash";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mContext = SplashScreenActivity.this;
        MQTTServiceAlarmTask.cancelAlarm(mContext);
        new MQTTServiceAlarmTask(mContext).run();
        doNext();

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
    }

    private void doNext() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, mContext).equals(RescribeConstants.YES)) {
                    Intent intentObj = new Intent(SplashScreenActivity.this, HomePageActivity.class);
                    if (getIntent().getExtras() != null) {
                        String dataText = getIntent().getExtras().getString(FCMService.FCM_BODY);
                        if (dataText != null) {
                            Gson gson = new Gson();
                            FCMTokenData fcmTokenData = gson.fromJson(dataText, FCMTokenData.class);
                            intentObj.putExtra(TOKEN_DATA, fcmTokenData);
                            intentObj.setAction(TOKEN_DATA_ACTION);

                            int preCount = RescribePreferencesManager.getInt(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.TOKEN_ALERT_COUNT, mContext);
                            RescribePreferencesManager.putInt(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.TOKEN_ALERT_COUNT, preCount + 1, mContext);
                        }
                    }
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
