package com.rescribe.ui.fragments.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rescribe.R;
import com.rescribe.adapters.dashboard.MyAppointmentDashBoardAdapter;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.ui.activities.AppointmentActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jeetal on 7/11/17.
 */

public class MyAppointmentsFragment extends Fragment implements MyAppointmentDashBoardAdapter.OnCardOfAppointmentClickListener{

    Unbinder unbinder;
    private static Bundle args;
    @BindView(R.id.myAppoinmentRecyclerView)
    RecyclerView myAppoinmentRecyclerView;
    private View mRootView;
    ArrayList<DoctorList> dashboardDoctorLists;
    private DividerItemDecoration mDividerItemDecoration;
    private MyAppointmentDashBoardAdapter mMyAppointmentAdapter;

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
            mMyAppointmentAdapter = new MyAppointmentDashBoardAdapter(getActivity(), dashboardDoctorLists,this);
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


    @Override
    public void onClickOfCard(String menuName) {
        if(menuName.equals(getString(R.string.my_appointments))){
            Intent intent = new Intent(getActivity(), AppointmentActivity.class);
            startActivity(intent);
        }else if(menuName.equals(getString(R.string.sponsered_doctor))){

        }
    }
}