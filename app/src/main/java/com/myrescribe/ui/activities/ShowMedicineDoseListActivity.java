package com.myrescribe.ui.activities;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.myrescribe.R;
import com.myrescribe.adapters.ShowMedicineDoseListAdapter;
import com.myrescribe.helpers.database.AppDBHelper;
import com.myrescribe.helpers.prescription.PrescriptionHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.prescription_response_model.PatientPrescriptionModel;
import com.myrescribe.model.prescription_response_model.PrescriptionData;
import com.myrescribe.notification.AppointmentAlarmTask;
import com.myrescribe.notification.DosesAlarmTask;
import com.myrescribe.notification.InvestigationAlarmTask;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowMedicineDoseListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HelperResponse, View.OnClickListener {

    private ShowMedicineDoseListAdapter mAdapter;
    private final String TAG = "MyRescribe/ShowMedicineDoseListActivity";
    Context mContext;
    private String mGetMealTime;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

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
        bindView();
        doGetPrescriptionList();
        Calendar c = Calendar.getInstance();
        int hour24 = c.get(Calendar.HOUR_OF_DAY);
        int Min = c.get(Calendar.MINUTE);
        mGetMealTime = CommonMethods.getMealTime(hour24, Min, this);
    }

    private void notificationForMedicine() {
        String breakFast = "9:17 AM";
        String lunchTime = "9:19 AM";
        String dinnerTime = "9:21 AM";
        String snacksTime = "9:21 AM";

        AppDBHelper appDBHelper = new AppDBHelper(ShowMedicineDoseListActivity.this);
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

        String times[] = {breakFast, lunchTime, dinnerTime,snacksTime};
        String date = CommonMethods.getCurrentTimeStamp(MyRescribeConstants.DD_MM_YYYY);

        new DosesAlarmTask(ShowMedicineDoseListActivity.this, times, date).run();
        new InvestigationAlarmTask(ShowMedicineDoseListActivity.this, "9:00 am", getResources().getString(R.string.investigation_msg)).run();
        new AppointmentAlarmTask(ShowMedicineDoseListActivity.this, "9:00 am", getResources().getString(R.string.appointment_msg)).run();
    }

    private void initializeVariables() {
        mContext = ShowMedicineDoseListActivity.this;
        notificationForMedicine();
        mPrescriptionHelper = new PrescriptionHelper(this, this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.going_medication));
        mToolbar.setNavigationIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_white_24dp, null));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                onBackPressed();
            }
        });
    }

    private void bindView() {
        mNavigationView.setNavigationItemSelectedListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.main, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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
        return true;
    }

    private void doGetPrescriptionList() {
        mPrescriptionHelper.doGetPrescriptionList();
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equals(MyRescribeConstants.TASK_PRESCRIPTION_LIST)) {
            PatientPrescriptionModel prescriptionDataReceived = (PatientPrescriptionModel) customResponse;

            ArrayList<PrescriptionData> data = prescriptionDataReceived.getData();

            if (data != null) {
                if (data.size() != 0) {
                    mAdapter = new ShowMedicineDoseListAdapter(this, data, false, mGetMealTime);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
          /*  case R.id.backArrow:
                finish();
                break;*/
        }
    }
}
