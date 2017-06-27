package com.myrescribe.broadcast_receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.myrescribe.util.MyRescribeConstants;

/**
 * Created by jeetal on 16/5/17.
 */

public class AppointmentNotificationNoClickReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = (String) intent.getExtras().get(MyRescribeConstants.APPOINTMENT_MESSAGE);
        int notificationId = intent.getIntExtra("notificationId", 0);

        Toast.makeText(context,action + " " + "Not Accepted", Toast.LENGTH_SHORT).show();

        // if you want cancel notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
    }
}
