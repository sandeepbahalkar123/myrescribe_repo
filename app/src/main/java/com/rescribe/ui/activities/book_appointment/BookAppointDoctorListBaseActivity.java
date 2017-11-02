package com.rescribe.ui.activities.book_appointment;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
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
import com.google.android.gms.maps.model.LatLng;
import com.rescribe.R;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.fragments.book_appointment.BookAppointDoctorDescriptionFragment;
import com.rescribe.ui.fragments.book_appointment.BookAppointFilteredDoctorListFragment;
import com.rescribe.ui.fragments.book_appointment.DrawerForFilterDoctorBookAppointment;
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
public class BookAppointDoctorListBaseActivity extends AppCompatActivity implements HelperResponse, DrawerForFilterDoctorBookAppointment.OnDrawerInteractionListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "BookAppointDoctorListBaseActivity";
    @BindView(R.id.bookAppointmentBackButton)
    ImageView bookAppointmentBackButton;
    static CustomTextView title;
    static CustomTextView locationTextView;
    static CustomTextView showlocation;
    @BindView(R.id.nav_view)
    FrameLayout mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    private DoctorDataHelper mDoctorDataHelper;
    private Fragment mCurrentlyLoadedFragment; //TODO, fragmentById is not working hence hold this object.
    private BookAppointmentBaseModel mReceivedBookAppointmentBaseModel;
    private BookAppointmentBaseModel mPreviousReqReceivedBookAppointmentBaseModel;

    private FragmentManager mSupportFragmentManager;
    private Fragment mDrawerLoadedFragment;
    private int PLACE_PICKER_REQUEST = 1;
    private boolean isLocationChange = false;

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
        mDrawerLayout.setFocusableInTouchMode(false);
        //----------------
        new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        //--------
        title = (CustomTextView) findViewById(R.id.title);
        locationTextView = (CustomTextView) findViewById(R.id.locationTextView);
        locationTextView.setVisibility(View.GONE);
        showlocation = (CustomTextView) findViewById(R.id.showlocation);

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //Called when a drawer's position changes.
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //Called when a drawer has settled in a completely open state.
                //The drawer is interactive at this point.
                // If you have 2 drawers (left and right) you can distinguish
                // them by using id of the drawerView. int id = drawerView.getId();
                // id will be your layout's id: for example R.id.left_drawer
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // Called when a drawer has settled in a completely closed state.
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // Called when the drawer motion state changes. The new state will be one of STATE_IDLE, STATE_DRAGGING or STATE_SETTLING.
            }
        });

        //------
        String locationReceived = "";
        Intent intent = getIntent();
        String title = "";
        if (intent != null) {
            HashMap<String, String> userSelectedLocationInfo = DoctorDataHelper.getUserSelectedLocationInfo();
            locationReceived = userSelectedLocationInfo.get(getString(R.string.location));
            locationTextView.setText("" + locationReceived);
            title = intent.getStringExtra(getString(R.string.clicked_item_data));
        }
        //------
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        mDrawerLoadedFragment = DrawerForFilterDoctorBookAppointment.newInstance();
        fragmentTransaction.replace(R.id.nav_view, mDrawerLoadedFragment);
        fragmentTransaction.commit();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.title), title);
        mCurrentlyLoadedFragment = RecentVisitDoctorFragment.newInstance(bundle);
        mDoctorDataHelper = new DoctorDataHelper(this, this);
        //----split based on location------
        if(locationReceived!=null) {
            String[] split = locationReceived.split(",");
            if (split.length == 2) {
                mDoctorDataHelper.doGetDoctorData(split[1], split[0], mComplaintsUserSearchFor);
            } else {
                mDoctorDataHelper.doGetDoctorData("", "", mComplaintsUserSearchFor);
            }
        }
        //----------
    }

    @Override
    public void onSuccess(final String mOldDataTag, final CustomResponse customResponse) {

        mReceivedBookAppointmentBaseModel = (BookAppointmentBaseModel) customResponse;

        if (mReceivedBookAppointmentBaseModel.getDoctorServicesModel() != null) {
            for (int i = 0; i < mReceivedBookAppointmentBaseModel.getDoctorServicesModel().getDoctorList().size(); i++) {
                if (!mReceivedBookAppointmentBaseModel.getDoctorServicesModel().getDoctorList().get(i).getDocName().contains("Dr.")) {
                    mReceivedBookAppointmentBaseModel.getDoctorServicesModel().getDoctorList().get(i).setDocName("Dr. " + mReceivedBookAppointmentBaseModel.getDoctorServicesModel().getDoctorList().get(i).getDocName());
                }
            }
            if (isLocationChange) {
                if (mCurrentlyLoadedFragment instanceof BookAppointFilteredDoctorListFragment) {
                    BookAppointFilteredDoctorListFragment d = (BookAppointFilteredDoctorListFragment) mCurrentlyLoadedFragment;
                    d.updateViewData();
                } else if (mCurrentlyLoadedFragment instanceof BookAppointDoctorDescriptionFragment) {
                    BookAppointDoctorDescriptionFragment d = (BookAppointDoctorDescriptionFragment) mCurrentlyLoadedFragment;
                    d.updateViewData();
                } else if (mCurrentlyLoadedFragment instanceof RecentVisitDoctorFragment) {
                    RecentVisitDoctorFragment d = (RecentVisitDoctorFragment) mCurrentlyLoadedFragment;
                    d.updateViewData();
                }
            } else {
                loadFragment(mCurrentlyLoadedFragment, false);
            }
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        loadFragment(mCurrentlyLoadedFragment, false);

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onApply(Bundle b, boolean drawerRequired) {
        mDrawerLayout.closeDrawers();
        doOperationOnDrawer(drawerRequired);
        if (mCurrentlyLoadedFragment instanceof BookAppointFilteredDoctorListFragment) {
            BookAppointFilteredDoctorListFragment d = (BookAppointFilteredDoctorListFragment) mCurrentlyLoadedFragment;
            if (mDrawerLoadedFragment instanceof DrawerForFilterDoctorBookAppointment) {
                d.onApplyClicked(b);
            }
        }
    }

    @Override
    public void onReset(boolean drawerRequired) {
        mDrawerLayout.closeDrawers();
        doOperationOnDrawer(drawerRequired);
        if (mCurrentlyLoadedFragment instanceof BookAppointFilteredDoctorListFragment) {
            BookAppointFilteredDoctorListFragment d = (BookAppointFilteredDoctorListFragment) mCurrentlyLoadedFragment;
            if (mDrawerLoadedFragment instanceof DrawerForFilterDoctorBookAppointment) {
                d.onResetClicked();
            }
        }
    }

    public BookAppointmentBaseModel getReceivedBookAppointmentBaseModel() {

        return mReceivedBookAppointmentBaseModel;
    }

    public void setReceivedBookAppointmentBaseModel(BookAppointmentBaseModel temp) {
        if (mPreviousReqReceivedBookAppointmentBaseModel == null) {
            mPreviousReqReceivedBookAppointmentBaseModel = mReceivedBookAppointmentBaseModel;
        }
        this.mReceivedBookAppointmentBaseModel = temp;
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
                BookAppointDoctorListBaseActivityPermissionsDispatcher.callPickPlaceWithCheck(this);
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
                    setSelectedLocationText(locality + ", " + city);
                    //-------
                    HashMap<String, String> userSelectedLocationInfo = DoctorDataHelper.getUserSelectedLocationInfo();
                    String s = userSelectedLocationInfo.get(getString(R.string.location));
                    if (s != null) {
                        isLocationChange = true;
                        String[] split = s.split(",");
                        mDoctorDataHelper.doGetDoctorData(city.trim(), split[0].trim(), mComplaintsUserSearchFor);
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


    @Override
    public void onBackPressed() {

        if (mSupportFragmentManager == null) {
            finish();
        } else {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                mDrawerLayout.closeDrawer(GravityCompat.END);
            } else {
                int backStackEntryCount = mSupportFragmentManager.getBackStackEntryCount();
                if (backStackEntryCount == 1) {
                    finish();
                } else {
                    super.onBackPressed();
                    Fragment id = mSupportFragmentManager.findFragmentById(R.id.viewContainer);
                    mCurrentlyLoadedFragment = id;

                    // This to recall API when came from ComplaintFragmnet.
                    if (mCurrentlyLoadedFragment instanceof RecentVisitDoctorFragment) {
                        if (mComplaintsUserSearchFor.size() > 0) {
                            // To clear complaints Map, and call API to get data.
                            mComplaintsUserSearchFor.clear();
                            HashMap<String, String> userSelectedLocationInfo = DoctorDataHelper.getUserSelectedLocationInfo();
                            String s = userSelectedLocationInfo.get(getString(R.string.location));
                            if (s != null) {
                                isLocationChange = true;
                                String[] split = s.split(",");
                                mDoctorDataHelper.doGetDoctorData(split[1].trim(), split[0].trim(), mComplaintsUserSearchFor);
                            }
                            //-------
                        }
                    }
                }
            }
        }
    }

    public void loadFragment(Fragment fragmentToLoad, boolean requiredDrawer) {
        if (fragmentToLoad != null) {
            mSupportFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mSupportFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.viewContainer, fragmentToLoad);
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commitAllowingStateLoss();
            this.mCurrentlyLoadedFragment = fragmentToLoad;

            //----This is done to hold complaints ----
            Bundle arguments = fragmentToLoad.getArguments();
            if (arguments != null) {
                if (getString(R.string.complaints).equalsIgnoreCase(arguments.getString(getString(R.string.opening_mode)))) {
                    mComplaintsUserSearchFor.put(getString(R.string.complaints), getString(R.string.complaints));
                    mComplaintsUserSearchFor.put(getString(R.string.complaint1), arguments.getString(getString(R.string.complaint1)));
                    mComplaintsUserSearchFor.put(getString(R.string.complaint2), arguments.getString(getString(R.string.complaint2)));
                }
            }
            //--------
            doOperationOnDrawer(requiredDrawer);
        }
    }

    public void doOperationOnDrawer(boolean flag) {
        if (flag) { // for open
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    public DrawerLayout getActivityDrawerLayout() {
        return mDrawerLayout;
    }

    public static void setToolBarTitle(String toolbartitle, boolean isLocationVisible) {
        title.setText(toolbartitle);
        if (isLocationVisible) {
            locationTextView.setVisibility(View.VISIBLE);
            showlocation.setVisibility(View.GONE);
        } else {
            locationTextView.setVisibility(View.GONE);
            showlocation.setVisibility(View.VISIBLE);
            showlocation.setText(locationTextView.getText().toString());
        }
    }

    public static void setSelectedLocationText(String locationText) {
        locationTextView.setText("" + locationText);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public interface OnActivityDrawerListener {
        void onApplyClicked(Bundle data);

        void onResetClicked();
    }

    public interface AddUpdateViewDataListener {
        void updateViewData();
    }


}
