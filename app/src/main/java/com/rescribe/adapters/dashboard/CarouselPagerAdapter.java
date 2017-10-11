/*
package com.rescribe.adapters.dashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.rescribe.R;
import com.rescribe.model.dashboard.DoctorData;
import com.rescribe.ui.activities.HomePageActivity;
import com.rescribe.ui.customesViews.CarouselLinearLayout;
import com.rescribe.ui.fragments.ItemFragment;

import java.util.ArrayList;

public class CarouselPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.7f;
    private HomePageActivity context;
    ArrayList<DoctorData> doctorLists;
    private FragmentManager fragmentManager;

    public CarouselPagerAdapter(HomePageActivity context, FragmentManager fm,ArrayList<DoctorData> doctorLists) {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;
        this.doctorLists = doctorLists;
    }

    @Override
    public Fragment getItem(int position) {
        // make the first pager bigger than others


        return ItemFragment.newInstance(context, position,doctorLists);
    }

    @Override
    public int getCount() {
        int count = 0;
       */
/* try {
            count = HomePageActivity.count * HomePageActivity.LOOPS;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }*//*

        return count;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        */
/*try {
            if (positionOffset >= 0f && positionOffset <= 1f) {
                CarouselLinearLayout cur = getRootView(position);
                CarouselLinearLayout next = getRootView(position + 1);

              //  cur.setScaleBoth(BIG_SCALE - DIFF_SCALE * positionOffset);
                next.setScaleBoth(SMALL_SCALE + DIFF_SCALE * positionOffset);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*//*

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @SuppressWarnings("ConstantConditions")
    private CarouselLinearLayout getRootView(int position) {
        return (CarouselLinearLayout) fragmentManager.findFragmentByTag(this.getFragmentTag(position))
                .getView().findViewById(R.id.root_container);
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + context.mViewPager.getId() + ":" + position;
    }
}*/
