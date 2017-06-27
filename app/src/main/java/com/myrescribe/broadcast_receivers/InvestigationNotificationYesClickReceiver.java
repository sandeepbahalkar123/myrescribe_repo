package com.myrescribe.broadcast_receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.myrescribe.ui.activities.InvestigationActivity;
import com.myrescribe.ui.activities.NotificationActivity;
import com.myrescribe.util.MyRescribeConstants;

/**
 * Created by jeetal on 16/5/17.
 */
public class InvestigationNotificationYesClickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra("notificationId", 0);
//        Toast.makeText(context,action + " " + "Dose Rejected", Toast.LENGTH_SHORT).show();
        // if you want cancel notification

        Intent intentNotification = new Intent(context, InvestigationActivity.class);
        intentNotification.putExtra(MyRescribeConstants.TIME, intent.getStringExtra(MyRescribeConstants.TIME));
        intentNotification.putExtra(MyRescribeConstants.INVESTIGATION_MESSAGE, intent.getBundleExtra(MyRescribeConstants.INVESTIGATION_MESSAGE));
        intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(intentNotification);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
    }
}
