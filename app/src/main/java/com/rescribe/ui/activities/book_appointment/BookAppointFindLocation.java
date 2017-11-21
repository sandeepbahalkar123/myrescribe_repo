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
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.rescribe.R;
import com.rescribe.adapters.book_appointment.RecentPlacesAdapter;
import com.rescribe.adapters.book_appointment.ShowPopularPlacesAdapter;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.helpers.dashboard.DashboardHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.search_doctors.RecentVisitedBaseModel;
import com.rescribe.model.filter.DoctorData;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.activities.HomePageActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.LocationUtil.PermissionUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.utils.GridSpacingItemDecoration;


public class BookAppointFindLocation extends AppCompatActivity implements PlaceSelectionListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, HelperResponse, ShowPopularPlacesAdapter.OnPopularPlacesListener, RecentPlacesAdapter.OnRecentPlacesListener {

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
    Location mCurrentLocation;
    String mLastUpdateTime;
    double longitude;
    ArrayList<String> permissions = new ArrayList<>();
    PermissionUtils permissionUtils;
    boolean isPermissionGranted;
    LocationRequest mLocationRequest;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    DoctorDataHelper mDoctorDataHelper;
    private ShowPopularPlacesAdapter mShowPopularPlacesAdapter;
    private RecentPlacesAdapter mRecentPlacesAdapter;
    String address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appoint_select_location);
        ButterKnife.bind(this);
        mContext = BookAppointFindLocation.this;
        init();

        findViewById(R.id.bookAppointmentToolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {
        mDoctorDataHelper = new DoctorDataHelper(this, this);
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
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());

                address = String.format("%s", place.getAddress());
                stBuilder.append("Name: ");
                stBuilder.append(placename);
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

                    RescribeApplication.setUserSelectedLocationInfo(BookAppointFindLocation.this, place.getLatLng(), locality + ", " + city);
                    finish();

                }
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
            } else if (requestCode == RESULT_CANCELED) {

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

    @OnClick({R.id.bookAppointmentToolbar, R.id.detectLocation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bookAppointmentToolbar:
                break;
            case R.id.detectLocation:
                createLocationRequest();
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
                mGoogleApiClient.connect();

                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

        Log.d(TAG, "Location update started ..............: ");
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
                Toast.makeText(this, "Address not found.", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    @Override
    protected void onResume() {
        super.onResume();

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
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());

            /*tvLocation.setText("At Time: " + mLastUpdateTime + "\n" +
                    "Latitude: " + lat + "\n" +
                    "Longitude: " + lng + "\n" +
                    "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
                    "Provider: " + mCurrentLocation.getProvider());*/
            Log.e("Latitude Longitude ===============", lat + "///////////" + lng);

            getAddress(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            stopLocationUpdates();
        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
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
}