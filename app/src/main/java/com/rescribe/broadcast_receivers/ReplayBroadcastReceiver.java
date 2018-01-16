package com.rescribe.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.chat.MQTTMessage;
import com.rescribe.model.chat.SendMessageModel;
import com.rescribe.notification.MessageNotification;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.services.MQTTService;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import static com.rescribe.services.MQTTService.MESSAGE_TOPIC;
import static com.rescribe.services.MQTTService.PATIENT;
import static com.rescribe.services.MQTTService.REPLY_ACTION;
import static com.rescribe.services.MQTTService.SEND_MESSAGE;
import static com.rescribe.ui.activities.DoctorConnectActivity.FREE;

public class ReplayBroadcastReceiver extends BroadcastReceiver implements HelperResponse {

    public static final String MESSAGE_LIST = "message_list";

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
            messageL.setTopic(MQTTService.TOPIC[MESSAGE_TOPIC]);
            messageL.setMsg(message.toString());

            String generatedId = recievedMessage.getPatId() + "_" + 0 + System.nanoTime();
            messageL.setMsgId(generatedId);

            messageL.setDocId(recievedMessage.getDocId());
            messageL.setPatId(recievedMessage.getPatId());

            String patientName = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.USER_NAME, context);
            String imageUrl = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PROFILE_PHOTO, context);

            messageL.setName(patientName);
            messageL.setImageUrl(imageUrl);
            messageL.setOnlineStatus(RescribeConstants.USER_STATUS.ONLINE);

            messageL.setFileUrl("");
            messageL.setSpecialization("");
            messageL.setPaidStatus(FREE);
            messageL.setFileType("");

            messageL.setSender(PATIENT);

            // 2017-10-13 13:08:07
            String msgTime = CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.UTC_PATTERN);
            messageL.setMsgTime(msgTime);

            // send msg by http api

//            ChatHelper chatHelper = new ChatHelper(context, ReplayBroadcastReceiver.this);
//            chatHelper.sendMsgToPatient(messageL);

            // send via mqtt
            Intent intentService = new Intent(context, MQTTService.class);
            intentService.putExtra(SEND_MESSAGE, true);
            intentService.putExtra(MESSAGE_LIST, messageL);
            context.startService(intentService);
            MessageNotification.cancel(context, recievedMessage.getDocId());
            appDBHelper.deleteUnreadMessage(String.valueOf(recievedMessage.getDocId()));

        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (customResponse instanceof SendMessageModel) {
            if (recievedMessage != null) {
                MessageNotification.cancel(context, recievedMessage.getDocId());
                appDBHelper.deleteUnreadMessage(String.valueOf(recievedMessage.getDocId()));
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