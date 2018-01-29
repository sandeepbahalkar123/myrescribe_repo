package com.rescribe.ui.activities.book_appointment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
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
import com.rescribe.helpers.book_appointment.ServicesCardViewImpl;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.book_appointment.request_appointment_confirmation.ResponseAppointmentConfirmationModel;
import com.rescribe.ui.activities.HomePageActivity;
import com.rescribe.ui.activities.MapsActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.rescribe.util.RescribeConstants.CANCEL_TYPE;
import static com.rescribe.util.RescribeConstants.MIXED_APPOINTMENT_TYPE;
import static com.rescribe.util.RescribeConstants.RESHEDULE_TYPE;


/**
 * Created by jeetal on 28/12/17.
 */

@RuntimePermissions
public class ConfirmAppointmentActivity extends AppCompatActivity implements HelperResponse {
    public static final int RESCHEDULE_OK = 200;
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
    private String mobileNo = "";
    private DoctorDataHelper mDoctorDataHelper;
    String callType;
    private boolean isCanceled;
    private Intent intent;
    private int mTokenNo;
    private int mLocationId;

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
        mDoctorDataHelper = new DoctorDataHelper(this, this);
        SpannableString location = new SpannableString(getString(R.string.location) + ":");
        location.setSpan(new UnderlineSpan(), 0, location.length(), 0);
        locationText.setText(location);
        setUpDataInViews();

    }

    private void setUpDataInViews() {
        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            mDoctorObject = extras.getParcelable(getString(R.string.clicked_item_data));
            mTokenNo = Integer.parseInt(extras.getString(RescribeConstants.TOKEN_NO));
            mLocationId = Integer.parseInt(extras.getString(RescribeConstants.LOCATION_ID));
            callType = extras.getString(RescribeConstants.CALL_FROM_DASHBOARD);
        }
        if (mDoctorObject != null) {
            if (mDoctorObject.isAppointmentTypeMixed()) {
                rescheduleButton.setVisibility(View.GONE);
                partitionLine.setVisibility(View.GONE);

            } else {
                partitionLine.setVisibility(View.VISIBLE);
                rescheduleButton.setVisibility(View.VISIBLE);
            }
            doctorName.setText(mDoctorObject.getDocName());
            aboutDoctor.setText(mDoctorObject.getDegree());
            clinicAddress.setText(mDoctorObject.getAddressOfDoctorString());
            if (!mDoctorObject.getAddressOfDoctorString().isEmpty()) {

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.staticmap);
                requestOptions.error(R.drawable.staticmap);
                requestOptions.centerCrop();

                Glide.with(mContext)
                        .load("https://maps.googleapis.com/maps/api/staticmap?center=" + mDoctorObject.getAddressOfDoctorString() + "&markers=color:red%7Clabel:C%7C" + mDoctorObject.getAddressOfDoctorString() + "&zoom=12&size=640x300")
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
            if (!mDoctorObject.getAptTime().isEmpty() || !mDoctorObject.getAptDate().isEmpty()) {
                String dateValueToShow = (String) DateFormat.format("EEE", CommonMethods.convertStringToDate(mDoctorObject.getAptDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD));
                String ordinal = CommonMethods.ordinal(CommonMethods.getFormattedDate(mDoctorObject.getAptDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD, "dd"));
                String timeToShow = CommonMethods.formatDateTime(mDoctorObject.getAptTime(), RescribeConstants.DATE_PATTERN.hh_mm_a,
                        RescribeConstants.DATE_PATTERN.HH_mm_ss, RescribeConstants.TIME).toLowerCase();
                String dateToShow = dateValueToShow + ", " + ordinal + " " + CommonMethods.getFormattedDate(mDoctorObject.getAptDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD, "MMM yyyy") + " @" + timeToShow;
                showTimedate.setText(dateToShow);
            }
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
        ConfirmAppointmentActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

    }

    @OnClick({R.id.cancelButton, R.id.rescheduleButton, R.id.bookAppointmentBackButton, R.id.locationImageView, R.id.phoneNumberLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancelButton:
                if (mDoctorObject.isAppointmentTypeMixed()) {
                    showTokenStatusMessageBox(mContext.getString(R.string.cancel_token_msg), "0", MIXED_APPOINTMENT_TYPE);
                } else {
                    showTokenStatusMessageBox(mContext.getString(R.string.cancel_msg), mDoctorObject.getAptId(), CANCEL_TYPE);

                }
                break;
            case R.id.rescheduleButton:
                showTokenStatusMessageBox(mContext.getString(R.string.reschedule_msg), mDoctorObject.getAptId(), RESHEDULE_TYPE);
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
                ConfirmAppointmentActivityPermissionsDispatcher.doCallSupportWithCheck(this);
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
        if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_CANCEL_RESCHEDULE_APPOINTMENT)) {
            ResponseAppointmentConfirmationModel mResponseAppointmentConfirmationModel = (ResponseAppointmentConfirmationModel) customResponse;
            if (mResponseAppointmentConfirmationModel.getCommon() != null)
                if (mResponseAppointmentConfirmationModel.getCommon().isSuccess()) {
                    if (!isCanceled) {
                        if (mDoctorObject.isTypedashboard()) {
                            // Toast.makeText(mContext, mResponseAppointmentConfirmationModel.getCommon().getStatusMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(this, SelectSlotToBookAppointmentBaseActivity.class);
                            intent1.putExtra(getString(R.string.clicked_item_data_type_value), getString(R.string.chats));
                            intent1.putExtra(getString(R.string.toolbarTitle), getString(R.string.book_appointment));
                            ServicesCardViewImpl.setUserSelectedDoctorListDataObject(mDoctorObject);
                            startActivity(intent1);
                            setResult(RESCHEDULE_OK);
                            finish();
                        } else {
                            setResult(Activity.RESULT_OK);
                            finish();
                        }
                    } else {
                        Toast.makeText(mContext, mResponseAppointmentConfirmationModel.getCommon().getStatusMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ConfirmAppointmentActivity.this, BookAppointDoctorListBaseActivity.class);
                        Bundle bundle = new Bundle();
                        //This CALL_FROM_DASHBOARD is passed to handle onBackPressed of Book Appointment Page which is directed to HomePageActivity
                        bundle.putString(RescribeConstants.CALL_FROM_DASHBOARD, RescribeConstants.DASHBOARD_CALL_CONFIRMATION_PAGE);
                        bundle.putString(getString(R.string.clicked_item_data), getString(R.string.doctorss));
                        intent.putExtras(bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(mContext, mResponseAppointmentConfirmationModel.getCommon().getStatusMessage(), Toast.LENGTH_SHORT).show();
                }
        } else if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_CANCEL_GET_TOKEN)) {
            ResponseAppointmentConfirmationModel mResponseAppointmentConfirmationModel = (ResponseAppointmentConfirmationModel) customResponse;
            if (mResponseAppointmentConfirmationModel.getCommon() != null)
                if (mResponseAppointmentConfirmationModel.getCommon().isSuccess()) {
                    // Toast.makeText(mContext, mResponseAppointmentConfirmationModel.getCommon().getStatusMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ConfirmAppointmentActivity.this, BookAppointDoctorListBaseActivity.class);
                    Bundle bundle = new Bundle();
                    //This CALL_FROM_DASHBOARD is passed to handle onBackPressed of Book Appointment Page which is directed to HomePageActivity
                    bundle.putString(RescribeConstants.CALL_FROM_DASHBOARD, RescribeConstants.DASHBOARD_CALL_CONFIRMATION_PAGE);
                    bundle.putString(getString(R.string.clicked_item_data), getString(R.string.doctorss));
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
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

    public void showTokenStatusMessageBox(String message, final String aptId, final String type) {

        final Dialog dialog = new Dialog(mContext);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.token_dialog_popup);
        dialog.setCancelable(true);

        TextView messageView = (TextView) dialog.findViewById(R.id.messageView);
        ImageView icon_get_token = (ImageView) dialog.findViewById(R.id.icon);
        TextView textheading = (TextView) dialog.findViewById(R.id.textheading);
        textheading.setText(mContext.getString(R.string.appointment));
        messageView.setGravity(Gravity.CENTER);
        icon_get_token.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.bookappointment));
        if (type.equals(CANCEL_TYPE)) {
            isCanceled = true;
        } else {
            isCanceled = false;
        }
        messageView.setText(message);

        //-----------------
        TextView yesButton = (TextView) dialog.findViewById(R.id.yesButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equalsIgnoreCase(CANCEL_TYPE) || type.equalsIgnoreCase(RESHEDULE_TYPE)) {
                    mDoctorDataHelper.doCancelResheduleAppointmentRequest(aptId, 4, type);
                    dialog.cancel();
                } else {
                    mDoctorDataHelper.doCancelTokenNumber(mDoctorObject.getDocId(), mLocationId, mTokenNo);
                    dialog.cancel();
                }
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
