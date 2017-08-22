package com.rescribe.helpers.notification;

import android.content.Context;

import com.android.volley.Request;
import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.network.ConnectRequest;
import com.rescribe.network.ConnectionFactory;
import com.rescribe.preference.MyRescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.MyRescribeConstants;

/**
 * Created by riteshpandhurkar on 1/3/17.
 */

public class AppointmentHelper implements ConnectionListener {

    private final String patient_id;
    private String TAG = this.getClass().getName();
    private Context mContext;

    public AppointmentHelper(Context context) {
        this.mContext = context;
        patient_id = MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext);
    }


    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                CommonMethods.Log(TAG, customResponse.getClass() + " success");
                ((HelperResponse) mContext).onSuccess(mOldDataTag, customResponse);
                break;
            case ConnectionListener.PARSE_ERR0R:
                CommonMethods.Log(TAG, "parse error");
                ((HelperResponse) mContext).onParseError(mOldDataTag, "parse error");
                break;
            case ConnectionListener.SERVER_ERROR:
                CommonMethods.Log(TAG, "server error");
                ((HelperResponse) mContext).onServerError(mOldDataTag, "server error");
                break;
            case ConnectionListener.NO_CONNECTION_ERROR:
                CommonMethods.Log(TAG, "no connection error");
                ((HelperResponse) mContext).onNoConnectionError(mOldDataTag, "no connection error");
                break;
            default:
                CommonMethods.Log(TAG, "default error");
                break;
        }
    }

    @Override
    public void onTimeout(ConnectRequest request) {

    }

    public void getDoctorList() {
        if (!patient_id.equals("")) {
            ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, false, MyRescribeConstants.APPOINTMENT_NOTIFICATION, Request.Method.GET, true);
            mConnectionFactory.setHeaderParams();
            mConnectionFactory.setUrl(Config.APPOINTMENTS + "?patientId=" + patient_id + "&status=Upcoming");
            mConnectionFactory.createConnection(MyRescribeConstants.APPOINTMENT_NOTIFICATION);
        }
    }

}
