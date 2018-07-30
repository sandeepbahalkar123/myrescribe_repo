package com.rescribe.helpers.doctor_connect;

import android.content.Context;

import com.android.volley.Request;
import com.rescribe.R;
import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.network.ConnectRequest;
import com.rescribe.network.ConnectionFactory;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.RescribeConstants;

/**
 * Created by jeetal on 6/9/17.
 */

public class DoctorConnectSearchHelper implements ConnectionListener {

    private String TAG = this.getClass().getName();
    private Context mContext;
    private HelperResponse mHelperResponseManager;

    public DoctorConnectSearchHelper(Context context, HelperResponse doctorConnectSearchActivity) {
        this.mContext = context;
        this.mHelperResponseManager = doctorConnectSearchActivity;
    }


    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag.equals(RescribeConstants.TASK_DOCTOR__FILTER_DOCTOR_SPECIALITY_LIST)) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                }
                break;
            case ConnectionListener.PARSE_ERR0R:
                CommonMethods.Log(TAG, mContext.getString(R.string.parse_error));
                ((HelperResponse) mContext).onParseError(mOldDataTag, mContext.getString(R.string.parse_error));
                break;
            case ConnectionListener.SERVER_ERROR:
                CommonMethods.Log(TAG, mContext.getString(R.string.server_error));
                ((HelperResponse) mContext).onServerError(mOldDataTag, mContext.getString(R.string.server_error));

                break;
            case ConnectionListener.NO_CONNECTION_ERROR:
                CommonMethods.Log(TAG, mContext.getString(R.string.no_connection_error));
                ((HelperResponse) mContext).onNoConnectionError(mOldDataTag, mContext.getString(R.string.no_connection_error));
                break;
            case ConnectionListener.NO_INTERNET:
                CommonMethods.Log(TAG, mContext.getString(R.string.no_connection_error));
                ((HelperResponse) mContext).onNoConnectionError(mOldDataTag, mContext.getString(R.string.no_connection_error));
                break;
            default:
                CommonMethods.Log(TAG, mContext.getString(R.string.default_error));
                break;
        }
    }

    @Override
    public void onTimeout(ConnectRequest request) {

    }

    public void getDoctorSpecialityList() {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_DOCTOR__FILTER_DOCTOR_SPECIALITY_LIST, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.FILTER_DOCTOR_SPECIALIST_LIST);
        mConnectionFactory.createConnection(RescribeConstants.TASK_DOCTOR__FILTER_DOCTOR_SPECIALITY_LIST);
    }

}


