package com.myrescribe.helpers.history;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.myrescribe.interfaces.ConnectionListener;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.history.PatientHistoryData;
import com.myrescribe.model.history.PatientHistoryMain;
import com.myrescribe.model.history.PatientHistoryModel;
import com.myrescribe.network.ConnectRequest;
import com.myrescribe.network.ConnectionFactory;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.Config;
import com.myrescribe.util.MyRescribeConstants;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by riteshpandhurkar on 1/3/17.
 */

public class HistoryHelper implements ConnectionListener {

    String TAG = this.getClass().getName();
    Context mContext;
    HelperResponse mHelperResponseManager;

    public HistoryHelper(Context context, HelperResponse activity) {
        this.mContext = context;
        this.mHelperResponseManager = activity;
    }


    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        //CommonMethods.Log(TAG, customResponse.toString());
        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag == MyRescribeConstants.TASK_HISTORY) {
                    PatientHistoryModel model = (PatientHistoryModel) customResponse;
                    PatientHistoryData historyDataInstance = model.getHistoryDataInstance();
                    mHelperResponseManager.onSuccess(mOldDataTag, historyDataInstance);
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
    public void doGetHistory() {
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
            InputStream is = mContext.getAssets().open("patient_history_list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Log.e(TAG, "doGetHistory" + json);

            PatientHistoryModel patientHistoryModel = new Gson().fromJson(json, PatientHistoryModel.class);
            onResponse(ConnectionListener.RESPONSE_OK, patientHistoryModel, MyRescribeConstants.TASK_HISTORY);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
