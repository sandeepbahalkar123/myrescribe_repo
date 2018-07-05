package com.rescribe.broadcast_receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.rescribe.R;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.investigation.InvestigationHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.investigation.InvestigationData;
import com.rescribe.notification.AppointmentAlarmTask;
import com.rescribe.notification.DosesAlarmTask;
import com.rescribe.notification.InvestigationAlarmTask;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.activities.NotificationActivity;
import com.rescribe.ui.activities.SplashScreenActivity;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import static com.rescribe.util.RescribeConstants.APPOINTMENT_NOTIFICATION_TAG;
import static com.rescribe.util.RescribeConstants.INVESTIGATION_NOTIFICATION_TAG;
import static com.rescribe.util.RescribeConstants.MEDICATIONS_NOTIFICATION_TAG;

/**
 * Created by jeetal on 16/5/17.
 */

public class ClickOnNotificationReceiver extends BroadcastReceiver implements HelperResponse {

    private Context mContext;

    @Override
    public void onReceive(Context mContext, Intent intent) {

        this.mContext = mContext;

        int notificationId = intent.getIntExtra(RescribeConstants.NOTIFICATION_ID, 10);
        int investigation_notification_id = intent.getIntExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_NOTIFICATION_ID, 10);
        int appointment_notification_id = intent.getIntExtra(RescribeConstants.APPOINTMENT_NOTIFICATION_ID, 10);
        String loginStatus = RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.LOGIN_STATUS, mContext);

        int unreadMessNotificationID = intent.getIntExtra(mContext.getString(R.string.unread_notification_update_received), -1);
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (!loginStatus.isEmpty()) {
            if (notificationId == DosesAlarmTask.BREAKFAST_NOTIFICATION_ID || notificationId == DosesAlarmTask.LUNCH_NOTIFICATION_ID || notificationId == DosesAlarmTask.DINNER_NOTIFICATION_ID || notificationId == DosesAlarmTask.EVENING_NOTIFICATION_ID) {

                int notificationCount = RescribePreferencesManager.getInt(RescribeConstants.NOTIFICATION_COUNT, mContext);
                if (notificationCount > 0)
                    RescribePreferencesManager.putInt(RescribeConstants.NOTIFICATION_COUNT, notificationCount - 1, mContext);

                Intent intentNotification = new Intent(mContext, NotificationActivity.class);
                intentNotification.putExtra(RescribeConstants.NOTIFICATION_ID, unreadMessNotificationID);
                intentNotification.putExtra(RescribeConstants.MEDICINE_SLOT, intent.getStringExtra(RescribeConstants.MEDICINE_SLOT));
                intentNotification.putExtra(RescribeConstants.NOTIFICATION_DATE, intent.getStringExtra(RescribeConstants.NOTIFICATION_DATE));
                intentNotification.putExtra(RescribeConstants.NOTIFICATION_TIME, intent.getStringExtra(RescribeConstants.NOTIFICATION_TIME));
                intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intentNotification);

                manager.cancel(MEDICATIONS_NOTIFICATION_TAG, notificationId);
            } else if (investigation_notification_id == InvestigationAlarmTask.INVESTIGATION_NOTIFICATION_ID) {

                ArrayList<InvestigationData> investigationData = intent.getParcelableArrayListExtra(RescribeConstants.INVESTIGATION_LIST);

                int invId = investigationData.get(0).getId();
                InvestigationHelper mInvestigationHelper = new InvestigationHelper(mContext, this);
                mInvestigationHelper.doSkipInvestigation(invId, false);

                AppDBHelper.getInstance(mContext).deleteUnreadReceivedNotificationMessage(String.valueOf(unreadMessNotificationID), RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT);
                int notificationCount = RescribePreferencesManager.getInt(RescribeConstants.NOTIFICATION_COUNT, mContext);
                if (notificationCount > 0)
                    RescribePreferencesManager.putInt(RescribeConstants.NOTIFICATION_COUNT, notificationCount - 1, mContext);

                // Skip notification and should come next day directly
//                RescribePreferencesManager.putBoolean(mContext.getString(R.string.investigation_alert), false, mContext); // It will turn off notification

                manager.cancel(INVESTIGATION_NOTIFICATION_TAG, investigationData.get(0).getDrId());

            } else if (appointment_notification_id == AppointmentAlarmTask.APPOINTMENT_NOTIFICATION_ID) {
                String action = (String) intent.getExtras().get(RescribeConstants.APPOINTMENT_MESSAGE);
                Toast.makeText(mContext, action + " " + "Not Accepted", Toast.LENGTH_SHORT).show();
                manager.cancel(APPOINTMENT_NOTIFICATION_TAG, appointment_notification_id);
            }
        } else {
            Intent intentNotification = new Intent(mContext, SplashScreenActivity.class);
            intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intentNotification);
        }

        // Collapse the notification bar.

        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        mContext.sendBroadcast(it);

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (RescribeConstants.TASK_DO_SKIP_INVESTIGATION.equals(mOldDataTag)) {
            CommonBaseModelContainer commonbject = (CommonBaseModelContainer) customResponse;
            CommonMethods.showToast(mContext, commonbject.getCommonRespose().getStatusMessage());
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
