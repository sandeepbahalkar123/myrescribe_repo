package com.rescribe.ui.activities.dashboard;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.adapters.unread_notification_message_list.UnreadBookAppointTokenNotificationAdapter;
import com.rescribe.adapters.unread_notification_message_list.UnreadMedicationNotificationAdapter;
import com.rescribe.adapters.unread_notification_message_list.UnreadNotificationAlertAdapter;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.investigation.InvestigationHelper;
import com.rescribe.helpers.notification.RespondToNotificationHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.book_appointment.unread_token_notification.UnreadBookAppointTokenNotificationBaseModel;
import com.rescribe.model.book_appointment.unread_token_notification.UnreadBookAppointTokenNotificationData;
import com.rescribe.model.dashboard_api.unread_notification_message_list.UnreadSavedNotificationMessageData;
import com.rescribe.model.investigation.InvestigationNotification;
import com.rescribe.model.notification.Medication;
import com.rescribe.model.notification.NotificationData;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.activities.AppointmentActivity;
import com.rescribe.ui.activities.InvestigationActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

import static com.rescribe.singleton.RescribeApplication.clearNotification;
import static com.rescribe.util.RescribeConstants.APPOINTMENT_NOTIFICATION_TAG;
import static com.rescribe.util.RescribeConstants.INVESTIGATION_NOTIFICATION_TAG;
import static com.rescribe.util.RescribeConstants.MEDICATIONS_NOTIFICATION_TAG;


/**
 * Created by jeetal on 27/11/17.
 */

public class UnreadNotificationMessageActivity extends AppCompatActivity implements HelperResponse, UnreadNotificationAlertAdapter.OnNotificationItemClicked, UnreadMedicationNotificationAdapter.OnMedicationNotificationEventClick, UnreadBookAppointTokenNotificationAdapter.OnUnreadTokenNotificationItemClicked {

    @BindView(R.id.bookAppointmentBackButton)
    ImageView mBackButton;
    @BindView(R.id.title)
    CustomTextView mTitleView;
    @BindView(R.id.appointmentsListView)
    RecyclerView mAppointmentAlertList;
    @BindView(R.id.investigationsListView)
    RecyclerView mInvestigationsListView;
    @BindView(R.id.unreadTokenNotificationListView)
    RecyclerView mUnreadTokenNotificationListView;
    @BindView(R.id.onGoingMedicationListView)
    RecyclerView mOnGoingMedicationListView;
    @BindView(R.id.onGoingMedicationListViewLayout)
    LinearLayout mOnGoingMedicationListViewLayout;
    @BindView(R.id.investigationsListViewLayout)
    LinearLayout investigationsListViewLayout;
    @BindView(R.id.appointmentsListViewLayout)
    LinearLayout appointmentsListViewLayout;
    @BindView(R.id.unreadTokenNotificationListViewLayout)
    LinearLayout unreadTokenNotificationListViewLayout;
    @BindView(R.id.getTokenFirstMessageTimeStamp)
    CustomTextView mGetTokenFirstMessageTimeStamp;
    @BindView(R.id.onGoingMedicationFirstMessageTimeStamp)
    CustomTextView mOnGoingMedicationFirstMessageTimeStamp;
    @BindView(R.id.appointmentsFirstMessageTimeStamp)
    CustomTextView mAppointmentsFirstMessageTimeStamp;
    @BindView(R.id.investigationFirstMessageTimeStamp)
    CustomTextView mInvestigationFirstMessageTimeStamp;
    @BindView(R.id.emptyListMessageView)
    ImageView emptyListMessageView;
    private UnreadNotificationAlertAdapter mAppointmentNotificationAlertAdapter;
    private UnreadNotificationAlertAdapter mInvestigationNotificationAlertAdapter;
    private UnreadBookAppointTokenNotificationAdapter mUnreadBookAppointTokenNotificationAdapter;
    private SectionedRecyclerViewAdapter mUnreadMedicationNotificationAdapter;
    private ArrayList<UnreadSavedNotificationMessageData> mUnreadMedicationNotificationMessageDataList = new ArrayList<>();
    private DoctorDataHelper mDoctorDataHelper;
    private RespondToNotificationHelper mMedicationToNotificationHelper;
    private UnreadSavedNotificationMessageData mClickedUnreadInvestigationMessageData;
    private boolean isMedicationLoadMoreFooterClickedPreviously;
    public boolean isAllListEmpty = true;
    Calendar c = Calendar.getInstance();
    int hour24 = c.get(Calendar.HOUR_OF_DAY);
    int Min = c.get(Calendar.MINUTE);

