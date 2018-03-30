package com.rescribe.ui.activities.book_appointment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.helpers.notification.AppointmentHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.follow_up.FollowUpRequest;
import com.rescribe.model.token.FCMData;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.activities.HomePageActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.fragments.book_appointment.SelectSlotTimeToBookAppointmentFragment;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rescribe.services.fcm.FCMService.FCM_DATA;
import static com.rescribe.services.fcm.FCMService.FOLLOW_UP_DATA_ACTION;
import static com.rescribe.services.fcm.FCMService.TOKEN_DATA_ACTION;

/**
 * Created by jeetal on 1/11/17.
 */

public class SelectSlotToBookAppointmentBaseActivity extends AppCompatActivity implements HelperResponse {

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

    private Dialog followUpDialog;
    private FCMData fcmFollowUpData;

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

        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        if (extras == null) {
            extras = new Bundle();
        }
        title.setText(extras.getString(getString(R.string.toolbarTitle)));

        if (intent.getAction() != null) {
            if (intent.getAction().equals(TOKEN_DATA_ACTION) || intent.getAction().equals(FOLLOW_UP_DATA_ACTION))
                extras.putParcelable(FCM_DATA, intent.getParcelableExtra(FCM_DATA));

            if (intent.getAction().equals(FOLLOW_UP_DATA_ACTION))
                fcmFollowUpData = intent.getParcelableExtra(FCM_DATA);
        }

        SelectSlotTimeToBookAppointmentFragment mSelectSlotTimeToBookAppointmentFragment = SelectSlotTimeToBookAppointmentFragment.newInstance(extras);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewContainer, mSelectSlotTimeToBookAppointmentFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        if (fcmFollowUpData != null)
            showFollowUpDialog(fcmFollowUpData.getNotificationId());
        else
            super.onBackPressed();
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

    public void showFollowUpDialog(int notificationId) {

        followUpDialog = new Dialog(this);

        followUpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        followUpDialog.setContentView(R.layout.follow_up_dialog_popup);
        followUpDialog.setCancelable(true);

        final RelativeLayout alreadyBooked = (RelativeLayout) followUpDialog.findViewById(R.id.already_booked);
        final RelativeLayout followUpComplete = (RelativeLayout) followUpDialog.findViewById(R.id.follow_up_complete);
        final RelativeLayout notInterested = (RelativeLayout) followUpDialog.findViewById(R.id.not_interested);

        final TextView alreadyBookedText = (TextView) followUpDialog.findViewById(R.id.already_booked_text);
        final TextView followUpCompleteText = (TextView) followUpDialog.findViewById(R.id.follow_up_complete_text);
        final TextView notInterestedText = (TextView) followUpDialog.findViewById(R.id.not_interested_text);

        final AppointmentHelper appointmentHelper = new AppointmentHelper(this);
        final FollowUpRequest followUpRequest = new FollowUpRequest();
        followUpRequest.setNotificationId(notificationId);

        alreadyBooked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alreadyBookedText.setTextColor(getResources().getColor(R.color.white));
                alreadyBooked.setBackgroundColor(getResources().getColor(R.color.tagColor));

                followUpCompleteText.setTextColor(getResources().getColor(R.color.follow_up_button));
                followUpComplete.setBackgroundColor(getResources().getColor(R.color.white));

                notInterestedText.setTextColor(getResources().getColor(R.color.follow_up_button));
                notInterested.setBackground(getResources().getDrawable(R.drawable.not_intrested_back_white));

                followUpRequest.setResponse(alreadyBookedText.getText().toString());
                appointmentHelper.followUpSkip(followUpRequest);
            }
        });

        followUpComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alreadyBookedText.setTextColor(getResources().getColor(R.color.follow_up_button));
                alreadyBooked.setBackgroundColor(getResources().getColor(R.color.white));

                followUpCompleteText.setTextColor(getResources().getColor(R.color.white));
                followUpComplete.setBackgroundColor(getResources().getColor(R.color.tagColor));

                notInterestedText.setTextColor(getResources().getColor(R.color.follow_up_button));
                notInterested.setBackground(getResources().getDrawable(R.drawable.not_intrested_back_white));

                followUpRequest.setResponse(followUpCompleteText.getText().toString());
                appointmentHelper.followUpSkip(followUpRequest);
            }
        });

        notInterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alreadyBookedText.setTextColor(getResources().getColor(R.color.follow_up_button));
                alreadyBooked.setBackgroundColor(getResources().getColor(R.color.white));

                followUpCompleteText.setTextColor(getResources().getColor(R.color.follow_up_button));
                followUpComplete.setBackgroundColor(getResources().getColor(R.color.white));

                notInterestedText.setTextColor(getResources().getColor(R.color.white));
                notInterested.setBackground(getResources().getDrawable(R.drawable.not_intrested_back_blue));

                followUpRequest.setResponse(notInterestedText.getText().toString());
                appointmentHelper.followUpSkip(followUpRequest);
            }
        });

        followUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(followUpDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        followUpDialog.getWindow().setAttributes(lp);
        followUpDialog.setCanceledOnTouchOutside(false);
        followUpDialog.show();
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (RescribeConstants.FOLLOW_UP.equals(mOldDataTag)) {
            CommonBaseModelContainer commonbject = (CommonBaseModelContainer) customResponse;
            CommonMethods.showToast(this, commonbject.getCommonRespose().getStatusMessage());
            followUpDialog.dismiss();
            Intent intent = new Intent(this, HomePageActivity.class);
            startActivity(intent);
            finishAffinity();
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
}
