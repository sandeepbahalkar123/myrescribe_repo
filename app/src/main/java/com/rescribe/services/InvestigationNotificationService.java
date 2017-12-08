package com.rescribe.services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.rescribe.R;
import com.rescribe.broadcast_receivers.ClickOnCheckBoxOfNotificationReceiver;
import com.rescribe.broadcast_receivers.ClickOnNotificationReceiver;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.investigation.InvestigationHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.investigation.InvestigationData;
import com.rescribe.model.investigation.InvestigationListModel;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.rescribe.notification.InvestigationAlarmTask.INVESTIGATION_NOTIFICATION_ID;


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
    private Intent intent;

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        this.intent = intent;

        String loginStatus = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, this);
        boolean isNotificationOn = RescribePreferencesManager.getBoolean(getString(R.string.investigation_alert), this);

        if (loginStatus.equals(RescribeConstants.YES) && isNotificationOn) {
            notification_id = intent.getIntExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_NOTIFICATION_ID, INVESTIGATION_NOTIFICATION_ID);
            // If this service was started by out DosesAlarmTask intent then we want to show our notification
            InvestigationHelper investigationHelper;
            if (intent.getBooleanExtra(INTENT_NOTIFY, false)) {
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

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void customNotification(ArrayList<InvestigationData> value) {

        int preCount = RescribePreferencesManager.getInt(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT, InvestigationNotificationService.this);
        RescribePreferencesManager.putInt(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT, preCount + 1, InvestigationNotificationService.this);

        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(),
                R.layout.investigation_notification_layout);

        Intent mNotifyYesIntent = new Intent(this, ClickOnCheckBoxOfNotificationReceiver.class);
        mNotifyYesIntent.putExtra(RescribeConstants.INVESTIGATION_LIST, value);
        mNotifyYesIntent.putExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_NOTIFICATION_ID, notification_id);
        mNotifyYesIntent.putExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_TIME, intent.getStringExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_TIME));
        PendingIntent mYesPendingIntent = PendingIntent.getBroadcast(this, value.get(0).getDrId(), mNotifyYesIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.notificationLayout, mYesPendingIntent);

        Intent mNotifyNoIntent = new Intent(this, ClickOnNotificationReceiver.class);
        mNotifyNoIntent.putExtra(RescribeConstants.INVESTIGATION_LIST, value);
        mNotifyNoIntent.putExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_NOTIFICATION_ID, notification_id);
        PendingIntent mNoPendingIntent = PendingIntent.getBroadcast(this, value.get(0).getDrId(), mNotifyNoIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.buttonSkip, mNoPendingIntent);

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
        mRemoteViews.setTextViewText(R.id.questionText, getText(R.string.investigation_msg) + value.get(0).getDoctorName() + "?");
        mRemoteViews.setTextViewText(R.id.timeText, intent.getStringExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_TIME));
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(value.get(0).getDrId(), builder.build());

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