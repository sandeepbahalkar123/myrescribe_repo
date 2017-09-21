package com.rescribe.ui.activities.book_appointment;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.rescribe.adapters.book_appointment.BookAppointFilteredDocList;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.ui.activities.MapsActivity;
import com.rescribe.util.CommonMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    }
    public void init_modal_bottomsheet() {
        View modalbottomsheet = getActivity().getLayoutInflater().inflate(R.layout.show_bottom_sheet_on_doctors, null);

        dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(true);;
        dialog.setCancelable(true);
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

   /* public void isDataListViewVisible(boolean flag) {
        if (flag) {
            mEmptyListView.setVisibility(View.GONE);
            mDoctorListView.setVisibility(View.VISIBLE);
        } else {
            mEmptyListView.setVisibility(View.VISIBLE);
            mDoctorListView.setVisibility(View.GONE);
        }
    }*/


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
        doctorRating.setText("" + doctorLists.get(Integer.parseInt(marker.getTitle())).getExperience());
        doctorName.setText("" + doctorLists.get(Integer.parseInt(marker.getTitle())).getDocName());

        return infoView;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        init_modal_bottomsheet();
    }

}
