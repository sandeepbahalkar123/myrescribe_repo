package com.myrescribe.helpers.one_day_visit;

import android.content.Context;

import com.android.volley.Request;
import com.myrescribe.R;
import com.myrescribe.interfaces.ConnectionListener;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.case_details.CaseDetailsModel;
import com.myrescribe.network.ConnectRequest;
import com.myrescribe.network.ConnectionFactory;

import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.Config;
import com.myrescribe.util.MyRescribeConstants;


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

        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag == MyRescribeConstants.TASK_ONE_DAY_VISIT) {
                    CaseDetailsModel model = (CaseDetailsModel) customResponse;
                    mHelperResponseManager.onSuccess(mOldDataTag, model.getData());
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

    public void doGetOneDayVisit(String opdId) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, MyRescribeConstants.TASK_ONE_DAY_VISIT, Request.Method.GET, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.ONE_DAY_VISIT_URL+opdId+mContext.getString(R.string.and_sign)+MyRescribeConstants.PATIENT_ID+ MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATIENT_ID,mContext));
        mConnectionFactory.createConnection(MyRescribeConstants.TASK_ONE_DAY_VISIT);
    }
}

