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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.rescribe.ui.activities.book_appointment.BookAppointmentServices;
import com.rescribe.ui.activities.dashboard.DashboardShowCategoryNameByListBaseActivity;
import com.rescribe.ui.activities.dashboard.DoctorDescriptionBaseActivity;
import com.rescribe.ui.activities.dashboard.ProfileActivity;
import com.rescribe.ui.activities.dashboard.SettingsActivity;
import com.rescribe.ui.activities.dashboard.SupportActivity;
import com.rescribe.ui.activities.doctor.DoctorListActivity;
import com.rescribe.ui.activities.find_doctors.FindDoctorsActivity;
import com.rescribe.ui.activities.health_repository.HealthRepository;
import com.rescribe.ui.activities.vital_graph.VitalGraphActivity;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;
import net.gotev.uploadservice.UploadService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
public class HomePageActivity extends DrawerActivity implements HelperResponse, MenuOptionsDashBoardAdapter.onMenuListClickListener, ShowDoctorViewPagerAdapter.OnClickOfCardOnDashboard, BottomMenuAdapter.onBottomMenuClickListener {

    private static final long MANAGE_ACCOUNT = 121;
    private static final long ADD_ACCOUNT = 122;
    private static final String TAG = "HomePage";

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.viewPagerDoctorItem)
    ViewPager viewPagerDoctorItem;
    @BindView(R.id.menuOptionsListView)
    RecyclerView mMenuOptionsListView;
    @BindView(R.id.menuIcon)
    ImageView menuIcon;
    private Context mContext;
    private String mGetMealTime;
    String breakFastTime = "";
    String lunchTime = "";
    String dinnerTime = "";
    String snacksTime = "";
    private Toolbar toolbar;
    private AppDBHelper appDBHelper;
    private DashboardHelper mDashboardHelper;
    private MenuOptionsDashBoardAdapter mMenuOptionsDashBoardAdapter;
    private String patientId;
    private LoginHelper loginHelper;
    Calendar c = Calendar.getInstance();
    int hour24 = c.get(Calendar.HOUR_OF_DAY);
    int Min = c.get(Calendar.MINUTE);
    private ShowDoctorViewPagerAdapter mShowDoctorViewPagerAdapter;
    private ShowBackgroundViewPagerAdapter mShowBackgroundViewPagerAdapter;
    DoctorDataHelper mDoctorDataHelper;
    ArrayList<DoctorList> mDashboardDoctorListsToShowDashboardDoctor;
    int mClickedDoctorID;
    private int widthPixels;
    DashboardDataModel mDashboardDataModel;
    ArrayList<DashboardBottomMenuList> dashboardBottomMenuLists;
    private ImageView mCLickedFavDocIDImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dashboard_layout);
        ButterKnife.bind(this);

        widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        mContext = HomePageActivity.this;

        //-------
        mDashboardDoctorListsToShowDashboardDoctor = new ArrayList<>();
        mDashboardHelper = new DashboardHelper(this, this);
        mDashboardHelper.doGetDashboard();
        //------
        HomePageActivityPermissionsDispatcher.getPermissionWithCheck(HomePageActivity.this);
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
                    //Intent popup = new Intent(getApplicationContext(), SettingActivity.class);
                    //     Intent popup = new Intent(getApplicationContext(), BookAppointFindLocation.class);
                    // startActivity(popup);
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

            DashBoardBaseModel mDashboardBaseModel = (DashBoardBaseModel) customResponse;

            mDashboardDataModel = mDashboardBaseModel.getDashboardModel();
            ArrayList<DoctorList> dashboardDoctorListsToShowDashboardDoctor = new ArrayList<>();

            if (mDashboardDataModel != null) {
                //----------
                Map<String, Integer> dataMap = new LinkedHashMap<>();
                ArrayList<DoctorList> myAppoint = mDashboardDataModel.getCategoryWiseDoctorList(getString(R.string.my_appointments));
                ArrayList<DoctorList> sponsered = mDashboardDataModel.getCategoryWiseDoctorList(getString(R.string.sponsored_doctor));
                ArrayList<DoctorList> recently_visit_doctor = mDashboardDataModel.getCategoryWiseDoctorList(getString(R.string.recently_visited_doctor));
                ArrayList<DoctorList> favoriteList = mDashboardDataModel.getFavouriteDocList();

                dataMap.put(getString(R.string.my_appointments), myAppoint.size());
                dataMap.put(getString(R.string.sponsored_doctor), sponsered.size());
                dataMap.put(getString(R.string.recently_visited_doctor), recently_visit_doctor.size());
                dataMap.put(getString(R.string.favorite), favoriteList.size());

                dashboardDoctorListsToShowDashboardDoctor.add(myAppoint.get(0));
                dashboardDoctorListsToShowDashboardDoctor.add(sponsered.get(0));
                dashboardDoctorListsToShowDashboardDoctor.add(recently_visit_doctor.get(0));
                dashboardDoctorListsToShowDashboardDoctor.add(favoriteList.get(0));
                //----------

                mDashboardDoctorListsToShowDashboardDoctor.addAll(dashboardDoctorListsToShowDashboardDoctor);

                mShowDoctorViewPagerAdapter = new ShowDoctorViewPagerAdapter(this, dashboardDoctorListsToShowDashboardDoctor, this, dataMap);
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
                //----------

                mMenuOptionsDashBoardAdapter = new MenuOptionsDashBoardAdapter(this, this, mDashboardDataModel.getDashboardMenuList());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                mMenuOptionsListView.setLayoutManager(linearLayoutManager);
                mMenuOptionsListView.setHasFixedSize(true);
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
                    bottomMenu.setSelected(dashboardBottomMenuList.getName().equals(getString(R.string.app_logo)));

                    addBottomMenu(bottomMenu);
                }

            } else {

            }
            //------------
        } else if (mOldDataTag.equals(RescribeConstants.LOGOUT))
            logout();
        else if (mOldDataTag.equals(ACTIVE_STATUS))
            CommonMethods.Log(ACTIVE_STATUS, "active");
        else if (mOldDataTag.equals(RescribeConstants.TASK_SET_FAVOURITE_DOCTOR)) {
            if (customResponse != null) {
                CommonBaseModelContainer responseFavouriteDoctorBaseModel = (CommonBaseModelContainer) customResponse;
                if (responseFavouriteDoctorBaseModel.getCommonRespose().isSuccess()) {
                    DoctorList doctorListById = mDashboardDataModel.findDoctorListById("" + mClickedDoctorID);
                    boolean status = doctorListById.getFavourite() ? false : true;
                    doctorListById.setFavourite(status);
                    mDashboardDataModel.replaceDoctorListById("" + doctorListById.getDocId(), doctorListById);
                    if (mCLickedFavDocIDImageView != null) {
                        if (doctorListById.getFavourite()) {
                            mCLickedFavDocIDImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dashboard_heart_fav));
                        } else {
                            mCLickedFavDocIDImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.result_line_heart_fav));
                        }
                    }
                }
                CommonMethods.showToast(this, responseFavouriteDoctorBaseModel.getCommonRespose().getStatusMessage());
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
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.menuIcon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menuIcon:
                openDrawer();
                break;
        }
    }

    @Override
    public void onClickOfDashboardDoctorItem(String mDashBoardCardName) {
        if (mDashBoardCardName.equals(getString(R.string.my_appointments))) {
            Intent intent = new Intent(HomePageActivity.this, AppointmentActivity.class);
            startActivity(intent);
        } else if (mDashBoardCardName.equals(getString(R.string.sponsered_doctor))) {
            Intent intent = new Intent(HomePageActivity.this, DoctorDescriptionBaseActivity.class);
            intent.putExtra(getString(R.string.clicked_item_data), mDashboardDataModel.getCategoryWiseDoctorList(getString(R.string.sponsered_doctor)).get(0));
            intent.putExtra(getString(R.string.toolbarTitle), getString(R.string.sponsered_doctor));
            startActivity(intent);
        } else if (mDashBoardCardName.equals(getString(R.string.recently_visit_doctor))) {
            Intent intent = new Intent(HomePageActivity.this, DoctorDescriptionBaseActivity.class);
            intent.putExtra(getString(R.string.clicked_item_data), mDashboardDataModel.getCategoryWiseDoctorList(getString(R.string.recently_visit_doctor)).get(0));
            intent.putExtra(getString(R.string.toolbarTitle), getString(R.string.sponsered_doctor));
            startActivity(intent);
        }
    }

    @Override
    public void onClickOfCount(String nameOfCategoryType) {
        if (nameOfCategoryType.equals(getString(R.string.my_appointments))) {
            Intent intent = new Intent(HomePageActivity.this, DashboardShowCategoryNameByListBaseActivity.class);
            intent.putExtra(getString(R.string.toolbarTitle), getString(R.string.my_appointments));
            intent.putExtra(getString(R.string.clicked_item_data), mDashboardDataModel.getCategoryWiseDoctorList(getString(R.string.my_appointments)));
            startActivity(intent);
        } else if (nameOfCategoryType.equals(getString(R.string.sponsered_doctor))) {
            Intent intent = new Intent(HomePageActivity.this, DashboardShowCategoryNameByListBaseActivity.class);
            intent.putExtra(getString(R.string.toolbarTitle), getString(R.string.sponsered_doctor));
            intent.putExtra(getString(R.string.clicked_item_data), mDashboardDataModel.getCategoryWiseDoctorList(getString(R.string.sponsered_doctor)));
            startActivity(intent);
            /*Intent intent = new Intent(HomePageActivity.this, AppointmentActivity.class);
            startActivity(intent);*/
        } else if (nameOfCategoryType.equals(getString(R.string.recently_visit_doctor))) {
            Intent intent = new Intent(HomePageActivity.this, DashboardShowCategoryNameByListBaseActivity.class);
            intent.putExtra(getString(R.string.toolbarTitle), getString(R.string.recently_visit_doctor));
            intent.putExtra(getString(R.string.clicked_item_data), mDashboardDataModel.getCategoryWiseDoctorList(getString(R.string.recently_visit_doctor)));
            startActivity(intent);
            /*Intent intent = new Intent(HomePageActivity.this, AppointmentActivity.class);
            startActivity(intent);*/
        }
    }

    @Override
    public void onClickOfFavourite(boolean isFavourite, int docId, ImageView favorite) {
        mClickedDoctorID = docId;
        this.mCLickedFavDocIDImageView = favorite;
        mDoctorDataHelper = new DoctorDataHelper(this, this);
        mDoctorDataHelper.setFavouriteDoctor(isFavourite, docId);
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

    @Override
    public void onBottomMenuClick(BottomMenu bottomMenu) {

        String menuName = bottomMenu.getMenuName();

        if (menuName.equalsIgnoreCase(getString(R.string.alerts))) {

            mGetMealTime = CommonMethods.getMealTime(hour24, Min, this);
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

            startActivity(intent);
            finish();

        } else if (menuName.equalsIgnoreCase(getString(R.string.profile))) {
            Intent intent = new Intent(HomePageActivity.this, ProfileActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);
            finish();
        } else if (menuName.equalsIgnoreCase(getString(R.string.settings))) {
            Intent intent = new Intent(HomePageActivity.this, SettingsActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);
            finish();
        } else if (menuName.equalsIgnoreCase(getString(R.string.support))) {
            Intent intent = new Intent(HomePageActivity.this, SupportActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);
            finish();
        }
    }
}
