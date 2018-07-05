package com.rescribe.broadcast_receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rescribe.R;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.notification.RespondToNotificationHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.investigation.InvestigationData;
import com.rescribe.model.response_model_notification.NotificationResponseBaseModel;
import com.rescribe.notification.AppointmentAlarmTask;
import com.rescribe.notification.DosesAlarmTask;
import com.rescribe.notification.InvestigationAlarmTask;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.activities.AppointmentActivity;
import com.rescribe.ui.activities.InvestigationActivity;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.NetworkUtil;
import com.rescribe.util.RescribeConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.rescribe.util.RescribeConstants.APPOINTMENT_NOTIFICATION_TAG;
import static com.rescribe.util.RescribeConstants.INVESTIGATION_NOTIFICATION_TAG;
import static com.rescribe.util.RescribeConstants.MEDICATIONS_NOTIFICATION_TAG;

/**
 * Created by jeetal on 16/5/17.
 */
public class ClickOnCheckBoxOfNotificationReceiver extends BroadcastReceiver implements HelperResponse {
    RespondToNotificationHelper respondToNotificationHelper;
    Context mContext;
    private int notificationId;

    @Override
    public void onReceive(Context mContext, Intent intent) {
        Integer medicineID = null;
        String medicineSlot = "";
        this.mContext = mContext;
        respondToNotificationHelper = new RespondToNotificationHelper(mContext, this);
        int notificationId = intent.getIntExtra(RescribeConstants.NOTIFICATION_ID, -1);
        int investigation_notification_id = intent.getIntExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_NOTIFICATION_ID, -1);
        int appointment_notification_id = intent.getIntExtra(RescribeConstants.APPOINTMENT_NOTIFICATION_ID, -1);
        int appointment_id = intent.getIntExtra(RescribeConstants.APPOINTMENT_ID, -1);

