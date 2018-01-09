package com.rescribe.ui.activities.dashboard;

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
import com.rescribe.adapters.unread_notification_message_list.UnreadAppointmentNotificationAlert;
import com.rescribe.adapters.unread_notification_message_list.UnreadBookAppointTokenNotificationAdapter;
import com.rescribe.adapters.unread_notification_message_list.UnreadChatNotificationList;
import com.rescribe.adapters.unread_notification_message_list.UnreadMedicationNotificationAdapter;
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
import com.rescribe.model.doctor_connect.ChatDoctor;
import com.rescribe.model.investigation.InvestigationNotification;
import com.rescribe.model.notification.Medication;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.singleton.RescribeApplication;
import com.rescribe.ui.activities.AppointmentActivity;
import com.rescribe.ui.activities.ChatActivity;
import com.rescribe.ui.activities.InvestigationActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

import static com.rescribe.notification.DosesAlarmTask.BREAKFAST_NOTIFICATION_ID;
import static com.rescribe.notification.DosesAlarmTask.DINNER_NOTIFICATION_ID;
import static com.rescribe.notification.DosesAlarmTask.EVENING_NOTIFICATION_ID;
import static com.rescribe.notification.DosesAlarmTask.LUNCH_NOTIFICATION_ID;
import static com.rescribe.singleton.RescribeApplication.clearNotification;
import static com.rescribe.util.RescribeConstants.APPOINTMENT_NOTIFICATION_TAG;
import static com.rescribe.util.RescribeConstants.INVESTIGATION_NOTIFICATION_TAG;
import static com.rescribe.util.RescribeConstants.MEDICATIONS_NOTIFICATION_TAG;
import static com.rescribe.util.RescribeConstants.USER_STATUS.ONLINE;


/**
 * Created by jeetal on 27/11/17.
 */

