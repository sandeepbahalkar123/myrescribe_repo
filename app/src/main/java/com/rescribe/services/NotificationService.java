package com.rescribe.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.broadcast_receivers.ClickOnCheckBoxOfNotificationReceiver;
import com.rescribe.broadcast_receivers.ClickOnNotificationReceiver;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.notification.NotificationHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.notification.Medication;
import com.rescribe.model.notification.NotificationData;
import com.rescribe.model.notification.NotificationModel;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.facebook.login.widget.ProfilePictureView.TAG;
import static com.rescribe.notification.DosesAlarmTask.BREAKFAST_NOTIFICATION_ID;
import static com.rescribe.notification.DosesAlarmTask.DINNER_NOTIFICATION_ID;
import static com.rescribe.notification.DosesAlarmTask.EVENING_NOTIFICATION_ID;
import static com.rescribe.notification.DosesAlarmTask.LUNCH_NOTIFICATION_ID;
import static com.rescribe.util.RescribeConstants.MEDICATIONS_NOTIFICATION_TAG;


/**
 * PRESCRIPTIONS NOTIFICATION SERVICE
 * This service is started when an Alarm has been raised
 * <p>
 * We pop a notification into the status bar for the user to click on
 * When the user clicks the notification a new activity is opened
 *
 * @author paul.blundell
 */
public class NotificationService extends Service implements HelperResponse {

    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "com.rescribe";
    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();
    Calendar c = Calendar.getInstance();
    int hour24 = c.get(Calendar.HOUR_OF_DAY);

    NotificationHelper mNotificationHelper;

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        CommonMethods.Log("ALARM", "MedicationNotificationService");

