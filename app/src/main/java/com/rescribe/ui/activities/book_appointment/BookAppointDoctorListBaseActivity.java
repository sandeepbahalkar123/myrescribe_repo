package com.rescribe.ui.activities.book_appointment;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.fragments.book_appointment.BookAppointmentDoctorListFragment;
import com.rescribe.ui.fragments.book_appointment.RecentVisitDoctorFragment;
import com.rescribe.util.RescribeConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 15/9/17.
 */
public class BookAppointDoctorListBaseActivity extends AppCompatActivity implements HelperResponse {

    @BindView(R.id.bookAppointmentBackButton)
    ImageView bookAppointmentBackButton;
    @BindView(R.id.title)
    CustomTextView title;
    @BindView(R.id.locationTextView)
    CustomTextView locationTextView;

    Intent intent;
    private RecentVisitDoctorFragment mChangeColorFragment;
    private DoctorDataHelper mDoctorDataHelper;
    private Fragment currentlyLoadedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        setContentView(R.layout.activity_book_appoint_doc_base_list);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        if (getIntent() != null) {
            locationTextView.setText(intent.getStringExtra(getString(R.string.title)));
        }

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        RecentVisitDoctorFragment recentVisitDoctorFragment = RecentVisitDoctorFragment.newInstance(new Bundle());
        fragmentTransaction.replace(R.id.viewContainer, recentVisitDoctorFragment);
        fragmentTransaction.commit();
        this.currentlyLoadedFragment = recentVisitDoctorFragment;

        fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_view, BookAppointmentDoctorListFragment.createNewFragment());
        fragmentTransaction.commit();
        mDoctorDataHelper = new DoctorDataHelper(this, this);
        mDoctorDataHelper.doGetDoctorData();
    }

    @Override
    public void onSuccess(final String mOldDataTag, final CustomResponse customResponse) {
        if (currentlyLoadedFragment instanceof RecentVisitDoctorFragment) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    RecentVisitDoctorFragment tempFrag = (RecentVisitDoctorFragment) currentlyLoadedFragment;

                    tempFrag.onSuccess(mOldDataTag, customResponse);

                }
            }, RescribeConstants.TIME_STAMPS.ONE_SECONDS);

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
}
