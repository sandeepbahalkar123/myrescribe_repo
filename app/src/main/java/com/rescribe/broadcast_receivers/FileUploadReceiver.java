package com.rescribe.broadcast_receivers;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.model.chat.MQTTMessage;
import com.rescribe.model.chat.uploadfile.ChatFileUploadModel;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.services.MQTTService;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

import static com.rescribe.broadcast_receivers.ReplayBroadcastReceiver.MESSAGE_LIST;
import static com.rescribe.services.MQTTService.SEND_MESSAGE;

public class FileUploadReceiver extends UploadServiceBroadcastReceiver {
    AppDBHelper instance;
    Gson gson = new Gson();

    @Override
    public void onProgress(Context context, UploadInfo uploadInfo) {
        CommonMethods.Log("ImagedUploadIdHome", uploadInfo.getUploadId() + " onProgress " + uploadInfo.getProgressPercent());
    }

    @Override
    public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

        if (instance == null)
            instance = new AppDBHelper(context);

        String patientId = RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PATIENT_ID, context);

        String prefix[] = uploadInfo.getUploadId().split("_");
        if (prefix[0].equals(patientId)) {
                instance.updateMessageUpload(uploadInfo.getUploadId(), RescribeConstants.FAILED);
            } else {
                instance.updateMyRecordsData(uploadInfo.getUploadId(), RescribeConstants.FAILED);
            }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(uploadInfo.getNotificationID());
        CommonMethods.Log("ImagedUploadIdHome", uploadInfo.getUploadId() + " onError");
    }

    @Override
    public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
        if (instance == null)
            instance = new AppDBHelper(context);

        String patientId = RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PATIENT_ID, context);

        String prefix[] = uploadInfo.getUploadId().split("_");
        if (prefix[0].equals(patientId)) {

                MQTTMessage mqttMessage = instance.getMessageUploadById(uploadInfo.getUploadId());

                String response = serverResponse.getBodyAsString();
                ChatFileUploadModel chatFileUploadModel = gson.fromJson(response, ChatFileUploadModel.class);

                String fileUrl = chatFileUploadModel.getData().getDocUrl();
                // send via mqtt
                if (mqttMessage != null) {

                    mqttMessage.setFileUrl(fileUrl);

                    Intent intentService = new Intent(context, MQTTService.class);
                    intentService.putExtra(SEND_MESSAGE, true);
                    intentService.putExtra(MESSAGE_LIST, mqttMessage);
                    context.startService(intentService);
                }
                instance.updateMessageUpload(uploadInfo.getUploadId(), RescribeConstants.COMPLETED);
            } else {
                instance.updateMyRecordsData(uploadInfo.getUploadId(), RescribeConstants.COMPLETED);
            }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(uploadInfo.getNotificationID());

        CommonMethods.Log("ImagedUploadIdHome", uploadInfo.getUploadId() + " onCompleted");
    }

    @Override
    public void onCancelled(Context context, UploadInfo uploadInfo) {
        CommonMethods.Log("ImagedUploadIdHome", uploadInfo.getUploadId() + " onCancelled");
    }
}