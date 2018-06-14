package com.rescribe.ui.activities.dashboard;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.heinrichreimersoftware.materialdrawer.app_logo.BottomSheetMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuActivity;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuAdapter;
import com.rescribe.BuildConfig;
import com.rescribe.R;
import com.rescribe.adapters.settings.SettingsAdapter;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.database.MyRecordsData;
import com.rescribe.helpers.login.LoginHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.chat.MQTTMessage;
import com.rescribe.model.dashboard_api.ClickEvent;
import com.rescribe.model.dashboard_api.ClickOption;
import com.rescribe.model.dashboard_api.DashboardBottomMenuList;
import com.rescribe.model.investigation.Image;
import com.rescribe.model.login.ActiveRequest;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.services.MQTTService;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.activities.AppointmentActivity;
import com.rescribe.ui.activities.ConnectSplashActivity;
import com.rescribe.ui.activities.LoginSignUpActivity;
import com.rescribe.ui.activities.MyRecordsActivity;
import com.rescribe.ui.activities.NotificationSettingActivity;
import com.rescribe.ui.activities.PrescriptionActivity;
import com.rescribe.ui.activities.SelectedRecordsGroupActivity;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.activities.doctor.DoctorListActivity;
import com.rescribe.ui.activities.saved_articles.SavedArticlesActivity;
import com.rescribe.ui.activities.vital_graph.VitalGraphActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.customesViews.EditTextWithDeleteButton;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import net.gotev.uploadservice.UploadService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuAdapter.connectIndex;
import static com.rescribe.services.MQTTService.MESSAGE_TOPIC;
import static com.rescribe.services.MQTTService.NOTIFY;
import static com.rescribe.services.MQTTService.TOPIC;
import static com.rescribe.singleton.RescribeApplication.appUnreadNotificationMessageList;
import static com.rescribe.util.RescribeConstants.BOTTOM_MENU.APP_LOGO;
import static com.rescribe.util.RescribeConstants.BOTTOM_MENU.BOOK;
import static com.rescribe.util.RescribeConstants.BOTTOM_MENU.CONNECT;
import static com.rescribe.util.RescribeConstants.BOTTOM_MENU.HOME;
import static com.rescribe.util.RescribeConstants.BOTTOM_MENU.SETTINGS;
import static com.rescribe.util.RescribeConstants.BOTTOM_MENUS;
import static com.rescribe.util.RescribeConstants.DRAWABLE;
import static com.rescribe.util.RescribeConstants.SALUTATION;
import static com.rescribe.util.RescribeConstants.TITLE;

/**
 * Created by jeetal on 3/11/17.
 */

public class SettingsActivity extends BottomMenuActivity implements BottomMenuAdapter.OnBottomMenuClickListener, SettingsAdapter.OnClickOfSettingItemListener, HelperResponse {

    private static final String TAG = "SettingsActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
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
    @BindView(R.id.title)
    CustomTextView title;

    private Context mContext;
    private AppDBHelper appDBHelper;

    ArrayList<DashboardBottomMenuList> dashboardBottomMenuLists;
    private DashboardBottomMenuList mCurrentSelectedBottomMenu;
    private BroadcastReceiver mUpdateAppUnreadNotificationCount = new UpdateAppUnreadNotificationCount();

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
        setContentView(R.layout.settings_layout);
        ButterKnife.bind(this);
        dashboardBottomMenuLists = getIntent().getParcelableArrayListExtra(BOTTOM_MENUS);

        int appCount = RescribeApplication.doGetUnreadNotificationCount(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT);
        int invCount = RescribeApplication.doGetUnreadNotificationCount(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT);
        int medCount = RescribeApplication.doGetUnreadNotificationCount(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT);
        //int tokCount = RescribePreferencesManager.getInt(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.TOKEN_ALERT_COUNT, this);

         /* START: Notification count is stored in shared-preferences now,
                check AppDbHelper.insertUnreadReceivedNotificationMessage();
                Chat count is not showing now.
             */
        int notificationCount = RescribePreferencesManager.getInt(RescribeConstants.NOTIFICATION_COUNT, this);//appCount + invCount + medCount;// + tokCount;
        //END

