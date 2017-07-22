package com.myrescribe.ui.activities;

import android.os.Bundle;
import android.os.Handler;
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

import com.myrescribe.R;
import com.myrescribe.adapters.CustomSpinnerAdapter;
import com.myrescribe.helpers.doctor.DoctorHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.doctors.doctor_info.DoctorDetail;
import com.myrescribe.model.util.TimePeriod;
import com.myrescribe.ui.fragments.DoctorListFragment;
import com.myrescribe.ui.fragments.filter.FilterFragment;
import com.myrescribe.ui.fragments.filter.SelectDoctorsOrSpecialityFragment;
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

public class DoctorListActivity extends AppCompatActivity implements HelperResponse, View.OnClickListener, FilterFragment.OnDrawerInteractionListener, SelectDoctorsOrSpecialityFragment.OnSelectDoctorInteractionListener {

    @BindView(R.id.backArrow)
    ImageView mBackArrow;
    @BindView(R.id.tabFragment)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    private CustomSpinnerAdapter mCustomSpinAdapter;
    @BindView(R.id.year)
    Spinner mYearSpinnerView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    FrameLayout nav_view;

    private ArrayList<String> mYearList;
    private ArrayList<TimePeriod> mTimePeriodList;
    private TimePeriod mCurrentSelectedTimePeriodTab;
    private DoctorHelper mDoctorHelper;
    private ViewPagerAdapter mViewPagerAdapter;
    private HashSet<String> mGeneratedRequestForYearList = new HashSet<>();
    private FragmentManager fragmentManager;

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
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FilterFragment filterFragment = FilterFragment.newInstance();
        fragmentTransaction.add(R.id.nav_view, filterFragment, "Filter");
        fragmentTransaction.commit();

        mYearList = CommonMethods.getYearForDoctorList();
        mBackArrow.setOnClickListener(this);

        mCustomSpinAdapter = new CustomSpinnerAdapter(this, mYearList);
        mYearSpinnerView.setAdapter(mCustomSpinAdapter);
        YearSpinnerInteractionListener listener = new YearSpinnerInteractionListener();
        mYearSpinnerView.setOnTouchListener(listener);
        mYearSpinnerView.setOnItemSelectedListener(listener);
        //-------
        mDoctorHelper = new DoctorHelper(this, this);
        //-------
        mCurrentSelectedTimePeriodTab = new TimePeriod();
        mCurrentSelectedTimePeriodTab.setMonthName(new SimpleDateFormat("MMM").format(new Date()));
        mCurrentSelectedTimePeriodTab.setYear(new SimpleDateFormat("yyyy").format(new Date()));
        //-------
        //----
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        if (year == Integer.parseInt(mYearList.get(mYearList.size() - 1))) {
            mTimePeriodList = CommonMethods.getMonthsWithYear(mYearList.get(0) + "-01-01", year + "-" + month + "-01", MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD);
        } else {
            mTimePeriodList = CommonMethods.getMonthsWithYear(mYearList.get(0) + "-01-01", mYearList.get(mYearList.size() - 1) + "-12-01", MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD);
        }
        //---------
        //----
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mTabLayout.setupWithViewPager(mViewpager);
    }

    private void setupViewPager() {
        for (TimePeriod data :
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
                    TimePeriod temp = mTimePeriodList.get(i);
                    if (temp.getYear().equalsIgnoreCase(mCurrentSelectedTimePeriodTab.getYear()) &&
                            temp.getMonthName().equalsIgnoreCase(mCurrentSelectedTimePeriodTab.getMonthName())) {
                        mViewpager.setCurrentItem(i);
                        break;
                    }
                }
            }
        }, 00);
        //---------
    }

    @Override
    public void onDrawerClose() {
        drawer.closeDrawer(GravityCompat.END);
    }

    @Override
    public void onSelectDoctors() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SelectDoctorsOrSpecialityFragment selectDoctorsOrSpecialityFragment = SelectDoctorsOrSpecialityFragment.newInstance(getResources().getString(R.string.select_doctors));
        fragmentTransaction.add(R.id.nav_view, selectDoctorsOrSpecialityFragment, getResources().getString(R.string.select_doctors));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onSelectSpeciality() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SelectDoctorsOrSpecialityFragment selectDoctorsOrSpecialityFragment = SelectDoctorsOrSpecialityFragment.newInstance(getResources().getString(R.string.select_doctors_speciality));
        fragmentTransaction.add(R.id.nav_view, selectDoctorsOrSpecialityFragment, getResources().getString(R.string.select_doctors_speciality));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBack() {
        fragmentManager.popBackStack();
    }

    //---------------
    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<TimePeriod> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

        public void addFragment(Fragment fragment, TimePeriod title) {
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

    //---------------

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.backArrow:
                finish();
                break;
        }
    }

    //---------------
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