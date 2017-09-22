package com.rescribe.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.adapters.chat.ChatAdapter;
import com.rescribe.helpers.chat.ChatHelper;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.chat.MQTTMessage;
import com.rescribe.model.chat.SendMessageModel;
import com.rescribe.model.chat.history.ChatHistory;
import com.rescribe.model.chat.history.ChatHistoryModel;
import com.rescribe.model.doctor_connect.ChatDoctor;
import com.rescribe.notification.MessageNotification;
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
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ChatActivity extends AppCompatActivity implements HelperResponse {

    private static final int MAX_ATTACHMENT_COUNT = 10;
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
    RecyclerView chatRecyclerView;
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
                MQTTMessage message = intent.getParcelableExtra(MQTTService.MESSAGE);
                if (message.getDocId() == chatList.getId()) {
                    if (chatAdapter != null) {
                        mqttMessage.add(message);
                        chatAdapter.notifyItemInserted(mqttMessage.size() - 1);
                        chatRecyclerView.smoothScrollToPosition(mqttMessage.size() - 1);
                    }
                } else {
                    // Other patient message

                }
            }
        }
    };

    private ChatHelper chatHelper;
    private boolean isSend = false;
    private boolean isExistInChat = false;

    private static final String TAG = "ChatActivity";
    private ChatAdapter chatAdapter;
    private ArrayList<MQTTMessage> mqttMessage = new ArrayList<>();

    private String patId;
    private TextDrawable doctorTextDrawable;

    // load more
    int next = 1;

    private AppDBHelper appDBHelper;
    private int isFirstTime = 0;
    private String patientName;
    private String imageUrl = "";
    private String fileUrl = "";

    private ChatDoctor chatList;
    private int statusColor;

    @Override
    public void onBackPressed() {
        if (isExistInChat) {
            if (mqttMessage.isEmpty())
                setResult(Activity.RESULT_CANCELED);
            else {
                Intent in = new Intent();
                in.putExtra(RescribeConstants.CHAT_USERS, chatList);
                setResult(Activity.RESULT_OK, in);
            }
        } else setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        appDBHelper = new AppDBHelper(this);

        chatList = getIntent().getParcelableExtra(RescribeConstants.DOCTORS_INFO);
        statusColor = getIntent().getIntExtra(RescribeConstants.STATUS_COLOR, ContextCompat.getColor(ChatActivity.this, R.color.green_light));

        receiverName.setText(chatList.getDoctorName());
        String doctorName = chatList.getDoctorName();

        if (doctorName != null) {
            doctorName = doctorName.replace("Dr. ", "");
            int color2 = ColorGenerator.MATERIAL.getColor(doctorName);
            doctorTextDrawable = TextDrawable.builder()
                    .beginConfig()
                    .width(Math.round(getResources().getDimension(R.dimen.dp40)))  // width in px
                    .height(Math.round(getResources().getDimension(R.dimen.dp40))) // height in px
                    .endConfig()
                    .buildRound(("" + doctorName.charAt(0)).toUpperCase(), color2);
        }

        if (chatList.getImageUrl() != null) {
            if (!chatList.getImageUrl().equals("")) {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.dontAnimate();
                requestOptions.override(CommonMethods.convertDpToPixel(40), CommonMethods.convertDpToPixel(40));
                requestOptions.placeholder(doctorTextDrawable);
                requestOptions.error(doctorTextDrawable);

                Glide.with(ChatActivity.this)
                        .load(chatList.getImageUrl())
                        .apply(requestOptions).thumbnail(0.5f)
                        .into(profilePhoto);

            } else {
                profilePhoto.setImageDrawable(doctorTextDrawable);
            }
        } else {
            profilePhoto.setImageDrawable(doctorTextDrawable);
        }

        dateTime.setText(chatList.getOnlineStatus());
        dateTime.setTextColor(statusColor);

        chatHelper = new ChatHelper(this, this);
        patId = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, this);
        patientName = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.USER_NAME, this);
        imageUrl = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PROFILE_PHOTO, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(mLayoutManager);
        chatAdapter = new ChatAdapter(mqttMessage, doctorTextDrawable);
        chatRecyclerView.setAdapter(chatAdapter);

        chatHelper.getChatHistory(next, chatList.getId(), Integer.parseInt(patId));

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                chatHelper.getChatHistory(next, chatList.getId(), Integer.parseInt(patId));
            }
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
                onBackPressed();
                break;
            case R.id.attachmentButton:
                ChatActivityPermissionsDispatcher.onPickDocWithCheck(ChatActivity.this);
                break;
            case R.id.cameraButton:
                ChatActivityPermissionsDispatcher.onPickPhotoWithCheck(ChatActivity.this);
                break;
            case R.id.recorderOrSendButton:
                if (isSend) {

                    // SendButton
                    String message = messageType.getText().toString();
                    message = message.trim();
                    if (!message.equals("")) {

                        MQTTMessage messageL = new MQTTMessage();
                        messageL.setTopic(MQTTService.DOCTOR_CONNECT);
                        messageL.setSender(MQTTService.PATIENT);
                        messageL.setMsg(message);
                        // hard coded
                        messageL.setMsgId(0);
                        messageL.setDocId(chatList.getId());
                        messageL.setPatId(Integer.parseInt(patId));

                        messageL.setName(patientName);
                        messageL.setOnlineStatus(RescribeConstants.ONLINE);
                        messageL.setImageUrl(imageUrl);

                        messageL.setFileUrl(fileUrl);
                        messageL.setSpecialization("");
                        messageL.setPaidStatus(chatList.getPaidStatus());

                        // send msg by http api
//                        chatHelper.sendMsgToPatient(messageL);

                        // send msg by mqtt
                        mqttService.passMessage(messageL);

                        if (mqttService.getNetworkStatus()) {
                            if (chatAdapter != null) {
                                mqttMessage.add(messageL);
                                chatAdapter.notifyItemInserted(mqttMessage.size() - 1);
                                chatRecyclerView.smoothScrollToPosition(mqttMessage.size() - 1);
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

    // File Selecting

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onPickPhoto() {
        FilePickerBuilder.getInstance().setMaxCount(MAX_ATTACHMENT_COUNT)
                .setSelectedFiles(new ArrayList<String>())
                .setActivityTheme(R.style.AppTheme)
                .enableVideoPicker(false)
                .enableCameraSupport(true)
                .showGifs(false)
                .showFolderView(true)
                .enableOrientation(true)
                .pickPhoto(this);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onPickDoc() {
        String[] documents = {".doc", ".docx", ".odt", ".pdf", ".xls", ".xlsx", ".ods", ".ppt", ".pptx"};
        FilePickerBuilder.getInstance().setMaxCount(MAX_ATTACHMENT_COUNT)
                .setSelectedFiles(new ArrayList<String>())
                .setActivityTheme(R.style.AppTheme)
                .addFileSupport(documents)
                .enableDocSupport(false)
                .enableOrientation(true)
                .pickFile(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ChatActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
                if (!data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).isEmpty()) {

                }
            } else if (requestCode == FilePickerConst.REQUEST_CODE_DOC) {
                if (data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS).isEmpty()) {

                }
            }
        }
    }

    // End File Selecting

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

            // set Current Chat User
            mqttService.setCurrentChatUser(chatList.getId());
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent mIntent = new Intent(this, MQTTService.class);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    }

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

        if (isFirstTime > 0) {
            ArrayList<MQTTMessage> unreadMessages = appDBHelper.getUnreadMessagesById(chatList.getId());
            if (unreadMessages.size() > 0) {
                mqttMessage.addAll(unreadMessages);
                chatAdapter.notifyItemRangeInserted(mqttMessage.size() - 1, unreadMessages.size());
                MessageNotification.cancel(this, chatList.getId());
                appDBHelper.deleteUnreadMessage(chatList.getId());
            }
        }

        isFirstTime += 1;
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);

        mqttService.setCurrentChatUser(0);
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
                    mqttMessage.remove(mqttMessage.size() - 1);
                    chatAdapter.notifyItemRemoved(mqttMessage.size() - 1);
                }
                CommonMethods.showToast(ChatActivity.this, sendMessageModel.getCommon().getStatusMessage());
            }
        } else if (customResponse instanceof ChatHistoryModel) {
            ChatHistoryModel chatHistoryModel = (ChatHistoryModel) customResponse;
            if (chatHistoryModel.getCommon().getStatusCode().equals(RescribeConstants.SUCCESS)) {
                final List<ChatHistory> chatHistory = chatHistoryModel.getHistoryData().getChatHistory();

//                messageListTemp.clear();

                for (ChatHistory chatH : chatHistory) {
                    MQTTMessage messageL = new MQTTMessage();
                    messageL.setMsgId(chatH.getChatId());
                    messageL.setMsg(chatH.getMsg());
                    messageL.setDocId(chatH.getUser1Id());
                    messageL.setPatId(chatH.getUser2Id());
                    messageL.setSender(chatH.getSender());

                    messageL.setName(chatH.getName());
                    messageL.setSpecialization(chatH.getSpecialization());
                    messageL.setOnlineStatus(chatH.getOnlineStatus());
                    messageL.setAddress(chatH.getAddress());
                    messageL.setImageUrl(chatH.getImageUrl());
                    messageL.setPaidStatus(chatH.getPaidStatus());
                    messageL.setFileType(chatH.getFileType());

                    String msgTime = CommonMethods.getFormatedDate(chatH.getMsgTime(), RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DATE_PATTERN.YYYY_MM_DD_hh_mm_ss);
                    messageL.setMsgTime(msgTime);
//                    messageListTemp.add(messageL);
                    mqttMessage.add(0, messageL);
                }

//                messageList.addAll(0, messageListTemp);
                if (next == 1) {
                    isExistInChat = mqttMessage.isEmpty();
                    chatRecyclerView.scrollToPosition(mqttMessage.size() - 1);
                    chatAdapter.notifyDataSetChanged();

                    // cancel notification
                    appDBHelper.deleteUnreadMessage(chatList.getId());
                    MessageNotification.cancel(this, chatList.getId());

                } else {
                    chatRecyclerView.post(new Runnable() {
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
            mqttMessage.remove(mqttMessage.size() - 1);
            chatAdapter.notifyItemRemoved(mqttMessage.size() - 1);
        }
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        swipeLayout.setRefreshing(false);
        if (chatAdapter != null) {
            mqttMessage.remove(mqttMessage.size() - 1);
            chatAdapter.notifyItemRemoved(mqttMessage.size() - 1);
        }
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        swipeLayout.setRefreshing(false);
        if (chatAdapter != null) {
            mqttMessage.remove(mqttMessage.size() - 1);
            chatAdapter.notifyItemRemoved(mqttMessage.size() - 1);
        }
    }
}