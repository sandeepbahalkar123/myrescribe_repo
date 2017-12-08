package com.rescribe.ui.activities.dashboard;

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
import com.rescribe.adapters.unread_notification_message_list.UnreadMedicationNotificationAdapter_Test;
import com.rescribe.helpers.dashboard.DashboardHelper;
import com.rescribe.helpers.database.AppDBHelper;
import com.rescribe.helpers.notification.RespondToNotificationHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.dashboard_api.unread_notification_message_list.UnreadNotificationMessageData;
import com.rescribe.model.notification.Medication;
import com.rescribe.preference.RescribePreferencesManager;
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

public class UnreadNotificationMessageActivity extends AppCompatActivity implements HelperResponse, UnreadAppointmentNotificationAlert.OnNotificationItemClicked, UnreadMedicationNotificationAdapter_Test.OnMedicationNotificationEventClick {

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
    //-----------
    private UnreadAppointmentNotificationAlert mAppointmentNotificationAlertAdapter;
    private UnreadAppointmentNotificationAlert mInvestigationNotificationAlertAdapter;
    private SectionedRecyclerViewAdapter mUnreadMedicationNotificationAdapter;
    private ArrayList<UnreadNotificationMessageData> mUnreadMedicationNotificationMessageDataList;
    private RespondToNotificationHelper mMedicationToNotificationHelper;
    private String mMedicationCheckBoxClickedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_all_unread_notification_layout);
        ButterKnife.bind(this);

        mTitleView.setText(getString(R.string.notification));

        initialize();
    }

    private void initialize() {
        ArrayList<UnreadNotificationMessageData> appAlertList = DashboardHelper.doFindUnreadNotificationMessageByType(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT);
        ArrayList<UnreadNotificationMessageData> investigationAlertList = DashboardHelper.doFindUnreadNotificationMessageByType(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT);
        ArrayList<UnreadNotificationMessageData> medicationAlertList = DashboardHelper.doFindUnreadNotificationMessageByType(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT);

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

    private void setAppointmentAlertListAdapter(ArrayList<UnreadNotificationMessageData> appAlertList) {
        mAppointmentNotificationAlertAdapter = new UnreadAppointmentNotificationAlert(this, appAlertList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mAppointmentAlertList.setLayoutManager(mLayoutManager);
        mAppointmentAlertList.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mAppointmentAlertList.getContext(),
                DividerItemDecoration.VERTICAL);
        mAppointmentAlertList.addItemDecoration(dividerItemDecoration);
        mAppointmentAlertList.setAdapter(mAppointmentNotificationAlertAdapter);
    }

    private void setInvestigationAlertListAdapter(ArrayList<UnreadNotificationMessageData> appAlertList) {
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
    public void onMoreClicked(UnreadNotificationMessageData unreadNotificationMessageData) {
        if (RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT.equalsIgnoreCase(unreadNotificationMessageData.getNotificationMessageType())) {
            mAppointmentNotificationAlertAdapter.addAllElementToList();
            mAppointmentNotificationAlertAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLessClicked(UnreadNotificationMessageData unreadNotificationMessageData) {
        if (RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT.equalsIgnoreCase(unreadNotificationMessageData.getNotificationMessageType())) {
            mAppointmentNotificationAlertAdapter.addSingleElementToList();
            mAppointmentNotificationAlertAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSkipClicked() {

    }

    @Override
    public void onNotificationRowClicked(UnreadNotificationMessageData unreadNotificationMessageData) {
        if (RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT.equalsIgnoreCase(unreadNotificationMessageData.getNotificationMessageType())) {
            new AppDBHelper(this).deleteUnreadReceivedNotificationMessage(Integer.parseInt(unreadNotificationMessageData.getId()), unreadNotificationMessageData.getNotificationMessageType());
            DashboardHelper.deleteUnreadNotificationMessageById(unreadNotificationMessageData.getId(), unreadNotificationMessageData.getNotificationMessageType());
            initialize();
        } else if (RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT.equalsIgnoreCase(unreadNotificationMessageData.getNotificationMessageType())) {
            new AppDBHelper(this).deleteUnreadReceivedNotificationMessage(Integer.parseInt(unreadNotificationMessageData.getId()), unreadNotificationMessageData.getNotificationMessageType());
            DashboardHelper.deleteUnreadNotificationMessageById(unreadNotificationMessageData.getId(), unreadNotificationMessageData.getNotificationMessageType());
            initialize();
        }
    }


    //&&&&&&&&&&&&************** MEDICATION START------------------

    private void setMedicationAlertListAdapter(ArrayList<UnreadNotificationMessageData> dataArrayList) {

        mUnreadMedicationNotificationAdapter = new SectionedRecyclerViewAdapter();
        //------
        mUnreadMedicationNotificationMessageDataList = new ArrayList<UnreadNotificationMessageData>();
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
            } else if (!isRequiredAllElements) {
                build = new SectionParameters.Builder(R.layout.tablet_notification_item)
                        .headerResourceId(R.layout.tablet_notification_item_header)
                        .footerResourceId(R.layout.more_item_textview)
                        .build();
            }
            mUnreadMedicationNotificationAdapter.addSection(new UnreadMedicationNotificationAdapter_Test(build, this, key, stringArrayListHashMap.get(key), !isRequiredAllElements, this));
            if (!isRequiredAllElements) {
                break;
            }
        }
        mOnGoingMedicationListView.setLayoutManager(new LinearLayoutManager(this));
    }


    private HashMap<String, ArrayList<Medication>> configureGroupChildMapList(HashSet<String> listDataGroup, ArrayList<UnreadNotificationMessageData> dataArrayList) {
        Gson gson = new Gson();
        HashMap<String, ArrayList<Medication>> listDataChild = new HashMap<>();
        //-- set child data
        for (String groupName :
                listDataGroup) {
            ArrayList<Medication> temp = new ArrayList<>();
            for (UnreadNotificationMessageData dataObject :
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

    private HashSet<String> configureHeaderMapList(ArrayList<UnreadNotificationMessageData> dataArrayList, boolean isRequiredAllElements) {

        HashSet<String> listDataGroup = new HashSet<>();

        //--- set header data
        for (UnreadNotificationMessageData dataObject :
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

            ArrayList<UnreadNotificationMessageData> unreadNotificationMessageData = appDBHelper.doGetUnreadReceivedNotificationMessage();
            DashboardHelper.setUnreadNotificationMessageList(unreadNotificationMessageData);
            ArrayList<UnreadNotificationMessageData> medicationAlertList = DashboardHelper.doFindUnreadNotificationMessageByType(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT);

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

    //-------
    @OnClick({R.id.bookAppointmentBackButton})
    public void onViewClick(View v) {

        switch (v.getId()) {
            case R.id.bookAppointmentBackButton:
                finish();
                break;
        }
    }
    //------
}
