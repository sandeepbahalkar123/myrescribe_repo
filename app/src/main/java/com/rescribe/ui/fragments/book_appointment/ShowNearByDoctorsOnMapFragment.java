package com.rescribe.ui.fragments.book_appointment;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Dimension;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rescribe.R;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jeetal on 20/9/17.
 */
public class ShowNearByDoctorsOnMapFragment extends Fragment implements View.OnClickListener, HelperResponse, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.InfoWindowAdapter {

    Unbinder unbinder;
    private View mRootView;
    public static Bundle args;
    private GoogleMap mMap;
    Address p1 = null;
    MapFragment mapFragment;
    BottomSheetDialog dialog;
    BookAppointmentBaseModel receivedBookAppointmentBaseModel;
    ArrayList<DoctorList> doctorLists = new ArrayList<>();
    float[] distanceResults = new float[1];
    private BookAppointDoctorListBaseActivity activity;

    public ShowNearByDoctorsOnMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.show_doctors_on_map, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        init();
        return mRootView;
    }

    public static ShowNearByDoctorsOnMapFragment newInstance(Bundle b) {
        ShowNearByDoctorsOnMapFragment fragment = new ShowNearByDoctorsOnMapFragment();
        args = b;
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    private void init() {
        BookAppointDoctorListBaseActivity.setToolBarTitle(args.getString(getString(R.string.clicked_item_data)), false);
    }

    public void init_modal_bottomsheet(final Marker marker) {
        View modalbottomsheet = getActivity().getLayoutInflater().inflate(R.layout.show_bottom_sheet_on_doctors, null);
        TextView doctorName = (TextView) modalbottomsheet.findViewById(R.id.doctorName);
        TextView doctorRating = (TextView) modalbottomsheet.findViewById(R.id.doctorRating);
        TextView kilometers = (TextView) modalbottomsheet.findViewById(R.id.kilometers);
        TextView doctorReviews = (TextView) modalbottomsheet.findViewById(R.id.doctorReviews);
        ImageView directions = (ImageView) modalbottomsheet.findViewById(R.id.directions);
        RatingBar ratingBar = (RatingBar) modalbottomsheet.findViewById(R.id.ratingBar);
        ImageView moreInfo = (ImageView) modalbottomsheet.findViewById(R.id.moreInfo);

        // TextView doctorName = (TextView)modalbottomsheet.findViewById(R.id.doctorName);
        dialog = new BottomSheetDialog(getActivity());
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
                args.putParcelable(getString(R.string.clicked_item_data), doctorList);
                BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
                activity.loadFragment(BookAppointDoctorDescriptionFragment.newInstance(args), false);
            }
        });
        ratingBar.setRating(Float.parseFloat(doctorLists.get(Integer.parseInt(marker.getTitle())).getRating()));
        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + args.getString(getString(R.string.latitude)) + "," + args.getString(getString(R.string.longitude)) + "&daddr=" + doctorLists.get(Integer.parseInt(marker.getTitle())).getLatitude() + "," + doctorLists.get(Integer.parseInt(marker.getTitle())).getLongitude()));
                startActivity(intent);
            }
        });
       doctorReviews.setText(getString(R.string.openingbrace) + doctorLists.get(Integer.parseInt(marker.getTitle())).getTotalReview() + getString(R.string.closeingbrace));
        doctorReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setCancelable(true);
                BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.doctorId), String.valueOf(doctorLists.get(Integer.parseInt(marker.getTitle())).getDocId ()));
                bundle.putParcelable(getString(R.string.doctor_data), activity.getReceivedBookAppointmentBaseModel());
                activity.loadFragment(ShowReviewsOnDoctorFragment.newInstance(bundle), false);

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
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
        BookAppointDoctorListBaseActivity activity = (BookAppointDoctorListBaseActivity) getActivity();
        receivedBookAppointmentBaseModel = activity.getReceivedBookAppointmentBaseModel();
        // setDoctorListAdapter(receivedBookAppointmentBaseModel);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
                CommonMethods.showToast(getActivity(), getString(R.string.address_not_found));
        }

    }

    public Address getLocationFromAddress(String strAddress) {
        Address location = null;
        Geocoder coder = new Geocoder(getActivity());
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
        View infoView = getActivity().getLayoutInflater().inflate(R.layout.marker_map_activity, null);
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
