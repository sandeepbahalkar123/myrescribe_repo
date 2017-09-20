package com.rescribe.ui.activities.book_appointment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.rescribe.R;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.ui.fragments.book_appointment.FilterForDoctorBookAppointmentFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by jeetal on 15/9/17.
 */

public class DoctorListToBookAppointment extends AppCompatActivity implements HelperResponse, FilterForDoctorBookAppointmentFragment.OnDrawerInteractionListener {

    // Filter Start
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.nav_view)
    FrameLayout nav_view;

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_drawer_layout_container);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        mFragmentManager = getSupportFragmentManager();
        loadFilterFragment();
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

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

    public void loadFilterFragment() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        FilterForDoctorBookAppointmentFragment selectDoctorsFragment = FilterForDoctorBookAppointmentFragment.newInstance();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.add(R.id.nav_view, selectDoctorsFragment, getResources().getString(R.string.doctors));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onApply() {

    }

    @Override
    public void onReset() {

    }
}
