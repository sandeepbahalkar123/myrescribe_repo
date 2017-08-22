package com.rescribe.ui.fragments.doctor;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.adapters.DoctorFilteredExpandableList;
import com.rescribe.helpers.doctor.DoctorHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.doctors.filter_doctor_list.DoctorFilterModel;
import com.rescribe.model.doctors.filter_doctor_list.DoctorFilteredInfoAndCaseDetails;
import com.rescribe.model.filter.filter_request.DrFilterRequestModel;
import com.rescribe.ui.activities.DoctorListActivity;
import com.rescribe.util.MyRescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DoctorFilteredListFragment extends Fragment implements HelperResponse {

    private DoctorHelper mDoctorHelper;
    @BindView(R.id.expandFilterDocListView)
    ExpandableListView mExpandFilterDocListView;
    @BindView(R.id.emptyListView)
    RelativeLayout mEmptyListView;
    @BindView(R.id.docFilterToolbar)
    Toolbar mDocFilterToolbar;
    private Context mContext;
    private DrFilterRequestModel mRequestedFilterRequestModel;
    private DoctorListActivity mParentActivity;

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
        supportActionBar.setTitle(getString(R.string.doctor_details) + getString(R.string.details));
        setHasOptionsMenu(true);

        mParentActivity = (DoctorListActivity) getActivity();

        Bundle arguments = getArguments();
        DrFilterRequestModel tempReceivedObject = new DrFilterRequestModel();
        if (arguments != null) {
            tempReceivedObject = arguments.getParcelable(MyRescribeConstants.FILTER_REQUEST);
        }
        initialize(tempReceivedObject);
        return mRootView;
    }


    public void initialize(DrFilterRequestModel tempReceivedObject) {
        this.mRequestedFilterRequestModel = tempReceivedObject;
        mDoctorHelper = new DoctorHelper(mContext, this);
        mDoctorHelper.doFilterDoctorList(mRequestedFilterRequestModel);

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
        mEmptyListView.setVisibility(View.VISIBLE);
        mExpandFilterDocListView.setVisibility(View.GONE);
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {

    }
    //---------------

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

    @OnClick({R.id.fab})
    public void onViewClicked(View view) {

        DrawerLayout activityDrawer = mParentActivity.getActivityDrawer();

        switch (view.getId()) {

            case R.id.fab:
                if (activityDrawer.isDrawerOpen(GravityCompat.END)) {
                    activityDrawer.closeDrawer(GravityCompat.END);
                } else {
                    activityDrawer.openDrawer(GravityCompat.END);
                }
                break;
        }
    }
}
