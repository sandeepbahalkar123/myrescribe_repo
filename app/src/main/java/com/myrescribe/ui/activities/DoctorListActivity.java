package com.myrescribe.ui.activities;

import android.os.Bundle;
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

import com.myrescribe.R;
import com.myrescribe.adapters.CustomSpinnerAdapter;
import com.myrescribe.model.doctors.DoctorDetail;
import com.myrescribe.ui.fragments.DynamicFragment;
import com.myrescribe.util.CommonMethods;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 14/6/17.
 */

public class DoctorListActivity extends AppCompatActivity implements View.OnClickListener ,AdapterView.OnItemSelectedListener{

    @BindView(R.id.backArrow)
    ImageView mBackArrow;
    @BindView(R.id.tabFragment)
    TabLayout mTabFragment;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    private CustomSpinnerAdapter mCustomSpinAdapter;
    @BindView(R.id.year)
    Spinner mYear;
    ArrayList<String> monthValues;
    ArrayList<String> yearValues;
    private ArrayList<String> allMonthsList ;
    private String mSelectedYear;
    ArrayList<String> getMonthOfYearList;

    public boolean isSetCurrentTabSetFirstTime() {
        return isSetCurrentTabSetFirstTime;
    }

    public void setSetCurrentTabSetFirstTime(boolean setCurrentTabSetFirstTime) {
        isSetCurrentTabSetFirstTime = setCurrentTabSetFirstTime;
    }

    private boolean isSetCurrentTabSetFirstTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_activity);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        allMonthsList = new ArrayList<>();
        allMonthsList.add("2017");
        allMonthsList.add("2016");
        allMonthsList.add("2015");
        mYear.setOnItemSelectedListener(this);
        mBackArrow.setOnClickListener(this);
        mCustomSpinAdapter = new CustomSpinnerAdapter(this, allMonthsList);
        mYear.setAdapter(mCustomSpinAdapter);
        setupViewPager(mViewpager);
        mTabFragment.setupWithViewPager(mViewpager);
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
        mTabFragment.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                String year = yearValues.get(position);
              mYear.setSelection(allMonthsList.indexOf(yearValues.get(position)));


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                String year = yearValues.get(position);
                mYear.setSelection(allMonthsList.indexOf(year));
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        monthValues = new ArrayList<String>();
        yearValues = new ArrayList<String>();
        getMonthOfYearList = CommonMethods.getMonthsWithYear("2015-01-01", "2017-06-01", "yyyy-MM-dd");
// add elements to al, including duplicates

        String startDate = "2017-06-01";
        String[] splitStartDate = startDate.split("-");
        mYear.setSelection(0);
        for (int j = 0; j < getMonthOfYearList.size(); j++) {
            String monthyear = getMonthOfYearList.get(j);
            String[] splitValues = monthyear.split("-");
            String month = splitValues[0];
            String year = splitValues[1];
            monthValues.add(month);
            yearValues.add(year);

        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < getMonthOfYearList.size(); i++) {
            Fragment fragment = DynamicFragment.createNewFragment(getMonthOfYearList.get(i)); // pass data here
            adapter.addFragment(fragment, monthValues.get(i)); // pass title here
        }
        viewPager.setAdapter(adapter);
        Set<String> hs = new HashSet<>();
        hs.addAll(monthValues);
        monthValues.clear();
        monthValues.addAll(hs);


       viewPager.setCurrentItem(getMonthOfYearList.size()-1);

    }


    //TODO : Here main lengthy parsing begins to format list properly.
    private ArrayList<DoctorDetail> formatResponseDataForAdapter(HashMap<String, ArrayList<DoctorDetail>> dataList) {
        ArrayList<DoctorDetail> formattedDoctorList = new ArrayList<>();
        for (String key : dataList.keySet()) {
            boolean flag = true;
            System.out.println(key);
            ArrayList<DoctorDetail> doctorDetails = dataList.get(key);
            for (DoctorDetail dataObject : doctorDetails) {
                if (flag) {
                    dataObject.setIsStartElement(true);
                    flag = false;
                }
                dataObject.setRespectiveDate(key);
                formattedDoctorList.add(dataObject);
            }
        }
        return formattedDoctorList;
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.year) {
            int indexSselectedId = parent.getSelectedItemPosition();
            mSelectedYear = allMonthsList.get(indexSselectedId);
            String monthYear = "JAN-"+mSelectedYear;

            for(int i = 0 ; i<getMonthOfYearList.size();i++) {
                if (monthYear.equalsIgnoreCase(getMonthOfYearList.get(i))) {
                    if(isSetCurrentTabSetFirstTime) {
                        mViewpager.setCurrentItem(i);
                    }
                    break;
                }


            }
            setSetCurrentTabSetFirstTime(true);
            //mViewpager.setCurrentItem(indexSselectedId);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

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

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}