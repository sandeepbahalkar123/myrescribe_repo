package com.myrescribe.helpers.prescription;

import android.content.Context;

import com.android.volley.Request;
import com.myrescribe.interfaces.ConnectionListener;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.prescription_response_model.PatientPrescriptionModel;
import com.myrescribe.network.ConnectRequest;
import com.myrescribe.network.ConnectionFactory;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.Config;
import com.myrescribe.util.MyRescribeConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by riteshpandhurkar on 1/3/17.
 */

public class PrescriptionHelper implements ConnectionListener {

    String TAG = "MyRescribe/PrescriptionHelper";
    Context mContext;
    HelperResponse mHelperResponseManager;

    public PrescriptionHelper(Context context, HelperResponse loginActivity1) {
        this.mContext = context;
        this.mHelperResponseManager = loginActivity1;
    }


    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        //CommonMethods.Log(TAG, customResponse.toString());
        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag == MyRescribeConstants.TASK_PRESCRIPTION_LIST) {
                    PatientPrescriptionModel model = (PatientPrescriptionModel) customResponse;
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

    public void doGetPrescriptionList() {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, MyRescribeConstants.TASK_PRESCRIPTION_LIST, Request.Method.GET, true);
        Map<String, String> testParams = new HashMap<String, String>();

        testParams.put(MyRescribeConstants.AUTHORIZATION_TOKEN,"$1$H.4rlsUn$wXeAO5BuxnVxnTpfcgb4m1");

        testParams.put(MyRescribeConstants.AUTH_KEY, "simplerestapi");
        testParams.put(MyRescribeConstants.CLIENT_SERVICE, "frontend-client");
        testParams.put(MyRescribeConstants.USER_ID, "7");
        mConnectionFactory.setHeaderParams(testParams);
        // mConnectionFactory.setPostParams(testParams);
        mConnectionFactory.setUrl(Config.PRESCRIPTION_URL);
        mConnectionFactory.createConnection(MyRescribeConstants.TASK_PRESCRIPTION_LIST);
    }

}
