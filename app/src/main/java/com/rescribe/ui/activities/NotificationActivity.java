package com.rescribe.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heinrichreimersoftware.materialdrawer.app_logo.BottomSheetMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenu;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuActivity;
import com.heinrichreimersoftware.materialdrawer.bottom_menu.BottomMenuAdapter;
import com.rescribe.R;
import com.rescribe.adapters.NotificationAdapter;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.notification.NotificationHelper;
import com.rescribe.helpers.notification.RespondToNotificationHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.listeners.SwipeDismissTouchListener;
import com.rescribe.model.dashboard_api.DashboardBottomMenuList;
import com.rescribe.model.notification.AdapterNotificationData;
import com.rescribe.model.notification.AdapterNotificationModel;
import com.rescribe.model.notification.Medication;
import com.rescribe.model.notification.NotificationData;
import com.rescribe.model.notification.NotificationModel;
import com.rescribe.model.notification.SlotModel;
import com.rescribe.model.response_model_notification.NotificationResponseBaseModel;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.activities.dashboard.ProfileActivity;
import com.rescribe.ui.activities.dashboard.SettingsActivity;
import com.rescribe.ui.activities.dashboard.SupportActivity;
import com.rescribe.ui.customesViews.CustomProgressDialog;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rescribe.util.RescribeConstants.NOTIFICATION_ID;

public class NotificationActivity extends BottomMenuActivity implements HelperResponse, NotificationAdapter.OnNotificationClickListener, BottomMenuAdapter.OnBottomMenuClickListener {

