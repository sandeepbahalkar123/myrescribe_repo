package com.rescribe.ui.activities.book_appointment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.heinrichreimersoftware.materialdrawer.app_logo.BottomSheetMenu;
import com.heinrichreimersoftware.materialdrawer.app_logo.BottomSheetMenuAdapter;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuActivity;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuAdapter;
import com.rescribe.R;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.database.MyRecordsData;
import com.rescribe.model.dashboard_api.DashboardBottomMenuList;
import com.rescribe.model.investigation.Image;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.activities.AppointmentActivity;
import com.rescribe.ui.activities.HomePageActivity;
import com.rescribe.ui.activities.MyRecordsActivity;
import com.rescribe.ui.activities.NotificationActivity;
import com.rescribe.ui.activities.PrescriptionActivity;
import com.rescribe.ui.activities.SelectedRecordsGroupActivity;
import com.rescribe.ui.activities.dashboard.SettingsActivity;
import com.rescribe.ui.activities.dashboard.SupportActivity;
import com.rescribe.ui.activities.doctor.DoctorListActivity;
import com.rescribe.ui.activities.saved_articles.SavedArticles;
import com.rescribe.ui.activities.vital_graph.VitalGraphActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.fragments.book_appointment.DrawerForFilterDoctorBookAppointment;
import com.rescribe.ui.fragments.book_appointment.RecentVisitDoctorFragment;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rescribe.util.RescribeConstants.BOTTOM_MENUS;

/**
 * Created by jeetal on 15/9/17.
 */


public class BookAppointDoctorListBaseActivity extends BottomMenuActivity implements BottomMenuAdapter.OnBottomMenuClickListener, GoogleApiClient.OnConnectionFailedListener, DrawerForFilterDoctorBookAppointment.OnDrawerInteractionListener {


    private static final String TAG = "BookAppointDoctorListBaseActivity";
    @BindView(R.id.bookAppointmentBackButton)
    ImageView bookAppointmentBackButton;
    @BindView(R.id.title)
    CustomTextView mTitleView;
    @BindView(R.id.locationTextView)
    CustomTextView locationTextView;
    @BindView(R.id.showlocation)
    CustomTextView showlocation;
    @BindView(R.id.nav_view)
    FrameLayout mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private RecentVisitDoctorFragment mRecentVisitDoctorFragment;
    private int PLACE_PICKER_REQUEST = 1;
    private HashMap<String, String> mComplaintsUserSearchFor = new HashMap<>();

    private DrawerForFilterDoctorBookAppointment mDrawerLoadedFragment;

