package com.rescribe.ui.activities.book_appointment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.ui.activities.MapsActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by jeetal on 28/12/17.
 */

public class ConfirmAppointmentActivity extends AppCompatActivity {
    @BindView(R.id.bookAppointmentBackButton)
    ImageView bookAppointmentBackButton;
    @BindView(R.id.title)
    CustomTextView title;
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
    private String mobileNo = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_confirmation_layout);
        ButterKnife.bind(this);
        initialize();
        title.setText(getString(R.string.appointment_confirmation));
    }

    private void initialize() {
        mContext = ConfirmAppointmentActivity.this;
        SpannableString location = new SpannableString(getString(R.string.location) + ":");
        location.setSpan(new UnderlineSpan(), 0, location.length(), 0);
        locationText.setText(location);
        setUpDataInViews();

    }

    private void setUpDataInViews() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mDoctorObject = extras.getParcelable(getString(R.string.clicked_item_data));
        }
        if (mDoctorObject != null) {
            doctorName.setText(mDoctorObject.getDocName());
            clinicName.setText(mDoctorObject.getClinicDataList().get(0).getClinicName());
            aboutDoctor.setText(mDoctorObject.getDegree());
            clinicAddress.setText(mDoctorObject.getClinicDataList().get(0).getClinicAddress());
            if (mobileNo.equals("")) {
                phoneNumberLayout.setVisibility(View.GONE);
            }else{
                phoneNumberLayout.setVisibility(View.VISIBLE);
            }
            if (!mDoctorObject.getAptTime().isEmpty() && !mDoctorObject.getAptDate().isEmpty()) {
                String dateValueToShow = (String) DateFormat.format("EEE", CommonMethods.convertStringToDate(mDoctorObject.getAptDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD));
                String ordinal = CommonMethods.ordinal(Integer.parseInt(CommonMethods.getFormattedDate(mDoctorObject.getAptDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD, "dd")));
                String timeToShow = CommonMethods.formatDateTime(mDoctorObject.getAptTime(), RescribeConstants.DATE_PATTERN.hh_mm_a,
                        RescribeConstants.DATE_PATTERN.HH_mm_ss, RescribeConstants.TIME).toLowerCase();
                String dateToShow = dateValueToShow + ", " + ordinal + " " + CommonMethods.getFormattedDate(mDoctorObject.getAptDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD, "MMM yyyy").toUpperCase() + " @" + timeToShow;
                showTimedate.setText(dateToShow);
            }
            if (!mDoctorObject.getClinicDataList().get(0).getClinicAddress().isEmpty()) {

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.staticmap);
                requestOptions.error(R.drawable.staticmap);
                requestOptions.centerCrop();

                Glide.with(mContext)
                        .load("https://maps.googleapis.com/maps/api/staticmap?center=" + mDoctorObject.getClinicDataList().get(0).getClinicAddress() + "&markers=color:red%7Clabel:C%7C" + mDoctorObject.getClinicDataList().get(0).getClinicAddress() + "&zoom=12&size=640x300")
                        .into(locationImageView);
            }
            // mobileNumber.setText(mDoctorObject.);

        }

    }

    @OnClick({R.id.cancelButton, R.id.rescheduleButton, R.id.bookAppointmentBackButton, R.id.locationImageView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancelButton:

                break;
            case R.id.rescheduleButton:
                break;
            case R.id.bookAppointmentBackButton:
                onBackPressed();
                break;
            case R.id.locationImageView:
                Intent intent = new Intent(mContext, MapsActivity.class);
                intent.putExtra(mContext.getString(R.string.address), mDoctorObject.getClinicDataList().get(0).getClinicAddress());
                intent.putExtra(RescribeConstants.DOCTOR_NAME, mDoctorObject.getDocName());
                mContext.startActivity(intent);
                break;
        }
    }
}
