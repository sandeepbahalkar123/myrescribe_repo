package com.rescribe.services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.broadcast_receivers.ClickOnCheckBoxOfNotificationReceiver;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.investigation.InvestigationHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.investigation.Images;
import com.rescribe.model.investigation.InvestigationData;
import com.rescribe.model.investigation.InvestigationListModel;
import com.rescribe.notification.InvestigationAlarmTask;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;


/**
 * This service is started when an Alarm has been raised
 * <p>
 * We pop a notification into the status bar for the user to click on
 * When the user clicks the notification a new activity is opened
 *
 * @author paul.blundell
 */
public class InvestigationNotificationService extends Service implements HelperResponse {

//    static int mNotificationNoTextField = 0;

    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "com.rescribe";
    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();
    private int notification_id;
    private AppDBHelper appDBHelper;
    private Intent intent;

    @Override
    public void onCreate() {
        Log.i("NotificationService", "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        this.intent = intent;

        if (RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, this).equals(RescribeConstants.YES)) {
            notification_id = intent.getIntExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_NOTIFICATION_ID, 0);
            // If this service was started by out DosesAlarmTask intent then we want to show our notification
            InvestigationHelper investigationHelper;
            if (intent.getBooleanExtra(INTENT_NOTIFY, false)) {
                appDBHelper = new AppDBHelper(this);
                investigationHelper = new InvestigationHelper(this);
                investigationHelper.getInvestigationList(false);
            } else {
                PendingIntent mAlarmPendingIntent = PendingIntent.getActivity(this, notification_id, intent, flags);
                AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                aManager.cancel(mAlarmPendingIntent);
            }
        } else stopSelf();

        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }

    private void checkAllUploaded() {
        String drName = "";
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
            if (cursor.getCount() != notUploaded && cursor.getCount() > 0) {
                message = "Have you done the " + docs + " investigation advised by " + drName + "?";
            } else if (cursor.getCount() > 0) {
                message = "Have you done the investigations advised by " + drName + "?";
//                message = intentData.getStringExtra(RescribeConstants.INVESTIGATION_MESSAGE);
            }

            if (!drName.equals("")) {
                intent.putExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_MESSAGE, message);
                customNotification();
            }
        }
        cursor.close();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void customNotification() {

        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(),
                R.layout.investigation_notification_layout);

        Intent mNotifyYesIntent = new Intent(this, ClickOnCheckBoxOfNotificationReceiver.class);
        mNotifyYesIntent.putExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_NOTIFICATION_ID, notification_id);
        mNotifyYesIntent.putExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_TIME, intent.getStringExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_TIME));
        PendingIntent mYesPendingIntent = PendingIntent.getBroadcast(this, notification_id, mNotifyYesIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.notificationLayout, mYesPendingIntent);

        /*Intent mNotifyNoIntent = new Intent(this, ClickOnNotificationReceiver.class);
        mNotifyNoIntent.putExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_NOTIFICATION_ID, notification_id);
        PendingIntent mNoPendingIntent = PendingIntent.getBroadcast(this, notification_id, mNotifyNoIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.notificationLayout, mNoPendingIntent);*/

        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.logosmall)
                // Set Ticker Message
                .setTicker(getString(R.string.investigation))
                // Dismiss Notification
                .setAutoCancel(true)
                // Set RemoteViews into Notification
                .setContent(mRemoteViews)
                .setStyle(new android.support.v7.app.NotificationCompat.DecoratedCustomViewStyle());

        mRemoteViews.setTextViewText(R.id.showMedicineName, getResources().getString(R.string.investigation));
        mRemoteViews.setTextViewText(R.id.questionText, intent.getStringExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_MESSAGE));
        mRemoteViews.setTextViewText(R.id.timeText, intent.getStringExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_TIME));
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(notification_id, builder.build());

        stopSelf();
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (customResponse instanceof InvestigationListModel) {

            InvestigationListModel investigationListModel = (InvestigationListModel) customResponse;

            ArrayList<InvestigationData> investigation = investigationListModel.getData();

            if (investigation.size() > 0) {

                for (InvestigationData dataObject : investigation) {
                    Images images = new Images();
                    images.setImageArray(dataObject.getPhotos());
                    appDBHelper.insertInvestigationData(dataObject.getId(), dataObject.getTitle(), dataObject.getInvestigationKey(), dataObject.getDoctorName(), dataObject.getOpdId(), dataObject.isUploaded(), new Gson().toJson(images));
                }

                checkAllUploaded();

            }
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {

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