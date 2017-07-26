package com.myrescribe.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
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
import android.widget.Toast;

import com.myrescribe.R;
import com.myrescribe.adapters.NotificationListAdapter;
import com.myrescribe.helpers.database.AppDBHelper;
import com.myrescribe.helpers.notification.NotificationHelper;
import com.myrescribe.helpers.notification.RespondToNotificationHelper;
import com.myrescribe.helpers.prescription.PrescriptionHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.notification.Common;
import com.myrescribe.model.notification.NotificationData;
import com.myrescribe.model.notification.Medication;
import com.myrescribe.model.notification.NotificationModel;
import com.myrescribe.model.response_model_notification.ResponseLogNotificationModel;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.ui.customesViews.CustomProgressDialog;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;
import com.myrescribe.listeners.SwipeDismissTouchListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity implements HelperResponse, NotificationListAdapter.OnHeaderClickListener {

    private RecyclerView mRecyclerView;
    private NotificationListAdapter mAdapter;
    private String medicineSlot;
    private String mNotificationDate;
    private Integer mMedicineId = null;
    public String TAG = getClass().getName();
    private Context mContext;
    CustomProgressDialog mProgressDialog;
    private boolean isHeaderExpand = true;
    private LinearLayout mTabletListLayout;
    private CheckBox mSelectView;
    private LinearLayout mHeaderLayout;
    private View mDividerLine;
    private RespondToNotificationHelper mRespondToNotificationHelper;
    private LinearLayout mHeaderLayoutParent;
    private String mNotificationTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.notification));
        mContext = NotificationActivity.this;
        mProgressDialog = new CustomProgressDialog(mContext);

        toolbar.setNavigationIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_white_24dp, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mRespondToNotificationHelper = new RespondToNotificationHelper(this, this);
        Intent intent = getIntent();
        medicineSlot = intent.getStringExtra(MyRescribeConstants.MEDICINE_SLOT);
        mNotificationDate = intent.getStringExtra(MyRescribeConstants.NOTIFICATION_DATE);
        mNotificationTime = intent.getStringExtra(MyRescribeConstants.NOTIFICATION_TIME);
