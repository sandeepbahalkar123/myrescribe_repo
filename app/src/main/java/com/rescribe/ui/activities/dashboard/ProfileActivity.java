package com.rescribe.ui.activities.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuActivity;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuAdapter;
import com.rescribe.R;
import com.rescribe.model.dashboard_api.DashboardBottomMenuList;
import com.rescribe.ui.activities.HomePageActivity;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rescribe.util.RescribeConstants.BOTTOM_MENUS;

/**
 * Created by jeetal on 3/11/17.
 */

public class ProfileActivity extends BottomMenuActivity implements BottomMenuAdapter.onBottomMenuClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    ArrayList<DashboardBottomMenuList> dashboardBottomMenuLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        ButterKnife.bind(this);
        initialize();

        dashboardBottomMenuLists = getIntent().getParcelableArrayListExtra(BOTTOM_MENUS);
        for (DashboardBottomMenuList dashboardBottomMenuList : dashboardBottomMenuLists) {
            BottomMenu bottomMenu = new BottomMenu();
            bottomMenu.setMenuIcon(dashboardBottomMenuList.getIconImageUrl());
            bottomMenu.setMenuName(dashboardBottomMenuList.getName());
            bottomMenu.setAppIcon(dashboardBottomMenuList.getName().equals(getString(R.string.app_logo)));
            bottomMenu.setSelected(dashboardBottomMenuList.getName().equals(getString(R.string.profile)));
            addBottomMenu(bottomMenu);
        }

    }

    private void initialize() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.profile));
    }

    @Override
    public void onBottomMenuClick(BottomMenu bottomMenu) {

        String menuName = bottomMenu.getMenuName();

      if (menuName.equalsIgnoreCase(getString(R.string.settings))) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);
            finish();
        } else if (menuName.equalsIgnoreCase(getString(R.string.support))) {
            Intent intent = new Intent(this, SupportActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);
            finish();
        }else if (menuName.equalsIgnoreCase(getString(R.string.appointment))) {
            Intent intent = new Intent(this, BookAppointDoctorListBaseActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
          Bundle bundle = new Bundle();
          bundle.putString(getString(R.string.clicked_item_data), getString(R.string.doctorss));
          intent.putExtras(bundle);
            startActivity(intent);
            finish();
        } else if (menuName.equalsIgnoreCase(getString(R.string.home))) {

            Intent intent = new Intent(this, HomePageActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);
            finish();

        }
    }
}
