package com.rescribe.ui.fragments.book_appointment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.adapters.book_appointment.TimeSlotAdapter;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.activities.book_appointment.MapActivityPlotNearByDoctor;
import com.rescribe.ui.activities.book_appointment.MapActivityShowDoctorLocation;
import com.rescribe.ui.activities.book_appointment.SelectSlotToBookAppointmentBaseActivity;
import com.rescribe.ui.customesViews.CircularImageView;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class BookAppointDoctorDescriptionFragment extends Fragment implements HelperResponse, BookAppointDoctorListBaseActivity.AddUpdateViewDataListener {

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
    @BindView(R.id.allTimingListViewLayout)
    LinearLayout mAllTimingListViewLayout;
    @BindView(R.id.allTimeSlotListView)
    RecyclerView mAllTimeSlotListView;
    @BindView(R.id.locationImage)
    ImageView locationImage;
    @BindView(R.id.addressOfClinic)
    CustomTextView addressOfClinic;
    @BindView(R.id.clinicName)
    CustomTextView clinicName;
    @BindView(R.id.aboutDoctor)
    CustomTextView aboutDoctor;
    @BindView(R.id.bookAppointmentButton)
    AppCompatButton bookAppointmentButton;
    private View mRootView;
    private int mImageSize;
    Unbinder unbinder;
    private DoctorList mClickedDoctorObject;
    public static Bundle args;

    public BookAppointDoctorDescriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.book_appointment_doctor_description_layout, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        init();
        return mRootView;
    }

    public static BookAppointDoctorDescriptionFragment newInstance(Bundle b) {
        BookAppointDoctorDescriptionFragment fragment = new BookAppointDoctorDescriptionFragment();
        args = b;
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    private void init() {
        setColumnNumber(getActivity(), 2);
        BookAppointDoctorListBaseActivity.setToolBarTitle(args.getString(getString(R.string.toolbarTitle)), false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mClickedDoctorObject = (DoctorList) arguments.getParcelable(getString(R.string.clicked_item_data));
            CommonMethods.Log("TAG", " parcelable :" + mClickedDoctorObject.toString());
            setDataInViews();
        }
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
        SpannableString content = new SpannableString(aboutDoctor.getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        aboutDoctor.setText(content);

        clinicName.setText("" + mClickedDoctorObject.getNameOfClinicString());
      /*  addressOfClinic.setText("" + mClickedDoctorObject.getAddressOfDoctor());*/

        mDocRating.setText("" + mClickedDoctorObject.getRating());
        mDoctorName.setText("" + mClickedDoctorObject.getDocName());
        mDoctorSpecialization.setText("" + mClickedDoctorObject.getDegree());
        mAboutDoctorDescription.setText("" + mClickedDoctorObject.getAboutDoctor());
        mDoctorExperience.setText("" + mClickedDoctorObject.getExperience() + getString(R.string.space) + getString(R.string.years_experience));
        mDoctorFees.setText(getString(R.string.fee) + getString(R.string.space) + getString(R.string.rupees) + mClickedDoctorObject.getAmount() + getString(R.string.space) + getString(R.string.slash) + getString(R.string.space) + getString(R.string.session));

        int spanCount = 2; // 3 columns
        int spacing = 30; // 50px
        boolean includeEdge = false;
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mAllTimeSlotListView.setLayoutManager(linearlayoutManager);
        mAllTimeSlotListView.setHasFixedSize(true);
        TimeSlotAdapter t = new TimeSlotAdapter(getActivity(), mClickedDoctorObject.getClinicName());
        mAllTimeSlotListView.setAdapter(t);

        //requestOptions.placeholder(R.drawable.layer_12);
        if (!mClickedDoctorObject.getAddressOfDoctorString().isEmpty()) {
            Glide.with(getActivity())
                    .load("https://maps.googleapis.com/maps/api/staticmap?center=" + mClickedDoctorObject.getAddressOfDoctorString() + "&markers=color:red%7Clabel:C%7C" + mClickedDoctorObject.getAddressOfDoctorString() + "&zoom=12&size=640x250")
                    .into(locationImage);
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

    @OnClick({R.id.locationImage, R.id.bookAppointmentButton, R.id.viewAllClinicsOnMap})
    public void onClickOfView(View view) {

        switch (view.getId()) {
            case R.id.locationImage:
                HashMap<String, String> userSelectedLocationInfo = DoctorDataHelper.getUserSelectedLocationInfo();
                Intent intent = new Intent(getActivity(), MapActivityShowDoctorLocation.class);
                intent.putExtra(getString(R.string.toolbarTitle), args.getString(getString(R.string.toolbarTitle)));
                intent.putExtra(getString(R.string.location), userSelectedLocationInfo.get(getString(R.string.location)));
                intent.putExtra(getString(R.string.address), mClickedDoctorObject.getAddressOfDoctorString());
                startActivity(intent);
                break;
            case R.id.bookAppointmentButton:
                Intent intentObject = new Intent(getActivity(), SelectSlotToBookAppointmentBaseActivity.class);
                intentObject.putExtra(getString(R.string.clicked_item_data), mClickedDoctorObject);
                intentObject.putExtra(getString(R.string.toolbarTitle), args.getString(getString(R.string.toolbarTitle)));
                startActivity(intentObject);
                break;
            case R.id.viewAllClinicsOnMap: // on view-all location clicked
                //-----Show all doc clinic on map, copied from BookAppointFilteredDoctorListFragment.java----
                //this list is sorted for plotting map for each clinic location, the values of clinicName and doctorAddress are set in string here, which are coming from arraylist.
                ArrayList<DoctorList> doctorListByClinics = new ArrayList<>();
                ArrayList<String> clinicNameList = mClickedDoctorObject.getClinicName();
                for (int i = 0; i < clinicNameList.size(); i++) {
                    DoctorList doctorListByClinic = new DoctorList();
                    doctorListByClinic = mClickedDoctorObject;
                    doctorListByClinic.setAddressOfDoctorString(mClickedDoctorObject.getDoctorAddress().get(i));
                    doctorListByClinic.setNameOfClinicString(clinicNameList.get(i));
                    doctorListByClinics.add(doctorListByClinic);
                }
                Intent intentObjectMap = new Intent(getActivity(), MapActivityPlotNearByDoctor.class);
                intentObjectMap.putParcelableArrayListExtra(getString(R.string.doctor_data), doctorListByClinics);
                intentObjectMap.putExtra(getString(R.string.toolbarTitle), "");
                startActivity(intentObjectMap);
                //--------
                break;
        }
    }

    @Override
    public void updateViewData() {

    }
}
