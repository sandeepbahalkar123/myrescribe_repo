package com.rescribe.helpers.single_visit_details;

import android.content.Context;

import com.android.volley.Request;
import com.rescribe.R;
import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.case_details.CaseDetailsModel;
import com.rescribe.network.ConnectRequest;
import com.rescribe.network.ConnectionFactory;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.RescribeConstants;


/**
 * Created by jeetal on 11/7/17.
 */

public class SingleVisitDetailHelper implements ConnectionListener {

    private String TAG = this.getClass().getName();
    private Context mContext;
    private HelperResponse mHelperResponseManager;

    public SingleVisitDetailHelper(Context context, HelperResponse oneDayVisitActivity) {
        this.mContext = context;
        this.mHelperResponseManager = oneDayVisitActivity;
    }


    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag.equals(RescribeConstants.TASK_ONE_DAY_VISIT)) {
                    CaseDetailsModel model = (CaseDetailsModel) customResponse;
                    mHelperResponseManager.onSuccess(mOldDataTag, model.getData());
                }
                break;
            case ConnectionListener.PARSE_ERR0R:
                CommonMethods.Log(TAG, mContext.getString(R.string.parse_error));
                mHelperResponseManager.onParseError(mOldDataTag, mContext.getString(R.string.parse_error));
                break;
            case ConnectionListener.SERVER_ERROR:
                CommonMethods.Log(TAG, mContext.getString(R.string.server_error));
                mHelperResponseManager.onServerError(mOldDataTag, mContext.getString(R.string.server_error));
                break;
            case ConnectionListener.NO_INTERNET:
                CommonMethods.Log(TAG, mContext.getString(R.string.no_connection_error));
                mHelperResponseManager.onNoConnectionError(mOldDataTag, mContext.getString(R.string.no_connection_error));
                break;
            case ConnectionListener.NO_CONNECTION_ERROR:
                CommonMethods.Log(TAG, mContext.getString(R.string.no_connection_error));
                mHelperResponseManager.onNoConnectionError(mOldDataTag, mContext.getString(R.string.no_connection_error));
                break;
            default:
                CommonMethods.Log(TAG, mContext.getString(R.string.default_error));
                break;
        }
    }

    @Override
    public void onTimeout(ConnectRequest request) {

    }

    //get case study list
    public void doGetOneDayVisit(String opdId, String doctorId) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_ONE_DAY_VISIT, Request.Method.GET, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.ONE_DAY_VISIT_URL + opdId + "&docId=" + doctorId + "&patientId=" + RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PATIENT_ID, mContext));
        mConnectionFactory.createConnection(RescribeConstants.TASK_ONE_DAY_VISIT);
    }
}

