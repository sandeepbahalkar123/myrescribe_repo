package com.rescribe.helpers.book_appointment;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.network.ConnectRequest;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jeetal on 19/9/17.
 */

public class DoctorDataHelper implements ConnectionListener {

    String TAG = this.getClass().getName();
    Context mContext;
    HelperResponse mHelperResponseManager;

    public DoctorDataHelper(Context context, HelperResponse servicesActivity) {
        this.mContext = context;
        this.mHelperResponseManager = servicesActivity;
    }

    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag == RescribeConstants.TASK_GET_DOCTOR_DATA) {
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

    public void doGetDoctorData() {
//        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_BOOK_APPOINTMENT_SERVICES, Request.Method.GET, true);
//        mConnectionFactory.setHeaderParams();
//        mConnectionFactory.setUrl(Config.SERVICES_URL);
//        mConnectionFactory.createConnection(RescribeConstants.TASK_BOOK_APPOINTMENT_SERVICES);
        try {
            InputStream is = mContext.getAssets().open("doctor_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Log.e(TAG, "doctor_data_book_appointment" + json);

            BookAppointmentBaseModel bookAppointmentBaseModel = new Gson().fromJson(json, BookAppointmentBaseModel.class);
            onResponse(ConnectionListener.RESPONSE_OK, bookAppointmentBaseModel, RescribeConstants.TASK_GET_DOCTOR_DATA);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