public class UnreadNotificationMessageActivity extends AppCompatActivity implements HelperResponse, UnreadAppointmentNotificationAlert.OnNotificationItemClicked, UnreadMedicationNotificationAdapter.OnMedicationNotificationEventClick, UnreadBookAppointTokenNotificationAdapter.OnUnreadTokenNotificationItemClicked, UnreadChatNotificationList.OnDocConnectItemClicked {

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
    @BindView(R.id.onGoingMedicationListView)
    RecyclerView mOnGoingMedicationListView;
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
    @BindView(R.id.getTokenFirstMessageTimeStamp)
    CustomTextView mGetTokenFirstMessageTimeStamp;
    @BindView(R.id.onGoingMedicationFirstMessageTimeStamp)
    CustomTextView mOnGoingMedicationFirstMessageTimeStamp;
    @BindView(R.id.appointmentsFirstMessageTimeStamp)
    CustomTextView mAppointmentsFirstMessageTimeStamp;
    @BindView(R.id.investigationFirstMessageTimeStamp)
    CustomTextView mInvestigationFirstMessageTimeStamp;
    @BindView(R.id.doctorConnectFirstMessageTimeStamp)
    CustomTextView mDoctorConnectFirstMessageTimeStamp;
    @BindView(R.id.emptyListMessageView)
    ImageView emptyListMessageView;
    private UnreadAppointmentNotificationAlert mAppointmentNotificationAlertAdapter;
    private UnreadAppointmentNotificationAlert mInvestigationNotificationAlertAdapter;
    private UnreadChatNotificationList mUnreadChatNotificationListAdapter;
    private UnreadBookAppointTokenNotificationAdapter mUnreadBookAppointTokenNotificationAdapter;
    private SectionedRecyclerViewAdapter mUnreadMedicationNotificationAdapter;
    private ArrayList<UnreadSavedNotificationMessageData> mUnreadMedicationNotificationMessageDataList;
    private String mMedicationCheckBoxClickedData;
    private DoctorDataHelper mDoctorDataHelper;
    private RespondToNotificationHelper mMedicationToNotificationHelper;
    private InvestigationHelper mInvestigationHelper;
    private UnreadSavedNotificationMessageData mClickedUnreadInvestigationMessageData;
    private boolean isMedicationLoadMoreFooterClickedPreviously = false;
    public boolean isAllListEmpty = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_all_unread_notification_layout);
        ButterKnife.bind(this);
        mTitleView.setText(getString(R.string.notifications));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialize();
        doGetUnreadTokenNotification();
    }

    private void initialize() {
        initializeChatListView();
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

    private void initializeChatListView() {
        ArrayList<UnreadSavedNotificationMessageData> chatAlertList = RescribeApplication.doFindUnreadNotificationMessageByType(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.CHAT_ALERT_COUNT);
        if (chatAlertList.isEmpty()) {
            docConnectListViewLayout.setVisibility(View.GONE);
        } else {
            docConnectListViewLayout.setVisibility(View.VISIBLE);
            setUnreadChatAlertListAdapter(chatAlertList);
        }
        //--
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
        mAppointmentNotificationAlertAdapter = new UnreadAppointmentNotificationAlert(this, appAlertList, this);
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
        mInvestigationNotificationAlertAdapter = new UnreadAppointmentNotificationAlert(this, appAlertList, this);
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

    private void setUnreadChatAlertListAdapter(ArrayList<UnreadSavedNotificationMessageData> appAlertList) {
        mUnreadChatNotificationListAdapter = new UnreadChatNotificationList(this, appAlertList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mDocConnectListView.setLayoutManager(mLayoutManager);
        mDocConnectListView.setAdapter(mUnreadChatNotificationListAdapter);

        UnreadSavedNotificationMessageData unreadSavedNotificationMessageData = appAlertList.get(0);
        String formattedDate = CommonMethods.getFormattedDate(unreadSavedNotificationMessageData.getNotificationTimeStamp(), RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DATE_PATTERN.DD_MM_YYYY);
        String time = CommonMethods.formatDateTime(unreadSavedNotificationMessageData.getNotificationTimeStamp(), RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.TIME);
        String dayFromDate = CommonMethods.getDayFromDate(RescribeConstants.DATE_PATTERN.DD_MM_YYYY, formattedDate);

        if (getString(R.string.today).equalsIgnoreCase(dayFromDate)) {
            mDoctorConnectFirstMessageTimeStamp.setText(time);
        } else {
            mDoctorConnectFirstMessageTimeStamp.setText(dayFromDate + " " + time);
        }
        mDoctorConnectFirstMessageTimeStamp.setVisibility(View.VISIBLE);

        if (!appAlertList.isEmpty())
            isAllListEmpty = false;
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
        mInvestigationHelper = new InvestigationHelper(this, this);
        InvestigationNotification data = new Gson().fromJson(unreadNotificationMessageData.getNotificationData(), InvestigationNotification.class);
        mInvestigationHelper.doSkipInvestigation(data.getNotifications().get(0).getId(), true);

        clearNotification(this, INVESTIGATION_NOTIFICATION_TAG, unreadNotificationMessageData.getId());
    }

    @Override
    public void onNotificationRowClicked(UnreadSavedNotificationMessageData unreadNotificationMessageData) {
        AppDBHelper instance = AppDBHelper.getInstance(this);
        if (RescribePreferencesManager.NOTIFICATION_COUNT_KEY.APPOINTMENT_ALERT_COUNT.equalsIgnoreCase(unreadNotificationMessageData.getNotificationMessageType())) {
            int unReadCount = instance.deleteUnreadReceivedNotificationMessage(Integer.parseInt(unreadNotificationMessageData.getId()), unreadNotificationMessageData.getNotificationMessageType());
            Intent intentNotification = new Intent(this, AppointmentActivity.class);
            startActivity(intentNotification);

            if (unReadCount == 0) {
                isAllListEmpty = true;
                showMessage();
            }

            clearNotification(this, APPOINTMENT_NOTIFICATION_TAG, unreadNotificationMessageData.getId());

        } else if (RescribePreferencesManager.NOTIFICATION_COUNT_KEY.INVESTIGATION_ALERT_COUNT.equalsIgnoreCase(unreadNotificationMessageData.getNotificationMessageType())) {
            int unReadCount = instance.deleteUnreadReceivedNotificationMessage(Integer.parseInt(unreadNotificationMessageData.getId()), unreadNotificationMessageData.getNotificationMessageType());
            String notificationData = unreadNotificationMessageData.getNotificationData();

            Intent intentNotification = new Intent(this, InvestigationActivity.class);
            InvestigationNotification investigationNotification = new Gson().fromJson(notificationData, InvestigationNotification.class);
            intentNotification.putExtra(RescribeConstants.INVESTIGATION_LIST, investigationNotification.getNotifications());
            intentNotification.putExtra(RescribeConstants.INVESTIGATION_KEYS.INVESTIGATION_TIME, unreadNotificationMessageData.getNotificationTimeStamp());
            startActivity(intentNotification);

            if (unReadCount == 0) {
                isAllListEmpty = true;
                showMessage();
            }

            clearNotification(this, INVESTIGATION_NOTIFICATION_TAG, unreadNotificationMessageData.getId());
            // OPEN INVESTIGATION SCREEN, pending for ganesh code
        }
    }


    //&&&&&&&&&&&&************** MEDICATION START------------------

    private void setMedicationAlertListAdapter(ArrayList<UnreadSavedNotificationMessageData> dataArrayList, boolean isMedicationLoadMoreFooterClickedPreviously) {

        mUnreadMedicationNotificationAdapter = new SectionedRecyclerViewAdapter();
        //------
        mUnreadMedicationNotificationMessageDataList = new ArrayList<UnreadSavedNotificationMessageData>();
        mUnreadMedicationNotificationMessageDataList.addAll(dataArrayList);
        //------
        doCreateMedicationDataMap(isMedicationLoadMoreFooterClickedPreviously);
        mOnGoingMedicationListView.setAdapter(mUnreadMedicationNotificationAdapter);
    }

    @Override
    public void onMedicationLoadMoreFooterClicked() {
        doCreateMedicationDataMap(true);
        isMedicationLoadMoreFooterClickedPreviously = true;
        mOnGoingMedicationFirstMessageTimeStamp.setVisibility(View.INVISIBLE);
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
        String presDate = CommonMethods.getFormattedDate(CommonMethods.getCurrentDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
        //-------------
        mMedicationToNotificationHelper.doRespondToNotificationForNotificationAdapter(
                Integer.valueOf(RescribePreferencesManager.getString(
                        RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, this)),
                slotType, medication.getMedicineId(), presDate, 0, RescribeConstants.TASK_RESPOND_NOTIFICATION_FOR_HEADER_ADAPTER + "_" + mHeaderPosition);

    }

    private void doCreateMedicationDataMap(boolean isRequiredAllElements) {

        mUnreadMedicationNotificationAdapter.removeAllSections();

        TreeSet<String> mainActualHeaderList = configureHeaderMapList(mUnreadMedicationNotificationMessageDataList, true);

        String firstElementKey = mainActualHeaderList.first();
        HashMap<String, ArrayList<Medication>> stringArrayListHashMap = new HashMap<>();
        //-----if isReqAllElement==false, and find actual size of Group header list-------
        HashSet<String> formattedSet = new HashSet<>();
        if (!isRequiredAllElements) {
            if (!mainActualHeaderList.isEmpty()) {
                formattedSet.add(mainActualHeaderList.first());
                stringArrayListHashMap = configureGroupChildMapList(formattedSet, mUnreadMedicationNotificationMessageDataList);
            }
        } else {
            formattedSet.addAll(mainActualHeaderList);
            stringArrayListHashMap = configureGroupChildMapList(formattedSet, mUnreadMedicationNotificationMessageDataList);
        }
        //------ Show timeStamp of first element in header view------
        if (firstElementKey != null) {
            ArrayList<Medication> medications = stringArrayListHashMap.get(firstElementKey);
            if (!medications.isEmpty()) {
                Medication medication = medications.get(0);
                //---------
                String formattedDate = CommonMethods.getFormattedDate(medication.getUnreadNotificationMessageDataTimeStamp(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE_PATTERN.DD_MM_YYYY);
                String time = CommonMethods.formatDateTime(medication.getUnreadNotificationMessageDataTimeStamp(), RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.DATE_PATTERN.DD_MM_YYYY + " " + RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.TIME);
                String dayFromDate = CommonMethods.getDayFromDate(RescribeConstants.DATE_PATTERN.DD_MM_YYYY, formattedDate);
                if (getString(R.string.today).equalsIgnoreCase(dayFromDate)) {
                    mOnGoingMedicationFirstMessageTimeStamp.setText(time);
                } else {
                    mOnGoingMedicationFirstMessageTimeStamp.setText(dayFromDate + " " + time);
                }
                mOnGoingMedicationFirstMessageTimeStamp.setVisibility(View.VISIBLE);
                //---------
            }
        }
        //------------

        int count = 0;
        for (String key : stringArrayListHashMap.keySet()) {
            count = count + 1;
            SectionParameters build = new SectionParameters.Builder(R.layout.tablet_notification_item)
                    .headerResourceId(R.layout.tablet_notification_item_header)
                    .build();
            if (!isRequiredAllElements && mainActualHeaderList.size() > 1) {
                build = new SectionParameters.Builder(R.layout.tablet_notification_item)
                        .headerResourceId(R.layout.tablet_notification_item_header)
                        .footerResourceId(R.layout.more_item_textview)
                        .build();
            }

            ArrayList<Medication> medications = stringArrayListHashMap.get(key);

            Medication medication = medications.get(0);

            mUnreadMedicationNotificationAdapter.addSection(new UnreadMedicationNotificationAdapter(build, this, key, medications, !isRequiredAllElements, medication.getUnreadNotificationMessageDataTimeStamp(), this));
            if (!isRequiredAllElements) {
                break;
            }

            // check is data there empty

            if (!medications.isEmpty())
                isAllListEmpty = false;
        }
        mOnGoingMedicationListView.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
    }


    private HashMap<String, ArrayList<Medication>> configureGroupChildMapList(HashSet<String> listDataGroup, ArrayList<UnreadSavedNotificationMessageData> dataArrayList) {
        Gson gson = new Gson();
        HashMap<String, ArrayList<Medication>> listDataChild = new HashMap<>();
        //-- set child data
        for (String groupName : listDataGroup) {
            ArrayList<Medication> temp = new ArrayList<>();
            for (UnreadSavedNotificationMessageData dataObject : dataArrayList) {
                String notificationMessage = dataObject.getNotificationMessage();
                if (notificationMessage.equalsIgnoreCase(groupName)) {
                    Medication medication = gson.fromJson(dataObject.getNotificationData(), Medication.class);
                    temp.add(medication);
                }
            }
            listDataChild.put(groupName, temp);
        }
        //------
        return listDataChild;
    }

    private TreeSet<String> configureHeaderMapList(ArrayList<UnreadSavedNotificationMessageData> dataArrayList, boolean isRequiredAllElements) {

        TreeSet<String> listDataGroup = new TreeSet<>();

        //--- set header data
        for (UnreadSavedNotificationMessageData dataObject :
                dataArrayList) {
            listDataGroup.add(dataObject.getNotificationMessage());
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
            AppDBHelper appDBHelper = AppDBHelper.getInstance(this);
            String[] split = mMedicationCheckBoxClickedData.split("\\|");
            appDBHelper.updateUnreadReceivedNotificationMessage(split[0], RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT, split[2]);
            //----$$$$$$$$$$$$$ Delete medication if all medicines are isTabSelected=true------

            ArrayList<UnreadSavedNotificationMessageData> medicationAlertList = RescribeApplication.doFindUnreadNotificationMessageByType(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT);

            //--******* get keys,value pair for data to delete:START --
            TreeSet<String> groupHeader = configureHeaderMapList(medicationAlertList, true);
            HashMap<String, ArrayList<Medication>> groupHeaderAndMap = configureGroupChildMapList(new HashSet<String>(groupHeader), medicationAlertList);
            //--******* get keys,value pair for data to delete:END --
            //--******* ITERATE AND DELETE MEDICATION with isTABselected=true : START
            for (Map.Entry<String, ArrayList<Medication>> entry : groupHeaderAndMap.entrySet()) {
                ArrayList<Medication> value = entry.getValue();
                ArrayList<String> tempArryForId = new ArrayList<>();
                for (Medication dataObject : value) {
                    if (dataObject.isTabSelected()) {
                        tempArryForId.add(dataObject.getUnreadNotificationMessageDataID());
                    }
                }
                if (tempArryForId.size() == value.size()) {

                    String slot = entry.getKey();

                    if (slot.contains(getResources().getString(R.string.breakfast_medication)))
                        clearNotification(this, MEDICATIONS_NOTIFICATION_TAG, String.valueOf(BREAKFAST_NOTIFICATION_ID));
                    else if (slot.contains(getResources().getString(R.string.lunch_medication)))
                        clearNotification(this, MEDICATIONS_NOTIFICATION_TAG, String.valueOf(LUNCH_NOTIFICATION_ID));
                    else if (slot.contains(getResources().getString(R.string.snacks_medication)))
                        clearNotification(this, MEDICATIONS_NOTIFICATION_TAG, String.valueOf(EVENING_NOTIFICATION_ID));
                    else if (slot.contains(getResources().getString(R.string.dinner_medication)))
                        clearNotification(this, MEDICATIONS_NOTIFICATION_TAG, String.valueOf(DINNER_NOTIFICATION_ID));

                    for (String idsToDelete : tempArryForId) {
                        int unReadCount = appDBHelper.deleteUnreadReceivedNotificationMessage(Integer.parseInt(idsToDelete), RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT);
                        if (unReadCount == 0) {
                            isAllListEmpty = true;
                            showMessage();
                        }
                    }
                }
            }
            //--******* ITERATE AND DELETE MEDICATION with isTABselected=true : END
            //--******* RE-INITIALIZE UnreadNotificationMessageList----
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
        } else if (RescribeConstants.TASK_DO_SKIP_INVESTIGATION == mOldDataTag) {
            CommonBaseModelContainer commonbject = (CommonBaseModelContainer) customResponse;
            CommonMethods.showToast(this, commonbject.getCommonRespose().getStatusMessage());
            AppDBHelper instance = AppDBHelper.getInstance(this);

            int unReadCount = instance.deleteUnreadReceivedNotificationMessage(Integer.parseInt(mClickedUnreadInvestigationMessageData.getId()), mClickedUnreadInvestigationMessageData.getNotificationMessageType());

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

    @OnClick({R.id.bookAppointmentBackButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bookAppointmentBackButton:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onDocConnectMoreClicked(UnreadSavedNotificationMessageData unreadNotificationMessageData) {
        mDoctorConnectFirstMessageTimeStamp.setVisibility(View.INVISIBLE);
        mUnreadChatNotificationListAdapter.addAllElementToList();
        mUnreadChatNotificationListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDocConnectRowClicked(UnreadSavedNotificationMessageData unreadNotificationMessageData) {
        AppDBHelper instance = AppDBHelper.getInstance(this);
        int unReadCount = instance.deleteUnreadReceivedNotificationMessage(Integer.parseInt(unreadNotificationMessageData.getId()), unreadNotificationMessageData.getNotificationMessageType());

        ChatDoctor chatDoctor = new ChatDoctor();
        chatDoctor.setId(Integer.parseInt(unreadNotificationMessageData.getId()));
        chatDoctor.setDoctorName(unreadNotificationMessageData.getNotificationMessage().replace(getString(R.string.message_from), ""));
        chatDoctor.setOnlineStatus(ONLINE);
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(RescribeConstants.DOCTORS_INFO, chatDoctor);
        startActivity(intent);

        if (unReadCount == 0) {
            isAllListEmpty = true;
            showMessage();
        }
    }
}