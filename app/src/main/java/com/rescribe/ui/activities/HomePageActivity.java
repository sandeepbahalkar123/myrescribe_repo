package com.rescribe.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.heinrichreimersoftware.materialdrawer.DrawerActivity;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuAdapter;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.heinrichreimersoftware.materialdrawer.theme.DrawerTheme;
import com.rescribe.R;
import com.rescribe.adapters.dashboard.MenuOptionsDashBoardAdapter;
import com.rescribe.adapters.dashboard.ShowBackgroundViewPagerAdapter;
import com.rescribe.adapters.dashboard.ShowDoctorViewPagerAdapter;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.helpers.book_appointment.ServicesCardViewImpl;
import com.rescribe.helpers.dashboard.DashboardHelper;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.login.LoginHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.dashboard_api.DashBoardBaseModel;
import com.rescribe.model.dashboard_api.DashboardBottomMenuList;
import com.rescribe.model.dashboard_api.DashboardDataModel;
import com.rescribe.model.login.ActiveRequest;
import com.rescribe.notification.AppointmentAlarmTask;
import com.rescribe.notification.DosesAlarmTask;
import com.rescribe.notification.InvestigationAlarmTask;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.activities.book_appointment.BookAppointFindLocation;
import com.rescribe.ui.activities.book_appointment.BookAppointmentServices;
import com.rescribe.ui.activities.dashboard.HealthOffersActivity;
import com.rescribe.ui.activities.dashboard.SettingsActivity;
import com.rescribe.ui.activities.dashboard.SupportActivity;
import com.rescribe.ui.activities.doctor.DoctorListActivity;
import com.rescribe.ui.activities.find_doctors.FindDoctorsActivity;
import com.rescribe.ui.activities.health_repository.HealthRepository;
import com.rescribe.ui.activities.vital_graph.VitalGraphActivity;
import com.rescribe.ui.customesViews.CustomProgressDialog;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.GoogleSettingsApi;
import com.rescribe.util.RescribeConstants;

import net.gotev.uploadservice.UploadService;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.rescribe.util.RescribeConstants.ACTIVE_STATUS;
import static com.rescribe.util.RescribeConstants.TASK_DASHBOARD_API;

/**
 * Created by jeetal on 28/6/17.
 */

@RuntimePermissions
public class HomePageActivity extends DrawerActivity implements HelperResponse, MenuOptionsDashBoardAdapter.onMenuListClickListener, BottomMenuAdapter.onBottomMenuClickListener, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleSettingsApi.LocationSettings {

