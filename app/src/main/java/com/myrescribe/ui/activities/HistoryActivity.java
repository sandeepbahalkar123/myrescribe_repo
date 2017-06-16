package com.myrescribe.ui.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.myrescribe.R;
import com.myrescribe.ui.fragments.EightFragment;
import com.myrescribe.ui.fragments.FiveFragment;
import com.myrescribe.ui.fragments.FourFragment;
import com.myrescribe.ui.fragments.NineFragment;
import com.myrescribe.ui.fragments.OneFragment;
import com.myrescribe.ui.fragments.SevenFragment;
import com.myrescribe.ui.fragments.SixFragment;
import com.myrescribe.ui.fragments.TenFragment;
import com.myrescribe.ui.fragments.ThreeFragment;
import com.myrescribe.ui.fragments.TwoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeetal on 14/6/17.
 */

public class HistoryActivity extends AppCompatActivity {

    private LinearLayout toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String[] strings= {"One","Two","Three","Four"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        toolbar = (LinearLayout) findViewById(R.id.toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < strings.length; i++) {
            Fragment fragment = OneFragment.createNewFragment(strings[i]); // pass data here
            adapter.addFrag(fragment,strings[i]); // pass title here
        }
        viewPager.setAdapter(adapter);
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

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }



        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}