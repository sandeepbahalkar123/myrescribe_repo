package com.rescribe.helpers.dashboard;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.dashboard.DashboardBaseModel;
import com.rescribe.model.doctors.appointments.DoctorAppointmentModel;
import com.rescribe.model.doctors.doctor_info.DoctorBaseModel;
import com.rescribe.model.doctors.doctor_info.DoctorDataModel;
import com.rescribe.model.doctors.doctor_info.DoctorDetail;
import com.rescribe.model.doctors.doctor_info.DoctorInfoMonthContainer;
import com.rescribe.model.filter.filter_request.DrFilterRequestModel;
import com.rescribe.network.ConnectRequest;
import com.rescribe.network.ConnectionFactory;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.RescribeConstants;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by riteshpandhurkar on 1/3/17.
 */

public class DashboardHelper implements ConnectionListener {

    String TAG = this.getClass().getName();
    Context mContext;
    HelperResponse mHelperResponseManager;
    private Map<String, Map<String, ArrayList<DoctorDetail>>> yearWiseSortedDoctorList = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public DashboardHelper(Context context) {
        this.mContext = context;
        this.mHelperResponseManager = (HelperResponse) context;
    }

    public DashboardHelper(Context context, HelperResponse loginActivity) {
        this.mContext = context;
        this.mHelperResponseManager = loginActivity;
    }

    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        //CommonMethods.Log(TAG, customResponse.toString());
        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
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

    public void doGetDashboard() {
        try {
            InputStream is = mContext.getAssets().open("dashboard.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Log.e(TAG, "dashboard" + json);

            DashboardBaseModel model = new Gson().fromJson(json, DashboardBaseModel.class);
            onResponse(ConnectionListener.RESPONSE_OK, model, RescribeConstants.TASK_DASHBOARD_API);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