//        medicines = (ArrayList<Medicine>) intent.getBundleExtra(MyRescribeConstants.MEDICINE_NAME).getSerializable(MyRescribeConstants.MEDICINE_NAME);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        doGetNotification();
        // doGetPrescriptionList();
    }

    private void doGetNotification() {
        NotificationHelper mPrescriptionHelper = new NotificationHelper(this, this);
        mPrescriptionHelper.doGetNotificationList();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NotificationActivity.this, HomePageActivity.class);
        intent.putExtra(MyRescribeConstants.ALERT, false);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onBackPressed();
    }

    // Added Header
    private void addHeader(final List<NotificationData> data) {

        String slotMedicine = "";

        mHeaderLayout = (LinearLayout) findViewById(R.id.headerLayout);
        mHeaderLayoutParent = (LinearLayout) findViewById(R.id.headerLayoutParent);
        mDividerLine = (View) findViewById(R.id.dividerLineInHeader);
        TextView slotTextView = (TextView) findViewById(R.id.slotTextView);
        TextView timeTextView = (TextView) findViewById(R.id.timeTextView);
        TextView dateTextView = (TextView) findViewById(R.id.dateTextView);
        if (mContext.getResources().getString(R.string.breakfast_medication).equalsIgnoreCase(medicineSlot)) {
            slotMedicine = getString(R.string.smallcasebreakfast);
        } else if (mContext.getResources().getString(R.string.lunch_medication).equalsIgnoreCase(medicineSlot)) {
            slotMedicine = getString(R.string.smallcaselunch);
        } else if (mContext.getResources().getString(R.string.dinner_medication).equalsIgnoreCase(medicineSlot)) {
            slotMedicine = getString(R.string.smallcasedinner);
        } else if (mContext.getResources().getString(R.string.snacks_medication).equalsIgnoreCase(medicineSlot)) {
            slotMedicine = getString(R.string.smallcasesnacks);
        }
        slotTextView.setText(medicineSlot);
        timeTextView.setText(CommonMethods.getDayFromDate(MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY, CommonMethods.getCurrentDateTime()));
        dateTextView.setText(mNotificationDate);

        mTabletListLayout = (LinearLayout) findViewById(R.id.tabletListLayout);
        mSelectView = (CheckBox) findViewById(R.id.selectView);

        addHeaderTabletView(mTabletListLayout, data);

        mTabletListLayout.setVisibility(View.VISIBLE);
        mSelectView.setVisibility(View.INVISIBLE);
        final String finalSlotMedicine = slotMedicine;
        mSelectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectView.isChecked())
                    mHeaderLayoutParent.removeView(mHeaderLayout);
                mRespondToNotificationHelper.doRespondToNotification(Integer.valueOf(MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATEINTID, mContext)), finalSlotMedicine, mMedicineId, CommonMethods.formatDateTime(CommonMethods.getCurrentDateTime(),MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD,MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY,MyRescribeConstants.DATE), 1);
            }
        });

        mHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHeaderExpand) {
                    onHeaderCollapse();
                } else {
                    isHeaderExpand = true;
                    if (mAdapter.preExpandedPos != -1) {
                        mAdapter.collapseAll();
                        mAdapter.notifyItemChanged(mAdapter.preExpandedPos);
                        mAdapter.preExpandedPos = -1;
                    }
                    mDividerLine.setVisibility(View.VISIBLE);
                    mTabletListLayout.setVisibility(View.VISIBLE);
                    mSelectView.setVisibility(View.INVISIBLE);
                }
            }
        });

        SwipeDismissTouchListener swipeDismissTouchListener = new SwipeDismissTouchListener(
                mHeaderLayout,
                null,
                new SwipeDismissTouchListener.OnDismissCallback() {
                    @Override
                    public void onDismiss(View view, Object token) {
                        mHeaderLayoutParent.removeView(view);
                    }
                });

        mHeaderLayout.setOnTouchListener(swipeDismissTouchListener);
    }

    private void addHeaderTabletView(final ViewGroup parent, final List<NotificationData> data) {
        List<Medication> medicationList = null;
        for (int j = 0; j < data.size(); j++) {
            medicationList = data.get(j).getMedication();
        }
        final ArrayList<Medication> medi = new ArrayList<>();

        for (int i = 0; i < medicationList.size(); i++) {
            if (mContext.getResources().getString(R.string.breakfast_medication).equalsIgnoreCase(medicineSlot)) {
                if (medicationList.get(i).getMedicinSlot().equalsIgnoreCase(getString(R.string.breakfast_after)) || medicationList.get(i).getMedicinSlot().equalsIgnoreCase(getString(R.string.breakfast_before))) {
                    medi.add(medicationList.get(i));
                }
            } else if (mContext.getResources().getString(R.string.lunch_medication).equalsIgnoreCase(medicineSlot)) {
                if (medicationList.get(i).getMedicinSlot().equalsIgnoreCase(getString(R.string.lunch_after)) || medicationList.get(i).getMedicinSlot().equalsIgnoreCase(getString(R.string.lunch_before))) {
                    medi.add(medicationList.get(i));
                }
            } else if (mContext.getResources().getString(R.string.dinner_medication).equalsIgnoreCase(medicineSlot)) {
                if (medicationList.get(i).getMedicinSlot().equalsIgnoreCase(getString(R.string.dinner_after)) || medicationList.get(i).getMedicinSlot().equalsIgnoreCase(getString(R.string.dinner_before))) {
                    medi.add(medicationList.get(i));
                }
            } else if (mContext.getResources().getString(R.string.snacks_medication).equalsIgnoreCase(medicineSlot)) {
                if (medicationList.get(i).getMedicinSlot().equalsIgnoreCase(getString(R.string.snacks_after)) || !medicationList.get(i).getMedicinSlot().equalsIgnoreCase(getString(R.string.snacks_before))) {
                    medi.add(medicationList.get(i));
                }
            }
        }

        for (int i = 0; i < medi.size(); i++) {

            final View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.tablet_list, parent, false);

            final CheckBox selectViewTab = (CheckBox) view.findViewById(R.id.selectViewTab);
            ImageView tabTypeView = (ImageView) view.findViewById(R.id.tabTypeView);
            TextView tabNameTextView = (TextView) view.findViewById(R.id.tabNameTextView);
            TextView tabCountTextView = (TextView) view.findViewById(R.id.tabCountTextView);
            selectViewTab.setChecked(medi.get(i).isTabSelected());

            final int finalI = i;
            selectViewTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectViewTab.isChecked()) {
                        medi.get(finalI).setTabSelected(true);
                        if (mAdapter.getSelectedCount(medi) == medi.size())
                            mHeaderLayoutParent.removeView(mHeaderLayout);
                        mRespondToNotificationHelper.doRespondToNotification(Integer.valueOf(MyRescribePreferencesManager.getString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATEINTID, mContext)), medi.get(finalI).getMedicinSlot(), medi.get(finalI).getMedicineId(), CommonMethods.formatDateTime(CommonMethods.getCurrentDateTime(),MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD,MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY,MyRescribeConstants.DATE), 0);
                    } else {
                        medi.get(finalI).setTabSelected(false);


                    }
