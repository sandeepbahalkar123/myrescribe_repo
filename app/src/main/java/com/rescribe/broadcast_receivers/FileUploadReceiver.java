package com.rescribe.broadcast_receivers;

import android.content.Context;

import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

public class FileUploadReceiver extends UploadServiceBroadcastReceiver {
    AppDBHelper instance;

    @Override
    public void onProgress(Context context, UploadInfo uploadInfo) {
        CommonMethods.Log("ImagedUploadIdHome", uploadInfo.getUploadId() + " onProgress " + uploadInfo.getProgressPercent());
    }

    @Override
    public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

        if (instance == null)
            instance = new AppDBHelper(context);

        instance.updateMyRecordsData(uploadInfo.getUploadId(), RescribeConstants.FAILED);
        CommonMethods.Log("ImagedUploadIdHome", uploadInfo.getUploadId() + " onError");
    }

    @Override
    public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
        if (instance == null)
            instance = new AppDBHelper(context);

        instance.updateMyRecordsData(uploadInfo.getUploadId(), RescribeConstants.COMPLETED);
        CommonMethods.Log("ImagedUploadIdHome", uploadInfo.getUploadId() + " onCompleted");
    }

    @Override
    public void onCancelled(Context context, UploadInfo uploadInfo) {
        CommonMethods.Log("ImagedUploadIdHome", uploadInfo.getUploadId() + " onCancelled");
    }
}