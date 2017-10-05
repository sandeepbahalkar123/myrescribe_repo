package com.rescribe.ui.activities.book_appointment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rescribe.R;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.fragments.book_appointment.BookAppointDoctorDescriptionFragment;
import com.rescribe.ui.fragments.book_appointment.ShowReviewsOnDoctorFragment;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 4/10/17.
 */

public class MapActivityPlotNearByDoctor extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.InfoWindowAdapter {

    @BindView(R.id.bookAppointmentBackButton)
    ImageView bookAppointmentBackButton;
    @BindView(R.id.title)
    CustomTextView title;
    @BindView(R.id.locationTextView)
    CustomTextView locationTextView;
    @BindView(R.id.showlocation)
    CustomTextView showlocation;
    private GoogleMap mMap;
    Address p1 = null;
    MapFragment mapFragment;
    BottomSheetDialog dialog;
    Bundle mBundle = new Bundle();
    BookAppointmentBaseModel receivedBookAppointmentBaseModel;
    ArrayList<DoctorList> doctorLists;
    private Intent intent;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        intent = getIntent();
        mContext = MapActivityPlotNearByDoctor.this;
        doctorLists = this.getIntent().getParcelableArrayListExtra(getString(R.string.doctor_data));
        bookAppointmentBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        title.setText(intent.getStringExtra(getString(R.string.toolbarTitle)));
        showlocation.setVisibility(View.VISIBLE);
        locationTextView.setVisibility(View.GONE);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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


