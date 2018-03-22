package com.rescribe.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.heinrichreimersoftware.materialdrawer.app_logo.BottomSheetMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuActivity;
import com.rescribe.BuildConfig;
import com.rescribe.R;
import com.rescribe.adapters.dashboard.MenuOptionsDashBoardAdapter;
import com.rescribe.adapters.dashboard.ShowBackgroundViewPagerAdapter;
import com.rescribe.adapters.dashboard.ShowDoctorViewPagerAdapter;
import com.rescribe.helpers.book_appointment.ServicesCardViewImpl;
import com.rescribe.helpers.dashboard.DashboardHelper;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.database.MyRecordsData;
import com.rescribe.helpers.login.LoginHelper;
import com.rescribe.helpers.notification.NotificationHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.chat.MQTTMessage;
import com.rescribe.model.dashboard_api.ClickOption;
import com.rescribe.model.dashboard_api.DashBoardBaseModel;
import com.rescribe.model.dashboard_api.DashboardBottomMenuList;
import com.rescribe.model.dashboard_api.DashboardDataModel;
import com.rescribe.model.dashboard_api.DashboardMenuData;
import com.rescribe.model.dashboard_api.DashboardMenuList;
import com.rescribe.model.investigation.Image;
import com.rescribe.model.login.ActiveRequest;
import com.rescribe.model.notification.Medication;
import com.rescribe.model.notification.NotificationData;
import com.rescribe.model.notification.NotificationModel;
import com.rescribe.notification.AppointmentAlarmTask;
import com.rescribe.notification.DeleteUnreadNotificationAlarmTask;
import com.rescribe.notification.DosesAlarmTask;
import com.rescribe.notification.InvestigationAlarmTask;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.services.MQTTService;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.activities.book_appointment.BookAppointFindLocation;
import com.rescribe.ui.activities.book_appointment.BookAppointmentServices;
import com.rescribe.ui.activities.dashboard.HealthOffersActivity;
import com.rescribe.ui.activities.dashboard.ProfileActivity;
import com.rescribe.ui.activities.dashboard.SettingsActivity;
import com.rescribe.ui.activities.dashboard.UnreadNotificationMessageActivity;
import com.rescribe.ui.activities.doctor.DoctorListActivity;
import com.rescribe.ui.activities.find_doctors.FindDoctorsActivity;
import com.rescribe.ui.activities.health_repository.HealthRepositoryActivity;
import com.rescribe.ui.activities.saved_articles.SavedArticlesActivity;
import com.rescribe.ui.activities.vital_graph.VitalGraphActivity;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.Config;
import com.rescribe.util.GoogleSettingsApi;
import com.rescribe.util.RescribeConstants;

import java.io.IOException;
import java.io.InputStream;
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

import static com.rescribe.notification.DosesAlarmTask.BREAKFAST_NOTIFICATION_ID;
import static com.rescribe.notification.DosesAlarmTask.DINNER_NOTIFICATION_ID;
import static com.rescribe.notification.DosesAlarmTask.EVENING_NOTIFICATION_ID;
import static com.rescribe.notification.DosesAlarmTask.LUNCH_NOTIFICATION_ID;
import static com.rescribe.services.MQTTService.MESSAGE_TOPIC;
import static com.rescribe.services.MQTTService.NOTIFY;
import static com.rescribe.services.MQTTService.TOPIC;
import static com.rescribe.ui.activities.book_appointment.BookAppointFindLocation.REQUEST_CHECK_SETTINGS;
import static com.rescribe.util.RescribeConstants.ACTIVE_STATUS;
import static com.rescribe.util.RescribeConstants.BOTTOM_MENU.APP_LOGO;
import static com.rescribe.util.RescribeConstants.BOTTOM_MENU.BOOK;
import static com.rescribe.util.RescribeConstants.BOTTOM_MENU.CONNECT;
import static com.rescribe.util.RescribeConstants.BOTTOM_MENU.HOME;
import static com.rescribe.util.RescribeConstants.BOTTOM_MENU.SETTINGS;
import static com.rescribe.util.RescribeConstants.DRAWABLE;
import static com.rescribe.util.RescribeConstants.SALUTATION;
import static com.rescribe.util.RescribeConstants.TASK_DASHBOARD_API;

/**
 * Created by jeetal on 28/6/17.
 */

