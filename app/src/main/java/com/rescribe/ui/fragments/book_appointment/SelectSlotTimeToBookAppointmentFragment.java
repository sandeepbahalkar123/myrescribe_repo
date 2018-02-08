package com.rescribe.ui.fragments.book_appointment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;
import com.philliphsu.bottomsheetpickers.time.BottomSheetTimePickerDialog;
import com.philliphsu.bottomsheetpickers.time.grid.GridTimePickerDialog;
import com.rescribe.R;
import com.rescribe.adapters.book_appointment.SelectSlotToBookAppointmentAdapter;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.helpers.book_appointment.ServicesCardViewImpl;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.Common;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.book_appointment.ConfirmTokenModel;
import com.rescribe.model.book_appointment.doctor_data.ClinicData;
import com.rescribe.model.book_appointment.doctor_data.ClinicTokenDetails;
import com.rescribe.model.book_appointment.doctor_data.ClinicTokenDetailsBaseModel;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.book_appointment.request_appointment_confirmation.Reschedule;
import com.rescribe.model.book_appointment.request_appointment_confirmation.ResponseAppointmentConfirmationModel;
import com.rescribe.model.book_appointment.select_slot_book_appointment.TimeSlotListBaseModel;
import com.rescribe.model.book_appointment.select_slot_book_appointment.TimeSlotListDataModel;
import com.rescribe.model.doctor_connect.ChatDoctor;
import com.rescribe.model.token.FCMTokenData;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.activities.ChatActivity;
import com.rescribe.ui.activities.book_appointment.MapActivityPlotNearByDoctor;
import com.rescribe.ui.activities.book_appointment.confirmation_type_activities.ConfirmAppointmentActivity;
import com.rescribe.ui.activities.book_appointment.confirmation_type_activities.ConfirmTokenInfoActivity;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.rescribe.adapters.book_appointment.ShowTimingsBookAppointmentDoctor.mAptslotId;
import static com.rescribe.adapters.book_appointment.ShowTimingsBookAppointmentDoctor.mSelectedTimeSlot;
import static com.rescribe.adapters.book_appointment.ShowTimingsBookAppointmentDoctor.mSelectedToTimeSlot;
import static com.rescribe.services.fcm.FCMService.TOKEN_DATA;
import static com.rescribe.ui.activities.DoctorConnectActivity.PAID;
import static com.rescribe.util.RescribeConstants.APPOINTMENT_ID;
import static com.rescribe.util.RescribeConstants.FROM;
import static com.rescribe.util.RescribeConstants.USER_STATUS.ONLINE;

/**
 * Created by jeetal on 31/10/17.
 */

public class SelectSlotTimeToBookAppointmentFragment extends Fragment implements HelperResponse, DatePickerDialog.OnDateSetListener, BottomSheetTimePickerDialog.OnTimeSetListener {

    public static final int CONFIRM_REQUESTCODE = 212;
    private final String TASKID_TIME_SLOT_WITH_DOC_DATA = RescribeConstants.TASK_TIME_SLOT_TO_BOOK_APPOINTMENT_WITH_DOCTOR_DETAILS;
    private final String TASKID_TIME_SLOT = RescribeConstants.TASK_TIME_SLOT_TO_BOOK_APPOINTMENT;
    //-------------
    @BindView(R.id.profileImage)
    CircularImageView mProfileImage;
    @BindView(R.id.docRating)
    CustomTextView mDocRating;
    @BindView(R.id.docRatingBar)
    RatingBar mDocRatingBar;
    @BindView(R.id.docRatingBarLayout)
    LinearLayout mDocRatingBarLayout;
    @BindView(R.id.doctorName)
    CustomTextView mDoctorName;
    @BindView(R.id.doctorSpecialization)
    CustomTextView mDoctorSpecialization;
    @BindView(R.id.rupeesLayout)
    LinearLayout mRupeesLayout;
    @BindView(R.id.doctorFees)
    CustomTextView mDoctorFees;
    @BindView(R.id.clinicName)
    CustomTextView mClinicName;
    @BindView(R.id.docPracticesLocationCount)
    CustomTextView mDocPracticesLocationCount;
    @BindView(R.id.premiumType)
    CustomTextView mPremiumType;
    @BindView(R.id.clinicNameSpinner)
    Spinner mClinicNameSpinner;
    @BindView(R.id.favorite)
    ImageView mFavorite;
    @BindView(R.id.doctorExperienceLayout)
    LinearLayout mDoctorExperienceLayout;
    @BindView(R.id.clinicNameSpinnerParentLayout)
    LinearLayout mClinicNameSpinnerParentLayout;
    @BindView(R.id.allClinicPracticeLocationMainLayout)
    LinearLayout mAllClinicPracticeLocationMainLayout;
    @BindView(R.id.doChat)
    ImageView doChat;
    @BindView(R.id.viewAllClinicsOnMap)
    ImageView viewAllClinicsOnMap;
    //-------------
    @BindView(R.id.leftArrow)
    ImageView mPreviousDayLeftArrow;
    @BindView(R.id.rightArrow)
    ImageView mNextDayRightArrow;
    @BindView(R.id.selectDateTime)
    CustomTextView mSelectDateTime;
    //-------------
    @BindView(R.id.selectTimeDateExpandableView)
    ExpandableListView selectTimeDateExpandableView;
    @BindView(R.id.appointmentTypeIsTokenButton)
    AppCompatButton appointmentTypeIsTokenButton;
    @BindView(R.id.appointmentTypeIsBookButton)
    AppCompatButton appointmentTypeIsBookButton;
    @BindView(R.id.appointmentTypeFooterButtonBarLayout)
    LinearLayout mAppointmentTypeFooterButtonBarLayout;
    @BindView(R.id.no_data_found)
    LinearLayout noDataFound;
    @BindView(R.id.timeSlotListViewLayout)
    LinearLayout mTimeSlotListViewLayout;
    @BindView(R.id.confirmedTokenMainLayout)
    LinearLayout mConfirmedTokenMainLayout;
    //--------------
    @BindView(R.id.waitingTime)
    CustomTextView mWaitingTime;
    @BindView(R.id.receivedTokenNumber)
    CustomTextView mReceivedTokenNumber;
    @BindView(R.id.scheduledAppointmentsTimeStamp)
    CustomTextView mScheduledAppointmentsTimeStamp;
    @BindView(R.id.selectClinicLine)
    View selectClinicLine;
    @BindView(R.id.yearsExperienceLine)
    View yearsExperienceLine;
    @BindView(R.id.ruppeeShadow)
    ImageView ruppeeShadow;
    Unbinder unbinder;
    private DoctorDataHelper mDoctorDataHelper;
    private DoctorList mClickedDoctorObject;
    private Context mContext;
    private int mLastExpandedPosition = -1;
    private SelectSlotToBookAppointmentAdapter mSelectSlotToBookAppointmentAdapter;
    private String mSelectedTimeSlotDate;
    private ClinicData mSelectedClinicDataObject;
    private int mSelectedClinicDataPosition = -1;
    private String activityOpeningFrom;
    private String mCurrentDate;
    private Date mMaxDateRange;
    private DatePickerDialog mDatePickerDialog;
    private String mSelectedTimeStampForNewToken;
    private ColorGenerator mColorGenerator;
    private Bundle bundleData;
    private FCMTokenData fcmTokenData;

