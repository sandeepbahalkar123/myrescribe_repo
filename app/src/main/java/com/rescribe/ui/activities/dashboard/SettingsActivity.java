package com.rescribe.ui.activities.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.heinrichreimersoftware.materialdrawer.app_logo.BottomSheetMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuActivity;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuAdapter;
import com.rescribe.R;
import com.rescribe.adapters.settings.SettingsAdapter;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.database.MyRecordsData;
import com.rescribe.helpers.login.LoginHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.dashboard_api.ClickEvent;
import com.rescribe.model.dashboard_api.ClickOption;
import com.rescribe.model.dashboard_api.DashboardBottomMenuList;
import com.rescribe.model.investigation.Image;
import com.rescribe.model.login.ActiveRequest;
import com.rescribe.notification.AppointmentAlarmTask;
import com.rescribe.notification.DosesAlarmTask;
import com.rescribe.notification.InvestigationAlarmTask;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.activities.AppointmentActivity;
import com.rescribe.ui.activities.HomePageActivity;
import com.rescribe.ui.activities.LoginSignUpActivity;
import com.rescribe.ui.activities.MyRecordsActivity;
import com.rescribe.ui.activities.NotificationActivity;
import com.rescribe.ui.activities.NotificationSettingActivity;
import com.rescribe.ui.activities.PrescriptionActivity;
import com.rescribe.ui.activities.SelectedRecordsGroupActivity;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.activities.doctor.DoctorListActivity;
import com.rescribe.ui.activities.saved_articles.SavedArticles;
import com.rescribe.ui.activities.vital_graph.VitalGraphActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import net.gotev.uploadservice.UploadService;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rescribe.util.RescribeConstants.BOTTOM_MENUS;

/**
 * Created by jeetal on 3/11/17.
 */

public class SettingsActivity extends BottomMenuActivity implements BottomMenuAdapter.OnBottomMenuClickListener, SettingsAdapter.OnClickOofSettingItemListener, HelperResponse {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ArrayList<DashboardBottomMenuList> dashboardBottomMenuLists;
    @BindView(R.id.settingsMenuList)
    RecyclerView settingsMenuList;
    @BindView(R.id.menuIcon)
    ImageView menuIcon;
    @BindView(R.id.logout)
    CustomTextView logout;
    @BindView(R.id.dashboardArrowIcon)
    ImageView dashboardArrowIcon;
    @BindView(R.id.selectMenuLayout)
    RelativeLayout selectMenuLayout;
    private SettingsAdapter mSettingsAdapter;
    private Context mContext;
    private AppDBHelper appDBHelper;

    private DashboardBottomMenuList mCurrentSelectedBottomMenu;
    private String profileImageString;
    private BroadcastReceiver mUpdateAppUnreadNotificationCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        ButterKnife.bind(this);
        dashboardBottomMenuLists = getIntent().getParcelableArrayListExtra(BOTTOM_MENUS);

        int appCount = RescribeApplication.doGetUnreadNotificationCount(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT);
        int invCount = RescribeApplication.doGetUnreadNotificationCount(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT);
        int medCount = RescribeApplication.doGetUnreadNotificationCount(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT);
        int chatCount = RescribeApplication.doGetUnreadNotificationCount(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.CHAT_ALERT_COUNT);
        //int tokCount = RescribePreferencesManager.getInt(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.TOKEN_ALERT_COUNT, this);

        int notificationCount = appCount + invCount + medCount +chatCount;
        if (dashboardBottomMenuLists != null)
            bottomSheetMenus.clear();
        for (DashboardBottomMenuList dashboardBottomMenuList : dashboardBottomMenuLists) {
            BottomMenu bottomMenu = new BottomMenu();
            bottomMenu.setMenuIcon(dashboardBottomMenuList.getIconImageUrl());
            bottomMenu.setMenuName(dashboardBottomMenuList.getName());
            bottomMenu.setAppIcon(dashboardBottomMenuList.getName().equals(getString(R.string.app_logo)));
            bottomMenu.setNotificationCount(notificationCount);

            if (dashboardBottomMenuList.getName().equals(getString(R.string.settings))) {
                bottomMenu.setSelected(dashboardBottomMenuList.getName().equals(getString(R.string.settings)));
                mCurrentSelectedBottomMenu = dashboardBottomMenuList;
            }
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


        initialize();

        mUpdateAppUnreadNotificationCount = new UpdateAppUnreadNotificationCount();

        registerReceiver(mUpdateAppUnreadNotificationCount, new IntentFilter(getString(R.string.unread_notification_update_received)));

    }

