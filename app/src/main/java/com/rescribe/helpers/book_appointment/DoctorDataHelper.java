package com.rescribe.helpers.book_appointment;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.ServicesModel;
import com.rescribe.model.book_appointment.complaints.ComplaintsBaseModel;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.model.book_appointment.doctor_data.RequestFavouriteDoctorModel;
import com.rescribe.model.book_appointment.filterdrawer.BookAppointFilterBaseModel;
import com.rescribe.network.ConnectRequest;
import com.rescribe.network.ConnectionFactory;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.RescribeConstants;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by jeetal on 19/9/17.
 */

public class DoctorDataHelper implements ConnectionListener {

    String TAG = this.getClass().getName();
    Context mContext;
    HelperResponse mHelperResponseManager;
    private static HashMap<String, String> userSelectedLocationInfo = new HashMap<>();

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
                } else if (mOldDataTag == RescribeConstants.TASK_GET_COMPLAINTS) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                } else if (mOldDataTag == RescribeConstants.TASK_GET_BOOK_APPOINT_DRAWER_CONFIG) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                } else if (mOldDataTag == RescribeConstants.TASK_GET_REVIEW_LIST) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                }else  if (mOldDataTag == RescribeConstants.TASK_BOOK_APPOINTMENT_SERVICES) {
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

    public void doGetDoctorData(/*String city, String address*/) {
      /*  ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_GET_DOCTOR_DATA, Request.Method.POST, true);
        mConnectionFactory.setHeaderParams();
        RequestDoctorListBaseModel requestDoctorListBaseModel = new RequestDoctorListBaseModel();
        requestDoctorListBaseModel.setArea(address);
        requestDoctorListBaseModel.setCityName(city);
        requestDoctorListBaseModel.setPatientId(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext)));
        mConnectionFactory.setPostParams(requestDoctorListBaseModel);
        mConnectionFactory.setUrl(Config.DOCTOR_LIST_BY_LOCATION);
        mConnectionFactory.createConnection(RescribeConstants.TASK_GET_DOCTOR_DATA);*/
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

    public void doGetDrawerFilterConfigurationData() {
        try {
            InputStream is = mContext.getAssets().open("book_appointment_filter_drawer.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Log.e(TAG, "book_appointment_filter_drawer" + json);

            BookAppointFilterBaseModel bookAppointmentBaseModel = new Gson().fromJson(json, BookAppointFilterBaseModel.class);
            onResponse(ConnectionListener.RESPONSE_OK, bookAppointmentBaseModel, RescribeConstants.TASK_GET_BOOK_APPOINT_DRAWER_CONFIG);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void doGetComplaintsList() {
        try {
            InputStream is = mContext.getAssets().open("complaint_list");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Log.e(TAG, "complaint_list" + json);

            ComplaintsBaseModel complaintsBaseModel = new Gson().fromJson(json, ComplaintsBaseModel.class);
            onResponse(ConnectionListener.RESPONSE_OK, complaintsBaseModel, RescribeConstants.TASK_GET_COMPLAINTS);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void doGetReviewsList(String docId) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_GET_REVIEW_LIST, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.REVIEW_URL);
        mConnectionFactory.createConnection(RescribeConstants.TASK_GET_REVIEW_LIST);
    }

    public void setFavouriteDoctor(Boolean isFavourite, String docId) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_SET_FAVOURITE_DOCTOR, Request.Method.POST, true);
        RequestFavouriteDoctorModel requestFavouriteDoctorModel = new RequestFavouriteDoctorModel();
        requestFavouriteDoctorModel.setPatientId(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext)));
        requestFavouriteDoctorModel.setFavouriteflag(isFavourite);
        requestFavouriteDoctorModel.setDoctorId(Integer.valueOf(docId));
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.SET_FAVOURITE_URL);
        mConnectionFactory.createConnection(RescribeConstants.TASK_SET_FAVOURITE_DOCTOR);
    }

    public static HashMap<String, String> getUserSelectedLocationInfo() {
        return userSelectedLocationInfo;
    }

    public static void setUserSelectedLocationInfo(Context ctx, LatLng data, String locationText) {
        DoctorDataHelper.userSelectedLocationInfo.put(ctx.getString(R.string.location), locationText);
        DoctorDataHelper.userSelectedLocationInfo.put(ctx.getString(R.string.latitude), "" + data.latitude);
        DoctorDataHelper.userSelectedLocationInfo.put(ctx.getString(R.string.longitude), "" + data.longitude);
    }
    public void doGetServices() {
//        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_BOOK_APPOINTMENT_SERVICES, Request.Method.GET, true);
//        mConnectionFactory.setHeaderParams();
//        mConnectionFactory.setUrl(Config.SERVICES_URL);
//        mConnectionFactory.createConnection(RescribeConstants.TASK_BOOK_APPOINTMENT_SERVICES);
        try {
            InputStream is = mContext.getAssets().open("book_appointment_services.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Log.e(TAG, "doDoctorConnectChat" + json);

            ServicesModel servicesModel = new Gson().fromJson(json, ServicesModel.class);
            onResponse(ConnectionListener.RESPONSE_OK, servicesModel, RescribeConstants.TASK_BOOK_APPOINTMENT_SERVICES);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
