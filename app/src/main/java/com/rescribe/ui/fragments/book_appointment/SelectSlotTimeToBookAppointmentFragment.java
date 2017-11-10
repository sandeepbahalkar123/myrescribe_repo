package com.rescribe.ui.fragments.book_appointment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;
import com.rescribe.R;
import com.rescribe.adapters.book_appointment.SelectSlotToBookAppointmentAdapter;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.book_appointment.select_slot_book_appointment.TimeSlotListDataModel;
import com.rescribe.model.book_appointment.select_slot_book_appointment.TimeSlotListBaseModel;
import com.rescribe.model.book_appointment.doctor_data.ClinicData;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.activities.book_appointment.MapActivityPlotNearByDoctor;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by jeetal on 31/10/17.
 */

public class SelectSlotTimeToBookAppointmentFragment extends Fragment implements HelperResponse, BookAppointDoctorListBaseActivity.AddUpdateViewDataListener, DatePickerDialog.OnDateSetListener {

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
    //-------------
    @BindView(R.id.leftArrow)
    ImageView leftArrow;
    @BindView(R.id.rightArrow)
    ImageView rightArrow;
    @BindView(R.id.selectDateTime)
    CustomTextView selectDateTime;
    @BindView(R.id.selectTimeDateExpandableView)
    ExpandableListView selectTimeDateExpandableView;
    //--------------
    private View mRootView;
    private int mImageSize;
    Unbinder unbinder;

    private DoctorDataHelper mDoctorDataHelper;
    private DoctorList mClickedDoctorObject;
    public static Bundle args;
    private Context mContext;
    private int mLastExpandedPosition = -1;
    private SelectSlotToBookAppointmentAdapter mSelectSlotToBookAppointmentAdapter;
    private String mSelectedTimeSlotDate;
    private ClinicData mSelectedClinicDataObject;

    public SelectSlotTimeToBookAppointmentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.book_appoint_doc_desc_select_time_slot, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        init();
        return mRootView;
    }

    public static SelectSlotTimeToBookAppointmentFragment newInstance(Bundle b) {
        SelectSlotTimeToBookAppointmentFragment fragment = new SelectSlotTimeToBookAppointmentFragment();
        args = b;
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    private void init() {
        //------------
        Calendar now = Calendar.getInstance();
        mSelectedTimeSlotDate = now.get(Calendar.YEAR) + "-" + now.get((Calendar.MONTH + 1)) + "-" + now.get(Calendar.DAY_OF_MONTH);
        //----------

        String dayFromDate = CommonMethods.getDayFromDate(RescribeConstants.DD_MM_YYYY, CommonMethods.getCurrentDate());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(RescribeConstants.DATE_PATTERN.DD_MMM);

        selectDateTime.setText(dayFromDate + ", " + simpleDateFormat.format(new Date()));
        //----------
        mDoctorDataHelper = new DoctorDataHelper(getActivity(), this);
        // setColumnNumber(getActivity(), 2);

        //     BookAppointDoctorListBaseActivity.setToolBarTitle(args.getString(getString(R.string.toolbarTitle)), false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mClickedDoctorObject = arguments.getParcelable(getString(R.string.clicked_item_data));
            setDataInViews();
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

    private void setColumnNumber(Context context, int columnNum) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        mImageSize = (widthPixels / columnNum) - CommonMethods.convertDpToPixel(30);
    }

    private void setDataInViews() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        requestOptions.override(mImageSize, mImageSize);
        requestOptions.placeholder(R.drawable.layer_12);

        Glide.with(getActivity())
                .load(mClickedDoctorObject.getDoctorImageUrl())
                .apply(requestOptions).thumbnail(0.5f)
                .into(mProfileImage);
        //-------
        if (mClickedDoctorObject.getFavourite()) {
            mFavorite.setImageResource(R.drawable.fav_icon);
        } else {
            mFavorite.setImageResource(R.drawable.result_line_heart_fav);
        }
        //---------------
        String rating = "" + mClickedDoctorObject.getRating();
        if (rating == null || RescribeConstants.BLANK.equalsIgnoreCase(rating)) {
            mDocRatingBarLayout.setVisibility(View.INVISIBLE);
        } else {
            mDocRatingBarLayout.setVisibility(View.VISIBLE);
            mDocRating.setText("" + rating);
            mDocRatingBar.setRating(Float.parseFloat(rating));
        }
        //----------
        mDoctorName.setText("" + mClickedDoctorObject.getDocName());
        mDoctorSpecialization.setText("" + mClickedDoctorObject.getDegree());
        //------------
        mDoctorExperienceLayout.setVisibility(View.GONE);
        //----------
        int size = mClickedDoctorObject.getClinicDataList().size();
        if (size > 0) {
            String updatedString = getString(R.string.practices_at_locations).replace("$$", "" + size);
            SpannableString contentExp = new SpannableString(updatedString);
            contentExp.setSpan(new ForegroundColorSpan(
                            ContextCompat.getColor(getActivity(), R.color.tagColor)),
                    13, 13 + size,//hightlight mSearchString
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mDocPracticesLocationCount.setText(contentExp);
        }
        //------------
        if (mClickedDoctorObject.getCategorySpeciality() != null) {
            mPremiumType.setText("" + mClickedDoctorObject.getCategorySpeciality());
            mPremiumType.setVisibility(View.VISIBLE);
        } else {
            mPremiumType.setVisibility(View.INVISIBLE);
        }
        //-------------------
        ArrayAdapter<ClinicData> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.global_item_simple_spinner, mClickedDoctorObject.getClinicDataList());
        mClinicNameSpinner.setAdapter(arrayAdapter);
        mClinicNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedClinicDataObject = mClickedDoctorObject.getClinicDataList().get(position);
                mClinicName.setText("" + mSelectedClinicDataObject.getClinicName());
                mDoctorFees.setText(
                        "" + mSelectedClinicDataObject.getAmount());
                mDoctorDataHelper.getTimeSlotToBookAppointmentWithDoctor("" + mClickedDoctorObject.getDocId(), "" + mSelectedClinicDataObject.getLocationId(), mSelectedTimeSlotDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //---------
    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

        switch (mOldDataTag) {
            case RescribeConstants.TASK_TIME_SLOT_TO_BOOK_APPOINTMENT:
                TimeSlotListBaseModel slotListBaseModel = (TimeSlotListBaseModel) customResponse;
                if (slotListBaseModel != null) {
                    TimeSlotListDataModel selectSlotList = slotListBaseModel.getTimeSlotListDataModel();
                    if (selectSlotList != null) {
                        mSelectSlotToBookAppointmentAdapter = new SelectSlotToBookAppointmentAdapter(getActivity(), selectSlotList.getTimeSlotsInfoList());
                        selectTimeDateExpandableView.setAdapter(mSelectSlotToBookAppointmentAdapter);
                    }
                }
                break;
            case RescribeConstants.TASK_SET_FAVOURITE_DOCTOR:
                CommonBaseModelContainer temp = (CommonBaseModelContainer) customResponse;
                if (temp.getCommonRespose().isSuccess()) {
                    boolean status = mClickedDoctorObject.getFavourite() ? false : true;
                    mClickedDoctorObject.setFavourite(status);
                    BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
                    activity.replaceDoctorListById("" + mClickedDoctorObject.getDocId(), mClickedDoctorObject);
                    if (mClickedDoctorObject.getFavourite()) {
                        mFavorite.setImageResource(R.drawable.fav_icon);
                    } else {
                        mFavorite.setImageResource(R.drawable.result_line_heart_fav);
                    }
                }
                CommonMethods.showToast(getActivity(), temp.getCommonRespose().getStatusMessage());
                break;
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.selectDateTime, R.id.bookAppointmentButton, R.id.viewAllClinicsOnMap, R.id.favorite})
    public void onClickOfView(View view) {

        switch (view.getId()) {
            case R.id.selectDateTime:
                DatePickerDialog datePickerDialog;
                Calendar now = Calendar.getInstance();
                // As of version 2.3.0, `BottomSheetDatePickerDialog` is deprecated.
                datePickerDialog = DatePickerDialog.newInstance(
                        this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setAccentColor(getResources().getColor(R.color.tagColor));
                datePickerDialog.setMinDate(Calendar.getInstance());
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, mSelectedClinicDataObject.getApptScheduleLmtDays());
                datePickerDialog.setMaxDate(calendar);
                datePickerDialog.show(getFragmentManager(), getResources().getString(R.string.select_date_text));
                break;
            case R.id.bookAppointmentButton:
                break;
            case R.id.viewAllClinicsOnMap: // on view-all location clicked
                //-----Show all doc clinic on map, copied from BookAppointFilteredDoctorListFragment.java----
                //this list is sorted for plotting map for each clinic location, the values of clinicName and doctorAddress are set in string here, which are coming from arraylist.
                ArrayList<DoctorList> doctorListByClinics = new ArrayList<>();
                ArrayList<ClinicData> clinicNameList = mClickedDoctorObject.getClinicDataList();
                for (int i = 0; i < clinicNameList.size(); i++) {
                    DoctorList doctorListByClinic = new DoctorList();
                    doctorListByClinic = mClickedDoctorObject;
                    doctorListByClinic.setAddressOfDoctorString(clinicNameList.get(i).getClinicAddress());
                    doctorListByClinic.setNameOfClinicString(clinicNameList.get(i).getClinicName());
                    doctorListByClinics.add(doctorListByClinic);
                }
                Intent intentObjectMap = new Intent(getActivity(), MapActivityPlotNearByDoctor.class);
                intentObjectMap.putParcelableArrayListExtra(getString(R.string.doctor_data), doctorListByClinics);
                intentObjectMap.putExtra(getString(R.string.toolbarTitle), "");
                startActivity(intentObjectMap);
                //--------
                break;
            case R.id.favorite:
                boolean status = mClickedDoctorObject.getFavourite() ? false : true;
                mDoctorDataHelper.setFavouriteDoctor(status, mClickedDoctorObject.getDocId());
                break;
        }
    }

    @Override
    public void updateViewData() {

    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {

        String dateConverted = "" + dayOfMonth;
        if (dayOfMonth < 10) {
            dateConverted = "0" + dayOfMonth;
        }
        mSelectedTimeSlotDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        selectDateTime.setText(CommonMethods.getDayFromDate(RescribeConstants.DATE_PATTERN.DD_MM_YYYY, dateConverted + "-" + (monthOfYear + 1) + "-" + year) + "," + getString(R.string.space) + CommonMethods.getFormattedDate(dateConverted + "-" + (monthOfYear + 1) + "-" + year, RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE_PATTERN.DD_MMM));
        mDoctorDataHelper.getTimeSlotToBookAppointmentWithDoctor("" + mClickedDoctorObject.getDocId(), "" + mSelectedClinicDataObject.getLocationId(), mSelectedTimeSlotDate);
    }
}