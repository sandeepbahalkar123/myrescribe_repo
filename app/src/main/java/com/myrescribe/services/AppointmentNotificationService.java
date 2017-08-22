package com.myrescribe.services;

import android.app.AlarmManager;
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
import com.myrescribe.broadcast_receivers.NoClickReceiver;
import com.myrescribe.broadcast_receivers.YesClickReceiver;
import com.myrescribe.helpers.notification.AppointmentHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.notification.AppointmentsNotificationData;
import com.myrescribe.model.notification.AppointmentsNotificationModel;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.util.List;


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
    public static final String INTENT_NOTIFY = "com.myrescribe";
    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();
    private String notifyTime;

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // If this service was started by out DosesAlarmTask intent then we want to show our notification

        if (MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, this).equals(MyRescribeConstants.YES)) {

            int notification_id = intent.getIntExtra(MyRescribeConstants.APPOINTMENT_NOTIFICATION_ID, 0);
            notifyTime = intent.getStringExtra(MyRescribeConstants.APPOINTMENT_TIME);

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

    public void customNotification(List<AppointmentsNotificationData> data, int index) {

        String drName = data.get(index).getDoctorName();
        int subNotificationId = data.get(index).getAptId();
        String date = CommonMethods.getFormatedDate(data.get(index).getAptDate(), MyRescribeConstants.DATE_PATTERN.UTC_PATTERN, MyRescribeConstants.DD_MM_YYYY);
        String time = CommonMethods.getFormatedDate(data.get(index).getAptTime(), MyRescribeConstants.DATE_PATTERN.HH_mm_ss, MyRescribeConstants.DATE_PATTERN.hh_mm_a);
        String message = "You have an appointment with " + drName + " on " + date + " at " + time.toLowerCase() + ".";

        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(),
                R.layout.appointment_notification_layout);

        Intent mNotifyYesIntent = new Intent(this, YesClickReceiver.class);
        mNotifyYesIntent.putExtra(MyRescribeConstants.APPOINTMENT_NOTIFICATION_ID, subNotificationId);
        mNotifyYesIntent.putExtra(MyRescribeConstants.APPOINTMENT_TIME, time);
        mNotifyYesIntent.putExtra(MyRescribeConstants.APPOINTMENT_MESSAGE, message);
        PendingIntent mYesPendingIntent = PendingIntent.getBroadcast(this, subNotificationId, mNotifyYesIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.notificationLayout, mYesPendingIntent);

        Intent mNotifyNoIntent = new Intent(this, NoClickReceiver.class);
        mNotifyNoIntent.putExtra(MyRescribeConstants.APPOINTMENT_NOTIFICATION_ID, subNotificationId);
        mNotifyNoIntent.putExtra(MyRescribeConstants.APPOINTMENT_TIME, time);
        mNotifyNoIntent.putExtra(MyRescribeConstants.APPOINTMENT_MESSAGE, message);
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
                .setStyle(new android.support.v7.app.NotificationCompat.DecoratedCustomViewStyle());

        mRemoteViews.setTextViewText(R.id.showMedicineName, getResources().getString(R.string.appointment));
        mRemoteViews.setTextViewText(R.id.questionText, message);
        mRemoteViews.setTextViewText(R.id.timeText, notifyTime);
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(subNotificationId, builder.build());
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

        if (customResponse instanceof AppointmentsNotificationModel) {
            AppointmentsNotificationModel appointmentsNotificationModel = (AppointmentsNotificationModel) customResponse;
            if (!appointmentsNotificationModel.getData().isEmpty()) {
                for (int index = 0; index < appointmentsNotificationModel.getData().size(); index++)
                    customNotification(appointmentsNotificationModel.getData(), index);
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