package com.rescribe.ui.activities.book_appointment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.rescribe.R;
import com.rescribe.adapters.autocomplete_place.PlaceArrayAdapter;
import com.rescribe.adapters.book_appointment.RecentPlacesAdapter;
import com.rescribe.adapters.book_appointment.ShowPopularPlacesAdapter;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.search_doctors.RecentVisitedBaseModel;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.customesViews.CustomTextView;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.utils.GridSpacingItemDecoration;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class BookAppointFindLocation extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, HelperResponse, ShowPopularPlacesAdapter.OnPopularPlacesListener, RecentPlacesAdapter.OnRecentPlacesListener {

    public static final String TAG = "BookApFindLocation";
    private static final int GOOGLE_API_CLIENT_ID = 0;

    @BindView(R.id.bookAppointmentToolbar)
    ImageView bookAppointmentToolbar;
    @BindView(R.id.title)
    CustomTextView title;
    @BindView(R.id.detectLocation)
    CustomTextView detectLocation;

    @BindView(R.id.findLocation)
    AutoCompleteTextView mAutocompleteTextView;

    @BindView(R.id.popularPlacesRecyclerView)
    RecyclerView popularPlacesRecyclerView;
    @BindView(R.id.recentlyVisitedRecyclerView)
    RecyclerView recentlyVisitedRecyclerView;

    // For Location things

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private final static String LOCATION_KEY = "location-key";
    private final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 10;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private String mLastUpdateTime;

    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));

    // Location Things End

    private Context mContext;

    DoctorDataHelper mDoctorDataHelper;
    private ShowPopularPlacesAdapter mShowPopularPlacesAdapter;
    private RecentPlacesAdapter mRecentPlacesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appoint_select_location);
        ButterKnife.bind(this);

        mContext = BookAppointFindLocation.this;

        init(savedInstanceState);

        findViewById(R.id.bookAppointmentToolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init(Bundle savedInstanceState) {

        // Location

        buildGoogleApiClient();
        updateValuesFromBundle(savedInstanceState);

        mAutocompleteTextView.setThreshold(2);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

        // End Location

        mDoctorDataHelper = new DoctorDataHelper(this, this);
        mDoctorDataHelper.doGetRecentlyVisitedDoctorPlacesData();

    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);

            getAddress(place.getLatLng().latitude, place.getLatLng().longitude);
        }
    };

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

    @OnClick({R.id.bookAppointmentToolbar, R.id.detectLocation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bookAppointmentToolbar:
                break;
            case R.id.detectLocation:
                startUpdatesButtonHandler();
                break;
        }
    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(BookAppointFindLocation.this, Locale.getDefault());
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
                LatLng location = new LatLng(lat, lng);

                RescribeApplication.setUserSelectedLocationInfo(mContext, location, getArea(obj) + "," + obj.getLocality());
                detectLocation.setText(getArea(obj) + "," + obj.getLocality());
                finish();
                //DoctorDataHelper.setPreviousUserSelectedLocationInfo(mContext, location, getArea(obj) + "," + obj.getLocality());
               /* mDashboardHelper = new DashboardHelper(this, this);
                if (obj.getLocality() != null) {

                    mDashboardHelper.doGetDashboard(obj.getLocality());
                } else {
                    mDashboardHelper.doGetDashboard("");
                }*/

                Log.d("AREA", getArea(obj));
            } else {
                Toast.makeText(mContext, "Address not found.", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (customResponse != null) {
            RecentVisitedBaseModel recentVisitedBaseModel = (RecentVisitedBaseModel) customResponse;
            if (recentVisitedBaseModel.getRecentVisitedModel() != null) {
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
                popularPlacesRecyclerView.setLayoutManager(layoutManager);
                popularPlacesRecyclerView.setItemAnimator(new DefaultItemAnimator());
                int spanCount = 3; // 3 columns
                int spacing = 20; // 50px
                boolean includeEdge = true;
                popularPlacesRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
                mShowPopularPlacesAdapter = new ShowPopularPlacesAdapter(mContext, recentVisitedBaseModel.getRecentVisitedModel().getAreaList(), this);
                popularPlacesRecyclerView.setAdapter(mShowPopularPlacesAdapter);
                popularPlacesRecyclerView.setNestedScrollingEnabled(false);

                mRecentPlacesAdapter = new RecentPlacesAdapter(mContext, recentVisitedBaseModel.getRecentVisitedModel().getRecentlyVisitedAreaList(), this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClickOfPopularPlaces(String location) {
        RescribeApplication.setUserSelectedLocationInfo(this, null, location);
        finish();

    }

    @Override
    public void onClickOfRecentPlaces(String location) {
        RescribeApplication.setUserSelectedLocationInfo(this, null, location);
        finish();
    }

    // Location Things

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
        stopUpdatesButtonHandler();
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        Log.i(TAG, "Updating values from bundle");
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }
            updateUI();
        }
    }

    private void updateUI() {
        if (mCurrentLocation == null) return;

        // update UI
        getAddress(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void startUpdatesButtonHandler() {
        if (!isPlayServicesAvailable(this)) return;

        if (Build.VERSION.SDK_INT < 23) {
            setButtonsEnabledState(false);
            startLocationUpdates();
            return;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            setButtonsEnabledState(false);
            startLocationUpdates();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                showRationaleDialog();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
    }

    public void stopUpdatesButtonHandler() {
        setButtonsEnabledState(true);
        stopLocationUpdates();
    }

    private void startLocationUpdates() {
        Log.i(TAG, "startLocationUpdates");

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        if (ContextCompat.checkSelfPermission(BookAppointFindLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, BookAppointFindLocation.this);
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            status.startResolutionForResult(BookAppointFindLocation.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    private void setButtonsEnabledState(boolean isLocationUpdateStopped) {
        detectLocation.setEnabled(isLocationUpdateStopped);
    }

    protected void stopLocationUpdates() {
        Log.i(TAG, "stopLocationUpdates");
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setButtonsEnabledState(false);
                    startLocationUpdates();
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        setButtonsEnabledState(true);
                    } else {
                        showRationaleDialog();
                    }
                }
                break;
            }
        }
    }

    private void showRationaleDialog() {
        new AlertDialog.Builder(this)
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(BookAppointFindLocation.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }
                })
                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setButtonsEnabledState(false);
                    }
                })
                .setCancelable(false)
                .setMessage("Location Permission needed for sharing location.")
                .show();
    }

    public static boolean isPlayServicesAvailable(Context context) {

        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog((Activity) context, resultCode, 2).show();
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        isPlayServicesAvailable(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        stopLocationUpdates();
        mGoogleApiClient.disconnect();

        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (mCurrentLocation == null) {

            // take last known location if you want.
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        }

        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mPlaceArrayAdapter.setGoogleApiClient(null);
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    // End Location Things
}