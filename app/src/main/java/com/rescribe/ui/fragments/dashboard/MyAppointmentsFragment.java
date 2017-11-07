package com.rescribe.ui.fragments.dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rescribe.R;
import com.rescribe.adapters.book_appointment.SortByClinicAndDoctorNameAdapter;
import com.rescribe.adapters.dashboard.MyAppointmentAdapter;
import com.rescribe.model.dashboard_api.DashboardDoctorList;
import com.rescribe.ui.fragments.book_appointment.RecentVisitDoctorFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jeetal on 7/11/17.
 */

public class MyAppointmentsFragment extends Fragment {

    Unbinder unbinder;
    private static Bundle args;
    @BindView(R.id.myAppoinmentRecyclerView)
    RecyclerView myAppoinmentRecyclerView;
    private View mRootView;
    ArrayList<DashboardDoctorList> dashboardDoctorLists;
    private DividerItemDecoration mDividerItemDecoration;
    private MyAppointmentAdapter mMyAppointmentAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyAppointmentsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MyAppointmentsFragment newInstance(Bundle data) {

        MyAppointmentsFragment fragment = new MyAppointmentsFragment();
        args = new Bundle();
        args = data;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.show_myappointment_layout, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        init();
        return mRootView;
    }

    private void init() {
        dashboardDoctorLists = args.getParcelableArrayList(getString(R.string.clicked_item_data));
        if(dashboardDoctorLists!=null){
            mMyAppointmentAdapter = new MyAppointmentAdapter(getActivity(), dashboardDoctorLists);
            LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            myAppoinmentRecyclerView.setLayoutManager(linearlayoutManager);
            myAppoinmentRecyclerView.setHasFixedSize(true);
            myAppoinmentRecyclerView.setAdapter(mMyAppointmentAdapter);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        //  setDoctorListAdapter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void setAdapter() {

    }


}