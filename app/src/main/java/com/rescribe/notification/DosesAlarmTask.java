package com.rescribe.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rescribe.R;
import com.rescribe.services.NotificationService;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.Calendar;

/**
 * Set an alarm for the time passed into the constructor
 * When the alarm is raised it will start the NotificationService
 * <p>
 * This uses the android build in alarm manager *NOTE* if the phone is turned off this alarm will be cancelled
 * <p>
 * This will run on it's own thread.
 *
 * @author paul.blundell
 */
public class DosesAlarmTask implements Runnable {

    public static final int BREAKFAST_NOTIFICATION_ID = 0;
    public static final int LUNCH_NOTIFICATION_ID = 1;
    public static final int DINNER_NOTIFICATION_ID = 2;
    public static final int EVENING_NOTIFICATION_ID = 3;

    // The time selected for the alarm
    private final String time[];
    private final String date;

    // The android system alarm manager
    private final AlarmManager am;
    // Your context to retrieve the alarm manager from
    private final Context context;

    public DosesAlarmTask(Context context, String time[], String date) {
        this.context = context;
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.time = time;
        this.date = date;
    }

    private Calendar getCalendar(String time) {

        time = CommonMethods.getFormatedDate(time, "hh:mm a", "HH:mm");

        String[] hour = time.split(":");
        String[] minute = hour[1].split(" ");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(minute[0]));
        calendar.set(Calendar.SECOND, 0);

        CommonMethods.Log("AllTimes", Integer.parseInt(hour[0]) + ":" + Integer.parseInt(minute[0]));

        return calendar;
    }

    @Override
    public void run() {
        if (time == null || date == null) {
            cancelAlarm(BREAKFAST_NOTIFICATION_ID);
            cancelAlarm(LUNCH_NOTIFICATION_ID);
            cancelAlarm(DINNER_NOTIFICATION_ID);
            cancelAlarm(EVENING_NOTIFICATION_ID);
        } else {
            setAlarm(time[0], context.getResources().getString(R.string.breakfast_medication), BREAKFAST_NOTIFICATION_ID);
            setAlarm(time[1], context.getResources().getString(R.string.lunch_medication), LUNCH_NOTIFICATION_ID);
            setAlarm(time[2], context.getResources().getString(R.string.dinner_medication), DINNER_NOTIFICATION_ID);
            setAlarm(time[3], context.getResources().getString(R.string.snacks_medication), EVENING_NOTIFICATION_ID);
        }
    }

    private void cancelAlarm(int requestCode) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.putExtra(NotificationService.INTENT_NOTIFY, false);
        intent.putExtra(RescribeConstants.NOTIFICATION_ID, requestCode);
        context.startService(intent);
    }

    private void setAlarm(String time, String medicineSlot, int requestCode) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.putExtra(NotificationService.INTENT_NOTIFY, true);
        intent.putExtra(RescribeConstants.NOTIFICATION_TIME, time);
        intent.putExtra(RescribeConstants.MEDICINE_SLOT, medicineSlot);
        intent.putExtra(RescribeConstants.NOTIFICATION_DATE, date);
        intent.putExtra(RescribeConstants.NOTIFICATION_ID, requestCode);

        Bundle bundle = new Bundle();
        intent.putExtra(RescribeConstants.MEDICINE_NAME, bundle);

        PendingIntent pendingIntent = PendingIntent.getService(context, requestCode, intent, 0);

        // Sets an alarm - note this alarm will be lost if the phone is turned off and on again
//        am.set(AlarmManager.RTC_WAKEUP, getCalendar(time).getTimeInMillis(), pendingIntent);

        am.setRepeating(AlarmManager.RTC_WAKEUP, getCalendar(time).getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
