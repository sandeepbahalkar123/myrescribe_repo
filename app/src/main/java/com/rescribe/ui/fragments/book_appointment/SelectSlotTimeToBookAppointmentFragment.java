package com.rescribe.ui.fragments.book_appointment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;
import com.rescribe.R;
import com.rescribe.adapters.book_appointment.SelectSlotToBookAppointmentAdapter;
import com.rescribe.adapters.book_appointment.TimeSlotAdapter;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.book_appointment.select_slot_book_appointment.SelectSlotList;
import com.rescribe.model.book_appointment.select_slot_book_appointment.SlotListBaseModel;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by jeetal on 31/10/17.
 */

public class SelectSlotTimeToBookAppointmentFragment extends Fragment implements HelperResponse, BookAppointDoctorListBaseActivity.AddUpdateViewDataListener, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.profileImage)
    CircularImageView profileImage;
    @BindView(R.id.docRating)
    CustomTextView docRating;
    @BindView(R.id.clinicName)
    CustomTextView clinicName;
    @BindView(R.id.doctorName)
    CustomTextView doctorName;
    @BindView(R.id.doctorSpecialization)
    CustomTextView doctorSpecialization;
    @BindView(R.id.leftArrow)
    ImageView leftArrow;
    @BindView(R.id.rightArrow)
    ImageView rightArrow;
    @BindView(R.id.selectDateTime)
    CustomTextView selectDateTime;
    @BindView(R.id.selectTimeDateExpandableView)
    ExpandableListView selectTimeDateExpandableView;
    private View mRootView;
    private int mImageSize;
    Unbinder unbinder;
    private DatePickerDialog datePickerDialog;
    private DoctorDataHelper mDoctorDataHelper;
    private DoctorList mClickedDoctorObject;
    public static Bundle args;
    private Context mContext;
    private int mLastExpandedPosition = -1;
    private SelectSlotToBookAppointmentAdapter mSelectSlotToBookAppointmentAdapter;

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
        // setColumnNumber(getActivity(), 2);
   //     BookAppointDoctorListBaseActivity.setToolBarTitle(args.getString(getString(R.string.toolbarTitle)), false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mClickedDoctorObject = (DoctorList) arguments.getParcelable(getString(R.string.clicked_item_data));
            setDataInViews();
        }
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
        calendar.add(Calendar.DATE, 10);
        datePickerDialog.setMaxDate(calendar);
    }

    private void setColumnNumber(Context context, int columnNum) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        mImageSize = (widthPixels / columnNum) - CommonMethods.convertDpToPixel(30);
    }

    private void setDataInViews() {
        mDoctorDataHelper = new DoctorDataHelper(getActivity(), this);
        mDoctorDataHelper.getSlotTimingToBookAppointment();
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        requestOptions.override(mImageSize, mImageSize);
        requestOptions.placeholder(R.drawable.layer_12);
        Glide.with(getActivity())
                .load(mClickedDoctorObject.getDoctorImageUrl())
                .apply(requestOptions).thumbnail(0.5f)
                .into(profileImage);
        docRating.setText("" + mClickedDoctorObject.getRating());
        doctorName.setText("" + mClickedDoctorObject.getDocName());
        doctorSpecialization.setText("" + mClickedDoctorObject.getDegree());

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (customResponse != null) {
            SlotListBaseModel slotListBaseModel = (SlotListBaseModel) customResponse;
            SelectSlotList selectSlotList = slotListBaseModel.getSelectSlotList();
            mSelectSlotToBookAppointmentAdapter = new SelectSlotToBookAppointmentAdapter(getActivity(), selectSlotList.getSlotList());
            selectTimeDateExpandableView.setAdapter(mSelectSlotToBookAppointmentAdapter);
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

    @OnClick({R.id.selectDateTime, R.id.bookAppointmentButton})
    public void onClickOfView(View view) {

        switch (view.getId()) {
            case R.id.selectDateTime:
                datePickerDialog.show(getFragmentManager(), getResources().getString(R.string.select_date_text));
                break;
            case R.id.bookAppointmentButton:

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
        selectDateTime.setText(CommonMethods.getDayFromDate(RescribeConstants.DATE_PATTERN.DD_MM_YYYY, dateConverted + "-" + (monthOfYear + 1) + "-" + year) + "," + getString(R.string.space) + CommonMethods.getFormattedDate(dateConverted + "-" + (monthOfYear + 1) + "-" + year, RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE_PATTERN.DD_MMM));
    }
}