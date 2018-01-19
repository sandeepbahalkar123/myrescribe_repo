package com.rescribe.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.util.CommonMethods;

import java.util.Calendar;


/**
 * PRESCRIPTIONS NOTIFICATION SERVICE
 * This service is started when an Alarm has been raised
 * <p>
 * We pop a notification into the status bar for the user to click on
 * When the user clicks the notification a new activity is opened
 *
 * @author paul.blundell
 */
public class DeleteUnreadNotificationService extends Service {

    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Calendar instance = Calendar.getInstance();
        int hour24 = instance.get(Calendar.HOUR_OF_DAY);

        if (hour24 >= 0 && hour24 <= 7) {

            // Clear all notification
            NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nMgr.cancelAll();

            AppDBHelper appDBHelper = new AppDBHelper(this);
            int deleted = appDBHelper.deleteUnreadReceivedNotificationMessage();
            CommonMethods.Log("ALARM", deleted + " Deleted Unread Notification at 12:00 am");
        }

        stopSelf();

        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {
        DeleteUnreadNotificationService getService() {
            return DeleteUnreadNotificationService.this;
        }
    }

}