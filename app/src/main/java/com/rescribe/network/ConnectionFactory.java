package com.rescribe.network;

/**
 * @author Sandeep Bahalkar
 */

import android.content.Context;
import android.view.View;

import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.Connector;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.preference.MyRescribePreferencesManager;
import com.rescribe.singleton.Device;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.MyRescribeConstants;

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
        String authorizationString = MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.AUTHTOKEN, mContext);
        headerParams.put(MyRescribeConstants.CONTENT_TYPE, MyRescribeConstants.APPLICATION_JSON);
        headerParams.put(MyRescribeConstants.AUTHORIZATION_TOKEN, authorizationString);
        headerParams.put(MyRescribeConstants.DEVICEID, device.getDeviceId());
        headerParams.put(MyRescribeConstants.OS, device.getOS());
        headerParams.put(MyRescribeConstants.OSVERSION, device.getOSVersion());
        headerParams.put(MyRescribeConstants.DEVICE_TYPE, device.getDeviceType());
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