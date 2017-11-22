package com.rescribe.ui.activities.book_appointment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.rescribe.R;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.fragments.book_appointment.BookAppointFilteredDoctorListFragment;
import com.rescribe.ui.fragments.book_appointment.DrawerForFilterDoctorBookAppointment;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rescribe.util.RescribeConstants.DOCTOR_DATA;
import static com.rescribe.util.RescribeConstants.DOCTOR_DATA_REQUEST_CODE;

/**
 * Created by jeetal on 6/11/17.
 */

public class ServicesFilteredDoctorListActivity extends AppCompatActivity implements DrawerForFilterDoctorBookAppointment.OnDrawerInteractionListener {

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

    private Fragment mDrawerLoadedFragment;
    private int PLACE_PICKER_REQUEST = 1;
    private boolean isLocationChangeViewClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appoint_doc_base_list);
        ButterKnife.bind(this);
        initialize();

    }

    private void initialize() {
        showlocation.setVisibility(View.GONE);
        locationTextView.setVisibility(View.VISIBLE);
        userSelectedLocationInfo = RescribeApplication.getUserSelectedLocationInfo();
        //  showlocation.setText(userSelectedLocationInfo.get(getString(R.string.location)));
        //--------
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            title.setText(extras.getString(getString(R.string.toolbarTitle)));
        }
        mBookAppointFilteredDoctorListFragment = BookAppointFilteredDoctorListFragment.newInstance(extras);
        getSupportFragmentManager().beginTransaction().replace(R.id.viewContainer, mBookAppointFilteredDoctorListFragment).commit();

        //------------
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //Called when a drawer's position changes.
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //Called when a drawer has settled in a completely open state.
                //The drawer is interactive at this point.
                // If you have 2 drawers (left and right) you can distinguish
                // them by using id of the drawerView. int id = drawerView.getId();
                // id will be your layout's id: for example R.id.left_drawer
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // Called when a drawer has settled in a completely closed state.
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // Called when the drawer motion state changes. The new state will be one of STATE_IDLE, STATE_DRAGGING or STATE_SETTLING.
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerLoadedFragment = DrawerForFilterDoctorBookAppointment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_view, mDrawerLoadedFragment).commit();
            }
        }, 100);

    }

    @OnClick({R.id.bookAppointmentBackButton, R.id.locationTextView, R.id.showlocation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bookAppointmentBackButton:
                onBackPressed();
                break;
            case R.id.locationTextView:
                isLocationChangeViewClicked = true;
                //    BookAppointDoctorListBaseActivityPermissionsDispatcher.callPickPlaceWithCheck(this);
                Intent start = new Intent(this, BookAppointFindLocation.class);
                startActivityForResult(start, PLACE_PICKER_REQUEST);
                break;
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