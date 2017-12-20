package com.rescribe.ui.activities.book_appointment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.rescribe.R;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.fragments.book_appointment.BookAppointFilteredDoctorListFragment;
import com.rescribe.ui.fragments.book_appointment.BookAppointListOnLocationSelectionFragment;
import com.rescribe.ui.fragments.book_appointment.DrawerForFilterDoctorBookAppointment;
import com.rescribe.ui.fragments.doctor.DoctorListFragmentContainer;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by jeetal on 6/11/17.
 */

public class BookAppointListOnLocationSelection extends AppCompatActivity implements DrawerForFilterDoctorBookAppointment.OnDrawerInteractionListener {

    @BindView(R.id.nav_view)
    FrameLayout mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.bookAppointmentBackButton)
    ImageView bookAppointmentBackButton;
    @BindView(R.id.title)
    CustomTextView title;
    @BindView(R.id.locationTextView)
    CustomTextView locationTextView;
    @BindView(R.id.showlocation)
    CustomTextView showlocation;
    @BindView(R.id.viewContainer)
    FrameLayout viewContainer;
    private FragmentManager mFragmentManager;
    private DrawerForFilterDoctorBookAppointment mDrawerLoadedFragment;
    private BookAppointListOnLocationSelectionFragment mLoadedFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_drawer_layout_container);
        ButterKnife.bind(this);
        mFragmentManager = getSupportFragmentManager();

        //------
        Bundle extras = getIntent().getExtras();
        mLoadedFragment = BookAppointListOnLocationSelectionFragment.newInstance(extras);
        getSupportFragmentManager().beginTransaction().replace(R.id.viewContainer, mLoadedFragment).commit();
        //-----------
        //------
        mDrawerLoadedFragment = DrawerForFilterDoctorBookAppointment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_view, mDrawerLoadedFragment).commit();

    }

    @Override
    public void onApply(Bundle b, boolean drawerRequired) {
        mDrawerLayout.closeDrawers();
        mLoadedFragment.onApplyClicked(b);
    }

    @Override
    public void onReset(boolean drawerRequired) {

    }

    public DrawerLayout getActivityDrawerLayout() {
        return mDrawerLayout;
    }
}