//                    CommonMethods.showToast(mContext, "Checked in " + position + " " + selectViewTab.isChecked());
                }
            });


                setDose(tabCountTextView, medi.get(i).getQuantity(), medi.get(i));
                tabNameTextView.setText(medi.get(i).getMedicineName());
                tabTypeView.setImageDrawable(CommonMethods.getMedicalTypeIcon(medi.get(i).getMedicineTypeName(), mContext));
                parent.addView(view);


        }
    }

    private void setDose(TextView tabCountTextView, String count, Medication prescriptionData) {
        tabCountTextView.setText(count);
    }

    private void doGetPrescriptionList() {
        PrescriptionHelper mPrescriptionHelper = new PrescriptionHelper(this, this);
        mProgressDialog.show();
        mPrescriptionHelper.doGetPrescriptionList();
    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        List<Medication> notificationDataList = null;
        NotificationData notificationDataForHeader = new NotificationData();
        List<NotificationData> notificationListForAdapter = new ArrayList<>();
        List<NotificationData> notificationListForHeader = new ArrayList<>();
        String todayDate = null;
        if(mOldDataTag.equals(MyRescribeConstants.TASK_RESPOND_NOTIFICATION)){
            ResponseLogNotificationModel responseLogNotificationModel = (ResponseLogNotificationModel)customResponse;
            Common common =responseLogNotificationModel.getCommon();
            CommonMethods.showToast(mContext,common.getStatusMessage());
        }
        if (mOldDataTag.equals(MyRescribeConstants.TASK_NOTIFICATION)) {
            NotificationModel prescriptionDataReceived = (NotificationModel) customResponse;
            List<NotificationData> notificationData = prescriptionDataReceived.getData();
            String date = CommonMethods.getCurrentDateTime();
            CommonMethods.Log(TAG, date);
            for (int k = 0; k < notificationData.size(); k++) {
                if (notificationData.get(k).getPrescriptionDate().equals(CommonMethods.getCurrentDateTime())) {
                    String prescriptionDate = notificationData.get(k).getPrescriptionDate();
                    notificationDataList = notificationData.get(k).getMedication();
                    notificationDataForHeader.setMedication(notificationDataList);
                    notificationDataForHeader.setPrescriptionDate(prescriptionDate);
                    notificationListForHeader.add(notificationDataForHeader);
                } else {
                    NotificationData notificationDataForAdapter = new NotificationData();
                    String datePrescription = notificationData.get(k).getPrescriptionDate();
                    notificationDataList = notificationData.get(k).getMedication();
                    notificationDataForAdapter.setMedication(notificationDataList);
                    notificationDataForAdapter.setPrescriptionDate(datePrescription);
                    notificationListForAdapter.add(notificationDataForAdapter);

                }

                //  medicationList.add(notificationDataList);
                //  String mNotificationDate = notificationData.get(k).getPrescriptionDate();
               /* Prescription prescription  =    notificationData.get(k).getPrescription();
                notificationDataList = prescription.getMedication();*/
            }
            CommonMethods.Log(TAG, notificationDataList.toString());


            if (notificationListForHeader != null) {
                if (notificationListForHeader.size() != 0) {
                    for (int m = 0; m < notificationListForHeader.size(); m++) {
                        todayDate = notificationListForHeader.get(m).getPrescriptionDate();
                        notificationDataList = notificationListForHeader.get(m).getMedication();
                    }

                    List<Medication> notificationDummyData = new ArrayList<>();

                    int j = -1;
                    for (int i = 0; i < notificationDataList.size(); i++, --j) {
                        notificationDataList.get(i).setDate(CommonMethods.getCalculatedDate(todayDate, j));
                    }

                        addHeader(notificationListForHeader);

                    notificationDummyData.addAll(notificationDataList);
                    mAdapter = new NotificationListAdapter(mContext, notificationListForAdapter, getTimeArray());
                    mRecyclerView.setAdapter(mAdapter);
                    mProgressDialog.dismiss();

                }
            }

        }
    }

    private String[] getTimeArray() {
        String breakFast = "8:00 AM";
        String lunchTime = "2:00 PM";
        String dinnerTime = "8:00 PM";
        String snacksTime = "8:00 PM";

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

        return new String[]{dinnerTime, lunchTime, breakFast, snacksTime};
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
        mDividerLine.setVisibility(View.GONE);
        mTabletListLayout.setVisibility(View.GONE);
        mSelectView.setVisibility(View.VISIBLE);
        isHeaderExpand = false;
    }
}
