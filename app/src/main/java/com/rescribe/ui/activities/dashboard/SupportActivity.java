package com.rescribe.ui.activities.dashboard;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuActivity;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuAdapter;
import com.rescribe.R;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.model.dashboard_api.DashboardBottomMenuList;
import com.rescribe.ui.activities.NotificationActivity;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rescribe.util.RescribeConstants.BOTTOM_MENUS;

/**
 * Created by jeetal on 3/11/17.
 */

public class SupportActivity extends BottomMenuActivity implements BottomMenuAdapter.onBottomMenuClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    ArrayList<DashboardBottomMenuList> dashboardBottomMenuLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_layout);
        ButterKnife.bind(this);
        initialize();

        dashboardBottomMenuLists = getIntent().getParcelableArrayListExtra(BOTTOM_MENUS);
        for (DashboardBottomMenuList dashboardBottomMenuList : dashboardBottomMenuLists) {
            BottomMenu bottomMenu = new BottomMenu();
            bottomMenu.setMenuIcon(dashboardBottomMenuList.getImageUrl());
            bottomMenu.setMenuName(dashboardBottomMenuList.getName());
            bottomMenu.setAppIcon(dashboardBottomMenuList.getName().equals(getString(R.string.app_logo)));
            bottomMenu.setSelected(dashboardBottomMenuList.getName().equals(getString(R.string.support)));
            addBottomMenu(bottomMenu);
        }

    }

    private void initialize() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.support));
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
        }
    }
}
