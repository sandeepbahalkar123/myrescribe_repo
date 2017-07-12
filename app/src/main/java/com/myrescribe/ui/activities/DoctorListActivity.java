package com.myrescribe.ui.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.ui.fragments.DoctorListFragment;
import com.myrescribe.util.CommonMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 14/6/17.
 */

public class DoctorListActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.backArrow)
    ImageView mBackArrow;
    @BindView(R.id.tabFragment)
    TabLayout mTabFragment;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.year)
    TextView mYear;
    ArrayList<String> monthValues;
    ArrayList<String> yearValues;
    private String[] nameOfMonths = {"JAN", "FEB", "MAR", "APR", "MAY", "JUNE", "JULY", "AUG", "SEP", "OCT", "NOV", "DEC"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_activity);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        mBackArrow.setOnClickListener(this);
        setupViewPager(mViewpager);
        mTabFragment.setupWithViewPager(mViewpager);
        mTabFragment.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                String year = yearValues.get(position);
                mYear.setText(year);


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                String year = yearValues.get(position);
                mYear.setText(year);

            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        monthValues = new ArrayList<String>();
        yearValues = new ArrayList<String>();
        ArrayList<String> getMonthOfYearList = CommonMethods.getMonthsWithYear("2015-01-01", "2017-06-01", "yyyy-MM-dd");
        String startDate = "2015-01-01";
        String[] splitStartDate = startDate.split("-");
        String startD = splitStartDate[0];
        mYear.setText(startD);
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
            Fragment fragment = DoctorListFragment.createNewFragment(monthValues.get(i)); // pass data here
            adapter.addFragment(fragment, monthValues.get(i)); // pass title here
        }
        viewPager.setAdapter(adapter);
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