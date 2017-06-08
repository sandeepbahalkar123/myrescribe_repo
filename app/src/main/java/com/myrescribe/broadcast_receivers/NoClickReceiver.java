package com.myrescribe.broadcast_receivers;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.myrescribe.ui.activities.NotificationActivity;
import com.myrescribe.util.MyRescribeConstants;

/**
 * Created by jeetal on 16/5/17.
 */

public class NoClickReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra("notificationId", 0);
//        Toast.makeText(context,action + " " + "Dose Rejected", Toast.LENGTH_SHORT).show();
        // if you want cancel notification

        Intent intentNotification = new Intent(context, NotificationActivity.class);
        intentNotification.putExtra(MyRescribeConstants.MEDICINE_SLOT, intent.getStringExtra(MyRescribeConstants.MEDICINE_SLOT));
        intentNotification.putExtra(MyRescribeConstants.DATE, intent.getStringExtra(MyRescribeConstants.DATE));
        intentNotification.putExtra(MyRescribeConstants.TIME, intent.getStringExtra(MyRescribeConstants.TIME));
        intentNotification.putExtra(MyRescribeConstants.MEDICINE_NAME, intent.getStringExtra(MyRescribeConstants.MEDICINE_NAME));
        intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentNotification);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);

    }
}
