package com.myrescribe.broadcast_receivers;

import android.content.Context;
import android.util.Log;

import com.myrescribe.helpers.database.AppDBHelper;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

public class FileUploadReceiver extends UploadServiceBroadcastReceiver {
    @Override
    public void onProgress(Context context, UploadInfo uploadInfo) {
        CommonMethods.Log("ImagedUploadIdHome", uploadInfo.getUploadId() + " onProgress " + uploadInfo.getProgressPercent());
    }

    @Override
    public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
        AppDBHelper.getInstance(context).updateMyRecordsData(uploadInfo.getUploadId(), MyRescribeConstants.FAILED);
        CommonMethods.Log("ImagedUploadIdHome", uploadInfo.getUploadId() + " onError");
    }

    @Override
    public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
        AppDBHelper.getInstance(context).updateMyRecordsData(uploadInfo.getUploadId(), MyRescribeConstants.COMPLETED);
        CommonMethods.Log("ImagedUploadIdHome", uploadInfo.getUploadId() + " onCompleted");
    }

    @Override
    public void onCancelled(Context context, UploadInfo uploadInfo) {
        CommonMethods.Log("ImagedUploadIdHome", uploadInfo.getUploadId() + " onCancelled");
    }
}