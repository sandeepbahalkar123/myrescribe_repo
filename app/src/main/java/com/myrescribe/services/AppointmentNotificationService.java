package com.myrescribe.services;

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
public class AppointmentNotificationService extends Service {

//    static int mNotificationNoTextField = 0;

    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "com.myrescribe";
    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();

    @Override
    public void onCreate() {
        Log.i("NotificationService", "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // If this service was started by out DosesAlarmTask intent then we want to show our notification
        if (intent.getBooleanExtra(INTENT_NOTIFY, false))
            customNotification(intent);

        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void customNotification(Intent intentData) {

        int notification_id = intentData.getIntExtra(MyRescribeConstants.APPOINTMENT_NOTIFICATION_ID, 0);
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(),
                R.layout.appointment_notification_layout);

        Intent mNotifyYesIntent = new Intent(this, YesClickReceiver.class);
        mNotifyYesIntent.putExtra(MyRescribeConstants.APPOINTMENT_NOTIFICATION_ID, notification_id);
        mNotifyYesIntent.putExtra(MyRescribeConstants.APPOINTMENT_TIME, intentData.getStringExtra(MyRescribeConstants.APPOINTMENT_TIME));
        mNotifyYesIntent.putExtra(MyRescribeConstants.APPOINTMENT_MESSAGE, intentData.getStringExtra(MyRescribeConstants.APPOINTMENT_MESSAGE));
        PendingIntent mYesPendingIntent = PendingIntent.getBroadcast(this, notification_id, mNotifyYesIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.notificationLayout, mYesPendingIntent);

        Intent mNotifyNoIntent = new Intent(this, NoClickReceiver.class);
        mNotifyNoIntent.putExtra(MyRescribeConstants.APPOINTMENT_NOTIFICATION_ID, notification_id);
        mNotifyNoIntent.putExtra(MyRescribeConstants.APPOINTMENT_TIME, intentData.getStringExtra(MyRescribeConstants.APPOINTMENT_TIME));
        mNotifyNoIntent.putExtra(MyRescribeConstants.APPOINTMENT_MESSAGE, intentData.getStringExtra(MyRescribeConstants.APPOINTMENT_MESSAGE));
        PendingIntent mNoPendingIntent = PendingIntent.getBroadcast(this, notification_id, mNotifyNoIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.buttonYes, mNoPendingIntent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.logosmall)
                // Set Ticker Message
                .setTicker(getString(R.string.appointment))
                // Dismiss Notification
                .setAutoCancel(true)
                // Set RemoteViews into Notification
                .setContent(mRemoteViews);

        mRemoteViews.setTextViewText(R.id.showMedicineName, getResources().getString(R.string.appointment));
        mRemoteViews.setTextViewText(R.id.questionText, intentData.getStringExtra(MyRescribeConstants.APPOINTMENT_MESSAGE));
        mRemoteViews.setTextViewText(R.id.timeText, intentData.getStringExtra(MyRescribeConstants.APPOINTMENT_TIME));
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(notification_id, builder.build());

        stopSelf();
    }

    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {
        AppointmentNotificationService getService() {
            return AppointmentNotificationService.this;
        }
    }

}