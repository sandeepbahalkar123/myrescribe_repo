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

import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.broadcast_receivers.SnoozeAlarmNotificationReceiver;
import com.rescribe.helpers.book_appointment.ServicesCardViewImpl;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.token.FCMData;
import com.rescribe.notification.MQTTServiceAlarmTask;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.services.fcm.FCMService;
import com.rescribe.ui.activities.book_appointment.SelectSlotToBookAppointmentBaseActivity;
import com.rescribe.ui.activities.dashboard.UnreadNotificationMessageActivity;
import com.rescribe.util.RescribeConstants;

import static com.rescribe.services.fcm.FCMService.FCM_DATA;
import static com.rescribe.services.fcm.FCMService.FOLLOW_UP_DATA_ACTION;
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
                    if (getIntent().getExtras() != null) {
                        String dataText = getIntent().getExtras().getString(FCMService.FCM_BODY);
                        if (dataText != null) {

                            Gson gson = new Gson();
                            FCMData fcmTokenData = gson.fromJson(dataText, FCMData.class);

                            if (fcmTokenData.getIdentifier().equalsIgnoreCase(FOLLOW_UP_DATA_ACTION)) {
                                Intent intent = new Intent(SplashScreenActivity.this, UnreadNotificationMessageActivity.class);
                                intent.putExtra(FCM_DATA, fcmTokenData);
                                intent.setAction(FOLLOW_UP_DATA_ACTION);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(SplashScreenActivity.this, SelectSlotToBookAppointmentBaseActivity.class);
                                intent.putExtra(FCM_DATA, fcmTokenData);
                                intent.setAction(TOKEN_DATA_ACTION);

                                // call book appointment
                                intent.putExtra(getString(R.string.clicked_item_data_type_value), getString(R.string.chats));
                                intent.putExtra(getString(R.string.toolbarTitle), getString(R.string.book_appointment));

                                DoctorList doctorListData1 = new DoctorList();
                                doctorListData1.setDocId(fcmTokenData.getDocId());
                                doctorListData1.setLocationId(fcmTokenData.getLocationId());
                                ServicesCardViewImpl.setUserSelectedDoctorListDataObject(doctorListData1);

                                startActivity(intent);
                            }

                        } else callDashBoard();
                    } else
                        callDashBoard();
                } else {
                    Intent intentObj = new Intent(SplashScreenActivity.this, LoginSignUpActivity.class);
                    startActivity(intentObj);
                }
                finish();
            }

            private void callDashBoard() {
                Intent intentObj = new Intent(SplashScreenActivity.this, HomePageActivity.class);
                startActivity(intentObj);
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
