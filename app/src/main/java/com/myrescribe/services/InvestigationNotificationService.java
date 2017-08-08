package com.myrescribe.services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.myrescribe.R;
import com.myrescribe.broadcast_receivers.NoClickReceiver;
import com.myrescribe.broadcast_receivers.YesClickReceiver;
import com.myrescribe.helpers.database.AppDBHelper;
import com.myrescribe.notification.InvestigationAlarmTask;
import com.myrescribe.util.MyRescribeConstants;


/**
 * This service is started when an Alarm has been raised
 * <p>
 * We pop a notification into the status bar for the user to click on
 * When the user clicks the notification a new activity is opened
 *
 * @author paul.blundell
 */
public class InvestigationNotificationService extends Service {

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

        notification_id = intent.getIntExtra(MyRescribeConstants.INVESTIGATION_NOTIFICATION_ID, 0);

        // If this service was started by out DosesAlarmTask intent then we want to show our notification
        if (intent.getBooleanExtra(INTENT_NOTIFY, false))
            checkAllUploaded(intent);
        else {
            PendingIntent mAlarmPendingIntent = PendingIntent.getActivity(this, notification_id, intent, flags);
            AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            aManager.cancel(mAlarmPendingIntent);
        }

        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }

    private void checkAllUploaded(Intent intentData) {
        String drName = "";
        AppDBHelper appDBHelper = new AppDBHelper(this);
        Cursor cursor = appDBHelper.getAllInvestigationData();
        String docs = "";
        int notUploaded = 0;
        int uploaded = 0;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getInt(cursor.getColumnIndex(AppDBHelper.INV_UPLOAD_STATUS)) == 0) {
                    if (docs.equals("")) {
                        docs = docs + cursor.getString(cursor.getColumnIndex(AppDBHelper.INV_NAME));
                        drName = cursor.getString(cursor.getColumnIndex(AppDBHelper.INV_DR_NAME));
                    } else {
                        docs = docs + " | " + cursor.getString(cursor.getColumnIndex(AppDBHelper.INV_NAME));
                    }
                    notUploaded += 1;
                } else uploaded += 1;
                cursor.moveToNext();
            }
        }

        String message = "";
        if (cursor.getCount() == uploaded && cursor.getCount() > 0) {
            // cancel notification
            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, InvestigationNotificationService.class);
            PendingIntent pendingIntent = PendingIntent.getService(this, InvestigationAlarmTask.INVESTIGATION_NOTIFICATION_ID, intent, 0);
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
            }
        } else {
            if (cursor.getCount() == notUploaded) {
                message = intentData.getStringExtra(MyRescribeConstants.INVESTIGATION_MESSAGE);
            } else {
                message = "Have you done the" + docs + " investigation advised by " + drName + "?";
            }

            intentData.putExtra(MyRescribeConstants.INVESTIGATION_MESSAGE, message);
            customNotification(intentData);
        }
        cursor.close();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void customNotification(Intent intentData) {

        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(),
                R.layout.investigation_notification_layout);

        Intent mNotifyYesIntent = new Intent(this, YesClickReceiver.class);
        mNotifyYesIntent.putExtra(MyRescribeConstants.INVESTIGATION_NOTIFICATION_ID, notification_id);
        mNotifyYesIntent.putExtra(MyRescribeConstants.INVESTIGATION_TIME, intentData.getStringExtra(MyRescribeConstants.INVESTIGATION_TIME));
        mNotifyYesIntent.putExtra(MyRescribeConstants.INVESTIGATION_MESSAGE, intentData.getStringExtra(MyRescribeConstants.INVESTIGATION_MESSAGE));
        PendingIntent mYesPendingIntent = PendingIntent.getBroadcast(this, notification_id, mNotifyYesIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.buttonYes, mYesPendingIntent);

        Intent mNotifyNoIntent = new Intent(this, NoClickReceiver.class);
      /*  mNotifyNoIntent.putExtra(MyRescribeConstants.INVESTIGATION_MESSAGE, intentData.getStringExtra(MyRescribeConstants.INVESTIGATION_MESSAGE));
        mNotifyNoIntent.putExtra(MyRescribeConstants.TIME, intentData.getStringExtra(MyRescribeConstants.TIME));*/
        mNotifyNoIntent.putExtra(MyRescribeConstants.INVESTIGATION_NOTIFICATION_ID, notification_id);
        PendingIntent mNoPendingIntent = PendingIntent.getBroadcast(this, notification_id, mNotifyNoIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.notificationLayout, mNoPendingIntent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.logosmall)
                // Set Ticker Message
                .setTicker(getString(R.string.investigation))
                // Dismiss Notification
                .setAutoCancel(true)
                // Set RemoteViews into Notification
                .setContent(mRemoteViews);

        mRemoteViews.setTextViewText(R.id.showMedicineName, getResources().getString(R.string.investigation));
        mRemoteViews.setTextViewText(R.id.questionText, intentData.getStringExtra(MyRescribeConstants.INVESTIGATION_MESSAGE));
        mRemoteViews.setTextViewText(R.id.timeText, intentData.getStringExtra(MyRescribeConstants.INVESTIGATION_TIME));
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(notification_id, builder.build());

        stopSelf();
    }

    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {
        InvestigationNotificationService getService() {
            return InvestigationNotificationService.this;
        }
    }

}