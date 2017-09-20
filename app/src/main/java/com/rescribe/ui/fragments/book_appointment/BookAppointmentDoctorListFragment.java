package com.rescribe.ui.fragments.book_appointment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.adapters.DoctorListAdapter;
import com.rescribe.helpers.doctor.DoctorHelper;
import com.rescribe.model.doctors.doctor_info.DoctorDetail;
import com.rescribe.model.login.Year;
import com.rescribe.ui.activities.doctor.DoctorListActivity;
import com.rescribe.ui.fragments.doctor.DoctorListFragmentContainer;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class BookAppointmentDoctorListFragment extends Fragment implements View.OnClickListener {


    private View mRootView;

    public BookAppointmentDoctorListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.book_appointment_drawer_filter, container, false);
        init();

         return mRootView;
    }

    public static BookAppointmentDoctorListFragment createNewFragment() {
        BookAppointmentDoctorListFragment fragment = new BookAppointmentDoctorListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void init() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
