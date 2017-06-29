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
import com.myrescribe.broadcast_receivers.NotificationNoClickReceiver;
import com.myrescribe.broadcast_receivers.NotificationYesClickReceiver;
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

    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {
        NotificationService getService() {
            return NotificationService.this;
        }
    }

    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "com.myrescribe";

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

    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();

    public void customNotification(Intent intentData) {
        int NOTIFICATION_ID = (int) System.currentTimeMillis();
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(),
                R.layout.notification_layout);

        Intent mNotifyYesIntent = new Intent(this, NotificationYesClickReceiver.class);
        mNotifyYesIntent.putExtra(MyRescribeConstants.MEDICINE_SLOT, intentData.getStringExtra(MyRescribeConstants.MEDICINE_SLOT));
        mNotifyYesIntent.putExtra("notificationId", NOTIFICATION_ID);
        PendingIntent mYesPendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, mNotifyYesIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.buttonYes, mYesPendingIntent);

        Intent mNotifyNoIntent = new Intent(this, NotificationNoClickReceiver.class);
        mNotifyNoIntent.putExtra(MyRescribeConstants.MEDICINE_SLOT, intentData.getStringExtra(MyRescribeConstants.MEDICINE_SLOT));
        mNotifyNoIntent.putExtra(MyRescribeConstants.DATE, intentData.getStringExtra(MyRescribeConstants.DATE));
        mNotifyNoIntent.putExtra(MyRescribeConstants.TIME, intentData.getStringExtra(MyRescribeConstants.TIME));
        mNotifyNoIntent.putExtra(MyRescribeConstants.MEDICINE_NAME, intentData.getBundleExtra(MyRescribeConstants.MEDICINE_NAME));
        mNotifyNoIntent.putExtra("notificationId", NOTIFICATION_ID);
        PendingIntent mNoPendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, mNotifyNoIntent, 0);
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
        mRemoteViews.setTextViewText(R.id.questionText,getText(R.string.taken_medicine));
        mRemoteViews.setTextViewText(R.id.timeText, intentData.getStringExtra(MyRescribeConstants.TIME));
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(NOTIFICATION_ID, builder.build());

        stopSelf();
    }

}