        String loginStatus = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, this);
        boolean isNotificationOn = RescribePreferencesManager.getBoolean(getString(R.string.medication_alert), this);

        if (loginStatus.equals(RescribeConstants.YES) && isNotificationOn) {
            mNotificationHelper = new NotificationHelper(this);
            mNotificationHelper.doGetNotificationList();
        } else stopSelf();

        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void customNotification(NotificationData notificationData, String medicineSlot, int notification_id) {

        String notificationTime = CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.hh_mm_a);

        //---- Save notification in db---
        String timeStamp = CommonMethods.getCurrentDate() + " " + notificationTime;
        AppDBHelper appDBHelper = new AppDBHelper(getApplicationContext());
        int id = (int) System.currentTimeMillis(); // medication.getMedicineId();

        String medicationDataDetails = getText(R.string.have_u_taken) + medicineSlot + "?";
        String medicineData = getText(R.string.have_u_taken) + medicineSlot.toLowerCase() + "?";

        appDBHelper.insertUnreadReceivedNotificationMessage(String.valueOf(notification_id), RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT, medicationDataDetails, new Gson().toJson(notificationData), timeStamp, true);

        Intent mNotifyYesIntent = new Intent(this.getApplicationContext(), ClickOnCheckBoxOfNotificationReceiver.class);
        mNotifyYesIntent.putExtra(RescribeConstants.MEDICINE_SLOT, medicineSlot);
        mNotifyYesIntent.putExtra(RescribeConstants.NOTIFICATION_ID, notification_id);
        mNotifyYesIntent.putExtra(getString(R.string.unread_notification_update_received), id);

        PendingIntent mYesPendingIntent = PendingIntent.getBroadcast(this, notification_id, mNotifyYesIntent, 0);

        Intent mNotifyNoIntent = new Intent(this, ClickOnNotificationReceiver.class);
        mNotifyNoIntent.putExtra(RescribeConstants.MEDICINE_SLOT, medicineSlot);
        mNotifyNoIntent.putExtra(RescribeConstants.NOTIFICATION_DATE, CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.DD_MM_YYYY));
        mNotifyNoIntent.putExtra(RescribeConstants.NOTIFICATION_TIME, notificationTime);
        mNotifyNoIntent.putExtra(RescribeConstants.NOTIFICATION_ID, notification_id);
        mNotifyNoIntent.putExtra(getString(R.string.unread_notification_update_received), id);

        PendingIntent mNoPendingIntent = PendingIntent.getBroadcast(this, notification_id, mNotifyNoIntent, 0);

        // **********************************************************************************

        // Collapse

        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews mRemoteViewCollapse = new RemoteViews(getPackageName(), R.layout.notification_layout);
        mRemoteViewCollapse.setOnClickPendingIntent(R.id.notificationLayout, mNoPendingIntent);
        mRemoteViewCollapse.setOnClickPendingIntent(R.id.buttonYes, mYesPendingIntent);
        mRemoteViewCollapse.setTextViewText(R.id.showMedicineName, medicineSlot);
        mRemoteViewCollapse.setTextViewText(R.id.questionText, medicineData);
        mRemoteViewCollapse.setTextViewText(R.id.timeText, notificationTime);

        // **********************************************************************************

        // Expanded

        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews mRemoteViewExpanded = new RemoteViews(getPackageName(), R.layout.notification_layout_expanded);
        mRemoteViewExpanded.setOnClickPendingIntent(R.id.notificationLayout, mNoPendingIntent);
        mRemoteViewExpanded.setOnClickPendingIntent(R.id.buttonYes, mYesPendingIntent);
        mRemoteViewExpanded.setTextViewText(R.id.showMedicineName, medicineSlot);
        mRemoteViewExpanded.setTextViewText(R.id.questionText, medicineData);
        mRemoteViewExpanded.setTextViewText(R.id.timeText, notificationTime);

        // **********************************************************************************

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.logosmall)
                // Set Ticker Message
                .setTicker(getString(R.string.customnotificationticker))
                // Dismiss Notification
                .setAutoCancel(true);

        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = builder.build();

        notification.contentView = mRemoteViewCollapse;
        if (Build.VERSION.SDK_INT >= 16) {
            notification.bigContentView = mRemoteViewExpanded;
        }

        notificationmanager.notify(MEDICATIONS_NOTIFICATION_TAG, notification_id, notification);
        //--- Show notification/Alarm based on user configured setting : END

        stopSelf();
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equals(RescribeConstants.TASK_NOTIFICATION)) {
            NotificationModel prescriptionDataReceived = (NotificationModel) customResponse;

            String slot = CommonMethods.getMealTime(hour24, this);

            if (!prescriptionDataReceived.getNotificationPrescriptionModel().getPresriptionNotification().isEmpty()) {
                ArrayList<Medication> notificationDataList;
                NotificationData notificationDataForHeader = new NotificationData();
                List<NotificationData> notificationData = prescriptionDataReceived.getNotificationPrescriptionModel().getPresriptionNotification();
                String date = CommonMethods.getCurrentDateTime();
                CommonMethods.Log(TAG, date);
                //Current date and slot data is sorted to show in header of UI
                for (NotificationData notificationD : notificationData) {
                    if (notificationD.getPrescriptionDate().equals(CommonMethods.getCurrentDateTime())) {
                        String prescriptionDate = notificationD.getPrescriptionDate();
                        notificationDataList = notificationD.getMedication();
                        notificationDataForHeader.setMedication(notificationDataList);
                        notificationDataForHeader.setPrescriptionDate(prescriptionDate);
                    }
                }

                NotificationData filteredData = mNotificationHelper.getFilteredData(notificationDataForHeader, slot);

                int notification_id = 0;
                String medicineSlot = null;

                if (slot.equals(getString(R.string.break_fast))) {
                    medicineSlot = getString(R.string.breakfast_medication);
                    notification_id = BREAKFAST_NOTIFICATION_ID;
                } else if (slot.equals(getString(R.string.mlunch))) {
                    medicineSlot = getString(R.string.lunch_medication);
                    notification_id = LUNCH_NOTIFICATION_ID;
                } else if (slot.equals(getString(R.string.msnacks))) {
                    medicineSlot = getString(R.string.snacks_medication);
                    notification_id = EVENING_NOTIFICATION_ID;
                } else if (slot.equals(getString(R.string.mdinner))) {
                    medicineSlot = getString(R.string.dinner_medication);
                    notification_id = DINNER_NOTIFICATION_ID;
                }

                if (filteredData.getMedication() != null && medicineSlot != null) {
                    if (!filteredData.getMedication().isEmpty())
                        customNotification(filteredData, medicineSlot, notification_id);
                }

            }

            stopSelf();
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