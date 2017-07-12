package com.myrescribe.helpers.one_day_visit;

import android.content.Context;

import com.android.volley.Request;
import com.myrescribe.interfaces.ConnectionListener;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.prescription_response_model.PrescriptionModel;
import com.myrescribe.model.visit_details.Data;
import com.myrescribe.model.visit_details.VisitDetailsModel;
import com.myrescribe.network.ConnectRequest;
import com.myrescribe.network.ConnectionFactory;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.Config;
import com.myrescribe.util.MyRescribeConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeetal on 11/7/17.
 */

public class OneDayVisitHelper implements ConnectionListener {

    String TAG = "MyRescribe/PrescriptionHelper";
    Context mContext;
    HelperResponse mHelperResponseManager;

    public OneDayVisitHelper(Context context, HelperResponse oneDayVisitActivity) {
        this.mContext = context;
        this.mHelperResponseManager = oneDayVisitActivity;
    }


    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        //CommonMethods.Log(TAG, customResponse.toString());
        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag == MyRescribeConstants.TASK_ONE_DAY_VISIT) {
                    VisitDetailsModel model = (VisitDetailsModel) customResponse;
                    Data mData = model.getData();
                    mHelperResponseManager.onSuccess(mOldDataTag, mData);
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

    public void doGetOneDayVisit() {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, MyRescribeConstants.TASK_ONE_DAY_VISIT, Request.Method.GET, true);
        Map<String, String> testParams = new HashMap<String, String>();
        testParams.put(MyRescribeConstants.AUTHORIZATION_TOKEN, MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.AUTHTOKEN, mContext));
        mConnectionFactory.setHeaderParams(testParams);
        mConnectionFactory.setUrl(Config.ONE_DAY_VISIT_URL);
        mConnectionFactory.createConnection(MyRescribeConstants.TASK_ONE_DAY_VISIT);
    }
}

