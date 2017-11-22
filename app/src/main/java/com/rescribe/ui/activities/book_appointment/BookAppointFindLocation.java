package com.rescribe.ui.activities.book_appointment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class BookAppointFindLocation extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, HelperResponse, ShowPopularPlacesAdapter.OnPopularPlacesListener, RecentPlacesAdapter.OnRecentPlacesListener {

    public static final String TAG = "BookAppointFindLocation";
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
    private Context mContext;
    private final static int PLAY_SERVICES_REQUEST = 1000;
    private final static int REQUEST_CHECK_SETTINGS = 2000;
    private Location mLastLocation;
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

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;

    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));

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

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(LocationServices.API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mAutocompleteTextView.setThreshold(2);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

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

            // Format the returned place's details and display them in the TextView.
           /* CommonMethods.showToast(mContext, "" + formatPlaceDetails(getResources(), place.getName(), place.getId(),
                    place.getAddress(), place.getPhoneNumber(), place.getWebsiteUri()));

            CharSequence attributions = place.getAttributions();
            if (!TextUtils.isEmpty(attributions)) {
                CommonMethods.showToast(mContext, "" + Html.fromHtml(attributions.toString()));
            } else {
                CommonMethods.showToast(mContext, "");
            }*/
        }
    };


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
                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (mLocationRequest == null)
            createLocationRequest();
        else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                CommonMethods.Log(TAG, "Permission not granted");
                return;
            }
            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

            Log.d(TAG, "Location update started ..............: ");
        }
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
                Toast.makeText(mContext, "Address not found.", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
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