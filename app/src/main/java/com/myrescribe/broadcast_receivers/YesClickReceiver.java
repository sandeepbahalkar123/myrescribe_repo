package com.myrescribe.broadcast_receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.myrescribe.R;
import com.myrescribe.helpers.notification.RespondToNotificationHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.response_model_notification.ResponseLogNotificationModel;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.ui.activities.AppointmentActivity;
import com.myrescribe.ui.activities.InvestigationActivity;
import com.myrescribe.ui.activities.SplashScreenActivity;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;
import com.myrescribe.util.NetworkUtil;

/**
 * Created by jeetal on 16/5/17.
 */
public class YesClickReceiver extends BroadcastReceiver implements HelperResponse {
    RespondToNotificationHelper respondToNotificationHelper;
    Context mContext;

    @Override


    public void onReceive(Context mContext, Intent intent) {
        Integer medicineID = null;
        String medicineSlot = "";
        this.mContext = mContext;
        respondToNotificationHelper = new RespondToNotificationHelper(mContext, this);
        int notificationId = intent.getIntExtra(MyRescribeConstants.NOTIFICATION_ID, 10);
        int investigation_notification_id = intent.getIntExtra(MyRescribeConstants.INVESTIGATION_NOTIFICATION_ID, 10);
        int appointment_notification_id = intent.getIntExtra(MyRescribeConstants.APPOINTMENT_NOTIFICATION_ID, 10);

        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        String loginStatus = MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, mContext);

        if (!MyRescribeConstants.BLANK.equalsIgnoreCase(loginStatus)) {


            if (notificationId == 0 || notificationId == 1 || notificationId == 2 || notificationId == 3) {
                String slot = (String) intent.getExtras().get(MyRescribeConstants.MEDICINE_SLOT);
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
                    respondToNotificationHelper.doRespondToNotification(Integer.valueOf(MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATEINT_ID, mContext)), medicineSlot, medicineID, CommonMethods.formatDateTime(CommonMethods.getCurrentDateTime(), MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD, MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY, MyRescribeConstants.DATE), 1);
                    //Toast.makeText(mContext, slot + " " + notificationId + " " + "Dose Accepted", Toast.LENGTH_SHORT).show();
                    manager.cancel(notificationId);
                } else {
                    CommonMethods.showToast(mContext, mContext.getString(R.string.internet));
                }
            } else if (investigation_notification_id == 4) {
                Intent intentNotification = new Intent(mContext, InvestigationActivity.class);
                intentNotification.putExtra(MyRescribeConstants.INVESTIGATION_TIME, intent.getStringExtra(MyRescribeConstants.INVESTIGATION_TIME));
                intentNotification.putExtra(MyRescribeConstants.INVESTIGATION_MESSAGE, intent.getBundleExtra(MyRescribeConstants.INVESTIGATION_MESSAGE));
                intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intentNotification);
                manager.cancel(investigation_notification_id);
            } else if (appointment_notification_id == 5) {
                Intent intentNotification = new Intent(mContext, AppointmentActivity.class);
                intentNotification.putExtra(MyRescribeConstants.APPOINTMENT_TIME, intent.getStringExtra(MyRescribeConstants.APPOINTMENT_TIME));
                intentNotification.putExtra(MyRescribeConstants.APPOINTMENT_MESSAGE, intent.getBundleExtra(MyRescribeConstants.APPOINTMENT_MESSAGE));
                intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intentNotification);
                manager.cancel(appointment_notification_id);
            }
        } else {
            Intent intentNotification = new Intent(mContext, SplashScreenActivity.class);
            intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intentNotification);
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

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {

    }
}
