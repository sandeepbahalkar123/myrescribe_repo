package com.rescribe.ui.activities.dashboard;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.model.dashboard_api.DashboardMenuList;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.fragments.health_offers.HealthOffersFragment;
import com.rescribe.util.RescribeConstants;

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
    @BindView(R.id.title)
    CustomTextView title;
    private DashboardMenuList mReceivedDashboardMenuListData;
    private Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_offers_base_layout);
        ButterKnife.bind(this);
        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/roboto_bold.ttf");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mReceivedDashboardMenuListData = extras.getParcelable(RescribeConstants.ITEM_DATA);
            String value = extras.getString(RescribeConstants.ITEM_DATA_VALUE);

            if (mReceivedDashboardMenuListData != null)
                title.setText(mReceivedDashboardMenuListData.getName());
            else if (value != null)
               title.setText(value);
        }

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
        ViewGroup vg = (ViewGroup) mTabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(mTypeface, Typeface.NORMAL);
                }
            }
        }
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


