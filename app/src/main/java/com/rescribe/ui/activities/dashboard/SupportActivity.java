package com.rescribe.ui.activities.dashboard;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.heinrichreimersoftware.materialdrawer.app_logo.BottomSheetMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuActivity;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuAdapter;
import com.rescribe.R;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.database.MyRecordsData;
import com.rescribe.model.dashboard_api.DashboardBottomMenuList;
import com.rescribe.model.investigation.Image;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.activities.AppointmentActivity;
import com.rescribe.ui.activities.MyRecordsActivity;
import com.rescribe.ui.activities.NotificationActivity;
import com.rescribe.ui.activities.PrescriptionActivity;
import com.rescribe.ui.activities.SelectedRecordsGroupActivity;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.activities.doctor.DoctorListActivity;
import com.rescribe.ui.activities.saved_articles.SavedArticles;
import com.rescribe.ui.activities.vital_graph.VitalGraphActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.rescribe.util.RescribeConstants.BOTTOM_MENUS;

/**
 * Created by jeetal on 3/11/17.
 */

@RuntimePermissions
public class SupportActivity extends BottomMenuActivity implements BottomMenuAdapter.OnBottomMenuClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    ArrayList<DashboardBottomMenuList> dashboardBottomMenuLists;
    @BindView(R.id.callTextView)
    CustomTextView callTextView;
    @BindView(R.id.emailtextView)
    CustomTextView emailtextView;
    @BindView(R.id.title)
    CustomTextView title;
    private AppDBHelper appDBHelper;
    private String profileImageString;
    private UpdateAppUnreadNotificationCount mUpdateAppUnreadNotificationCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_base_layout);
        ButterKnife.bind(this);
        initialize();

        int appCount = RescribeApplication.doGetUnreadNotificationCount(this, RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT);
        int invCount = RescribeApplication.doGetUnreadNotificationCount(this, RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT);
        int medCount = RescribeApplication.doGetUnreadNotificationCount(this, RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT);
        int chatCount = RescribeApplication.doGetUnreadNotificationCount(this, RescribePreferencesManager.NOTIFICATION_COUNT_KEY.CHAT_ALERT_COUNT);
        //int tokCount = RescribePreferencesManager.getInt(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.TOKEN_ALERT_COUNT, this);

        int notificationCount = appCount + invCount + medCount +chatCount;
        bottomMenus.clear();
        dashboardBottomMenuLists = getIntent().getParcelableArrayListExtra(BOTTOM_MENUS);
        for (DashboardBottomMenuList dashboardBottomMenuList : dashboardBottomMenuLists) {
            BottomMenu bottomMenu = new BottomMenu();
            bottomMenu.setMenuIcon(dashboardBottomMenuList.getIconImageUrl());
            bottomMenu.setMenuName(dashboardBottomMenuList.getName());
            bottomMenu.setAppIcon(dashboardBottomMenuList.getName().equals(getString(R.string.app_logo)));
            bottomMenu.setSelected(dashboardBottomMenuList.getName().equals(getString(R.string.support)));
            bottomMenu.setNotificationCount(notificationCount);
            addBottomMenu(bottomMenu);
        }
        bottomSheetMenus.clear();
        for (int i = 0; i < dashboardBottomMenuLists.size(); i++) {
            if (dashboardBottomMenuLists.get(i).getName().equals(getString(R.string.app_logo))) {

                for (int j = 0; j < dashboardBottomMenuLists.get(i).getClickEvent().getClickOptions().size(); j++) {
                    if (dashboardBottomMenuLists.get(i).getClickEvent().getClickOptions().get(j).getName().equalsIgnoreCase(getString(R.string.profile))) {
                        profileImageString = dashboardBottomMenuLists.get(i).getClickEvent().getClickOptions().get(j).getIconImageUrl().getUrl();
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

        setUpAdapterForBottomSheet(profileImageString, RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.USER_NAME, this), RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER, this));

        mUpdateAppUnreadNotificationCount = new UpdateAppUnreadNotificationCount();
        registerReceiver(mUpdateAppUnreadNotificationCount, new IntentFilter(getString(R.string.unread_notification_update_received)));

    }

    private void initialize() {
        appDBHelper = new AppDBHelper(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        title.setText(getString(R.string.support));
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    void doCallSupport() {
        callSupport();
    }

    private void callSupport() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:123456789"));
        startActivity(callIntent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SupportActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

    }

    @Override
    public void onBottomMenuClick(BottomMenu bottomMenu) {

        String menuName = bottomMenu.getMenuName();

        if (menuName.equalsIgnoreCase(getString(R.string.alerts))) {

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
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);
            finish();
        } else if (menuName.equalsIgnoreCase(getString(R.string.settings))) {
            Intent intent = new Intent(this, SettingsActivity.class);
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
        } else if (menuName.equalsIgnoreCase(getString(R.string.home))) {
            finish();
        }
        super.onBottomMenuClick(bottomMenu);
    }

    @Override
    public void onProfileImageClick() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);

        super.onProfileImageClick();
    }

    @OnClick({R.id.callTextView, R.id.emailtextView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.callTextView:
                SupportActivityPermissionsDispatcher.doCallSupportWithCheck(this);
                break;
            case R.id.emailtextView:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "your_email"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
                    intent.putExtra(Intent.EXTRA_TEXT, "your_text");
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    //TODO smth
                }

                break;
        }
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
                intent = new Intent(this, MyRecordsActivity.class);
            } else {
                intent = new Intent(this, SelectedRecordsGroupActivity.class);
                intent.putExtra(RescribeConstants.UPLOADING_STATUS, true);
                intent.putExtra(RescribeConstants.VISIT_DATE, myRecordsData.getVisitDate());
                intent.putExtra(RescribeConstants.OPD_ID, myRecordsData.getDocId());
                intent.putExtra(RescribeConstants.DOCTORS_ID, myRecordsData.getDocId());
                intent.putExtra(RescribeConstants.DOCUMENTS, myRecordsData.getImageArrayList());
            }
            startActivity(intent);
        } else if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.on_going_treatment))) {
            Intent intent = new Intent(this, PrescriptionActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.clicked_item_data_type_value), bottomMenu.getName());
            intent.putExtras(bundle);
            startActivity(intent);
        }
        if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.doctor_details))) {
            Intent intent = new Intent(this, DoctorListActivity.class);
            startActivity(intent);
        } else if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.my_appointments))) {
            Intent intent = new Intent(this, AppointmentActivity.class);
            startActivity(intent);
        } else if (bottomMenu.getName().equalsIgnoreCase(getString(R.string.saved_articles))) {
            Intent intent = new Intent(this, SavedArticles.class);
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.clicked_item_data), bottomMenu.getName());
            intent.putExtras(bundle);
            startActivity(intent);
        }

        super.onBottomSheetMenuClick(bottomMenu);
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
            int appCount = RescribeApplication.doGetUnreadNotificationCount(context, RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT);
            int invCount = RescribeApplication.doGetUnreadNotificationCount(context, RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT);
            int medCount = RescribeApplication.doGetUnreadNotificationCount(context, RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT);
            int chatCount = RescribeApplication.doGetUnreadNotificationCount(context, RescribePreferencesManager.NOTIFICATION_COUNT_KEY.CHAT_ALERT_COUNT);
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
            ArrayList<BottomSheetMenu> bottomSheetMenus = SupportActivity.this.bottomSheetMenus;
            for (BottomSheetMenu object :
                    bottomSheetMenus) {
                if (object.getName().equalsIgnoreCase(getString(R.string.notifications))) {
                    object.setNotificationCount(notificationCount);
                }
            }
            setUpAdapterForBottomSheet(profileImageString, RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.USER_NAME, context), RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER, context));
            //--------------------------
            //---- Update bottom sheet notification_count : END
        }
    }
}
