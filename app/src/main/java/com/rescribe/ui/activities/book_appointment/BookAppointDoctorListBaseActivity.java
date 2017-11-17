package com.rescribe.ui.activities.book_appointment;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuActivity;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuAdapter;
import com.rescribe.R;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.model.book_appointment.doctor_data.BookAppointmentBaseModel;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.dashboard_api.DashboardBottomMenuList;
import com.rescribe.ui.activities.HomePageActivity;
import com.rescribe.ui.activities.dashboard.SettingsActivity;
import com.rescribe.ui.activities.dashboard.SupportActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.fragments.book_appointment.RecentVisitDoctorFragment;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.rescribe.util.RescribeConstants.BOTTOM_MENUS;

/**
 * Created by jeetal on 15/9/17.
 */

public class BookAppointDoctorListBaseActivity extends BottomMenuActivity implements BottomMenuAdapter.onBottomMenuClickListener {

    private static final String TAG = "BookAppointDoctorListBaseActivity";
    @BindView(R.id.bookAppointmentBackButton)
    ImageView bookAppointmentBackButton;

    CustomTextView title;
    CustomTextView locationTextView;
    CustomTextView showlocation;

    @BindView(R.id.nav_view)
    FrameLayout mNavView;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    private DoctorDataHelper mDoctorDataHelper;
    private Fragment mCurrentlyLoadedFragment; //TODO, fragmentById is not working hence hold this object.
    private BookAppointmentBaseModel mReceivedBookAppointmentBaseModel;
    private BookAppointmentBaseModel mPreviousReqReceivedBookAppointmentBaseModel;
    private FragmentManager mSupportFragmentManager;
    private Fragment mDrawerLoadedFragment;
    private boolean isLocationChange = false;
    private RecentVisitDoctorFragment mRecentVisitDoctorFragment;
    private int PLACE_PICKER_REQUEST = 1;
    private HashMap<String, String> mComplaintsUserSearchFor = new HashMap<>();
    private ArrayList<DashboardBottomMenuList> dashboardBottomMenuLists;
    //-----

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appoint_doc_base_list);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        if(getIntent().getParcelableArrayListExtra(BOTTOM_MENUS)!=null) {
            dashboardBottomMenuLists = getIntent().getParcelableArrayListExtra(BOTTOM_MENUS);
            for (DashboardBottomMenuList dashboardBottomMenuList : dashboardBottomMenuLists) {
                BottomMenu bottomMenu = new BottomMenu();
                bottomMenu.setMenuIcon(dashboardBottomMenuList.getImageUrl());
                bottomMenu.setMenuName(dashboardBottomMenuList.getName());
                bottomMenu.setAppIcon(dashboardBottomMenuList.getName().equals(getString(R.string.app_logo)));
                bottomMenu.setSelected(dashboardBottomMenuList.getName().equals(getString(R.string.appointment)));
                addBottomMenu(bottomMenu);
            }
        }

        title = (CustomTextView) findViewById(R.id.title);
        locationTextView = (CustomTextView) findViewById(R.id.locationTextView);
        locationTextView.setVisibility(View.GONE);
        showlocation = (CustomTextView) findViewById(R.id.showlocation);

        //------
        String locationReceived = "";
        Intent intent = getIntent();
        String title = "";
        if (intent != null) {
            HashMap<String, String> userSelectedLocationInfo = DoctorDataHelper.getUserSelectedLocationInfo();
            locationReceived = userSelectedLocationInfo.get(getString(R.string.location));
            //locationTextView.setText("" + locationReceived);
            title = intent.getStringExtra(getString(R.string.clicked_item_data));
        }
        //------
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.title), title);
        mRecentVisitDoctorFragment = RecentVisitDoctorFragment.newInstance(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.viewContainer, mRecentVisitDoctorFragment).commit();

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






    //TODO: PENDING
    public DrawerLayout getActivityDrawerLayout() {
        return null;
//        return mDrawerLayout;
    }



    @Override
    public void onBottomMenuClick(BottomMenu bottomMenu) {
        String menuName = bottomMenu.getMenuName();

        if (menuName.equalsIgnoreCase(getString(R.string.home))) {

            Intent intent = new Intent(BookAppointDoctorListBaseActivity.this, HomePageActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);
            finish();

        }  else if (menuName.equalsIgnoreCase(getString(R.string.settings))) {
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
}
