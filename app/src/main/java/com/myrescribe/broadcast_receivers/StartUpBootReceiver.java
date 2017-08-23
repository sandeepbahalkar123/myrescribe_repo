package com.myrescribe.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.myrescribe.R;
import com.myrescribe.helpers.database.AppDBHelper;
import com.myrescribe.notification.AppointmentAlarmTask;
import com.myrescribe.notification.DosesAlarmTask;
import com.myrescribe.notification.InvestigationAlarmTask;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

/**
 * Created by ganeshshirole on 27/6/17.
 */

public class StartUpBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            if(MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, context).equals(MyRescribeConstants.YES))
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
        String date = CommonMethods.getCurrentTimeStamp(MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY);

        new DosesAlarmTask(context, times, date).run();
        new InvestigationAlarmTask(context, MyRescribeConstants.INVESTIGATION_NOTIFICATION_TIME, context.getResources().getString(R.string.investigation_msg)).run();
        new AppointmentAlarmTask(context, MyRescribeConstants.APPOINTMENT_NOTIFICATION_TIME, context.getResources().getString(R.string.appointment_msg)).run();
    }
}