package com.rescribe.helpers.login;

import android.content.Context;

import com.android.volley.Request;
import com.rescribe.R;
import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.login.ActiveRequest;
import com.rescribe.model.login.LoginModel;
import com.rescribe.model.login.SignUpModel;
import com.rescribe.model.requestmodel.login.LoginRequestModel;
import com.rescribe.model.requestmodel.login.SignUpRequestModel;
import com.rescribe.model.requestmodel.login.SignUpVerifyOTPRequestModel;
import com.rescribe.network.ConnectRequest;
import com.rescribe.network.ConnectionFactory;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.RescribeConstants;

/**
 * Created by ganeshshirole on 10/7/17.
 */

public class LoginHelper implements ConnectionListener {
    String TAG = this.getClass().getName();
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
                switch (mOldDataTag) {
                    case RescribeConstants.TASK_LOGIN:
                        LoginModel loginModel = (LoginModel) customResponse;
                        mHelperResponseManager.onSuccess(mOldDataTag, loginModel);
                        break;
                    case RescribeConstants.TASK_SIGN_UP:
                        SignUpModel signUpModel = (SignUpModel) customResponse;
                        mHelperResponseManager.onSuccess(mOldDataTag, signUpModel);
                        break;
                    case RescribeConstants.TASK_VERIFY_SIGN_UP_OTP:
                        mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                        break;
                    case RescribeConstants.TASK_LOGIN_WITH_PASSWORD:
                        mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                        break;
                    case RescribeConstants.TASK_LOGIN_WITH_OTP:
                        mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                        break;
                    case RescribeConstants.LOGOUT:
                        mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                        break;
                    case RescribeConstants.ACTIVE_STATUS:
                        mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                        break;
                }
                break;
            case ConnectionListener.PARSE_ERR0R:
                CommonMethods.Log(TAG, mContext.getString(R.string.parse_error));
                break;
            case ConnectionListener.SERVER_ERROR:
                CommonMethods.Log(TAG, mContext.getString(R.string.server_error));
                mHelperResponseManager.onServerError(mOldDataTag, mContext.getString(R.string.server_error));
                break;
            case ConnectionListener.NO_CONNECTION_ERROR:
                CommonMethods.Log(TAG,mContext.getString(R.string.no_connection_error));
                mHelperResponseManager.onNoConnectionError(mOldDataTag,mContext.getString(R.string.no_connection_error));
                break;
            default:
                CommonMethods.Log(TAG, mContext.getString(R.string.default_error));
                break;
        }
    }

    @Override
    public void onTimeout(ConnectRequest request) {

    }
     //Do login using mobileNo and password
    public void doLogin(String mobileNo,String password) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_LOGIN, Request.Method.POST, true);
        mConnectionFactory.setHeaderParams();
        LoginRequestModel loginRequestModel = new LoginRequestModel();
        loginRequestModel.setMobileNumber(mobileNo);
        loginRequestModel.setPassword(password);
        mConnectionFactory.setPostParams(loginRequestModel);
        mConnectionFactory.setUrl(Config.LOGIN_URL);
        mConnectionFactory.createConnection(RescribeConstants.TASK_LOGIN);
    }
   //Do login using Otp
    public void doLoginByOTP(String otp) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_LOGIN_WITH_OTP, Request.Method.POST, true);
        mConnectionFactory.setHeaderParams();
        LoginRequestModel loginRequestModel = new LoginRequestModel();
        loginRequestModel.setMobileNumber(otp);
        mConnectionFactory.setPostParams(loginRequestModel);
        mConnectionFactory.setUrl(Config.LOGIN_WITH_OTP_URL);
        mConnectionFactory.createConnection(RescribeConstants.TASK_LOGIN_WITH_OTP);
    }
    //Verify Otp sent
    public void doVerifyGeneratedSignUpOTP(SignUpVerifyOTPRequestModel requestModel) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_VERIFY_SIGN_UP_OTP, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(requestModel);
        mConnectionFactory.setUrl(Config.VERIFY_SIGN_UP_OTP);
        mConnectionFactory.createConnection(RescribeConstants.TASK_VERIFY_SIGN_UP_OTP);
    }

  //SignUp
    public void doSignUp(SignUpRequestModel signUpRequestModel) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_SIGN_UP, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(signUpRequestModel);
        mConnectionFactory.setUrl(Config.SIGN_UP_URL);
        mConnectionFactory.createConnection(RescribeConstants.TASK_SIGN_UP);
    }

    // Logout
    public void doLogout(ActiveRequest activeRequest) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.LOGOUT, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(activeRequest);
        mConnectionFactory.setUrl(Config.LOGOUT);
        mConnectionFactory.createConnection(RescribeConstants.LOGOUT);
    }

    // ActiveStatus
    public void doActiveStatus(ActiveRequest activeRequest) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.ACTIVE_STATUS, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(activeRequest);
        mConnectionFactory.setUrl(Config.ACTIVE);
        mConnectionFactory.createConnection(RescribeConstants.ACTIVE_STATUS);
    }
}
