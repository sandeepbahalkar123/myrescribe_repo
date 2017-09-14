package com.rescribe.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.adapters.chat.ChatAdapter;
import com.rescribe.helpers.chat.ChatHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.chat.MessageList;
import com.rescribe.model.chat.MessageModel;
import com.rescribe.model.chat.SendMessageModel;
import com.rescribe.model.doctor_connect.ConnectList;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.services.MQTTService;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity implements HelperResponse {

    @BindView(R.id.backButton)
    ImageView backButton;
    @BindView(R.id.profilePhoto)
    ImageView profilePhoto;
    @BindView(R.id.receiverName)
    CustomTextView receiverName;
    @BindView(R.id.onlineStatus)
    CustomTextView onlineStatus;
    @BindView(R.id.titleLayout)
    RelativeLayout titleLayout;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.chatList)
    RecyclerView chatList;
    @BindView(R.id.messageType)
    EditText messageType;
    @BindView(R.id.attachmentButton)
    ImageButton attachmentButton;
    @BindView(R.id.cameraButton)
    ImageButton cameraButton;
    @BindView(R.id.buttonLayout)
    LinearLayout buttonLayout;
    @BindView(R.id.messageTypeSubLayout)
    RelativeLayout messageTypeSubLayout;
    @BindView(R.id.recorderOrSendButton)
    ImageView recorderOrSendButton;
    @BindView(R.id.messageTypeLayout)
    RelativeLayout messageTypeLayout;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isFailed = intent.getBooleanExtra(MQTTService.FAILED, false);

            if (!isFailed) {
                MessageList message = intent.getParcelableExtra(MQTTService.MESSAGE);
                if (message.getDocId() == connectList.getId()) {
                    if (chatAdapter != null) {
                        messageList.add(message);
                        chatAdapter.notifyItemInserted(messageList.size() - 1);
                        chatList.smoothScrollToPosition(messageList.size() - 1);
                    }
                } else {
                    // Other patient message
                }
            }

        }
    };

    private ChatHelper chatHelper;
    private boolean isSend = false;
    private Intent serviceIntent;

    private static final String TAG = "ChatActivity";
    private ChatAdapter chatAdapter;
    private ArrayList<MessageList> messageList = new ArrayList<>();
    private Gson gson = new Gson();

    private ConnectList connectList;
    private String patId;
    private TextDrawable mTextDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        connectList = getIntent().getParcelableExtra(RescribeConstants.DOCTORS_INFO);

        chatHelper = new ChatHelper(this, this);
        patId = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, this);


        //------set values----
        receiverName.setText(connectList.getDoctorName());
        onlineStatus.setText(connectList.getOnlineStatus());
        //--- TODO, PROFILE SHOULD BE HERE, added temperately
        String patientName = connectList.getDoctorName();
        patientName = patientName.replace("Dr. ", "");
        if (patientName != null) {
            int color2 = ColorGenerator.MATERIAL.getColor(patientName);
              mTextDrawable = TextDrawable.builder()
                    .beginConfig()
                    .width(Math.round(getResources().getDimension(R.dimen.dp40)))  // width in px
                    .height(Math.round(getResources().getDimension(R.dimen.dp40))) // height in px
                    .endConfig()
                    .buildRound(("" + patientName.charAt(0)).toUpperCase(), color2);
            profilePhoto.setImageDrawable(mTextDrawable);
        }
        //---------


        // startService

        // use this to start and trigger a service
        serviceIntent = new Intent(this, MQTTService.class);
        // potentially add data to the serviceIntent
        serviceIntent.putExtra(MQTTService.IS_MESSAGE, false);
        startService(serviceIntent);

        // add history api
        String data = "{ \"messageList\": [] }";

        MessageModel messageModel = gson.fromJson(data, MessageModel.class);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        chatList.setLayoutManager(mLayoutManager);
        messageList.addAll(messageModel.getMessageList());
        chatAdapter = new ChatAdapter(messageList,mTextDrawable);
        chatList.setAdapter(chatAdapter);
        chatList.scrollToPosition(messageList.size() - 1);

        messageType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    recorderOrSendButton.setImageResource(R.drawable.speak);
                    cameraButton.setVisibility(View.VISIBLE);
                    isSend = false;
                } else {
                    recorderOrSendButton.setImageResource(R.drawable.send);
                    cameraButton.setVisibility(View.GONE);
                    isSend = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @OnClick({R.id.backButton, R.id.attachmentButton, R.id.cameraButton, R.id.recorderOrSendButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                onBackPressed();
                break;
            case R.id.attachmentButton:
                break;
            case R.id.cameraButton:
                break;
            case R.id.recorderOrSendButton:
                if (isSend) {

                    // SendButton

                    String message = messageType.getText().toString();
                    message = message.trim();
                    if (!message.equals("")) {

                        MessageList messageL = new MessageList();
                        messageL.setTopic(MQTTService.DOCTOR_CONNECT);
                        messageL.setWho(ChatAdapter.SENDER);
                        messageL.setMsg(message);
                        messageL.setMsgId(0);
                        messageL.setDocId(connectList.getId());
                        messageL.setPatId(Integer.parseInt(patId));

                        if (chatAdapter != null) {
                            messageList.add(messageL);
                            chatAdapter.notifyItemInserted(messageList.size() - 1);
                            chatList.smoothScrollToPosition(messageList.size() - 1);
                        }

                        /*serviceIntent.putExtra(MQTTService.IS_MESSAGE, true);
                        serviceIntent.putExtra(MQTTService.MESSAGE, messageL);
                        startService(serviceIntent);

                        messageType.setText("");*/

                        chatHelper.sendMsgToPatient(messageL);

                    }
                } else {

                    // Record Button

                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(
                MQTTService.NOTIFY));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (customResponse instanceof SendMessageModel) {
            SendMessageModel sendMessageModel = (SendMessageModel) customResponse;
            if (sendMessageModel.getCommon().getStatusCode().equals(RescribeConstants.SUCCESS)) {
                // message sent
                messageType.setText("");
            } else {
                if (chatAdapter != null) {
                    messageList.remove(messageList.size() - 1);
                    chatAdapter.notifyItemRemoved(messageList.size() - 1);
                }
                CommonMethods.showToast(ChatActivity.this, sendMessageModel.getCommon().getStatusMessage());
            }
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        if (chatAdapter != null) {
            messageList.remove(messageList.size() - 1);
            chatAdapter.notifyItemRemoved(messageList.size() - 1);
        }
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        if (chatAdapter != null) {
            messageList.remove(messageList.size() - 1);
            chatAdapter.notifyItemRemoved(messageList.size() - 1);
        }
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        if (chatAdapter != null) {
            messageList.remove(messageList.size() - 1);
            chatAdapter.notifyItemRemoved(messageList.size() - 1);
        }
    }
}
