package com.myrescribe.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.myrescribe.R;
import com.myrescribe.model.Medicine;
import com.myrescribe.services.NotifyService;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Set an alarm for the time passed into the constructor
 * When the alarm is raised it will start the NotifyService
 * <p>
 * This uses the android build in alarm manager *NOTE* if the phone is turned off this alarm will be cancelled
 * <p>
 * This will run on it's own thread.
 *
 * @author paul.blundell
 */
public class AlarmTask implements Runnable {
    // The time selected for the alarm
    private final String time[];
    private final String date;

    // The android system alarm manager
    private final AlarmManager am;
    // Your context to retrieve the alarm manager from
    private final Context context;
//    private final ArrayList<Medicine> medicines;

    public AlarmTask(Context context, String time[], String date/*, ArrayList<Medicine> medicines*/) {
        this.context = context;
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.time = time;
        this.date = date;
//        this.medicines = medicines;
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
        setAlarm(time[0], context.getResources().getString(R.string.breakfast_medication), 0/*, medicines*/);
        setAlarm(time[1], context.getResources().getString(R.string.lunch_medication), 1/*, medicines*/);
        setAlarm(time[2], context.getResources().getString(R.string.dinner_medication), 2/*, medicines*/);
    }

    private void setAlarm(String time, String medicineSlot, int requestCode/*, ArrayList<Medicine> medicines*/){
        Intent intent = new Intent(context, NotifyService.class);
        intent.putExtra(NotifyService.INTENT_NOTIFY, true);
        intent.putExtra(MyRescribeConstants.TIME, time);
        intent.putExtra(MyRescribeConstants.MEDICINE_SLOT, medicineSlot);
        intent.putExtra(MyRescribeConstants.DATE, date);

        Bundle bundle = new Bundle();
//        bundle.putSerializable(MyRescribeConstants.MEDICINE_NAME, medicines);
        intent.putExtra(MyRescribeConstants.MEDICINE_NAME, bundle);

        PendingIntent pendingIntent = PendingIntent.getService(context, requestCode, intent, 0);

        // Sets an alarm - note this alarm will be lost if the phone is turned off and on again
        am.set(AlarmManager.RTC_WAKEUP, getCalendar(time).getTimeInMillis(), pendingIntent);

//        am.setRepeating(AlarmManager.RTC_WAKEUP, getCalendar(time).getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

}