    private HashMap<String, String> medicNotificationTimeId = new HashMap<>();
    private HashMap<String, ArrayList<Medication>> listDataChild = new HashMap<>();

    private Medication medicationToUpdate;
    private String medicationKeyUpdate;
    private boolean medicationToCheck;

    public boolean isExpanded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_all_unread_notification_layout);
        ButterKnife.bind(this);
        mTitleView.setText(getString(R.string.notifications));

        // Clear all notifications
        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialize();
        doGetUnreadTokenNotification();
    }

    private void initialize() {
        initializeAppointmentsListView();
        initializeInvestigationListView();
        initializeMedicationListView();
        showMessage();
    }

    private void showMessage() {
        if (isAllListEmpty)
            emptyListMessageView.setVisibility(View.VISIBLE);
        else emptyListMessageView.setVisibility(View.GONE);
    }

    private void initializeAppointmentsListView() {
        ArrayList<UnreadSavedNotificationMessageData> appAlertList = RescribeApplication.doFindUnreadNotificationMessageByType(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT);
        //----------------
        if (appAlertList.isEmpty()) {
            appointmentsListViewLayout.setVisibility(View.GONE);
        } else {
            appointmentsListViewLayout.setVisibility(View.VISIBLE);
            setAppointmentAlertListAdapter(appAlertList);
        }
        //----------------
    }

    private void initializeInvestigationListView() {
        ArrayList<UnreadSavedNotificationMessageData> investigationAlertList = RescribeApplication.doFindUnreadNotificationMessageByType(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT);
        if (investigationAlertList.isEmpty()) {
            investigationsListViewLayout.setVisibility(View.GONE);
        } else {
            investigationsListViewLayout.setVisibility(View.VISIBLE);
            setInvestigationAlertListAdapter(investigationAlertList);
        }
    }

    private void initializeMedicationListView() {
        ArrayList<UnreadSavedNotificationMessageData> medicationAlertList = RescribeApplication.doFindUnreadNotificationMessageByType(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT);

        //----------------
        if (medicationAlertList.isEmpty()) {
            mOnGoingMedicationListViewLayout.setVisibility(View.GONE);
        } else {
            //sortListByMealTime(medicationAlertList);
            isAllListEmpty = false;
            mMedicationToNotificationHelper = new RespondToNotificationHelper(this, this);
            mOnGoingMedicationListViewLayout.setVisibility(View.VISIBLE);
            setMedicationAlertListAdapter(medicationAlertList, isMedicationLoadMoreFooterClickedPreviously);
        }
    }

    private void setAppointmentAlertListAdapter(ArrayList<UnreadSavedNotificationMessageData> appAlertList) {

        UnreadSavedNotificationMessageData unreadSavedNotificationMessageData = appAlertList.get(0);

        String formattedDate = CommonMethods.getFormattedDate(unreadSavedNotificationMessageData.getNotificationTimeStamp(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE_PATTERN.DD_MM_YYYY);
        String time = CommonMethods.formatDateTime(unreadSavedNotificationMessageData.getNotificationTimeStamp(), RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.DATE_PATTERN.DD_MM_YYYY + " " + RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.TIME);

        String dayFromDate = CommonMethods.getDayFromDate(RescribeConstants.DATE_PATTERN.DD_MM_YYYY, formattedDate);

        if (getString(R.string.today).equalsIgnoreCase(dayFromDate)) {
            mAppointmentsFirstMessageTimeStamp.setText(time);
        } else {
            mAppointmentsFirstMessageTimeStamp.setText(dayFromDate + " " + time);
        }
        mAppointmentsFirstMessageTimeStamp.setVisibility(View.VISIBLE);
        //------------
        Collections.reverse(appAlertList);
        mAppointmentNotificationAlertAdapter = new UnreadNotificationAlertAdapter(this, appAlertList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mAppointmentAlertList.setLayoutManager(mLayoutManager);
        mAppointmentAlertList.setAdapter(mAppointmentNotificationAlertAdapter);

        if (!appAlertList.isEmpty())
            isAllListEmpty = false;
    }

    private void setInvestigationAlertListAdapter(ArrayList<UnreadSavedNotificationMessageData> appAlertList) {

        UnreadSavedNotificationMessageData unreadSavedNotificationMessageData = appAlertList.get(0);
        String formattedDate = CommonMethods.getFormattedDate(unreadSavedNotificationMessageData.getNotificationTimeStamp(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE_PATTERN.DD_MM_YYYY);
        String time = CommonMethods.formatDateTime(unreadSavedNotificationMessageData.getNotificationTimeStamp(), RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.DATE_PATTERN.DD_MM_YYYY + " " + RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.TIME);

        String dayFromDate = CommonMethods.getDayFromDate(RescribeConstants.DATE_PATTERN.DD_MM_YYYY, formattedDate);

        if (getString(R.string.today).equalsIgnoreCase(dayFromDate)) {
            mInvestigationFirstMessageTimeStamp.setText(time);
        } else {
            mInvestigationFirstMessageTimeStamp.setText(dayFromDate + " " + time);
        }
        mInvestigationFirstMessageTimeStamp.setVisibility(View.VISIBLE);
        //------------
        mInvestigationNotificationAlertAdapter = new UnreadNotificationAlertAdapter(this, appAlertList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mInvestigationsListView.setLayoutManager(mLayoutManager);
        mInvestigationsListView.setAdapter(mInvestigationNotificationAlertAdapter);

        if (!appAlertList.isEmpty())
            isAllListEmpty = false;
    }


    public int isShowFirstMessageTimeStamp(String notificationType) {
        int status = mAppointmentsFirstMessageTimeStamp.getVisibility();
        if (notificationType.equalsIgnoreCase(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT)) {
            status = mAppointmentsFirstMessageTimeStamp.getVisibility();
        } else if (notificationType.equalsIgnoreCase(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT)) {
            status = mInvestigationFirstMessageTimeStamp.getVisibility();
        } else if (notificationType.equalsIgnoreCase(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.TOKEN_ALERT_COUNT)) {
            status = mGetTokenFirstMessageTimeStamp.getVisibility();
        } else if (notificationType.equalsIgnoreCase(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT)) {
            status = mOnGoingMedicationFirstMessageTimeStamp.getVisibility();
        }
        return status;
    }

    @Override
    public void onMoreClicked(UnreadSavedNotificationMessageData unreadNotificationMessageData) {
        if (RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT.equalsIgnoreCase(unreadNotificationMessageData.getNotificationMessageType())) {
            mAppointmentNotificationAlertAdapter.addAllElementToList();
            mAppointmentsFirstMessageTimeStamp.setVisibility(View.INVISIBLE);
            mAppointmentNotificationAlertAdapter.notifyDataSetChanged();

        } else if (RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT.equalsIgnoreCase(unreadNotificationMessageData.getNotificationMessageType())) {
            mInvestigationNotificationAlertAdapter.addAllElementToList();
            mInvestigationFirstMessageTimeStamp.setVisibility(View.INVISIBLE);
            mInvestigationNotificationAlertAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSkipClicked(UnreadSavedNotificationMessageData unreadNotificationMessageData) {
        this.mClickedUnreadInvestigationMessageData = unreadNotificationMessageData;
        InvestigationHelper mInvestigationHelper = new InvestigationHelper(this, this);
        InvestigationNotification data = new Gson().fromJson(unreadNotificationMessageData.getNotificationData(), InvestigationNotification.class);
        mInvestigationHelper.doSkipInvestigation(data.getNotifications().get(0).getId(), true);

        clearNotification(this, INVESTIGATION_NOTIFICATION_TAG, unreadNotificationMessageData.getId());
    }

    @Override
    public void onNotificationRowClicked(UnreadSavedNotificationMessageData unreadNotificationMessageData) {
        AppDBHelper instance = AppDBHelper.getInstance(this);
        if (RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT.equalsIgnoreCase(unreadNotificationMessageData.getNotificationMessageType())) {
            int unReadCount = instance.deleteUnreadReceivedNotificationMessage(unreadNotificationMessageData.getId(), unreadNotificationMessageData.getNotificationMessageType());
            Intent intentNotification = new Intent(this, AppointmentActivity.class);
            intentNotification.putExtra(RescribeConstants.CALL_FROM_DASHBOARD, "");
            startActivity(intentNotification);

            if (unReadCount == 0) {
                isAllListEmpty = true;
                showMessage();
            }

            clearNotification(this, APPOINTMENT_NOTIFICATION_TAG, unreadNotificationMessageData.getId());

        } else if (RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT.equalsIgnoreCase(unreadNotificationMessageData.getNotificationMessageType())) {
            String notificationData = unreadNotificationMessageData.getNotificationData();

            Intent intentNotification = new Intent(this, InvestigationActivity.class);
            InvestigationNotification investigationNotification = new Gson().fromJson(notificationData, InvestigationNotification.class);
            intentNotification.putExtra(RescribeConstants.INVESTIGATION_LIST, investigationNotification.getNotifications());
            intentNotification.putExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_TIME, unreadNotificationMessageData.getNotificationTimeStamp());
            intentNotification.putExtra(RescribeConstants.NOTIFICATION_ID, unreadNotificationMessageData.getId());
            intentNotification.putExtra(RescribeConstants.FROM, "unread");
            startActivity(intentNotification);

            /*int unReadCount = instance.deleteUnreadReceivedNotificationMessage(unreadNotificationMessageData.getId(), unreadNotificationMessageData.getNotificationMessageType());
            if (unReadCount == 0) {
                isAllListEmpty = true;
                showMessage();
            }
            clearNotification(this, INVESTIGATION_NOTIFICATION_TAG, unreadNotificationMessageData.getId());*/
            // OPEN INVESTIGATION SCREEN, pending for ganesh code
        }
    }


    //&&&&&&&&&&&&************** MEDICATION START------------------

    private void setMedicationAlertListAdapter(ArrayList<UnreadSavedNotificationMessageData> dataArrayList, boolean isMedicationLoadMoreFooterClickedPreviously) {

        mUnreadMedicationNotificationAdapter = new SectionedRecyclerViewAdapter();
        //------
        mUnreadMedicationNotificationMessageDataList.clear();
        mUnreadMedicationNotificationMessageDataList.addAll(dataArrayList);
        //------
        doCreateMedicationDataMap(isMedicationLoadMoreFooterClickedPreviously);
        mOnGoingMedicationListView.setAdapter(mUnreadMedicationNotificationAdapter);
    }

    @Override
    public void onMedicationLoadMoreFooterClicked() {
        isExpanded = true;
        doCreateMedicationDataMap(true);
        isMedicationLoadMoreFooterClickedPreviously = true;
        mOnGoingMedicationFirstMessageTimeStamp.setVisibility(View.INVISIBLE);
        mUnreadMedicationNotificationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMedicationCheckBoxClicked(Medication medication, String header, int mHeaderPosition, boolean checked) {

//      Ganesh
        medicationToUpdate = medication;
        medicationKeyUpdate = header;
        medicationToCheck = checked;

        //-------------
        String presDate = CommonMethods.getFormattedDate(CommonMethods.getCurrentDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
        //-------------
        mMedicationToNotificationHelper.doRespondToNotificationForNotificationAdapter(
                Integer.valueOf(RescribePreferencesManager.getString(
                        RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, this)),
                medication.getMedicinSlot(), medication.getMedicineId(), presDate, 0, RescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER_ADAPTER + "_" + mHeaderPosition, checked ? 1 : 0);

    }

    private void doCreateMedicationDataMap(boolean isRequiredAllElements) {

        mUnreadMedicationNotificationAdapter.removeAllSections();

        configureGroupChildMapList(mUnreadMedicationNotificationMessageDataList);
        Map.Entry<String, ArrayList<Medication>> entry = listDataChild.entrySet().iterator().next();

        //------ Show timeStamp of first element in header view------
        ArrayList<Medication> medications = listDataChild.get(entry.getKey());
        String date = medicNotificationTimeId.get(entry.getKey().split("\\|")[0]);

        String formattedDate = CommonMethods.getFormattedDate(date, RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE_PATTERN.DD_MM_YYYY);
        String time = CommonMethods.formatDateTime(date, RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.DATE_PATTERN.DD_MM_YYYY + " " + RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.TIME);
        String dayFromDate = CommonMethods.getDayFromDate(RescribeConstants.DATE_PATTERN.DD_MM_YYYY, formattedDate);
        if (getString(R.string.today).equalsIgnoreCase(dayFromDate)) {
            mOnGoingMedicationFirstMessageTimeStamp.setText(time);
        } else {
            mOnGoingMedicationFirstMessageTimeStamp.setText(dayFromDate + " " + time);
        }
        mOnGoingMedicationFirstMessageTimeStamp.setVisibility(View.VISIBLE);
        //------------

        for (String key : listDataChild.keySet()) {
            SectionParameters build = new SectionParameters.Builder(R.layout.tablet_notification_item)
                    .headerResourceId(R.layout.tablet_notification_item_header)
                    .build();

            if (!isRequiredAllElements && listDataChild.size() > 1) {
                build = new SectionParameters.Builder(R.layout.tablet_notification_item)
                        .headerResourceId(R.layout.tablet_notification_item_header)
                        .footerResourceId(R.layout.more_item_textview)
                        .build();
            }

            mUnreadMedicationNotificationAdapter.addSection(new UnreadMedicationNotificationAdapter(build, this, key, listDataChild.get(key), medicNotificationTimeId.get(key), this));

            // check is data there empty

            if (!medications.isEmpty())
                isAllListEmpty = false;

            if (!isRequiredAllElements)
                break;
        }
        mOnGoingMedicationListView.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
    }


    private void configureGroupChildMapList(ArrayList<UnreadSavedNotificationMessageData> dataArrayList) {
        Gson gson = new Gson();
        medicNotificationTimeId.clear();
        listDataChild.clear();

        for (UnreadSavedNotificationMessageData unreadSavedNotificationMessageData : dataArrayList) {
            NotificationData notificationData = gson.fromJson(unreadSavedNotificationMessageData.getNotificationData(), NotificationData.class);
            for (Medication medication : notificationData.getMedication()) {
                if (medication.isTabSelected() == 0) {
                    listDataChild.put(unreadSavedNotificationMessageData.getNotificationMessage(), notificationData.getMedication());
                    medicNotificationTimeId.put(unreadSavedNotificationMessageData.getNotificationMessage(), unreadSavedNotificationMessageData.getNotificationTimeStamp() + "|" + unreadSavedNotificationMessageData.getId());
                    break;
                }
            }
        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.startsWith(RescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER_ADAPTER)) {
            //---- update notification in db---

            ArrayList<Medication> medications = listDataChild.get(medicationKeyUpdate);
            String time = medicNotificationTimeId.get(medicationKeyUpdate).split("\\|")[0];
            String id = medicNotificationTimeId.get(medicationKeyUpdate).split("\\|")[1];

            ArrayList<Integer> tempArrayForId = new ArrayList<>();

            for (Medication medication : medications) {
                if (medication.getMedicineId() == medicationToUpdate.getMedicineId())
                    medication.setTabSelected(medicationToCheck ? 1 : 0);

                if (medication.isTabSelected() == 1)
                    tempArrayForId.add(medication.getMedicineId());
            }

            NotificationData notificationData = new NotificationData();
            notificationData.setMedication(medications);
            notificationData.setPrescriptionDate(time);

            AppDBHelper appDBHelper = AppDBHelper.getInstance(this);
            appDBHelper.updateUnreadReceivedNotificationMessage(id, RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT, new Gson().toJson(notificationData));

            if (tempArrayForId.size() == medications.size()) {

                clearNotification(this, MEDICATIONS_NOTIFICATION_TAG, id);
                int unReadCount = appDBHelper.deleteUnreadReceivedNotificationMessage(id, RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT);

                if (unReadCount == 0) {
                    isAllListEmpty = true;
                    showMessage();
                }
            }

            initializeMedicationListView();
            //-------
        } else if (mOldDataTag.equals(RescribeConstants.TASK_TO_GET_TOKEN_REMAINDER_UNREAD_NOTIFICATIONS)) {
            UnreadBookAppointTokenNotificationBaseModel customResponse1 = (UnreadBookAppointTokenNotificationBaseModel) customResponse;
            if (customResponse1 != null) {
                UnreadBookAppointTokenNotificationBaseModel.UnreadTokenNotificationDataModel dataModel = customResponse1.getUnreadTokenNotificationDataModel();
                if (dataModel != null) {
                    ArrayList<UnreadBookAppointTokenNotificationData> list = dataModel.getUnreadTokenNotificationDataList();
                    if (list != null) {
                        if (!list.isEmpty()) {
                            setUnreadBookAppointTokenAlertListAdapter(list);
                            isAllListEmpty = false;
                        } else {
                            unreadTokenNotificationListViewLayout.setVisibility(View.GONE);
                        }
                    } else {
                        unreadTokenNotificationListViewLayout.setVisibility(View.GONE);
                    }
                } else {
                    unreadTokenNotificationListViewLayout.setVisibility(View.GONE);
                }
            } else {
                unreadTokenNotificationListViewLayout.setVisibility(View.GONE);
            }
        } else if (mOldDataTag.equals(RescribeConstants.TASK_TO_REJECT_RECEIVED_TOKEN_NOTIFICATION_REMAINDER) ||
                mOldDataTag.equals(RescribeConstants.TASK_TO_UNREAD_TOKEN_REMAINDER_CONFIRMATION)) {
            CommonBaseModelContainer commonbject = (CommonBaseModelContainer) customResponse;
            CommonMethods.showToast(this, commonbject.getCommonRespose().getStatusMessage());

            doGetUnreadTokenNotification();
        } else if (RescribeConstants.TASK_DO_SKIP_INVESTIGATION.equals(mOldDataTag)) {
            CommonBaseModelContainer commonbject = (CommonBaseModelContainer) customResponse;
            CommonMethods.showToast(this, commonbject.getCommonRespose().getStatusMessage());
            AppDBHelper instance = AppDBHelper.getInstance(this);

            int unReadCount = instance.deleteUnreadReceivedNotificationMessage(mClickedUnreadInvestigationMessageData.getId(), mClickedUnreadInvestigationMessageData.getNotificationMessageType());

            if (unReadCount == 0) {
                isAllListEmpty = true;
                showMessage();
            }

            initializeInvestigationListView();
        }

    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        CommonMethods.showToast(this, errorMessage);
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(this, serverErrorMessage);

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(this, serverErrorMessage);

    }

    //-------
    @OnClick({R.id.bookAppointmentBackButton})
    public void onViewClick(View v) {

        switch (v.getId()) {
            case R.id.bookAppointmentBackButton:
                finish();
                break;
        }
    }


    //----************ Token notification :START------------
    private void doGetUnreadTokenNotification() {
        mDoctorDataHelper = new DoctorDataHelper(this, this);
        mDoctorDataHelper.doGetTokenUnreadNotification();
    }


    private void setUnreadBookAppointTokenAlertListAdapter(ArrayList<UnreadBookAppointTokenNotificationData> appAlertList) {

        UnreadBookAppointTokenNotificationData unreadSavedNotificationMessageData = appAlertList.get(0);
        String formattedDate = CommonMethods.getFormattedDate(unreadSavedNotificationMessageData.getCreatedDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE_PATTERN.DD_MM_YYYY);
        String time = CommonMethods.formatDateTime(unreadSavedNotificationMessageData.getCreatedDate(), RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.DATE_PATTERN.DD_MM_YYYY + " " + RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.TIME);
        String dayFromDate = CommonMethods.getDayFromDate(RescribeConstants.DATE_PATTERN.DD_MM_YYYY, formattedDate);

        if (getString(R.string.today).equalsIgnoreCase(dayFromDate)) {
            mGetTokenFirstMessageTimeStamp.setText(time);

        } else {
            mGetTokenFirstMessageTimeStamp.setText(dayFromDate + " " + time);
        }
        mGetTokenFirstMessageTimeStamp.setVisibility(View.VISIBLE);
        //--------------
        unreadTokenNotificationListViewLayout.setVisibility(View.VISIBLE);
        mUnreadBookAppointTokenNotificationAdapter = new UnreadBookAppointTokenNotificationAdapter(this, appAlertList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mUnreadTokenNotificationListView.setLayoutManager(mLayoutManager);
        mUnreadTokenNotificationListView.setAdapter(mUnreadBookAppointTokenNotificationAdapter);
    }


    @Override
    public void onTokenMoreButtonClicked(UnreadBookAppointTokenNotificationData unreadNotificationMessageData) {
        mUnreadBookAppointTokenNotificationAdapter.addAllElementToList();
        mGetTokenFirstMessageTimeStamp.setVisibility(View.INVISIBLE);
        mUnreadBookAppointTokenNotificationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onButtonClicked(String type, UnreadBookAppointTokenNotificationData unreadNotificationMessageData) {
        if (type.equalsIgnoreCase(getString(R.string.no)))
            mDoctorDataHelper.doRejectBookAppointReceivedToken(unreadNotificationMessageData.getTime(), unreadNotificationMessageData.getDocId(), unreadNotificationMessageData.getLocationId());
        else
            mDoctorDataHelper.doConfirmBookAppointReceivedToken(unreadNotificationMessageData.getTime(), unreadNotificationMessageData.getDocId(), unreadNotificationMessageData.getLocationId());
    }

    @Override
    public void onNotificationRowClicked(UnreadBookAppointTokenNotificationData unreadNotificationMessageData) {

    }
    //----************ Token notification :END------------

    //------

    @OnClick({R.id.bookAppointmentBackButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bookAppointmentBackButton:
                onBackPressed();
                break;
        }
    }
}