package com.myrescribe.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.myrescribe.R;
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
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 14/6/17.
 */

public class DoctorListActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    @BindView(R.id.backArrow)
    ImageView mBackArrow;
    @BindView(R.id.tabFragment)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    private CustomSpinnerAdapter mCustomSpinAdapter;
    @BindView(R.id.year)
    Spinner mYearSpinner;

    private ArrayList<String> mYearList;
    private ArrayList<TimePeriod> mTimePeriodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_activity);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        mYearList = CommonMethods.getYearForDoctorList();
        mYearSpinner.setOnItemSelectedListener(this);
        mBackArrow.setOnClickListener(this);
        //----
        mCustomSpinAdapter = new CustomSpinnerAdapter(this, mYearList);
        mYearSpinner.setAdapter(mCustomSpinAdapter);
        //----
        setupViewPager();
        mTabLayout.setupWithViewPager(mViewpager);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
//                String year = yearValues.get(position);
//                mYear.setSelection(allYearList.indexOf(yearValues.get(position)));
                TimePeriod timePeriod = mTimePeriodList.get(position);
                for (int i = 0; i < mYearList.size(); i++) {
                    if (mYearList.get(i).equalsIgnoreCase(timePeriod.getYear())) {
                        mYearSpinner.setSelection(i);
                        break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                int position = tab.getPosition();
//                String year = yearValues.get(position);
//                mYear.setSelection(allYearList.indexOf(year));
            }
        });

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
        mViewpager.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewpager.setCurrentItem(mTimePeriodList.size() - 1);
            }
        }, 100);
        //---------

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        /*if (parent.getId() == R.id.year) {
            String selectedYear = mYearList.get(parent.getSelectedItemPosition());

            for (int i = 0; i < mTimePeriodList.size(); i++) {
                if (mTimePeriodList.get(i).getYear().equalsIgnoreCase("" + selectedYear)) {
                    mViewpager.setCurrentItem(i);
                    break;
                }
            }
        }*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.backArrow:
                finish();
                break;
        }
    }
}