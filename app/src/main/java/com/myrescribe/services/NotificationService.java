package com.myrescribe.services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.myrescribe.R;
import com.myrescribe.broadcast_receivers.NoClickReceiver;
import com.myrescribe.broadcast_receivers.YesClickReceiver;
import com.myrescribe.util.MyRescribeConstants;


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
    public static final String INTENT_NOTIFY = "com.myrescribe";
    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();
    private int notification_id;

    @Override
    public void onCreate() {
        Log.i("NotificationService", "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

         notification_id = intent.getIntExtra(MyRescribeConstants.NOTIFICATION_ID, 0);

        // If this service was started by out DosesAlarmTask intent then we want to show our notification
        if (intent.getBooleanExtra(INTENT_NOTIFY, false))
            customNotification(intent);
        else {
            PendingIntent mAlarmPendingIntent = PendingIntent.getActivity(this, notification_id, intent, flags);
            AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            aManager.cancel(mAlarmPendingIntent);
        }

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

        Intent mNotifyYesIntent = new Intent(this.getApplicationContext(), YesClickReceiver.class);
        mNotifyYesIntent.putExtra(MyRescribeConstants.MEDICINE_SLOT, intentData.getStringExtra(MyRescribeConstants.MEDICINE_SLOT));
        mNotifyYesIntent.putExtra(MyRescribeConstants.NOTIFICATION_ID, notification_id);
        PendingIntent mYesPendingIntent = PendingIntent.getBroadcast(this, notification_id, mNotifyYesIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.buttonYes, mYesPendingIntent);

        Intent mNotifyNoIntent = new Intent(this, NoClickReceiver.class);
        mNotifyNoIntent.putExtra(MyRescribeConstants.MEDICINE_SLOT, intentData.getStringExtra(MyRescribeConstants.MEDICINE_SLOT));
        mNotifyNoIntent.putExtra(MyRescribeConstants.NOTIFICATION_DATE, intentData.getStringExtra(MyRescribeConstants.NOTIFICATION_DATE));
        mNotifyNoIntent.putExtra(MyRescribeConstants.NOTIFICATION_TIME, intentData.getStringExtra(MyRescribeConstants.NOTIFICATION_TIME));
        mNotifyNoIntent.putExtra(MyRescribeConstants.MEDICINE_NAME, intentData.getBundleExtra(MyRescribeConstants.MEDICINE_NAME));
        mNotifyNoIntent.putExtra(MyRescribeConstants.NOTIFICATION_ID, notification_id);
        PendingIntent mNoPendingIntent = PendingIntent.getBroadcast(this, notification_id, mNotifyNoIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.notificationLayout, mNoPendingIntent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.logosmall)
                // Set Ticker Message
                .setTicker(getString(R.string.customnotificationticker))
                // Dismiss Notification
                .setAutoCancel(true)
                // Set RemoteViews into Notification
                .setContent(mRemoteViews);

        mRemoteViews.setTextViewText(R.id.showMedicineName, intentData.getStringExtra(MyRescribeConstants.MEDICINE_SLOT));
        mRemoteViews.setTextViewText(R.id.questionText, getText(R.string.taken_medicine));
        mRemoteViews.setTextViewText(R.id.timeText, intentData.getStringExtra(MyRescribeConstants.NOTIFICATION_TIME));
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