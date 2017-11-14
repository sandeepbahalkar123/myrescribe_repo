package com.rescribe.ui.activities.dashboard;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.rescribe.R;
import com.rescribe.ui.activities.AppointmentActivity;
import com.rescribe.ui.fragments.AppointmentFragment;
import com.rescribe.ui.fragments.health_offers.HealthOffersFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 14/11/17.
 */

public class HealthOffersActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.healthOffersViewpager)
    ViewPager healthOffersViewpager;
    String[] mFragmentTitleList = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_offers_base_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra(getString(R.string.toolbarTitle)));
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mFragmentTitleList[0] = getString(R.string.consultation);
        mFragmentTitleList[1] = getString(R.string.medicines);
        mFragmentTitleList[2] = getString(R.string.lab_test);
        setupViewPager();

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(healthOffersViewpager);

        initialize();
    }

    private void initialize() {

    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < mFragmentTitleList.length; i++) {
            Fragment fragment = HealthOffersFragment.newInstance(mFragmentTitleList[i]); // pass data here
            adapter.addFragment(fragment, mFragmentTitleList[i]); // pass title here
        }
        healthOffersViewpager.setAdapter(adapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(/*{R.id.myRecordsLayout, R.id.vitalGraphsLayout, R.id.doctorVisitsLayout, R.id.savedArticlesLayout}*/)
    public void onViewClicked(View view) {
        switch (view.getId()) {
          /*  case R.id.myRecordsLayout:
                MyRecordsData myRecordsData = appDBHelper.getMyRecordsData();
                int completeCount = 0;
                for (Image image : myRecordsData.getImageArrayList()) {
                    if (image.isUploading() == RescribeConstants.COMPLETED)
                        completeCount++;
                }
                Intent intent;
                if (completeCount == myRecordsData.getImageArrayList().size()) {
                    appDBHelper.deleteMyRecords();
                    intent = new Intent(HealthOffersActivity.this, MyRecordsActivity.class);
                } else {
                    intent = new Intent(HealthOffersActivity.this, SelectedRecordsGroupActivity.class);
                    intent.putExtra(RescribeConstants.UPLOADING_STATUS, true);
                    intent.putExtra(RescribeConstants.VISIT_DATE, myRecordsData.getVisitDate());
                    intent.putExtra(RescribeConstants.OPD_ID, myRecordsData.getDocId());
                    intent.putExtra(RescribeConstants.DOCTORS_ID, myRecordsData.getDocId());
                    intent.putExtra(RescribeConstants.DOCUMENTS, myRecordsData.getImageArrayList());
                }
                startActivity(intent);
                break;
            case R.id.vitalGraphsLayout:
                Intent intentVital = new Intent(HealthOffersActivity.this, VitalGraphActivity.class);
                startActivity(intentVital);
                break;
            case R.id.doctorVisitsLayout:
                Intent intentDoctorVisit = new Intent(HealthOffersActivity.this, DoctorListActivity.class);
                startActivity(intentDoctorVisit);
                break;
            case R.id.savedArticlesLayout:
                break;*/
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


