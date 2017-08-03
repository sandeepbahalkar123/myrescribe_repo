package com.myrescribe.ui.fragments.doctor;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.adapters.DoctorFilteredExpandableList;
import com.myrescribe.adapters.DoctorListAdapter;
import com.myrescribe.helpers.doctor.DoctorHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.doctors.doctor_info.DoctorDetail;
import com.myrescribe.model.doctors.filter_doctor_list.DoctorFilterModel;
import com.myrescribe.model.doctors.filter_doctor_list.DoctorFilteredInfoAndCaseDetails;
import com.myrescribe.model.filter.filter_request.DrFilterRequestModel;
import com.myrescribe.model.login.Year;
import com.myrescribe.ui.activities.DoctorListActivity;
import com.myrescribe.util.MyRescribeConstants;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DoctorFilteredListFragment extends Fragment implements HelperResponse {

    private DoctorHelper mDoctorHelper;
    @BindView(R.id.expandFilterDocListView)
    ExpandableListView mExpandFilterDocListView;
    @BindView(R.id.emptyListView)
    TextView mEmptyListView;
    @BindView(R.id.docFilterToolbar)
    Toolbar mDocFilterToolbar;
    private Context mContext;
    private DrFilterRequestModel mRequestedFilterRequestModel;

    public DoctorFilteredListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mRootView = inflater.inflate(R.layout.doctor_filtered_list_activity, container, false);
        ButterKnife.bind(this, mRootView);
        mContext = inflater.getContext();

        ((AppCompatActivity) getActivity()).setSupportActionBar(mDocFilterToolbar);
        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        initialize();
        return mRootView;
    }


    private void initialize() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mRequestedFilterRequestModel = arguments.getParcelable(MyRescribeConstants.FILTER_REQUEST);
        }
        mDoctorHelper = new DoctorHelper(mContext, this);
    }

    public static DoctorFilteredListFragment newInstance(DrFilterRequestModel drFilterRequestModel) {
        DoctorFilteredListFragment fragment = new DoctorFilteredListFragment();
        Bundle b = new Bundle();
        b.putParcelable(MyRescribeConstants.FILTER_REQUEST, drFilterRequestModel);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        DoctorFilterModel model = (DoctorFilterModel) customResponse;
        if (model != null) {
            ArrayList<DoctorFilteredInfoAndCaseDetails> doctorsInfoAndCaseDetailsList = model.getDoctorsInfoAndCaseDetailsList();
            if (doctorsInfoAndCaseDetailsList != null) {
                setListAdapter(doctorsInfoAndCaseDetailsList);
            }
        }
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
    //---------------


    @Override
    public void onResume() {
        super.onResume();
        mDoctorHelper.doFilterDoctorList(mRequestedFilterRequestModel);
    }

    private void setListAdapter(ArrayList<DoctorFilteredInfoAndCaseDetails> doctorFilteredInfoList) {
        if (doctorFilteredInfoList.size() == 0) {
            mExpandFilterDocListView.setVisibility(View.GONE);
            mEmptyListView.setVisibility(View.VISIBLE);
        } else {
            mEmptyListView.setVisibility(View.GONE);
            mExpandFilterDocListView.setVisibility(View.VISIBLE);
            DoctorFilteredExpandableList doctorFilteredExpandableList = new DoctorFilteredExpandableList(mContext, doctorFilteredInfoList);
            mExpandFilterDocListView.setAdapter(doctorFilteredExpandableList);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getActivity().getSupportFragmentManager().popBackStack();
        return super.onOptionsItemSelected(item);
    }
}
