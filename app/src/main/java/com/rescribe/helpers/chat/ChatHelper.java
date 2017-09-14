package com.rescribe.helpers.chat;

import android.content.Context;

import com.android.volley.Request;
import com.rescribe.interfaces.ConnectionListener;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.chat.MessageList;
import com.rescribe.model.chat.MessageRequestModel;
import com.rescribe.network.ConnectRequest;
import com.rescribe.network.ConnectionFactory;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.RescribeConstants;

/**
 * Created by jeetal on 5/9/17.
 */

public class ChatHelper implements ConnectionListener {

    String TAG = this.getClass().getName();
    Context mContext;
    HelperResponse mHelperResponseManager;

    public ChatHelper(Context context) {
        this.mContext = context;
        this.mHelperResponseManager = (HelperResponse) context;
    }

    public ChatHelper(Context context, HelperResponse doctorConnectActivity) {
        this.mContext = context;
        this.mHelperResponseManager = doctorConnectActivity;
    }

    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        //CommonMethods.Log(TAG, customResponse.toString());
        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag.equals(RescribeConstants.SEND_MESSAGE)) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                }
                break;
            case ConnectionListener.PARSE_ERR0R:
                CommonMethods.Log(TAG, "parse error");
                mHelperResponseManager.onParseError(mOldDataTag, "parse error");
                break;
            case ConnectionListener.SERVER_ERROR:
                CommonMethods.Log(TAG, "server error");
                mHelperResponseManager.onServerError(mOldDataTag, "server error");

                break;
            case ConnectionListener.NO_INTERNET:
                CommonMethods.Log(TAG, "no connection error");
                mHelperResponseManager.onNoConnectionError(mOldDataTag, "no connection error");

                break;
            case ConnectionListener.NO_CONNECTION_ERROR:
                CommonMethods.Log(TAG, "no connection error");
                mHelperResponseManager.onNoConnectionError(mOldDataTag, "no connection error");

                break;
            default:
                CommonMethods.Log(TAG, "default error");
                break;
        }
    }

    @Override
    public void onTimeout(ConnectRequest request) {

    }

    public void sendMsgToPatient(MessageList messageL){
        MessageRequestModel messageRequestModel = new MessageRequestModel();
        messageRequestModel.setMsg(messageL.getMsg());
        // 2017-10-13 12:08:07
        messageRequestModel.setMsgTime(CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.YYYY_MM_DD_hh_mm_ss));
        messageRequestModel.setSender("user2");
        messageRequestModel.setUser1id(messageL.getPatId());
        messageRequestModel.setUser2id(messageL.getDocId());

        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.SEND_MESSAGE, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(messageRequestModel);
        mConnectionFactory.setUrl(Config.SEND_MSG_TO_DOCTOR);
        mConnectionFactory.createConnection(RescribeConstants.SEND_MESSAGE);
    }
}


