package com.myrescribe.ui.activities;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.adapters.ShowMedicineDoseListAdapter;
import com.myrescribe.helpers.database.AppDBHelper;
import com.myrescribe.helpers.prescription.PrescriptionHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.prescription_response_model.PatientPrescriptionModel;
import com.myrescribe.model.prescription_response_model.PrescriptionData;
import com.myrescribe.notification.AlarmTask;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowMedicineDoseListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HelperResponse ,View.OnClickListener{

    private ShowMedicineDoseListAdapter mAdapter;
    private final String TAG = "MyRescribe/ShowMedicineDoseListActivity";
    Context mContext;
    private  String mGetMealTime;

    public Boolean getIsclicked() {
        return isclicked;
    }

    public void setIsclicked(Boolean isclicked) {
        this.isclicked = isclicked;
    }

    private Boolean isclicked = false;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.searchByPatientName)
    EditText mSearchByPatientName;

    @BindView(R.id.showAppName)
    TextView mShowAppName;

    @BindView(R.id.search)
    ImageView mSearchIcon;


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
        onTextChanged();
        doGetPrescriptionList();

        Calendar c = Calendar.getInstance();
        int hour24 = c.get(Calendar.HOUR_OF_DAY);
        int Min = c.get(Calendar.MINUTE);
       mGetMealTime = CommonMethods.getMealTime(hour24, Min, this);
    }

    private void onTextChanged() {
        mSearchByPatientName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            String  newText =  mSearchByPatientName.getText().toString();
                if (TextUtils.isEmpty(newText)) {
                    mAdapter.filter("");
                } else {
                    mAdapter.filter(newText);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                String  newText1 =  mSearchByPatientName.getText().toString();
                if (TextUtils.isEmpty(newText1)) {
                    mAdapter.filter("");
                } else {
                    mAdapter.filter(newText1);
                }

            }
        });
    }

    private void notificationForMedicine() {

        String breakFast = "9:17 AM";
        String lunchTime = "9:19 AM";
        String dinnerTime = "9:21 AM";

        AppDBHelper appDBHelper = new AppDBHelper(ShowMedicineDoseListActivity.this);
        appDBHelper = AppDBHelper.getInstance(ShowMedicineDoseListActivity.this);
        Cursor cursor = appDBHelper.getPreferences("1");
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                breakFast = cursor.getString(cursor.getColumnIndex(AppDBHelper.BREAKFAST_TIME));
                lunchTime = cursor.getString(cursor.getColumnIndex(AppDBHelper.LUNCH_TIME));
                dinnerTime = cursor.getString(cursor.getColumnIndex(AppDBHelper.DINNER_TIME));
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

        //setSupportActionBar(mToolbar);

       /* ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back));*/
        mNavigationView.setNavigationItemSelectedListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mSearchIcon.setOnClickListener(this);




    }

    @Override
    public void onBackPressed() {
         super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Search Function to search particular prescribed medicine
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.main, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    mAdapter.filter("");
                } else {
                    mAdapter.filter(newText);
                }
                return true;
            }
        });

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
        if (mOldDataTag == MyRescribeConstants.TASK_PRESCRIPTION_LIST) {
            PatientPrescriptionModel prescriptionDataReceived = (PatientPrescriptionModel) customResponse;

            ArrayList<PrescriptionData> data = prescriptionDataReceived.getData();
            if (data != null) {
                if (data.size() != 0) {
                    mAdapter = new ShowMedicineDoseListAdapter(this, data, false,mGetMealTime);
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
            //onclick on floating button
            case R.id.search:
                if(!isclicked) {
                    mSearchByPatientName.setVisibility(View.VISIBLE);
                    mShowAppName.setVisibility(View.GONE);
                    mSearchIcon.setImageResource(R.drawable.cross);
                    setIsclicked(true);
                }else
                {
                    mSearchByPatientName.setVisibility(View.GONE);
                    mShowAppName.setVisibility(View.VISIBLE);
                    mSearchIcon.setImageResource(R.mipmap.search);
                    mSearchByPatientName.setText("");
                    setIsclicked(false);
                }
                break;
        }

    }
}
