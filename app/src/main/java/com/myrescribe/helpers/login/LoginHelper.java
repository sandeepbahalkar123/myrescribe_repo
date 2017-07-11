package com.myrescribe.helpers.login;

import android.content.Context;

import com.android.volley.Request;
import com.myrescribe.interfaces.ConnectionListener;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.login.LoginModel;
import com.myrescribe.model.prescription_response_model.PatientPrescriptionModel;
import com.myrescribe.model.requestmodel.login.LoginRequestModel;
import com.myrescribe.network.ConnectRequest;
import com.myrescribe.network.ConnectionFactory;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.Config;
import com.myrescribe.util.MyRescribeConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ganeshshirole on 10/7/17.
 */

public class LoginHelper implements ConnectionListener{
    String TAG = "MyRescribe/LoginHelper";
    Context mContext;
    HelperResponse mHelperResponseManager;

    public LoginHelper(Context context, HelperResponse loginActivity) {
        this.mContext = context;
        this.mHelperResponseManager = loginActivity;
    }


    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        //CommonMethods.Log(TAG, customResponse.toString());
        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag.equals(MyRescribeConstants.TASK_LOGIN)) {
                    LoginModel loginModel = (LoginModel) customResponse;
                    mHelperResponseManager.onSuccess(mOldDataTag, loginModel);
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

    public void doLogin(String userName, String password) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, MyRescribeConstants.TASK_LOGIN, Request.Method.POST, false);
        Map<String, String> headerParams = new HashMap<String, String>();

        headerParams.put(MyRescribeConstants.CONTENT_TYPE, MyRescribeConstants.APPLICATION_JSON);
        mConnectionFactory.setHeaderParams(headerParams);

        LoginRequestModel loginRequestModel = new LoginRequestModel();
        loginRequestModel.setUsername(userName);
        loginRequestModel.setPassword(password);

        mConnectionFactory.setPostParams(loginRequestModel);

        mConnectionFactory.setUrl(Config.LOGIN_URL);
        mConnectionFactory.createConnection(MyRescribeConstants.TASK_LOGIN);
    }
}
