package com.rescribe.ui.fragments.doctor_connect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.adapters.DoctorConnectChatAdapter;
import com.rescribe.helpers.doctor_connect.DoctorConnectChatHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.chat.MQTTMessage;
import com.rescribe.model.doctor_connect.ChatDoctor;
import com.rescribe.model.doctor_connect.RecentChatDoctorModel;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by jeetal on 6/9/17.
 */

public class DoctorConnectChatFragment extends Fragment implements HelperResponse {
    @BindView(R.id.listView)
    RecyclerView mRecyclerView;

    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;

    Unbinder unbinder;
    DoctorConnectChatAdapter mDoctorConnectChatAdapter;
    private DoctorConnectChatHelper mDoctorConnectChatHelper;
    private ArrayList<ChatDoctor> chatDoctors = new ArrayList<>();
    private String patientId;

    public static DoctorConnectChatFragment newInstance() {
        DoctorConnectChatFragment fragment = new DoctorConnectChatFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public DoctorConnectChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.doctor_connect_recycle_view_layout, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        init();
        return mRootView;
    }

    private void init() {
        patientId = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, getContext());
        mDoctorConnectChatHelper = new DoctorConnectChatHelper(getActivity(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (chatDoctors.isEmpty()) {
            mDoctorConnectChatHelper.doDoctorConnectChat(patientId);
        } else {
            setAdapter();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(RescribeConstants.CHAT_USERS)) {
            RecentChatDoctorModel mDoctorConnectChatBaseModel = (RecentChatDoctorModel) customResponse;
            if (mDoctorConnectChatBaseModel.getDoctorConnectDataModel() == null) {
                emptyListView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            } else {
                if (mDoctorConnectChatBaseModel.getDoctorConnectDataModel().getChatDoctor().isEmpty()) {
                    emptyListView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    emptyListView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                chatDoctors.addAll(mDoctorConnectChatBaseModel.getDoctorConnectDataModel().getChatDoctor());
                setAdapter();
            }
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        emptyListView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        CommonMethods.showToast(getActivity(), errorMessage);
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        emptyListView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        CommonMethods.showToast(getActivity(), serverErrorMessage);
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        emptyListView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    public void setAdapter() {
        //Added Dr. to doctorName
        for (int i = 0; i < chatDoctors.size(); i++) {
            String doctorName = chatDoctors.get(i).getDoctorName();
            //TODO : Temporary Fix as data from Server is not in Proper format
            if (doctorName.startsWith("DR. ")) {
              String drName =  doctorName.replace("DR. ", "Dr. ");
                chatDoctors.get(i).setDoctorName(drName);
            } else if (doctorName.startsWith("DR.")) {
                String drName =   doctorName.replace("DR.", "Dr. ");
                chatDoctors.get(i).setDoctorName(drName);
            }  else if (doctorName.startsWith("Dr. ")) {
                String drName =   doctorName.replace("Dr. ", "Dr. ");
                chatDoctors.get(i).setDoctorName(drName);
            } else {
                chatDoctors.get(i).setDoctorName("Dr. " + doctorName);
            }
        }

        mDoctorConnectChatAdapter = new DoctorConnectChatAdapter(getActivity(), chatDoctors);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(mDoctorConnectChatAdapter);
    }

    public void notifyCount(MQTTMessage message) {
        boolean isThere = false;
        if (chatDoctors != null) {
            for (int index = 0; index < chatDoctors.size(); index++) {
                if (chatDoctors.get(index).getId() == message.getDocId()) {
                    mDoctorConnectChatAdapter.notifyItemChanged(index);
                    isThere = true;
                    break;
                }
            }

            if (!isThere) {
                ChatDoctor chatDoctor = new ChatDoctor();
                chatDoctor.setId(message.getDocId());
                chatDoctor.setDoctorName(message.getSenderName());
                chatDoctor.setImageUrl(message.getSenderImgUrl());
                chatDoctor.setUnreadMessages(1);
                chatDoctor.setPaidStatus(message.getPaidStatus());
                chatDoctor.setSpecialization(message.getSpecialization());
                chatDoctor.setOnlineStatus(RescribeConstants.USER_STATUS.ONLINE);
                chatDoctors.add(0, chatDoctor);
                mDoctorConnectChatAdapter.notifyDataSetChanged();

                if (!chatDoctors.isEmpty()){
                    emptyListView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void addItem(ChatDoctor chatDoctor) {
        String time = CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.UTC_PATTERN);
        chatDoctor.setLastChatTime(time);

        boolean isThere = false;
        for (int index = 0; index < chatDoctors.size(); index++) {
            if (chatDoctors.get(index).getId() == chatDoctor.getId()) {
                isThere = true;
                break;
            }
        }

        if (!isThere)
            chatDoctors.add(0, chatDoctor);

        mDoctorConnectChatAdapter.notifyDataSetChanged();
    }
}

