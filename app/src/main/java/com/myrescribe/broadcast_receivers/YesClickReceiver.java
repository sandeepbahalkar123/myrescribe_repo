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
        String action = (String) intent.getExtras().get(MyRescribeConstants.MEDICINE_SLOT);
        int notificationId = intent.getIntExtra("notificationId", 0);

        Toast.makeText(context,action + " " + "Dose Accepted", Toast.LENGTH_SHORT).show();

        // if you want cancel notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);


    }
}
