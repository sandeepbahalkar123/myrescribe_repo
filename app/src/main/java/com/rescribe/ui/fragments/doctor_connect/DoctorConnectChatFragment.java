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
import com.rescribe.model.doctor_connect_chat.ChatData;
import com.rescribe.model.doctor_connect_chat.DoctorConnectChatBaseModel;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

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
    private View mRootView;
    private DoctorConnectChatHelper mDoctorConnectChatHelper;
    private DoctorConnectChatBaseModel mDoctorConnectChatBaseModel;
    private ChatData mData = new ChatData();

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
        mRootView = inflater.inflate(R.layout.doctor_connect_recycle_view_layout, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        init();
        return mRootView;
    }

    private void init() {
        mDoctorConnectChatHelper = new DoctorConnectChatHelper(getActivity(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mData.getChatList() == null) {
            mDoctorConnectChatHelper.doDoctorConnectChat();
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
        if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_DOCTOR_CONNECT_CHAT)) {
            mDoctorConnectChatBaseModel = (DoctorConnectChatBaseModel) customResponse;
            if (mDoctorConnectChatBaseModel.getData() == null) {
                emptyListView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            } else {
                emptyListView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mData = mDoctorConnectChatBaseModel.getData();
                setAdapter();
            }
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        CommonMethods.showToast(getActivity(), errorMessage);

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(getActivity(), serverErrorMessage);
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        emptyListView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    public void setAdapter() {
        //Added Dr. to doctorName
        for (int i = 0; i < mData.getChatList().size(); i++) {
            String doctorName = mData.getChatList().get(i).getDoctorName();
            //TODO : Temporary Fix as data from Server is not in Proper format
            if (doctorName.startsWith("DR. ")) {
              String drName =  doctorName.replace("DR. ", "Dr. ");
                mData.getChatList().get(i).setDoctorName(drName);
            } else if (doctorName.startsWith("DR.")) {
                String drName =   doctorName.replace("DR.", "Dr. ");
                mData.getChatList().get(i).setDoctorName(drName);
            }  else if (doctorName.startsWith("Dr. ")) {
                String drName =   doctorName.replace("Dr. ", "Dr. ");
                mData.getChatList().get(i).setDoctorName(drName);
            } else {
                mData.getChatList().get(i).setDoctorName("Dr. " + doctorName);
            }
        }

        mDoctorConnectChatAdapter = new DoctorConnectChatAdapter(getActivity(), mData.getChatList());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(mDoctorConnectChatAdapter);
    }
}

