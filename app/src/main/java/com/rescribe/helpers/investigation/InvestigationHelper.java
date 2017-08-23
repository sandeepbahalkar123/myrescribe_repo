package com.rescribe.helpers.investigation;

import android.content.Context;

import com.android.volley.Request;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by riteshpandhurkar on 1/3/17.
 */

public class InvestigationHelper implements ConnectionListener {

    private String TAG = this.getClass().getName();
    private Context mContext;

    public InvestigationHelper(Context context) {
        this.mContext = context;
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

    public void getInvestigationList(boolean progressBar) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, progressBar, RescribeConstants.INVESTIGATION_LIST, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.INVESTIGATION_LIST + "?patientId=" + RescribePreferencesManager.getString(RescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext));
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

    public void uploadFromAlreadyUploaded(String imageIds, String invIds) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.INVESTIGATION_UPLOAD_FROM_UPLOADED, Request.Method.POST, false);

        Device device = Device.getInstance(mContext);

        Map<String, String> headerParams = new HashMap<>();
        String authorizationString = RescribePreferencesManager.getString(RescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.AUTHTOKEN, mContext);
        headerParams.put(RescribeConstants.CONTENT_TYPE, RescribeConstants.APPLICATION_URL_ENCODED);
        headerParams.put(RescribeConstants.AUTHORIZATION_TOKEN, authorizationString);
        headerParams.put(RescribeConstants.DEVICEID, device.getDeviceId());
        headerParams.put(RescribeConstants.OS, device.getOS());
        headerParams.put(RescribeConstants.OSVERSION, device.getOSVersion());
        headerParams.put(RescribeConstants.DEVICE_TYPE, device.getDeviceType());
        headerParams.put(RescribeConstants.INVESTIGATION_KEYS.IMAGE_ID, imageIds);
        headerParams.put(RescribeConstants.INVESTIGATION_KEYS.INV_ID, invIds);
        mConnectionFactory.setHeaderParams(headerParams);
        mConnectionFactory.setUrl(Config.INVESTIGATION_UPLOAD);
        mConnectionFactory.createConnection(RescribeConstants.INVESTIGATION_UPLOAD_FROM_UPLOADED);
    }
}
