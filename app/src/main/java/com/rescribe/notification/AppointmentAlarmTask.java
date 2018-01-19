package com.rescribe.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.rescribe.services.AppointmentNotificationService;
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
public class AppointmentAlarmTask implements Runnable {
    public static final int APPOINTMENT_NOTIFICATION_ID = 5;
    private static final String TAG = "APPOINTMENT_ALARM";

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

        time = CommonMethods.getFormattedDate(time, "hh:mm a", "HH:mm");

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
        setAlarm(time, msg);
    }

    private void setAlarm(String time, String msg) {

        Intent intent = new Intent(context, AppointmentNotificationService.class);
        intent.putExtra(AppointmentNotificationService.INTENT_NOTIFY, true);
        intent.putExtra(RescribeConstants.APPOINTMENT_TIME, time);
        intent.putExtra(RescribeConstants.APPOINTMENT_MESSAGE, msg);
        intent.putExtra(RescribeConstants.APPOINTMENT_NOTIFICATION_ID, APPOINTMENT_NOTIFICATION_ID);

        PendingIntent pendingIntent = PendingIntent.getService(context, APPOINTMENT_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, getCalendar(time).getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
