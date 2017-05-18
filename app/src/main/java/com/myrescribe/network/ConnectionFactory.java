
package com.myrescribe.network;

/**
 * @author Sandeep Bahalkar
 */

import android.content.Context;
import android.view.View;

import com.myrescribe.interfaces.ConnectionListener;
import com.myrescribe.interfaces.Connector;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.preference.AppPreferencesManager;
import com.myrescribe.singleton.Device;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class ConnectionFactory extends ConnectRequest {

    private static final String TAG = "ConnectionFactory";
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
        String contentType = AppPreferencesManager.getString(Constants.LOGIN_SUCCESS, mContext);

        if (contentType.equalsIgnoreCase(Constants.TRUE)) {
            authorizationString = AppPreferencesManager.getString(Constants.TOKEN_TYPE, mContext)
                    + " " + AppPreferencesManager.getString(Constants.ACCESS_TOKEN, mContext);
            headerParams.put(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
        } else {
            headerParams.put(Constants.CONTENT_TYPE, Constants.APPLICATION_URL_ENCODED);
        }

        headerParams.put(Constants.AUTHORIZATION, authorizationString);
        headerParams.put(Constants.DEVICEID, device.getDeviceId());

        headerParams.put(Constants.OS, device.getOS());
        headerParams.put(Constants.OSVERSION, device.getOSVersion());
        //  headerParams.put(Constants.DEVICETYPE, device.getDeviceType());
//        headerParams.put(Constants.ACCESS_TOKEN, "");
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
        String baseUrl = AppPreferencesManager.getString(AppPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mContext);
        this.mURL = baseUrl + url;
        CommonMethods.Log(TAG,"mURL: "+this.mURL);
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