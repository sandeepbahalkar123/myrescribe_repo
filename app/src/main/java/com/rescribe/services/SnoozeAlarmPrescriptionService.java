package com.rescribe.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;

import com.rescribe.R;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.activities.AppointmentAlarmNotify;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SnoozeAlarmPrescriptionService extends Service {
    private Context mContext;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.mContext = this;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*if(!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }*/
        //if(!utilObj.readDataInSharedPreferences(Constants.SharedPreferenceTag.TAG_PREFERENCE_NAME, Context.MODE_PRIVATE, Constants.SharedPreferenceTag.TAG_PREFERENCE_USERNAME_ID).equalsIgnoreCase("")){
        sendDataToServer();
        //}
        return START_STICKY;
    }

    private void sendDataToServer() {
        SharedPreferences sharedPreference = RescribePreferencesManager.getSharedPreference(mContext);
        Map<String, ?> keys = sharedPreference.getAll();

        SimpleDateFormat df = new SimpleDateFormat(RescribeConstants.DATE_PATTERN.DD_MM_YYYY + " " + RescribeConstants.DATE_PATTERN.HH_mm_ss);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            String key = entry.getKey();
            //  if (key.startsWith(getString(R.string.snooze_interval)) && !(key.endsWith("null"))) {
            if (key.startsWith(getString(R.string.snooze_interval))) {
                String value = entry.getValue().toString();
                try {
                    String[] split = key.split("\\|");
                    Date receivedDate = df.parse(value);
                    Calendar receivedCalendar = Calendar.getInstance();
                    receivedCalendar.setTime(receivedDate);
                    int receivedHours = receivedCalendar.get(Calendar.HOUR_OF_DAY);
                    int receivedMinutes = receivedCalendar.get(Calendar.MINUTE);
                //    CommonMethods.Log("SnoozeAlarmPrescriptionService", "service called " + receivedHours + "|" + receivedMinutes + "|| " + hours + "|" + minutes);
                    if (hours == receivedHours && minutes == receivedMinutes) {
                        //----------
                      /*  Intent popup = new Intent(getApplicationContext(), AppointmentAlarmNotify.class);
                        popup.putExtra(RescribeConstants.MEDICINE_SLOT, split[4]);
                        popup.putExtra(RescribeConstants.NOTIFICATION_TIME, split[3]);
                        popup.putExtra(RescribeConstants.NOTIFICATION_ID, "" + split[1]);
                        popup.putExtra(RescribeConstants.TITLE, split[2]);
                        popup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startActivity(popup);*/
                        //----------
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
