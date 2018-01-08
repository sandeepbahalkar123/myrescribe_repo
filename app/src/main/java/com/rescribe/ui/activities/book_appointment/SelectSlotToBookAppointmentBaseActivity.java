package com.rescribe.ui.activities.book_appointment;

import android.content.Context;
import android.content.Intent;
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

import static com.rescribe.services.fcm.FCMService.TOKEN_DATA;
import static com.rescribe.services.fcm.FCMService.TOKEN_DATA_ACTION;

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
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_slot_book_app_layout);
        ButterKnife.bind(this);

        initialize();
    }

    private void initialize() {
        mContext = SelectSlotToBookAppointmentBaseActivity.this;
        showlocation.setVisibility(View.GONE);
        locationTextView.setVisibility(View.GONE);
      /*  String coachMarkStatus = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.COACHMARK_GET_TOKEN, mContext);
        if (coachMarkStatus.equals(RescribeConstants.YES)) {
            coachmark.setVisibility(View.GONE);
        }*/
        userSelectedLocationInfo = RescribeApplication.getUserSelectedLocationInfo();

        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        if (extras == null) {
            extras = new Bundle();
        }
        title.setText(extras.getString(getString(R.string.toolbarTitle)));

        if (intent.getAction() != null) {
            if (intent.getAction().equals(TOKEN_DATA_ACTION)) {
                extras.putParcelable(TOKEN_DATA, intent.getParcelableExtra(TOKEN_DATA));
            }
        }

        //extras.putString(getString(R.string.toolbarTitle), getIntent().getStringExtra(getString(R.string.toolbarTitle)));
        // extras.putString(getString(R.string.clicked_item_data_type_value), getIntent().getStringExtra(getString(R.string.clicked_item_data_type_value)));
        mSelectSlotTimeToBookAppointmentFragment = SelectSlotTimeToBookAppointmentFragment.newInstance(extras);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewContainer, mSelectSlotTimeToBookAppointmentFragment);
        fragmentTransaction.commit();


    }

    @OnClick({R.id.bookAppointmentBackButton, R.id.locationTextView, R.id.showlocation/*R.id.coachmark*/})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bookAppointmentBackButton:
                onBackPressed();
                break;
            case R.id.locationTextView:
                break;
            case R.id.showlocation:
                break;
           /* case R.id.coachmark:
                coachmark.setVisibility(View.GONE);
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.COACHMARK_GET_TOKEN, RescribeConstants.YES, mContext);
                break;*/
        }
    }

}

