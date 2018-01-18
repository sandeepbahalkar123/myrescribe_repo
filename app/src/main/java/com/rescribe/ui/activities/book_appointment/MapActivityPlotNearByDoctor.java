package com.rescribe.ui.activities.book_appointment;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rescribe.R;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.helpers.book_appointment.ServicesCardViewImpl;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.model.book_appointment.doctor_data.ClinicData;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.book_appointment.doctor_data.DoctorServicesModel;
import com.rescribe.model.dashboard_api.DashboardDataModel;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 4/10/17.
 */

public class MapActivityPlotNearByDoctor extends AppCompatActivity implements HelperResponse, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    @BindView(R.id.bookAppointmentBackButton)
    ImageView bookAppointmentBackButton;
    @BindView(R.id.title)
    CustomTextView title;
    @BindView(R.id.locationTextView)
    CustomTextView locationTextView;
    @BindView(R.id.showDocDetailBottomSheet)
    LinearLayout mShowDocDetailBottomSheet;
    @BindView(R.id.showlocation)
    CustomTextView showlocation;
    private GoogleMap mMap;
    Address p1 = null;
    HashMap<String, DashboardDataModel> mLocations = new HashMap<>();
    HashMap<String, String> mUserSelectedLocationInfo;
    TextView mDoctorName;
    TextView mDoctorRating;
    TextView mDoctorReviews;
    ImageView mDirections;
    RatingBar mRatingBar;
    ImageView mMoreInfo;
    private String isActivityOpenedFromFilterViewMapFAB;
    private DoctorDataHelper mDoctorDataHelper;
    //----------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mUserSelectedLocationInfo = RescribeApplication.getUserSelectedLocationInfo();
        ArrayList<DoctorList> mDoctorLists = getIntent().getExtras().getParcelableArrayList(getString(R.string.doctor_data));

        if (mDoctorLists != null) {
            for (DoctorList doctorList : mDoctorLists) {

                if (doctorList.getAddressOfDoctorString().equals("")) {
                    if (!doctorList.getClinicDataList().isEmpty())
                        doctorList.setAddressOfDoctorString(doctorList.getClinicDataList().get(0).getClinicAddress());
                }

                if (mLocations.containsKey(doctorList.getAddressOfDoctorString()))
                    mLocations.get(doctorList.getAddressOfDoctorString()).getDoctorList().add(doctorList);
                else {
                    DashboardDataModel dashboardDataModel = new DashboardDataModel();
                    dashboardDataModel.getDoctorList().add(doctorList);
                    mLocations.put(doctorList.getAddressOfDoctorString(), dashboardDataModel);
                }
            }
        }

        bookAppointmentBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        title.setText(getString(R.string.location));

        //--------
        showlocation.setVisibility(View.GONE);
        isActivityOpenedFromFilterViewMapFAB = getIntent().getStringExtra(getString(R.string.clicked_item_data_type_value));
        if (getString(R.string.filter).equalsIgnoreCase(isActivityOpenedFromFilterViewMapFAB)) {
            locationTextView.setVisibility(View.VISIBLE);
            mDoctorDataHelper = new DoctorDataHelper(this, this);
        } else
            locationTextView.setVisibility(View.GONE);
        //--------

        // showlocation.setText(mUserSelectedLocationInfo.get(getString(R.string.location)));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initializeMarkerBottomSheetViews();
            }
        }, RescribeConstants.TIME_STAMPS.FIVE_FIFTY);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        drawMarkerOnMapReady();

    }

    private void drawMarkerOnMapReady() {
        //------------

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<String, DashboardDataModel> entry : mLocations.entrySet()) {
                    DoctorList doctorList = entry.getValue().getDoctorList().get(0);

                    p1 = getLocationFromAddress(doctorList.getAddressOfDoctorString());
                    if (p1 != null) {
                        LatLng currentLocation = new LatLng(p1.getLatitude(), p1.getLongitude());
                        doctorList.setLatitude(p1.getLatitude());
                        doctorList.setLongitude(p1.getLongitude());

                        View itemView = getLayoutInflater().inflate(R.layout.custom_marker_map, null);
                        TextView ratingText = (TextView) itemView.findViewById(R.id.ratingText);
                        TextView doctorNameText = (TextView) itemView.findViewById(R.id.doctorNameText);
                        if (doctorList.getRating() == 0) {
                            ratingText.setText("");
                        } else {
                            ratingText.setText(String.valueOf(doctorList.getRating()));
                        }
                        doctorNameText.setText(doctorList.getDocName());

                        mMap.addMarker(new MarkerOptions().position(currentLocation).title(entry.getKey()).icon(BitmapDescriptorFactory.fromBitmap(CommonMethods.createDrawableFromView(MapActivityPlotNearByDoctor.this, itemView))));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(p1.getLatitude(), p1.getLongitude()), RescribeConstants.ZOOM_CAMERA_VALUE));
                    }/* else
                        CommonMethods.showToast(MapActivityPlotNearByDoctor.this, getString(R.string.address_not_found));*/
                }
            }
        }, 100);
        //------------
    }

    public Address getLocationFromAddress(String strAddress) {
        Address location = null;
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            location = address.get(0);
            location.getLatitude();
            location.getLongitude();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (mShowDocDetailBottomSheet.getVisibility() == View.GONE) {
            mShowDocDetailBottomSheet.setVisibility(View.VISIBLE);
            Animation slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.slide_up_animation);
            mShowDocDetailBottomSheet.startAnimation(slideUpAnimation);
        }

        DashboardDataModel dashboardDataModel = mLocations.get(marker.getTitle());

        int size = dashboardDataModel.getDoctorList().size();

        if (dashboardDataModel.getIndex() < size)
            init_modal_bottomsheet(dashboardDataModel.getDoctorList().get(dashboardDataModel.getIndex()));

        int index = dashboardDataModel.getIndex() + 1;

        if (size > index)
            dashboardDataModel.setIndex(index);
        else if (size == index)
            dashboardDataModel.setIndex(index);
        else {

            if (mShowDocDetailBottomSheet.getVisibility() == View.VISIBLE) {
                Animation slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_down_animation);
                mShowDocDetailBottomSheet.startAnimation(slideDownAnimation);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mShowDocDetailBottomSheet.setVisibility(View.GONE);
                    }
                }, RescribeConstants.TIME_STAMPS.FIVE_FIFTY);
            }

            dashboardDataModel.setIndex(0);
        }

        return true;
    }

    private void initializeMarkerBottomSheetViews() {

        mDoctorName = (TextView) findViewById(R.id.doctorName);
        mDoctorRating = (TextView) findViewById(R.id.doctorRating);
        mDoctorReviews = (TextView) findViewById(R.id.doctorReviews);
        mDirections = (ImageView) findViewById(R.id.directions);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mMoreInfo = (ImageView) findViewById(R.id.moreInfo);
    }

    // On map marker click display details of doctor on bottom dialog
    public void init_modal_bottomsheet(final DoctorList doctorL) {
        // Map activity direscted to doctor description page
        mMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DoctorList doctorList= (DoctorList) doctorL.clone();
                    Intent intent = new Intent(MapActivityPlotNearByDoctor.this, DoctorDescriptionBaseActivity.class);
                    intent.putExtra(getString(R.string.toolbarTitle),getIntent().getStringExtra(getString(R.string.toolbarTitle)));
                    intent.putExtra(getString(R.string.clicked_item_data), doctorList);
                    ServicesCardViewImpl.setUserSelectedDoctorListDataObject(doctorList);
                    startActivityForResult(intent, RescribeConstants.DOCTOR_DATA_REQUEST_CODE);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

            }
        });
        try {

            if (doctorL.getRating() == 0) {
                mDoctorRating.setVisibility(View.GONE);
                mRatingBar.setVisibility(View.GONE);
            } else {
                mDoctorRating.setVisibility(View.VISIBLE);
                mRatingBar.setVisibility(View.VISIBLE);
                mDoctorRating.setText("" + doctorL.getRating());
                mRatingBar.setRating((float) doctorL.getRating());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        mDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + mUserSelectedLocationInfo.get(getString(R.string.latitude)) + "," + mUserSelectedLocationInfo.get(getString(R.string.longitude)) + "&daddr=" + doctorL.getLatitude() + "," + mLocations.get(doctorL.getLongitude())));
                startActivity(intent);
            }
        });

        //TODO : REVIEWS NEED TO ADDED , NOT IN UPDATED JSON.
        //  mDoctorReviews.setText(getString(R.string.openingbrace) + mDoctorLists.get(Integer.parseInt(marker.getTitle())).getTotalReview() + getString(R.string.closeingbrace));
        mDoctorReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivityPlotNearByDoctor.this, ShowReviewListActivity.class);
                intent.putExtra(getString(R.string.doctorId), doctorL.getDocId());
                startActivity(intent);
            }
        });
        mDoctorName.setText("" + doctorL.getDocName());

    }

    @Override
    public void onBackPressed() {
        if (mShowDocDetailBottomSheet.getVisibility() == View.VISIBLE) {
            Animation slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.slide_down_animation);
            mShowDocDetailBottomSheet.startAnimation(slideDownAnimation);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mShowDocDetailBottomSheet.setVisibility(View.GONE);

                }
            }, RescribeConstants.TIME_STAMPS.FIVE_FIFTY);

        } else {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.locationTextView})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.locationTextView:
                Intent start = new Intent(this, BookAppointFindLocation.class);
                startActivity(start);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getString(R.string.filter).equalsIgnoreCase(isActivityOpenedFromFilterViewMapFAB)) {
            HashMap<String, String> userSelectedLocationInfo = RescribeApplication.getUserSelectedLocationInfo();
            String selectedLocation = userSelectedLocationInfo.get(getString(R.string.location));
            String[] split = selectedLocation.split(",");
            if (split.length == 2) {
                mDoctorDataHelper.doGetDoctorData(split[1], split[0], null);
            } else {
                mDoctorDataHelper.doGetDoctorData("", "", null);
            }
        }

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        BookAppointmentBaseModel receivedBookAppointmentBaseModel = (BookAppointmentBaseModel) customResponse;
        if (receivedBookAppointmentBaseModel != null) {
            DoctorServicesModel mReceivedDoctorServicesModel = receivedBookAppointmentBaseModel.getDoctorServicesModel();
            if (mReceivedDoctorServicesModel != null) {
                ArrayList<DoctorList> doctorList = mReceivedDoctorServicesModel.getDoctorList();
                new ServicesCardViewImpl(this, this).setReceivedDoctorDataList(doctorList);

                //--------
                ArrayList<DoctorList> doctorListByClinics = new ArrayList<>();
                for (DoctorList mainObject :
                        doctorList) {
                    ArrayList<ClinicData> clinicDataList = mainObject.getClinicDataList();
                    for (int i = 0; i < clinicDataList.size(); i++) {
                        DoctorList doctorListByClinic = new DoctorList();
                        doctorListByClinic = mainObject;
                        doctorListByClinic.setAddressOfDoctorString(clinicDataList.get(i).getClinicAddress());
                        doctorListByClinic.setNameOfClinicString(clinicDataList.get(i).getClinicName());
                        doctorListByClinics.add(doctorListByClinic);
                    }
                }

                // mMap.clear();
                drawMarkerOnMapReady();
                //-------------
            }
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
