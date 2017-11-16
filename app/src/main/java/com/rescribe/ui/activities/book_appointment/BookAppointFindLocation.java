package com.rescribe.ui.activities.book_appointment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.rescribe.R;
import com.rescribe.adapters.book_appointment.RecentPlacesAdapter;
import com.rescribe.adapters.book_appointment.ShowPopularPlacesAdapter;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.search_doctors.RecentVisitedBaseModel;
import com.rescribe.model.filter.DoctorData;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.LocationUtil.PermissionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.utils.GridSpacingItemDecoration;


public class BookAppointFindLocation extends AppCompatActivity implements PlaceSelectionListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback,
        PermissionUtils.PermissionResultCallback ,HelperResponse{

    public static final String TAG = "BookAppointFindLocation";
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 10;
    @BindView(R.id.bookAppointmentToolbar)
    ImageView bookAppointmentToolbar;
    @BindView(R.id.title)
    CustomTextView title;
    @BindView(R.id.detectLocation)
    CustomTextView detectLocation;
    @BindView(R.id.findLocation)
    CustomTextView findLocation;
    @BindView(R.id.popularPlacesRecyclerView)
    RecyclerView popularPlacesRecyclerView;
    @BindView(R.id.recentlyVisitedRecyclerView)
    RecyclerView recentlyVisitedRecyclerView;
    private Context mContext;
    private final static int PLAY_SERVICES_REQUEST = 1000;
    private final static int REQUEST_CHECK_SETTINGS = 2000;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    double latitude;
    double longitude;
    ArrayList<String> permissions = new ArrayList<>();
    PermissionUtils permissionUtils;
    boolean isPermissionGranted;
    DoctorDataHelper mDoctorDataHelper;
    private ShowPopularPlacesAdapter mShowPopularPlacesAdapter;
    private RecentPlacesAdapter mRecentPlacesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appoint_select_location);
        ButterKnife.bind(this);
        mContext = BookAppointFindLocation.this;
        init();

        permissionUtils = new PermissionUtils(BookAppointFindLocation.this);

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionUtils.check_permission(permissions, "Need GPS permission for getting your location", 1);

        findViewById(R.id.bookAppointmentToolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }
        /*// Retrieve the PlaceAutocompleteFragment.
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        autocompleteFragment.setOnPlaceSelectedListener(this);*/
    }

    private void init() {
        mDoctorDataHelper = new DoctorDataHelper(this,this);
        mDoctorDataHelper.doGetRecentlyVisitedDoctorPlacesData();

    }

    /**
     * Callback invoked when a place has been selected from the PlaceAutocompleteFragment.
     */
    @Override
    public void onPlaceSelected(Place place) {
        CommonMethods.Log(TAG, "Place Selected: " + place.getName());

        // Format the returned place's details and display them in the TextView.
        CommonMethods.showToast(this, "" + formatPlaceDetails(getResources(), place.getName(), place.getId(),
                place.getAddress(), place.getPhoneNumber(), place.getWebsiteUri()));

        CharSequence attributions = place.getAttributions();
        if (!TextUtils.isEmpty(attributions)) {
            CommonMethods.showToast(this, "" + Html.fromHtml(attributions.toString()));
        } else {
            CommonMethods.showToast(this, "");
        }
    }

    /**
     * Callback invoked when PlaceAutocompleteFragment encounters an error.
     */
    @Override
    public void onError(Status status) {
        CommonMethods.Log(TAG, "onError: Status = " + status.toString());

        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Helper method to format information about a place nicely.
     */
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        CommonMethods.Log(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

    public void callPlaceAutocompleteActivityIntent(View v) {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
//PLACE_AUTOCOMPLETE_REQUEST_CODE is integer for request code
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        super.onActivityResult(requestCode, resultCode, data);
        //autocompleteFragment.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                CommonMethods.Log(TAG, "Place:" + place.toString());
                setResult(Activity.RESULT_OK, data);
                finish();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                CommonMethods.Log(TAG, status.getStatusMessage());
                setResult(Activity.RESULT_CANCELED, data);
                finish();
            } else if (resultCode == RESULT_CANCELED) {
                setResult(Activity.RESULT_CANCELED, data);
                finish();
            }
        } else if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                getLocation();
            } else if (requestCode == RESULT_CANCELED) {

            }
        }
    }

    @OnClick({R.id.bookAppointmentToolbar, R.id.detectLocation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bookAppointmentToolbar:
                break;
            case R.id.detectLocation:
                getLocation();

                if (mLastLocation != null) {
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();
                    detectLocation.setText("" + getAddress(BookAppointFindLocation.this, latitude, longitude));

                } else {


                    // showToast("Couldn't get the location. Make sure location is enabled on the device");
                }

                break;
        }
    }

    private void getLocation() {

        if (isPermissionGranted) {

            try {
                mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);
            } catch (SecurityException e) {
                e.printStackTrace();
            }

        }

    }


    public static String getAddress(Context context, double LATITUDE, double LONGITUDE) {
        String city = "";
        String area = "";
        //Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city = addresses.get(0).getLocality();
                area = addresses.get(0).getSubAdminArea();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                Log.d(TAG, "getAddress: address" + address);
                Log.d(TAG, "getAddress: city" + city);
                Log.d(TAG, "getAddress: state" + state);
                Log.d(TAG, "getAddress: postalCode" + postalCode);
                Log.d(TAG, "getAddress: knownName" + knownName);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return city + "," + area;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    @Override
    public void PermissionGranted(int request_code) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {

    }

    @Override
    public void PermissionDenied(int request_code) {

    }

    @Override
    public void NeverAskAgain(int request_code) {

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {

                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location requests here
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(BookAppointFindLocation.this, REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }


    /**
     * Method to verify google play services on the device
     */

    private boolean checkPlayServices() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this, resultCode,
                        PLAY_SERVICES_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if(customResponse!=null) {
            RecentVisitedBaseModel recentVisitedBaseModel = (RecentVisitedBaseModel)customResponse;
            if(recentVisitedBaseModel.getRecentVisitedModel()!=null) {
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
                popularPlacesRecyclerView.setLayoutManager(layoutManager);
                popularPlacesRecyclerView.setItemAnimator(new DefaultItemAnimator());
                int spanCount = 3; // 3 columns
                int spacing = 20; // 50px
                boolean includeEdge = true;
                popularPlacesRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
                mShowPopularPlacesAdapter = new ShowPopularPlacesAdapter(mContext, recentVisitedBaseModel.getRecentVisitedModel().getAreaList());
                popularPlacesRecyclerView.setAdapter(mShowPopularPlacesAdapter);
                popularPlacesRecyclerView.setNestedScrollingEnabled(false);

                mRecentPlacesAdapter = new RecentPlacesAdapter(mContext,recentVisitedBaseModel.getRecentVisitedModel().getRecentlyVisitedAreaList());
                LinearLayoutManager linearlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                recentlyVisitedRecyclerView.setLayoutManager(linearlayoutManager);
                recentlyVisitedRecyclerView.setNestedScrollingEnabled(false);
                recentlyVisitedRecyclerView.setHasFixedSize(true);
                recentlyVisitedRecyclerView.setAdapter(mRecentPlacesAdapter);
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