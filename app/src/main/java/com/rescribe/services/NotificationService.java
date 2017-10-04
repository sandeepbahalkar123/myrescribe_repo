package com.rescribe.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.rescribe.R;
import com.rescribe.broadcast_receivers.ClickOnNotificationReceiver;
import com.rescribe.broadcast_receivers.ClickOnCheckBoxOfNotificationReceiver;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.investigation.InvestigationHelper;
import com.rescribe.helpers.notification.NotificationHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.notification.NotificationModel;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.Calendar;


/**
 * This service is started when an Alarm has been raised
 * <p>
 * We pop a notification into the status bar for the user to click on
 * When the user clicks the notification a new activity is opened
 *
 * @author paul.blundell
 */
public class NotificationService extends Service implements HelperResponse {

//    static int mNotificationNoTextField = 0;

    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "com.rescribe";
    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();
    private int notification_id;
    private AppDBHelper appDBHelper;
    private NotificationHelper mNotificationHelper;
    private Intent intent;
    Calendar c = Calendar.getInstance();
    int hour24 = c.get(Calendar.HOUR_OF_DAY);
    int Min = c.get(Calendar.MINUTE);
    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         this.intent = intent;
        if (RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, this).equals(RescribeConstants.YES)) {

            notification_id = intent.getIntExtra(RescribeConstants.NOTIFICATION_ID, 0);

            // If this service was started by out DosesAlarmTask intent then we want to show our notification
            if (intent.getBooleanExtra(INTENT_NOTIFY, false)) {
                appDBHelper = new AppDBHelper(this);
                mNotificationHelper = new NotificationHelper(this);
                mNotificationHelper.doGetNotificationList();
                //customNotification(intent);
            } else {
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

        RingtoneManager ringtoneManager = new RingtoneManager(this.getApplicationContext());
        android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.logosmall)
                // Set Ticker Message
                .setTicker(getString(R.string.customnotificationticker))
                // Dismiss Notification
                .setAutoCancel(true)
                // Set RemoteViews into Notification
                .setContent(mRemoteViews)
                .setVibrate(new long[]{1000, 1000,1000})
                .setSound(ringtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                //   .setPriority(NotificationCompat.PRIORITY_HIGH) //must give priority to High, Max which will considered as heads-up notification
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle());

        mRemoteViews.setTextViewText(R.id.showMedicineName, intentData.getStringExtra(RescribeConstants.MEDICINE_SLOT));
        mRemoteViews.setTextViewText(R.id.questionText, getText(R.string.taken_medicine));
        mRemoteViews.setTextViewText(R.id.timeText, intentData.getStringExtra(RescribeConstants.NOTIFICATION_TIME));
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification build = builder.build();
       // build.flags |= Notification.FLAG_INSISTENT;
        notificationmanager.notify(notification_id, build);

        stopSelf();
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equals(RescribeConstants.TASK_NOTIFICATION)) {
            NotificationModel prescriptionDataReceived = (NotificationModel) customResponse;
            if (prescriptionDataReceived.getNotificationPrescriptionModel().getPresriptionNotification() != null) {
                String slot = CommonMethods.getMealTime(hour24,Min,this);
                if(slot.equals(getString(R.string.break_fast))){
                  if(intent.getStringExtra(RescribeConstants.MEDICINE_SLOT).equals(getString(R.string.breakfast_medication))){
                      customNotification(intent);
                  }
              }else if(slot.equals(getString(R.string.mlunch))){
                  if(intent.getStringExtra(RescribeConstants.MEDICINE_SLOT).equals(getString(R.string.lunch_medication))){
                      customNotification(intent);
                  }
                }else if(slot.equals(getString(R.string.msnacks))){
                  if(intent.getStringExtra(RescribeConstants.MEDICINE_SLOT).equals(getString(R.string.snacks_medication))){
                      customNotification(intent);
                  }
              }else if(slot.equals(getString(R.string.mdinner))){
                  if(intent.getStringExtra(RescribeConstants.MEDICINE_SLOT).equals(getString(R.string.dinner_medication))){
                      customNotification(intent);
                  }
              }



            }

        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        stopSelf();
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        stopSelf();
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
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