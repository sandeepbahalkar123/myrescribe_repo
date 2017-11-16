package com.rescribe.ui.activities.book_appointment;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.rescribe.R;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.fragments.book_appointment.RecentVisitDoctorFragment;
import com.rescribe.util.CommonMethods;

import java.io.IOException;
import java.util.HashMap;
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
public class BookAppointDoctorListBaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "BookAppointDoctorListBaseActivity";
    @BindView(R.id.bookAppointmentBackButton)
    ImageView bookAppointmentBackButton;

    CustomTextView mTitleView;
    CustomTextView locationTextView;
    CustomTextView showlocation;

    @BindView(R.id.nav_view)
    FrameLayout mNavView;
    private RecentVisitDoctorFragment mRecentVisitDoctorFragment;
    private int PLACE_PICKER_REQUEST = 1;

    private HashMap<String, String> mComplaintsUserSearchFor = new HashMap<>();

    //-----
    String latitude = "";
    String longitude = "";
    String address;
    //-----

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appoint_doc_base_list);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        //----------------
        new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        //--------
        mTitleView = (CustomTextView) findViewById(R.id.title);
        locationTextView = (CustomTextView) findViewById(R.id.locationTextView);
        locationTextView.setVisibility(View.GONE);
        showlocation = (CustomTextView) findViewById(R.id.showlocation);

        //------
        HashMap<String, String> userSelectedLocationInfo = DoctorDataHelper.getUserSelectedLocationInfo();
        String locationReceived = userSelectedLocationInfo.get(getString(R.string.location));
        if (locationReceived != null) {
            locationTextView.setText("" + locationReceived);
        }
        //-----
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String title = extras.getString(getString(R.string.clicked_item_data));
                mTitleView.setText(title);
                bundle.putString(getString(R.string.title), title);
            }
        }
        //------
        mRecentVisitDoctorFragment = RecentVisitDoctorFragment.newInstance(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.viewContainer, mRecentVisitDoctorFragment).commit();
        //-----------
    }

    @OnClick({R.id.bookAppointmentBackButton, R.id.title, R.id.locationTextView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bookAppointmentBackButton:
                onBackPressed();
                break;
            case R.id.title:
                break;
            case R.id.locationTextView:
                //    BookAppointDoctorListBaseActivityPermissionsDispatcher.callPickPlaceWithCheck(this);
                Intent start = new Intent(this, BookAppointFindLocation.class);
                startActivityForResult(start, PLACE_PICKER_REQUEST);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BookAppointDoctorListBaseActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION})
    public void callPickPlace() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            Intent intentPlace = builder.build(BookAppointDoctorListBaseActivity.this);
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
                address = String.format("%s", place.getAddress());
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
                    //-------
                    String locality = "";
                    //-------
                    if (placename.contains(" ")) {
                        locality = getArea(addresses.get(0));
                    } else {
                        locality = placename;
                    }
                    String city = addresses.get(0).getLocality();

                   /* Address address = addresses.get(0);
                    String addressLine = address.getAddressLine(1);
                    String addressLineArray[] = addressLine.split(",");
                    addressLine = addressLineArray[addressLineArray.length - 1];

                    if (placename.toLowerCase().contains(addressLine)) {
                        locality = addressLine;
                    } else if (addressLine.toLowerCase().contains(placename)) {
                        locality = placename;
                    }*/
                    //-------
                    //DoctorDataHelper.setUserSelectedLocationInfo(BookAppointDoctorListBaseActivity.this, place.getLatLng(), placename + ", " + city);
                    DoctorDataHelper.setUserSelectedLocationInfo(BookAppointDoctorListBaseActivity.this, place.getLatLng(), locality + ", " + city);
                    //-------
                    HashMap<String, String> userSelectedLocationInfo = DoctorDataHelper.getUserSelectedLocationInfo();
                    String s = userSelectedLocationInfo.get(getString(R.string.location));
                    if (s != null) {
                        String[] split = s.split(",");
                        mRecentVisitDoctorFragment.doGetLatestDoctorListOnLocationChange(mComplaintsUserSearchFor);
                        locationTextView.setText("" + city);
                    }
                }
                CommonMethods.Log("Address: ", stBuilder.toString());
            }
        }
    }

    private String getArea(Address obj) {

        if (obj.getThoroughfare() != null)
            return obj.getThoroughfare();
        else if (obj.getSubLocality() != null)
            return obj.getSubLocality();
        else if (obj.getSubAdminArea() != null)
            return obj.getSubAdminArea();
        else if (obj.getLocality() != null)
            return obj.getLocality();
        else if (obj.getAdminArea() != null)
            return obj.getAdminArea();
        else
            return obj.getCountryName();
    }

    //TODO: PENDING
    public DrawerLayout getActivityDrawerLayout() {
        return null;
//        return mDrawerLayout;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
