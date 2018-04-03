package com.rescribe.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.broadcast_receivers.ClickOnCheckBoxOfNotificationReceiver;
import com.rescribe.broadcast_receivers.ClickOnNotificationReceiver;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.investigation.InvestigationHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.investigation.InvestigationData;
import com.rescribe.model.investigation.InvestigationListModel;
import com.rescribe.model.investigation.InvestigationNotification;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.rescribe.notification.InvestigationAlarmTask.INVESTIGATION_NOTIFICATION_ID;
import static com.rescribe.util.RescribeConstants.INVESTIGATION_NOTIFICATION_TAG;
import static com.rescribe.util.RescribeConstants.SAMSUNG;


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

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        CommonMethods.Log("ALARM", "InvestigationNotificationService");

        String loginStatus = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, this);
        boolean isNotificationOn = RescribePreferencesManager.getBoolean(getString(R.string.investigation_alert), this);

        if (loginStatus.equals(RescribeConstants.YES) && isNotificationOn) {
            notification_id = intent.getIntExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_NOTIFICATION_ID, INVESTIGATION_NOTIFICATION_ID);
            // If this service was started by out DosesAlarmTask intent then we want to show our notification
            InvestigationHelper investigationHelper = new InvestigationHelper(this, this);
            investigationHelper.getInvestigationList(false);

        } else stopSelf();

        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void customNotification(ArrayList<InvestigationData> value) {

        //-------------

        //        String notificationTimeSlot = intentData.getStringExtra(RescribeConstants.NOTIFICATION_TIME);
        String notificationTime = CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.hh_mm_a);

        InvestigationNotification data = new InvestigationNotification();
        data.setNotifications(value);

        String doctorName;
        if (value.get(0).getDoctorName().toLowerCase().contains("dr."))
            doctorName = value.get(0).getDoctorName();
        else
            doctorName = "Dr. " + value.get(0).getDoctorName();

        String time = CommonMethods.getCurrentDate() + " " + notificationTime;
        String message = getText(R.string.investigation_msg) + doctorName + "?";
        //--------------

        //---- Save notification in db---
        AppDBHelper appDBHelper = AppDBHelper.getInstance(getApplicationContext());
        appDBHelper.insertUnreadReceivedNotificationMessage(String.valueOf(value.get(0).getDrId()), RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT, message, new Gson().toJson(data), time, true);

        //---------
        Intent mNotifyYesIntent = new Intent(this, ClickOnCheckBoxOfNotificationReceiver.class);
        mNotifyYesIntent.putExtra(RescribeConstants.INVESTIGATION_LIST, value);
        mNotifyYesIntent.putExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_NOTIFICATION_ID, notification_id);
        mNotifyYesIntent.putExtra(getString(R.string.unread_notification_update_received), value.get(0).getDrId());

        mNotifyYesIntent.putExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_TIME, notificationTime);
        PendingIntent mYesPendingIntent = PendingIntent.getBroadcast(this, value.get(0).getDrId(), mNotifyYesIntent, 0);

        Intent mNotifyNoIntent = new Intent(this, ClickOnNotificationReceiver.class);
        mNotifyNoIntent.putExtra(RescribeConstants.INVESTIGATION_LIST, value);
        mNotifyNoIntent.putExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_NOTIFICATION_ID, notification_id);
        mNotifyNoIntent.putExtra(getString(R.string.unread_notification_update_received), value.get(0).getDrId());

        PendingIntent mNoPendingIntent = PendingIntent.getBroadcast(this, value.get(0).getDrId(), mNotifyNoIntent, 0);

        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.logosmall)
                // Set Ticker Message
                .setTicker(getString(R.string.investigation))
                // Dismiss Notification
                .setAutoCancel(true);

        // *************************************************************************************************************

        // Collapsed
        
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews mRemoteViewCollapse = new RemoteViews(getPackageName(),
                R.layout.investigation_notification_layout);
        mRemoteViewCollapse.setOnClickPendingIntent(R.id.notificationLayout, mYesPendingIntent);
        mRemoteViewCollapse.setOnClickPendingIntent(R.id.buttonSkip, mNoPendingIntent);
        mRemoteViewCollapse.setTextViewText(R.id.showMedicineName, getResources().getString(R.string.investigation));
        mRemoteViewCollapse.setTextViewText(R.id.questionText, getText(R.string.investigation_msg) + doctorName + "?");
        mRemoteViewCollapse.setTextViewText(R.id.timeText, notificationTime);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP && Build.MANUFACTURER.contains(SAMSUNG)){
            mRemoteViewCollapse.setTextColor(R.id.showMedicineName, Color.WHITE);
            mRemoteViewCollapse.setTextColor(R.id.questionText, Color.WHITE);
            mRemoteViewCollapse.setTextColor(R.id.timeText, Color.WHITE);
        }

        // **************************************************************************************************************

        // Expanded

        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews mRemoteViewExpanded = new RemoteViews(getPackageName(),
                R.layout.investigation_notification_layout_expanded);
        mRemoteViewExpanded.setOnClickPendingIntent(R.id.notificationLayout, mYesPendingIntent);
        mRemoteViewExpanded.setOnClickPendingIntent(R.id.buttonSkip, mNoPendingIntent);
        mRemoteViewExpanded.setTextViewText(R.id.showMedicineName, getResources().getString(R.string.investigation));
        mRemoteViewExpanded.setTextViewText(R.id.questionText, getText(R.string.investigation_msg) + doctorName + "?");
        mRemoteViewExpanded.setTextViewText(R.id.timeText, notificationTime);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP && android.os.Build.MANUFACTURER.contains(SAMSUNG)){
            mRemoteViewExpanded.setTextColor(R.id.showMedicineName, Color.WHITE);
            mRemoteViewExpanded.setTextColor(R.id.questionText, Color.WHITE);
            mRemoteViewExpanded.setTextColor(R.id.timeText, Color.WHITE);
        }

        // *************************************************************************************************************
        
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification = builder.build();
        notification.contentView = mRemoteViewCollapse;
        if (Build.VERSION.SDK_INT >= 16) {
            notification.bigContentView = mRemoteViewExpanded;
        }

        notificationmanager.notify(INVESTIGATION_NOTIFICATION_TAG, value.get(0).getDrId(), notification);

        stopSelf();
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (customResponse instanceof InvestigationListModel) {

            InvestigationListModel investigationListModel = (InvestigationListModel) customResponse;

            HashMap<Integer, ArrayList<InvestigationData>> sortedData = new HashMap<>();

            ArrayList<InvestigationData> investigation = investigationListModel.getInvestigationNotification().getNotifications();

            if (investigation.size() > 0) {
                for (InvestigationData dataObject : investigation) {
                    if (!sortedData.containsKey(dataObject.getDrId()))
                        sortedData.put(dataObject.getDrId(), new ArrayList<InvestigationData>());
                    sortedData.get(dataObject.getDrId()).add(dataObject);
                }
            }

            for (Map.Entry entry : sortedData.entrySet()) {
                ArrayList<InvestigationData> value = (ArrayList<InvestigationData>) entry.getValue();
                customNotification(value);
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