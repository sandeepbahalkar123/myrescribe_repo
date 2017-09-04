package com.rescribe.helpers.notification;

import android.content.Context;

import com.android.volley.Request;
import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.response_model_notification.ResponseLogNotificationModel;
import com.rescribe.model.response_model_notification.ResponseNotificationModel;
import com.rescribe.network.ConnectRequest;
import com.rescribe.network.ConnectionFactory;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.RescribeConstants;

/**
 * Created by jeetal on 20/7/17.
 */

public class RespondToNotificationHelper implements ConnectionListener {

    String TAG = this.getClass().getName();
    Context mContext;
    HelperResponse mHelperResponseManager;

    public RespondToNotificationHelper(Context context, HelperResponse respondNotificationActivity) {
        this.mContext = context;
        this.mHelperResponseManager = respondNotificationActivity;
    }


    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        //CommonMethods.Log(TAG, customResponse.toString());
        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag.startsWith(RescribeConstants.TASK_RESPOND_NOTIFICATION)) {
                    ResponseLogNotificationModel responseLogNotificationModel = (ResponseLogNotificationModel) customResponse;
                    mHelperResponseManager.onSuccess(mOldDataTag, responseLogNotificationModel);

                }else if(mOldDataTag.startsWith(RescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER)){
                    ResponseLogNotificationModel responseLogNotificationModel = (ResponseLogNotificationModel) customResponse;
                    mHelperResponseManager.onSuccess(mOldDataTag, responseLogNotificationModel);
                }else if(mOldDataTag.startsWith(RescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER_ADAPTER)){
                    ResponseLogNotificationModel responseLogNotificationModel = (ResponseLogNotificationModel) customResponse;
                    mHelperResponseManager.onSuccess(mOldDataTag, responseLogNotificationModel);
                }else if(mOldDataTag.startsWith(RescribeConstants.TASK_RESPOND_NOTIFICATION_ADAPTER)){
                    ResponseLogNotificationModel responseLogNotificationModel = (ResponseLogNotificationModel) customResponse;
                    mHelperResponseManager.onSuccess(mOldDataTag, responseLogNotificationModel);
                }
                break;
            case ConnectionListener.PARSE_ERR0R:
                CommonMethods.Log(TAG, "parse error");
                mHelperResponseManager.onParseError(mOldDataTag, "parse error");
                break;
            case ConnectionListener.SERVER_ERROR:
                CommonMethods.Log(TAG, "server error");
                mHelperResponseManager.onServerError(mOldDataTag, "server error");
                break;
            case ConnectionListener.NO_INTERNET:
                CommonMethods.Log(TAG, "no connection error");
                mHelperResponseManager.onNoConnectionError(mOldDataTag, "no connection error");
                break;
            case ConnectionListener.NO_CONNECTION_ERROR:
                CommonMethods.Log(TAG, "no connection error");
                mHelperResponseManager.onNoConnectionError(mOldDataTag, "no connection error");
                break;
            default:
                CommonMethods.Log(TAG, "default error");
                break;
        }
    }

    @Override
    public void onTimeout(ConnectRequest request) {

    }

  // for click of checkbox in sublist , whether medicine taken or not , isBundle = 0 .i.e. respond to medicne in the list one at time
    public void doRespondToNotification(Integer patientID,String slot,Integer medicineId,String takenDate,Integer isBundle,String pos) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, false, pos, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        ResponseNotificationModel responseNotificationModel = new ResponseNotificationModel();
        responseNotificationModel.setPatientId(patientID);
        responseNotificationModel.setSlot(slot);
        responseNotificationModel.setMedicineId(medicineId);
        responseNotificationModel.setTakenDate(takenDate);
        responseNotificationModel.setIsBundle(isBundle);
        mConnectionFactory.setPostParams(responseNotificationModel);
        mConnectionFactory.setUrl(Config.RESPOND_TO_NOTIFICATION_URL);
        mConnectionFactory.createConnection(pos);
    }
    // for click of checkbox in header  , whether bundle of medicines of particular slot taken or not i.e. isBundle = 1
    public void doRespondToNotificationForHeader(Integer patientID,String slot,Integer medicineId,String takenDate,Integer isBundle,String pos) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, false, pos, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        ResponseNotificationModel responseNotificationModel = new ResponseNotificationModel();
        responseNotificationModel.setPatientId(patientID);
        responseNotificationModel.setSlot(slot);
        responseNotificationModel.setMedicineId(medicineId);
        responseNotificationModel.setTakenDate(takenDate);
        responseNotificationModel.setIsBundle(isBundle);
        mConnectionFactory.setPostParams(responseNotificationModel);
        mConnectionFactory.setUrl(Config.RESPOND_TO_NOTIFICATION_URL);
        mConnectionFactory.createConnection(pos);
    }

    public void doRespondToNotificationForNotificationAdapter(Integer patientID,String slot,Integer medicineId,String takenDate,Integer isBundle,String pos) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, false, pos, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        ResponseNotificationModel responseNotificationModel = new ResponseNotificationModel();
        responseNotificationModel.setPatientId(patientID);
        responseNotificationModel.setSlot(slot);
        responseNotificationModel.setMedicineId(medicineId);
        responseNotificationModel.setTakenDate(takenDate);
        responseNotificationModel.setIsBundle(isBundle);
        mConnectionFactory.setPostParams(responseNotificationModel);
        mConnectionFactory.setUrl(Config.RESPOND_TO_NOTIFICATION_URL);
        mConnectionFactory.createConnection(pos);
    }
    public void doRespondToNotificationForHeaderOfNotificationAdapter(Integer patientID,String slot,Integer medicineId,String takenDate,Integer isBundle,String pos) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, false, pos, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        ResponseNotificationModel responseNotificationModel = new ResponseNotificationModel();
        responseNotificationModel.setPatientId(patientID);
        responseNotificationModel.setSlot(slot);
        responseNotificationModel.setMedicineId(medicineId);
        responseNotificationModel.setTakenDate(takenDate);
        responseNotificationModel.setIsBundle(isBundle);
        mConnectionFactory.setPostParams(responseNotificationModel);
        mConnectionFactory.setUrl(Config.RESPOND_TO_NOTIFICATION_URL);
        mConnectionFactory.createConnection(pos);
    }


}