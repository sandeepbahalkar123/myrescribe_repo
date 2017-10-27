package com.rescribe.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.heinrichreimersoftware.materialdrawer.DrawerActivity;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.heinrichreimersoftware.materialdrawer.theme.DrawerTheme;
import com.rescribe.R;
import com.rescribe.adapters.dashboard.MenuOptionsDashBoardAdapter;
import com.rescribe.adapters.dashboard.ShowBackgroundViewPagerAdapter;
import com.rescribe.adapters.dashboard.ShowDoctorViewPagerAdapter;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.helpers.dashboard.DashboardHelper;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.login.LoginHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.dashboard.DashboardBaseModel;
import com.rescribe.model.dashboard.DashboardDataModel;
import com.rescribe.model.login.ActiveRequest;
import com.rescribe.notification.AppointmentAlarmTask;
import com.rescribe.notification.DosesAlarmTask;
import com.rescribe.notification.InvestigationAlarmTask;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.activities.book_appointment.BookAppointmentServices;
import com.rescribe.ui.activities.doctor.DoctorListActivity;
import com.rescribe.ui.activities.find_doctors.FindDoctorsActivity;
import com.rescribe.ui.activities.health_repository.HealthRepository;
import com.rescribe.ui.activities.vital_graph.VitalGraphActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import net.gotev.uploadservice.UploadService;

import java.util.Calendar;

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
public class HomePageActivity extends DrawerActivity implements HelperResponse, MenuOptionsDashBoardAdapter.onMenuListClickListener {

