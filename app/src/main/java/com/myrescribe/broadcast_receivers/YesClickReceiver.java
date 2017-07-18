package com.myrescribe.broadcast_receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.myrescribe.ui.activities.AppoinmentActivity;
import com.myrescribe.ui.activities.InvestigationActivity;
import com.myrescribe.util.MyRescribeConstants;

/**
 * Created by jeetal on 16/5/17.
 */
public class YesClickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int notificationId = intent.getIntExtra(MyRescribeConstants.NOTIFICATION_ID, 10);
        int investigation_notification_id = intent.getIntExtra(MyRescribeConstants.INVESTIGATION_NOTIFICATION_ID, 10);
        int appointment_notification_id = intent.getIntExtra(MyRescribeConstants.APPOINTMENT_NOTIFICATION_ID, 10);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationId == 0 || notificationId == 1 || notificationId == 2 || notificationId == 3) {
            String action = (String) intent.getExtras().get(MyRescribeConstants.MEDICINE_SLOT);
            Toast.makeText(context, action + " " + notificationId + " " + "Dose Accepted", Toast.LENGTH_SHORT).show();
            manager.cancel(notificationId);
        } else if (investigation_notification_id == 4) {
            Intent intentNotification = new Intent(context, InvestigationActivity.class);
            intentNotification.putExtra(MyRescribeConstants.INVESTIGATION_TIME, intent.getStringExtra(MyRescribeConstants.INVESTIGATION_TIME));
            intentNotification.putExtra(MyRescribeConstants.INVESTIGATION_MESSAGE, intent.getBundleExtra(MyRescribeConstants.INVESTIGATION_MESSAGE));
            intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intentNotification);
            manager.cancel(investigation_notification_id);
        } else if (appointment_notification_id == 5) {
            Intent intentNotification = new Intent(context, AppoinmentActivity.class);
            intentNotification.putExtra(MyRescribeConstants.APPOINTMENT_TIME, intent.getStringExtra(MyRescribeConstants.APPOINTMENT_TIME));
            intentNotification.putExtra(MyRescribeConstants.APPOINTMENT_MESSAGE, intent.getBundleExtra(MyRescribeConstants.APPOINTMENT_MESSAGE));
            intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intentNotification);
            manager.cancel(appointment_notification_id);
        }

    }
}
