package com.rescribe.ui.activities.book_appointment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.rescribe.R;
import com.rescribe.adapters.book_appointment.ServicesAdapter;
import com.rescribe.helpers.book_appointment.ServicesHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.ServicesList;
import com.rescribe.model.book_appointment.ServicesModel;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by jeetal on 15/9/17.
 */

@RuntimePermissions
public class BookAppointmentServices extends AppCompatActivity implements HelperResponse, GoogleApiClient.OnConnectionFailedListener, ServicesAdapter.OnServicesClickListener {
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
    ServicesHelper mServicesHelper;
    private Context mContext;
    private int PLACE_PICKER_REQUEST = 1;
    String latitude = "";
    String longitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment_services);
        ButterKnife.bind(this);
        title.setText(getString(R.string.services));
        initialize();
    }

    private void initialize() {
        new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        mContext = BookAppointmentServices.this;
        mServicesHelper = new ServicesHelper(this, this);
        mServicesHelper.doGetServices();


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

    @OnClick({R.id.bookAppointmentToolbar, R.id.locationTextView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bookAppointmentToolbar:
                onBackPressed();
                break;
            case R.id.locationTextView:
                LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
                boolean enabled = service
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (!enabled) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                } else {
                    BookAppointmentServicesPermissionsDispatcher.callPickPlaceWithCheck(this);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BookAppointmentServicesPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION})
    public void callPickPlace() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            Intent intentPlace = builder.build(BookAppointmentServices.this);
            startActivityForResult(intentPlace, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                latitude = String.valueOf(place.getLatLng().latitude);
                longitude = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                stBuilder.append("Name: ");
                stBuilder.append(placename);
                stBuilder.append("\n");
                stBuilder.append("Latitude: ");
                stBuilder.append(latitude);
                stBuilder.append("\n");
                stBuilder.append("Logitude: ");
                stBuilder.append(longitude);
                stBuilder.append("\n");
                stBuilder.append("Address: ");
                stBuilder.append(address);
                Geocoder gcd = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses != null && addresses.size() > 0) {
                    String locality = addresses.get(0).getLocality();
                    locationTextView.setText(locality);
                }
                CommonMethods.Log("Address: ", stBuilder.toString());

            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void setOnClickOfServices(ServicesList servicesObject) {

        if (locationTextView.getText().toString().equals(getString(R.string.location))) {
            Toast.makeText(mContext, getString(R.string.please_select_location), Toast.LENGTH_SHORT).show();
        } else {

            // TODO, THIS IS ADDED FOR NOW, OPEN ONLY IF clicked value == DOCTOR
            if (servicesObject.getServiceName().equalsIgnoreCase(getString(R.string.doctor))) {
                Intent intent = new Intent(BookAppointmentServices.this, BookAppointDoctorListBaseActivity.class);
                intent.putExtra(getString(R.string.latitude), latitude);
                intent.putExtra(getString(R.string.longitude), longitude);
                intent.putExtra(getString(R.string.location), locationTextView.getText().toString());
                intent.putExtra(getString(R.string.clicked_item_data), servicesObject.getServiceName());
                startActivity(intent);
            }
        }
    }
}
