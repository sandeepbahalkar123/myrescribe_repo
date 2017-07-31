package com.myrescribe.ui.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.myrescribe.R;
import com.myrescribe.adapters.CustomSpinnerAdapter;
import com.myrescribe.adapters.filter.FilterCaseDetailsAdapter;
import com.myrescribe.adapters.filter.FilterDoctorSpecialitiesAdapter;
import com.myrescribe.adapters.filter.FilterDoctorsAdapter;
import com.myrescribe.helpers.database.AppDBHelper;
import com.myrescribe.helpers.doctor.DoctorHelper;
import com.myrescribe.helpers.filter.FilterHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.doctors.doctor_info.DoctorDetail;
import com.myrescribe.model.filter.CaseDetailsData;
import com.myrescribe.model.filter.CaseDetailsListModel;
import com.myrescribe.model.filter.DoctorData;
import com.myrescribe.model.filter.DoctorSpecialityData;
import com.myrescribe.model.filter.FilterDoctorListModel;
import com.myrescribe.model.filter.FilterDoctorSpecialityListModel;
import com.myrescribe.model.filter.filter_request.DrFilterRequestModel;
import com.myrescribe.model.login.LoginModel;
import com.myrescribe.model.login.Year;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.ui.fragments.DoctorListFragment;
import com.myrescribe.ui.fragments.filter.FilterFragment;
import com.myrescribe.ui.fragments.filter.SelectDoctorsFragment;
import com.myrescribe.ui.fragments.filter.SelectSpecialityFragment;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 14/6/17.
 */

public class DoctorListActivity extends AppCompatActivity implements HelperResponse, FilterFragment.OnDrawerInteractionListener, SelectDoctorsFragment.OnSelectDoctorInteractionListener, SelectSpecialityFragment.OnSelectSpecialityInteractionListener, FilterDoctorsAdapter.ItemClickListener, FilterDoctorSpecialitiesAdapter.ItemClickListener, FilterCaseDetailsAdapter.ItemClickListener {

    @BindView(R.id.backArrow)
    ImageView mBackArrow;
    @BindView(R.id.tabFragment)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    private CustomSpinnerAdapter mCustomSpinAdapter;
    @BindView(R.id.year)
    Spinner mYearSpinnerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    // Filter Start

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    FrameLayout nav_view;

    // Filter End

    private ArrayList<String> mYearList = new ArrayList<>();
    private ArrayList<Year> mTimePeriodList = new ArrayList<>();
    private Year mCurrentSelectedTimePeriodTab;
    private DoctorHelper mDoctorHelper;
    private ViewPagerAdapter mViewPagerAdapter;
    private HashSet<String> mGeneratedRequestForYearList = new HashSet<>();

    private FragmentManager fragmentManager;
    private FilterFragment filterFragment;
    private FilterDoctorListModel filterDoctorListModel = new FilterDoctorListModel();
    private FilterDoctorSpecialityListModel filterDoctorSpecialityListModel = new FilterDoctorSpecialityListModel();
    private CaseDetailsListModel caseDetailsListModel = new CaseDetailsListModel();

