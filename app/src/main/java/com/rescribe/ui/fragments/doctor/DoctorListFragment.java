package com.rescribe.ui.fragments.doctor;

import android.os.Bundle;
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
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Map;


public class DoctorListFragment extends Fragment implements View.OnClickListener {

    RecyclerView mDoctorListView;
    DoctorListAdapter showDoctorListAdapter;
    private View mRootView;
    private String mMonthName;
    private String mYear;
    private RelativeLayout mEmptyListView;


    public DoctorListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.globle_recycle_viewlist, container, false);
        init();

        Bundle arguments = getArguments();
        if (arguments != null) {
            mMonthName = arguments.getString(RescribeConstants.MONTH);
            mYear = arguments.getString(RescribeConstants.YEAR);
        }
        return mRootView;
    }

    public static DoctorListFragment createNewFragment(Year dataString) {
        DoctorListFragment fragment = new DoctorListFragment();
        Bundle args = new Bundle();
        args.putString(RescribeConstants.MONTH, dataString.getMonthName());
        args.putString(RescribeConstants.YEAR, dataString.getYear());
        fragment.setArguments(args);
        return fragment;
    }

    private void init() {
        mDoctorListView = (RecyclerView) mRootView.findViewById(R.id.listView);
        mEmptyListView = (RelativeLayout) mRootView.findViewById(R.id.emptyListView);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
        setDoctorListAdapter();
    }

    private void setDoctorListAdapter() {
        DoctorListFragmentContainer parentFragment = (DoctorListFragmentContainer) this.getParentFragment();
        DoctorHelper parentDoctorHelper = parentFragment.getParentDoctorHelper();
        if (parentDoctorHelper != null) {
            Map<String, Map<String, ArrayList<DoctorDetail>>> yearWiseSortedDoctorList = parentDoctorHelper.getYearWiseSortedDoctorList();
            if (yearWiseSortedDoctorList.size() != 0) {
                Map<String, ArrayList<DoctorDetail>> monthArrayListHashMap = yearWiseSortedDoctorList.get(mYear);
                if (monthArrayListHashMap != null) {
                    ArrayList<DoctorDetail> formattedDoctorList = parentDoctorHelper.getFormattedDoctorList(mMonthName, monthArrayListHashMap);
                    if (formattedDoctorList.size() == 0) {
                        mDoctorListView.setVisibility(View.GONE);
                        mEmptyListView.setVisibility(View.VISIBLE);
                    } else {
                        mEmptyListView.setVisibility(View.GONE);
                        mDoctorListView.setVisibility(View.VISIBLE);
                        showDoctorListAdapter = new DoctorListAdapter(getActivity(), formattedDoctorList);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        mDoctorListView.setLayoutManager(layoutManager);
                        mDoctorListView.setHasFixedSize(true);
                        mDoctorListView.setAdapter(showDoctorListAdapter);
                    }
                } else {
                    mDoctorListView.setVisibility(View.GONE);
                    mEmptyListView.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