    private NotificationAdapter mAdapter;
    private String mMedicineSlot;
    private String mNotificationDate;
    private Integer mMedicineId = null;
    public String TAG = getClass().getName();
    private Context mContext;
    private CustomProgressDialog mProgressDialog;
    private boolean isHeaderExpand = true;
    private RespondToNotificationHelper mRespondToNotificationHelper;
    private ArrayList<Medication> mTodayDataList;
    ArrayList<DashboardBottomMenuList> dashboardBottomMenuLists;

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.selectView)
    CheckBox mSelectView;
    @BindView(R.id.headerLayout)
    LinearLayout mHeaderLayout;
    @BindView(R.id.dividerLineInHeader)
    View mDividerLine;
    @BindView(R.id.doseCompletedLabel)
    TextView mDoseCompletedLabel;
    @BindView(R.id.dividerLineInList)
    View mDividerLineInList;
    @BindView(R.id.slotTextView)
    TextView mSlotTextView;
    @BindView(R.id.timeTextView)
    TextView mTimeTextView;
    @BindView(R.id.dateTextView)
    TextView mDateTextView;
    View mView;
    @BindView(R.id.notificationLayout)
    LinearLayout mNotificationLayout;
    @BindView(R.id.noDataAvailable)
    RelativeLayout mNoDataAvailable;
    @BindView(R.id.tabletListLayout)
    LinearLayout mTabletListLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.headerLayoutParent)
    LinearLayout mHeaderLayoutParent;

    private int notificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        initialize();
        notificationId = getIntent().getIntExtra(NOTIFICATION_ID, -1);
    }

    private void initialize() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.notifications));
        mContext = NotificationActivity.this;
        mProgressDialog = new CustomProgressDialog(mContext);
        mToolbar.setNavigationIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_white_24dp, null));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mRespondToNotificationHelper = new RespondToNotificationHelper(this, this);
        Intent intent = getIntent();
        mMedicineSlot = intent.getStringExtra(RescribeConstants.MEDICINE_SLOT);
        mNotificationDate = intent.getStringExtra(RescribeConstants.NOTIFICATION_DATE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        doGetNotification();
    }

    private void doGetNotification() {
        //notification api called
        NotificationHelper mPrescriptionHelper = new NotificationHelper(this);
        mPrescriptionHelper.doGetNotificationList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // Added Header
    private void addHeader(final List<NotificationData> data) {
        List<Medication> medicationList = null;
        for (int j = 0; j < data.size(); j++) {
            medicationList = data.get(j).getMedication();
        }
        final ArrayList<Medication> medi = new ArrayList<>();

        for (int i = 0; i < medicationList.size(); i++) {
            if (getResources().getString(R.string.breakfast_medication).equalsIgnoreCase(mMedicineSlot)) {
                if (medicationList.get(i).getMedicinSlot().equalsIgnoreCase(getString(R.string.breakfast_after)) || medicationList.get(i).getMedicinSlot().equalsIgnoreCase(getString(R.string.breakfast_before))) {
                    medi.add(medicationList.get(i));
                }
            } else if (getResources().getString(R.string.lunch_medication).equalsIgnoreCase(mMedicineSlot)) {
                if (medicationList.get(i).getMedicinSlot().equalsIgnoreCase(getString(R.string.lunch_after)) || medicationList.get(i).getMedicinSlot().equalsIgnoreCase(getString(R.string.lunch_before))) {
                    medi.add(medicationList.get(i));
                }
            } else if (getResources().getString(R.string.dinner_medication).equalsIgnoreCase(mMedicineSlot)) {
                if (medicationList.get(i).getMedicinSlot().equalsIgnoreCase(getString(R.string.dinner_after)) || medicationList.get(i).getMedicinSlot().equalsIgnoreCase(getString(R.string.dinner_before))) {
                    medi.add(medicationList.get(i));
                }
            } else if (getResources().getString(R.string.snacks_medication).equalsIgnoreCase(mMedicineSlot)) {
                if (medicationList.get(i).getMedicinSlot().equalsIgnoreCase(getString(R.string.snacks_after)) || medicationList.get(i).getMedicinSlot().equalsIgnoreCase(getString(R.string.snacks_before))) {
                    medi.add(medicationList.get(i));
                }
            }
        }

        if (!medi.isEmpty()) {
            if (medi.size() != 0) {
                mHeaderLayoutParent.setVisibility(View.VISIBLE);
                String slotMedicine = "";

                if (getResources().getString(R.string.breakfast_medication).equalsIgnoreCase(mMedicineSlot)) {
                    slotMedicine = getString(R.string.smallcasebreakfast);
                } else if (getResources().getString(R.string.lunch_medication).equalsIgnoreCase(mMedicineSlot)) {
                    slotMedicine = getString(R.string.smallcaselunch);
                } else if (getResources().getString(R.string.dinner_medication).equalsIgnoreCase(mMedicineSlot)) {
                    slotMedicine = getString(R.string.smallcasedinner);
                } else if (getResources().getString(R.string.snacks_medication).equalsIgnoreCase(mMedicineSlot)) {
                    slotMedicine = getString(R.string.smallcasesnacks);
                }
                mSlotTextView.setText(mMedicineSlot);
                mTimeTextView.setText(CommonMethods.getDayFromDate(RescribeConstants.DATE_PATTERN.DD_MM_YYYY, CommonMethods.getCurrentDateTime()));
                mDateTextView.setText(mNotificationDate);
                mDoseCompletedLabel.setText(getString(R.string.dosage_completed));
                mDividerLineInList.setVisibility(View.VISIBLE);
                mDividerLine.setVisibility(View.VISIBLE);
                addHeaderTabletView(mTabletListLayout, medi);
                mTabletListLayout.setVisibility(View.VISIBLE);
                mSelectView.setVisibility(View.INVISIBLE);
                final String finalSlotMedicine = slotMedicine;
                mSelectView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRespondToNotificationHelper.doRespondToNotificationForHeader(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext)), finalSlotMedicine, mMedicineId, CommonMethods.formatDateTime(CommonMethods.getCurrentDateTime(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE), 1, RescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER + "_" + 0);
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

                final String finalSlotMedicine1 = slotMedicine;
                SwipeDismissTouchListener swipeDismissTouchListener = new SwipeDismissTouchListener(
                        mHeaderLayout,
                        null,
                        new SwipeDismissTouchListener.OnDismissCallback() {
                            @Override
                            public void onDismiss(View view, Object token) {
                                mHeaderLayoutParent.removeView(view);
                                onSwiped(finalSlotMedicine1);
                            }
                        });

                mHeaderLayout.setOnTouchListener(swipeDismissTouchListener);
            }
        }
    }

    private void addHeaderTabletView(final ViewGroup parent, final ArrayList<Medication> data) {
        mTodayDataList = new ArrayList<>();
        mTodayDataList.addAll(data);
        for (int i = 0; i < mTodayDataList.size(); i++) {

            final View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.tablet_list, parent, false);

            final CheckBox selectViewTab = (CheckBox) view.findViewById(R.id.selectViewTab);
            ImageView tabTypeView = (ImageView) view.findViewById(R.id.tabTypeView);
            TextView tabNameTextView = (TextView) view.findViewById(R.id.tabNameTextView);
            TextView tabCountTextView = (TextView) view.findViewById(R.id.tabCountTextView);
            selectViewTab.setChecked(mTodayDataList.get(i).isTabSelected() == 1);
            selectViewTab.setEnabled(mTodayDataList.get(i).isTabWebService());
            final int finalI = i;
            selectViewTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mView = view;
                    mRespondToNotificationHelper.doRespondToNotification(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext)), data.get(finalI).getMedicinSlot(), data.get(finalI).getMedicineId(), CommonMethods.formatDateTime(CommonMethods.getCurrentDateTime(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE), 0, RescribeConstants.TASK_RESPOND_NOTIFICATION + "_" + finalI, selectViewTab.isChecked() ? 1 : 0);
                }
            });

            setDose(tabCountTextView, mTodayDataList.get(i).getQuantity(), mTodayDataList.get(i));
            tabNameTextView.setText(mTodayDataList.get(i).getMedicineName());
            tabTypeView.setImageDrawable(CommonMethods.getMedicineTypeImage(mTodayDataList.get(i).getMedicineTypeName(), mContext, ContextCompat.getColor(mContext, R.color.tagColor)));
            parent.addView(view);
        }

    }

    private void setDose(TextView tabCountTextView, String count, Medication prescriptionData) {
        tabCountTextView.setText(count);
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

        ArrayList<Medication> notificationDataList;
        NotificationData notificationDataForHeader = new NotificationData();
        List<NotificationData> notificationListForAdapter = new ArrayList<>();
        List<NotificationData> notificationListForHeader = new ArrayList<>();
        // on click of NotificationActivity checkbox of sublist layout
        if (mOldDataTag.startsWith(RescribeConstants.TASK_RESPOND_NOTIFICATION)) {
            //onclick of checkbox of sublist
            NotificationResponseBaseModel responseLogNotificationModel = (NotificationResponseBaseModel) customResponse;
            String[] count = mOldDataTag.split("_");
            String counter = count[1];
            if (responseLogNotificationModel.getCommon().isSuccess()) {
                // CommonMethods.showToast(mContext, responseLogNotificationModel.getNotificationResponseModel().getMsg());

                CheckBox checkBox = (CheckBox) mView.findViewById(R.id.selectViewTab);
                mTodayDataList.get(Integer.parseInt(counter)).setTabSelected(checkBox.isChecked() ? 1 : 0);
                mTodayDataList.get(Integer.parseInt(counter)).setTabWebService(!checkBox.isChecked());

                if (mAdapter.getSelectedCount(mTodayDataList) == mTodayDataList.size()) {
                    mHeaderLayoutParent.removeView(mHeaderLayout);
                    if (mAdapter != null) {
                        if (mAdapter.getItemCount() == 0)
                            mNoDataAvailable.setVisibility(View.VISIBLE);
                    }

                    if (notificationId != -1)
                        AppDBHelper.getInstance(mContext).deleteUnreadReceivedNotificationMessage(String.valueOf(notificationId), RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT);
                }
            }
        } else if (mOldDataTag.equals(RescribeConstants.TASK_NOTIFICATION)) {

            NotificationModel prescriptionDataReceived = (NotificationModel) customResponse;
            if (prescriptionDataReceived.getNotificationPrescriptionModel().getPresriptionNotification().size() != 0) {

                mNotificationLayout.setVisibility(View.VISIBLE);
                mNoDataAvailable.setVisibility(View.GONE);

                List<NotificationData> notificationData = prescriptionDataReceived.getNotificationPrescriptionModel().getPresriptionNotification();
                String date = CommonMethods.getCurrentDateTime();
                CommonMethods.Log(TAG, date);
                //Current date and slot data is sorted to show in header of UI
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
                }
                if (notificationListForHeader.size() != 0) {
                    if (!notificationListForHeader.isEmpty()) {
                        addHeader(notificationListForHeader);
                    }
                }
                // DoctorConnectChatData for recyclerview Adapter is sorted to set data according to UI .
                List<AdapterNotificationData> adapterNotificationParentData = new ArrayList<>();
                List<AdapterNotificationModel> adapterNotificationModelListForDinner = new ArrayList<>();
                String notifyDate;
                for (int i = 0; i < notificationListForAdapter.size(); i++) {
                    List<Medication> medications;
                    SlotModel slotModel = new SlotModel();

                    AdapterNotificationModel adapterNotificationModel = new AdapterNotificationModel();

                    medications = notificationListForAdapter.get(i).getMedication();
                    notifyDate = notificationListForAdapter.get(i).getPrescriptionDate();
                    List<Medication> dinnerList = new ArrayList<>();
                    List<Medication> lunchList = new ArrayList<>();
                    List<Medication> breakfastList = new ArrayList<>();
                    List<Medication> snackList = new ArrayList<>();

                    for (int j = 0; j < medications.size(); j++) {
                        if (medications.get(j).getMedicinSlot().equalsIgnoreCase(mContext.getString(R.string.dinner_after)) || medications.get(j).getMedicinSlot().equalsIgnoreCase(mContext.getString(R.string.dinner_before))) {
                            Medication medicationDinner = new Medication();
                            medicationDinner.setMedicineName(medications.get(j).getMedicineName());
                            medicationDinner.setQuantity(medications.get(j).getQuantity());
                            medicationDinner.setMedicineId(medications.get(j).getMedicineId());
                            medicationDinner.setMedicinSlot(medications.get(j).getMedicinSlot());
                            medicationDinner.setMedicineTypeName(medications.get(j).getMedicineTypeName());
                            medicationDinner.setDate(notifyDate);
                            dinnerList.add(medicationDinner);
                        } else if (medications.get(j).getMedicinSlot().equalsIgnoreCase(mContext.getString(R.string.lunch_after)) || medications.get(j).getMedicinSlot().equalsIgnoreCase(mContext.getString(R.string.lunch_before))) {
                            Medication medicationLunch = new Medication();
                            medicationLunch.setMedicineName(medications.get(j).getMedicineName());
                            medicationLunch.setQuantity(medications.get(j).getQuantity());
                            medicationLunch.setMedicineId(medications.get(j).getMedicineId());
                            medicationLunch.setMedicinSlot(medications.get(j).getMedicinSlot());
                            medicationLunch.setMedicineTypeName(medications.get(j).getMedicineTypeName());
                            medicationLunch.setDate(notifyDate);
                            lunchList.add(medicationLunch);
                        } else if (medications.get(j).getMedicinSlot().equalsIgnoreCase(mContext.getString(R.string.breakfast_after)) || medications.get(j).getMedicinSlot().equalsIgnoreCase(mContext.getString(R.string.breakfast_before))) {
                            Medication medicationBreakfast = new Medication();
                            medicationBreakfast.setMedicineName(medications.get(j).getMedicineName());
                            medicationBreakfast.setQuantity(medications.get(j).getQuantity());
                            medicationBreakfast.setMedicineId(medications.get(j).getMedicineId());
                            medicationBreakfast.setMedicinSlot(medications.get(j).getMedicinSlot());
                            medicationBreakfast.setMedicineTypeName(medications.get(j).getMedicineTypeName());
                            medicationBreakfast.setDate(notifyDate);
                            breakfastList.add(medicationBreakfast);
                        } else if (medications.get(j).getMedicinSlot().equalsIgnoreCase(mContext.getString(R.string.snacks_after)) || medications.get(j).getMedicinSlot().equalsIgnoreCase(mContext.getString(R.string.snacks_before))) {
                            Medication medicationSnack = new Medication();
                            medicationSnack.setMedicineName(medications.get(j).getMedicineName());
                            medicationSnack.setQuantity(medications.get(j).getQuantity());
                            medicationSnack.setMedicineId(medications.get(j).getMedicineId());
                            medicationSnack.setMedicinSlot(medications.get(j).getMedicinSlot());
                            medicationSnack.setMedicineTypeName(medications.get(j).getMedicineTypeName());
                            medicationSnack.setDate(notifyDate);
                            snackList.add(medicationSnack);
                        }
                    }
                    slotModel.setSnacks(snackList);
                    slotModel.setDinner(dinnerList);
                    slotModel.setBreakfast(breakfastList);
                    slotModel.setLunch(lunchList);
                    adapterNotificationModel.setMedication(slotModel);
                    adapterNotificationModel.setPrescriptionDate(notifyDate);
                    adapterNotificationModelListForDinner.add(adapterNotificationModel);
                }

                mAdapter = new NotificationAdapter(mContext, adapterNotificationModelListForDinner, getTimeArray());
                mRecyclerView.setAdapter(mAdapter);
                mProgressDialog.dismiss();
                CommonMethods.Log("", "" + adapterNotificationParentData);
            } else {
                mNotificationLayout.setVisibility(View.GONE);
                mNoDataAvailable.setVisibility(View.VISIBLE);
            }
        } else if (mOldDataTag.startsWith(RescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER)) {
            NotificationResponseBaseModel responseLogNotificationModel = (NotificationResponseBaseModel) customResponse;
            //onclick of NotificationActivity checkbox of header layout
            if (responseLogNotificationModel.getCommon().isSuccess()) {
                //    CommonMethods.showToast(mContext, responseLogNotificationModel.getNotificationResponseModel().getMsg());
                mHeaderLayoutParent.removeView(mHeaderLayout);
                if (mAdapter != null) {
                    if (mAdapter.getItemCount() == 0)
                        mNoDataAvailable.setVisibility(View.VISIBLE);
                }

                if (notificationId != -1)
                    AppDBHelper.getInstance(mContext).deleteUnreadReceivedNotificationMessage(String.valueOf(notificationId), RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT);
            }
            //handled click from NotificationAdapter checkbox in header layout
        } else if (mOldDataTag.startsWith(RescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER_ADAPTER)) {
            mAdapter.onSuccessOfNotificationCheckBoxClick(mOldDataTag, customResponse);
            //handled click from NotificationAdapter checkbox in sublist layout
        } else if (mOldDataTag.startsWith(RescribeConstants.TASK_RESPOND_NOTIFICATION_ADAPTER)) {
            mAdapter.onSuccessOfNotificationCheckBoxClick(mOldDataTag, customResponse);
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
        if (mOldDataTag.startsWith(RescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER)) {
            mSelectView.setEnabled(true);
            mSelectView.setChecked(false);
        } else if (mOldDataTag.startsWith(RescribeConstants.TASK_RESPOND_NOTIFICATION)) {
            CheckBox checkBox = (CheckBox) mView.findViewById(R.id.selectViewTab);
            checkBox.setChecked(!checkBox.isChecked());
        } else if (mOldDataTag.equals(RescribeConstants.TASK_NOTIFICATION)) {
            mNoDataAvailable.setVisibility(View.VISIBLE);
        } else if (mOldDataTag.startsWith(RescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER_ADAPTER)) {
            mAdapter.onNoConnectionOfNotificationCheckBoxClick(mOldDataTag, serverErrorMessage);
        } else if (mOldDataTag.startsWith(RescribeConstants.TASK_RESPOND_NOTIFICATION_ADAPTER)) {
            mAdapter.onNoConnectionOfNotificationCheckBoxClick(mOldDataTag, serverErrorMessage);

        }


    }

    @Override
    public void onHeaderCollapse() {
        mDividerLine.setVisibility(View.GONE);
        mTabletListLayout.setVisibility(View.GONE);
        mSelectView.setVisibility(View.VISIBLE);
        isHeaderExpand = false;
    }

    @Override
    public void clickCheckBox(View mViewForHeader, int pos, String slotType, ViewGroup viewGroup, Integer medicineId, String takenDate, Integer bundleValue, String taskName, boolean isHeaderCheckboxClick, boolean checked) {
        if (isHeaderCheckboxClick)
            mRespondToNotificationHelper.doRespondToNotificationForHeaderOfNotificationAdapter(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext)), slotType, medicineId, takenDate, bundleValue, taskName, checked ? 1 : 0);
        else
            mRespondToNotificationHelper.doRespondToNotificationForNotificationAdapter(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext)), slotType, medicineId, takenDate, bundleValue, taskName, checked ? 1 : 0);
    }

    @Override
    public void onSwiped(String slotType) {
        if (mAdapter != null) {
            if (mAdapter.getItemCount() == 0)
                mNoDataAvailable.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBottomMenuClick(BottomMenu bottomMenu) {

        String menuName = bottomMenu.getMenuName();

        if (menuName.equalsIgnoreCase(getString(R.string.profile))) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);
            finish();
        } else if (menuName.equalsIgnoreCase(getString(R.string.settings))) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);
            finish();
        } else if (menuName.equalsIgnoreCase(getString(R.string.support))) {
            Intent intent = new Intent(this, SupportActivity.class);
            intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onProfileImageClick() {

    }

    @Override
    public void onBottomSheetMenuClick(BottomSheetMenu bottomMenu) {

    }
}