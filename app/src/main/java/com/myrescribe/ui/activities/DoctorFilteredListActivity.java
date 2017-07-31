package com.myrescribe.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.adapters.CustomSpinnerAdapter;
import com.myrescribe.adapters.DoctorFilteredExpandableList;
import com.myrescribe.helpers.doctor.DoctorHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.doctors.doctor_info.DoctorDetail;
import com.myrescribe.model.doctors.filter_doctor_list.DoctorFilterModel;
import com.myrescribe.model.doctors.filter_doctor_list.DoctorFilteredInfo;
import com.myrescribe.model.filter.filter_request.DrFilterRequestModel;
import com.myrescribe.model.util.TimePeriod;
import com.myrescribe.ui.fragments.DoctorListFragment;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 14/6/17.
 */

public class DoctorFilteredListActivity extends AppCompatActivity implements HelperResponse, View.OnClickListener {

    private ActionBar mActionBar;
    private DoctorHelper mDoctorHelper;
    @BindView(R.id.expandFilterDocListView)
    ExpandableListView mExpandFilterDocListView;
    @BindView(R.id.emptyListView)
    TextView mEmptyListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_filtered_list_activity);
        ButterKnife.bind(this);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(getString(R.string.doctor_details) + getString(R.string.details));
        initialize();
    }

    private void initialize() {
        DrFilterRequestModel drFilterRequestModel = getIntent().getParcelableExtra(MyRescribeConstants.FILTER_REQUEST);
        mDoctorHelper = new DoctorHelper(this);
    }


    @Override
    public void onClick(View v) {

    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        DoctorFilterModel model = (DoctorFilterModel) customResponse;
        if (model != null) {
            ArrayList<DoctorFilteredInfo> doctorFilteredInfoList = model.getDoctorFilteredInfoList();
            if (doctorFilteredInfoList != null) {
                setListAdapter(doctorFilteredInfoList);
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
    protected void onResume() {
        super.onResume();
        mDoctorHelper.doFilterDoctorList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void setListAdapter(ArrayList<DoctorFilteredInfo> doctorFilteredInfoList) {
        if (doctorFilteredInfoList.size() == 0) {
            mExpandFilterDocListView.setVisibility(View.GONE);
            mEmptyListView.setVisibility(View.VISIBLE);
        } else {
            mEmptyListView.setVisibility(View.GONE);
            mExpandFilterDocListView.setVisibility(View.VISIBLE);
            DoctorFilteredExpandableList doctorFilteredExpandableList = new DoctorFilteredExpandableList(this, doctorFilteredInfoList);
            mExpandFilterDocListView.setAdapter(doctorFilteredExpandableList);
        }
    }
}
