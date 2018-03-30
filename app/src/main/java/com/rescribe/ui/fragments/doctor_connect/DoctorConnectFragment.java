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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.adapters.DoctorConnectAdapter;
import com.rescribe.helpers.doctor_connect.DoctorConnectHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.doctor_connect.ChatDoctor;
import com.rescribe.model.doctor_connect.DoctorConnectBaseModel;
import com.rescribe.model.doctor_connect.DoctorConnectDataModel;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.activities.DoctorConnectActivity;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.rescribe.ui.activities.DoctorConnectActivity.FREE;
import static com.rescribe.ui.activities.DoctorConnectActivity.PAID;

/**
 * Created by jeetal on 5/9/17.
 */
public class DoctorConnectFragment extends Fragment implements HelperResponse {
    private static final String DATA = "DATA";
    @BindView(R.id.listView)
    RecyclerView mRecyclerView;
    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
    @BindView(R.id.emptyMessageView)
    LinearLayout emptyMessageView;
    Unbinder unbinder;
    private DoctorConnectHelper mDoctorConnectHelper;
    private DoctorConnectDataModel mDoctorConnectDataModel = new DoctorConnectDataModel();


    public DoctorConnectFragment() {
    }

    public static DoctorConnectFragment newInstance() {
        DoctorConnectFragment fragment = new DoctorConnectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        mDoctorConnectHelper = new DoctorConnectHelper(getActivity(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDoctorConnectDataModel.getChatDoctor().isEmpty()) {
            String patientId = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, getContext());
            mDoctorConnectHelper.doDoctorConnecList(patientId);
        } else {
            setAdapter();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setAdapter() {

        ArrayList<ChatDoctor> chatDoctors = new ArrayList<>();

        //Added Dr. to doctorName
        for (int i = 0; i < mDoctorConnectDataModel.getChatDoctor().size(); i++) {
            String doctorName = mDoctorConnectDataModel.getChatDoctor().get(i).getDoctorName();
            //TODO : Temporary Fix as data from Server is not in Proper format
            if (doctorName.startsWith("DR. ")) {
                String drName = doctorName.replace("DR. ", "Dr. ");
                mDoctorConnectDataModel.getChatDoctor().get(i).setDoctorName(drName);
            } else if (doctorName.startsWith("DR.")) {
                String drName = doctorName.replace("DR.", "Dr. ");
                mDoctorConnectDataModel.getChatDoctor().get(i).setDoctorName(drName);
            } else if (doctorName.startsWith("Dr. ")) {
                String drName = doctorName.replace("Dr. ", "Dr. ");
                mDoctorConnectDataModel.getChatDoctor().get(i).setDoctorName(drName);
            } else {
                mDoctorConnectDataModel.getChatDoctor().get(i).setDoctorName("Dr. " + doctorName);
            }

            if (mDoctorConnectDataModel.getChatDoctor().get(i).getPaidStatus() == FREE)
                chatDoctors.add(mDoctorConnectDataModel.getChatDoctor().get(i));
        }

        if (chatDoctors.isEmpty()) {
            emptyListView.setVisibility(View.VISIBLE);
            emptyMessageView.setBackgroundResource(R.drawable.no_chat_conversation_yet);

            mRecyclerView.setVisibility(View.GONE);
        } else {
            emptyListView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        DoctorConnectAdapter doctorConnectAdapter = new DoctorConnectAdapter(getActivity(), chatDoctors);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(doctorConnectAdapter);
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_DOCTOR_CONNECT)) {
            DoctorConnectBaseModel doctorConnectBaseModel = (DoctorConnectBaseModel) customResponse;
            mDoctorConnectDataModel = doctorConnectBaseModel.getDoctorConnectDataModel();
            DoctorConnectActivity activity = (DoctorConnectActivity) getActivity();
            if (mDoctorConnectDataModel != null) {
                activity.setmChatDoctors(mDoctorConnectDataModel.getChatDoctor());
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
        CommonMethods.showToast(getActivity(), serverErrorMessage);
        emptyListView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }
}
