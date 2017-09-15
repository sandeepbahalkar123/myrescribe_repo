package com.rescribe.ui.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.rescribe.R;
import com.rescribe.adapters.chat.ChatAdapter;
import com.rescribe.helpers.chat.ChatHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.chat.MessageList;
import com.rescribe.model.chat.SendMessageModel;
import com.rescribe.model.chat.history.ChatHistory;
import com.rescribe.model.chat.history.ChatHistoryModel;
import com.rescribe.model.doctor_connect.ConnectList;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.services.MQTTService;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.dateTime)
    CustomTextView dateTime;
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

    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean delivered = intent.getBooleanExtra(MQTTService.DELIVERED, false);
            boolean isReceived = intent.getBooleanExtra(MQTTService.RECEIVED, false);

            if (delivered) {

                messageType.setText("");

                Log.d(TAG, "Delivery Complete");
                Log.d(TAG + " MESSAGE_ID", intent.getStringExtra(MQTTService.MESSAGE_ID));

            } else if (isReceived) {
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

    private static final String TAG = "ChatActivity";
    private ChatAdapter chatAdapter;
    private ArrayList<MessageList> messageList = new ArrayList<>();

    private ConnectList connectList;
    private String patId;
    private TextDrawable mTextDrawable;

    // load more
    int next = 1;
//    private ArrayList<MessageList> messageListTemp = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        connectList = getIntent().getParcelableExtra(RescribeConstants.DOCTORS_INFO);

        receiverName.setText(connectList.getDoctorName());

        String doctorName = connectList.getDoctorName();
        doctorName = doctorName.replace("Dr. ", "");
        if (doctorName != null) {
            int color2 = ColorGenerator.MATERIAL.getColor(doctorName);
            mTextDrawable = TextDrawable.builder()
                    .beginConfig()
                    .width(Math.round(getResources().getDimension(R.dimen.dp40)))  // width in px
                    .height(Math.round(getResources().getDimension(R.dimen.dp40))) // height in px
                    .endConfig()
                    .buildRound(("" + doctorName.charAt(0)).toUpperCase(), color2);
            profilePhoto.setImageDrawable(mTextDrawable);
        }

        dateTime.setText(connectList.getOnlineStatus());

        chatHelper = new ChatHelper(this, this);
        patId = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, this);
        chatHelper.getChatHistory(1, connectList.getId(), Integer.parseInt(patId));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        chatList.setLayoutManager(mLayoutManager);
        chatAdapter = new ChatAdapter(messageList, mTextDrawable);
        chatList.setAdapter(chatAdapter);

        chatHelper.getChatHistory(next, connectList.getId(), Integer.parseInt(patId));

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                chatHelper.getChatHistory(next, connectList.getId(), Integer.parseInt(patId));            }
        });

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


        //----------
    }

    @OnClick({R.id.backButton, R.id.attachmentButton, R.id.cameraButton, R.id.recorderOrSendButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
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
                        messageL.setSender(MQTTService.PATIENT);
                        messageL.setMsg(message);
                        // hard coded
                        messageL.setMsgId(0);
                        messageL.setDocId(connectList.getId());
                        messageL.setPatId(Integer.parseInt(patId));

                        // send msg by http api
                        chatHelper.sendMsgToPatient(messageL);

                        // send msg by mqtt
//                        mqttService.passMessage(messageL);

                        if (mqttService.getNetworkStatus()) {
                            if (chatAdapter != null) {
                                messageList.add(messageL);
                                chatAdapter.notifyItemInserted(messageList.size() - 1);
                                chatList.smoothScrollToPosition(messageList.size() - 1);
                            }
                        } else
                            CommonMethods.showToast(ChatActivity.this, getResources().getString(R.string.internet));
                    }
                } else {

                    // Record Button stuff here

                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent mIntent = new Intent(this, MQTTService.class);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    }


    boolean mBounded;
    MQTTService mqttService;

    ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {
//            Toast.makeText(ChatActivity.this, "Service is disconnected", Toast.LENGTH_SHORT).show();
            mBounded = false;
            mqttService = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
//            Toast.makeText(ChatActivity.this, "Service is connected", Toast.LENGTH_SHORT).show();
            mBounded = true;
            MQTTService.LocalBinder mLocalBinder = (MQTTService.LocalBinder) service;
            mqttService = mLocalBinder.getServerInstance();
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if (mBounded) {
            unbindService(mConnection);
            mBounded = false;
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
        } else if (customResponse instanceof ChatHistoryModel) {
            ChatHistoryModel chatHistoryModel = (ChatHistoryModel) customResponse;
            if (chatHistoryModel.getCommon().getStatusCode().equals(RescribeConstants.SUCCESS)) {
                final List<ChatHistory> chatHistory = chatHistoryModel.getHistoryData().getChatHistory();

//                messageListTemp.clear();

                for (ChatHistory chatH : chatHistory) {
                    MessageList messageL = new MessageList();
                    messageL.setMsgId(chatH.getChatId());
                    messageL.setMsg(chatH.getMsg());
                    messageL.setDocId(chatH.getUser1Id());
                    messageL.setPatId(chatH.getUser2Id());
                    messageL.setSender(chatH.getSender());
                    String msgTime = CommonMethods.getFormatedDate(chatH.getMsgTime(), RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DATE_PATTERN.YYYY_MM_DD_hh_mm_ss);
                    messageL.setMsgTime(msgTime);
//                    messageListTemp.add(messageL);
                    messageList.add(0, messageL);
                }

//                messageList.addAll(0, messageListTemp);
                if (next == 1) {
                    chatList.scrollToPosition(messageList.size() - 1);
                    chatAdapter.notifyDataSetChanged();
                } else {
                    chatList.post(new Runnable() {
                        @Override
                        public void run() {
                            // Notify adapter with appropriate notify methods
                            chatAdapter.notifyItemRangeInserted(0, chatHistory.size());
                        }
                    });
                }

                next += 1;
            }

            swipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        swipeLayout.setRefreshing(false);
        if (chatAdapter != null) {
            messageList.remove(messageList.size() - 1);
            chatAdapter.notifyItemRemoved(messageList.size() - 1);
        }
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        swipeLayout.setRefreshing(false);
        if (chatAdapter != null) {
            messageList.remove(messageList.size() - 1);
            chatAdapter.notifyItemRemoved(messageList.size() - 1);
        }
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        swipeLayout.setRefreshing(false);
        if (chatAdapter != null) {
            messageList.remove(messageList.size() - 1);
            chatAdapter.notifyItemRemoved(messageList.size() - 1);
        }
    }
}