package com.myrescribe.helpers.notification;

import android.content.Context;

import com.android.volley.Request;
import com.myrescribe.interfaces.ConnectionListener;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.response_model_notification.ResponseLogNotificationModel;
import com.myrescribe.model.response_model_notification.ResponseNotificationModel;
import com.myrescribe.network.ConnectRequest;
import com.myrescribe.network.ConnectionFactory;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.Config;
import com.myrescribe.util.MyRescribeConstants;

/**
 * Created by jeetal on 20/7/17.
 */

public class RespondToNotificationHelper implements ConnectionListener {

    String TAG = "MyRescribe/RespondToNotificationHelper";
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
                if (mOldDataTag == MyRescribeConstants.TASK_RESPOND_NOTIFICATION) {

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


    public void doRespondToNotification(Integer patientID,String slot,Integer medicineId,String takenDate,Integer isBundle) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, false, MyRescribeConstants.TASK_RESPOND_NOTIFICATION, Request.Method.POST, true);
        mConnectionFactory.setHeaderParams();
        ResponseNotificationModel responseNotificationModel = new ResponseNotificationModel();
        responseNotificationModel.setPatientId(patientID);
        responseNotificationModel.setSlot(slot);
        responseNotificationModel.setMedicineId(medicineId);
        responseNotificationModel.setTakenDate(takenDate);
        responseNotificationModel.setIsBundle(isBundle);
        mConnectionFactory.setPostParams(responseNotificationModel);
        mConnectionFactory.setUrl(Config.RESPOND_TO_NOTIFICATION_URL);
        mConnectionFactory.createConnection(MyRescribeConstants.TASK_RESPOND_NOTIFICATION);
    }


}
