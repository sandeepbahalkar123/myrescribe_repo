package com.myrescribe.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.myrescribe.R;
import com.myrescribe.helpers.doctor.DoctorHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.doctors.DoctorDetail;
import com.myrescribe.model.util.TimePeriod;
import com.myrescribe.ui.fragments.DoctorListFragment;
import com.myrescribe.adapters.CustomSpinnerAdapter;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.lang.reflect.Array;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 14/6/17.
 */

public class DoctorListActivity extends AppCompatActivity implements HelperResponse, View.OnClickListener {

    @BindView(R.id.backArrow)
    ImageView mBackArrow;
    @BindView(R.id.tabFragment)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    private CustomSpinnerAdapter mCustomSpinAdapter;
    @BindView(R.id.year)
    Spinner mYearSpinnerView;
    private ArrayList<String> mYearList;
    private ArrayList<TimePeriod> mTimePeriodList;
    private TimePeriod mCurrentSelectedTimePeriodTab;
    private DoctorHelper mDoctorHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_activity);
        ButterKnife.bind(this);

        initialize();
    }

    private void initialize() {
        mYearList = CommonMethods.getYearForDoctorList();
        mBackArrow.setOnClickListener(this);
        //----
        setupViewPager();
        mTabLayout.setupWithViewPager(mViewpager);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mCurrentSelectedTimePeriodTab = mTimePeriodList.get(position);
                for (int i = 0; i < mYearList.size(); i++) {
                    if (mYearList.get(i).equalsIgnoreCase(mCurrentSelectedTimePeriodTab.getYear())) {
                        mYearSpinnerView.setSelection(i);
                        break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //----
        mCustomSpinAdapter = new CustomSpinnerAdapter(this, mYearList);
        mYearSpinnerView.setAdapter(mCustomSpinAdapter);
        YearSpinnerInteractionListener listener = new YearSpinnerInteractionListener();
        mYearSpinnerView.setOnTouchListener(listener);
        mYearSpinnerView.setOnItemSelectedListener(listener);
        //-------
        mDoctorHelper = new DoctorHelper(this, this);
        //-------
    }

    private void setupViewPager() {
        //----
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        //----
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        if (year == Integer.parseInt(mYearList.get(mYearList.size() - 1))) {
            mTimePeriodList = CommonMethods.getMonthsWithYear(mYearList.get(0) + "-01-01", year + "-" + month + "-01", MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD);
        } else {
            mTimePeriodList = CommonMethods.getMonthsWithYear(mYearList.get(0) + "-01-01", mYearList.get(mYearList.size() - 1) + "-12-01", MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD);
        }
        //---------
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (TimePeriod data :
                mTimePeriodList) {
            Fragment fragment = DoctorListFragment.createNewFragment(data); // pass data here
            adapter.addFragment(fragment, data); // pass title here
        }
        mViewpager.setOffscreenPageLimit(0);
        mViewpager.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewpager.setCurrentItem(mTimePeriodList.size() - 1);
            }
        }, 00);
        //---------

    }

    //---------------
    class ViewPagerAdapter extends FragmentPagerAdapter {
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

    public TimePeriod getCurrentSelectedTimePeriodTab() {
        return mCurrentSelectedTimePeriodTab;
    }

    //---------------
    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

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
        if (mCurrentSelectedTimePeriodTab != null) {
            Map<String, Map<String, ArrayList<DoctorDetail>>> yearWiseSortedDoctorList = mDoctorHelper.getYearWiseSortedDoctorList();
            if (yearWiseSortedDoctorList.get(mCurrentSelectedTimePeriodTab.getYear()) == null) {
                mDoctorHelper.doGetDoctorList();
            }
        } else {
            mDoctorHelper.doGetDoctorList();
        }
    }

    public DoctorHelper getParentDoctorHelper() {
        return mDoctorHelper;
    }
}