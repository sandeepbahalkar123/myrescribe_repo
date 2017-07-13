package com.myrescribe.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myrescribe.R;
import com.myrescribe.adapters.DoctorListAdapter;
import com.myrescribe.helpers.doctor.DoctorHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.doctors.DoctorDetail;
import com.myrescribe.model.doctors.DoctorModel;
import com.myrescribe.model.util.TimePeriod;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.sql.Time;
import java.util.ArrayList;


public class DoctorListFragment extends Fragment implements HelperResponse, View.OnClickListener {

    private static final String COUNT = "column-count";
    private static final String MONT = "VALUE";
    private static final String VALUE = "VALUE";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    RecyclerView mDoctorListView;
    DoctorListAdapter showDoctorListAdapter;
    private DoctorHelper mDoctorHelper;
    private View mRootView;

    public DoctorListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_all_view_doctor_history, container, false);
        init();
        return mRootView;
    }

    public static DoctorListFragment createNewFragment(TimePeriod dataString) {
        DoctorListFragment fragment = new DoctorListFragment();
        Bundle args = new Bundle();
        args.putString(MyRescribeConstants.MONTH, dataString.getMonthName());
        args.putString(MyRescribeConstants.YEAR, dataString.getYear());
        fragment.setArguments(args);
        return fragment;
    }

    private void init() {
        mDoctorListView = (RecyclerView) mRootView.findViewById(R.id.doctorListView);
        mDoctorHelper = new DoctorHelper(getActivity(), this);
        mDoctorHelper.doGetDoctorList();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        DoctorModel data = (DoctorModel) customResponse;
        CommonMethods.Log("onSuccess", data.toString());
        ArrayList<DoctorDetail> jan = data.getDoctorInfoMonthContainer().getFormattedDoctorList("JAN", getActivity());
        showDoctorListAdapter = new DoctorListAdapter(getActivity(), jan);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mDoctorListView.setLayoutManager(layoutManager);
        mDoctorListView.setHasFixedSize(true);
        mDoctorListView.setAdapter(showDoctorListAdapter);
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {

    }
}