    private ArrayList<String> caseList = new ArrayList<>();
    private ArrayList<String> doctorSpecialityList = new ArrayList<>();
    private ArrayList<Integer> docIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_activity);
        ButterKnife.bind(this);
        initialize();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    private void initialize() {

        // Filter Start

        FilterHelper filterHelper = new FilterHelper(this);
        filterHelper.getDoctorList();
        filterHelper.getDoctorSpecialityList();
        filterHelper.getCaseDetailsList();

        // Filter End

        mYearList = CommonMethods.getYearForDoctorList();

        mCustomSpinAdapter = new CustomSpinnerAdapter(this, mYearList);
        mYearSpinnerView.setAdapter(mCustomSpinAdapter);
        YearSpinnerInteractionListener listener = new YearSpinnerInteractionListener();
        mYearSpinnerView.setOnTouchListener(listener);
        mYearSpinnerView.setOnItemSelectedListener(listener);
        //-------
        mDoctorHelper = new DoctorHelper(this);
        //-------
        mCurrentSelectedTimePeriodTab = new Year();
        mCurrentSelectedTimePeriodTab.setMonthName(new SimpleDateFormat("MMM", Locale.US).format(new Date()));
        mCurrentSelectedTimePeriodTab.setYear(new SimpleDateFormat("yyyy", Locale.US).format(new Date()));
        //-------
        //----

        AppDBHelper appDBHelper = new AppDBHelper(DoctorListActivity.this);

        if (appDBHelper.dataTableNumberOfRows(MyRescribeConstants.TASK_LOGIN) > 0) {
            Cursor cursor = appDBHelper.getData(MyRescribeConstants.TASK_LOGIN);
            cursor.moveToFirst();
            String loginData = cursor.getString(cursor.getColumnIndex(AppDBHelper.COLUMN_DATA));
            Gson gson = new Gson();
            LoginModel loginModel = gson.fromJson(loginData, LoginModel.class);
            mTimePeriodList = loginModel.getYearList();

        }

        if (mTimePeriodList.size() < 6){
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
            mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }else {
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        }

        //---------
        //----
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mTabLayout.setupWithViewPager(mViewpager);
    }

    private void setupViewPager() {
        mViewPagerAdapter.mFragmentList.clear();
        mViewPagerAdapter.mFragmentTitleList.clear();
        for (Year data :
                mTimePeriodList) {
            Fragment fragment = DoctorListFragment.createNewFragment(data); // pass data here
            mViewPagerAdapter.addFragment(fragment, data); // pass title here
        }
        mViewpager.setOffscreenPageLimit(0);
        mViewpager.setAdapter(mViewPagerAdapter);

        //------------
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                DoctorListFragment item = (DoctorListFragment) mViewPagerAdapter.getItem(position);
                Bundle arguments = item.getArguments();
                String month = arguments.getString(MyRescribeConstants.MONTH);
                String year = arguments.getString(MyRescribeConstants.YEAR);
                CommonMethods.Log("onPageSelected", month + " " + year);
                mCurrentSelectedTimePeriodTab.setMonthName(month);
                mCurrentSelectedTimePeriodTab.setYear(year);

                for (int i = 0; i < mYearList.size(); i++) {
                    if (mYearList.get(i).equalsIgnoreCase(year)) {
                        mYearSpinnerView.setSelection(i);
                        break;
                    }
                }

                //-----THis condition calls API only once for that specific year.----
                if (!mGeneratedRequestForYearList.contains(year)) {
                    Map<String, Map<String, ArrayList<DoctorDetail>>> yearWiseSortedDoctorList = mDoctorHelper.getYearWiseSortedDoctorList();
                    if (yearWiseSortedDoctorList.get(year) == null) {
                        mGeneratedRequestForYearList.add(year);
                        mDoctorHelper.doGetDoctorList(year);
                    }
                }
                //---------
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mTimePeriodList.size(); i++) {
                    Year temp = mTimePeriodList.get(i);
                    if (temp.getYear().equalsIgnoreCase(mCurrentSelectedTimePeriodTab.getYear()) &&
                            temp.getMonthName().equalsIgnoreCase(mCurrentSelectedTimePeriodTab.getMonthName())) {
                        mViewpager.setCurrentItem(i);
                        break;
                    } else if (temp.getYear().equalsIgnoreCase(mCurrentSelectedTimePeriodTab.getYear())) {
                        mViewpager.setCurrentItem(i);
                        break;
                    }
                }
            }
        }, 0);
        //---------
    }

    // Filter Start

    @Override
    public void onApply() {

        DrFilterRequestModel drFilterRequestModel = new DrFilterRequestModel();
        drFilterRequestModel.setPatientId(Integer.parseInt(MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATEINT_ID, DoctorListActivity.this)));
        drFilterRequestModel.setDocId(docIdList);
        drFilterRequestModel.setDocSpeciality(doctorSpecialityList);
        drFilterRequestModel.setStartDate(filterFragment.getFromDate());
        drFilterRequestModel.setEndDate(filterFragment.getToDate());
        drFilterRequestModel.setCases(caseList);

        Gson gson = new Gson();
        CommonMethods.Log("FilterRequest", gson.toJson(drFilterRequestModel, DrFilterRequestModel.class));

        drawer.closeDrawer(GravityCompat.END);
        Intent intent = new Intent(DoctorListActivity.this, DoctorFilteredListActivity.class);
        intent.putExtra(MyRescribeConstants.FILTER_REQUEST, drFilterRequestModel);
        startActivity(intent);

    }

    @Override
    public void onSelectDoctors() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SelectDoctorsFragment selectDoctorsFragment = SelectDoctorsFragment.newInstance(filterDoctorListModel.getData(), getResources().getString(R.string.doctors));
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.add(R.id.nav_view, selectDoctorsFragment, getResources().getString(R.string.doctors));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onSelectSpeciality() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SelectSpecialityFragment selectSpecialityFragment = SelectSpecialityFragment.newInstance(filterDoctorSpecialityListModel.getDoctorSpecialityData(), getResources().getString(R.string.doctors_speciality));
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.add(R.id.nav_view, selectSpecialityFragment, getResources().getString(R.string.doctors_speciality));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onReset() {

        docIdList.clear();
        doctorSpecialityList.clear();
        caseList.clear();

        for (DoctorData doctorDetail : filterDoctorListModel.getData()) {
            doctorDetail.setSelected(false);
        }

        for (DoctorSpecialityData doctorSpecialityData : filterDoctorSpecialityListModel.getDoctorSpecialityData()) {
            doctorSpecialityData.setSelected(false);
        }

        for (CaseDetailsData caseDetails : caseDetailsListModel.getCaseDetailsDatas())
            caseDetails.setSelected(false);

        filterFragment.notifyCaseDetails();
    }

    @Override
    public void onFragmentBack() {
        fragmentManager.popBackStack();
    }

    @Override
    public void setDoctorSpeciality(String speciality) {
        filterFragment.setDoctorSpeciality(speciality);
    }

    @Override
    public void setDoctorName(String name) {
        filterFragment.setDoctorName(name);
    }

    @Override
    public void onDoctorClick() {

        docIdList.clear();

        String doctorName = getResources().getString(R.string.select_doctors);
        int count = 0;
        for (DoctorData doctorDetail : filterDoctorListModel.getData()) {
            if (doctorDetail.isSelected()) {

                docIdList.add(doctorDetail.getId());

                count++;
                if (count == 1)
                    doctorName = doctorDetail.getDoctorName();
            }

        }
        if (count > 1)
            filterFragment.setDoctorName(doctorName + " + " + (count - 1));
        else if (count == 1) filterFragment.setDoctorName(doctorName);
        else filterFragment.setDoctorName(doctorName);
    }

    @Override
    public void onDoctorSpecialityClick() {

        doctorSpecialityList.clear();

        String doctorSpeciality = getResources().getString(R.string.select_doctors_speciality);
        int count = 0;
        for (DoctorSpecialityData doctorSpecialityData : filterDoctorSpecialityListModel.getDoctorSpecialityData()) {
            if (doctorSpecialityData.isSelected()) {

                doctorSpecialityList.add(doctorSpecialityData.getSpeciality());

                count++;
                if (count == 1)
                    doctorSpeciality = doctorSpecialityData.getSpeciality();
            }

        }
        if (count > 1)
            filterFragment.setDoctorSpeciality(doctorSpeciality + " + " + (count - 1));
        else if (count == 1) filterFragment.setDoctorSpeciality(doctorSpeciality);
        else filterFragment.setDoctorSpeciality(doctorSpeciality);
    }

    @Override
    public void onCaseClick() {

        caseList.clear();

        for (CaseDetailsData caseDetailsData : caseDetailsListModel.getCaseDetailsDatas()) {
            if (caseDetailsData.isSelected())
                caseList.add(caseDetailsData.getName());
        }
    }

    @OnClick({R.id.backArrow, R.id.fab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                finish();
                break;
            case R.id.fab:
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
                break;
        }
    }

    // Filter End

    //---------------
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<Year> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, Year title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position).getMonthName();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    private class YearSpinnerInteractionListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {

        boolean mYearSpinnerConfigChange = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mYearSpinnerConfigChange = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (mYearSpinnerConfigChange) {
                // Your selection handling code here
                mYearSpinnerConfigChange = false;
                if (parent.getId() == R.id.year && !mYearSpinnerConfigChange) {
                    String selectedYear = mYearList.get(parent.getSelectedItemPosition());
                    for (int i = 0; i < mTimePeriodList.size(); i++) {
                        if (mTimePeriodList.get(i).getYear().equalsIgnoreCase("" + selectedYear)) {
                            mCurrentSelectedTimePeriodTab = mTimePeriodList.get(i);
                            mViewpager.setCurrentItem(i);
                            break;
                        }
                    }
                } else {
                    mYearSpinnerConfigChange = false;
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    //---------------
    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        //  mViewPagerAdapter.notifyDataSetChanged();
        if (customResponse instanceof FilterDoctorListModel) {
            filterDoctorListModel = (FilterDoctorListModel) customResponse;

        } else if (customResponse instanceof FilterDoctorSpecialityListModel) {
            filterDoctorSpecialityListModel = (FilterDoctorSpecialityListModel) customResponse;
        } else if (customResponse instanceof CaseDetailsListModel) {
            caseDetailsListModel = (CaseDetailsListModel) customResponse;

            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            filterFragment = FilterFragment.newInstance(caseDetailsListModel.getCaseDetailsDatas());
            fragmentTransaction.add(R.id.nav_view, filterFragment, "Filter");
            fragmentTransaction.commit();

        } else
        setupViewPager();
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
        if (!mGeneratedRequestForYearList.contains(mCurrentSelectedTimePeriodTab.getYear())) {
            Map<String, Map<String, ArrayList<DoctorDetail>>> yearWiseSortedDoctorList = mDoctorHelper.getYearWiseSortedDoctorList();
            if (yearWiseSortedDoctorList.get(mCurrentSelectedTimePeriodTab.getYear()) == null) {
                mDoctorHelper.doGetDoctorList(mCurrentSelectedTimePeriodTab.getYear());
                mGeneratedRequestForYearList.add(mCurrentSelectedTimePeriodTab.getYear());
            }
        }
    }

    public DoctorHelper getParentDoctorHelper() {
        return mDoctorHelper;
    }
}