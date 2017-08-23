package com.myrescribe.helpers.investigation;

import android.content.Context;

import com.android.volley.Request;
import com.myrescribe.interfaces.ConnectionListener;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.investigation.request.InvestigationUploadByGmailRequest;
import com.myrescribe.network.ConnectRequest;
import com.myrescribe.network.ConnectionFactory;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.singleton.Device;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.Config;
import com.myrescribe.util.MyRescribeConstants;

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
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, progressBar, MyRescribeConstants.INVESTIGATION_LIST, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.INVESTIGATION_LIST + "?patientId=" + MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext));
        // HardCoded
//        mConnectionFactory.setUrl(Config.INVESTIGATION_LIST + "?patientId=4092");
        mConnectionFactory.createConnection(MyRescribeConstants.INVESTIGATION_LIST);
    }

    public void uploadByGmail(InvestigationUploadByGmailRequest investigationUploadByGmailRequest) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, MyRescribeConstants.INVESTIGATION_UPLOAD_BY_GMAIL, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(investigationUploadByGmailRequest);
        mConnectionFactory.setUrl(Config.INVESTIGATION_UPLOAD_BY_GMAIL);
        mConnectionFactory.createConnection(MyRescribeConstants.INVESTIGATION_UPLOAD_BY_GMAIL);
    }

    public void uploadFromAlreadyUploaded(String imageIds, String invIds) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, MyRescribeConstants.INVESTIGATION_UPLOAD_FROM_UPLOADED, Request.Method.POST, false);

        Device device = Device.getInstance(mContext);

        Map<String, String> headerParams = new HashMap<>();
        String authorizationString = MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.AUTHTOKEN, mContext);
        headerParams.put(MyRescribeConstants.CONTENT_TYPE, MyRescribeConstants.APPLICATION_URL_ENCODED);
        headerParams.put(MyRescribeConstants.AUTHORIZATION_TOKEN, authorizationString);
        headerParams.put(MyRescribeConstants.DEVICEID, device.getDeviceId());
        headerParams.put(MyRescribeConstants.OS, device.getOS());
        headerParams.put(MyRescribeConstants.OSVERSION, device.getOSVersion());
        headerParams.put(MyRescribeConstants.DEVICE_TYPE, device.getDeviceType());

        headerParams.put(MyRescribeConstants.INVESTIGATION_KEYS.IMAGE_ID, imageIds);
        headerParams.put(MyRescribeConstants.INVESTIGATION_KEYS.INV_ID, invIds);

        mConnectionFactory.setHeaderParams(headerParams);
        mConnectionFactory.setUrl(Config.INVESTIGATION_UPLOAD);
        mConnectionFactory.createConnection(MyRescribeConstants.INVESTIGATION_UPLOAD_FROM_UPLOADED);
    }
}