public class HomePageActivity extends BottomMenuActivity implements HelperResponse, MenuOptionsDashBoardAdapter.onMenuListClickListener, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "HomePage";
    @BindView(R.id.custom_progress_bar)
    RelativeLayout custom_progress_bar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.viewPagerDoctorItem)
    ViewPager viewPagerDoctorItem;
    @BindView(R.id.menuOptionsListView)
    RecyclerView mMenuOptionsListView;

    @BindView(R.id.locationImageView)
    ImageView locationImageView;

    @BindView(R.id.preloadView)
    ImageView preloadView;

    private Context mContext;
    String locationReceived = "";
    String previousLocationReceived = "";
    ArrayList<DoctorList> mDashboardDoctorListsToShowDashboardDoctor;
    private int widthPixels;
    DashboardDataModel mDashboardDataModel;
    DashboardMenuData mDashboardMenuData;
    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    ArrayList<DashboardBottomMenuList> dashboardBottomMenuLists;
    private ServicesCardViewImpl mDashboardDataBuilder;
    private AppDBHelper appDBHelper;
    private DashboardHelper mDashboardHelper;
    ArrayList<DoctorList> myAppoint;
    ArrayList<DoctorList> sponsered;
    ArrayList<DoctorList> recently_visit_doctor;
    ArrayList<DoctorList> favoriteList;
    private static final long INTERVAL = 1000 * 50;
    private static final long FASTEST_INTERVAL = 1000 * 20;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    private String profileImageString;
    private UpdateAppUnreadNotificationCount mUpdateAppUnreadNotificationCount;

    String breakFast = "8:00 AM";
    String lunchTime = "2:00 PM";
    String dinnerTime = "8:00 PM";
    String snacksTime = "5:00 PM";

    private String activityCreatedTimeStamp;
    private final static String FOLDER_PATH = "images/dashboard/cardBgImage/android/";
    private String imageBaseURL;
    private NotificationHelper mNotificationPrescriptionHelper;
    private boolean mIsAppOpenFromLogin;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() != null) {
                if (intent.getAction().equals(NOTIFY)) {

                    String topic = intent.getStringExtra(MQTTService.TOPIC_KEY);

                    if (intent.getBooleanExtra(MQTTService.DELIVERED, false)) {

                        Log.d(TAG, "Delivery Complete");
                        Log.d(TAG + " MSG_ID", intent.getStringExtra(MQTTService.MESSAGE_ID));

                    } else if (topic.equals(TOPIC[MESSAGE_TOPIC])) {

                        // User message
                        CommonMethods.Log(TAG, "User message");
                        MQTTMessage message = intent.getParcelableExtra(MQTTService.MESSAGE);

                        int unreadMessageCount = appDBHelper.unreadMessageCount();
                        setConnectBadgeCount(unreadMessageCount);

                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dashboard_layout);
        ButterKnife.bind(this);
        RescribeApplication.setPreviousUserSelectedLocationInfo(this, null, null);
        mContext = HomePageActivity.this;

        mIsAppOpenFromLogin = getIntent().getBooleanExtra(RescribeConstants.APP_OPENING_FROM_LOGIN, false);

        activityCreatedTimeStamp = CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.YYYY_MM_DD_HH_mm_ss);

        // Pre load

        preLoadBannerImages();

        // Pre load end

        appDBHelper = new AppDBHelper(mContext);
        mDashboardHelper = new DashboardHelper(this, this);

        doConfigureMenuOptions();

        custom_progress_bar.setVisibility(View.VISIBLE);

        createLocationRequest();
        widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        mDashboardDoctorListsToShowDashboardDoctor = new ArrayList<>();
        mDashboardDataBuilder = new ServicesCardViewImpl(this, this);

        String patientId = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext);
        LoginHelper loginHelper = new LoginHelper(mContext, HomePageActivity.this);
        ActiveRequest activeRequest = new ActiveRequest();
        activeRequest.setId(Integer.parseInt(patientId));
        loginHelper.doActiveStatus(activeRequest);
        //------

        boolean need_notify = RescribePreferencesManager.getBoolean(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.NEED_NOTIFY, mContext);

        if (need_notify)
            notificationForMedicine();

        mUpdateAppUnreadNotificationCount = new UpdateAppUnreadNotificationCount();

        registerReceiver(mUpdateAppUnreadNotificationCount, new IntentFilter(getString(R.string.unread_notification_update_received)));
    }

    @SuppressLint("CheckResult")
    private void preLoadBannerImages() {
        String density = CommonMethods.getDeviceResolution(mContext) + "/";
        imageBaseURL = Config.BASE_URL + FOLDER_PATH + density;

        RequestOptions requestOptions1 = new RequestOptions();
        requestOptions1.dontAnimate();
        requestOptions1.signature(new ObjectKey(activityCreatedTimeStamp + imageBaseURL + "myappointments.jpg"));

        Glide.with(mContext)
                .load(imageBaseURL + "myappointments.jpg")
                .apply(requestOptions1)
                .into(preloadView);

        RequestOptions requestOptions2 = new RequestOptions();
        requestOptions2.dontAnimate();
        requestOptions2.signature(new ObjectKey(activityCreatedTimeStamp + imageBaseURL + "sponsored.jpg"));

        Glide.with(mContext)
                .load(imageBaseURL + "sponsored.jpg")
                .apply(requestOptions2)
                .into(preloadView);

        RequestOptions requestOptions3 = new RequestOptions();
        requestOptions3.dontAnimate();
        requestOptions3.signature(new ObjectKey(activityCreatedTimeStamp + imageBaseURL + "recentlyvisited.jpg"));

        Glide.with(mContext)
                .load(imageBaseURL + "recentlyvisited.jpg")
                .apply(requestOptions3)
                .into(preloadView);

        RequestOptions requestOptions4 = new RequestOptions();
        requestOptions4.dontAnimate();
        requestOptions4.signature(new ObjectKey(activityCreatedTimeStamp + imageBaseURL + "favorite.jpg"));

        Glide.with(mContext)
                .load(imageBaseURL + "favorite.jpg")
                .apply(requestOptions4)
                .into(preloadView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        break;
                    case Activity.RESULT_CANCELED:
                        HashMap<String, String> userSelectedLocationInfo = RescribeApplication.getUserSelectedLocationInfo();
                        if (userSelectedLocationInfo.get(mContext.getString(R.string.location)) == null) {
                            String lastCapturedLocation = RescribePreferencesManager.getString(getString(R.string.location), mContext);
                            if (!lastCapturedLocation.isEmpty()) {
                                try {
                                    Double lat = Double.valueOf(RescribePreferencesManager.getString(getString(R.string.latitude), mContext));
                                    Double lng = Double.valueOf(RescribePreferencesManager.getString(getString(R.string.longitude), mContext));
                                    LatLng location = new LatLng(lat, lng);
                                    RescribeApplication.setUserSelectedLocationInfo(mContext, location, lastCapturedLocation);
                                    String[] split = lastCapturedLocation.split(",");
                                    mDashboardHelper.doGetDashboard(split[1]);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                        break;
                }
                break;
        }
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

        Cursor cursor = appDBHelper.getPreferences("1");
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                breakFast = cursor.getString(cursor.getColumnIndex(AppDBHelper.BREAKFAST_TIME));
                lunchTime = cursor.getString(cursor.getColumnIndex(AppDBHelper.LUNCH_TIME));
                dinnerTime = cursor.getString(cursor.getColumnIndex(AppDBHelper.DINNER_TIME));
                snacksTime = cursor.getString(cursor.getColumnIndex(AppDBHelper.SNACKS_TIME));
                cursor.moveToNext();
            }
        }
        cursor.close();

        String times[] = {breakFast, lunchTime, dinnerTime, snacksTime};

        // Set Alarms Again when date changed.
        new DeleteUnreadNotificationAlarmTask(mContext).run();
        new DosesAlarmTask(mContext, times).run();
        new InvestigationAlarmTask(mContext, RescribeConstants.INVESTIGATION_NOTIFICATION_TIME, getResources().getString(R.string.investigation_msg)).run();
        new AppointmentAlarmTask(mContext, RescribeConstants.APPOINTMENT_NOTIFICATION_TIME, getResources().getString(R.string.appointment_msg)).run();

        RescribePreferencesManager.putBoolean(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.NEED_NOTIFY, false, mContext);
    }

    @Override
    public void onBackPressed() {
        if (isOpen)
            closeSheet();
        else
            finishAffinity();
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

                    if (bottomMenus.isEmpty() || bottomSheetMenus.isEmpty())
                        doConfigureMenuOptions();

                    custom_progress_bar.setVisibility(View.GONE);
                }

                if (mIsAppOpenFromLogin) {
                    mIsAppOpenFromLogin = false;
                    doGetMedicationNotificationOnNewLogin();
                }
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
                    //     CommonMethods.showToast(this, responseFavouriteDoctorBaseModel.getCommonRespose().getStatusMessage());
                }
                break;
            case RescribeConstants.TASK_NOTIFICATION:
                doProcessReceivedMedicationNotificationData((NotificationModel) customResponse);

                break;
        }
    }

    private void setUpViewPager() {
        //----------
        //   mCustomProgressDialog.cancel();
        Map<String, Integer> dataMap = new LinkedHashMap<>();
        myAppoint = mDashboardDataBuilder.getCategoryWiseDoctorList(getString(R.string.my_appointments), -1);
        sponsered = mDashboardDataBuilder.getCategoryWiseDoctorList(getString(R.string.sponsored_doctor), -1);
        recently_visit_doctor = mDashboardDataBuilder.getCategoryWiseDoctorList(getString(R.string.recently_visited_doctor), -1);
        favoriteList = mDashboardDataBuilder.getFavouriteDocList(-1);
        dataMap.put(getString(R.string.my_appointments), myAppoint.size());
        dataMap.put(getString(R.string.sponsored_doctor), sponsered.size());
        dataMap.put(getString(R.string.recently_visited_doctor), recently_visit_doctor.size());
        dataMap.put(getString(R.string.favorite), favoriteList.size());

        ArrayList<DoctorList> mergeList = new ArrayList<>();
        ArrayList<String> cardBgImage = new ArrayList<>();

        if (myAppoint.size() > 0) {
            mergeList.add(myAppoint.get(0));
            cardBgImage.add(imageBaseURL + "myappointments.jpg");
        }

        if (sponsered.size() > 0) {
            mergeList.add(sponsered.get(0));
            cardBgImage.add(imageBaseURL + "sponsored.jpg");
        }

        if (recently_visit_doctor.size() > 0) {
            mergeList.add(recently_visit_doctor.get(0));
            cardBgImage.add(imageBaseURL + "recentlyvisited.jpg");
        }

        if (favoriteList.size() > 0) {
            mergeList.add(favoriteList.get(0));
            cardBgImage.add(imageBaseURL + "favorite.jpg");
        }

        //------------
        mDashboardDoctorListsToShowDashboardDoctor.addAll(mergeList);
        ShowDoctorViewPagerAdapter mShowDoctorViewPagerAdapter = new ShowDoctorViewPagerAdapter(this, mergeList, mDashboardDataBuilder, dataMap, this);
        viewPagerDoctorItem.setAdapter(mShowDoctorViewPagerAdapter);
        //-----------
        // Disable clip to padding
        viewPagerDoctorItem.setClipToPadding(false);

        int pager_padding = getResources().getDimensionPixelSize(R.dimen.pager_padding);
        viewPagerDoctorItem.setPadding(pager_padding, 0, pager_padding, 0);
        int pager_margin = getResources().getDimensionPixelSize(R.dimen.pager_margin);
        viewPagerDoctorItem.setPageMargin(pager_margin);

        ShowBackgroundViewPagerAdapter mShowBackgroundViewPagerAdapter = new ShowBackgroundViewPagerAdapter(this, cardBgImage, activityCreatedTimeStamp);
        viewpager.setOffscreenPageLimit(4);
        viewpager.setAdapter(mShowBackgroundViewPagerAdapter);

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
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        if (mOldDataTag.equals(TASK_DASHBOARD_API))
            custom_progress_bar.setVisibility(View.GONE);
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        if (mOldDataTag.equals(TASK_DASHBOARD_API))
            custom_progress_bar.setVisibility(View.GONE);
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        if (mOldDataTag.equals(TASK_DASHBOARD_API))
            custom_progress_bar.setVisibility(View.GONE);
    }


    @OnClick({R.id.locationImageView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.locationImageView:
                Intent start = new Intent(this, BookAppointFindLocation.class);

                start.putExtra(getString(R.string.opening_mode), getString(R.string.home));

                int PLACE_PICKER_REQUEST = 10;

                startActivityForResult(start, PLACE_PICKER_REQUEST);
                break;
        }
    }

    @Override
    public void onClickOfMenu(DashboardMenuList menu) {
        Intent intent = null;
        if (menu.getName().equalsIgnoreCase(getString(R.string.find_doctors))) {
            intent = new Intent(mContext, FindDoctorsActivity.class);
            Bundle b = new Bundle();
            b.putParcelable(getString(R.string.clicked_item_data), menu);
            b.putString(getString(R.string.clicked_item_data_type_value), menu.getName());
            intent.putExtras(b);
        } else if (menu.getName().toLowerCase().startsWith(getString(R.string.on_going_treatment).toLowerCase())) {
            intent = new Intent(mContext, PrescriptionActivity.class);
            Bundle b = new Bundle();
            b.putParcelable(getString(R.string.clicked_item_data), menu);
            b.putString(getString(R.string.clicked_item_data_type_value), menu.getName());
            intent.putExtras(b);
        } else if (menu.getName().equalsIgnoreCase(getString(R.string.health_repository))) {
            intent = new Intent(mContext, HealthRepositoryActivity.class);
            Bundle b = new Bundle();
            b.putParcelable(getString(R.string.clicked_item_data), menu);
            b.putString(getString(R.string.clicked_item_data_type_value), menu.getName());
            intent.putExtras(b);
        } else if (menu.getName().equalsIgnoreCase(getString(R.string.health_offers))) {
            intent = new Intent(mContext, HealthOffersActivity.class);
            Bundle b = new Bundle();
            b.putParcelable(getString(R.string.clicked_item_data), menu);
            b.putString(getString(R.string.clicked_item_data_type_value), menu.getName());
            intent.putExtras(b);
        } else if (menu.getName().equalsIgnoreCase(getString(R.string.health_education))) {
            intent = new Intent(mContext, HealthEducation.class);
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.clicked_item_data), menu.getName());
            intent.putExtras(bundle);
        } else if (menu.getName().equalsIgnoreCase(getString(R.string.health_services))) {
            intent = new Intent(mContext, BookAppointmentServices.class);
            Bundle b = new Bundle();
            b.putParcelable(getString(R.string.clicked_item_data), menu);
            b.putString(getString(R.string.clicked_item_data_type_value), menu.getName());
            intent.putExtras(b);
        }
        if (intent != null)
            startActivity(intent);
    }

    @Override
    public void onBottomMenuClick(BottomMenu bottomMenu) {

        String menuName = bottomMenu.getMenuName();

        if (menuName.equalsIgnoreCase(BOOK)) {
            Intent intent = new Intent(HomePageActivity.this, BookAppointDoctorListBaseActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            Bundle bundle = new Bundle();
            bundle.putString(RescribeConstants.CALL_FROM_DASHBOARD, "");
            bundle.putString(getString(R.string.clicked_item_data), getString(R.string.doctorss));
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (menuName.equalsIgnoreCase(SETTINGS)) {
            Intent intent = new Intent(HomePageActivity.this, SettingsActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);
        } else if (menuName.equalsIgnoreCase(CONNECT)) {
            Intent intent = new Intent(HomePageActivity.this, ConnectSplashActivity.class);
            startActivity(intent);
        }

        super.onBottomMenuClick(bottomMenu);
    }

    @Override
    public void onProfileImageClick() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);

        super.onProfileImageClick();
    }

    private void doConfigureMenuOptions() {

        try {
            InputStream is = mContext.getAssets().open("dashboard_menu.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            mDashboardMenuData = new Gson().fromJson(json, DashboardMenuData.class);

        } catch (IOException ex) {
            ex.printStackTrace();
        }


        int appCount = RescribeApplication.doGetUnreadNotificationCount(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT);
        int invCount = RescribeApplication.doGetUnreadNotificationCount(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT);
        int medCount = RescribeApplication.doGetUnreadNotificationCount(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT);
        // int tokCount = RescribePreferencesManager.getInt(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.TOKEN_ALERT_COUNT, this);

        /*
         -->
         START: Notification count is stored in shared-preferences now,
                check AppDbHelper.insertUnreadReceivedNotificationMessage();
                Chat count is not showing now.
         <--
        */
        //-->END

        ArrayList<DashboardMenuList> dashboardMenuList = mDashboardMenuData.getDashboardMenuList();
        //------- Menus received from server, like find_doc,ongoing_medication : START
        MenuOptionsDashBoardAdapter mMenuOptionsDashBoardAdapter = new MenuOptionsDashBoardAdapter(this, this, dashboardMenuList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mMenuOptionsListView.setLayoutManager(linearLayoutManager);
        mMenuOptionsListView.setNestedScrollingEnabled(false);
        mMenuOptionsListView.setAdapter(mMenuOptionsDashBoardAdapter);
        //------- Menus received from server, like find_doc,ongoing_medication : START

        // add bottom menu : like home,setting,support
        dashboardBottomMenuLists = mDashboardMenuData.getDashboardBottomMenuList();
        for (DashboardBottomMenuList dashboardBottomMenuList : dashboardBottomMenuLists) {
            BottomMenu bottomMenu = new BottomMenu();

            int resourceId = getResources().getIdentifier(dashboardBottomMenuList.getIconImageUrl(), DRAWABLE, BuildConfig.APPLICATION_ID);
            if (resourceId > 0)
                bottomMenu.setMenuIcon(getResources().getDrawable(resourceId));
            else
                CommonMethods.Log(TAG, "Resource does not exist");

            bottomMenu.setMenuName(dashboardBottomMenuList.getName());
            bottomMenu.setAppIcon(dashboardBottomMenuList.getName().equals(APP_LOGO));
            bottomMenu.setSelected(dashboardBottomMenuList.getName().equals(HOME));

            addBottomMenu(bottomMenu);
        }

        // add bottomSheet menu like notification,my_records etc (on clicked of app_logo)
        for (int i = 0; i < dashboardBottomMenuLists.size(); i++) {
            if (dashboardBottomMenuLists.get(i).getName().equals(APP_LOGO)) {

                for (int j = 0; j < dashboardBottomMenuLists.get(i).getClickEvent().getClickOptions().size(); j++) {
                    ClickOption clickOption = dashboardBottomMenuLists.get(i).getClickEvent().getClickOptions().get(j);
                    if (clickOption.getName().equalsIgnoreCase(getString(R.string.profile))) {
                        profileImageString = clickOption.getIconImageUrl();
                    }
                    if (!clickOption.getName().equalsIgnoreCase(getString(R.string.profile))) {
                        BottomSheetMenu bottomSheetMenu = new BottomSheetMenu();
                        bottomSheetMenu.setName(clickOption.getName());

                        int resourceId = getResources().getIdentifier(dashboardBottomMenuLists.get(i).getClickEvent().getClickOptions().get(j).getIconImageUrl(), DRAWABLE, BuildConfig.APPLICATION_ID);
                        if (resourceId > 0)
                            bottomSheetMenu.setIconImageUrl(getResources().getDrawable(resourceId));
                        else
                            CommonMethods.Log(TAG, "Resource does not exist");

                        addBottomSheetMenu(bottomSheetMenu);
                    }
                }
                break;
            }
        }


        String userName = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.USER_NAME, mContext);
        String salutation = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.SALUTATION, mContext);

        String salutationText = "";


        salutationText = SALUTATION[Integer.parseInt(salutation)];

        setUpAdapterForBottomSheet(profileImageString, userName, RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER, mContext), salutationText);

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

        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        if (mCurrentLocation != null) {
            if (!isSameLocation(location)) {
                mCurrentLocation = location;
                updateUI();
            } else mCurrentLocation = location;
        } else {
            mCurrentLocation = location;
            updateUI();
        }
    }

    public boolean isSameLocation(Location location) {
        return getLocation(location.getLatitude(), location.getLongitude()).equalsIgnoreCase(getLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    @Override
    public void onResume() {
        super.onResume();

        registerReceiver(receiver, new IntentFilter(
                MQTTService.NOTIFY));

        checkAndroidVersion();
        if (mDashboardDataModel != null) {
            setUpViewPager();
        }

        int notificationCount = RescribePreferencesManager.getInt(RescribeConstants.NOTIFICATION_COUNT, this);//appCount + invCount + medCount;// + tokCount;
        setBadgeCount(notificationCount);

        int unreadMessageCount = appDBHelper.unreadMessageCount();
        setConnectBadgeCount(unreadMessageCount);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
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

    public String getLocation(double lat, double lng) {
        Geocoder geocoder = new Geocoder(HomePageActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (!addresses.isEmpty()) {
                Address obj = addresses.get(0);
                return getArea(obj) + "," + obj.getLocality();
            } else {
                return "";
            }
        } catch (IOException e) {
            return "";
        }
    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(HomePageActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);

            if (!addresses.isEmpty()) {
                Address obj = addresses.get(0);
                LatLng location = new LatLng(lat, lng);
                mDashboardHelper = new DashboardHelper(this, this);
                String locationString;
                if (obj.getLocality() == null)
                    locationString = getArea(obj);
                else
                    locationString = obj.getSubLocality();

                RescribeApplication.setUserSelectedLocationInfo(mContext, location, locationString + "," + obj.getLocality());
                if (obj.getLocality() != null)
                    mDashboardHelper.doGetDashboard(obj.getLocality());

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
    public void onBottomSheetMenuClick(BottomSheetMenu bottomMenu) {
        if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.vital_graph))) {
            Intent intent = new Intent(this, VitalGraphActivity.class);
            startActivity(intent);
        } else if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.notification) + "s")) {

            Intent intent = new Intent(this, UnreadNotificationMessageActivity.class);
            startActivity(intent);

            RescribePreferencesManager.putInt(RescribeConstants.NOTIFICATION_COUNT, 0, this);
            setBadgeCount(0);

        } else if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.my_records))) {
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
        } else if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.on_going_treatment))) {
            Intent intent = new Intent(mContext, PrescriptionActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.clicked_item_data_type_value), bottomMenu.getName());
            intent.putExtras(bundle);
            startActivity(intent);
        }
        if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.doctor_details))) {
            Intent intent = new Intent(mContext, DoctorListActivity.class);
            startActivity(intent);
        } else if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.my_appointments))) {
            Intent intent = new Intent(mContext, AppointmentActivity.class);
            intent.putExtra(RescribeConstants.CALL_FROM_DASHBOARD, "");
            startActivity(intent);
        } else if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.saved_articles))) {
            Intent intent = new Intent(mContext, SavedArticlesActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.clicked_item_data), bottomMenu.getName());
            intent.putExtras(bundle);
            startActivity(intent);
        }

        super.onBottomSheetMenuClick(bottomMenu);
    }

    private class UpdateAppUnreadNotificationCount extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(getString(R.string.unread_notification_update_received))) {

                int appCount = RescribeApplication.doGetUnreadNotificationCount(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT);
                int invCount = RescribeApplication.doGetUnreadNotificationCount(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT);
                int medCount = RescribeApplication.doGetUnreadNotificationCount(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT);
                // int tokCount = RescribePreferencesManager.getInt(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.TOKEN_ALERT_COUNT, this);

                  /* START: Notification count is stored in shared-preferences now,
                    check AppDbHelper.insertUnreadReceivedNotificationMessage();
                     Chat count is not showing now.
                   */
                int notificationCount = RescribePreferencesManager.getInt(RescribeConstants.NOTIFICATION_COUNT, context);//appCount + invCount + medCount;// + tokCount;
                //-->END

                //--- Update count on App_logo
                for (BottomMenu object :
                        bottomMenus) {
                    if (object.isAppIcon()) {
                        object.setNotificationCount(notificationCount);
                    }
                }
                doNotifyDataSetChanged();
                //--------------- :END
                //---- Update bottom sheet notification_count : START
                ArrayList<BottomSheetMenu> bottomSheetMenus = HomePageActivity.this.bottomSheetMenus;
                for (BottomSheetMenu object :
                        bottomSheetMenus) {
                    if (object.getName().equalsIgnoreCase(getString(R.string.notifications))) {
                        object.setNotificationCount(notificationCount);
                    }
                }


                String userName = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.USER_NAME, mContext);
                String salutation = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.SALUTATION, mContext);

                String salutationText = "";


                    salutationText = SALUTATION[Integer.parseInt(salutation)];

                setUpAdapterForBottomSheet(profileImageString, userName, RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER, mContext), salutationText);

                //--------------------------
                //---- Update bottom sheet notification_count : END
            } else CommonMethods.Log(TAG, "Other Broadcast");
        }
    }

    @Override
    protected void onDestroy() {
        if (mUpdateAppUnreadNotificationCount != null) {
            unregisterReceiver(mUpdateAppUnreadNotificationCount);
            mUpdateAppUnreadNotificationCount = null;
        }

        super.onDestroy();
    }

    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();

        } else {
            doCallDashBoardAPI();
            // write your logic here
        }
    }

    @SuppressLint("NewApi")
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) + ContextCompat
                .checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (this, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                HashMap<String, String> userSelectedLocationInfo = RescribeApplication.getUserSelectedLocationInfo();
                if (userSelectedLocationInfo.get(mContext.getString(R.string.location)) == null) {
                    String lastCapturedLocation = RescribePreferencesManager.getString(getString(R.string.location), mContext);
                    if (!lastCapturedLocation.isEmpty()) {
                        try {
                            Double lat = Double.valueOf(RescribePreferencesManager.getString(getString(R.string.latitude), mContext));
                            Double lng = Double.valueOf(RescribePreferencesManager.getString(getString(R.string.longitude), mContext));
                            LatLng location = new LatLng(lat, lng);
                            RescribeApplication.setUserSelectedLocationInfo(mContext, location, lastCapturedLocation);
                            String[] split = lastCapturedLocation.split(",");
                            mDashboardHelper.doGetDashboard(split[1]);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                    }
                }
                Snackbar.make(this.findViewById(android.R.id.content),
                        "Please Grant Permissions",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestPermissions(
                                        new String[]{Manifest.permission
                                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                                        PERMISSIONS_MULTIPLE_REQUEST);
                            }
                        }).show();
            } else {
                requestPermissions(
                        new String[]{Manifest.permission
                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            doCallDashBoardAPI();
            // write your logic code if permission already granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean accessFinePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean accessCoarsePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (accessCoarsePermission && writeExternalFile && accessFinePermission) {
                        doCallDashBoardAPI(); // write your logic here
                    } else {
                        HashMap<String, String> userSelectedLocationInfo = RescribeApplication.getUserSelectedLocationInfo();
                        if (userSelectedLocationInfo.get(mContext.getString(R.string.location)) == null) {
                            String lastCapturedLocation = RescribePreferencesManager.getString(getString(R.string.location), mContext);
                            if (!lastCapturedLocation.isEmpty()) {
                                try {
                                    Double lat = Double.valueOf(RescribePreferencesManager.getString(getString(R.string.latitude), mContext));
                                    Double lng = Double.valueOf(RescribePreferencesManager.getString(getString(R.string.longitude), mContext));
                                    LatLng location = new LatLng(lat, lng);
                                    RescribeApplication.setUserSelectedLocationInfo(mContext, location, lastCapturedLocation);
                                    String[] split = lastCapturedLocation.split(",");
                                    mDashboardHelper.doGetDashboard(split[1]);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                        Snackbar.make(this.findViewById(android.R.id.content),
                                "Please Grant Permissions",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @SuppressLint("NewApi")
                                    @Override
                                    public void onClick(View v) {
                                        requestPermissions(
                                                new String[]{Manifest.permission
                                                        .WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                                                PERMISSIONS_MULTIPLE_REQUEST);
                                    }
                                }).show();
                    }
                }
                break;
        }
    }

    //-- Medication Prescription notification configuration : START

    private void doGetMedicationNotificationOnNewLogin() {
        //notification api called
        Calendar c = Calendar.getInstance();
        int hour24 = c.get(Calendar.HOUR_OF_DAY);
        String slot = CommonMethods.getMealTime(hour24, this);

        if (!getString(R.string.break_fast).equalsIgnoreCase(slot)) {
            mNotificationPrescriptionHelper = new NotificationHelper(this);
            mNotificationPrescriptionHelper.doGetNotificationList();
        }
    }

    private void doProcessReceivedMedicationNotificationData(NotificationModel prescriptionDataReceived) {

        Calendar c = Calendar.getInstance();
        int hour24 = c.get(Calendar.HOUR_OF_DAY);
        String slot = CommonMethods.getMealTime(hour24, this);
        //--------

        List<NotificationData> notPrescriptionDataList = prescriptionDataReceived.getNotificationPrescriptionModel().getPresriptionNotification();
        if (!notPrescriptionDataList.isEmpty()) {
            ArrayList<Medication> notificationDataList;
            NotificationData notificationDataForHeader = new NotificationData();
            String date = CommonMethods.getCurrentDateTime();
            CommonMethods.Log(TAG, date);
            //Current date and slot data is sorted to show in header of UI
            for (NotificationData notificationD : notPrescriptionDataList) {
                if (notificationD.getPrescriptionDate().equals(CommonMethods.getCurrentDateTime())) {
                    String prescriptionDate = notificationD.getPrescriptionDate();
                    notificationDataList = notificationD.getMedication();
                    notificationDataForHeader.setMedication(notificationDataList);
                    notificationDataForHeader.setPrescriptionDate(prescriptionDate);
                }
            }

            if (getString(R.string.mlunch).equalsIgnoreCase(slot)) {
                // save slot=breakfast for mLunch slot
                doStoreMedicationNotificationInDb(notificationDataForHeader, getString(R.string.break_fast));
            } else if (getString(R.string.msnacks).equalsIgnoreCase(slot)) {
                // save slot=breakfast for Evening(Snacks) firstly
                doStoreMedicationNotificationInDb(notificationDataForHeader, getString(R.string.break_fast));
                //----------------
                // save slot=LUNCH for Evening(Snacks) Secondly
                doStoreMedicationNotificationInDb(notificationDataForHeader, getString(R.string.mlunch));
            } else if (getString(R.string.mdinner).equalsIgnoreCase(slot)) {
                // save slot=breakfast for Dinner firstly
                doStoreMedicationNotificationInDb(notificationDataForHeader, getString(R.string.break_fast));
                //----------------
                // save slot=LUNCH for Dinner Secondly
                doStoreMedicationNotificationInDb(notificationDataForHeader, getString(R.string.mlunch));
                //----------------
                // save slot=Snacks for Dinner thirdly
                doStoreMedicationNotificationInDb(notificationDataForHeader, getString(R.string.msnacks));
            }
        }
    }

    private void doStoreMedicationNotificationInDb(NotificationData notificationDataForHeader, String slot) {

        //----
        NotificationData filteredData = mNotificationPrescriptionHelper.getFilteredData(notificationDataForHeader, slot);
        if (filteredData.getMedication() != null) {
            if (!filteredData.getMedication().isEmpty()) {
                //----

                int notification_id = 0;
                String medicineSlot = null;

                if (slot.equals(getString(R.string.break_fast))) {
                    medicineSlot = getString(R.string.breakfast_medication);
                    notification_id = BREAKFAST_NOTIFICATION_ID;
                } else if (slot.equals(getString(R.string.mlunch))) {
                    medicineSlot = getString(R.string.lunch_medication);
                    notification_id = LUNCH_NOTIFICATION_ID;
                } else if (slot.equals(getString(R.string.msnacks))) {
                    medicineSlot = getString(R.string.snacks_medication);
                    notification_id = EVENING_NOTIFICATION_ID;
                } else if (slot.equals(getString(R.string.mdinner))) {
                    medicineSlot = getString(R.string.dinner_medication);
                    notification_id = DINNER_NOTIFICATION_ID;
                }

                if (medicineSlot != null) {

                    String notificationTime = CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.hh_mm_a);

                    //---- Save notification in db---
                    String timeStamp = CommonMethods.getCurrentDate() + " " + notificationTime;
                    int id = (int) System.currentTimeMillis(); // medication.getMedicineId();

                    String medicationDataDetails = getText(R.string.have_u_taken).toString() + medicineSlot + "?";

                    appDBHelper.insertUnreadReceivedNotificationMessage(String.valueOf(notification_id), RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT, medicationDataDetails, new Gson().toJson(filteredData), timeStamp);
                }
            }
        }
    }
    //-- Medication Prescription notification configuration : END

}
