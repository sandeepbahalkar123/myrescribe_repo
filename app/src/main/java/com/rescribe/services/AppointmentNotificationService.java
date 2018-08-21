package com.rescribe.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.rescribe.R;
import com.rescribe.broadcast_receivers.ClickOnCheckBoxOfNotificationReceiver;
import com.rescribe.broadcast_receivers.ClickOnNotificationReceiver;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.notification.AppointmentHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.notification.AppointmentsNotificationData;
import com.rescribe.model.notification.AppointmentsNotificationModel;
import com.rescribe.notification.AppointmentAlarmTask;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static com.rescribe.util.RescribeConstants.APPOINTMENT_NOTIFICATION_TAG;
import static com.rescribe.util.RescribeConstants.SAMSUNG;


/**
 * This service is started when an Alarm has been raised
 * <p>
 * We pop a notification into the status bar for the user to click on
 * When the user clicks the notification a new activity is opened
 *
 * @author paul.blundell
 */
public class AppointmentNotificationService extends Service implements HelperResponse {

    public static final String APPOINTMENT_CHANNEL = "appointment_notification";
    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "com.rescribe";
    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();
    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        createChannel();
    }

    public void createChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Create the channel object with the unique ID CONNECT_CHANNEL
            NotificationChannel connectChannel = new NotificationChannel(
                    APPOINTMENT_CHANNEL, "Appointment Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the channel's initial settings
            connectChannel.setLightColor(Color.GREEN);
            connectChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            // Submit the notification channel object to the notification manager
            getNotificationManager().createNotificationChannel(connectChannel);
        }
    }

    /**
     * Get the notification mNotificationManager.
     * <p>
     * <p>Utility method as this helper works with it a lot.
     *
     * @return The system service NotificationManager
     */
    public NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        CommonMethods.Log("ALARM", "AppointmentNotificationService");

        // If this service was started by out DosesAlarmTask intent then we want to show our notification

        String loginStatus = RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.LOGIN_STATUS, this);
        boolean isNotificationOn = RescribePreferencesManager.getBoolean(getString(R.string.appointment_alert), this);

        if (loginStatus.equals(RescribeConstants.YES) && isNotificationOn) {

            AppointmentHelper appointmentHelper = new AppointmentHelper(this);
            appointmentHelper.getDoctorList();

        } else stopSelf();
        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void customNotification(AppointmentsNotificationData data, int index) {

        //        String notificationTimeSlot = intentData.getStringExtra(RescribeConstants.NOTIFICATION_TIME);
        String notificationTime = CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.hh_mm_a);

        String drName = data.getDoctorName();
        String doctorName = "";
        if (drName.toLowerCase().contains("dr.")) {
            doctorName = data.getDoctorName();
        } else {
            doctorName = "Dr. " + data.getDoctorName();
        }
        int subNotificationId = Integer.parseInt(data.getAptId());
        String date = CommonMethods.getFormattedDate(data.getAptDate(), RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DD_MM_YYYY);
        String time = CommonMethods.getFormattedDate(data.getAptTime(), RescribeConstants.DATE_PATTERN.HH_mm_ss, RescribeConstants.DATE_PATTERN.hh_mm_a);
        String message = "You have an appointment with " + doctorName + " on " + date + " at " + time.toLowerCase() + ".";

        //---- Save notification in db---
        AppDBHelper appDBHelper = new AppDBHelper(getApplicationContext());
        String currentTimeStamp = CommonMethods.getCurrentDate() + " " + notificationTime;
        appDBHelper.insertUnreadReceivedNotificationMessage("" + subNotificationId, RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT, message, message, currentTimeStamp, true);
        //------

        Intent mNotifyYesIntent = new Intent(this, ClickOnCheckBoxOfNotificationReceiver.class);
        mNotifyYesIntent.putExtra(RescribeConstants.APPOINTMENT_NOTIFICATION_ID, AppointmentAlarmTask.APPOINTMENT_NOTIFICATION_ID);
        mNotifyYesIntent.putExtra(RescribeConstants.APPOINTMENT_ID, subNotificationId);
        mNotifyYesIntent.putExtra(RescribeConstants.APPOINTMENT_TIME, time);
        mNotifyYesIntent.putExtra(RescribeConstants.APPOINTMENT_DATE, date);
        mNotifyYesIntent.putExtra(RescribeConstants.APPOINTMENT_MESSAGE, message);
        mNotifyYesIntent.putExtra(getString(R.string.unread_notification_update_received), subNotificationId);

        PendingIntent mYesPendingIntent = PendingIntent.getBroadcast(this, subNotificationId, mNotifyYesIntent, 0);
        

        Intent mNotifyNoIntent = new Intent(this, ClickOnNotificationReceiver.class);
        mNotifyNoIntent.putExtra(RescribeConstants.APPOINTMENT_NOTIFICATION_ID, AppointmentAlarmTask.APPOINTMENT_NOTIFICATION_ID);
        mNotifyNoIntent.putExtra(RescribeConstants.APPOINTMENT_ID, subNotificationId);
        mNotifyNoIntent.putExtra(RescribeConstants.APPOINTMENT_TIME, time);
        mNotifyNoIntent.putExtra(RescribeConstants.APPOINTMENT_DATE, date);
        mNotifyNoIntent.putExtra(RescribeConstants.APPOINTMENT_MESSAGE, message);
        mNotifyNoIntent.putExtra(getString(R.string.unread_notification_update_received), subNotificationId);
        PendingIntent mNoPendingIntent = PendingIntent.getBroadcast(this, subNotificationId, mNotifyNoIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, APPOINTMENT_CHANNEL)
                // Set Icon
                .setSmallIcon(R.drawable.logosmall)
                // Set Ticker Message
                .setTicker(getString(R.string.appointment))
                // Dismiss Notification
                .setAutoCancel(true);

        // *************************************************************************************************************
        
        // Collapsed
        
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews mRemoteViewCollapse = new RemoteViews(getPackageName(),
                R.layout.appointment_notification_layout);
        mRemoteViewCollapse.setOnClickPendingIntent(R.id.notificationLayout, mYesPendingIntent);
        mRemoteViewCollapse.setOnClickPendingIntent(R.id.buttonYes, mNoPendingIntent);
        mRemoteViewCollapse.setTextViewText(R.id.showMedicineName, getResources().getString(R.string.appointment));
        mRemoteViewCollapse.setTextViewText(R.id.questionText, message);
        mRemoteViewCollapse.setTextViewText(R.id.timeText, notificationTime);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP && Build.MANUFACTURER.contains(SAMSUNG)){
            mRemoteViewCollapse.setTextColor(R.id.showMedicineName, Color.WHITE);
            mRemoteViewCollapse.setTextColor(R.id.questionText, Color.WHITE);
            mRemoteViewCollapse.setTextColor(R.id.timeText, Color.WHITE);
        }

        // *************************************************************************************************************
        
        // Expanded

        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews mRemoteViewExpanded = new RemoteViews(getPackageName(),
                R.layout.appointment_notification_layout_expanded);
        mRemoteViewExpanded.setOnClickPendingIntent(R.id.notificationLayout, mYesPendingIntent);
        mRemoteViewExpanded.setOnClickPendingIntent(R.id.buttonYes, mNoPendingIntent);
        mRemoteViewExpanded.setTextViewText(R.id.showMedicineName, getResources().getString(R.string.appointment));
        mRemoteViewExpanded.setTextViewText(R.id.questionText, message);
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

        notificationmanager.notify(APPOINTMENT_NOTIFICATION_TAG, subNotificationId, notification);
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

        if (customResponse instanceof AppointmentsNotificationModel) {
            AppointmentsNotificationModel appointmentsNotificationModel = (AppointmentsNotificationModel) customResponse;
            ArrayList<AppointmentsNotificationData> aptList = appointmentsNotificationModel.getData().getAptList();
            if (!aptList.isEmpty()) {

                Comparator<AppointmentsNotificationData> comparator = new Comparator<AppointmentsNotificationData>() {
                    @Override
                    public int compare(AppointmentsNotificationData o1, AppointmentsNotificationData o2) {
                        Date m1Date = CommonMethods.convertStringToDate(o1.getAptDate() + "_" + o1.getAptTime(), RescribeConstants.DATE_PATTERN.UTC_PATTERN + "_" + RescribeConstants.DATE_PATTERN.HH_mm_ss);
                        Date m2Date = CommonMethods.convertStringToDate(o2.getAptDate() + "_" + o2.getAptTime(), RescribeConstants.DATE_PATTERN.UTC_PATTERN + "_" + RescribeConstants.DATE_PATTERN.HH_mm_ss);
                        return m1Date.compareTo(m2Date);
                    }
                };

                Collections.sort(aptList, Collections.reverseOrder(comparator));

                for (int index = 0; index < aptList.size(); index++) {
                    AppointmentsNotificationData aptListObject = aptList.get(index);
                    Date dateOfApp = CommonMethods.convertStringToDate(aptListObject.getAptDate() + "_" + aptListObject.getAptTime(), RescribeConstants.DATE_PATTERN.UTC_PATTERN + "_" + RescribeConstants.DATE_PATTERN.HH_mm_ss);
                    Date today = CommonMethods.convertStringToDate(CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.UTC_PATTERN + "_" + RescribeConstants.DATE_PATTERN.HH_mm_ss), RescribeConstants.DATE_PATTERN.UTC_PATTERN + "_" + RescribeConstants.DATE_PATTERN.HH_mm_ss);

                    Calendar next48Hr = Calendar.getInstance();
                    next48Hr.setTime(today);
                    next48Hr.add(Calendar.DAY_OF_YEAR, 2);
                    Date next48HrDate = next48Hr.getTime();

                    boolean inRange = dateOfApp.before(next48HrDate);

                    if (inRange && !aptListObject.getAptId().isEmpty())
                        customNotification(aptListObject, index);
                }

            }
        }

        stopSelf();
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
        AppointmentNotificationService getService() {
            return AppointmentNotificationService.this;
        }
    }

}