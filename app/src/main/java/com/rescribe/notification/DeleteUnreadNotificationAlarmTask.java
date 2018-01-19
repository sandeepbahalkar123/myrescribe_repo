package com.rescribe.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.rescribe.services.DeleteUnreadNotificationService;
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
public class DeleteUnreadNotificationAlarmTask implements Runnable {

    private static final int DELETE_UNREAD_NOTIFICATION_ID = 999999910;
    // The android system alarm manager
    private final AlarmManager am;
    // Your context to retrieve the alarm manager from
    private final Context context;

    public DeleteUnreadNotificationAlarmTask(Context context) {
        this.context = context;
        this.am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
    }

    private Calendar getCalendar() {

        String time = CommonMethods.getFormattedDate("12:00 am", "hh:mm a", "HH:mm");

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
        setAlarm();
    }

    private void setAlarm() {
        Intent intent = new Intent(context, DeleteUnreadNotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, DELETE_UNREAD_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, getCalendar().getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
