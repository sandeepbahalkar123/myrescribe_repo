package com.rescribe.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rescribe.services.SnoozeAlarmPrescriptionService;


/**
 * Created by swarajpal on 13-12-2015.
 * BroadcastReceiver OtpReader for receiving and processing the SMS messages.
 */
public class SnoozeAlarmNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //CommonMethods.Log("SnoozeAlarmPrescriptionService  ", "SnoozeAlarmPrescriptionService : AlarmReceiver  called  ");
        Intent background = new Intent(context, SnoozeAlarmPrescriptionService.class);
        context.startService(background);
    }
}