    public void init_modal_bottomsheet(final Marker marker) {
        View modalbottomsheet = getLayoutInflater().inflate(R.layout.show_bottom_sheet_on_doctors, null);
        TextView doctorName = (TextView) modalbottomsheet.findViewById(R.id.doctorName);
        TextView doctorRating = (TextView) modalbottomsheet.findViewById(R.id.doctorRating);
        TextView kilometers = (TextView) modalbottomsheet.findViewById(R.id.kilometers);
        TextView doctorReviews = (TextView) modalbottomsheet.findViewById(R.id.doctorReviews);
        ImageView directions = (ImageView) modalbottomsheet.findViewById(R.id.directions);
        RatingBar ratingBar = (RatingBar) modalbottomsheet.findViewById(R.id.ratingBar);
        ImageView moreInfo = (ImageView) modalbottomsheet.findViewById(R.id.moreInfo);

        // TextView doctorName = (TextView)modalbottomsheet.findViewById(R.id.doctorName);
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DoctorList doctorList = new DoctorList();
                doctorList.setAboutDoctor(doctorLists.get(Integer.parseInt(marker.getTitle())).getAboutDoctor());
                doctorList.setAmount(doctorLists.get(Integer.parseInt(marker.getTitle())).getAmount());
                doctorList.setAvailableTimeSlots(doctorLists.get(Integer.parseInt(marker.getTitle())).getAvailableTimeSlots());
                doctorList.setDegree(doctorLists.get(Integer.parseInt(marker.getTitle())).getDegree());
                doctorList.setDistance(doctorLists.get(Integer.parseInt(marker.getTitle())).getDistance());
                doctorList.setDocId(doctorLists.get(Integer.parseInt(marker.getTitle())).getDocId());
                doctorList.setDocName(doctorLists.get(Integer.parseInt(marker.getTitle())).getDocName());
                doctorList.setDoctorAddress(doctorLists.get(Integer.parseInt(marker.getTitle())).getDoctorAddress());
                doctorList.setDoctorImageUrl(doctorLists.get(Integer.parseInt(marker.getTitle())).getDoctorImageUrl());
                doctorList.setExperience(doctorLists.get(Integer.parseInt(marker.getTitle())).getExperience());
                doctorList.setFavourite(doctorLists.get(Integer.parseInt(marker.getTitle())).getFavourite());
                doctorList.setWaitingTime(doctorLists.get(Integer.parseInt(marker.getTitle())).getWaitingTime());
                doctorList.setTokenNo(doctorLists.get(Integer.parseInt(marker.getTitle())).getTokenNo());
                doctorList.setSpeciality(doctorLists.get(Integer.parseInt(marker.getTitle())).getSpeciality());
                doctorList.setRecentlyVisited(doctorLists.get(Integer.parseInt(marker.getTitle())).getRecentlyVisited());
                doctorList.setOpenToday(doctorLists.get(Integer.parseInt(marker.getTitle())).getOpenToday());
                doctorList.setMorePracticePlaces(doctorLists.get(Integer.parseInt(marker.getTitle())).getMorePracticePlaces());
                doctorList.setLongitude(doctorLists.get(Integer.parseInt(marker.getTitle())).getLongitude());
                doctorList.setLatitude(doctorLists.get(Integer.parseInt(marker.getTitle())).getLatitude());
                doctorList.setTotalReview(doctorLists.get(Integer.parseInt(marker.getTitle())).getTotalReview());
             /*   doctorList.setReviewList(doctorLists.get(Integer.parseInt(marker.getTitle())).getReviewList());*/
               /* args.putParcelable(getString(R.string.clicked_item_data), doctorList);
                BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
                activity.loadFragment(BookAppointDoctorDescriptionFragment.newInstance(args), false);*/
            }
        });
        ratingBar.setRating(Float.parseFloat(doctorLists.get(Integer.parseInt(marker.getTitle())).getRating()));
        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookAppointDoctorListBaseActivity activity = new BookAppointDoctorListBaseActivity();
                LatLng userSelectedLocationLatLng = activity.getUserSelectedLocationLatLng();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + userSelectedLocationLatLng.latitude + "," + userSelectedLocationLatLng.longitude + "&daddr=" + doctorLists.get(Integer.parseInt(marker.getTitle())).getLatitude() + "," + doctorLists.get(Integer.parseInt(marker.getTitle())).getLongitude()));
                startActivity(intent);
            }
        });
        doctorReviews.setText(getString(R.string.openingbrace) + doctorLists.get(Integer.parseInt(marker.getTitle())).getTotalReview() + getString(R.string.closeingbrace));
        doctorReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* dialog.setCancelable(true);
                BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.doctorId), String.valueOf(doctorLists.get(Integer.parseInt(marker.getTitle())).getDocId ()));
                bundle.putParcelable(getString(R.string.doctor_data), activity.getReceivedBookAppointmentBaseModel());
                activity.loadFragment(ShowReviewsOnDoctorFragment.newInstance(bundle), false);*/

            }
        });
        doctorRating.setText("" + doctorLists.get(Integer.parseInt(marker.getTitle())).getRating());
        doctorName.setText("" + doctorLists.get(Integer.parseInt(marker.getTitle())).getDocName());
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.show();

       /* btn_cancel = (Button) modalbottomsheet.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);*/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);
        mMap.setInfoWindowAdapter(this);
        doctorLists = receivedBookAppointmentBaseModel.getDoctorServicesModel().getDoctorList();
        for (int index = 0; index < doctorLists.size(); index++) {
            DoctorList doctorList = doctorLists.get(index);
            p1 = getLocationFromAddress(doctorList.getDoctorAddress());
            if (p1 != null) {
                LatLng currentLocation = new LatLng(p1.getLatitude(), p1.getLongitude());
                doctorLists.get(index).setLatitude(p1.getLatitude());
                doctorLists.get(index).setLongitude(p1.getLongitude());
              /*  IconGenerator iconFactory = new IconGenerator(this);
                iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
                options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(mLastUpdateTime)));
                options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());*/
                Marker marker = mMap.addMarker(new MarkerOptions().position(currentLocation).title(String.valueOf(index)).icon(getMarkerIcon("#04abdf")));
                mMap.addMarker(new MarkerOptions().position(currentLocation).title(String.valueOf(index)).icon(getMarkerIcon("#04abdf")));
                marker.showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(p1.getLatitude(), p1.getLongitude()), 14.0f));
            } else
                CommonMethods.showToast(this, getString(R.string.address_not_found));
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

    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return prepareInfoView(marker);
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private View prepareInfoView(Marker marker) {
        View infoView = getLayoutInflater().inflate(R.layout.marker_map_activity, null);
        TextView doctorName = (TextView) infoView.findViewById(R.id.doctorName);
        TextView doctorRating = (TextView) infoView.findViewById(R.id.doctorRating);
        doctorRating.setText("" + doctorLists.get(Integer.parseInt(marker.getTitle())).getRating());
        doctorName.setText("" + doctorLists.get(Integer.parseInt(marker.getTitle())).getDocName());

        return infoView;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        init_modal_bottomsheet(marker);
    }


}
