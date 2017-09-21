package com.rescribe.ui.fragments.book_appointment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.adapters.book_appointment.TimeSlotAdapter;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import droidninja.filepicker.utils.GridSpacingItemDecoration;


public class BookAppointDoctorDescriptionFragment extends Fragment implements HelperResponse {


    @BindView(R.id.profileImage)
    CircularImageView mProfileImage;
    @BindView(R.id.docRating)
    CustomTextView mDocRating;
    @BindView(R.id.doctorName)
    CustomTextView mDoctorName;
    @BindView(R.id.doctorSpecialization)
    CustomTextView mDoctorSpecialization;
    @BindView(R.id.aboutDoctorDescription)
    CustomTextView mAboutDoctorDescription;
    @BindView(R.id.doctorExperience)
    CustomTextView mDoctorExperience;
    @BindView(R.id.doctorFees)
    CustomTextView mDoctorFees;
    @BindView(R.id.doctorPractices)
    CustomTextView mDoctorPractices;
    @BindView(R.id.openingTime)
    CustomTextView mOpeningTime;
    @BindView(R.id.showAllTimeSlotListView)
    CustomTextView mShowAllTimeSlotListView;
    @BindView(R.id.allTimingListViewLayout)
    LinearLayout mAllTimingListViewLayout;
    @BindView(R.id.openingTimeLayout)
    LinearLayout mOpeningTimeLayout;
    @BindView(R.id.allTimeSlotListView)
    RecyclerView mAllTimeSlotListView;
    @BindView(R.id.hideAllTimeSlotListView)
    CustomTextView mHideAllTimeSlotListView;
    Unbinder unbinder;
    private View mRootView;
    private DoctorList mClickedDoctorObject;

    public BookAppointDoctorDescriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.book_appointment_doctor_description, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        init();
        return mRootView;
    }

    public static BookAppointDoctorDescriptionFragment newInstance(Bundle b) {
        BookAppointDoctorDescriptionFragment fragment = new BookAppointDoctorDescriptionFragment();
        Bundle args = b;
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    private void init() {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mClickedDoctorObject = (DoctorList) arguments.getParcelable(getString(R.string.clicked_item_data));
            CommonMethods.Log("TAG", " parcelable :" + mClickedDoctorObject.toString());
            setDataInViews();
        }
    }

    private void setDataInViews() {
        mDocRating.setText("" + mClickedDoctorObject.getRating());
        mDoctorName.setText("" + mClickedDoctorObject.getDocName());
        mDoctorSpecialization.setText("" + mClickedDoctorObject.getSpeciality());
        mAboutDoctorDescription.setText("" + mClickedDoctorObject.getAboutDoctor());
        mDoctorExperience.setText("" + mClickedDoctorObject.getExperience());
        mDoctorFees.setText("" + mClickedDoctorObject.getAmount());
        List<String> morePracticePlaces = mClickedDoctorObject.getMorePracticePlaces();
        StringBuilder builder = new StringBuilder();
        for (String s :
                morePracticePlaces) {
            builder.append(s + "\n");
        }
        mDoctorPractices.setText("" + builder.toString());
        mOpeningTime.setText("" + mClickedDoctorObject.getOpenToday());
        if (mClickedDoctorObject.getAvailableTimeSlots().size() > 0) {
            mShowAllTimeSlotListView.setVisibility(View.VISIBLE);

            int spanCount = 5; // 3 columns
            int spacing = 30; // 50px
            boolean includeEdge = false;
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
            mAllTimeSlotListView.setLayoutManager(layoutManager);
            mAllTimeSlotListView.setItemAnimator(new DefaultItemAnimator());
            mAllTimeSlotListView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
            TimeSlotAdapter t = new TimeSlotAdapter(getActivity(), mClickedDoctorObject.getAvailableTimeSlots());
            mAllTimeSlotListView.setAdapter(t);
        }


    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

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

    @OnClick({R.id.showAllTimeSlotListView, R.id.hideAllTimeSlotListView})
    public void onClickOfView(View view) {

        switch (view.getId()) {
            case R.id.hideAllTimeSlotListView:
                mAllTimingListViewLayout.setVisibility(View.GONE);
                mOpeningTimeLayout.setVisibility(View.VISIBLE);
                if (mClickedDoctorObject.getAvailableTimeSlots().size() > 0) {
                    mShowAllTimeSlotListView.setVisibility(View.VISIBLE);
                } else {
                    mShowAllTimeSlotListView.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.showAllTimeSlotListView:
                mAllTimingListViewLayout.setVisibility(View.VISIBLE);
                mOpeningTimeLayout.setVisibility(View.GONE);
        }
    }
}
