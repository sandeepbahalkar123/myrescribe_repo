package com.rescribe.helpers.notification;

import android.content.Context;

import com.android.volley.Request;
import com.rescribe.R;
import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.network.ConnectRequest;
import com.rescribe.network.ConnectionFactory;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.RescribeConstants;

/**
 * Created by riteshpandhurkar on 1/3/17.
 */

public class AppointmentHelper implements ConnectionListener {

    private final String patient_id;
    private String TAG = this.getClass().getName();
    private Context mContext;

    public AppointmentHelper(Context context) {
        this.mContext = context;
        patient_id = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext);
    }


    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                CommonMethods.Log(TAG, customResponse.getClass() + " success");
                ((HelperResponse) mContext).onSuccess(mOldDataTag, customResponse);
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
                CommonMethods.Log(TAG,mContext.getString(R.string.no_connection_error));
                ((HelperResponse) mContext).onNoConnectionError(mOldDataTag,mContext.getString(R.string.no_connection_error));
                break;
            default:
                CommonMethods.Log(TAG, mContext.getString(R.string.default_error));
                break;
        }
    }

    @Override
    public void onTimeout(ConnectRequest request) {

    }

    public void getDoctorList() {
        if (!patient_id.equals("")) {

            String time = CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.HH_mm);
            String date = CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.YYYY_MM_DD);

            ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, false, RescribeConstants.APPOINTMENT_NOTIFICATION, Request.Method.GET, false);
            mConnectionFactory.setHeaderParams();
            mConnectionFactory.setUrl(Config.APPOINTMENTS + "?patientId=" + patient_id + "&status=Upcoming&date=" + date + "&time=" + time);
            mConnectionFactory.createConnection(RescribeConstants.APPOINTMENT_NOTIFICATION);
        }
    }

}
