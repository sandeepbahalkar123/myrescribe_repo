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

import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.fragments.book_appointment.DrawerForFilterDoctorBookAppointment;
import com.rescribe.ui.fragments.book_appointment.RecentVisitDoctorFragment;
import com.rescribe.util.RescribeConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 15/9/17.
 */
public class BookAppointDoctorListBaseActivity extends AppCompatActivity implements HelperResponse, DrawerForFilterDoctorBookAppointment.OnDrawerInteractionListener {

    @BindView(R.id.bookAppointmentBackButton)
    ImageView bookAppointmentBackButton;

    static CustomTextView title;
    static CustomTextView locationTextView;
    static CustomTextView showlocation;
    @BindView(R.id.nav_view)
    FrameLayout mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    static Intent intent;
    private RecentVisitDoctorFragment mChangeColorFragment;
    private DoctorDataHelper mDoctorDataHelper;
    private Fragment currentlyLoadedFragment; //TODO, fragmentById is not working hence hold this object.
    private BookAppointmentBaseModel mReceivedBookAppointmentBaseModel;
    private static String location;
    private FragmentManager mSupportFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        setContentView(R.layout.activity_book_appoint_doc_base_list);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        title = (CustomTextView) findViewById(R.id.title);
        locationTextView = (CustomTextView) findViewById(R.id.locationTextView);
        locationTextView.setVisibility(View.GONE);
        showlocation = (CustomTextView) findViewById(R.id.showlocation);
        if (getIntent() != null) {
            location = intent.getStringExtra(getString(R.string.title));
        }
        mDoctorDataHelper = new DoctorDataHelper(this, this);
        mDoctorDataHelper.doGetDoctorData();

        //  searchBarLinearLayout.setVisibility(View.VISIBLE);
        /*Bundle b = new Bundle();
        b.putString(getString(R.string.latitude), intent.getStringExtra(getString(R.string.latitude)));
        b.putString(getString(R.string.longitude), intent.getStringExtra(getString(R.string.longitude)));
        loadFragment(RecentVisitDoctorFragment.newInstance(b),false);

*/
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


        //  loadFragment(RecentVisitDoctorFragment.newInstance(new Bundle()), false);
        //------
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_view, DrawerForFilterDoctorBookAppointment.newInstance());
        fragmentTransaction.commit();
        //------

    }

    @Override


    public void onSuccess(final String mOldDataTag, final CustomResponse customResponse) {
        mReceivedBookAppointmentBaseModel = (BookAppointmentBaseModel) customResponse;
        if (mReceivedBookAppointmentBaseModel.getDoctorServicesModel() != null) {
            Bundle b = new Bundle();
            b.putString(getString(R.string.latitude), intent.getStringExtra(getString(R.string.latitude)));
            b.putString(getString(R.string.longitude), intent.getStringExtra(getString(R.string.longitude)));
            loadFragment(RecentVisitDoctorFragment.newInstance(b), false);
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onApply(boolean drawerRequired) {
        mDrawerLayout.closeDrawers();
        doOperationOnDrawer(drawerRequired);
    }

    @Override
    public void onReset(boolean drawerRequired) {
        mDrawerLayout.closeDrawers();
        doOperationOnDrawer(drawerRequired);
    }

    public BookAppointmentBaseModel getReceivedBookAppointmentBaseModel() {
        return mReceivedBookAppointmentBaseModel;
    }

    @OnClick({R.id.bookAppointmentBackButton, R.id.title, R.id.locationTextView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bookAppointmentBackButton:
                onBackPressed();
                break;
            case R.id.title:
                break;
            case R.id.locationTextView:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        int backStackEntryCount = mSupportFragmentManager.getBackStackEntryCount();
        if (backStackEntryCount == 1) {
            finish();
        }else{
            super.onBackPressed();
        }
    }

    public void loadFragment(Fragment fragmentToLoad, boolean requiredDrawer) {
        if (fragmentToLoad != null) {
            mSupportFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mSupportFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.viewContainer, fragmentToLoad);
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commit();
            this.currentlyLoadedFragment = fragmentToLoad;
            doOperationOnDrawer(requiredDrawer);
        }
    }

    public void doOperationOnDrawer(boolean flag) {
        if (flag) { // for open
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    public DrawerLayout getActivityDrawerLayout() {
        return mDrawerLayout;
    }

    public static void setToolBarTitle(String toolbartitle, boolean isLocationVisible) {
        title.setText(toolbartitle);
        if (isLocationVisible) {
            locationTextView.setVisibility(View.VISIBLE);
            locationTextView.setText(location);
            showlocation.setVisibility(View.GONE);
        } else {
            locationTextView.setVisibility(View.GONE);
            showlocation.setVisibility(View.VISIBLE);
            showlocation.setText(location);

        }
    }
}
