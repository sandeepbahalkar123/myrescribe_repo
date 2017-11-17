package com.rescribe.ui.activities.dashboard;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;

import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuActivity;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuAdapter;
import com.rescribe.R;
import com.rescribe.model.dashboard_api.DashboardBottomMenuList;
import com.rescribe.ui.activities.HomePageActivity;
import com.rescribe.ui.activities.book_appointment.BookAppointDoctorListBaseActivity;
import com.rescribe.ui.customesViews.switch_button.SwitchButton;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rescribe.util.RescribeConstants.BOTTOM_MENUS;

/**
 * Created by jeetal on 3/11/17.
 */

public class SettingsActivity extends BottomMenuActivity implements BottomMenuAdapter.onBottomMenuClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    ArrayList<DashboardBottomMenuList> dashboardBottomMenuLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        ButterKnife.bind(this);
        initialize();

        dashboardBottomMenuLists = getIntent().getParcelableArrayListExtra(BOTTOM_MENUS);
        if (dashboardBottomMenuLists != null)
            for (DashboardBottomMenuList dashboardBottomMenuList : dashboardBottomMenuLists) {
                BottomMenu bottomMenu = new BottomMenu();
                bottomMenu.setMenuIcon(dashboardBottomMenuList.getImageUrl());
                bottomMenu.setMenuName(dashboardBottomMenuList.getName());
                bottomMenu.setAppIcon(dashboardBottomMenuList.getName().equals(getString(R.string.app_logo)));
                bottomMenu.setSelected(dashboardBottomMenuList.getName().equals(getString(R.string.settings)));
                addBottomMenu(bottomMenu);
            }

    }

    private void initialize() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.settings));
    }

    /*@OnClick({R.id.sb_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.sb_text:

                break;
        }
    }*/

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
        }  else if (menuName.equalsIgnoreCase(getString(R.string.support))) {
            Intent intent = new Intent(this, SupportActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);
            finish();
        } else if (menuName.equalsIgnoreCase(getString(R.string.appointment))) {
            Intent intent = new Intent(this, BookAppointDoctorListBaseActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);
            finish();
        }

    }
}