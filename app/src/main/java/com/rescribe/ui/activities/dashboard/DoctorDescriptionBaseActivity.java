package com.rescribe.ui.activities.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.rescribe.R;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.fragments.book_appointment.BookAppointDoctorDescriptionFragment;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rescribe.util.RescribeConstants.DOCTOR_DATA;
import static com.rescribe.util.RescribeConstants.DOCTOR_DATA_REQUEST_CODE;

/**
 * Created by jeetal on 8/11/17.
 */

public class DoctorDescriptionBaseActivity extends AppCompatActivity {

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
    private BookAppointDoctorDescriptionFragment mBookAppointDoctorDescriptionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_slot_book_app_layout);
        ButterKnife.bind(this);
        initialize();

    }

    private void initialize() {

        locationTextView.setVisibility(View.GONE);
        userSelectedLocationInfo = RescribeApplication.getUserSelectedLocationInfo();
        showlocation.setText(userSelectedLocationInfo.get(getString(R.string.location)));
        //--------
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            title.setText(extras.getString(getString(R.string.toolbarTitle)));
        }
        mBookAppointDoctorDescriptionFragment = BookAppointDoctorDescriptionFragment.newInstance(extras);

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewContainer, mBookAppointDoctorDescriptionFragment);
        fragmentTransaction.commit();

    }

    @OnClick({R.id.bookAppointmentBackButton, R.id.locationTextView, R.id.showlocation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bookAppointmentBackButton:
                onBackPressed();
                break;
        }
    }

}