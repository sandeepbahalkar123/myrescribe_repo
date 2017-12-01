package com.rescribe.helpers.doctor_connect;

import android.content.Context;

import com.android.volley.Request;
import com.rescribe.R;
import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.network.ConnectRequest;
import com.rescribe.network.ConnectionFactory;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.RescribeConstants;

/**
 * Created by jeetal on 6/9/17.
 */

public class DoctorConnectHelper implements ConnectionListener {

    String TAG = this.getClass().getName();
    Context mContext;
    HelperResponse mHelperResponseManager;

    public DoctorConnectHelper(Context context) {
        this.mContext = context;
        this.mHelperResponseManager = (HelperResponse) context;
    }

    public DoctorConnectHelper(Context context, HelperResponse doctorConnectActivity) {
        this.mContext = context;
        this.mHelperResponseManager = doctorConnectActivity;
    }

    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        //CommonMethods.Log(TAG, customResponse.toString());
        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag == RescribeConstants.TASK_DOCTOR_CONNECT) {
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

    public void doDoctorConnecList(String patientId) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_DOCTOR_CONNECT, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.DOCTOR_CHAT_LIST_URL + "?patientId=" + patientId);
        mConnectionFactory.createConnection(RescribeConstants.TASK_DOCTOR_CONNECT);
      /*  try {
            InputStream is = mContext.getAssets().open("doctor_connect.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Log.e(TAG, "doDoctorConnecList" + json);

            DoctorConnectBaseModel doctorConnectBaseModel = new Gson().fromJson(json, DoctorConnectBaseModel.class);
            onResponse(ConnectionListener.RESPONSE_OK, doctorConnectBaseModel, RescribeConstants.TASK_DOCTOR_CONNECT);

        } catch (IOException ex) {
            ex.printStackTrace();
        }*/
    }

}
