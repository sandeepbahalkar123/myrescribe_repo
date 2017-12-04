package com.rescribe.services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Binder;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.rescribe.R;
import com.rescribe.broadcast_receivers.ClickOnCheckBoxOfNotificationReceiver;
import com.rescribe.broadcast_receivers.ClickOnNotificationReceiver;
import com.rescribe.helpers.notification.AppointmentHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.notification.AppointmentsNotificationData;
import com.rescribe.model.notification.AppointmentsNotificationModel;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
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
public class AppointmentNotificationService extends Service implements HelperResponse {

//    static int mNotificationNoTextField = 0;

    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "com.rescribe";
    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();
    private String notifyTime;

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // If this service was started by out DosesAlarmTask intent then we want to show our notification

        String loginStatus = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, this);
        boolean isNotificationOn = RescribePreferencesManager.getBoolean(RescribePreferencesManager.NOTIFICATION_SETTING_KEY.APPOINTMENT_ALERT, this);

        if (loginStatus.equals(RescribeConstants.YES) && isNotificationOn) {

            int notification_id = intent.getIntExtra(RescribeConstants.APPOINTMENT_NOTIFICATION_ID, 0);
            notifyTime = intent.getStringExtra(RescribeConstants.APPOINTMENT_TIME);

            if (intent.getBooleanExtra(INTENT_NOTIFY, false)) {
                AppointmentHelper appointmentHelper = new AppointmentHelper(this);
                appointmentHelper.getDoctorList();
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

    public void customNotification(ArrayList<AppointmentsNotificationData> data, int index) {

        int preCount = RescribePreferencesManager.getInt(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT, AppointmentNotificationService.this);
        RescribePreferencesManager.putInt(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT, preCount + 1, AppointmentNotificationService.this);

        String drName = data.get(index).getDoctorName();
        int subNotificationId = data.get(index).getAptId();
        String date = CommonMethods.getFormattedDate(data.get(index).getAptDate(), RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DD_MM_YYYY);
        String time = CommonMethods.getFormattedDate(data.get(index).getAptTime(), RescribeConstants.DATE_PATTERN.HH_mm_ss, RescribeConstants.DATE_PATTERN.hh_mm_a);
        String message = "You have an appointment with " + drName + " on " + date + " at " + time.toLowerCase() + ".";

        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(),
                R.layout.appointment_notification_layout);

        Intent mNotifyYesIntent = new Intent(this, ClickOnCheckBoxOfNotificationReceiver.class);
        mNotifyYesIntent.putExtra(RescribeConstants.APPOINTMENT_NOTIFICATION_ID, subNotificationId);
        mNotifyYesIntent.putExtra(RescribeConstants.APPOINTMENT_TIME, time);
        mNotifyYesIntent.putExtra(RescribeConstants.APPOINTMENT_MESSAGE, message);
        PendingIntent mYesPendingIntent = PendingIntent.getBroadcast(this, subNotificationId, mNotifyYesIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.notificationLayout, mYesPendingIntent);

        Intent mNotifyNoIntent = new Intent(this, ClickOnNotificationReceiver.class);
        mNotifyNoIntent.putExtra(RescribeConstants.APPOINTMENT_NOTIFICATION_ID, subNotificationId);
        mNotifyNoIntent.putExtra(RescribeConstants.APPOINTMENT_TIME, time);
        mNotifyNoIntent.putExtra(RescribeConstants.APPOINTMENT_MESSAGE, message);
        PendingIntent mNoPendingIntent = PendingIntent.getBroadcast(this, subNotificationId, mNotifyNoIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.buttonYes, mNoPendingIntent);

        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.logosmall)
                // Set Ticker Message
                .setTicker(getString(R.string.appointment))
                // Dismiss Notification
                .setAutoCancel(true)
                // Set RemoteViews into Notification
                .setContent(mRemoteViews)
                .setVibrate(new long[]{1000, 1000, 1000})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) //This sets the sound to play
                .setStyle(new android.support.v7.app.NotificationCompat.DecoratedCustomViewStyle());

        mRemoteViews.setTextViewText(R.id.showMedicineName, getResources().getString(R.string.appointment));
        mRemoteViews.setTextViewText(R.id.questionText, message);
        mRemoteViews.setTextViewText(R.id.timeText, notifyTime);
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationmanager.notify(subNotificationId, builder.build());


        /*//--- TODO : ADDED FOR TESTING & DEVELOPMENT, COMMENT THIS AS RIGHT NOW IN ONLY PRESCRIPTION NOTIFICATION REQUIRED NOT IN APPOINTMENT.
        // //--- TODO : Show notification/Alarm based on user configured setting :START
        String string = RescribePreferencesManager.getString(getString(R.string.notificationAlarmTypeSetting), this);
        if (getString(R.string.alarm).equalsIgnoreCase(string)) {
            //-----Open Alarm dialog based on config setting-----
            //----------
            Intent popup = new Intent(getApplicationContext(), SnoozeAlarmNotifyActivity.class);
            popup.putExtra(RescribeConstants.MEDICINE_SLOT, time);
            popup.putExtra(RescribeConstants.NOTIFICATION_TIME, time);
            popup.putExtra(RescribeConstants.NOTIFICATION_ID, "" + subNotificationId);
            popup.putExtra(RescribeConstants.TITLE, message);
            popup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(popup);
            //----------
            //----------
        } else {
            notificationmanager.notify(subNotificationId, builder.build());
        }
        //--- Show notification/Alarm based on user configured setting : END
        */
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

        if (customResponse instanceof AppointmentsNotificationModel) {
            AppointmentsNotificationModel appointmentsNotificationModel = (AppointmentsNotificationModel) customResponse;
            if (!appointmentsNotificationModel.getData().getAptList().isEmpty()) {
                for (int index = 0; index < appointmentsNotificationModel.getData().getAptList().size(); index++)
                    customNotification(appointmentsNotificationModel.getData().getAptList(), index);
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