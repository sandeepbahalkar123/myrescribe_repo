package com.rescribe.services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.rescribe.R;
import com.rescribe.broadcast_receivers.ClickOnNotificationReceiver;
import com.rescribe.broadcast_receivers.ClickOnCheckBoxOfNotificationReceiver;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.RescribeConstants;


/**
 * This service is started when an Alarm has been raised
 * <p>
 * We pop a notification into the status bar for the user to click on
 * When the user clicks the notification a new activity is opened
 *
 * @author paul.blundell
 */
public class NotificationService extends Service {

//    static int mNotificationNoTextField = 0;

    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "com.rescribe";
    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();
    private int notification_id;

    @Override
    public void onCreate() {
        Log.i("NotificationService", "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, this).equals(RescribeConstants.YES)) {

            notification_id = intent.getIntExtra(RescribeConstants.NOTIFICATION_ID, 0);

            // If this service was started by out DosesAlarmTask intent then we want to show our notification
            if (intent.getBooleanExtra(INTENT_NOTIFY, false))
                customNotification(intent);
            else {
                PendingIntent mAlarmPendingIntent = PendingIntent.getActivity(this, notification_id, intent, flags);
                AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                aManager.cancel(mAlarmPendingIntent);
            }

        } else stopSelf();

        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void customNotification(Intent intentData) {
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(),
                R.layout.notification_layout);

        Intent mNotifyYesIntent = new Intent(this.getApplicationContext(), ClickOnCheckBoxOfNotificationReceiver.class);
        mNotifyYesIntent.putExtra(RescribeConstants.MEDICINE_SLOT, intentData.getStringExtra(RescribeConstants.MEDICINE_SLOT));
        mNotifyYesIntent.putExtra(RescribeConstants.NOTIFICATION_ID, notification_id);
        PendingIntent mYesPendingIntent = PendingIntent.getBroadcast(this, notification_id, mNotifyYesIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.buttonYes, mYesPendingIntent);

        Intent mNotifyNoIntent = new Intent(this, ClickOnNotificationReceiver.class);
        mNotifyNoIntent.putExtra(RescribeConstants.MEDICINE_SLOT, intentData.getStringExtra(RescribeConstants.MEDICINE_SLOT));
        mNotifyNoIntent.putExtra(RescribeConstants.NOTIFICATION_DATE, intentData.getStringExtra(RescribeConstants.NOTIFICATION_DATE));
        mNotifyNoIntent.putExtra(RescribeConstants.NOTIFICATION_TIME, intentData.getStringExtra(RescribeConstants.NOTIFICATION_TIME));
        mNotifyNoIntent.putExtra(RescribeConstants.MEDICINE_NAME, intentData.getBundleExtra(RescribeConstants.MEDICINE_NAME));
        mNotifyNoIntent.putExtra(RescribeConstants.NOTIFICATION_ID, notification_id);
        PendingIntent mNoPendingIntent = PendingIntent.getBroadcast(this, notification_id, mNotifyNoIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.notificationLayout, mNoPendingIntent);

        android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.logosmall)
                // Set Ticker Message
                .setTicker(getString(R.string.customnotificationticker))
                // Dismiss Notification
                .setAutoCancel(true)
                // Set RemoteViews into Notification
                .setContent(mRemoteViews)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle());

        mRemoteViews.setTextViewText(R.id.showMedicineName, intentData.getStringExtra(RescribeConstants.MEDICINE_SLOT));
        mRemoteViews.setTextViewText(R.id.questionText, getText(R.string.taken_medicine));
        mRemoteViews.setTextViewText(R.id.timeText, intentData.getStringExtra(RescribeConstants.NOTIFICATION_TIME));
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(notification_id, builder.build());

        stopSelf();
    }

    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {
        NotificationService getService() {
            return NotificationService.this;
        }
    }

}