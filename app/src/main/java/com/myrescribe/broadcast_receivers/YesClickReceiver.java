package com.myrescribe.broadcast_receivers;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by jeetal on 16/5/17.
 */
public class YesClickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = (String) intent.getExtras().get("action");
        int notificationId = intent.getIntExtra("notificationId", 0);
        Toast.makeText(context,action + " " + "Dose Accepted", Toast.LENGTH_SHORT).show();
        // if you want cancel notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);


    }
}
