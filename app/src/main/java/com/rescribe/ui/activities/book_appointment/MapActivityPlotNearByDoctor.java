package com.rescribe.ui.activities.book_appointment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.activities.HomePageActivity;
import com.rescribe.ui.activities.LoginSignUpActivity;
import com.rescribe.ui.activities.SplashScreenActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 4/10/17.
 */

public class MapActivityPlotNearByDoctor extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

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
    ArrayList<DoctorList> mDoctorLists;
    private Intent mIntent;
    HashMap<String, String> mUserSelectedLocationInfo;
    private Context mContext;
    TextView mDoctorName;
    TextView mDoctorRating;
    TextView mKilometers;
    TextView mDoctorReviews;
    ImageView mDirections;
    RatingBar mRatingBar;
    ImageView mMoreInfo;
    //----------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mContext = MapActivityPlotNearByDoctor.this;
        mIntent = getIntent();
        mUserSelectedLocationInfo = DoctorDataHelper.getUserSelectedLocationInfo();
        mContext = MapActivityPlotNearByDoctor.this;
        mDoctorLists = this.getIntent().getParcelableArrayListExtra(getString(R.string.doctor_data));
        bookAppointmentBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        title.setText(mIntent.getStringExtra(getString(R.string.toolbarTitle)));
        showlocation.setVisibility(View.VISIBLE);
        showlocation.setText(mUserSelectedLocationInfo.get(getString(R.string.location)));
        locationTextView.setVisibility(View.GONE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initializeMarkerBottomSheetViews();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        for (int index = 0; index < mDoctorLists.size(); index++) {
            DoctorList doctorList = mDoctorLists.get(index);
            p1 = getLocationFromAddress(doctorList.getDoctorAddress());
            if (p1 != null) {
                LatLng currentLocation = new LatLng(p1.getLatitude(), p1.getLongitude());
                doctorList.setLatitude(p1.getLatitude());
                doctorList.setLongitude(p1.getLongitude());

                View itemView = getLayoutInflater().inflate(R.layout.custom_marker_map, null);
                TextView ratingText = (TextView) itemView.findViewById(R.id.ratingText);
                TextView doctorNameText = (TextView) itemView.findViewById(R.id.doctorNameText);
                ratingText.setText(String.valueOf(doctorList.getRating()));
                doctorNameText.setText(doctorList.getDocName());

                mMap.addMarker(new MarkerOptions().position(currentLocation).title(String.valueOf(index)).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(MapActivityPlotNearByDoctor.this, itemView))));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(p1.getLatitude(), p1.getLongitude()), 14.0f));
            } else
                CommonMethods.showToast(MapActivityPlotNearByDoctor.this, getString(R.string.address_not_found));
        }
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

    //creation of marker on map
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels,
                displayMetrics.heightPixels);

        view.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        view.buildDrawingCache(false);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (mShowDocDetailBottomSheet.getVisibility() == View.GONE) {
            mShowDocDetailBottomSheet.setVisibility(View.VISIBLE);
            Animation slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.slide_up_animation);
            mShowDocDetailBottomSheet.startAnimation(slideUpAnimation);
        }
        init_modal_bottomsheet(marker);

        return true;
    }

    private void initializeMarkerBottomSheetViews() {

        mDoctorName = (TextView) findViewById(R.id.doctorName);
        mDoctorRating = (TextView) findViewById(R.id.doctorRating);
        mKilometers = (TextView) findViewById(R.id.kilometers);
        mDoctorReviews = (TextView) findViewById(R.id.doctorReviews);
        mDirections = (ImageView) findViewById(R.id.directions);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mMoreInfo = (ImageView) findViewById(R.id.moreInfo);
    }

    // On map marker click display details of doctor on bottom dialog
    public void init_modal_bottomsheet(final Marker marker) {
     // Map activity direscted to doctor description page
        mMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DoctorList doctorList = new DoctorList();
                doctorList.setAboutDoctor(mDoctorLists.get(Integer.parseInt(marker.getTitle())).getAboutDoctor());
                doctorList.setAmount(mDoctorLists.get(Integer.parseInt(marker.getTitle())).getAmount());
                doctorList.setAvailableTimeSlots(mDoctorLists.get(Integer.parseInt(marker.getTitle())).getAvailableTimeSlots());
                doctorList.setDegree(mDoctorLists.get(Integer.parseInt(marker.getTitle())).getDegree());
                doctorList.setDistance(mDoctorLists.get(Integer.parseInt(marker.getTitle())).getDistance());
                doctorList.setDocId(mDoctorLists.get(Integer.parseInt(marker.getTitle())).getDocId());
                doctorList.setDocName(mDoctorLists.get(Integer.parseInt(marker.getTitle())).getDocName());
                doctorList.setDoctorAddress(mDoctorLists.get(Integer.parseInt(marker.getTitle())).getDoctorAddress());
                doctorList.setDoctorImageUrl(mDoctorLists.get(Integer.parseInt(marker.getTitle())).getDoctorImageUrl());
                doctorList.setExperience(mDoctorLists.get(Integer.parseInt(marker.getTitle())).getExperience());
                doctorList.setFavourite(mDoctorLists.get(Integer.parseInt(marker.getTitle())).getFavourite());
                doctorList.setWaitingTime(mDoctorLists.get(Integer.parseInt(marker.getTitle())).getWaitingTime());
                doctorList.setTokenNo(mDoctorLists.get(Integer.parseInt(marker.getTitle())).getTokenNo());
                doctorList.setSpeciality(mDoctorLists.get(Integer.parseInt(marker.getTitle())).getSpeciality());
                doctorList.setRecentlyVisited(mDoctorLists.get(Integer.parseInt(marker.getTitle())).getRecentlyVisited());
                doctorList.setOpenToday(mDoctorLists.get(Integer.parseInt(marker.getTitle())).getOpenToday());
                doctorList.setPracticePlaceInfos(mDoctorLists.get(Integer.parseInt(marker.getTitle())).getPracticePlaceInfos());
                doctorList.setLongitude(mDoctorLists.get(Integer.parseInt(marker.getTitle())).getLongitude());
                doctorList.setLatitude(mDoctorLists.get(Integer.parseInt(marker.getTitle())).getLatitude());
                doctorList.setTotalReview(mDoctorLists.get(Integer.parseInt(marker.getTitle())).getTotalReview());
                Intent intent = new Intent(MapActivityPlotNearByDoctor.this, ShowMoreInfoBaseActivity.class);
                intent.putExtra(getString(R.string.toolbarTitle), title.getText().toString());
                intent.putExtra(getString(R.string.doctor_data), doctorList);
                startActivity(intent);
            }
        });
        try {
            mDoctorRating.setText("" + mDoctorLists.get(Integer.parseInt(marker.getTitle())).getRating());
            mRatingBar.setRating(Float.parseFloat(mDoctorLists.get(Integer.parseInt(marker.getTitle())).getRating()));

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        mDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + mUserSelectedLocationInfo.get(getString(R.string.latitude)) + "," + mUserSelectedLocationInfo.get(getString(R.string.longitude)) + "&daddr=" + mDoctorLists.get(Integer.parseInt(marker.getTitle())).getLatitude() + "," + mDoctorLists.get(Integer.parseInt(marker.getTitle())).getLongitude()));
                startActivity(intent);
            }
        });
        mDoctorReviews.setText(getString(R.string.openingbrace) + mDoctorLists.get(Integer.parseInt(marker.getTitle())).getTotalReview() + getString(R.string.closeingbrace));
        mDoctorReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivityPlotNearByDoctor.this, ShowReviewListActivity.class);
                intent.putExtra(getString(R.string.doctorId), String.valueOf(mDoctorLists.get(Integer.parseInt(marker.getTitle())).getDocId()));
                startActivity(intent);

            }
        });
        mDoctorName.setText("" + mDoctorLists.get(Integer.parseInt(marker.getTitle())).getDocName());

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

    // On Map click listener close dialog
    @Override
    public void onMapClick(LatLng latLng) {
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

}
