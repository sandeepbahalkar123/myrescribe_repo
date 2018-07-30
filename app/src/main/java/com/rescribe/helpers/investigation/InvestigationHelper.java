package com.rescribe.helpers.investigation;

import android.content.Context;

import com.android.volley.Request;
import com.rescribe.R;
import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.investigation.request.InvestigationUploadByGmailRequest;
import com.rescribe.network.ConnectRequest;
import com.rescribe.network.ConnectionFactory;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.singleton.Device;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by riteshpandhurkar on 1/3/17.
 */

public class InvestigationHelper implements ConnectionListener {

    private HelperResponse mHelperResponseManager;
    private String TAG = this.getClass().getName();
    private Context mContext;

    public InvestigationHelper(Context context, HelperResponse mHelperResponseManager) {
        this.mContext = context;
        this.mHelperResponseManager = mHelperResponseManager;
    }

    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                CommonMethods.Log(TAG, customResponse.getClass() + " success");
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

    public void getInvestigationList(boolean progressBar) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, progressBar, RescribeConstants.INVESTIGATION_LIST, Request.Method.GET, false);
        mConnectionFactory.setHeaderParams();
        // HardCoded
        mConnectionFactory.setUrl(Config.INVESTIGATION_LIST + "?patientId=" + RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PATIENT_ID, mContext));
//        mConnectionFactory.setUrl(Config.INVESTIGATION_LIST + "?patientId=4092");
        mConnectionFactory.createConnection(RescribeConstants.INVESTIGATION_LIST);
    }

    public void uploadByGmail(InvestigationUploadByGmailRequest investigationUploadByGmailRequest) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.INVESTIGATION_UPLOAD_BY_GMAIL, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(investigationUploadByGmailRequest);
        mConnectionFactory.setUrl(Config.INVESTIGATION_UPLOAD_BY_GMAIL);
        mConnectionFactory.createConnection(RescribeConstants.INVESTIGATION_UPLOAD_BY_GMAIL);
    }

    public void uploadFromAlreadyUploaded(String imageIds, String invIds,String invTypes) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.INVESTIGATION_UPLOAD_FROM_UPLOADED, Request.Method.POST, false);

        Device device = Device.getInstance(mContext);

        Map<String, String> headerParams = new HashMap<>();
        String authorizationString = RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.AUTHTOKEN, mContext);
        headerParams.put(RescribeConstants.CONTENT_TYPE, RescribeConstants.APPLICATION_URL_ENCODED);
        headerParams.put(RescribeConstants.AUTHORIZATION_TOKEN, authorizationString);
        headerParams.put(RescribeConstants.DEVICEID, device.getDeviceId());
        headerParams.put(RescribeConstants.OS, device.getOS());
        headerParams.put(RescribeConstants.OSVERSION, device.getOSVersion());
        headerParams.put(RescribeConstants.DEVICE_TYPE, device.getDeviceType());
        headerParams.put(RescribeConstants.INVESTIGATION_KEYS.IMAGE_ID, imageIds);
        headerParams.put(RescribeConstants.INVESTIGATION_KEYS.INV_ID, invIds);
        headerParams.put(RescribeConstants.INVESTIGATION_KEYS.INV_TYPES, invTypes);
        mConnectionFactory.setHeaderParams(headerParams);
        mConnectionFactory.setUrl(Config.INVESTIGATION_UPLOAD);
        mConnectionFactory.createConnection(RescribeConstants.INVESTIGATION_UPLOAD_FROM_UPLOADED);
    }

    public void doSkipInvestigation(int invID, boolean progress, String type) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, progress, RescribeConstants.TASK_DO_SKIP_INVESTIGATION, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        String id = RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.PATIENT_ID, mContext);
        InvestigationUploadByGmailRequest obj = new InvestigationUploadByGmailRequest();
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(invID);
        obj.setInvestigationId(integers);
        ArrayList<String> types = new ArrayList<>();
        types.add(type);
        obj.setTypes(types);
        obj.setPatientId(Integer.parseInt(id));
        mConnectionFactory.setPostParams(obj);
        mConnectionFactory.setUrl(Config.DO_SKIP_INVESTIGATION);
        mConnectionFactory.createConnection(RescribeConstants.TASK_DO_SKIP_INVESTIGATION);
    }

}
