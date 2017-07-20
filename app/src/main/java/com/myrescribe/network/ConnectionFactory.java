package com.myrescribe.network;

/**
 * @author Sandeep Bahalkar
 */

import android.content.Context;
import android.view.View;

import com.myrescribe.interfaces.ConnectionListener;
import com.myrescribe.interfaces.Connector;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.singleton.Device;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.util.HashMap;
import java.util.Map;

public class ConnectionFactory extends ConnectRequest {

    private static final String TAG = "MyRescribe/ConnectionFactory";
    Connector connector = null;
    private Device device;

    public ConnectionFactory(Context context, ConnectionListener connectionListener, View viewById, boolean isProgressBarShown, String mOldDataTag, int reqPostOrGet, boolean isOffline) {
        super();
        this.mConnectionListener = connectionListener;
        this.mContext = context;
        this.mViewById = viewById;
        this.isProgressBarShown = isProgressBarShown;
        this.mOldDataTag = mOldDataTag;
        this.reqPostOrGet = reqPostOrGet;
        this.isOffline = isOffline;

        device = Device.getInstance(mContext);
    }

    public void setHeaderParams(Map<String, String> headerParams) {
        this.mHeaderParams = headerParams;
    }

    public void setHeaderParams() {

        Map<String, String> headerParams = new HashMap<>();

        String authorizationString = "";
        String contentType = MyRescribePreferencesManager.getString(MyRescribeConstants.LOGIN_SUCCESS, mContext);

        if (contentType.equalsIgnoreCase(MyRescribeConstants.TRUE)) {
            authorizationString = MyRescribePreferencesManager.getString(MyRescribeConstants.TOKEN_TYPE, mContext)
                    + " " + MyRescribePreferencesManager.getString(MyRescribeConstants.ACCESS_TOKEN, mContext);
            headerParams.put(MyRescribeConstants.CONTENT_TYPE, MyRescribeConstants.APPLICATION_JSON);
        } else {
            headerParams.put(MyRescribeConstants.CONTENT_TYPE, MyRescribeConstants.APPLICATION_URL_ENCODED);
        }

        headerParams.put(MyRescribeConstants.AUTHORIZATION, authorizationString);
        headerParams.put(MyRescribeConstants.DEVICEID, device.getDeviceId());

        headerParams.put(MyRescribeConstants.OS, device.getOS());
        headerParams.put(MyRescribeConstants.OSVERSION, device.getOSVersion());
        //  headerParams.put(MyRescribeConstants.DEVICETYPE, device.getDeviceType());
//        headerParams.put(MyRescribeConstants.ACCESS_TOKEN, "");
        CommonMethods.Log(TAG, "setHeaderParams:" + headerParams.toString());
        this.mHeaderParams = headerParams;
    }

    public void setPostParams(CustomResponse customResponse) {
        this.customResponse = customResponse;
    }

    public void setPostParams(Map<String, String> postParams) {
        this.mPostParams = postParams;
    }

    public void setUrl(String url) {
        String baseUrl = MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.SERVER_PATH, mContext);
        this.mURL = baseUrl + url;
        CommonMethods.Log(TAG, "mURL: " + this.mURL);
    }

    public Connector createConnection(String type) {

        connector = new RequestManager(mContext, mConnectionListener, type, mViewById, isProgressBarShown, mOldDataTag, reqPostOrGet, isOffline);

        if (customResponse != null) connector.setPostParams(customResponse);

        if (mPostParams != null) connector.setPostParams(mPostParams);

        if (mHeaderParams != null) connector.setHeaderParams(mHeaderParams);

        if (mURL != null) connector.setUrl(mURL);

        connector.connect();

        return connector;
    }

    public void cancel() {
        connector.abort();
    }
}