package com.rescribe.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rescribe.helpers.chat.ChatHelper;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.chat.MQTTMessage;
import com.rescribe.model.chat.SendMessageModel;
import com.rescribe.notification.MessageNotification;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.services.MQTTService;
import com.rescribe.util.RescribeConstants;

import static com.rescribe.services.MQTTService.REPLY_ACTION;

public class ReplayBroadcastReceiver extends BroadcastReceiver implements HelperResponse {
    private static final String MESSAGE_LIST = "message_list";
    private MQTTMessage recievedMessage;
    private Context context;
    private AppDBHelper appDBHelper;

    public static Intent getReplyMessageIntent(Context context, MQTTMessage MQTTMessage) {
        Intent intent = new Intent(context, ReplayBroadcastReceiver.class);
        intent.setAction(REPLY_ACTION);
        intent.putExtra(MESSAGE_LIST, MQTTMessage);
        return intent;
    }

    public ReplayBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (REPLY_ACTION.equals(intent.getAction())) {
            // do whatever you want with the message. Send to the server or add to the db.
            // for this tutorial, we'll just show it in a toast;
            CharSequence message = MQTTService.getReplyMessage(intent);
            appDBHelper = new AppDBHelper(context);
            recievedMessage = intent.getParcelableExtra(MESSAGE_LIST);
            this.context = context;

            MQTTMessage messageL = new MQTTMessage();
            messageL.setTopic(MQTTService.DOCTOR_CONNECT);
            messageL.setSender(MQTTService.PATIENT);
            messageL.setMsg(message.toString());
            // hard coded
            messageL.setMsgId(0);
            messageL.setDocId(recievedMessage.getDocId());
            messageL.setPatId(recievedMessage.getPatId());

            String patientName = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.USER_NAME, context);
            String imageUrl = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PROFILE_PHOTO, context);

            messageL.setName(patientName);
            messageL.setImageUrl(imageUrl);
            messageL.setOnlineStatus(RescribeConstants.ONLINE);

            messageL.setFileUrl("");
            messageL.setSpecialization("");
            messageL.setPaidStatus(0);
            messageL.setFileType("");

            // send msg by http api

            ChatHelper chatHelper = new ChatHelper(context, ReplayBroadcastReceiver.this);
            chatHelper.sendMsgToPatient(messageL);

            // send via mqtt
//            mqttService.passMessage(messageL);

        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (customResponse instanceof SendMessageModel) {
            if (recievedMessage != null) {
                MessageNotification.cancel(context, recievedMessage.getDocId());
                appDBHelper.deleteUnreadMessage(recievedMessage.getDocId());
            }
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {

    }
}