package com.rescribe.ui.activities.book_appointment.confirmation_type_activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.book_appointment.request_appointment_confirmation.ResponseAppointmentConfirmationModel;
import com.rescribe.ui.activities.HomePageActivity;
import com.rescribe.ui.activities.MapsActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.rescribe.util.RescribeConstants.CANCEL_TYPE;
import static com.rescribe.util.RescribeConstants.TOKEN_NO;
import static com.rescribe.util.RescribeConstants.WAITING_COUNT;
import static com.rescribe.util.RescribeConstants.WAITING_TIME;


/**
 * Created by jeetal on 28/12/17.
 */

@RuntimePermissions
public class ConfirmTokenInfoActivity extends AppCompatActivity implements HelperResponse {
    @BindView(R.id.bookAppointmentBackButton)
    ImageView bookAppointmentBackButton;
    @BindView(R.id.title)
    CustomTextView title;

    @BindView(R.id.tokenNumberTextView)
    CustomTextView tokenNumberTextView;
    @BindView(R.id.patientAheadTextView)
    CustomTextView patientAheadTextView;

    @BindView(R.id.timeInMinutesTextView)
    CustomTextView timeInMinutesTextView;

    @BindView(R.id.locationTextView)
    CustomTextView locationTextView;
    @BindView(R.id.showlocation)
    CustomTextView showlocation;
    @BindView(R.id.showTimedate)
    CustomTextView showTimedate;
    @BindView(R.id.doctorImage)
    ImageView doctorImage;
    @BindView(R.id.clinicName)
    CustomTextView clinicName;
    @BindView(R.id.doctorName)
    CustomTextView doctorName;
    @BindView(R.id.aboutDoctor)
    CustomTextView aboutDoctor;
    @BindView(R.id.phoneImage)
    ImageView phoneImage;
    @BindView(R.id.mobileNumber)
    CustomTextView mobileNumber;
    @BindView(R.id.addressIcon)
    ImageView addressIcon;
    @BindView(R.id.partitionLine)
    ImageView partitionLine;
    @BindView(R.id.clinicAddress)
    CustomTextView clinicAddress;
    @BindView(R.id.locationText)
    CustomTextView locationText;
    @BindView(R.id.locationImageView)
    ImageView locationImageView;
    @BindView(R.id.cancelButton)
    CustomTextView cancelButton;
    @BindView(R.id.rescheduleButton)
    CustomTextView rescheduleButton;
    Context mContext;
    @BindView(R.id.doctorDetailLayout)
    RelativeLayout doctorDetailLayout;
    @BindView(R.id.phoneNumberLayout)
    RelativeLayout phoneNumberLayout;
    @BindView(R.id.addressLayout)
    RelativeLayout addressLayout;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.buttonLayout)
    LinearLayout buttonLayout;
    private DoctorList mDoctorObject;
    private DoctorDataHelper mDoctorDataHelper;
    private int mTokenNo;
    private int mLocationId;
    private int mWaitingTime;
    private int mWaitingCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.token_confirmation_layout);
        ButterKnife.bind(this);
        initialize();
        title.setText(getString(R.string.token_confirmation));
    }

    private void initialize() {
        mContext = ConfirmTokenInfoActivity.this;
        mDoctorDataHelper = new DoctorDataHelper(this, this);
        SpannableString location = new SpannableString(getString(R.string.location) + ":");
        location.setSpan(new UnderlineSpan(), 0, location.length(), 0);
        locationText.setText(location);
        setUpDataInViews();

    }

    private void setUpDataInViews() {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mDoctorObject = extras.getParcelable(RescribeConstants.ITEM_DATA);
            if (extras.getString(TOKEN_NO) != null) {
                if (!extras.getString(TOKEN_NO).isEmpty())
                    mTokenNo = Integer.parseInt(extras.getString(RescribeConstants.TOKEN_NO));
            }

            if (extras.getString(WAITING_TIME) != null) {
                if (!extras.getString(WAITING_TIME).isEmpty())
                    mWaitingTime = Integer.parseInt(extras.getString(RescribeConstants.WAITING_TIME));
            }

            if (extras.getString(WAITING_COUNT) != null) {
                if (!extras.getString(WAITING_COUNT).isEmpty())
                    mWaitingCount = Integer.parseInt(extras.getString(RescribeConstants.WAITING_COUNT));
            }

            mLocationId = Integer.parseInt(extras.getString(RescribeConstants.LOCATION_ID));
            tokenNumberTextView.setText(String.valueOf(mTokenNo));
            patientAheadTextView.setText(String.valueOf(mWaitingCount));
            timeInMinutesTextView.setText(String.valueOf(mWaitingTime));
        }
        if (mDoctorObject != null) {
            rescheduleButton.setVisibility(View.GONE);
            partitionLine.setVisibility(View.GONE);

            String drName = mDoctorObject.getDocName().contains("Dr.") ? mDoctorObject.getDocName() : "Dr. " + mDoctorObject.getDocName();

            doctorName.setText(drName);
            aboutDoctor.setText(mDoctorObject.getDegree());
            clinicAddress.setText(mDoctorObject.getAddressOfDoctorString());
            if (!mDoctorObject.getAddressOfDoctorString().isEmpty()) {

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.staticmap);
                requestOptions.error(R.drawable.staticmap);
                requestOptions.centerCrop();

                Glide.with(mContext)
                        .load("https://maps.googleapis.com/maps/api/staticmap?center=" + mDoctorObject.getAddressOfDoctorString() + "&markers=color:red%7Clabel:C%7C" + mDoctorObject.getAddressOfDoctorString() + "&zoom=12&size=640x300&key="+getString(R.string.google_maps_key))
                        .into(locationImageView);
            }
            if (!mDoctorObject.getNameOfClinicString().isEmpty()) {
                clinicName.setText(mDoctorObject.getNameOfClinicString());
            }
            if (mDoctorObject.getDocPhone().equals("")) {
                phoneNumberLayout.setVisibility(View.GONE);
            } else {
                mobileNumber.setText(mDoctorObject.getDocPhone());
                phoneNumberLayout.setVisibility(View.VISIBLE);
            }

            Calendar now = Calendar.getInstance();
            now.add(Calendar.MINUTE, mWaitingTime);

            SimpleDateFormat formatterEEE = new SimpleDateFormat("EEE", Locale.US);
            String dateValueToShow = formatterEEE.format(now.getTime());

            SimpleDateFormat formatterDD = new SimpleDateFormat("dd", Locale.US);
            String ordinal = CommonMethods.ordinal(formatterDD.format(now.getTime()));

            SimpleDateFormat formatterMM_YYYY = new SimpleDateFormat("MMM yyyy", Locale.US);
            String monthYear = formatterMM_YYYY.format(now.getTime());

            SimpleDateFormat formatterHH_MM_A = new SimpleDateFormat(RescribeConstants.DATE_PATTERN.hh_mm_a, Locale.US);
            String timeToShow = formatterHH_MM_A.format(now.getTime()).toLowerCase();

            String dateToShow = dateValueToShow + ", " + ordinal + " " + monthYear;
            dateToShow = dateToShow + " @" + timeToShow;
            showTimedate.setText(dateToShow);
        }
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    void doCallSupport() {
        callSupport(mobileNumber.getText().toString());
    }

    private void callSupport(String phoneNo) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNo));
        startActivity(callIntent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ConfirmTokenInfoActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

    }

    @OnClick({R.id.cancelButton, R.id.bookAppointmentBackButton, R.id.locationImageView, R.id.phoneNumberLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancelButton:

                showTokenStatusMessageBox(mContext.getString(R.string.cancel_token_msg), mDoctorObject.getTokenNumber(), CANCEL_TYPE);

                break;

            case R.id.bookAppointmentBackButton:
                onBackPressed();
                break;
            case R.id.locationImageView:
                if (mDoctorObject != null) {
                    Intent intent = new Intent(mContext, MapsActivity.class);
                    intent.putExtra(mContext.getString(R.string.address), mDoctorObject.getAddressOfDoctorString());
                    intent.putExtra(RescribeConstants.RATING, mDoctorObject.getRating());
                    intent.putExtra(RescribeConstants.DOCTOR_NAME, mDoctorObject.getDocName());
                    mContext.startActivity(intent);
                }
                break;
            case R.id.phoneNumberLayout:
                ConfirmTokenInfoActivityPermissionsDispatcher.doCallSupportWithCheck(this);
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if (mDoctorObject.isTypedashboard()) {
            super.onBackPressed();
        } else {
            Intent intent = new Intent(this, HomePageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_CANCEL_GET_TOKEN)) {
            ResponseAppointmentConfirmationModel mResponseAppointmentConfirmationModel = (ResponseAppointmentConfirmationModel) customResponse;
            if (mResponseAppointmentConfirmationModel.getCommon() != null)
                if (mResponseAppointmentConfirmationModel.getCommon().isSuccess()) {
//                    appDBHelper.updateTokenNumber(mDoctorObject.getDocId(), mLocationId, "Mixed");
                    Intent intent = new Intent(ConfirmTokenInfoActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }
        }

    }


    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        Toast.makeText(mContext, serverErrorMessage, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        Toast.makeText(mContext, serverErrorMessage, Toast.LENGTH_SHORT).show();

    }

    public void showTokenStatusMessageBox(String message, final String tokenId, final String type) {

        final Dialog dialog = new Dialog(mContext);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.token_dialog_popup);
        dialog.setCancelable(true);

        TextView messageView = (TextView) dialog.findViewById(R.id.messageView);
        ImageView icon_get_token = (ImageView) dialog.findViewById(R.id.icon);
        TextView textheading = (TextView) dialog.findViewById(R.id.textheading);
        textheading.setText(mContext.getString(R.string.token));
        messageView.setGravity(Gravity.CENTER);
        icon_get_token.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.token_icon));
        messageView.setText(message);

        //-----------------
        TextView yesButton = (TextView) dialog.findViewById(R.id.yesButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDoctorDataHelper.doCancelTokenNumber(mDoctorObject.getDocId(), mLocationId, mTokenNo);
                dialog.cancel();

            }
        });
        //------------
        TextView noButton = (TextView) dialog.findViewById(R.id.noButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

}
