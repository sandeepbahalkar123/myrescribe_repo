package com.rescribe.broadcast_receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rescribe.R;
import com.rescribe.helpers.notification.RespondToNotificationHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.response_model_notification.ResponseLogNotificationModel;
import com.rescribe.notification.AppointmentAlarmTask;
import com.rescribe.notification.DosesAlarmTask;
import com.rescribe.notification.InvestigationAlarmTask;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.activities.AppointmentActivity;
import com.rescribe.ui.activities.InvestigationActivity;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;
import com.rescribe.util.NetworkUtil;

/**
 * Created by jeetal on 16/5/17.
 */
public class ClickOnCheckBoxOfNotificationReceiver extends BroadcastReceiver implements  HelperResponse{
    RespondToNotificationHelper respondToNotificationHelper;
    Context mContext;
    @Override


    public void onReceive(Context mContext, Intent intent) {
        Integer medicineID = null;
        String medicineSlot = "";
        this.mContext = mContext;
        respondToNotificationHelper = new RespondToNotificationHelper(mContext,this);
        int notificationId = intent.getIntExtra(RescribeConstants.NOTIFICATION_ID, 10);
        int investigation_notification_id = intent.getIntExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_NOTIFICATION_ID, 10);
        int appointment_notification_id = intent.getIntExtra(RescribeConstants.APPOINTMENT_NOTIFICATION_ID, 10);
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationId == DosesAlarmTask.BREAKFAST_NOTIFICATION_ID || notificationId == DosesAlarmTask.LUNCH_NOTIFICATION_ID || notificationId == DosesAlarmTask.DINNER_NOTIFICATION_ID || notificationId == DosesAlarmTask.EVENING_NOTIFICATION_ID) {
            String slot = (String) intent.getExtras().get(RescribeConstants.MEDICINE_SLOT);
            if(slot.equals(mContext.getString(R.string.breakfast_medication))){
                medicineSlot = mContext.getString(R.string.smallcasebreakfast);
            }else if (slot.equals(mContext.getString(R.string.lunch_medication))){
                medicineSlot = mContext.getString(R.string.smallcaselunch);
            }else if (slot.equals(mContext.getString(R.string.snacks_medication))){
                medicineSlot = mContext.getString(R.string.smallcasesnacks);
            }else if (slot.equals(mContext.getString(R.string.dinner_medication))){
                medicineSlot = mContext.getString(R.string.smallcasedinner);
            }
            if(NetworkUtil.isInternetAvailable(mContext)) {
                respondToNotificationHelper.doRespondToNotification(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext)), medicineSlot, medicineID, CommonMethods.formatDateTime(CommonMethods.getCurrentDateTime(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE), 1, RescribeConstants.TASK_RESPOND_NOTIFICATION);
                //Toast.makeText(mContext, slot + " " + notificationId + " " + "Dose Accepted", Toast.LENGTH_SHORT).show();
                manager.cancel(notificationId);
            }else{
                CommonMethods.showToast(mContext,mContext.getString(R.string.internet));
            }
        } else if (investigation_notification_id == InvestigationAlarmTask.INVESTIGATION_NOTIFICATION_ID) {
            Intent intentNotification = new Intent(mContext, InvestigationActivity.class);
            intentNotification.putExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_TIME, intent.getStringExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_TIME));
//            intentNotification.putExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_MESSAGE, intent.getBundleExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_MESSAGE));
            intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intentNotification);
            manager.cancel(investigation_notification_id);
        } else if (appointment_notification_id == AppointmentAlarmTask.APPOINTMENT_NOTIFICATION_ID) {
            Intent intentNotification = new Intent(mContext, AppointmentActivity.class);
            intentNotification.putExtra(RescribeConstants.APPOINTMENT_TIME, intent.getStringExtra(RescribeConstants.APPOINTMENT_TIME));
            intentNotification.putExtra(RescribeConstants.APPOINTMENT_MESSAGE, intent.getBundleExtra(RescribeConstants.APPOINTMENT_MESSAGE));
            intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intentNotification);
            manager.cancel(appointment_notification_id);
        }

    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        ResponseLogNotificationModel responseLogNotificationModel = (ResponseLogNotificationModel) customResponse;
        if (responseLogNotificationModel.getCommon().isSuccess()) {
            CommonMethods.showToast(mContext, responseLogNotificationModel.getCommon().getStatusMessage());
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        CommonMethods.showToast(mContext,errorMessage);

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(mContext,serverErrorMessage);
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(mContext,serverErrorMessage);

    }
}