    private String from;
    private String aptId;


    public SelectSlotTimeToBookAppointmentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mRootView = inflater.inflate(R.layout.book_appoint_doc_desc_select_time_slot, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        init();
        return mRootView;
    }

    public static SelectSlotTimeToBookAppointmentFragment newInstance(Bundle b) {
        SelectSlotTimeToBookAppointmentFragment fragment = new SelectSlotTimeToBookAppointmentFragment();
        Bundle args = b;
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    private void init() {
        mContext = getContext();
        mSelectedTimeSlot = null;
        mSelectedToTimeSlot = null;
        mAptslotId = null;

        yearsExperienceLine.setVisibility(View.GONE);
        selectClinicLine.setVisibility(View.GONE);
        mColorGenerator = ColorGenerator.MATERIAL;
        String coachMarkStatus = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.COACHMARK_GET_TOKEN, mContext);
        if (!coachMarkStatus.equals(RescribeConstants.YES)) {
            FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.coachmarkContainer, CoachFragment.newInstance());
            fragmentTransaction.addToBackStack("Coach");
            fragmentTransaction.commit();
        }

        Calendar now = Calendar.getInstance();
        //---------
        // As of version 2.3.0, `BottomSheetDatePickerDialog` is deprecated.
        mDatePickerDialog = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        //------------

        //----******************------
        String monthInt = "" + (now.get(Calendar.MONTH) + 1);
        if (monthInt.length() == 1) {
            monthInt = "0" + monthInt;
        }
        //----------
        String dayInt = "" + (now.get(Calendar.DAY_OF_MONTH));
        if (dayInt.length() == 1) {
            dayInt = "0" + dayInt;
        }
        //----------
        mSelectedTimeSlotDate = now.get(Calendar.YEAR) + "-" + monthInt + "-" + dayInt;
        mCurrentDate = mSelectedTimeSlotDate;
        //----******************------

        String dayFromDate = CommonMethods.getDayFromDate(RescribeConstants.DD_MM_YYYY, CommonMethods.getCurrentDate());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(RescribeConstants.DATE_PATTERN.DD_MMM, Locale.US);

        mSelectDateTime.setText(dayFromDate + ", " + simpleDateFormat.format(new Date()));
        //----------

        Bundle arguments = getArguments();
        if (arguments != null) {
            aptId = arguments.getString(APPOINTMENT_ID);
            from = arguments.getString(FROM);
            fcmTokenData = arguments.getParcelable(TOKEN_DATA);
            activityOpeningFrom = arguments.getString(getString(R.string.clicked_item_data_type_value));
            mSelectedClinicDataPosition = arguments.getInt(getString(R.string.selected_clinic_data_position), -1);
        }
        //--------------

