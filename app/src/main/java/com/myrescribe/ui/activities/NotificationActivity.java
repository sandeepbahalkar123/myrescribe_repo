package com.myrescribe.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.myrescribe.R;
import com.myrescribe.adapters.NotificationListAdapter;
import com.myrescribe.adapters.ShowMedicineDoseListAdapter;
import com.myrescribe.helpers.prescription.PrescriptionHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.prescription_response_model.PatientPrescriptionModel;
import com.myrescribe.model.prescription_response_model.PrescriptionData;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.util.ArrayList;
import java.util.Calendar;

public class NotificationActivity extends AppCompatActivity implements HelperResponse, ShowMedicineDoseListAdapter.RowClickListener {

    private RecyclerView recycler;
    private NotificationListAdapter mAdapter;
    private PrescriptionHelper mPrescriptionHelper;
    private String mGetMealTime;
    private String medicineSlot;
    private String date;
    private String time;
    private String medicineName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
         medicineSlot = intent.getStringExtra(MyRescribeConstants.MEDICINE_SLOT);
         date = intent.getStringExtra(MyRescribeConstants.DATE);
         time = intent.getStringExtra(MyRescribeConstants.TIME);
         medicineName = intent.getStringExtra(MyRescribeConstants.MEDICINE_NAME);

        doGetPrescriptionList();
    }

    private void doGetPrescriptionList() {
        mPrescriptionHelper = new PrescriptionHelper(this, this);
        mPrescriptionHelper.doGetPrescriptionList();
        Calendar c = Calendar.getInstance();
        int hour24 = c.get(Calendar.HOUR_OF_DAY);
        int Min = c.get(Calendar.MINUTE);
        mGetMealTime = CommonMethods.getMealTime(hour24, Min, this);
    }

    @Override
    public void onRowClicked(ArrayList<PrescriptionData> dataObjects, int position, View v, String mClickCodes) {

        if (mClickCodes.equals(MyRescribeConstants.CLICK_DELETE)) {
            dataObjects.remove(position);
            mAdapter.notifyItemRemoved(position);
        } else if (mClickCodes.equals(MyRescribeConstants.CLICK_EDIT)) {
            Intent intent = new Intent(NotificationActivity.this, EditPrescription.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag == MyRescribeConstants.TASK_PRESCRIPTION_LIST) {
            PatientPrescriptionModel prescriptionDataReceived = (PatientPrescriptionModel) customResponse;

            ArrayList<PrescriptionData> data = prescriptionDataReceived.getData();
            if (data != null) {
                if (data.size() != 0) {

                    recycler = (RecyclerView) findViewById(R.id.recycler);
                    recycler.setLayoutManager(new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false));

                    mAdapter = new NotificationListAdapter(NotificationActivity.this, data, false, mGetMealTime, medicineSlot, date, time, medicineName);
                    mAdapter.setRowClickListener(this);
                    recycler.setAdapter(mAdapter);
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
