package com.rescribe.ui.activities.book_appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.rescribe.R;
import com.rescribe.helpers.book_appointment.ServicesCardViewImpl;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.fragments.book_appointment.BookAppointFilteredDoctorListFragment;
import com.rescribe.ui.fragments.book_appointment.DrawerForFilterBookAppointmentFragment;
import com.rescribe.util.RescribeConstants;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by jeetal on 6/11/17.
 */

public class ServicesFilteredDoctorListActivity extends AppCompatActivity implements DrawerForFilterBookAppointmentFragment.OnDrawerInteractionListener {

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
    HashMap<String, String> userSelectedLocationInfo;
    private BookAppointFilteredDoctorListFragment mBookAppointFilteredDoctorListFragment;
    private int PLACE_PICKER_REQUEST = 1;
    private boolean isLocationChangeViewClicked = false;
    private DrawerForFilterBookAppointmentFragment mDrawerLoadedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appoint_doc_base_list);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        showlocation.setVisibility(View.GONE);
        locationTextView.setVisibility(View.GONE);
        userSelectedLocationInfo = RescribeApplication.getUserSelectedLocationInfo();

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            title.setText(extras.getString(RescribeConstants.TITLE));
        mBookAppointFilteredDoctorListFragment = BookAppointFilteredDoctorListFragment.newInstance(extras);
        getSupportFragmentManager().beginTransaction().replace(R.id.viewContainer, mBookAppointFilteredDoctorListFragment).commit();
    }

    public void disableDrawer(boolean isDisabled) {
        if (isDisabled) {
            getActivityDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
            getActivityDrawerLayout().closeDrawer(GravityCompat.END);
        } else {
            if (mDrawerLoadedFragment == null) {
                mDrawerLoadedFragment = DrawerForFilterBookAppointmentFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_view, mDrawerLoadedFragment).commit();
            }
            getActivityDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.END);
        }
    }

    @OnClick({R.id.bookAppointmentBackButton, R.id.locationTextView, R.id.showlocation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bookAppointmentBackButton:
                onBackPressed();
                break;
            case R.id.locationTextView:
                isLocationChangeViewClicked = true;
                Intent start = new Intent(this, BookAppointFindLocationActivity.class);
                startActivityForResult(start, PLACE_PICKER_REQUEST);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else {
            if (mBookAppointFilteredDoctorListFragment.getReceivedPreviousDoctorList() != null) {
                new ServicesCardViewImpl(this, this).setReceivedDoctorDataList(mBookAppointFilteredDoctorListFragment.getReceivedPreviousDoctorList());
            }
            super.onBackPressed();
        }
    }

    @Override
    public void onApply(Bundle b, boolean drawerRequired) {
        mDrawerLayout.closeDrawers();
        mBookAppointFilteredDoctorListFragment.onApplyClicked(b);
    }

    @Override
    public void onReset(boolean drawerRequired) {
        mBookAppointFilteredDoctorListFragment.onResetClicked();
    }

    public DrawerLayout getActivityDrawerLayout() {
        return mDrawerLayout;
    }

    public boolean isLocationChangeViewClicked() {
        return isLocationChangeViewClicked;
    }

    public void setLocationChangeViewClicked(boolean locationChangeViewClicked) {
        isLocationChangeViewClicked = locationChangeViewClicked;
    }
}