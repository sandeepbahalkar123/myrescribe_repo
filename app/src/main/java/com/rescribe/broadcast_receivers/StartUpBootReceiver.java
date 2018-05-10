package com.rescribe.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.rescribe.R;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.notification.AppointmentAlarmTask;
import com.rescribe.notification.DeleteUnreadNotificationAlarmTask;
import com.rescribe.notification.DosesAlarmTask;
import com.rescribe.notification.InvestigationAlarmTask;
import com.rescribe.notification.MQTTServiceAlarmTask;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

/**
 * Created by ganeshshirole on 27/6/17.
 */

public class StartUpBootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootDevice";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            CommonMethods.Log(TAG, "StartUpBootReceiver");

            if (RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, context).equals(RescribeConstants.YES)) {
                new MQTTServiceAlarmTask(context).run();
                notificationForMedicine(context);
            }
        }
    }

    private void notificationForMedicine(Context context) {
        String breakFast = "8:00 AM";
        String lunchTime = "2:00 PM";
        String dinnerTime = "8:00 PM";
        String snacksTime = "5:00 PM";

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

        String times[] = {breakFast, lunchTime, dinnerTime, snacksTime};

        new DeleteUnreadNotificationAlarmTask(context).run();
        new DosesAlarmTask(context, times).run();
        new InvestigationAlarmTask(context, RescribeConstants.INVESTIGATION_NOTIFICATION_TIME, context.getResources().getString(R.string.investigation_msg)).run();
        new AppointmentAlarmTask(context, RescribeConstants.APPOINTMENT_NOTIFICATION_TIME, context.getResources().getString(R.string.appointment_msg)).run();
    }
}