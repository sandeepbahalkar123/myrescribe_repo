package com.rescribe.ui.activities.book_appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.heinrichreimersoftware.materialdrawer.app_logo.BottomSheetMenuAdapter;
import com.heinrichreimersoftware.materialdrawer.app_logo.BottomSheetMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuActivity;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuAdapter;
import com.rescribe.R;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.model.dashboard_api.DashboardBottomMenuList;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.activities.HomePageActivity;
import com.rescribe.ui.activities.dashboard.SettingsActivity;
import com.rescribe.ui.activities.dashboard.SupportActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.fragments.book_appointment.DrawerForFilterDoctorBookAppointment;
import com.rescribe.ui.fragments.book_appointment.RecentVisitDoctorFragment;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rescribe.util.RescribeConstants.BOTTOM_MENUS;

/**
 * Created by jeetal on 15/9/17.
 */

public class BookAppointDoctorListBaseActivity extends BottomMenuActivity implements BottomSheetMenuAdapter.onBottomSheetMenuClickListener,BottomMenuAdapter.onBottomMenuClickListener, GoogleApiClient.OnConnectionFailedListener, DrawerForFilterDoctorBookAppointment.OnDrawerInteractionListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appoint_doc_base_list);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {

        if (getIntent().getParcelableArrayListExtra(BOTTOM_MENUS) != null) {
            dashboardBottomMenuLists = getIntent().getParcelableArrayListExtra(BOTTOM_MENUS);
            for (DashboardBottomMenuList dashboardBottomMenuList : dashboardBottomMenuLists) {
                BottomMenu bottomMenu = new BottomMenu();
                bottomMenu.setMenuIcon(dashboardBottomMenuList.getIconImageUrl());
                bottomMenu.setMenuName(dashboardBottomMenuList.getName());
                bottomMenu.setAppIcon(dashboardBottomMenuList.getName().equals(getString(R.string.app_logo)));
                bottomMenu.setSelected(dashboardBottomMenuList.getName().equals(getString(R.string.appointment)));
                addBottomMenu(bottomMenu);
            }
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

    }
}
