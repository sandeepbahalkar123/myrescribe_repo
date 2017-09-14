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
import com.rescribe.adapters.DoctorConnectAdapter;
import com.rescribe.adapters.DoctorConnectChatAdapter;
import com.rescribe.helpers.doctor_connect.DoctorConnectHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.doctor_connect.ConnectList;
import com.rescribe.model.doctor_connect.DoctorConnectBaseModel;
import com.rescribe.model.doctor_connect.DoctorConnectDataModel;
import com.rescribe.ui.activities.DoctorConnectActivity;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by jeetal on 5/9/17.
 */
public class DoctorConnectFragment extends Fragment implements HelperResponse {
    private static final String DATA = "DATA";
    @BindView(R.id.listView)
    RecyclerView mRecyclerView;
    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
    Unbinder unbinder;
    private View mRootView;
    private DoctorConnectAdapter doctorConnectAdapter;
    private DoctorConnectHelper mDoctorConnectHelper;
    private DoctorConnectBaseModel doctorConnectBaseModel;
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
        mRootView = inflater.inflate(R.layout.doctor_connect_recycle_view_layout, container, false);

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
        if (mDoctorConnectDataModel.getConnectList() == null) {
            mDoctorConnectHelper.doDoctorConnecList();
        }else {
            setAdapter();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setAdapter() {
        for (int i = 0; i < mDoctorConnectDataModel.getConnectList().size(); i++) {
            String doctorName = mDoctorConnectDataModel.getConnectList().get(i).getDoctorName();
            if (doctorName.startsWith(getString(R.string.dr))) {
                mDoctorConnectDataModel.getConnectList().get(i).setDoctorName(doctorName);
            } else {
                String drName = getString(R.string.dr) + doctorName;
                mDoctorConnectDataModel.getConnectList().get(i).setDoctorName(drName);
            }
        }
        doctorConnectAdapter = new DoctorConnectAdapter(getActivity(), mDoctorConnectDataModel.getConnectList());
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
            doctorConnectBaseModel = (DoctorConnectBaseModel) customResponse;
            mDoctorConnectDataModel = doctorConnectBaseModel.getDoctorConnectDataModel();
            DoctorConnectActivity activity = (DoctorConnectActivity) getActivity();
            activity.setmConnectLists(mDoctorConnectDataModel.getConnectList());
            setAdapter();
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        CommonMethods.showToast(getActivity(),errorMessage);

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(getActivity(),serverErrorMessage);
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(getActivity(),serverErrorMessage);
    }
}
