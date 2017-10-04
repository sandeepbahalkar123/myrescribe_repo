package com.rescribe.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.rescribe.R;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.notification.AppointmentAlarmTask;
import com.rescribe.notification.DosesAlarmTask;
import com.rescribe.notification.InvestigationAlarmTask;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.services.MQTTService;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

/**
 * Created by ganeshshirole on 27/6/17.
 */

public class StartUpBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            // start mqtt Service
            // use this to start and trigger a service
            Intent serviceIntent = new Intent(context, MQTTService.class);
            // potentially add data to the serviceIntent
            serviceIntent.putExtra(MQTTService.IS_MESSAGE, false);
            context.startService(serviceIntent);

            if(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, context).equals(RescribeConstants.YES))
                notificationForMedicine(context);
        }
    }

    private void notificationForMedicine(Context context) {
        String breakFast = "9:17 AM";
        String lunchTime = "9:19 AM";
        String dinnerTime = "9:21 AM";
        String snacksTime = "9:21 AM";

        AppDBHelper appDBHelper = new AppDBHelper(context);
        Cursor cursor = appDBHelper.getPreferences("1");
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                breakFast = cursor.getString(cursor.getColumnIndex(AppDBHelper.BREAKFAST_TIME));
                lunchTime = cursor.getString(cursor.getColumnIndex(AppDBHelper.LUNCH_TIME));
                dinnerTime = cursor.getString(cursor.getColumnIndex(AppDBHelper.DINNER_TIME));
                snacksTime = cursor.getString(cursor.getColumnIndex(AppDBHelper.SNACKS_TIME));
                cursor.moveToNext();
            }
        }
        cursor.close();

        String times[] = {breakFast, lunchTime, dinnerTime,snacksTime};
        String date = CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.DD_MM_YYYY);

        new DosesAlarmTask(context, times, date).run();
        new InvestigationAlarmTask(context, RescribeConstants.INVESTIGATION_NOTIFICATION_TIME, context.getResources().getString(R.string.investigation_msg)).run();
        new AppointmentAlarmTask(context, RescribeConstants.APPOINTMENT_NOTIFICATION_TIME, context.getResources().getString(R.string.appointment_msg)).run();
    }
}