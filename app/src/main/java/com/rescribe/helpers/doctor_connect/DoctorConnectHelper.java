package com.rescribe.helpers.doctor_connect;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.doctor_connect.DoctorConnectBaseModel;
import com.rescribe.network.ConnectRequest;
import com.rescribe.network.ConnectionFactory;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.RescribeConstants;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jeetal on 6/9/17.
 */

public class DoctorConnectHelper implements ConnectionListener {

    String TAG = this.getClass().getName();
    Context mContext;
    HelperResponse mHelperResponseManager;

    public DoctorConnectHelper(Context context) {
        this.mContext = context;
        this.mHelperResponseManager = (HelperResponse) context;
    }

    public DoctorConnectHelper(Context context, HelperResponse doctorConnectActivity) {
        this.mContext = context;
        this.mHelperResponseManager = doctorConnectActivity;
    }

    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        //CommonMethods.Log(TAG, customResponse.toString());
        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag == RescribeConstants.TASK_DOCTOR_CONNECT) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
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

    public void doDoctorConnecList() {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_DOCTOR_CONNECT, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.DOCTOR_CHAT_LIST_URL);
        mConnectionFactory.createConnection(RescribeConstants.TASK_DOCTOR_CONNECT);
      /*  try {
            InputStream is = mContext.getAssets().open("doctor_connect.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Log.e(TAG, "doDoctorConnecList" + json);

            DoctorConnectBaseModel doctorConnectBaseModel = new Gson().fromJson(json, DoctorConnectBaseModel.class);
            onResponse(ConnectionListener.RESPONSE_OK, doctorConnectBaseModel, RescribeConstants.TASK_DOCTOR_CONNECT);

        } catch (IOException ex) {
            ex.printStackTrace();
        }*/
    }

}
