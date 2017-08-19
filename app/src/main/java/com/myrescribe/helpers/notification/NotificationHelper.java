package com.myrescribe.helpers.notification;

import android.content.Context;

import com.android.volley.Request;
import com.myrescribe.interfaces.ConnectionListener;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.notification.NotificationModel;
import com.myrescribe.network.ConnectRequest;
import com.myrescribe.network.ConnectionFactory;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.Config;
import com.myrescribe.util.MyRescribeConstants;

/**
 * Created by jeetal on 20/7/17.
 */

public class NotificationHelper implements ConnectionListener {

    String TAG = "MyRescribe/NotificationHelper";
    Context mContext;
    HelperResponse mHelperResponseManager;

    public NotificationHelper(Context context) {
        this.mContext = context;
        this.mHelperResponseManager = (HelperResponse) context;
    }


    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        //CommonMethods.Log(TAG, customResponse.toString());
        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag == MyRescribeConstants.TASK_NOTIFICATION) {
                    NotificationModel model = (NotificationModel) customResponse;
                    mHelperResponseManager.onSuccess(mOldDataTag, model);
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

    public void doGetNotificationList() {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, false, MyRescribeConstants.TASK_NOTIFICATION, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.NOTIFICATION_URL + MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext));
        mConnectionFactory.createConnection(MyRescribeConstants.TASK_NOTIFICATION);
    }


}
