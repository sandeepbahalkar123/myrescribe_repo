package com.rescribe.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.rescribe.services.NotificationService;
import com.rescribe.util.CommonMethods;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

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

    public static final int BREAKFAST_NOTIFICATION_ID = 999999990;
    public static final int LUNCH_NOTIFICATION_ID = 999999991;
    public static final int DINNER_NOTIFICATION_ID = 999999992;
    public static final int EVENING_NOTIFICATION_ID = 999999993;
    private static final String TAG = "DOSES_ALARM";

    // The time selected for the alarm
    private final String time[];

    // The android system alarm manager
    private final AlarmManager am;
    // Your context to retrieve the alarm manager from
    private final Context context;

    public DosesAlarmTask(Context context, String... time) {
        this.context = context;
        this.am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        this.time = time;
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
        setAlarm(time);
    }

    private void setAlarm(String... time) {
        Intent intent = new Intent(context, NotificationService.class);

        PendingIntent pendingIntent0 = PendingIntent.getService(context, BREAKFAST_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, getCalendar(time[0]).getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent0);

        PendingIntent pendingIntent1 = PendingIntent.getService(context, LUNCH_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, getCalendar(time[1]).getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent1);

        PendingIntent pendingIntent2 = PendingIntent.getService(context, DINNER_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, getCalendar(time[2]).getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent2);

        PendingIntent pendingIntent3 = PendingIntent.getService(context, EVENING_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, getCalendar(time[3]).getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent3);
    }
}
