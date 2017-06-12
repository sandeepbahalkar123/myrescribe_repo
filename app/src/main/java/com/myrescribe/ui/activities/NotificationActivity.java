package com.myrescribe.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.adapters.NotificationListAdapter;
import com.myrescribe.helpers.database.AppDBHelper;
import com.myrescribe.helpers.prescription.PrescriptionHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.Medicine;
import com.myrescribe.model.prescription_response_model.PatientPrescriptionModel;
import com.myrescribe.model.prescription_response_model.PrescriptionData;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity implements HelperResponse, NotificationListAdapter.OnHeaderClickListener{

    private RecyclerView recycler;
    private NotificationListAdapter mAdapter;
    private String medicineSlot;
    private String date;
    private String time;
    private ArrayList<Medicine> medicines;
    private Context mContext;
    private boolean isHeaderExpand = true;
    private LinearLayout tabletListLayout;
    private CheckBox selectView;
    private ImageView trangleIconTop;
    private ImageView trangleIconBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = NotificationActivity.this;

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        medicineSlot = intent.getStringExtra(MyRescribeConstants.MEDICINE_SLOT);
        date = intent.getStringExtra(MyRescribeConstants.DATE);
        time = intent.getStringExtra(MyRescribeConstants.TIME);
        medicines = (ArrayList<Medicine>) intent.getBundleExtra(MyRescribeConstants.MEDICINE_NAME).getSerializable(MyRescribeConstants.MEDICINE_NAME);

        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setHasFixedSize(false);
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        doGetPrescriptionList();
    }

    // Added Header

    private void addHeader(final ArrayList<PrescriptionData> data) {

        final LinearLayout headerLayout = (LinearLayout) findViewById(R.id.headerLayout);

        TextView slotTextView = (TextView) findViewById(R.id.slotTextView);
        TextView timeTextView = (TextView) findViewById(R.id.timeTextView);
        TextView dateTextView = (TextView) findViewById(R.id.dateTextView);

        slotTextView.setText(medicineSlot);
        timeTextView.setText(time);
        dateTextView.setText(date);

        tabletListLayout = (LinearLayout) findViewById(R.id.tabletListLayout);
        selectView = (CheckBox) findViewById(R.id.selectView);
        trangleIconBottom = (ImageView) findViewById(R.id.trangleIconBottom);
        trangleIconTop = (ImageView) findViewById(R.id.trangleIconTop);

        addHeaderTabletView(tabletListLayout, medicines, data);

        tabletListLayout.setVisibility(View.VISIBLE);
        selectView.setVisibility(View.INVISIBLE);
        trangleIconBottom.setVisibility(View.INVISIBLE);
        trangleIconTop.setVisibility(View.VISIBLE);

        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHeaderExpand) {
                    tabletListLayout.setVisibility(View.GONE);
                    selectView.setVisibility(View.VISIBLE);
                    trangleIconBottom.setVisibility(View.VISIBLE);
                    trangleIconTop.setVisibility(View.INVISIBLE);
                    isHeaderExpand = false;
                } else {
                    tabletListLayout.setVisibility(View.VISIBLE);
                    selectView.setVisibility(View.INVISIBLE);
                    trangleIconBottom.setVisibility(View.INVISIBLE);
                    trangleIconTop.setVisibility(View.VISIBLE);
                    isHeaderExpand = true;
                    mAdapter.collapseAll();
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void addHeaderTabletView(final ViewGroup parent, final ArrayList<Medicine> medicines, ArrayList<PrescriptionData> data) {
        for (int i = 0; i < medicines.size(); i++) {

            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.tablet_list, parent, false);

            CheckBox selectViewTab = (CheckBox) view.findViewById(R.id.selectViewTab);
            ImageView tabTypeView = (ImageView) view.findViewById(R.id.tabTypeView);
            TextView tabNameTextView = (TextView) view.findViewById(R.id.tabNameTextView);
            TextView tabCountTextView = (TextView) view.findViewById(R.id.tabCountTextView);

            tabTypeView.setImageDrawable(CommonMethods.getMedicalTypeIcon(medicines.get(i).getMedicineType(), mContext));

            tabNameTextView.setText(medicines.get(i).getMedicineName());

            selectViewTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    CommonMethods.showToast(mContext, "Checked in " + position + " " + selectViewTab.isChecked());
                }
            });

            if (mContext.getResources().getString(R.string.breakfast_medication).equals(medicineSlot)) {
                if (!data.get(i).getMorningA().isEmpty() || !data.get(i).getMorningB().isEmpty()) {
                    setDose(tabCountTextView, data.get(i).getMorningA() + data.get(i).getMorningB(), medicines.get(i));
                    parent.addView(view);
                }
            } else if (mContext.getResources().getString(R.string.lunch_medication).equals(medicineSlot)) {
                if (!data.get(i).getLunchA().isEmpty() || !data.get(i).getLunchB().isEmpty()) {
                    setDose(tabCountTextView, data.get(i).getLunchA() + data.get(i).getLunchB(), medicines.get(i));
                    parent.addView(view);
                }
            } else if (mContext.getResources().getString(R.string.dinner_medication).equals(medicineSlot)) {
                if (!data.get(i).getDinnerA().isEmpty() || !data.get(i).getDinnerB().isEmpty()) {
                    setDose(tabCountTextView, data.get(i).getDinnerA() + data.get(i).getDinnerB(), medicines.get(i));
                    parent.addView(view);
                }
            }
        }
    }

    private void setDose(TextView tabCountTextView, String count, Medicine medicines) {
        tabCountTextView.setText("( " + count + " " + PrescriptionData.getMedicineTypeAbbreviation(medicines.getMedicineType()) + " )");
    }

    private void doGetPrescriptionList() {
        PrescriptionHelper mPrescriptionHelper = new PrescriptionHelper(this, this);
        mPrescriptionHelper.doGetPrescriptionList();
    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equals(MyRescribeConstants.TASK_PRESCRIPTION_LIST)) {
            PatientPrescriptionModel prescriptionDataReceived = (PatientPrescriptionModel) customResponse;

            ArrayList<PrescriptionData> data = prescriptionDataReceived.getData();
            if (data != null) {
                if (data.size() != 0) {

                    int j = -1;
                    for (int i = 0; i < data.size(); i++, --j)
                        data.get(i).setDate(CommonMethods.getCalculatedDate("dd-MM-yyyy", j));

                    addHeader(data);

                    mAdapter = new NotificationListAdapter(mContext, data, getTimeArray());
                    recycler.setAdapter(mAdapter);
                }
            }

        }
    }

    private String[] getTimeArray() {
        String breakFast = "8:00 AM";
        String lunchTime = "2:00 PM";
        String dinnerTime = "8:00 PM";

        AppDBHelper appDBHelper = new AppDBHelper(mContext);
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

        return new String[]{dinnerTime, lunchTime, breakFast};
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
    public void onHeaderCollapse() {
        tabletListLayout.setVisibility(View.GONE);
        selectView.setVisibility(View.VISIBLE);
        trangleIconBottom.setVisibility(View.VISIBLE);
        trangleIconTop.setVisibility(View.INVISIBLE);
        isHeaderExpand = false;
    }
}
