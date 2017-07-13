package com.myrescribe.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.myrescribe.R;
import com.myrescribe.helpers.database.AppDBHelper;
import com.myrescribe.notification.AppointmentAlarmTask;
import com.myrescribe.notification.DosesAlarmTask;
import com.myrescribe.notification.InvestigationAlarmTask;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

/**
 * Created by jeetal on 28/6/17.
 */

public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = HomePageActivity.this;
        if (getIntent().getBooleanExtra("ALERT", true))
            notificationForMedicine();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void notificationForMedicine() {
        String breakFast = "9:17 AM";
        String lunchTime = "9:19 AM";
        String dinnerTime = "9:21 AM";
        String snacksTime = "9:21 AM";

        AppDBHelper appDBHelper = new AppDBHelper(mContext);
        Cursor cursor = appDBHelper.getPreferences("1");
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                breakFast = cursor.getString(cursor.getColumnIndex(AppDBHelper.BREAKFAST_TIME));
                lunchTime = cursor.getString(cursor.getColumnIndex(AppDBHelper.LUNCH_TIME));
                dinnerTime = cursor.getString(cursor.getColumnIndex(AppDBHelper.DINNER_TIME));
                snacksTime = cursor.getString(cursor.getColumnIndex(AppDBHelper.SNACKS_TIME));
                cursor.moveToNext();
            }
        }
        cursor.close();

        String times[] = {breakFast, lunchTime, dinnerTime, snacksTime};
        String date = CommonMethods.getCurrentTimeStamp(MyRescribeConstants.DD_MM_YYYY);

        new DosesAlarmTask(mContext, times, date).run();
        new InvestigationAlarmTask(mContext, "9:00 am", getResources().getString(R.string.investigation_msg)).run();
        new AppointmentAlarmTask(mContext, "9:00 am", getResources().getString(R.string.appointment_msg)).run();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //   getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.visit_details) {
            Intent intent = new Intent(mContext, ViewDetailsActivity.class);
            startActivity(intent);

        } else if (id == R.id.doctor_details) {
            Intent intent = new Intent(mContext, DoctorListActivity.class);
            startActivity(intent);
        } else if (id == R.id.investigations) {
            Intent intent = new Intent(mContext, InvestigationActivity.class);

            startActivity(intent);
        }/*else  if (id == R.id.appointments) {
            Intent intent = new Intent(mContext, AppoinmentActivity.class);
            startActivity(intent);
<<<<<<< HEAD
        }*/ else if (id == R.id.onGoingMedication) {
            Intent intent = new Intent(HomePageActivity.this, ShowMedicineDoseListActivity.class);
            startActivity(intent);
        } else if (id == R.id.onGoingMedication) {
            Intent intent = new Intent(mContext, ShowMedicineDoseListActivity.class);

            startActivity(intent);
        } else if (id == R.id.logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        MyRescribePreferencesManager.clearSharedPref(mContext);
        Intent intent = new Intent(mContext, LoginMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