        if (dashboardBottomMenuLists != null) {

            bottomSheetMenus.clear();
            bottomMenus.clear();

            for (int i = 0; i < dashboardBottomMenuLists.size(); i++) {

                DashboardBottomMenuList dashboardBottomMenuList = dashboardBottomMenuLists.get(i);

                BottomMenu bottomMenu = new BottomMenu();
                int resourceId = getResources().getIdentifier(dashboardBottomMenuList.getIconImageUrl(), DRAWABLE, BuildConfig.APPLICATION_ID);
                if (resourceId > 0)
                    bottomMenu.setMenuIcon(getResources().getDrawable(resourceId));
                else
                    CommonMethods.Log(TAG, "Resource does not exist");
                bottomMenu.setMenuName(dashboardBottomMenuList.getName());
                bottomMenu.setAppIcon(dashboardBottomMenuList.getName().equals(APP_LOGO));
                bottomMenu.setNotificationCount(notificationCount);

                if (dashboardBottomMenuList.getName().equals(SETTINGS)) {
                    bottomMenu.setSelected(dashboardBottomMenuList.getName().equals(SETTINGS));
                    mCurrentSelectedBottomMenu = dashboardBottomMenuList;
                }
                addBottomMenu(bottomMenu);

                if (dashboardBottomMenuLists.get(i).getName().equals(APP_LOGO)) {

                    for (int j = 0; j < dashboardBottomMenuLists.get(i).getClickEvent().getClickOptions().size(); j++) {
                        if (!dashboardBottomMenuLists.get(i).getClickEvent().getClickOptions().get(j).getName().equalsIgnoreCase(getString(R.string.profile))) {
                            BottomSheetMenu bottomSheetMenu = new BottomSheetMenu();
                            bottomSheetMenu.setName(dashboardBottomMenuLists.get(i).getClickEvent().getClickOptions().get(j).getName());

                            int resourceIdProfile = getResources().getIdentifier(dashboardBottomMenuLists.get(i).getClickEvent().getClickOptions().get(j).getIconImageUrl(), DRAWABLE, BuildConfig.APPLICATION_ID);
                            if (resourceIdProfile > 0)
                                bottomSheetMenu.setIconImageUrl(getResources().getDrawable(resourceIdProfile));
                            else
                                CommonMethods.Log(TAG, "Resource does not exist");

                            bottomSheetMenu.setNotificationCount(notificationCount);

                            addBottomSheetMenu(bottomSheetMenu);
                        }
                    }
                } else if (dashboardBottomMenuLists.get(i).getName().equalsIgnoreCase(CONNECT))
                    connectIndex = i;
            }
        }

