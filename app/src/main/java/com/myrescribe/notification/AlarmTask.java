package com.myrescribe.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.myrescribe.services.NotifyService;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.Constants;

import java.util.Calendar;

/**
 * Set an alarm for the date passed into the constructor
 * When the alarm is raised it will start the NotifyService
 * <p>
 * This uses the android build in alarm manager *NOTE* if the phone is turned off this alarm will be cancelled
 * <p>
 * This will run on it's own thread.
 *
 * @author paul.blundell
 */
public class AlarmTask implements Runnable {
    // The date selected for the alarm
    private final String date[];
    // The android system alarm manager
    private final AlarmManager am;
    // Your context to retrieve the alarm manager from
    private final Context context;

    public AlarmTask(Context context, String date[]) {
        this.context = context;
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.date = date;
    }

    private Calendar getCalendar(String time) {

        time = CommonMethods.getFormatedDate(time, "hh:mm a", "HH:mm");

        String[] hour = time.split(":");
        String[] minuite = hour[1].split(" ");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(minuite[0]));
        calendar.set(Calendar.SECOND, 0);

        CommonMethods.Log("AllTimes", Integer.parseInt(hour[0]) + ":" + Integer.parseInt(minuite[0]));

        return calendar;
    }

    @Override
    public void run() {

        setAlarm(date[0], "Breakfast", 0);
        setAlarm(date[1], "Lunch", 1);
        setAlarm(date[2], "Dinner", 2);

    }

    private void setAlarm(String date, String medicineName, int requestCode){
        Intent intent = new Intent(context, NotifyService.class);
        intent.putExtra(NotifyService.INTENT_NOTIFY, true);
        intent.putExtra(Constants.TIME, date);
        intent.putExtra(Constants.MEDICINE_NAME, medicineName);

        PendingIntent pendingIntent = PendingIntent.getService(context, requestCode, intent, 0);

        // Sets an alarm - note this alarm will be lost if the phone is turned off and on again
        am.set(AlarmManager.RTC_WAKEUP, getCalendar(date).getTimeInMillis(), pendingIntent);

        //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (5 * 10000), pendingIntent);
    }

}
