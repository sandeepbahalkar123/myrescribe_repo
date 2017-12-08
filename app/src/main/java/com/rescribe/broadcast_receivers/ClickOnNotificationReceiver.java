package com.rescribe.broadcast_receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.rescribe.R;
import com.rescribe.model.investigation.InvestigationData;
import com.rescribe.notification.AppointmentAlarmTask;
import com.rescribe.notification.DosesAlarmTask;
import com.rescribe.notification.InvestigationAlarmTask;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.activities.InvestigationActivity;
import com.rescribe.ui.activities.NotificationActivity;
import com.rescribe.ui.activities.SplashScreenActivity;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

/**
 * Created by jeetal on 16/5/17.
 */

public class ClickOnNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context mContext, Intent intent) {

        int notificationId = intent.getIntExtra(RescribeConstants.NOTIFICATION_ID, 10);
        int investigation_notification_id = intent.getIntExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_NOTIFICATION_ID, 10);
        int appointment_notification_id = intent.getIntExtra(RescribeConstants.APPOINTMENT_NOTIFICATION_ID, 10);
        String loginStatus = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, mContext);

        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (!RescribeConstants.BLANK.equalsIgnoreCase(loginStatus)) {
            if (notificationId == DosesAlarmTask.BREAKFAST_NOTIFICATION_ID || notificationId == DosesAlarmTask.LUNCH_NOTIFICATION_ID || notificationId == DosesAlarmTask.DINNER_NOTIFICATION_ID || notificationId == DosesAlarmTask.EVENING_NOTIFICATION_ID) {
                Intent intentNotification = new Intent(mContext, NotificationActivity.class);
                intentNotification.putExtra(RescribeConstants.MEDICINE_SLOT, intent.getStringExtra(RescribeConstants.MEDICINE_SLOT));
                intentNotification.putExtra(RescribeConstants.NOTIFICATION_DATE, intent.getStringExtra(RescribeConstants.NOTIFICATION_DATE));
                intentNotification.putExtra(RescribeConstants.NOTIFICATION_TIME, intent.getStringExtra(RescribeConstants.NOTIFICATION_TIME));
                intentNotification.putExtra(RescribeConstants.MEDICINE_NAME, intent.getBundleExtra(RescribeConstants.MEDICINE_NAME));
                intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intentNotification);
                manager.cancel(notificationId);
            } else if (investigation_notification_id == InvestigationAlarmTask.INVESTIGATION_NOTIFICATION_ID) {

                ArrayList<InvestigationData> investigationData = intent.getParcelableArrayListExtra(RescribeConstants.INVESTIGATION_LIST);
                RescribePreferencesManager.putBoolean(mContext.getString(R.string.investigation_alert), false, mContext);
                manager.cancel(investigationData.get(0).getDrId());

            } else if (appointment_notification_id == AppointmentAlarmTask.APPOINTMENT_NOTIFICATION_ID) {
                String action = (String) intent.getExtras().get(RescribeConstants.APPOINTMENT_MESSAGE);
                Toast.makeText(mContext, action + " " + "Not Accepted", Toast.LENGTH_SHORT).show();
                manager.cancel(appointment_notification_id);
            }
        } else {
            Intent intentNotification = new Intent(mContext, SplashScreenActivity.class);
            intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intentNotification);
        }


    }
}