        String userName = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.USER_NAME, mContext);
        String salutation = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.SALUTATION, mContext);

        String salutationText = "";

            salutationText = SALUTATION[Integer.parseInt(salutation)];

        setUpAdapterForBottomSheet(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PROFILE_PHOTO, mContext), userName, RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER, mContext), salutationText);

        initialize();
    }

    private void initialize() {
        mContext = SettingsActivity.this;
        appDBHelper = new AppDBHelper(mContext);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        title.setText(getString(R.string.settings));

        ClickEvent clickEvent = mCurrentSelectedBottomMenu.getClickEvent();
        if (clickEvent != null) {
            ArrayList<ClickOption> clickOptions = clickEvent.getClickOptions();
            SettingsAdapter mSettingsAdapter = new SettingsAdapter(this, clickOptions, this);

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

        if (menuName.equalsIgnoreCase(HOME)) {
            finish();
        } else if (menuName.equalsIgnoreCase(CONNECT)) {
            Intent intent = new Intent(this, ConnectSplashActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);
            finish();
        } else if (menuName.equalsIgnoreCase(BOOK)) {
            Intent intent = new Intent(this, BookAppointDoctorListBaseActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            Bundle bundle = new Bundle();
            bundle.putString(RescribeConstants.CALL_FROM_DASHBOARD,"");
            bundle.putString(getString(R.string.clicked_item_data), getString(R.string.doctorss));
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }

        super.onBottomMenuClick(bottomMenu);
    }

    @Override
    public void onProfileImageClick() {
//        Intent intent = new Intent(this, ProfileActivity.class);
//        startActivity(intent);

        super.onProfileImageClick();
    }

    @Override
    public void onClickOfSettingMenuOption(ClickOption clickedOption) {
        //TODO : here 's' is added bcaz API giving notifications as name.
        if (clickedOption.getName().equalsIgnoreCase(RescribeConstants.SETTING_MENU.NOTIFICATIONS)) {
            Intent intent = new Intent(SettingsActivity.this, NotificationSettingActivity.class);
            Bundle b = new Bundle();
            b.putParcelable(getString(R.string.clicked_item_data), clickedOption);
            b.putString(getString(R.string.toolbarTitle), clickedOption.getName());
            intent.putExtras(b);
            startActivity(intent);
        } else if (clickedOption.getName().equalsIgnoreCase(RescribeConstants.SETTING_MENU.SUPPORT)) {
            Intent intent = new Intent(SettingsActivity.this, SupportActivity.class);
            intent.putExtra(TITLE, clickedOption.getName());
            startActivity(intent);
        } else if (clickedOption.getName().equalsIgnoreCase(RescribeConstants.SETTING_MENU.LOG_OUT)) {
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
        boolean appointmentAlert = RescribePreferencesManager.getBoolean(mContext.getString(R.string.appointment_alert), mContext);
        boolean investigationAlert = RescribePreferencesManager.getBoolean(mContext.getString(R.string.investigation_alert), mContext);
        boolean medicationAlert = RescribePreferencesManager.getBoolean(mContext.getString(R.string.medication_alert), mContext);
        boolean offersAlert = RescribePreferencesManager.getBoolean(mContext.getString(R.string.offers_alert), mContext);
        boolean allNotifyAlert = RescribePreferencesManager.getBoolean(mContext.getString(R.string.all_notifications), mContext);
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
        RescribePreferencesManager.putBoolean(mContext.getString(R.string.all_notifications), allNotifyAlert, mContext);
        RescribePreferencesManager.putBoolean(mContext.getString(R.string.appointment_alert), appointmentAlert, mContext);
        RescribePreferencesManager.putBoolean(mContext.getString(R.string.investigation_alert), investigationAlert, mContext);
        RescribePreferencesManager.putBoolean(mContext.getString(R.string.medication_alert), medicationAlert, mContext);
        RescribePreferencesManager.putBoolean(mContext.getString(R.string.offers_alert), offersAlert, mContext);
        RescribePreferencesManager.putString(RescribeConstants.GMAIL_LOGIN, gmailLogin, mContext);
        RescribePreferencesManager.putString(RescribeConstants.FACEBOOK_LOGIN, facebookLogin, mContext);
        RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER_GMAIL, mobileNoGmail, mContext);
        RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PASSWORD_GMAIL, passwordGmail, mContext);
        RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER_FACEBOOK, mobileNoFacebook, mContext);
        RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PASSWORD_FACEBOOK, passwordFacebook, mContext);
        RescribePreferencesManager.putString(getString(R.string.logout), "" + 1, mContext);

        // Clear all notification
        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();

        appDBHelper.deleteDatabase();
        appUnreadNotificationMessageList.clear();

        Intent intent = new Intent(mContext, LoginSignUpActivity.class);
        startActivity(intent);
        finishAffinity();
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
            intent.putExtra(RescribeConstants.CALL_FROM_DASHBOARD,"");
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
    protected void onResume() {
        super.onResume();

        registerReceiver(receiver, new IntentFilter(MQTTService.NOTIFY));
        registerReceiver(mUpdateAppUnreadNotificationCount, new IntentFilter(getString(R.string.unread_notification_update_received)));

        int unreadMessageCount = appDBHelper.unreadMessageCount();
        setConnectBadgeCount(unreadMessageCount);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        unregisterReceiver(mUpdateAppUnreadNotificationCount);
    }

    private class UpdateAppUnreadNotificationCount extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals(getString(R.string.unread_notification_update_received))) {
                    int notificationCount = RescribePreferencesManager.getInt(RescribeConstants.NOTIFICATION_COUNT, context);//appCount + invCount + medCount;// + tokCount;
                    setBadgeCount(notificationCount);
                } else CommonMethods.Log(TAG, "Other Broadcast");
            } else CommonMethods.Log(TAG, "Other Broadcast");
        }
    }
}