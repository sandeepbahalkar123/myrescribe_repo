package com.rescribe.ui.activities.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.rescribe.R;
import com.rescribe.adapters.unread_notification_message_list.UnreadAppointmentNotificationAlert;
import com.rescribe.adapters.unread_notification_message_list.UnreadBookAppointTokenNotificationAdapter;
import com.rescribe.adapters.unread_notification_message_list.UnreadMedicationNotificationAdapter;
import com.rescribe.helpers.book_appointment.DoctorDataHelper;
import com.rescribe.helpers.dashboard.DashboardHelper;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.notification.RespondToNotificationHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.CommonBaseModelContainer;
import com.rescribe.model.book_appointment.unread_token_notification.UnreadBookAppointTokenNotificationBaseModel;
import com.rescribe.model.book_appointment.unread_token_notification.UnreadBookAppointTokenNotificationData;
import com.rescribe.model.dashboard_api.unread_notification_message_list.UnreadSavedNotificationMessageData;
import com.rescribe.model.notification.Medication;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.activities.AppointmentActivity;
import com.rescribe.ui.activities.InvestigationActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

/**
 * Created by jeetal on 27/11/17.
 */

public class UnreadNotificationMessageActivity extends AppCompatActivity implements HelperResponse, UnreadAppointmentNotificationAlert.OnNotificationItemClicked, UnreadMedicationNotificationAdapter.OnMedicationNotificationEventClick, UnreadBookAppointTokenNotificationAdapter.OnUnreadTokenNotificationItemClicked {

    @BindView(R.id.bookAppointmentBackButton)
    ImageView mBackButton;
    @BindView(R.id.title)
    CustomTextView mTitleView;
    @BindView(R.id.appointmentsListView)
    RecyclerView mAppointmentAlertList;
    @BindView(R.id.docConnectListView)
    RecyclerView mDocConnectListView;
    @BindView(R.id.investigationsListView)
    RecyclerView mInvestigationsListView;
    @BindView(R.id.unreadTokenNotificationListView)
    RecyclerView mUnreadTokenNotificationListView;
    //----------

