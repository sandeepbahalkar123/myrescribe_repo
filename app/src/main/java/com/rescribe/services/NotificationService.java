package com.rescribe.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
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
import com.rescribe.ui.activities.SnoozeAlarmNotifyActivity;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.facebook.login.widget.ProfilePictureView.TAG;
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

//    static int mNotificationNoTextField = 0;

    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "com.rescribe";
    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();
    private int notification_id;
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

        String loginStatus = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, this);
        boolean isNotificationOn = RescribePreferencesManager.getBoolean(getString(R.string.medication_alert), this);

        if (loginStatus.equals(RescribeConstants.YES) && isNotificationOn) {

            notification_id = intent.getIntExtra(RescribeConstants.NOTIFICATION_ID, 0);

            // If this service was started by out DosesAlarmTask intent then we want to show our notification
            if (intent.getBooleanExtra(INTENT_NOTIFY, false)) {
                NotificationHelper mNotificationHelper = new NotificationHelper(this);
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

    public void customNotification(Intent intentData, NotificationData notificationData) {
        //--------------
        String medicineSlot = intentData.getStringExtra(RescribeConstants.MEDICINE_SLOT);
//        String notificationTimeSlot = intentData.getStringExtra(RescribeConstants.NOTIFICATION_TIME);
        String notificationTime = CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.hh_mm_a);
        String title = getText(R.string.taken_medicine).toString();

        //---- Save notification in db---
        String timeStamp = CommonMethods.getCurrentDate() + " " + notificationTime;
        AppDBHelper appDBHelper = new AppDBHelper(getApplicationContext());
        int id = (int) System.currentTimeMillis(); // medication.getMedicineId();

        String medicationDataDetails = getText(R.string.have_u_taken).toString() + medicineSlot + "?";
        String medicineData = getText(R.string.have_u_taken).toString() + medicineSlot.toLowerCase();

        appDBHelper.insertUnreadReceivedNotificationMessage(String.valueOf(notification_id), RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT, medicationDataDetails, new Gson().toJson(notificationData), timeStamp);

        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);

        Intent mNotifyYesIntent = new Intent(this.getApplicationContext(), ClickOnCheckBoxOfNotificationReceiver.class);
        mNotifyYesIntent.putExtra(RescribeConstants.MEDICINE_SLOT, medicineSlot);
        mNotifyYesIntent.putExtra(RescribeConstants.NOTIFICATION_ID, notification_id);
        mNotifyYesIntent.putExtra(getString(R.string.unread_notification_update_received), id);

        PendingIntent mYesPendingIntent = PendingIntent.getBroadcast(this, notification_id, mNotifyYesIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.buttonYes, mYesPendingIntent);

        Intent mNotifyNoIntent = new Intent(this, ClickOnNotificationReceiver.class);
        mNotifyNoIntent.putExtra(RescribeConstants.MEDICINE_SLOT, medicineSlot);
        mNotifyNoIntent.putExtra(RescribeConstants.NOTIFICATION_DATE, intentData.getStringExtra(RescribeConstants.NOTIFICATION_DATE));
        mNotifyNoIntent.putExtra(RescribeConstants.NOTIFICATION_TIME, notificationTime);
        mNotifyNoIntent.putExtra(RescribeConstants.MEDICINE_NAME, intentData.getBundleExtra(RescribeConstants.MEDICINE_NAME));
        mNotifyNoIntent.putExtra(RescribeConstants.NOTIFICATION_ID, notification_id);
        mNotifyNoIntent.putExtra(getString(R.string.unread_notification_update_received), id);

        PendingIntent mNoPendingIntent = PendingIntent.getBroadcast(this, notification_id, mNotifyNoIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.notificationLayout, mNoPendingIntent);


        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.logosmall)
                // Set Ticker Message
                .setTicker(getString(R.string.customnotificationticker))
                // Dismiss Notification
                .setAutoCancel(true)
                // Set RemoteViews into Notification
                .setContent(mRemoteViews)
                .setSound(soundUri) //This sets the sound to play
                .setVibrate(new long[]{1000, 1000, 1000})
                //.setSound(ringtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                //   .setPriority(NotificationCompat.PRIORITY_HIGH) //must give priority to High, Max which will considered as heads-up notification
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle());


        mRemoteViews.setTextViewText(R.id.showMedicineName, medicineSlot);
        mRemoteViews.setTextViewText(R.id.questionText, medicineData + "?");
        mRemoteViews.setTextViewText(R.id.timeText, notificationTime);
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification build = builder.build();
        // build.flags |= Notification.FLAG_INSISTENT;

        //--- Show notification/Alarm based on user configured setting :START
        String string = RescribePreferencesManager.getString(getString(R.string.notificationAlarmTypeSetting), this);
        if (getString(R.string.alarm).equalsIgnoreCase(string)) {
            //-----Open Alarm dialog based on config setting-----
            //----------
            Intent popup = new Intent(getApplicationContext(), SnoozeAlarmNotifyActivity.class);
            popup.putExtra(RescribeConstants.MEDICINE_SLOT, medicineSlot);
            popup.putExtra(RescribeConstants.NOTIFICATION_TIME, notificationTime);
            popup.putExtra(RescribeConstants.NOTIFICATION_ID, "" + notification_id);
            popup.putExtra(RescribeConstants.TITLE, title);
            popup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(popup);
            //----------
            //----------
        } else notificationmanager.notify(MEDICATIONS_NOTIFICATION_TAG, notification_id, build);
        //--- Show notification/Alarm based on user configured setting : END

        stopSelf();
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equals(RescribeConstants.TASK_NOTIFICATION)) {
            NotificationModel prescriptionDataReceived = (NotificationModel) customResponse;

            String slot = CommonMethods.getMealTime(hour24, Min, this);

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

                NotificationData filteredData = getFilteredData(notificationDataForHeader, slot.toLowerCase());
                if (filteredData.getMedication() != null) {
                    if (!filteredData.getMedication().isEmpty())
                        customNotification(intent, filteredData);
                }

            }

            stopSelf();
        }
    }

    private NotificationData getFilteredData(NotificationData notificationData, String slot) {

        ArrayList<Medication> notificationListForNotification = new ArrayList<>();
        NotificationData notificationDataForNotification = new NotificationData();

        if (notificationData.getMedication() != null) {
            for (Medication medication : notificationData.getMedication()) {
                if (medication.getMedicinSlot().contains(slot))
                    notificationListForNotification.add(medication);
            }

            notificationDataForNotification.setMedication(notificationListForNotification);
            notificationDataForNotification.setPrescriptionDate(notificationData.getPrescriptionDate());
        }

        return notificationDataForNotification;
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