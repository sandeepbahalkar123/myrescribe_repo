package com.myrescribe.helpers.doctor;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.myrescribe.interfaces.ConnectionListener;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.doctors.DoctorModel;
import com.myrescribe.network.ConnectRequest;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * Created by riteshpandhurkar on 1/3/17.
 */

public class DoctorHelper implements ConnectionListener {

    String TAG = this.getClass().getName();
    Context mContext;
    HelperResponse mHelperResponseManager;

    public DoctorHelper(Context context, HelperResponse activity) {
        this.mContext = context;
        this.mHelperResponseManager = activity;
    }


    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        //CommonMethods.Log(TAG, customResponse.toString());
        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag == MyRescribeConstants.TASK_DOCTOR_LIST) {
                    DoctorModel model = (DoctorModel) customResponse;
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

    //TODO: This is done for temp purpose
    public void doGetDoctorList() {
     /*   ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, MyRescribeConstants.TASK_HISTORY, Request.Method.GET, true);
        Map<String, String> testParams = new HashMap<String, String>();

        testParams.put(MyRescribeConstants.AUTHORIZATION_TOKEN, "$1$7bSdP6L.$XQW6i4Tj2Z2WeTanfgp2y1");

        testParams.put(MyRescribeConstants.AUTH_KEY, "simplerestapi");
        testParams.put(MyRescribeConstants.CLIENT_SERVICE, "frontend-client");
        testParams.put(MyRescribeConstants.USER_ID, "18");
        mConnectionFactory.setHeaderParams(testParams);
        // mConnectionFactory.setPostParams(testParams);
        mConnectionFactory.setUrl(Config.PRESCRIPTION_URL);
        mConnectionFactory.createConnection(MyRescribeConstants.TASK_HISTORY);*/


        // TODO : HARDCODED JSON STRING PARSING FROM assets foler
        try {
            InputStream is = mContext.getAssets().open("doctor_list_update.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Log.e(TAG, "doGetHistory" + json);

            //  Type mapType = new TypeToken<Map<String, Map<String, Map<String, Map<String, String>>>>>() {
/*
            Type mapType = new TypeToken<DoctorModel>() {
            }.getType();*/

            DoctorModel doctorsModel = new Gson().fromJson(json, DoctorModel.class);

            CommonMethods.Log("doGetDoctorList", "" + doctorsModel.toString());
            onResponse(ConnectionListener.RESPONSE_OK, doctorsModel, MyRescribeConstants.TASK_DOCTOR_LIST);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