    @BindView(R.id.onGoingMedicationListView)
    RecyclerView mOnGoingMedicationListView;
    //------------
    @BindView(R.id.onGoingMedicationListViewLayout)
    LinearLayout mOnGoingMedicationListViewLayout;
    @BindView(R.id.investigationsListViewLayout)
    LinearLayout investigationsListViewLayout;
    @BindView(R.id.appointmentsListViewLayout)
    LinearLayout appointmentsListViewLayout;
    @BindView(R.id.docConnectListViewLayout)
    LinearLayout docConnectListViewLayout;
    @BindView(R.id.unreadTokenNotificationListViewLayout)
    LinearLayout unreadTokenNotificationListViewLayout;
    //-----------
    private UnreadAppointmentNotificationAlert mAppointmentNotificationAlertAdapter;
    private UnreadAppointmentNotificationAlert mInvestigationNotificationAlertAdapter;
    private UnreadBookAppointTokenNotificationAdapter mUnreadBookAppointTokenNotificationAdapter;
    private SectionedRecyclerViewAdapter mUnreadMedicationNotificationAdapter;
    private ArrayList<UnreadSavedNotificationMessageData> mUnreadMedicationNotificationMessageDataList;
    private RespondToNotificationHelper mMedicationToNotificationHelper;
    private String mMedicationCheckBoxClickedData;
    private DoctorDataHelper mDoctorDataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_all_unread_notification_layout);
        ButterKnife.bind(this);

        mTitleView.setText(getString(R.string.notification));


    }

    @Override
    protected void onResume() {
        super.onResume();
        initialize();
        doGetUnreadTokenNotification();
    }

    private void initialize() {
        ArrayList<UnreadSavedNotificationMessageData> appAlertList = DashboardHelper.doFindUnreadNotificationMessageByType(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT);
        ArrayList<UnreadSavedNotificationMessageData> investigationAlertList = DashboardHelper.doFindUnreadNotificationMessageByType(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT);
        ArrayList<UnreadSavedNotificationMessageData> medicationAlertList = DashboardHelper.doFindUnreadNotificationMessageByType(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT);

        //----------------
        if (appAlertList.isEmpty()) {
            appointmentsListViewLayout.setVisibility(View.GONE);
        } else {
            appointmentsListViewLayout.setVisibility(View.VISIBLE);
            setAppointmentAlertListAdapter(appAlertList);
        }
        //----------------
        // ----------------
        if (investigationAlertList.isEmpty()) {
            investigationsListViewLayout.setVisibility(View.GONE);
        } else {
            investigationsListViewLayout.setVisibility(View.VISIBLE);
            setInvestigationAlertListAdapter(investigationAlertList);
        }
        //----------------
        if (medicationAlertList.isEmpty()) {
            mOnGoingMedicationListViewLayout.setVisibility(View.GONE);
        } else {
            mMedicationToNotificationHelper = new RespondToNotificationHelper(this, this);

            mOnGoingMedicationListViewLayout.setVisibility(View.VISIBLE);
            setMedicationAlertListAdapter(medicationAlertList);
        }

    }

    private void setAppointmentAlertListAdapter(ArrayList<UnreadSavedNotificationMessageData> appAlertList) {
        mAppointmentNotificationAlertAdapter = new UnreadAppointmentNotificationAlert(this, appAlertList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mAppointmentAlertList.setLayoutManager(mLayoutManager);
        mAppointmentAlertList.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mAppointmentAlertList.getContext(),
                DividerItemDecoration.VERTICAL);
        mAppointmentAlertList.addItemDecoration(dividerItemDecoration);
        mAppointmentAlertList.setAdapter(mAppointmentNotificationAlertAdapter);
    }

    private void setInvestigationAlertListAdapter(ArrayList<UnreadSavedNotificationMessageData> appAlertList) {
        mInvestigationNotificationAlertAdapter = new UnreadAppointmentNotificationAlert(this, appAlertList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mInvestigationsListView.setLayoutManager(mLayoutManager);
        mInvestigationsListView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mInvestigationsListView.getContext(),
                DividerItemDecoration.VERTICAL);
        mInvestigationsListView.addItemDecoration(dividerItemDecoration);
        mInvestigationsListView.setAdapter(mInvestigationNotificationAlertAdapter);
    }


    @Override
    public void onMoreClicked(UnreadSavedNotificationMessageData unreadNotificationMessageData) {
        if (RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT.equalsIgnoreCase(unreadNotificationMessageData.getNotificationMessageType())) {
            mAppointmentNotificationAlertAdapter.addAllElementToList();
            mAppointmentNotificationAlertAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSkipClicked() {

    }

    @Override
    public void onNotificationRowClicked(UnreadSavedNotificationMessageData unreadNotificationMessageData) {
        if (RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT.equalsIgnoreCase(unreadNotificationMessageData.getNotificationMessageType())) {
            new AppDBHelper(this).deleteUnreadReceivedNotificationMessage(Integer.parseInt(unreadNotificationMessageData.getId()), unreadNotificationMessageData.getNotificationMessageType());
            DashboardHelper.deleteUnreadNotificationMessageById(unreadNotificationMessageData.getId(), unreadNotificationMessageData.getNotificationMessageType());
            Intent intentNotification = new Intent(this, AppointmentActivity.class);
            startActivity(intentNotification);
        } else if (RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT.equalsIgnoreCase(unreadNotificationMessageData.getNotificationMessageType())) {
            new AppDBHelper(this).deleteUnreadReceivedNotificationMessage(Integer.parseInt(unreadNotificationMessageData.getId()), unreadNotificationMessageData.getNotificationMessageType());
            DashboardHelper.deleteUnreadNotificationMessageById(unreadNotificationMessageData.getId(), unreadNotificationMessageData.getNotificationMessageType());

            // OPEN INVESTIGATION SCREEN, pending for ganesh code
        }
    }


    //&&&&&&&&&&&&************** MEDICATION START------------------

    private void setMedicationAlertListAdapter(ArrayList<UnreadSavedNotificationMessageData> dataArrayList) {

        mUnreadMedicationNotificationAdapter = new SectionedRecyclerViewAdapter();
        //------
        mUnreadMedicationNotificationMessageDataList = new ArrayList<UnreadSavedNotificationMessageData>();
        mUnreadMedicationNotificationMessageDataList.addAll(dataArrayList);
        //------
        doCreateMedicationDataMap(false);
        mOnGoingMedicationListView.setAdapter(mUnreadMedicationNotificationAdapter);
    }

    @Override
    public void onMedicationLoadMoreFooterClicked() {
        doCreateMedicationDataMap(true);
        mUnreadMedicationNotificationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMedicationLoadLessFooterClicked() {
        doCreateMedicationDataMap(false);
        mUnreadMedicationNotificationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMedicationCheckBoxClicked(Medication medication, String header, int mHeaderPosition) {
        //--------------
        String dinnerMed = getString(R.string.dinner_medication).toString();
        String lunchMed = getString(R.string.lunch_medication).toString();
        String snacks_med = getString(R.string.snacks_medication).toString();
        String breakfast_med = getString(R.string.breakfast_medication).toString();

        //---- Save notification in db---
        String unreadNotificationMessageData = medication.getUnreadNotificationMessageDataID();
        mMedicationCheckBoxClickedData = unreadNotificationMessageData + "|" + header + "|" + new Gson().toJson(medication);
        //-------

        String slotType;
        if (header.endsWith(dinnerMed)) {
            slotType = RescribeConstants.DINNER;
        } else if (header.endsWith(lunchMed)) {
            slotType = RescribeConstants.LUNCH;
        } else if (header.endsWith(snacks_med)) {
            slotType = RescribeConstants.SNACKS;
        } else {
            slotType = RescribeConstants.BREAK_FAST;
        }
        //-------------
        String presDate = CommonMethods.getCurrentDate();
        //-------------
        mMedicationToNotificationHelper.doRespondToNotificationForNotificationAdapter(
                Integer.valueOf(RescribePreferencesManager.getString(
                        RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, this)),
                slotType, medication.getMedicineId(), presDate, 1, RescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER_ADAPTER + "_" + mHeaderPosition);

    }

    private void doCreateMedicationDataMap(boolean isRequiredAllElements) {

        mUnreadMedicationNotificationAdapter.removeAllSections();

        HashSet<String> strings = configureHeaderMapList(mUnreadMedicationNotificationMessageDataList, isRequiredAllElements);
        HashMap<String, ArrayList<Medication>> stringArrayListHashMap = configureGroupChildMapList(strings, mUnreadMedicationNotificationMessageDataList);

        int count = 0;
        for (String key : stringArrayListHashMap.keySet()) {
            count = count + 1;
            SectionParameters build = new SectionParameters.Builder(R.layout.tablet_notification_item)
                    .headerResourceId(R.layout.tablet_notification_item_header)
                    .build();
            if (isRequiredAllElements && count == 1) {
                build = new SectionParameters.Builder(R.layout.tablet_notification_item)
                        .headerResourceId(R.layout.tablet_notification_item_header)
                        .footerResourceId(R.layout.more_item_textview)
                        .build();
            } else if (!isRequiredAllElements && strings.size() > 1) {
                build = new SectionParameters.Builder(R.layout.tablet_notification_item)
                        .headerResourceId(R.layout.tablet_notification_item_header)
                        .footerResourceId(R.layout.more_item_textview)
                        .build();
            }
            mUnreadMedicationNotificationAdapter.addSection(new UnreadMedicationNotificationAdapter(build, this, key, stringArrayListHashMap.get(key), !isRequiredAllElements, this));
            if (!isRequiredAllElements) {
                break;
            }
        }
        mOnGoingMedicationListView.setLayoutManager(new LinearLayoutManager(this));
    }


    private HashMap<String, ArrayList<Medication>> configureGroupChildMapList(HashSet<String> listDataGroup, ArrayList<UnreadSavedNotificationMessageData> dataArrayList) {
        Gson gson = new Gson();
        HashMap<String, ArrayList<Medication>> listDataChild = new HashMap<>();
        //-- set child data
        for (String groupName :
                listDataGroup) {
            ArrayList<Medication> temp = new ArrayList<>();
            for (UnreadSavedNotificationMessageData dataObject :
                    dataArrayList) {
                String[] notificationData = dataObject.getNotificationData().split("\\|");
                if (notificationData[0].equalsIgnoreCase(groupName)) {
                    Medication medication = gson.fromJson(notificationData[1], Medication.class);
                    temp.add(medication);
                }
            }
            listDataChild.put(groupName, temp);
        }
        //------
        return listDataChild;
    }

    private HashSet<String> configureHeaderMapList(ArrayList<UnreadSavedNotificationMessageData> dataArrayList, boolean isRequiredAllElements) {

        HashSet<String> listDataGroup = new HashSet<>();

        //--- set header data
        for (UnreadSavedNotificationMessageData dataObject :
                dataArrayList) {
            String[] notificationData = dataObject.getNotificationData().split("\\|");
            listDataGroup.add(notificationData[0]);
            if (!isRequiredAllElements)
                break;
        }
        return listDataGroup;
    }
    //&&&&&&&&&&&&************** MEDICATION END------------------


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.startsWith(RescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER_ADAPTER)) {
            //---- update notification in db---
            AppDBHelper appDBHelper = new AppDBHelper(getApplicationContext());
            String[] split = mMedicationCheckBoxClickedData.split("\\|");
            appDBHelper.updateUnreadReceivedNotificationMessage(split[0], RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT, split[1] + "|" + split[2]);
            //----$$$$$$$$$$$$$ Delete medication if all medicines are isTabSelected=true------

            ArrayList<UnreadSavedNotificationMessageData> unreadNotificationMessageData = appDBHelper.doGetUnreadReceivedNotificationMessage();
            DashboardHelper.setUnreadNotificationMessageList(unreadNotificationMessageData);
            ArrayList<UnreadSavedNotificationMessageData> medicationAlertList = DashboardHelper.doFindUnreadNotificationMessageByType(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT);

            //--******* get keys,value pair for data to delete:START --
            HashSet<String> groupHeader = configureHeaderMapList(medicationAlertList, true);
            HashMap<String, ArrayList<Medication>> groupHeaderAndMap = configureGroupChildMapList(groupHeader, medicationAlertList);
            //--******* get keys,value pair for data to delete:END --
            //--******* ITERATE AND DELETE MEDICATION with isTABselected=true : START
            for (Map.Entry<String, ArrayList<Medication>> entry : groupHeaderAndMap.entrySet()) {
                ArrayList<Medication> value = entry.getValue();
                ArrayList<String> tempArryForId = new ArrayList<>();
                for (Medication dataObject :
                        value) {
                    if (dataObject.isTabSelected()) {
                        tempArryForId.add(dataObject.getUnreadNotificationMessageDataID());
                    }
                }
                if (tempArryForId.size() == value.size()) {
                    for (String idsToDelete :
                            tempArryForId) {
                        appDBHelper.deleteUnreadReceivedNotificationMessage(Integer.parseInt(idsToDelete), RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT);
                    }
                }
            }
            //--******* ITERATE AND DELETE MEDICATION with isTABselected=true : END
            //--******* RE-INITIALIZE UnreadNotificationMessageList----
            DashboardHelper.setUnreadNotificationMessageList(appDBHelper.doGetUnreadReceivedNotificationMessage());
            initialize();
            //-------
        } else if (mOldDataTag == RescribeConstants.TASK_TO_GET_TOKEN_REMAINDER_UNREAD_NOTIFICATIONS) {
            UnreadBookAppointTokenNotificationBaseModel customResponse1 = (UnreadBookAppointTokenNotificationBaseModel) customResponse;
            if (customResponse1 != null) {
                UnreadBookAppointTokenNotificationBaseModel.UnreadTokenNotificationDataModel dataModel = customResponse1.getUnreadTokenNotificationDataModel();
                if (dataModel != null) {
                    ArrayList<UnreadBookAppointTokenNotificationData> list = dataModel.getUnreadTokenNotificationDataList();
                    if (list != null) {
                        if (!list.isEmpty()) {
                            setUnreadBookAppointTokenAlertListAdapter(list);
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
        } else if (mOldDataTag == RescribeConstants.TASK_TO_REJECT_RECEIVED_TOKEN_NOTIFICATION_REMAINDER ||
                mOldDataTag == RescribeConstants.TASK_TO_UNREAD_TOKEN_REMAINDER_CONFIRMATION) {
            CommonBaseModelContainer commonbject = (CommonBaseModelContainer) customResponse;
            CommonMethods.showToast(this, commonbject.getCommonRespose().getStatusMessage());

            doGetUnreadTokenNotification();
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
        mUnreadBookAppointTokenNotificationAdapter = new UnreadBookAppointTokenNotificationAdapter(this, appAlertList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mUnreadTokenNotificationListView.setLayoutManager(mLayoutManager);
        mUnreadTokenNotificationListView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mUnreadTokenNotificationListView.getContext(),
                DividerItemDecoration.VERTICAL);
        mUnreadTokenNotificationListView.addItemDecoration(dividerItemDecoration);
        mUnreadTokenNotificationListView.setAdapter(mUnreadBookAppointTokenNotificationAdapter);
    }


    @Override
    public void onTokenMoreButtonClicked(UnreadBookAppointTokenNotificationData unreadNotificationMessageData) {
        mUnreadBookAppointTokenNotificationAdapter.addAllElementToList();
        mUnreadBookAppointTokenNotificationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onButtonClicked(String type, UnreadBookAppointTokenNotificationData unreadNotificationMessageData) {
        if (type.equalsIgnoreCase(getString(R.string.no))) {
            mDoctorDataHelper.doRejectBookAppointReceivedToken(unreadNotificationMessageData.getTime(), unreadNotificationMessageData.getDocId(), unreadNotificationMessageData.getLocationId());
        } else {
            mDoctorDataHelper.doConfirmBookAppointReceivedToken(unreadNotificationMessageData.getTime(), unreadNotificationMessageData.getDocId(), unreadNotificationMessageData.getLocationId(), unreadNotificationMessageData.getTokenNumber());
        }
    }

    @Override
    public void onNotificationRowClicked(UnreadBookAppointTokenNotificationData unreadNotificationMessageData) {

    }
    //----************ Token notification :END------------

    //------
}
