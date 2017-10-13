package com.rescribe.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.rescribe.interfaces.OTPListener;
import com.rescribe.services.SnoozeAlarmPrescriptionService;
import com.rescribe.util.CommonMethods;


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