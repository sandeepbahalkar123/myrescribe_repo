package com.rescribe.helpers.vital_graph_helper;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.chat.MQTTMessage;
import com.rescribe.model.chat.MessageRequestModel;
import com.rescribe.model.vital_graph.vital_description.VitalGraphInfoBaseModel;
import com.rescribe.model.vital_graph.vital_tracker.VitalGraphAddNewTrackerRequestModel;
import com.rescribe.network.ConnectRequest;
import com.rescribe.network.ConnectionFactory;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.RescribeConstants;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by jeetal on 5/9/17.
 */

public class VitalGraphHelper implements ConnectionListener {

    String TAG = this.getClass().getName();
    Context mContext;
    HelperResponse mHelperResponseManager;

    public VitalGraphHelper(Context context) {
        this.mContext = context;
        this.mHelperResponseManager = (HelperResponse) context;
    }

    public VitalGraphHelper(Context context, HelperResponse doctorConnectActivity) {
        this.mContext = context;
        this.mHelperResponseManager = doctorConnectActivity;
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

    public void doGetPatientVitalList() {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_GET_PATIENT_VITAL_LIST, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        String id = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext);
        mConnectionFactory.setUrl(Config.TASK_GET_PATIENT_VITAL_LIST + "?patientId=" + id);
        mConnectionFactory.createConnection(RescribeConstants.TASK_GET_PATIENT_VITAL_LIST);
    }

    public void doGetPatientVitalDetail(String vitalName) {
       ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_GET_PATIENT_VITAL_DETAIL, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        String id = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext);
        if (vitalName.contains(" ")) {
            try {
                mConnectionFactory.setUrl(Config.TASK_GET_PATIENT_VITAL_DETAIL + "?patientId=" + id + "&vitalName=" + URLEncoder.encode(vitalName, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                mConnectionFactory.setUrl(Config.TASK_GET_PATIENT_VITAL_DETAIL + "?patientId=" + id + "&vitalName=" + vitalName);
            }
        } else {
            mConnectionFactory.setUrl(Config.TASK_GET_PATIENT_VITAL_DETAIL + "?patientId=" + id + "&vitalName=" + vitalName);
        }
        mConnectionFactory.createConnection(RescribeConstants.TASK_GET_PATIENT_VITAL_DETAIL);

    }

    public void doGetPatientVitalTrackerList() {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_GET_VITAL_TRACKER_LIST, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        String id = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext);
        mConnectionFactory.setUrl(Config.TASK_GET_PATIENT_VITAL_TRACKER_LIST);
        mConnectionFactory.createConnection(RescribeConstants.TASK_GET_VITAL_TRACKER_LIST);
    }

    public void doAddNewVitalGraphTracker(VitalGraphAddNewTrackerRequestModel model) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_ADD_VITAL_MANUALLY, Request.Method.POST, true);
        mConnectionFactory.setHeaderParams();
        model.setPatientId(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext));
        mConnectionFactory.setPostParams(model);
        mConnectionFactory.setUrl(Config.TASK_PATIENT_ADD_VITAL_MANUALLY);
        mConnectionFactory.createConnection(RescribeConstants.TASK_ADD_VITAL_MANUALLY);
    }
}