        int unreadMessNotificationID = intent.getIntExtra(mContext.getString(R.string.unread_notification_update_received), -1);

        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationId == DosesAlarmTask.BREAKFAST_NOTIFICATION_ID || notificationId == DosesAlarmTask.LUNCH_NOTIFICATION_ID || notificationId == DosesAlarmTask.DINNER_NOTIFICATION_ID || notificationId == DosesAlarmTask.EVENING_NOTIFICATION_ID) {
            String slot = (String) intent.getExtras().get(RescribeConstants.MEDICINE_SLOT);
            if (slot.equals(mContext.getString(R.string.breakfast_medication))) {
                medicineSlot = mContext.getString(R.string.smallcasebreakfast);
            } else if (slot.equals(mContext.getString(R.string.lunch_medication))) {
                medicineSlot = mContext.getString(R.string.smallcaselunch);
            } else if (slot.equals(mContext.getString(R.string.snacks_medication))) {
                medicineSlot = mContext.getString(R.string.smallcasesnacks);
            } else if (slot.equals(mContext.getString(R.string.dinner_medication))) {
                medicineSlot = mContext.getString(R.string.smallcasedinner);
            }
            if (NetworkUtil.isInternetAvailable(mContext)) {
                this.notificationId = notificationId;
                respondToNotificationHelper.doRespondToNotification(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PATIENT_ID, mContext)), medicineSlot, medicineID, CommonMethods.formatDateTime(CommonMethods.getCurrentDateTime(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE), 1, RescribeConstants.TASK_RESPOND_NOTIFICATION, 1);
                //Toast.makeText(mContext, slot + " " + notificationId + " " + "Dose Accepted", Toast.LENGTH_SHORT).show();
                manager.cancel(MEDICATIONS_NOTIFICATION_TAG, notificationId);
            } else {
                CommonMethods.showToast(mContext, mContext.getString(R.string.internet));
            }
        } else if (investigation_notification_id == InvestigationAlarmTask.INVESTIGATION_NOTIFICATION_ID) {

            // don't delete investigation data till investigation upload not completed
//            AppDBHelper.getInstance(mContext).deleteUnreadReceivedNotificationMessage(String.valueOf(unreadMessNotificationID), RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT);

            int notificationCount = RescribePreferencesManager.getInt(RescribeConstants.NOTIFICATION_COUNT, mContext);
            if (notificationCount > 0)
                RescribePreferencesManager.putInt(RescribeConstants.NOTIFICATION_COUNT, notificationCount - 1, mContext);

            ArrayList<InvestigationData> investigationData = intent.getParcelableArrayListExtra(RescribeConstants.INVESTIGATION_LIST);
            Intent intentNotification = new Intent(mContext, InvestigationActivity.class);
            intentNotification.putExtra(RescribeConstants.INVESTIGATION_LIST, investigationData);
            intentNotification.putExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_TIME, intent.getStringExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_TIME));
            intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intentNotification);

            manager.cancel(INVESTIGATION_NOTIFICATION_TAG, investigationData.get(0).getDrId());

        } else if (appointment_notification_id == AppointmentAlarmTask.APPOINTMENT_NOTIFICATION_ID) {

            AppDBHelper.getInstance(mContext).deleteUnreadReceivedNotificationMessage(String.valueOf(unreadMessNotificationID), RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT);
            int notificationCount = RescribePreferencesManager.getInt(RescribeConstants.NOTIFICATION_COUNT, mContext);
            if (notificationCount > 0)
                RescribePreferencesManager.putInt(RescribeConstants.NOTIFICATION_COUNT, notificationCount - 1, mContext);

            SimpleDateFormat sdf = new SimpleDateFormat(RescribeConstants.DD_MM_YYYY + " " + RescribeConstants.DATE_PATTERN.hh_mm_a, Locale.US);
            try {

                String time = intent.getStringExtra(RescribeConstants.APPOINTMENT_TIME);
                String date = intent.getStringExtra(RescribeConstants.APPOINTMENT_DATE);

                String dateTime = date + " " + time;

                Date date1 = sdf.parse(CommonMethods.getCurrentTimeStamp(RescribeConstants.DD_MM_YYYY + " " +RescribeConstants.DATE_PATTERN.hh_mm_a));
                Date date2 = sdf.parse(dateTime);

                if (date2.before(date1))
                    CommonMethods.showToast(mContext, mContext.getResources().getString(R.string.appointment_time_has_lapsed));
                else {
                    Intent intentNotification = new Intent(mContext, AppointmentActivity.class);
                    intentNotification.putExtra(RescribeConstants.APPOINTMENT_TIME, time);
                    intentNotification.putExtra(RescribeConstants.CALL_FROM_DASHBOARD,"");
                    intentNotification.putExtra(RescribeConstants.APPOINTMENT_MESSAGE, intent.getBundleExtra(RescribeConstants.APPOINTMENT_MESSAGE));
                    intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intentNotification);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            manager.cancel(APPOINTMENT_NOTIFICATION_TAG, appointment_id);
        }

        // Collapse the notification bar.
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        mContext.sendBroadcast(it);

    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (customResponse instanceof NotificationResponseBaseModel) {
            NotificationResponseBaseModel responseLogNotificationModel = (NotificationResponseBaseModel) customResponse;
            if (responseLogNotificationModel.getCommon().isSuccess()) {

                CommonMethods.showToast(mContext, responseLogNotificationModel.getCommon().getStatusMessage());
                AppDBHelper.getInstance(mContext).deleteUnreadReceivedNotificationMessage(String.valueOf(notificationId), RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT);

                int notificationCount = RescribePreferencesManager.getInt(RescribeConstants.NOTIFICATION_COUNT, mContext);
                if (notificationCount > 0)
                    RescribePreferencesManager.putInt(RescribeConstants.NOTIFICATION_COUNT, notificationCount - 1, mContext);
            }
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        CommonMethods.showToast(mContext, errorMessage);
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(mContext, serverErrorMessage);
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(mContext, serverErrorMessage);
    }
}
