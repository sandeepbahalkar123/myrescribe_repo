package com.rescribe.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.rescribe.services.AppointmentNotificationService;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.MyRescribeConstants;

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
public class AppointmentAlarmTask implements Runnable {
    public static final int APPOINTMENT_NOTIFICATION_ID = 5;

    // The time selected for the alarm
    private final String time;
    private final String msg;

    // The android system alarm manager
    private final AlarmManager am;
    // Your context to retrieve the alarm manager from
    private final Context context;

    public AppointmentAlarmTask(Context context, String time, String msg) {
        this.context = context;
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.time = time;
        this.msg = msg;
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
        if (time == null || msg == null)
            cancelAlarm(APPOINTMENT_NOTIFICATION_ID);
        else 
        setAlarm(time, msg, APPOINTMENT_NOTIFICATION_ID);
    }

    private void setAlarm(String time, String msg, int requestCode) {
        Intent intent = new Intent(context, AppointmentNotificationService.class);
        intent.putExtra(AppointmentNotificationService.INTENT_NOTIFY, true);
        intent.putExtra(MyRescribeConstants.APPOINTMENT_TIME, time);
        intent.putExtra(MyRescribeConstants.APPOINTMENT_MESSAGE, msg);
        intent.putExtra(MyRescribeConstants.APPOINTMENT_NOTIFICATION_ID, requestCode);

        PendingIntent pendingIntent = PendingIntent.getService(context, requestCode, intent, 0);

        // Sets an alarm - note this alarm will be lost if the phone is turned off and on again
//        am.set(AlarmManager.RTC_WAKEUP, getCalendar(time).getTimeInMillis(), pendingIntent);

        am.setRepeating(AlarmManager.RTC_WAKEUP, getCalendar(time).getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void cancelAlarm(int requestCode) {
        Intent intent = new Intent(context, AppointmentNotificationService.class);
        intent.putExtra(AppointmentNotificationService.INTENT_NOTIFY, false);
        intent.putExtra(MyRescribeConstants.APPOINTMENT_NOTIFICATION_ID, requestCode);
        context.startService(intent);
    }
}
