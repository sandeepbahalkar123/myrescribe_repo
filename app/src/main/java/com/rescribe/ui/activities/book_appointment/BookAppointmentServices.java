package com.rescribe.ui.activities.book_appointment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.rescribe.R;
import com.rescribe.adapters.book_appointment.ServicesAdapter;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.ServicesList;
import com.rescribe.model.book_appointment.ServicesModel;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.GoogleSettingsApi;
import com.rescribe.util.RescribeConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.rescribe.util.RescribeConstants.DOCTOR_DATA;
import static com.rescribe.util.RescribeConstants.DOCTOR_DATA_REQUEST_CODE;

/**
 * Created by jeetal on 15/9/17.
 */

public class BookAppointmentServices extends AppCompatActivity implements HelperResponse, ServicesAdapter.OnServicesClickListener,  GoogleSettingsApi.LocationSettings {
    @BindView(R.id.bookAppointmentToolbar)
    ImageView mBookAppointmentToolbar;
    @BindView(R.id.title)
    CustomTextView title;
    @BindView(R.id.locationTextView)
    CustomTextView locationTextView;
    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
    ServicesAdapter mServicesAdapter;
    private Context mContext;
    private int PLACE_PICKER_REQUEST = 10;
    String latitude = "";
    String longitude = "";
    String address;
    private DoctorDataHelper mDoctorDataHelper;
    private ArrayList<DoctorList> doctorLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment_services);
        ButterKnife.bind(this);
        title.setText(getString(R.string.services));
        locationTextView.setVisibility(View.GONE);
       // locationTextView.setText(getString(R.string.location));
        initialize();
    }

    private void initialize() {
        mContext = BookAppointmentServices.this;
        mDoctorDataHelper = new DoctorDataHelper(this, this);
        mDoctorDataHelper.doGetServices();
    }

  /*  public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(BookAppointmentServices.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);

            if (!addresses.isEmpty()) {
                Address obj = addresses.get(0);

                System.out.println("obj.getThoroughfare()" + obj.getThoroughfare());
                System.out.println("obj.getSubLocality()" + obj.getSubLocality());
                System.out.println("obj.getSubAdminArea()" + obj.getSubAdminArea());
                System.out.println("obj.getLocality()" + obj.getLocality());
                System.out.println("obj.getAdminArea()" + obj.getAdminArea());
                System.out.println("obj.getCountryName()" + obj.getCountryName());


            } else {
                Toast.makeText(this, "Address not found.", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }*/


    @Override
    protected void onResume() {
        super.onResume();
        HashMap<String, String> userSelectedLocationInfo = RescribeApplication.getUserSelectedLocationInfo();
        if (userSelectedLocationInfo.get(getString(R.string.location)) == null) {
            //locationTextView.setText(getString(R.string.location));
        } else {
           // locationTextView.setText("" + userSelectedLocationInfo.get(getString(R.string.location)));
        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        ServicesModel servicesModel = (ServicesModel) customResponse;
        if (servicesModel.getServicesData() == null) {
            listView.setVisibility(View.GONE);
            emptyListView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            emptyListView.setVisibility(View.GONE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            listView.setLayoutManager(layoutManager);
            listView.setHasFixedSize(true);
            mServicesAdapter = new ServicesAdapter(mContext, servicesModel.getServicesData().getServicesList(), this);
            listView.setAdapter(mServicesAdapter);
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
        listView.setVisibility(View.GONE);
        emptyListView.setVisibility(View.VISIBLE);

    }

    @OnClick({R.id.bookAppointmentToolbar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bookAppointmentToolbar:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (doctorLists != null) {
            Intent intent = new Intent();
            intent.putExtra(DOCTOR_DATA, doctorLists);
            setResult(DOCTOR_DATA_REQUEST_CODE, intent);
        }

        super.onBackPressed();

    }


    @Override
    public void setOnClickOfServices(ServicesList servicesObject) {


        //TODO : AADED FOR DEVELOPMENT, REMOVE IT IN PRODUCTION.
        //---------
       /* DoctorDataHelper.setUserSelectedLocationInfo(mContext, new LatLng(18.5074, 73.8077), "kothrud,Pune");*/
       // locationTextView.setText("kothrud,Pune");
        //---------

        /*if (locationTextView.getText().toString().equals(getString(R.string.location))) {
            Toast.makeText(mContext, getString(R.string.please_select_location), Toast.LENGTH_SHORT).show();
        } else {*/

            // TODO, THIS IS ADDED FOR NOW, OPEN ONLY IF clicked value == DOCTOR
            if (servicesObject.getServiceName().equalsIgnoreCase(getString(R.string.doctorss))) {
                Intent intent = new Intent(BookAppointmentServices.this, BookAppointDoctorListBaseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.location_address), address);
                bundle.putString(getString(R.string.latitude), latitude);
                bundle.putString(getString(R.string.longitude), longitude);
                bundle.putString(getString(R.string.location), locationTextView.getText().toString());
                bundle.putString(getString(R.string.clicked_item_data), servicesObject.getServiceName());
                intent.putExtras(bundle);
                startActivityForResult(intent, DOCTOR_DATA_REQUEST_CODE);
        }
    }

    @Override
    public void gpsStatus() {
       // BookAppointmentServicesPermissionsDispatcher.callPickPlaceWithCheck(this);
    }
}
