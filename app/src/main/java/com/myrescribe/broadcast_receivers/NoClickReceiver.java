package com.myrescribe.broadcast_receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.myrescribe.notification.AppointmentAlarmTask;
import com.myrescribe.notification.DosesAlarmTask;
import com.myrescribe.notification.InvestigationAlarmTask;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.ui.activities.NotificationActivity;
import com.myrescribe.ui.activities.SplashScreenActivity;
import com.myrescribe.util.MyRescribeConstants;

/**
 * Created by jeetal on 16/5/17.
 */

public class NoClickReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int notificationId = intent.getIntExtra(MyRescribeConstants.NOTIFICATION_ID, 10);
        int investigation_notification_id = intent.getIntExtra(MyRescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_NOTIFICATION_ID, 10);
        int appointment_notification_id = intent.getIntExtra(MyRescribeConstants.APPOINTMENT_NOTIFICATION_ID, 10);
        String loginStatus = MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, context);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (!MyRescribeConstants.BLANK.equalsIgnoreCase(loginStatus)) {
            if (notificationId == DosesAlarmTask.BREAKFAST_NOTIFICATION_ID || notificationId == DosesAlarmTask.LUNCH_NOTIFICATION_ID || notificationId == DosesAlarmTask.DINNER_NOTIFICATION_ID || notificationId == DosesAlarmTask.EVENING_NOTIFICATION_ID) {
                Intent intentNotification = new Intent(context, NotificationActivity.class);
                intentNotification.putExtra(MyRescribeConstants.MEDICINE_SLOT, intent.getStringExtra(MyRescribeConstants.MEDICINE_SLOT));
                intentNotification.putExtra(MyRescribeConstants.NOTIFICATION_DATE, intent.getStringExtra(MyRescribeConstants.NOTIFICATION_DATE));
                intentNotification.putExtra(MyRescribeConstants.NOTIFICATION_TIME, intent.getStringExtra(MyRescribeConstants.NOTIFICATION_TIME));
                intentNotification.putExtra(MyRescribeConstants.MEDICINE_NAME, intent.getBundleExtra(MyRescribeConstants.MEDICINE_NAME));
                intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intentNotification);
                manager.cancel(notificationId);
            } else if (investigation_notification_id == InvestigationAlarmTask.INVESTIGATION_NOTIFICATION_ID) {
                manager.cancel(investigation_notification_id);
            } else if (appointment_notification_id == AppointmentAlarmTask.APPOINTMENT_NOTIFICATION_ID) {
                String action = (String) intent.getExtras().get(MyRescribeConstants.APPOINTMENT_MESSAGE);
                Toast.makeText(context, action + " " + "Not Accepted", Toast.LENGTH_SHORT).show();
                manager.cancel(appointment_notification_id);
            }
        } else {
            Intent intentNotification = new Intent(context, SplashScreenActivity.class);
            intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intentNotification);
        }


    }
}