    private static final long MANAGE_ACCOUNT = 121;
    private static final long ADD_ACCOUNT = 122;
    private static final String TAG = "HomePage";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.viewPagerDoctorItem)
    ViewPager viewPagerDoctorItem;
    @BindView(R.id.menuOptionsListView)
    RecyclerView mMenuOptionsListView;
    @BindView(R.id.menuIcon)
    ImageView menuIcon;
    @BindView(R.id.locationImageView)
    ImageView locationImageView;
    private Context mContext;
    private String mGetMealTime;
    String breakFastTime = "";
    String lunchTime = "";
    String dinnerTime = "";
    String snacksTime = "";
    String locationReceived = "";
    String previousLocationReceived = "";
    private MenuOptionsDashBoardAdapter mMenuOptionsDashBoardAdapter;
    private String patientId;
    private LoginHelper loginHelper;
    Calendar c = Calendar.getInstance();
    int hour24 = c.get(Calendar.HOUR_OF_DAY);
    int Min = c.get(Calendar.MINUTE);
    private ShowDoctorViewPagerAdapter mShowDoctorViewPagerAdapter;
    private ShowBackgroundViewPagerAdapter mShowBackgroundViewPagerAdapter;
    ArrayList<DoctorList> mDashboardDoctorListsToShowDashboardDoctor;
    private int widthPixels;
    DashboardDataModel mDashboardDataModel;
    ArrayList<DashboardBottomMenuList> dashboardBottomMenuLists;
    private ServicesCardViewImpl mDashboardDataBuilder;
    private AppDBHelper appDBHelper;
    private DashboardHelper mDashboardHelper;
   // CustomProgressDialog mCustomProgressDialog;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    private int PLACE_PICKER_REQUEST = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dashboard_layout);
        ButterKnife.bind(this);
       // mCustomProgressDialog = new CustomProgressDialog(HomePageActivity.this);
        createLocationRequest();


        widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        mContext = HomePageActivity.this;

        //-------
        mDashboardDoctorListsToShowDashboardDoctor = new ArrayList<>();

        mDashboardDataBuilder = new ServicesCardViewImpl(this, this);

        //------
        appDBHelper = new AppDBHelper(mContext);
        patientId = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext);
        loginHelper = new LoginHelper(mContext, HomePageActivity.this);
        ActiveRequest activeRequest = new ActiveRequest();
        activeRequest.setId(Integer.parseInt(patientId));
        loginHelper.doActiveStatus(activeRequest);
        //------
        String currentDate = CommonMethods.getCurrentDate();
        String pastDate = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.NOTIFY_DATE, mContext);

        if (!currentDate.equals(pastDate)) {
            if (getIntent().getBooleanExtra(RescribeConstants.ALERT, true))
                notificationForMedicine();
        }
        drawerConfiguration();
        //------
        //  alertTab.setVisibility(View.VISIBLE);
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void getWritePermission() {
        HomePageActivityPermissionsDispatcher.getFineLocationWithCheck(HomePageActivity.this);
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void getFineLocation() {
        HomePageActivityPermissionsDispatcher.getCourseLocationWithCheck(HomePageActivity.this);
    }

    @NeedsPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    void getCourseLocation() {
        doCallDashBoardAPI();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HomePageActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

    }

    protected void createLocationRequest() {
        new GoogleSettingsApi(this);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private void notificationForMedicine() {
        String currentDate = CommonMethods.getCurrentDate();
        RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.NOTIFY_DATE, currentDate, mContext);

        AppDBHelper appDBHelper = new AppDBHelper(mContext);
        Cursor cursor = appDBHelper.getPreferences("1");
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                breakFastTime = cursor.getString(cursor.getColumnIndex(AppDBHelper.BREAKFAST_TIME));
                lunchTime = cursor.getString(cursor.getColumnIndex(AppDBHelper.LUNCH_TIME));
                dinnerTime = cursor.getString(cursor.getColumnIndex(AppDBHelper.DINNER_TIME));
                snacksTime = cursor.getString(cursor.getColumnIndex(AppDBHelper.SNACKS_TIME));
                cursor.moveToNext();
            }
        }
        cursor.close();

        String times[] = {breakFastTime, lunchTime, dinnerTime, snacksTime};
        String date = CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.DD_MM_YYYY);
        // notification for prescription , investigation and appointment initiated here
        new DosesAlarmTask(mContext, times, date).run();
        new InvestigationAlarmTask(mContext, RescribeConstants.INVESTIGATION_NOTIFICATION_TIME, getResources().getString(R.string.investigation_msg)).run();
        new AppointmentAlarmTask(mContext, RescribeConstants.APPOINTMENT_NOTIFICATION_TIME, getResources().getString(R.string.appointment_msg)).run();
    }

    @Override
    public void onBackPressed() {

        closeDrawer();
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
        if (id == R.id.notification) {

            mGetMealTime = CommonMethods.getMealTime(hour24, Min, this);
            if (mGetMealTime.equals(getString(R.string.break_fast))) {
                Intent intentNotification = new Intent(HomePageActivity.this, NotificationActivity.class);
                intentNotification.putExtra(RescribeConstants.MEDICINE_SLOT, getString(R.string.breakfast_medication));
                intentNotification.putExtra(RescribeConstants.DATE, CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.DD_MM_YYYY));
                intentNotification.putExtra(RescribeConstants.TIME, breakFastTime);
                intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentNotification);

            } else if (mGetMealTime.equals(getString(R.string.mlunch))) {
                Intent intentNotification = new Intent(HomePageActivity.this, NotificationActivity.class);
                intentNotification.putExtra(RescribeConstants.MEDICINE_SLOT, getString(R.string.lunch_medication));
                intentNotification.putExtra(RescribeConstants.DATE, CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.DD_MM_YYYY));
                intentNotification.putExtra(RescribeConstants.TIME, lunchTime);
                intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentNotification);

            } else if (mGetMealTime.equals(getString(R.string.msnacks))) {
                Intent intentNotification = new Intent(HomePageActivity.this, NotificationActivity.class);
                intentNotification.putExtra(RescribeConstants.MEDICINE_SLOT, getString(R.string.snacks_medication));
                intentNotification.putExtra(RescribeConstants.DATE, CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.DD_MM_YYYY));
                intentNotification.putExtra(RescribeConstants.TIME, snacksTime);
                intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentNotification);

            } else if (mGetMealTime.equals(getString(R.string.mdinner))) {
                Intent intentNotification = new Intent(HomePageActivity.this, NotificationActivity.class);
                intentNotification.putExtra(RescribeConstants.MEDICINE_SLOT, getString(R.string.dinner_medication));
                intentNotification.putExtra(RescribeConstants.DATE, CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.DD_MM_YYYY));
                intentNotification.putExtra(RescribeConstants.TIME, dinnerTime);
                intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentNotification);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        String mobileNoGmail = "";
        String passwordGmail = "";
        String mobileNoFacebook = "";
        String passwordFacebook = "";
        String gmailLogin = "";
        String facebookLogin = "";

        // Stop Uploads
        UploadService.stopAllUploads();

        //Logout functionality
        if (RescribePreferencesManager.getString(RescribeConstants.GMAIL_LOGIN, mContext).equalsIgnoreCase(getString(R.string.login_with_gmail))) {
            gmailLogin = RescribePreferencesManager.getString(RescribeConstants.GMAIL_LOGIN, mContext);
            mobileNoGmail = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER_GMAIL, mContext);
            passwordGmail = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PASSWORD_GMAIL, mContext);

        }
        if (RescribePreferencesManager.getString(RescribeConstants.FACEBOOK_LOGIN, mContext).equalsIgnoreCase(getString(R.string.login_with_facebook))) {
            facebookLogin = RescribePreferencesManager.getString(RescribeConstants.FACEBOOK_LOGIN, mContext);
            mobileNoFacebook = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER_FACEBOOK, mContext);
            passwordFacebook = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PASSWORD_FACEBOOK, mContext);

        }

        RescribePreferencesManager.clearSharedPref(mContext);
        RescribePreferencesManager.putString(RescribeConstants.GMAIL_LOGIN, gmailLogin, mContext);
        RescribePreferencesManager.putString(RescribeConstants.FACEBOOK_LOGIN, facebookLogin, mContext);
        RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER_GMAIL, mobileNoGmail, mContext);
        RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PASSWORD_GMAIL, passwordGmail, mContext);
        RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER_FACEBOOK, mobileNoFacebook, mContext);
        RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PASSWORD_FACEBOOK, passwordFacebook, mContext);
        RescribePreferencesManager.putString(getString(R.string.logout), "" + 1, mContext);

        appDBHelper.deleteDatabase();

        new DosesAlarmTask(mContext, null, null).run();
        new AppointmentAlarmTask(mContext, null, null).run();
        new InvestigationAlarmTask(mContext, null, null).run();

        Intent intent = new Intent(mContext, LoginSignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    private void drawerConfiguration() {
        setDrawerTheme(
                new DrawerTheme(this)
                        .setBackgroundColorRes(R.color.drawer_bg)
                        .setTextColorPrimaryRes(R.color.drawer_menu_text_color)
                        .setTextColorSecondaryRes(R.color.drawer_menu_text_color)
        );

        addItems(new DrawerItem()
                        .setTextPrimary(getString(R.string.services))
                        .setImage(ContextCompat.getDrawable(this, R.drawable.services)),
                new DrawerItem()
                        .setTextPrimary(getString(R.string.on_going_treatment))
                        .setImage(ContextCompat.getDrawable(this, R.drawable.menu_prescription)),
                new DrawerItem()
                        .setTextPrimary(getString(R.string.appointments))
                        .setImage(ContextCompat.getDrawable(this, R.drawable.menu_appointments)),
                new DrawerItem()
                        .setTextPrimary(getString(R.string.doctor_details))
                        .setImage(ContextCompat.getDrawable(this, R.drawable.menu_doctor_visit)),
                /*new DrawerItem()
                        .setTextPrimary(getString(R.string.investigation))
                        .setImage(ContextCompat.getDrawable(this, R.drawable.menu_investigations)),*/
                new DrawerItem()
                        .setTextPrimary(getString(R.string.vital_graph))
                        .setImage(ContextCompat.getDrawable(this, R.drawable.menu_vital_graph)),
               /* new DrawerItem()
                        .setTextPrimary(getString(R.string.my_records))
                        .setImage(ContextCompat.getDrawable(this, R.drawable.menu_my_records)),*/
                new DrawerItem()
                        .setTextPrimary(getString(R.string.settings))
                        .setImage(ContextCompat.getDrawable(this, R.drawable.setting)),

                new DrawerItem()
                        .setTextPrimary(getString(R.string.logout))
                        .setImage(ContextCompat.getDrawable(this, R.drawable.menu_logout))

        );
        setOnItemClickListener(new DrawerItem.OnItemClickListener() {
            @Override
            public void onClick(DrawerItem item, long itemID, int position) {
                //  selectItem(position);
                String id = item.getTextPrimary();
                if (id.equalsIgnoreCase(getString(R.string.doctor_details))) {
                    Intent intent = new Intent(mContext, DoctorListActivity.class);
                    startActivity(intent);
                } else if (id.equalsIgnoreCase(getString(R.string.on_going_treatment))) {
                    Intent intent = new Intent(mContext, PrescriptionActivity.class);
                    intent.putExtra(getString(R.string.toolbarTitle), id);
                    startActivity(intent);
                } else if (id.equalsIgnoreCase(getString(R.string.appointments))) {
                    Intent intent = new Intent(mContext, AppointmentActivity.class);
                    startActivity(intent);
                } /*else if (id.equalsIgnoreCase(getString(R.string.investigation))) {
                    Intent intent = new Intent(mContext, InvestigationActivity.class);
                    startActivity(intent);
                }*//* else if (id.equalsIgnoreCase(getString(R.string.my_records))) {
                    MyRecordsData myRecordsData = appDBHelper.getMyRecordsData();
                    int completeCount = 0;
                    for (Image image : myRecordsData.getImageArrayList()) {
                        if (image.isUploading() == RescribeConstants.COMPLETED)
                            completeCount++;
                    }
                    Intent intent;
                    if (completeCount == myRecordsData.getImageArrayList().size()) {
                        appDBHelper.deleteMyRecords();
                        intent = new Intent(mContext, MyRecordsActivity.class);
                    } else {
                        intent = new Intent(mContext, SelectedRecordsGroupActivity.class);
                        intent.putExtra(RescribeConstants.UPLOADING_STATUS, true);
                        intent.putExtra(RescribeConstants.VISIT_DATE, myRecordsData.getVisitDate());
                        intent.putExtra(RescribeConstants.OPD_ID, myRecordsData.getDocId());
                        intent.putExtra(RescribeConstants.DOCTORS_ID, myRecordsData.getDocId());
                        intent.putExtra(RescribeConstants.DOCUMENTS, myRecordsData.getImageArrayList());
                    }
                    startActivity(intent);

                }  else if (id.equalsIgnoreCase(getString(R.string.logout))) {
                    logout();

                }*/ else if (id.equalsIgnoreCase(getString(R.string.logout))) {
                    ActiveRequest activeRequest = new ActiveRequest();
                    activeRequest.setId(Integer.parseInt(patientId));
                    loginHelper.doLogout(activeRequest);

                } else if (id.equalsIgnoreCase(getString(R.string.doctor_connect))) {
                    Intent intent = new Intent(mContext, DoctorConnectActivity.class);
                    startActivity(intent);
                } else if (id.equalsIgnoreCase(getString(R.string.vital_graph))) {
                    Intent intent = new Intent(mContext, VitalGraphActivity.class);
                    startActivity(intent);
                } else if (id.equalsIgnoreCase(getString(R.string.settings))) {


                    //TODO : added for testing purpose, remove it
                    //----------
                    //Intent popup = new Intent(getApplicationContext(), SettingActivity.class);
                    //     Intent popup = new Intent(getApplicationContext(), BookAppointFindLocation.class);
                    // startActivity(popup);
                    //----------
                } else if (id.equalsIgnoreCase(getString(R.string.services))) {
                    Intent intent = new Intent(mContext, BookAppointmentServices.class);
                    //    Intent intent = new Intent(mContext, DoctorListToBookAppointment.class);
                    startActivityForResult(intent, RescribeConstants.DOCTOR_DATA_REQUEST_CODE);
                }

                closeDrawer();
            }
        });

        // TODO : HARDEDCODED will get remove once done with APIs.
        addProfile(new DrawerProfile()
                .setId(1)
                .setRoundedAvatar((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.profile))
                .setBackground(ContextCompat.getDrawable(this, R.drawable.group_2))
                .setName(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.USER_NAME, mContext))
                .setDescription(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.USER_EMAIL, mContext))
        );

        addProfile(new DrawerProfile()
                .setId(2)
                .setRoundedAvatar((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.profile))
                .setBackground(ContextCompat.getDrawable(this, R.drawable.group_2))
                .setName("Mr.Sandeep Deshmukh ")
                .setDescription("sandeep_deshmukh@gmail.com")
        );

        addProfile(new DrawerProfile()
                .setId(ADD_ACCOUNT)
                .setRoundedAvatar((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.add_account))
                .setBackground(ContextCompat.getDrawable(this, R.drawable.group_2))
                .setDescription("Add Patient").setProfile(false) // for fixed item set profile false
        );

        addProfile(new DrawerProfile()
                .setId(MANAGE_ACCOUNT)
                .setRoundedAvatar((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.setting))
                .setBackground(ContextCompat.getDrawable(this, R.drawable.group_2))
                .setDescription("Manage Profile").setProfile(false) // for fixed item set profile false
        );

        setOnNonProfileClickListener(new DrawerProfile.OnNonProfileClickListener() {
            @Override
            public void onProfileItemClick(DrawerProfile profile, long id) {
                if (id == ADD_ACCOUNT) {

                    // Do stuff here

                    addProfile(new DrawerProfile()
                            .setId(3)
                            .setRoundedAvatar((BitmapDrawable) ContextCompat.getDrawable(mContext, R.drawable.profile))
                            .setBackground(ContextCompat.getDrawable(mContext, R.drawable.group_2))
                            .setName("Mr.Ganesh Deshmukh")
                            .setDescription("ganesh_deshmukh@gmail.com")
                    );
//                    CommonMethods.showToast(mContext, "Profile Added");

                } else if (id == MANAGE_ACCOUNT) {
                    // Do stuff here
//                    CommonMethods.showToast(mContext, profile.getDescription());
                }
                closeDrawer();
            }
        });

        setOnProfileSwitchListener(new DrawerProfile.OnProfileSwitchListener() {
            @Override
            public void onSwitch(DrawerProfile oldProfile, long oldId, DrawerProfile newProfile, long newId) {
                // do stuff here
//                CommonMethods.showToast(mContext, "Welcome " + newProfile.getName());
            }
        });
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

        switch (mOldDataTag) {
            case TASK_DASHBOARD_API:
                DashBoardBaseModel mDashboardBaseModel = (DashBoardBaseModel) customResponse;
                mDashboardDataModel = mDashboardBaseModel.getDashboardModel();
                mDashboardDataBuilder.setReceivedDoctorDataList(mDashboardDataModel.getDoctorList());
                if (mDashboardDataModel != null) {
                    //----------
                    setUpViewPager();
                    //----------
                    doConfigureMenuOptions();
                }
                break;
            case RescribeConstants.LOGOUT:
                logout();
                break;
            case ACTIVE_STATUS:
                CommonMethods.Log(ACTIVE_STATUS, "active");
                break;
            case RescribeConstants.TASK_SET_FAVOURITE_DOCTOR:
                if (customResponse != null) {
                    CommonBaseModelContainer responseFavouriteDoctorBaseModel = (CommonBaseModelContainer) customResponse;
                    if (responseFavouriteDoctorBaseModel.getCommonRespose().isSuccess()) {
                        mDashboardDataBuilder.updateFavStatusForDoctorDataObject(ServicesCardViewImpl.getUserSelectedDoctorListDataObject());
                        setUpViewPager();
                    }
                    CommonMethods.showToast(this, responseFavouriteDoctorBaseModel.getCommonRespose().getStatusMessage());
                }
        }
    }

    private void setUpViewPager() {
        //----------
     //   mCustomProgressDialog.cancel();
        Map<String, Integer> dataMap = new LinkedHashMap<>();
        ArrayList<DoctorList> myAppoint = mDashboardDataBuilder.getCategoryWiseDoctorList(getString(R.string.my_appointments));
        ArrayList<DoctorList> sponsered = mDashboardDataBuilder.getCategoryWiseDoctorList(getString(R.string.sponsored_doctor));
        ArrayList<DoctorList> recently_visit_doctor = mDashboardDataBuilder.getCategoryWiseDoctorList(getString(R.string.recently_visited_doctor));
        ArrayList<DoctorList> favoriteList = mDashboardDataBuilder.getFavouriteDocList();

        dataMap.put(getString(R.string.my_appointments), myAppoint.size());
        dataMap.put(getString(R.string.sponsored_doctor), sponsered.size());
        dataMap.put(getString(R.string.recently_visited_doctor), recently_visit_doctor.size());
        dataMap.put(getString(R.string.favorite), favoriteList.size());

        ArrayList<DoctorList> mergeList = new ArrayList<>();

        if (myAppoint.size() > 0)
            mergeList.add(myAppoint.get(0));

        if (sponsered.size() > 0)
            mergeList.add(sponsered.get(0));

        if (recently_visit_doctor.size() > 0)
            mergeList.add(recently_visit_doctor.get(0));

        if (favoriteList.size() > 0)
            mergeList.add(favoriteList.get(0));

        //------------
        mDashboardDoctorListsToShowDashboardDoctor.addAll(mergeList);
        mShowDoctorViewPagerAdapter = new ShowDoctorViewPagerAdapter(this, mergeList, mDashboardDataBuilder, dataMap, this);
        viewPagerDoctorItem.setAdapter(mShowDoctorViewPagerAdapter);
        //-----------
        // Disable clip to padding
        viewPagerDoctorItem.setClipToPadding(false);

        int pager_padding = getResources().getDimensionPixelSize(R.dimen.pager_padding);
        viewPagerDoctorItem.setPadding(pager_padding, 0, pager_padding, 0);
        int pager_margin = getResources().getDimensionPixelSize(R.dimen.pager_margin);
        viewPagerDoctorItem.setPageMargin(pager_margin);

        mShowBackgroundViewPagerAdapter = new ShowBackgroundViewPagerAdapter(this, mDashboardDataModel.getCardBgImageUrlList());
        viewpager.setAdapter(mShowBackgroundViewPagerAdapter);
        viewpager.setOffscreenPageLimit(mShowBackgroundViewPagerAdapter.getCount());

        final int scrollPixels = widthPixels * mShowDoctorViewPagerAdapter.getCount();
        final int exactScroll = scrollPixels - widthPixels;
        int itemWidth = (widthPixels - (pager_padding * 2)) + pager_margin;
        final int scrollWidth = itemWidth * (mShowDoctorViewPagerAdapter.getCount() - 1);

        viewPagerDoctorItem.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                float ans = (float) (exactScroll * viewPagerDoctorItem.getScrollX()) / scrollWidth;
                viewpager.setScrollX((int) ans);
            }

            @Override
            public void onPageSelected(int position) {
//                        viewpager.setCurrentItem(position, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
      ///  mCustomProgressDialog.cancel();
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
      //  mCustomProgressDialog.cancel();
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
       // mCustomProgressDialog.cancel();
    }


    @OnClick({R.id.menuIcon, R.id.locationImageView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menuIcon:
                openDrawer();
                break;
            case R.id.locationImageView:
                Intent start = new Intent(this, BookAppointFindLocation.class);
                startActivityForResult(start, PLACE_PICKER_REQUEST);
                break;

        }
    }

    @Override
    public void onClickOfMenu(String menuName) {
        if (menuName.equals(getString(R.string.find_doctors))) {
            Intent intent = new Intent(mContext, FindDoctorsActivity.class);
            intent.putExtra(getString(R.string.toolbarTitle), menuName);
            startActivity(intent);
        } else if (menuName.equals(getString(R.string.on_going_treatment))) {
            Intent intent = new Intent(mContext, PrescriptionActivity.class);
            intent.putExtra(getString(R.string.toolbarTitle), menuName);
            startActivity(intent);
        } else if (menuName.equals(getString(R.string.health_repository))) {
            Intent intent = new Intent(mContext, HealthRepository.class);
            intent.putExtra(getString(R.string.toolbarTitle), menuName);
            startActivity(intent);
        } else if (menuName.equals(getString(R.string.health_offers))) {
            Intent intent = new Intent(mContext, HealthOffersActivity.class);
            intent.putExtra(getString(R.string.toolbarTitle), menuName);
            startActivity(intent);

        } else if (menuName.equals(getString(R.string.health_education))) {

        }
    }

    @Override
    public void onBottomMenuClick(BottomMenu bottomMenu) {

        String menuName = bottomMenu.getMenuName();

        if (menuName.equalsIgnoreCase(getString(R.string.home))) {


           /* mGetMealTime = CommonMethods.getMealTime(hour24, Min, this);
            Intent intent = new Intent(HomePageActivity.this, NotificationActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            intent.putExtra(RescribeConstants.DATE, CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.DD_MM_YYYY));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);

            if (mGetMealTime.equals(getString(R.string.break_fast))) {
                intent.putExtra(RescribeConstants.MEDICINE_SLOT, getString(R.string.breakfast_medication));
                intent.putExtra(RescribeConstants.TIME, breakFastTime);
            } else if (mGetMealTime.equals(getString(R.string.mlunch))) {
                intent.putExtra(RescribeConstants.MEDICINE_SLOT, getString(R.string.lunch_medication));
                intent.putExtra(RescribeConstants.TIME, lunchTime);
            } else if (mGetMealTime.equals(getString(R.string.msnacks))) {
                intent.putExtra(RescribeConstants.MEDICINE_SLOT, getString(R.string.snacks_medication));
                intent.putExtra(RescribeConstants.TIME, snacksTime);
            } else if (mGetMealTime.equals(getString(R.string.mdinner))) {
                intent.putExtra(RescribeConstants.MEDICINE_SLOT, getString(R.string.dinner_medication));
                intent.putExtra(RescribeConstants.TIME, dinnerTime);
            } else if (mGetMealTime.isEmpty()) {
                intent.putExtra(RescribeConstants.MEDICINE_SLOT, getString(R.string.dinner_medication));
                intent.putExtra(RescribeConstants.TIME, dinnerTime);
            }

            startActivity(intent);*/


        } else if (menuName.equalsIgnoreCase(getString(R.string.appointment))) {
            Intent intent = new Intent(HomePageActivity.this, BookAppointDoctorListBaseActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);

        } else if (menuName.equalsIgnoreCase(getString(R.string.settings))) {
            Intent intent = new Intent(HomePageActivity.this, SettingsActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);

        } else if (menuName.equalsIgnoreCase(getString(R.string.support))) {
            Intent intent = new Intent(HomePageActivity.this, SupportActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);

        }
    }

    private void doConfigureMenuOptions() {

        mMenuOptionsDashBoardAdapter = new MenuOptionsDashBoardAdapter(this, this, mDashboardDataModel.getDashboardMenuList());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setAutoMeasureEnabled(true);
        mMenuOptionsListView.setLayoutManager(linearLayoutManager);
        mMenuOptionsListView.setNestedScrollingEnabled(false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mMenuOptionsListView.getContext(),
                linearLayoutManager.getOrientation());
        mMenuOptionsListView.addItemDecoration(dividerItemDecoration);
        mMenuOptionsListView.setAdapter(mMenuOptionsDashBoardAdapter);

        // add bottom menu
        dashboardBottomMenuLists = mDashboardDataModel.getDashboardBottomMenuList();
        for (DashboardBottomMenuList dashboardBottomMenuList : dashboardBottomMenuLists) {
            BottomMenu bottomMenu = new BottomMenu();
            bottomMenu.setMenuIcon(dashboardBottomMenuList.getImageUrl());
            bottomMenu.setMenuName(dashboardBottomMenuList.getName());

            bottomMenu.setAppIcon(dashboardBottomMenuList.getName().equals(getString(R.string.app_logo)));
            bottomMenu.setSelected(dashboardBottomMenuList.getName().equals(getString(R.string.home)));

            addBottomMenu(bottomMenu);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        // mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            CommonMethods.showToast(HomePageActivity.this, "Location Permission Required.");
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    @Override
    public void onResume() {
        super.onResume();

        HomePageActivityPermissionsDispatcher.getWritePermissionWithCheck(HomePageActivity.this);

       /* if (mDashboardDataModel != null) {
            setUpViewPager();
        }*/

    }

    private void doCallDashBoardAPI() {
        HashMap<String, String> userSelectedLocationInfo = RescribeApplication.getUserSelectedLocationInfo();
        if (userSelectedLocationInfo.get(getString(R.string.location)) != null) {
            locationReceived = userSelectedLocationInfo.get(getString(R.string.location));
        } else {
            locationReceived = "";
        }
        if (RescribeApplication.getPreviousUserSelectedLocationInfo() != null) {
            HashMap<String, String> userPreviousSelectedLocationInfo = RescribeApplication.getPreviousUserSelectedLocationInfo();
            previousLocationReceived = userPreviousSelectedLocationInfo.get(getString(R.string.location));
        } else {
            previousLocationReceived = "";
        }

        if (locationReceived.equalsIgnoreCase(previousLocationReceived)) {
            Log.d(TAG, "DASHBOARD API NOT CALLED");

        } else {
            Log.d(TAG, "DASHBOARD API  CALLED");
            if (locationReceived.equals("")) {
               // mCustomProgressDialog.show();
                if (!mGoogleApiClient.isConnected())
                    mGoogleApiClient.connect();
                else startLocationUpdates();
            } else {
                String[] split = locationReceived.split(",");
                mDashboardHelper = new DashboardHelper(this, this);
                mDashboardHelper.doGetDashboard(split[1]);
                Double lat = Double.valueOf(userSelectedLocationInfo.get(getString(R.string.latitude)));
                Double lng = Double.valueOf(userSelectedLocationInfo.get(getString(R.string.longitude)));
                LatLng latLng = new LatLng(lat, lng);
                RescribeApplication.setPreviousUserSelectedLocationInfo(mContext, latLng, locationReceived);
            }
        }

        if (mDashboardDataModel != null) {
            setUpViewPager();
        }

    }

    private void updateUI() {
        Log.d(TAG, "UI update initiated.");
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());

            /*tvLocation.setText("At Time: " + mLastUpdateTime + "\n" +
                    "Latitude: " + lat + "\n" +
                    "Longitude: " + lng + "\n" +
                    "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
                    "Provider: " + mCurrentLocation.getProvider());*/
            Log.e("Latitude Longitude", lat + "," + lng);

            getAddress(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            stopLocationUpdates();
        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(HomePageActivity.this, Locale.getDefault());
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
                mDashboardHelper = new DashboardHelper(this, this);
                RescribeApplication.setUserSelectedLocationInfo(mContext, location, getArea(obj) + "," + obj.getLocality());
                RescribeApplication.setPreviousUserSelectedLocationInfo(mContext, location, getArea(obj) + "," + obj.getLocality());
                if (obj.getLocality() != null) {

                    mDashboardHelper.doGetDashboard(obj.getLocality());
                } else {
                    mDashboardHelper.doGetDashboard("");
                }

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
    public void gpsStatus() {
      //  mCustomProgressDialog.show();
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
        else startLocationUpdates();
    }
}
