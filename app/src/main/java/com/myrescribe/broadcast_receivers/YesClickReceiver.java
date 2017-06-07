package com.myrescribe.broadcast_receivers;

import android.app.Activity;
import android.app.Notification;
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
public class YesClickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = (String) intent.getExtras().get("action");
        int notificationId = intent.getIntExtra("notificationId", 0);
//        Toast.makeText(context,action + " " + "Dose Accepted", Toast.LENGTH_SHORT).show();


        Intent intentNotification = new Intent(context, NotificationActivity.class);
        /*intentNotification.putExtra(MyRescribeConstants.MEDICINE_SLOT, intent.getStringExtra(MyRescribeConstants.MEDICINE_SLOT));
        intentNotification.putExtra(MyRescribeConstants.DATE, intent.getStringExtra(MyRescribeConstants.DATE));
        intentNotification.putExtra(MyRescribeConstants.MEDICINE_NAME, intent.getStringExtra(MyRescribeConstants.MEDICINE_NAME));*/

        intentNotification.putExtra(MyRescribeConstants.MEDICINE_SLOT, "Breakfast Medication");
        intentNotification.putExtra(MyRescribeConstants.TIME, "8:00 AM");
        intentNotification.putExtra(MyRescribeConstants.DATE, "7-06-2017");
        intentNotification.putExtra(MyRescribeConstants.MEDICINE_NAME, "");

        context.startActivity(intentNotification);

        // if you want cancel notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);


    }
}
