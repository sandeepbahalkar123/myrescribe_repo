package com.rescribe.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rescribe.R;
import com.rescribe.adapters.PrescriptionListAdapter;
import com.rescribe.helpers.prescription.PrescriptionHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.dashboard_api.DashboardMenuList;
import com.rescribe.model.prescription_response_model.PrescriptionBaseModel;
import com.rescribe.model.prescription_response_model.PrescriptionData;
import com.rescribe.model.prescription_response_model.PrescriptionModel;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrescriptionActivity extends AppCompatActivity
        implements HelperResponse, View.OnClickListener {

    @BindView(R.id.title)
    CustomTextView title;
    private final String TAG = this.getClass().getName();
    private Context mContext;
    private String mGetMealTime;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerViewShowMedicineDoseList)
    RecyclerView mRecyclerView;
    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
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
        Calendar c = Calendar.getInstance();
        int hour24 = c.get(Calendar.HOUR_OF_DAY);
        int Min = c.get(Calendar.MINUTE);
        mGetMealTime = CommonMethods.getMealTimeForPrescription(hour24, Min, this);
        doGetPrescriptionList();
    }

    private void initializeVariables() {
        // prescription given to particular user is fetched through below api
        mContext = PrescriptionActivity.this;
        mPrescriptionHelper = new PrescriptionHelper(this, this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            DashboardMenuList mReceivedDashboardMenuListData = extras.getParcelable(RescribeConstants.ITEM_DATA);
            String value = extras.getString(RescribeConstants.ITEM_DATA_VALUE);

            if (mReceivedDashboardMenuListData != null)
                title.setText(mReceivedDashboardMenuListData.getName());
            else if (value != null)
                title.setText(value);

        }
        mToolbar.setNavigationIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_white_24dp, null));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void bindView() {
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


    private void doGetPrescriptionList() {
        mPrescriptionHelper.doGetPrescriptionList();
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equals(RescribeConstants.TASK_PRESCRIPTION_LIST)) {
            PrescriptionBaseModel prescriptionBaseModel = (PrescriptionBaseModel) customResponse;
            PrescriptionData dataReceived = prescriptionBaseModel.getData();

            if (!dataReceived.getPrescriptionModels().isEmpty()) {
                mRecyclerView.setVisibility(View.VISIBLE);
                emptyListView.setVisibility(View.GONE);
            } else {
                mRecyclerView.setVisibility(View.GONE);
                emptyListView.setVisibility(View.VISIBLE);
            }

            List<PrescriptionModel> data = dataReceived.getPrescriptionModels();
            // Mealtime is set here because according to mealtime doseage is highlighted in UI
            if (data != null) {
                if (data.size() != 0) {
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).setMealTime(mGetMealTime);
                    }

                    PrescriptionListAdapter prescriptionListAdapter = new PrescriptionListAdapter(this, data);
                    mRecyclerView.setAdapter(prescriptionListAdapter);
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
        emptyListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
    }
}
