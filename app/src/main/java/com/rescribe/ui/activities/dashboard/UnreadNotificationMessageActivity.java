package com.rescribe.ui.activities.dashboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.rescribe.R;
import com.rescribe.adapters.unread_notification_message_list.UnreadAppointmentNotificationAlert;
import com.rescribe.helpers.dashboard.DashboardHelper;
import com.rescribe.model.dashboard_api.unread_notification_message_list.UnreadNotificationMessageData;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.customesViews.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 27/11/17.
 */

public class UnreadNotificationMessageActivity extends AppCompatActivity {

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
    private UnreadAppointmentNotificationAlert mAppointmentNotificationAlertAdapter;

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
        ArrayList<UnreadNotificationMessageData> medAlertList = DashboardHelper.doFindUnreadNotificationMessageByType(RescribePreferencesManager.NOTIFICATION_COUNT_KEY.MEDICATION_ALERT_COUNT);

        setAppointmentAlertListAdapter(investigationAlertList);
        setInvestigationAlertListAdapter(investigationAlertList);

    }

    private void setAppointmentAlertListAdapter(ArrayList<UnreadNotificationMessageData> appAlertList) {
        mAppointmentNotificationAlertAdapter = new UnreadAppointmentNotificationAlert(this, appAlertList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mAppointmentAlertList.setLayoutManager(mLayoutManager);
        mAppointmentAlertList.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mAppointmentAlertList.getContext(),
                DividerItemDecoration.VERTICAL);
        mAppointmentAlertList.addItemDecoration(dividerItemDecoration);
        mAppointmentAlertList.setAdapter(mAppointmentNotificationAlertAdapter);


    }

    private void setInvestigationAlertListAdapter(ArrayList<UnreadNotificationMessageData> appAlertList) {
        mAppointmentNotificationAlertAdapter = new UnreadAppointmentNotificationAlert(this, appAlertList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mInvestigationsListView.setLayoutManager(mLayoutManager);
        mInvestigationsListView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mAppointmentAlertList.getContext(),
                DividerItemDecoration.VERTICAL);
        mInvestigationsListView.addItemDecoration(dividerItemDecoration);
        mInvestigationsListView.setAdapter(mAppointmentNotificationAlertAdapter);
    }


    @OnClick({R.id.bookAppointmentBackButton, R.id.locationTextView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bookAppointmentBackButton:
                onBackPressed();
                break;
            case R.id.locationTextView:
                break;
        }
    }
}
