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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.myrescribe.R;
import com.myrescribe.adapters.ShowMedicineDoseListAdapter;
import com.myrescribe.helpers.database.AppDBHelper;
import com.myrescribe.helpers.prescription.PrescriptionHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.DataObject;
import com.myrescribe.model.prescription_response_model.PatientPrescriptionModel;
import com.myrescribe.model.prescription_response_model.PrescriptionData;
import com.myrescribe.notification.AlarmTask;
import com.myrescribe.util.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowMedicineDoseListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ShowMedicineDoseListAdapter.RowClickListener, HelperResponse {

    private static final String TAG = "ShowMedicineDose";
    private ShowMedicineDoseListAdapter mAdapter;
    private final String LOG = this.getClass().getSimpleName();
    Context mContext;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    @BindView(R.id.recyclerViewShowMedicineDoseList)
    RecyclerView mRecyclerView;

    private PrescriptionHelper mPrescriptionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_prescription_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {

        initializeVariables();
        notificationForMedicine();
        bindView();
        doGetPrescriptionList();
    }

    private void notificationForMedicine() {

        String breakFast = "9:17 AM";
        String lunchTime = "9:19 AM";
        String dinnerTime = "9:21 AM";

        AppDBHelper appDBHelper = new AppDBHelper(ShowMedicineDoseListActivity.this);
        Cursor cursor = appDBHelper.getPreferences("1");
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                breakFast = cursor.getString(cursor.getColumnIndex(AppDBHelper.BREAKFAST_TIME));
                lunchTime = cursor.getString(cursor.getColumnIndex(AppDBHelper.LUNCH_TIME));
                dinnerTime = cursor.getString(cursor.getColumnIndex(AppDBHelper.DINNER_TIME));
                // do what ever you want here
                cursor.moveToNext();
            }
        }
        cursor.close();

        String dates[] = {breakFast, lunchTime, dinnerTime};

        new AlarmTask(ShowMedicineDoseListActivity.this, dates).run();
    }

    private void initializeVariables() {
        mContext = ShowMedicineDoseListActivity.this;
        mPrescriptionHelper = new PrescriptionHelper(this, this);
    }

    private void bindView() {

        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        //  mAdapter = new ShowMedicineDoseListAdapter(ShowMedicineDoseListActivity.this, getDataSet(), false);
        //mAdapter.setMyClickListner(this);
        //mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();
        for (int index = 0; index < 20; index++) {
            DataObject obj = new DataObject("" + index,
                    "" + index);
            results.add(index, obj);
        }
        return results;
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
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

       /* int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
       /* if (id == R.id.nav_camera) {
            // Handle the camera action
        }  */

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onRowClicked(ArrayList<PrescriptionData> dataObjects, int position, View v, String mClickCodes) {

        if (mClickCodes.equals(Constants.CLICK_DELETE)) {
            dataObjects.remove(position);
            mAdapter.notifyItemRemoved(position);
        } else if (mClickCodes.equals(Constants.CLICK_EDIT)) {
            Intent intent = new Intent(mContext, EditPrescription.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void doGetPrescriptionList() {
        //mPrescriptionHelper.doGetPrescriptionList();
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag == Constants.TASK_PRESCRIPTION_LIST) {
            PatientPrescriptionModel prescriptionDataReceived = (PatientPrescriptionModel) customResponse;

            ArrayList<PrescriptionData> data = prescriptionDataReceived.getData();
            if (data != null) {
                if (data.size() != 0) {
                    mAdapter = new ShowMedicineDoseListAdapter(this, data, false);
                    mAdapter.setRowClickListener(this);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

        }
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
}
