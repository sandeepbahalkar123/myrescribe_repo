package com.rescribe.ui.activities.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuActivity;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuAdapter;
import com.rescribe.R;
import com.rescribe.adapters.settings.SettingsAdapter;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.model.dashboard_api.ClickEvent;
import com.rescribe.model.dashboard_api.ClickOption;
import com.rescribe.model.dashboard_api.DashboardBottomMenuList;
import com.rescribe.notification.AppointmentAlarmTask;
import com.rescribe.notification.DosesAlarmTask;
import com.rescribe.notification.InvestigationAlarmTask;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.activities.HomePageActivity;
import com.rescribe.ui.activities.LoginSignUpActivity;
import com.rescribe.ui.activities.NotificationSettingActivity;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.RescribeConstants;

import net.gotev.uploadservice.UploadService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rescribe.util.RescribeConstants.BOTTOM_MENUS;

/**
 * Created by jeetal on 3/11/17.
 */

public class SettingsActivity extends BottomMenuActivity implements BottomMenuAdapter.onBottomMenuClickListener, SettingsAdapter.OnClickOofSettingItemListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        ButterKnife.bind(this);


        dashboardBottomMenuLists = getIntent().getParcelableArrayListExtra(BOTTOM_MENUS);
        if (dashboardBottomMenuLists != null)
            for (DashboardBottomMenuList dashboardBottomMenuList : dashboardBottomMenuLists) {
                BottomMenu bottomMenu = new BottomMenu();
                bottomMenu.setMenuIcon(dashboardBottomMenuList.getIconImageUrl());
                bottomMenu.setMenuName(dashboardBottomMenuList.getName());
                bottomMenu.setAppIcon(dashboardBottomMenuList.getName().equals(getString(R.string.app_logo)));

                if (dashboardBottomMenuList.getName().equals(getString(R.string.settings))) {
                    bottomMenu.setSelected(dashboardBottomMenuList.getName().equals(getString(R.string.settings)));
                    mCurrentSelectedBottomMenu = dashboardBottomMenuList;
                }
                addBottomMenu(bottomMenu);
            }

        initialize();
    }

    private void initialize() {
        mContext = SettingsActivity.this;
        appDBHelper = new AppDBHelper(mContext);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.settings));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

    }

    @Override
    public void onClickOfSettingMenuOption(ClickOption clickedOption) {
        //TODO : here 's' is added bcaz API giving notifications as name.
        if (clickedOption.getName().equalsIgnoreCase(getString(R.string.notification) + "s")) {
            Intent intent = new Intent(SettingsActivity.this, NotificationSettingActivity.class);
            Bundle b = new Bundle();
            b.putParcelable(getString(R.string.clicked_item_data), clickedOption);
            intent.putExtras(b);
            startActivity(intent);
        }

    }

    @OnClick({R.id.menuIcon, R.id.logout, R.id.selectMenuLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menuIcon:
                break;
            case R.id.logout:
                break;
            case R.id.selectMenuLayout:
                ///onClick of logout layout
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
                break;
        }
    }
}