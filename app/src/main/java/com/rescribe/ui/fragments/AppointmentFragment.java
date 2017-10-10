package com.rescribe.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.adapters.AppointmentAdapter;
import com.rescribe.model.doctors.appointments.AptList;
import com.rescribe.ui.activities.AppointmentActivity;

import java.util.ArrayList;

/**
 * Created by jeetal on 19/7/17.
 */

public class AppointmentFragment extends Fragment {

    private static final String DATA = "DATA";
    private AppointmentActivity mParentActivity;
    private View mRootView;
    private RecyclerView mAppointmentListView;
    private RelativeLayout mEmptyListView;
    private String mAppointmentTypeName;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AppointmentFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AppointmentFragment newInstance(String data) {
        AppointmentFragment fragment = new AppointmentFragment();
        Bundle args = new Bundle();
        args.putString(DATA, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.globle_recycle_viewlist, container, false);

        mParentActivity = (AppointmentActivity) getActivity();

        Bundle arguments = getArguments();
        if (arguments != null) {
            mAppointmentTypeName = arguments.getString(DATA);
        }
        init();
        return mRootView;
    }

    private void init() {
        mAppointmentListView = (RecyclerView) mRootView.findViewById(R.id.listView);
        mEmptyListView = (RelativeLayout) mRootView.findViewById(R.id.emptyListView);
    }

    @Override
    public void onResume() {
        super.onResume();
        setDoctorListAdapter();
    }

    private void setDoctorListAdapter() {
        ArrayList<AptList> appointmentList = mParentActivity.getAppointmentList(mAppointmentTypeName);
        if (appointmentList != null) {
            if (appointmentList.size() == 0) {
                mAppointmentListView.setVisibility(View.GONE);
                mEmptyListView.setVisibility(View.VISIBLE);
            } else {
                mEmptyListView.setVisibility(View.GONE);
                mAppointmentListView.setVisibility(View.VISIBLE);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                mAppointmentListView.setLayoutManager(layoutManager);
                mAppointmentListView.setHasFixedSize(true);
                mAppointmentListView.setAdapter(new AppointmentAdapter(getActivity(), appointmentList, mAppointmentTypeName));
            }
        }
    }
}
