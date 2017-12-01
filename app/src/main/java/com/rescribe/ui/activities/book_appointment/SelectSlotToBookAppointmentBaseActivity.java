package com.rescribe.ui.activities.book_appointment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.rescribe.R;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.fragments.book_appointment.SelectSlotTimeToBookAppointmentFragment;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 1/11/17.
 */

public class SelectSlotToBookAppointmentBaseActivity extends AppCompatActivity {

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
    private SelectSlotTimeToBookAppointmentFragment mSelectSlotTimeToBookAppointmentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_slot_book_app_layout);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        showlocation.setVisibility(View.GONE);
        locationTextView.setVisibility(View.GONE);

        userSelectedLocationInfo = RescribeApplication.getUserSelectedLocationInfo();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            extras = new Bundle();
        }
        title.setText(""+extras.getString(getString(R.string.toolbarTitle)));

        //extras.putString(getString(R.string.toolbarTitle), getIntent().getStringExtra(getString(R.string.toolbarTitle)));
       // extras.putString(getString(R.string.clicked_item_data_type_value), getIntent().getStringExtra(getString(R.string.clicked_item_data_type_value)));
        mSelectSlotTimeToBookAppointmentFragment = SelectSlotTimeToBookAppointmentFragment.newInstance(extras);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewContainer, mSelectSlotTimeToBookAppointmentFragment);
        fragmentTransaction.commit();

    }

    @OnClick({R.id.bookAppointmentBackButton, R.id.locationTextView, R.id.showlocation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bookAppointmentBackButton:
                onBackPressed();
                break;
            case R.id.locationTextView:
                break;
            case R.id.showlocation:
                break;
        }
    }

}

