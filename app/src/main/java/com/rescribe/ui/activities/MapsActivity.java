package com.rescribe.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rescribe.R;
import com.rescribe.helpers.book_appointment.ServicesCardViewImpl;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.activities.book_appointment.DoctorDescriptionBaseActivity;
import com.rescribe.ui.activities.book_appointment.MapActivityPlotNearByDoctor;
import com.rescribe.ui.activities.book_appointment.ShowReviewListActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    @BindView(R.id.mapToolbar)
    Toolbar mapToolbar;

    @BindView(R.id.doctorName)
    CustomTextView doctorNameTextView;
    @BindView(R.id.directions)
    ImageView directions;
    @BindView(R.id.showDocDetailBottomSheet)
    LinearLayout showDocDetailBottomSheet;
    private GoogleMap mMap;
    private String mLatitude;
    private String mLongitude;
    Address p1 = null;
    Intent intent;
    String address;
    private Context mContext;
    private String doctorName;
    private HashMap<String, String> mUserSelectedLocationInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_map_activity);
        ButterKnife.bind(this);
        mContext = MapsActivity.this;
        mUserSelectedLocationInfo = RescribeApplication.getUserSelectedLocationInfo();
        intent = getIntent();
        if (intent != null) {
            address = intent.getStringExtra(getString(R.string.address));

            if (intent.getStringExtra(RescribeConstants.DOCTOR_NAME).contains("Dr.")) {
                doctorName = intent.getStringExtra(RescribeConstants.DOCTOR_NAME);
            } else {
                doctorName = "Dr. " + intent.getStringExtra(RescribeConstants.DOCTOR_NAME);
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mapToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.location));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        if (intent != null) {
            mLatitude = intent.getStringExtra(getString(R.string.latitude));
            mLongitude = intent.getStringExtra(getString(R.string.longitude));
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        drawMarkerOnMapReady();
    }

    //get latitude longitude from user address and map it on map
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

    //Change marker icon
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

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

    private void drawMarkerOnMapReady() {
        //------------

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                p1 = getLocationFromAddress(address);
                if (p1 != null) {
                    LatLng currentLocation = new LatLng(p1.getLatitude(), p1.getLongitude());

                    View itemView = getLayoutInflater().inflate(R.layout.custom_marker_map, null);
                    TextView ratingText = (TextView) itemView.findViewById(R.id.ratingText);
                    TextView doctorNameText = (TextView) itemView.findViewById(R.id.doctorNameText);
                    LinearLayout markerIconLayout = (LinearLayout) itemView.findViewById(R.id.markerIconLayout);
                    markerIconLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.appointment_locator));
                    doctorNameText.setTextColor(ContextCompat.getColor(mContext, R.color.tagColor));
                    doctorNameText.setText(doctorName);
                    mMap.addMarker(new MarkerOptions().position(currentLocation).title(address).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(MapsActivity.this, itemView))));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(p1.getLatitude(), p1.getLongitude()), RescribeConstants.ZOOM_CAMERA_VALUE));
                } else
                    CommonMethods.showToast(MapsActivity.this, getString(R.string.address_not_found));

            }
        }, 100);
        //------------
    }

    @Override
    public void onBackPressed() {
        if (showDocDetailBottomSheet.getVisibility() == View.VISIBLE) {
            Animation slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.slide_down_animation);
            showDocDetailBottomSheet.startAnimation(slideDownAnimation);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showDocDetailBottomSheet.setVisibility(View.GONE);

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
        showDocDetailBottomSheet.startAnimation(slideDownAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showDocDetailBottomSheet.setVisibility(View.GONE);
            }
        }, RescribeConstants.TIME_STAMPS.FIVE_FIFTY);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (showDocDetailBottomSheet.getVisibility() == View.GONE) {
            showDocDetailBottomSheet.setVisibility(View.VISIBLE);
            Animation slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.slide_up_animation);
            showDocDetailBottomSheet.startAnimation(slideUpAnimation);
        }
        init_modal_bottomsheet(marker);

        return true;
    }

    public void init_modal_bottomsheet(final Marker marker) {

        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + mUserSelectedLocationInfo.get(getString(R.string.latitude)) + "," + mUserSelectedLocationInfo.get(getString(R.string.longitude)) + "&daddr=" + p1.getLatitude() + "," + p1.getLongitude()));
                startActivity(intent);
            }
        });


        doctorNameTextView.setText(doctorName);

    }
}
