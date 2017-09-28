package com.rescribe.ui.fragments.book_appointment;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rescribe.R;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.ui.activities.MapsActivity;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jeetal on 28/9/17.
 */

public class ShowLocationOfDoctorOnMap extends Fragment implements OnMapReadyCallback {
    Unbinder unbinder;
    private View mRootView;
    public static Bundle args;
    private GoogleMap mMap;
    Address p1 = null;
    MapFragment mapFragment;
    BottomSheetDialog dialog;
    String address;
    BookAppointmentBaseModel receivedBookAppointmentBaseModel;
    ArrayList<DoctorList> doctorLists = new ArrayList<>();
    float[] distanceResults = new float[1];

    public ShowLocationOfDoctorOnMap() {
        // Required empty public constructor
    }
    public static ShowLocationOfDoctorOnMap newInstance(Bundle b) {
        ShowLocationOfDoctorOnMap fragment = new ShowLocationOfDoctorOnMap();
        args = b;
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.show_doctor_position, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        init();
        return mRootView;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       p1 = getLocationFromAddress( args.getString(getString(R.string.address)));
        if (p1 != null) {
            LatLng currentLocation = new LatLng(p1.getLatitude(), p1.getLongitude());
         mMap.addMarker(new MarkerOptions().position(currentLocation).title(address).icon(getMarkerIcon("#04abdf")));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(p1.getLatitude(), p1.getLongitude()), 14.0f));
        }
           // CommonMethods.showToast(MapsActivity.this, getString(R.string.address_not_found));

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
    //Change marker icon
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    private void init() {
        BookAppointDoctorListBaseActivity.setToolBarTitle(args.getString(getString(R.string.toolbarTitle)),false);
    }
}