    private void initialize() {
        mContext = SettingsActivity.this;
        appDBHelper = new AppDBHelper(mContext);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.settings));

        ClickEvent clickEvent = mCurrentSelectedBottomMenu.getClickEvent();
        if (clickEvent != null) {
            ArrayList<ClickOption> clickOptions = clickEvent.getClickOptions();
            mSettingsAdapter = new SettingsAdapter(this, clickOptions, this);

            LinearLayoutManager linearlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            settingsMenuList.setLayoutManager(linearlayoutManager);
            settingsMenuList.setHasFixedSize(true);
            settingsMenuList.setNestedScrollingEnabled(false);
            settingsMenuList.setAdapter(mSettingsAdapter);
        }


    }

    @Override
    public void onBottomMenuClick(BottomMenu bottomMenu) {

        String menuName = bottomMenu.getMenuName();

        if (menuName.equalsIgnoreCase(getString(R.string.home))) {
            Intent intent = new Intent(this, HomePageActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);
            finish();

        } else if (menuName.equalsIgnoreCase(getString(R.string.profile))) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);
            finish();
        } else if (menuName.equalsIgnoreCase(getString(R.string.support))) {
            Intent intent = new Intent(this, SupportActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);
            finish();
        } else if (menuName.equalsIgnoreCase(getString(R.string.appointment))) {
            Intent intent = new Intent(this, BookAppointDoctorListBaseActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.clicked_item_data), getString(R.string.doctorss));
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }

        super.onBottomMenuClick(bottomMenu);
    }

    @Override
    public void onProfileImageClick() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClickOfSettingMenuOption(com.rescribe.model.dashboard_api.ClickOption clickedOption) {
        //TODO : here 's' is added bcaz API giving notifications as name.
        if (clickedOption.getName().equalsIgnoreCase(getString(R.string.notification) + "s")) {
            Intent intent = new Intent(SettingsActivity.this, NotificationSettingActivity.class);
            Bundle b = new Bundle();
            b.putParcelable(getString(R.string.clicked_item_data), clickedOption);
            intent.putExtras(b);
            startActivity(intent);
        } else if (clickedOption.getName().equalsIgnoreCase(getString(R.string.logout))) {
            ActiveRequest activeRequest = new ActiveRequest();
            String patientId = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext);
            activeRequest.setId(Integer.parseInt(patientId));
            new LoginHelper(this, SettingsActivity.this).doLogout(activeRequest);
            logout();
        }
    }

    @OnClick({R.id.menuIcon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menuIcon:
                break;
        }
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

    @Override
    public void onBottomSheetMenuClick(BottomSheetMenu bottomMenu) {
        if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.vital_graph))) {
            Intent intent = new Intent(this, VitalGraphActivity.class);
            startActivity(intent);

        } else if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.notification) + "s")) {
            Intent intent = new Intent(this, UnreadNotificationMessageActivity.class);
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


    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

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
    protected void onDestroy() {
        if (mUpdateAppUnreadNotificationCount != null) {
            unregisterReceiver(mUpdateAppUnreadNotificationCount);
            mUpdateAppUnreadNotificationCount = null;
        }
        super.onDestroy();
    }

    // TODO : THIS IS EXACLTY COPIED FROM HOMEPAGEACTIVITY.java to update count.
    private class UpdateAppUnreadNotificationCount extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int appCount = RescribeApplication.doGetUnreadNotificationCount(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT);
            int invCount = RescribeApplication.doGetUnreadNotificationCount(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT);
            int medCount = RescribeApplication.doGetUnreadNotificationCount(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT);
            int chatCount = RescribeApplication.doGetUnreadNotificationCount(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.CHAT_ALERT_COUNT);
            // int tokCount = RescribePreferencesManager.getInt(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.TOKEN_ALERT_COUNT, this);

            int notificationCount = appCount + invCount + medCount + chatCount;// + tokCount;
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
            ArrayList<BottomSheetMenu> bottomSheetMenus = SettingsActivity.this.bottomSheetMenus;
            for (BottomSheetMenu object :
                    bottomSheetMenus) {
                if (object.getName().equalsIgnoreCase(getString(R.string.notifications))) {
                    object.setNotificationCount(notificationCount);
                }
            }
            setUpAdapterForBottomSheet(profileImageString, RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.USER_NAME, mContext), RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER, mContext));
            //--------------------------
            //---- Update bottom sheet notification_count : END
        }
    }
}