    private static final long MANAGE_ACCOUNT = 121;
    private static final long ADD_ACCOUNT = 122;
    private static final String TAG = "HomePage";
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.viewPagerDoctorItem)
    ViewPager viewPagerDoctorItem;
    @BindView(R.id.menuOptionsListView)
    RecyclerView mMenuOptionsListView;
    @BindView(R.id.alertsIcon)
    ImageView alertsIcon;
    @BindView(R.id.alerts)
    CustomTextView alerts;
    @BindView(R.id.alertsLayout)
    LinearLayout alertsLayout;
    @BindView(R.id.profileLayout)
    LinearLayout profileLayout;
    @BindView(R.id.logoLayout)
    LinearLayout logoLayout;
    @BindView(R.id.settingLayout)
    LinearLayout settingLayout;
    @BindView(R.id.supportLayout)
    LinearLayout supportLayout;
    private Context mContext;
    private String mGetMealTime;
    String breakFastTime = "";
    String lunchTime = "";
    String dinnerTime = "";
    String snacksTime = "";
    private Toolbar toolbar;
    private AppDBHelper appDBHelper;
    private DashboardHelper dashboardHelper;
    private MenuOptionsDashBoardAdapter mMenuOptionsDashBoardAdapter;
    private String patientId;
    private LoginHelper loginHelper;
    Calendar c = Calendar.getInstance();
    int hour24 = c.get(Calendar.HOUR_OF_DAY);
    int Min = c.get(Calendar.MINUTE);
    private ShowDoctorViewPagerAdapter mShowDoctorViewPagerAdapter;
    private ShowBackgroundViewPagerAdapter mShowBackgroundViewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dashboard_layout);
        ButterKnife.bind(this);
        String resolution = getImageSizeForPhone();
        dashboardHelper = new DashboardHelper(this, this);
        dashboardHelper.doGetDashboard();
        mContext = HomePageActivity.this;
        HomePageActivityPermissionsDispatcher.getPermissionWithCheck(HomePageActivity.this);
        appDBHelper = new AppDBHelper(mContext);
        patientId = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext);
        loginHelper = new LoginHelper(mContext, HomePageActivity.this);
        ActiveRequest activeRequest = new ActiveRequest();
        activeRequest.setId(Integer.parseInt(patientId));
        loginHelper.doActiveStatus(activeRequest);

        String currentDate = CommonMethods.getCurrentDate();
        String pastDate = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.NOTIFY_DATE, mContext);

        if (!currentDate.equals(pastDate)) {
            if (getIntent().getBooleanExtra(RescribeConstants.ALERT, true))
                notificationForMedicine();
        }
        drawerConfiguration();
    }

    private String getImageSizeForPhone() {
        String resolution = "";
        int density= getResources().getDisplayMetrics().densityDpi;
        switch(density)
        {
            case DisplayMetrics.DENSITY_LOW:
                resolution = "ldpi";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                resolution = "mdpi";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                resolution = "hdpi";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                resolution = "xhdpi";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                resolution = "xxhdpi";
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                resolution = "xxxhdpi";
                break;
        }
        return resolution;
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void getPermission() {
        CommonMethods.Log(TAG, "asked permission");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HomePageActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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
                    Intent popup = new Intent(getApplicationContext(), SettingActivity.class);
                    startActivity(popup);
                    //----------
                } else if (id.equalsIgnoreCase(getString(R.string.services))) {
                    DoctorDataHelper.getUserSelectedLocationInfo().clear();
                    Intent intent = new Intent(mContext, BookAppointmentServices.class);
                    //    Intent intent = new Intent(mContext, DoctorListToBookAppointment.class);
                    startActivity(intent);
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
        if (mOldDataTag.equalsIgnoreCase(TASK_DASHBOARD_API)) {
            DashboardBaseModel dashboardBaseModel = (DashboardBaseModel) customResponse;
            DashboardDataModel dashboardDataModel = dashboardBaseModel.getDashboardDataModel();

            if (dashboardDataModel != null) {

                //----------
                /*menuDashBoardAdapter = new MenuDashBoardAdapter(this, dashboardDataModel.getServicesList());
                menuListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                menuListView.setHasFixedSize(true);
                menuListView.setAdapter(menuDashBoardAdapter);*/
                mShowDoctorViewPagerAdapter = new ShowDoctorViewPagerAdapter(this, dashboardDataModel.getDoctorList());
                viewPagerDoctorItem.setAdapter(mShowDoctorViewPagerAdapter);

                // Disable clip to padding
                viewPagerDoctorItem.setClipToPadding(false);

                int pager_padding = getResources().getDimensionPixelSize(R.dimen.pager_padding);
                viewPagerDoctorItem.setPadding(pager_padding, 0, pager_padding, 0);
                int pager_margin = getResources().getDimensionPixelSize(R.dimen.pager_margin);
                viewPagerDoctorItem.setPageMargin(pager_margin);

                mShowBackgroundViewPagerAdapter = new ShowBackgroundViewPagerAdapter(this, dashboardDataModel.getDoctorList());
                viewpager.setAdapter(mShowBackgroundViewPagerAdapter);
                viewpager.setOffscreenPageLimit(mShowBackgroundViewPagerAdapter.getCount());

                final int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
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
                //----------

                mMenuOptionsDashBoardAdapter = new MenuOptionsDashBoardAdapter(this, this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                mMenuOptionsListView.setLayoutManager(linearLayoutManager);
                mMenuOptionsListView.setHasFixedSize(true);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mMenuOptionsListView.getContext(),
                        linearLayoutManager.getOrientation());
                mMenuOptionsListView.addItemDecoration(dividerItemDecoration);
                mMenuOptionsListView.setAdapter(mMenuOptionsDashBoardAdapter);

               /* LatestVitalReading latestVitalReading = dashboardDataModel.getLatestVitalReading();
                if (latestVitalReading != null) {
                    mLatestReadingVitalName.setText(latestVitalReading.getVitalName() + "-" + latestVitalReading.getVitalValue() + latestVitalReading.getUnit());
                    //------------
                    if (latestVitalReading.getDate() != null) {
                        Date timeStamp = CommonMethods.convertStringToDate(latestVitalReading.getDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(timeStamp);
                        String toDisplay = cal.get(Calendar.DAY_OF_MONTH) + "<sup>" + "" + CommonMethods.getSuffixForNumber(cal.get(Calendar.DAY_OF_MONTH)) + "</sup>" + " " + new SimpleDateFormat("MMM yy").format(cal.getTime());
                        //------
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            mLatestReadingVitalDate.setText(Html.fromHtml(toDisplay, Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            mLatestReadingVitalDate.setText(Html.fromHtml(toDisplay));
                        }
                    }
                }
                //------------
                ArrayList<PendingInvestigationData> pendingInvestigationList = dashboardDataModel.getPendingInvestigationList();
                if (pendingInvestigationList.size() > 2) {
                    mPendingInvestigationItemFirst.setText("" + pendingInvestigationList.get(0).getSpeciality());
                    mPendingInvestigationItemSecond.setText("" + pendingInvestigationList.get(1).getSpeciality());
                }*/

            }

            //------------
        } else if (mOldDataTag.equals(RescribeConstants.LOGOUT))
            logout();
        else if (mOldDataTag.equals(ACTIVE_STATUS))
            CommonMethods.Log(ACTIVE_STATUS, "active");

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

        } else if (menuName.equals(getString(R.string.health_education))) {

        }

    }

    @OnClick({R.id.alertsLayout, R.id.profileLayout, R.id.logoLayout, R.id.settingLayout, R.id.supportLayout, R.id.menuIcon})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.menuIcon:
                openDrawer();
                break;

            case R.id.alertsLayout:
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
                } else if (mGetMealTime.isEmpty()) {
                    Intent intentNotification = new Intent(HomePageActivity.this, NotificationActivity.class);
                    intentNotification.putExtra(RescribeConstants.MEDICINE_SLOT, getString(R.string.dinner_medication));
                    intentNotification.putExtra(RescribeConstants.DATE, CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.DD_MM_YYYY));
                    intentNotification.putExtra(RescribeConstants.TIME, dinnerTime);
                    intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentNotification);
                }
                break;
            case R.id.profileLayout:
                break;
            case R.id.logoLayout:
                break;
            case R.id.settingLayout:
                break;
            case R.id.supportLayout:
                break;
        }
    }
}