        selectTimeDateExpandableView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (mLastExpandedPosition != -1
                        && groupPosition != mLastExpandedPosition) {
                    selectTimeDateExpandableView.collapseGroup(mLastExpandedPosition);
                }
                mLastExpandedPosition = groupPosition;
            }
        });
        selectTimeDateExpandableView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                selectTimeDateExpandableView.collapseGroup(groupPosition);
                return false;
            }
        });
        //--------------

    }

    @SuppressLint("CheckResult")
    private void setDataInViews() {
        if (mClickedDoctorObject.getClinicDataList().size() > 0) {
            mAppointmentTypeFooterButtonBarLayout.setVisibility(View.VISIBLE);
            noDataFound.setVisibility(View.GONE);
        } else {
            mAppointmentTypeFooterButtonBarLayout.setVisibility(View.GONE);
            noDataFound.setVisibility(View.VISIBLE);
        }

        if (mClickedDoctorObject.getDoctorImageUrl() != null) {

            String doctorName = mClickedDoctorObject.getDocName();
            if (doctorName.contains("Dr. ")) {
                doctorName = doctorName.replace("Dr. ", "");
            }
            int color2 = mColorGenerator.getColor(doctorName);
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .width(Math.round(getActivity().getResources().getDimension(R.dimen.dp40))) // width in px
                    .height(Math.round(getActivity().getResources().getDimension(R.dimen.dp40))) // height in px
                    .endConfig()
                    .buildRound(("" + doctorName.charAt(0)).toUpperCase(), color2);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.dontAnimate();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.skipMemoryCache(true);
            requestOptions.placeholder(drawable);
            requestOptions.error(drawable);

            Glide.with(getActivity())
                    .load(mClickedDoctorObject.getDoctorImageUrl())
                    .apply(requestOptions).thumbnail(0.5f)
                    .into(mProfileImage);


        }

        //-------
        if (mClickedDoctorObject.getFavourite()) {
            mFavorite.setImageResource(R.drawable.fav_icon);
        } else {
            mFavorite.setImageResource(R.drawable.result_line_heart_fav);
        }
        //---------------

        if (mClickedDoctorObject.getRating() == 0) {
            mDocRatingBarLayout.setVisibility(View.INVISIBLE);
        } else {
            mDocRatingBarLayout.setVisibility(View.VISIBLE);
            mDocRating.setText("" + mClickedDoctorObject.getRating());
            mDocRatingBar.setRating((float) mClickedDoctorObject.getRating());
        }
        //----------
        mDoctorName.setText("" + mClickedDoctorObject.getDocName());
        mDoctorSpecialization.setText("" + mClickedDoctorObject.getDegree());
        //------------
        mDoctorExperienceLayout.setVisibility(View.GONE);
        //----------
        int size = mClickedDoctorObject.getClinicDataList().size();
        if (size > 0) {
            mAllClinicPracticeLocationMainLayout.setVisibility(View.VISIBLE);

            String mainString = getString(R.string.practices_at_locations);
            if (size == 1) {
                mainString = mainString.substring(0, mainString.length() - 1);
            }
            String updatedString = mainString.replace("$$", "" + size);
            SpannableString contentExp = new SpannableString(updatedString);
            contentExp.setSpan(new ForegroundColorSpan(
                            ContextCompat.getColor(getActivity(), R.color.tagColor)),
                    13, 13 + String.valueOf(size).length(),//hightlight mSearchString
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            mDocPracticesLocationCount.setText(contentExp);
        } else {
            mAllClinicPracticeLocationMainLayout.setVisibility(View.GONE);
        }
        //------------
        if (!mClickedDoctorObject.getCategorySpeciality().equalsIgnoreCase("")) {
            mPremiumType.setText("" + mClickedDoctorObject.getCategorySpeciality());
            mPremiumType.setVisibility(View.VISIBLE);
        } else {
            mPremiumType.setVisibility(View.INVISIBLE);
        }
        //-------------------

        if (mClickedDoctorObject.getClinicDataList().size() > 0) {
            mClinicNameSpinnerParentLayout.setVisibility(View.VISIBLE);

            ArrayAdapter<ClinicData> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.global_item_simple_spinner, mClickedDoctorObject.getClinicDataList());
            mClinicNameSpinner.setAdapter(arrayAdapter);
            mClinicNameSpinner.setSelection(mSelectedClinicDataPosition, false);

            mClinicNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mSelectedClinicDataObject = mClickedDoctorObject.getClinicDataList().get(position);
                    changeViewBasedOnAppointmentType();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            if (mClickedDoctorObject.getClinicDataList().size() == 1) {
                mClinicNameSpinner.setEnabled(false);
                mClinicNameSpinner.setClickable(false);
                mClinicNameSpinner.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent));
            } else {
                mClinicNameSpinner.setEnabled(true);
                mClinicNameSpinner.setClickable(true);
                mClinicNameSpinner.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.spinner_bg));
            }
        } else {
            mClinicNameSpinnerParentLayout.setVisibility(View.GONE);
        }

        //---------
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

        switch (mOldDataTag) {

            //first & second case are alomst same, only change in doc_data param.
            case TASKID_TIME_SLOT:
                TimeSlotListBaseModel slotListBaseModel = (TimeSlotListBaseModel) customResponse;
                if (slotListBaseModel != null) {
                    TimeSlotListDataModel selectSlotList = slotListBaseModel.getTimeSlotListDataModel();
                    if (selectSlotList != null) {
                        if (!selectSlotList.getTimeSlotsInfoList().isEmpty()) {
                            selectTimeDateExpandableView.setVisibility(View.VISIBLE);
                            mSelectSlotToBookAppointmentAdapter = new SelectSlotToBookAppointmentAdapter(getActivity(), selectSlotList.getTimeSlotsInfoList(), mSelectedTimeSlotDate);
                            selectTimeDateExpandableView.setAdapter(mSelectSlotToBookAppointmentAdapter);
                        } else {
                            selectTimeDateExpandableView.setVisibility(View.GONE);
                            CommonMethods.showToast(getActivity(), "" + slotListBaseModel.getCommon().getStatusMessage());
                        }
                    }
                }
                break;
            case TASKID_TIME_SLOT_WITH_DOC_DATA:
                TimeSlotListBaseModel slotListBase = (TimeSlotListBaseModel) customResponse;
                if (slotListBase != null) {
                    TimeSlotListDataModel selectSlotList = slotListBase.getTimeSlotListDataModel();
                    if (selectSlotList != null) {
                        //----*************----
                        mClickedDoctorObject = selectSlotList.getDoctorListData();
                        ServicesCardViewImpl.setUserSelectedDoctorListDataObject(mClickedDoctorObject);
                        //----*************----
                        setDataInViews();
                        //--------------------
                        if (!selectSlotList.getTimeSlotsInfoList().isEmpty()) {
                            selectTimeDateExpandableView.setVisibility(View.VISIBLE);
                            mSelectSlotToBookAppointmentAdapter = new SelectSlotToBookAppointmentAdapter(getActivity(), selectSlotList.getTimeSlotsInfoList(), mSelectedTimeSlotDate);
                            selectTimeDateExpandableView.setAdapter(mSelectSlotToBookAppointmentAdapter);
                        } else {
                            selectTimeDateExpandableView.setVisibility(View.GONE);
                        }
                    }
                }
                break;
            case RescribeConstants.TASK_SET_FAVOURITE_DOCTOR:
                CommonBaseModelContainer temp = (CommonBaseModelContainer) customResponse;
                if (temp.getCommonRespose().isSuccess()) {
                    boolean isUpdated = ServicesCardViewImpl.updateFavStatusForDoctorDataObject(mClickedDoctorObject);
                    //----THIS IS DONE FOR, WHEN PAGE OPENED FROM CHAT_ACTIVITY---
                    if (getString(R.string.chats).equalsIgnoreCase(activityOpeningFrom) && isUpdated) {
                        mClickedDoctorObject.setFavourite(!mClickedDoctorObject.getFavourite());
                    }
                    //-------
                    if (mClickedDoctorObject.getFavourite()) {
                        mFavorite.setImageResource(R.drawable.fav_icon);
                    } else {
                        mFavorite.setImageResource(R.drawable.result_line_heart_fav);
                    }
                }
                //   CommonMethods.showToast(getActivity(), temp.getCommonRespose().getStatusMessage());
                break;
            case RescribeConstants.TASK_GET_TOKEN_NUMBER_OTHER_DETAILS:
                ClinicTokenDetailsBaseModel clinicTokenDetailsBaseModel = (ClinicTokenDetailsBaseModel) customResponse;
                if (clinicTokenDetailsBaseModel != null) {
                    Common common = clinicTokenDetailsBaseModel.getCommon();
                    ClinicTokenDetails clinicTokenDetails = clinicTokenDetailsBaseModel.getClinicTokenDetails();
                    if (clinicTokenDetails != null) {
                        mWaitingTime.setText("" + clinicTokenDetails.getWaitingTime());
                        mScheduledAppointmentsTimeStamp.setText("" + clinicTokenDetails.getScheduledTimeStamp());
                        mReceivedTokenNumber.setText("" + clinicTokenDetails.getTokenNumber());
                    } else {
                        showTokenStatusMessageBox(-1, common.getStatusMessage(), mSelectedTimeStampForNewToken, mClickedDoctorObject.getDocId(), mSelectedClinicDataObject.getLocationId());
                    }
                }
                break;
            case RescribeConstants.TASK_TO_SET_TOKEN_NOTIFICATION_REMAINDER:
                CommonBaseModelContainer temp1 = (CommonBaseModelContainer) customResponse;
                CommonMethods.showToast(getActivity(), temp1.getCommonRespose().getStatusMessage());
                getActivity().finish();
                break;
            case RescribeConstants.TASK_TO_UNREAD_TOKEN_REMAINDER_CONFIRMATION:

                //get token flow redirected to confirmation page .
                if (customResponse != null) {
                    ConfirmTokenModel confirmTokenModel = (ConfirmTokenModel) customResponse;
                    CommonMethods.showToast(getActivity(), confirmTokenModel.getCommon().getStatusMessage());
                    if (confirmTokenModel.getCommon().isSuccess()) {
                        if (mSelectedClinicDataObject.getAppointmentType().equalsIgnoreCase(RescribeConstants.MIXED_APPOINTMENT_TYPE)) {
                            bundleData = new Bundle();
                            mClickedDoctorObject.setTypedashboard(false);
                            mClickedDoctorObject.setAppointmentTypeMixed(true);
                            mClickedDoctorObject.setAddressOfDoctorString(mSelectedClinicDataObject.getClinicAddress());
                            CommonMethods.formatDateTime(mScheduledAppointmentsTimeStamp.getText().toString(), RescribeConstants.DATE_PATTERN.hh_mm, RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.TIME);
                            mClickedDoctorObject.setAptTime(CommonMethods.formatDateTime(mScheduledAppointmentsTimeStamp.getText().toString(), RescribeConstants.DATE_PATTERN.HH_mm_ss, RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.TIME));
                            mClickedDoctorObject.setAptDate(mSelectedTimeSlotDate);
                            bundleData.putParcelable(getString(R.string.clicked_item_data), mClickedDoctorObject);
                            bundleData.putString(RescribeConstants.LOCATION_ID, "" + mSelectedClinicDataObject.getLocationId());
                            bundleData.putString(RescribeConstants.TOKEN_NO, String.valueOf(confirmTokenModel.getData().getTokenDetail().getTokenNumber()));
                            bundleData.putString(RescribeConstants.WAITING_TIME, confirmTokenModel.getData().getTokenDetail().getWaitingPatientTime());
                            bundleData.putString(RescribeConstants.WAITING_COUNT, confirmTokenModel.getData().getTokenDetail().getWaitingPatientCount());
                            Intent intentObject = new Intent(getContext(), ConfirmTokenInfoActivity.class);
                            intentObject.putExtras(bundleData);
                            startActivity(intentObject);
                        }
                    }

                    break;
                }
            case RescribeConstants.TASK_CONFIRM_APPOINTMENT:
                if (customResponse != null) {
                    ResponseAppointmentConfirmationModel mResponseAppointmentConfirmationModel = (ResponseAppointmentConfirmationModel) customResponse;
                    if (mResponseAppointmentConfirmationModel.getCommon().isSuccess()) {

                        bundleData = new Bundle();

                        mClickedDoctorObject.setTypedashboard(false);
                        mClickedDoctorObject.setRating(mResponseAppointmentConfirmationModel.getAptList().getRating());
                        mClickedDoctorObject.setAptId(mResponseAppointmentConfirmationModel.getAptList().getId());
                        mClickedDoctorObject.setDocId(mResponseAppointmentConfirmationModel.getAptList().getDoc_id());
                        mClickedDoctorObject.setDocPhone(mResponseAppointmentConfirmationModel.getAptList().getDocPhone());
                        mClickedDoctorObject.setDegree(mResponseAppointmentConfirmationModel.getAptList().getDoctorDegree());
                        mClickedDoctorObject.setDocName(mResponseAppointmentConfirmationModel.getAptList().getDoctorName());
                        mClickedDoctorObject.setDoctorImageUrl(mResponseAppointmentConfirmationModel.getAptList().getImageUrl());
                        mClickedDoctorObject.setAptTime(mResponseAppointmentConfirmationModel.getAptList().getAptTime());
                        mClickedDoctorObject.setAptDate(mResponseAppointmentConfirmationModel.getAptList().getAptDate());
                        mClickedDoctorObject.setNameOfClinicString(mResponseAppointmentConfirmationModel.getAptList().getClinic_name());
                        mClickedDoctorObject.setAddressOfDoctorString(mResponseAppointmentConfirmationModel.getAptList().getAddress());
                        bundleData.putParcelable(getString(R.string.clicked_item_data), mClickedDoctorObject);
                        bundleData.putString(RescribeConstants.LOCATION_ID, "" + 0);
                        bundleData.putString(RescribeConstants.TOKEN_NO, "" + 0);
                        Intent intentObject = new Intent(getContext(), ConfirmAppointmentActivity.class);
                        intentObject.putExtras(bundleData);
                        startActivityForResult(intentObject, CONFIRM_REQUESTCODE);
                        //This is done to call api for token slot when user clicks on cancel button of confirmpage.
                        mDoctorDataHelper = null;

                    } else {
                        Toast.makeText(mContext, mResponseAppointmentConfirmationModel.getCommon().getStatusMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

          /*  case TASK_CANCEL_RESCHEDULE_APPOINTMENT:
                ResponseAppointmentConfirmationModel mResponseAppointmentConfirmationModel = (ResponseAppointmentConfirmationModel) customResponse;
                if (mResponseAppointmentConfirmationModel.getCommon() != null)
                    if (mResponseAppointmentConfirmationModel.getCommon().isSuccess())
                        mDoctorDataHelper.doConfirmAppointmentRequest(mClickedDoctorObject.getDocId(), mSelectedClinicDataObject.getLocationId(), mSelectedTimeSlotDate, mSelectSlotToBookAppointmentAdapter.getSelectedTimeSlot(), mSelectSlotToBookAppointmentAdapter.getToTimeSlot(), Integer.parseInt(mSelectSlotToBookAppointmentAdapter.getSlotId()), null);
                    else
                        Toast.makeText(mContext, mResponseAppointmentConfirmationModel.getCommon().getStatusMessage(), Toast.LENGTH_SHORT).show();
                break;*/
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CONFIRM_REQUESTCODE) {
                mSelectedTimeSlot = null;
                mSelectedToTimeSlot = null;
                mAptslotId = null;
                init();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mDoctorDataHelper == null) {


            mDoctorDataHelper = new DoctorDataHelper(getActivity(), this);

            mClickedDoctorObject = ServicesCardViewImpl.getUserSelectedDoctorListDataObject();
            //--------------
            if (mSelectedClinicDataPosition != -1) {
                if (mClickedDoctorObject.getClinicDataList().size() > 0)
                    mSelectedClinicDataObject = mClickedDoctorObject.getClinicDataList().get(mSelectedClinicDataPosition);
            } //--------------

            if (getString(R.string.chats).equalsIgnoreCase(activityOpeningFrom)) {
                mConfirmedTokenMainLayout.setVisibility(View.GONE);
                mTimeSlotListViewLayout.setVisibility(View.VISIBLE);
                mDoctorDataHelper.getTimeSlotToBookAppointmentWithDoctor("" + mClickedDoctorObject.getDocId(), 0, mSelectedTimeSlotDate, true, TASKID_TIME_SLOT_WITH_DOC_DATA);

                if (fcmTokenData != null) {
                    showTokenStatusMessageBox(fcmTokenData.getTokenNumber(), fcmTokenData.getMsg(), null, fcmTokenData.getDocId(), fcmTokenData.getLocationId());
                }

            } else {
                setDataInViews();
                changeViewBasedOnAppointmentType();

            }

        }

    }

    @OnClick({R.id.selectDateTime, R.id.appointmentTypeIsTokenButton, R.id.viewAllClinicsOnMap, R.id.favorite, R.id.doChat, R.id.tokenNewTimeStamp, R.id.leftArrow, R.id.rightArrow, R.id.appointmentTypeIsBookButton})
    public void onClickOfView(View view) {
        Calendar now = Calendar.getInstance();
        switch (view.getId()) {
            case R.id.selectDateTime:

                //---------
                Calendar selectedTimeSlotDateCal = Calendar.getInstance();
                Date date1 = CommonMethods.convertStringToDate(mSelectedTimeSlotDate, RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
                selectedTimeSlotDateCal.setTime(date1);
                mDatePickerDialog = DatePickerDialog.newInstance(
                        this,
                        selectedTimeSlotDateCal.get(Calendar.YEAR),
                        selectedTimeSlotDateCal.get(Calendar.MONTH),
                        selectedTimeSlotDateCal.get(Calendar.DAY_OF_MONTH));
                //---------

                mDatePickerDialog.setAccentColor(getResources().getColor(R.color.tagColor));
                mDatePickerDialog.setMinDate(now);
                mDatePickerDialog.show(getFragmentManager(), getResources().getString(R.string.select_date_text));
                mDatePickerDialog.setOutOfRageInvisible();

                //-------------
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, mSelectedClinicDataObject.getApptScheduleLmtDays());
                mMaxDateRange = calendar.getTime();
                mDatePickerDialog.setMaxDate(calendar);
                //-------------

                break;
            case R.id.appointmentTypeIsTokenButton:
                String receiveTokenNo = mReceivedTokenNumber.getText().toString().trim();
                if (receiveTokenNo.length() > 0) {
                    int receivedTokenNo = Integer.parseInt(receiveTokenNo);
                    mDoctorDataHelper.doConfirmBookAppointReceivedToken(mSelectedTimeStampForNewToken, mClickedDoctorObject.getDocId(), mSelectedClinicDataObject.getLocationId(), receivedTokenNo);
                }
                break;
            case R.id.appointmentTypeIsBookButton:
                if (mSelectSlotToBookAppointmentAdapter != null) {
                    if (RescribeConstants.BLANK.equalsIgnoreCase(mSelectSlotToBookAppointmentAdapter.getSelectedTimeSlot()) || mSelectSlotToBookAppointmentAdapter.getSelectedTimeSlot() == null) {
                        CommonMethods.showToast(getContext(), getString(R.string.time_select_err));
                    } else {
                        if (from != null) {
//                            mDoctorDataHelper.doCancelResheduleAppointmentRequest(aptId, 4, type);
                            Reschedule reschedule = new Reschedule();
                            reschedule.setAptId(aptId);
                            reschedule.setStatus("4");

                            mDoctorDataHelper.doConfirmAppointmentRequest(mClickedDoctorObject.getDocId(), mSelectedClinicDataObject.getLocationId(), mSelectedTimeSlotDate, mSelectSlotToBookAppointmentAdapter.getSelectedTimeSlot(), mSelectSlotToBookAppointmentAdapter.getToTimeSlot(), Integer.parseInt(mSelectSlotToBookAppointmentAdapter.getSlotId()), reschedule);
                        } else {
                            mDoctorDataHelper.doConfirmAppointmentRequest(mClickedDoctorObject.getDocId(), mSelectedClinicDataObject.getLocationId(), mSelectedTimeSlotDate, mSelectSlotToBookAppointmentAdapter.getSelectedTimeSlot(), mSelectSlotToBookAppointmentAdapter.getToTimeSlot(), Integer.parseInt(mSelectSlotToBookAppointmentAdapter.getSlotId()), null);
                        }
                    }
                }


                break;
            case R.id.viewAllClinicsOnMap: // on view-all location clicked
                //-----Show all doc clinic on map, copied from BookAppointFilteredDoctorListFragment.java----
                //this list is sorted for plotting map for each clinic location, the values of clinicName and doctorAddress are set in string here, which are coming from arraylist.
                ArrayList<DoctorList> doctorListByClinics = new ArrayList<>();
                ArrayList<ClinicData> clinicNameList = mClickedDoctorObject.getClinicDataList();
                for (int i = 0; i < clinicNameList.size(); i++) {
                    DoctorList doctorListByClinic;
                    try {
                        doctorListByClinic = (DoctorList) mClickedDoctorObject.clone();
                        doctorListByClinic.setAddressOfDoctorString(clinicNameList.get(i).getClinicAddress());
                        doctorListByClinic.setNameOfClinicString(clinicNameList.get(i).getClinicName());
                        doctorListByClinics.add(doctorListByClinic);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
                Intent intentObjectMap = new Intent(getActivity(), MapActivityPlotNearByDoctor.class);
                intentObjectMap.putParcelableArrayListExtra(getString(R.string.doctor_data), doctorListByClinics);
                intentObjectMap.putExtra(getString(R.string.toolbarTitle), getString(R.string.doctor));
                intentObjectMap.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentObjectMap.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentObjectMap);
                //--------
                break;
            case R.id.favorite:
                mDoctorDataHelper.setFavouriteDoctor(!mClickedDoctorObject.getFavourite(), mClickedDoctorObject.getDocId());
                break;
            case R.id.doChat:
                if (mClickedDoctorObject.getPaidStatus() == PAID)
                    CommonMethods.showInfoDialog(getResources().getString(R.string.paid_doctor_message), getContext(), false);
                else {
                    ChatDoctor chatDoctor = new ChatDoctor();
                    chatDoctor.setId(mClickedDoctorObject.getDocId());
                    chatDoctor.setDoctorName(mClickedDoctorObject.getDocName());
                    chatDoctor.setOnlineStatus(ONLINE);
                    chatDoctor.setAddress(mClickedDoctorObject.getAddressOfDoctorString());
                    chatDoctor.setImageUrl(mClickedDoctorObject.getDoctorImageUrl());

                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra(RescribeConstants.DOCTORS_INFO, chatDoctor);
                    startActivity(intent);
                }
                break;
            case R.id.tokenNewTimeStamp:
                GridTimePickerDialog grid = GridTimePickerDialog.newInstance(
                        this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        DateFormat.is24HourFormat(getActivity()));

                grid.setAccentColor(getResources().getColor(R.color.tagColor));
                grid.show(getFragmentManager(), getResources().getString(R.string.select_date_text));
                break;
            case R.id.leftArrow:

                isShowPreviousDayLeftArrow(true);
                break;

            case R.id.rightArrow:

                Calendar calendarNow = Calendar.getInstance();
                calendarNow.add(Calendar.DATE, mSelectedClinicDataObject.getApptScheduleLmtDays());
                mMaxDateRange = calendarNow.getTime();
                //---------
                Date date = CommonMethods.convertStringToDate(this.mSelectedTimeSlotDate, RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
                Calendar c = Calendar.getInstance();
                c.setTime(date); // Now use today date.
                c.add(Calendar.DATE, 1); // Adding 1 days
                date = c.getTime();
                //---------
                if (mMaxDateRange.getTime() >= date.getTime()) {
                    onDateSet(mDatePickerDialog, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                }

                break;
        }
    }

    private void isShowPreviousDayLeftArrow(boolean isCallOnDateSet) {

        mPreviousDayLeftArrow.setVisibility(View.VISIBLE);
        //------------
        Date receivedDate = CommonMethods.convertStringToDate(this.mSelectedTimeSlotDate, RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
        Calendar cc = Calendar.getInstance();
        cc.setTime(receivedDate); // Now use today date.
        cc.add(Calendar.DATE, -1); // subtracting 1 days
        receivedDate = cc.getTime();
        //-----------
        String formattedCurrentDateString = CommonMethods.formatDateTime(CommonMethods.getCurrentDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DD_MM_YYYY, RescribeConstants.DATE);
        SimpleDateFormat dateFormat = new SimpleDateFormat(RescribeConstants.DATE_PATTERN.YYYY_MM_DD, Locale.US);
        String receivedDateString = dateFormat.format(receivedDate);
        //------------
        Date currentDate = new Date();
        //------------
        if ((currentDate.getTime() < receivedDate.getTime()) || (formattedCurrentDateString.equalsIgnoreCase(receivedDateString))) {
            if (isCallOnDateSet)
                onDateSet(mDatePickerDialog, cc.get(Calendar.YEAR), cc.get(Calendar.MONTH), cc.get(Calendar.DAY_OF_MONTH));
        } else {
            mPreviousDayLeftArrow.setVisibility(View.INVISIBLE);
        }
    }

    private void changeViewBasedOnAppointmentType() {
        if (mSelectedClinicDataObject != null) {
            //----------
            String clinicName = mSelectedClinicDataObject.getClinicName();
            if (clinicName.equals("")) {
                mClinicName.setVisibility(View.GONE);
            } else {
                mClinicName.setVisibility(View.VISIBLE);
                mClinicName.setText("" + clinicName);
            }
            //----------

            if (mSelectedClinicDataObject.getAmount() == 0) {
                mRupeesLayout.setVisibility(View.INVISIBLE);
                ruppeeShadow.setVisibility(View.INVISIBLE);
            } else {
                mRupeesLayout.setVisibility(View.VISIBLE);
                ruppeeShadow.setVisibility(View.VISIBLE);
                mDoctorFees.setText("" + mSelectedClinicDataObject.getAmount());
            }
            //-----------
            if (mSelectedClinicDataObject.getAppointmentType().equalsIgnoreCase(getString(R.string.token))) {
                mConfirmedTokenMainLayout.setVisibility(View.VISIBLE);
                mTimeSlotListViewLayout.setVisibility(View.GONE);
                //----
                mAppointmentTypeFooterButtonBarLayout.setVisibility(View.VISIBLE);
                appointmentTypeIsTokenButton.setVisibility(View.VISIBLE);
                appointmentTypeIsBookButton.setVisibility(View.GONE);
                //----
                mPreviousDayLeftArrow.setVisibility(View.INVISIBLE);
                mNextDayRightArrow.setVisibility(View.INVISIBLE);
                mSelectDateTime.setEnabled(false);
                mDoctorDataHelper.getTokenNumberDetails("" + mClickedDoctorObject.getDocId(), mSelectedClinicDataObject.getLocationId(), null);
            } else if (mSelectedClinicDataObject.getAppointmentType().equalsIgnoreCase(getString(R.string.book))) {
                mConfirmedTokenMainLayout.setVisibility(View.GONE);
                mTimeSlotListViewLayout.setVisibility(View.VISIBLE);
                //----
                mAppointmentTypeFooterButtonBarLayout.setVisibility(View.VISIBLE);
                appointmentTypeIsTokenButton.setVisibility(View.GONE);
                appointmentTypeIsBookButton.setVisibility(View.VISIBLE);
                //----
                mPreviousDayLeftArrow.setVisibility(View.INVISIBLE);
                mNextDayRightArrow.setVisibility(View.VISIBLE);
                mSelectDateTime.setEnabled(true);
                //------------
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, mSelectedClinicDataObject.getApptScheduleLmtDays());
                mMaxDateRange = calendar.getTime();
                //------------
                mDoctorDataHelper.getTimeSlotToBookAppointmentWithDoctor("" + mClickedDoctorObject.getDocId(), mSelectedClinicDataObject.getLocationId(), mSelectedTimeSlotDate, false, TASKID_TIME_SLOT);
            } else if (mSelectedClinicDataObject.getAppointmentType().equalsIgnoreCase(getString(R.string.mixed))) {
                //------------
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, mSelectedClinicDataObject.getApptScheduleLmtDays());
                mMaxDateRange = calendar.getTime();
                //------------

                String selectedDate = CommonMethods.getFormattedDate(mSelectedTimeSlotDate, RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(RescribeConstants.DATE_PATTERN.YYYY_MM_DD, Locale.US);
                String maxDate = simpleDateFormat.format(mMaxDateRange);
                //--------
                // IF MIXED, FOR SELECTED DATE == CURRENT DATE --> TOKEN FLOW WILL WORK
                //  FOR SELECTED DATE OTHER THAN CURRENT DATE--> BOOK FLOW WILL WORK
                // COPIED ABOVE 2 TOKEN AND BOOK FUNCTIONALITY. (EXCEPT mSelectDateTime.setEnabled);
                if (mSelectedTimeSlotDate.equalsIgnoreCase(mCurrentDate)) {
                    mConfirmedTokenMainLayout.setVisibility(View.VISIBLE);
                    mTimeSlotListViewLayout.setVisibility(View.GONE);

                    //----
                    mAppointmentTypeFooterButtonBarLayout.setVisibility(View.VISIBLE);
                    appointmentTypeIsTokenButton.setVisibility(View.VISIBLE);
                    appointmentTypeIsBookButton.setVisibility(View.GONE);
                    //----

                    if (selectedDate.equalsIgnoreCase(maxDate)) {
                        mPreviousDayLeftArrow.setVisibility(View.INVISIBLE);
                        mNextDayRightArrow.setVisibility(View.INVISIBLE);
                    } else {
                        mPreviousDayLeftArrow.setVisibility(View.INVISIBLE);
                        mNextDayRightArrow.setVisibility(View.VISIBLE);
                    }
                    //--------

                    mSelectDateTime.setEnabled(true);
                    mDoctorDataHelper.getTokenNumberDetails("" + mClickedDoctorObject.getDocId(), mSelectedClinicDataObject.getLocationId(), null);
                } else {
                    mConfirmedTokenMainLayout.setVisibility(View.GONE);
                    mTimeSlotListViewLayout.setVisibility(View.VISIBLE);
                    //--------
                    //----
                    mAppointmentTypeFooterButtonBarLayout.setVisibility(View.VISIBLE);
                    appointmentTypeIsTokenButton.setVisibility(View.GONE);
                    appointmentTypeIsBookButton.setVisibility(View.VISIBLE);
                    //----
                    //--------
                    if (selectedDate.equalsIgnoreCase(maxDate)) {
                        mPreviousDayLeftArrow.setVisibility(View.VISIBLE);
                        mNextDayRightArrow.setVisibility(View.INVISIBLE);
                    } else {
                        mNextDayRightArrow.setVisibility(View.VISIBLE);
                        mPreviousDayLeftArrow.setVisibility(View.VISIBLE);
                    }
                    //--------

                    mSelectDateTime.setEnabled(true);
                    mDoctorDataHelper.getTimeSlotToBookAppointmentWithDoctor("" + mClickedDoctorObject.getDocId(), mSelectedClinicDataObject.getLocationId(), mSelectedTimeSlotDate, false, TASKID_TIME_SLOT);
                }
            }
        }
    }

    @Override
    public void onTimeSet(ViewGroup viewGroup, int hourOfDay, int minute) {
        //-----------------
        String currentTimeStamp = CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
        Date maxCurrentDateTimeForDay = CommonMethods.convertStringToDate(currentTimeStamp + " " + "23:59:59", RescribeConstants.DATE_PATTERN.YYYY_MM_DD_HH_mm_ss);
        //---
        Date currentDateTimeForDay = CommonMethods.convertStringToDate(CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.YYYY_MM_DD_HH_mm_ss), RescribeConstants.DATE_PATTERN.YYYY_MM_DD_HH_mm_ss);
        Date selectedDateTimeForDay = CommonMethods.convertStringToDate(mSelectedTimeSlotDate + " " + hourOfDay + ":" + minute + ":00", RescribeConstants.DATE_PATTERN.YYYY_MM_DD_HH_mm_ss);

        if (currentDateTimeForDay.getTime() > selectedDateTimeForDay.getTime()) {
            CommonMethods.showToast(getContext(), getString(R.string.token_select_time_err_msg));
        } else if (maxCurrentDateTimeForDay.getTime() < selectedDateTimeForDay.getTime()) {
            CommonMethods.showToast(getContext(), getString(R.string.token_select_time_err_msg));
        } else {
            if (mSelectedClinicDataObject != null) {
                mSelectedTimeStampForNewToken = hourOfDay + ":" + minute;
                mDoctorDataHelper.getTokenNumberDetails("" + mClickedDoctorObject.getDocId(), mSelectedClinicDataObject.getLocationId(), hourOfDay + ":" + minute);
            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        String dateConverted = "" + dayOfMonth;
        if (dayOfMonth < 10) {
            dateConverted = "0" + dayOfMonth;
        }

        String monthOfYearData = "" + (monthOfYear + 1);
        if (monthOfYearData.length() == 1) {
            monthOfYearData = "0" + monthOfYearData;
        }

        String dayFromDate = CommonMethods.getDayFromDate(RescribeConstants.DATE_PATTERN.DD_MM_YYYY, dateConverted + "-" + monthOfYearData + "-" + year);

        mSelectedTimeSlotDate = year + "-" + monthOfYearData + "-" + dateConverted;
        mSelectDateTime.setText(dayFromDate + ", " + CommonMethods.getFormattedDate(dateConverted + "-" + monthOfYearData + "-" + year, RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE_PATTERN.DD_MMM));

        if (mSelectedClinicDataObject.getAppointmentType().equalsIgnoreCase(getString(R.string.mixed))) {
            changeViewBasedOnAppointmentType();
        } else {
            //-- TO SHOW LEFT ARROW FOR NAVIGATION TO PREVIOUS DATE.
            if (mSelectedClinicDataObject.getAppointmentType().equalsIgnoreCase(getString(R.string.book))) {
                isShowPreviousDayLeftArrow(false);
            }
            mDoctorDataHelper.getTimeSlotToBookAppointmentWithDoctor("" + mClickedDoctorObject.getDocId(), mSelectedClinicDataObject.getLocationId(), mSelectedTimeSlotDate, false, TASKID_TIME_SLOT);
        }
    }

    public void showTokenStatusMessageBox(final int tokenNumber, String message, final String mSelectedTimeStampForNewT, final int mDocId, final int mLocationId) {

        final Dialog dialog = new Dialog(mContext);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.token_dialog_popup);
        dialog.setCancelable(true);

        TextView messageView = (TextView) dialog.findViewById(R.id.messageView);

        String textToStyle = "Sorry";

        if (message.contains(textToStyle)) {
            SpannableString spannableStringSearch = new SpannableString(message);
            Pattern pattern = Pattern.compile(textToStyle, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(message);
            while (matcher.find()) {
                spannableStringSearch.setSpan(new ForegroundColorSpan(Color.RED),
                        matcher.start(), matcher.end(),//hightlight mSearchString
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringSearch.setSpan(new StyleSpan(Typeface.BOLD), matcher.start(), matcher.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }

            messageView.setText(spannableStringSearch);
        } else messageView.setText(message);

        //-----------------
        TextView yesButton = (TextView) dialog.findViewById(R.id.yesButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tokenNumber != -1)
                    mDoctorDataHelper.doConfirmBookAppointReceivedToken(mSelectedTimeStampForNewT, mDocId, mLocationId, tokenNumber);
                else
                    mDoctorDataHelper.doSetTokenNotificationReminder(mSelectedTimeStampForNewT, mDocId, mLocationId);
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