    private ArrayList<DashboardBottomMenuList> dashboardBottomMenuLists;
    private Context mContext;
    private AppDBHelper appDBHelper;
    private String profileImageString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appoint_doc_base_list);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        mContext = BookAppointDoctorListBaseActivity.this;
        appDBHelper = new AppDBHelper(mContext);
        if (getIntent().getParcelableArrayListExtra(BOTTOM_MENUS) != null) {
            setBottomMenu();
            bookAppointmentBackButton.setVisibility(View.INVISIBLE);
        } else {
            bookAppointmentBackButton.setVisibility(View.VISIBLE);
        }
        //------
        locationTextView.setVisibility(View.VISIBLE);
        //------
        HashMap<String, String> userSelectedLocationInfo = RescribeApplication.getUserSelectedLocationInfo();
        String locationReceived = userSelectedLocationInfo.get(getString(R.string.location));
        if (locationReceived != null) {
            // locationTextView.setText("" + locationReceived);
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
        //----------
        mDrawerLoadedFragment = DrawerForFilterDoctorBookAppointment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_view, mDrawerLoadedFragment).commit();

    }

    private void setBottomMenu() {

        int appCount = RescribePreferencesManager.getInt(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT, this);
        int invCount = RescribePreferencesManager.getInt(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT, this);
        int medCount = RescribePreferencesManager.getInt(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT, this);
        int tokCount = RescribePreferencesManager.getInt(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.TOKEN_ALERT_COUNT, this);

        int notificationCount = appCount + invCount + medCount + tokCount;

        bottomMenus.clear();
        dashboardBottomMenuLists = getIntent().getParcelableArrayListExtra(BOTTOM_MENUS);
        for (DashboardBottomMenuList dashboardBottomMenuList : dashboardBottomMenuLists) {
            BottomMenu bottomMenu = new BottomMenu();
            bottomMenu.setMenuIcon(dashboardBottomMenuList.getIconImageUrl());
            bottomMenu.setMenuName(dashboardBottomMenuList.getName());
            bottomMenu.setAppIcon(dashboardBottomMenuList.getName().equals(getString(R.string.app_logo)));
            bottomMenu.setSelected(dashboardBottomMenuList.getName().equals(getString(R.string.appointment)));
            bottomMenu.setNotificationCount(notificationCount);
            addBottomMenu(bottomMenu);
        }

        bottomSheetMenus.clear();
        for (int i = 0; i < dashboardBottomMenuLists.size(); i++) {
            if (dashboardBottomMenuLists.get(i).getName().equals(getString(R.string.app_logo))) {

                for (int j = 0; j < dashboardBottomMenuLists.get(i).getClickEvent().getClickOptions().size(); j++) {
                    if (dashboardBottomMenuLists.get(i).getClickEvent().getClickOptions().get(j).getName().equalsIgnoreCase(getString(R.string.profile))) {
                        profileImageString = dashboardBottomMenuLists.get(i).getClickEvent().getClickOptions().get(j).getIconImageUrl();
                    }
                    if (!dashboardBottomMenuLists.get(i).getClickEvent().getClickOptions().get(j).getName().equalsIgnoreCase(getString(R.string.profile))) {
                        BottomSheetMenu bottomSheetMenu = new BottomSheetMenu();
                        bottomSheetMenu.setName(dashboardBottomMenuLists.get(i).getClickEvent().getClickOptions().get(j).getName());
                        bottomSheetMenu.setIconImageUrl(dashboardBottomMenuLists.get(i).getClickEvent().getClickOptions().get(j).getIconImageUrl());
                        bottomSheetMenu.setNotificationCount(notificationCount);

                        //clickEvent.setClickOptions(dashboardBottomMenuLists.get(i).getClickEvent().getClickOptions());
                        addBottomSheetMenu(bottomSheetMenu);
                    }
                }
                break;
            }
        }


        setUpAdapterForBottomSheet(profileImageString, RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.USER_NAME, mContext), RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER, mContext));
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
    public void onBackPressed() {
        super.onBackPressed();
        DoctorDataHelper.setReceivedDoctorServicesModel(null);
    }

    @Override
    public void onBottomMenuClick(BottomMenu bottomMenu) {
        String menuName = bottomMenu.getMenuName();

        if (menuName.equalsIgnoreCase(getString(R.string.home))) {
            Intent intent = new Intent(BookAppointDoctorListBaseActivity.this, HomePageActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);
            finish();


        } else if (menuName.equalsIgnoreCase(getString(R.string.settings))) {
            Intent intent = new Intent(BookAppointDoctorListBaseActivity.this, SettingsActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);
            finish();

        } else if (menuName.equalsIgnoreCase(getString(R.string.support))) {
            Intent intent = new Intent(BookAppointDoctorListBaseActivity.this, SupportActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);
            finish();
        }
        super.onBottomMenuClick(bottomMenu);
    }

    @Override
    public void onProfileImageClick() {

    }

    @Override
    public void onApply(Bundle b, boolean drawerRequired) {
        mDrawerLayout.closeDrawers();
        mRecentVisitDoctorFragment.onApplyClicked(b);
    }

    @Override
    public void onReset(boolean drawerRequired) {

    }

    //TODO: PENDING
    public DrawerLayout getActivityDrawerLayout() {
        return mDrawerLayout;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBottomSheetMenuClick(BottomSheetMenu bottomMenu) {
        if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.vital_graph))) {
            Intent intent = new Intent(this, VitalGraphActivity.class);
            startActivity(intent);
        } else if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.notification) + "s")) {
            AppDBHelper appDBHelper = new AppDBHelper(this);
            Cursor cursor = appDBHelper.getPreferences("1");
            String breakFastTime = "";
            String lunchTime = "";
            String dinnerTime = "";
            String snacksTime = "";

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

            Calendar c = Calendar.getInstance();
            int hour24 = c.get(Calendar.HOUR_OF_DAY);
            int Min = c.get(Calendar.MINUTE);

            String mGetMealTime = CommonMethods.getMealTime(hour24, Min, this);
            Intent intent = new Intent(this, NotificationActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            intent.putExtra(RescribeConstants.DATE, CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.DD_MM_YYYY));

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
        } else if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.on_going_treatment) + "s")) {
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
            startActivity(intent);
        } else if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.saved_articles))) {
            Intent intent = new Intent(mContext, SavedArticles.class);
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.clicked_item_data), bottomMenu.getName());
            intent.putExtras(bundle);
            startActivity(intent);
        }
        super.onBottomSheetMenuClick(bottomMenu);

    }
}
