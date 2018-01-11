package com.rescribe.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rescribe.R;
import com.rescribe.adapters.dashboard.NotificationSettingListAdapter;
import com.rescribe.model.dashboard_api.ClickEvent;
import com.rescribe.model.dashboard_api.ClickOption;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.customesViews.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationSettingActivity extends AppCompatActivity implements NotificationSettingListAdapter.OnSwitchButtonClickListener {

    @BindView(R.id.title)
    CustomTextView title;
    @BindView(R.id.listView)
    RecyclerView listView;
    private Context mContext;

    ClickOption mClickedOptionData;
    private NotificationSettingListAdapter mNotificationSettingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);
        ButterKnife.bind(this);
        mContext = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mClickedOptionData = extras.getParcelable(getString(R.string.clicked_item_data));
            title.setText(extras.getString(getString(R.string.toolbarTitle)));
        }
        switchListeners();
    }

    private void switchListeners() {

        if (mClickedOptionData != null) {
            ClickEvent clickEvent = mClickedOptionData.getClickEvent();
            ArrayList<ClickOption> clickOptions = clickEvent.getClickOptions();

            //----------
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            listView.setLayoutManager(layoutManager);
            listView.setHasFixedSize(true);
            DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                    listView.getContext(),
                    layoutManager.getOrientation()
            );
            listView.addItemDecoration(mDividerItemDecoration);
            //----------
            mNotificationSettingListAdapter = new NotificationSettingListAdapter(this, clickOptions, this);
            listView.setAdapter(mNotificationSettingListAdapter);

        }

    }

    @OnClick({R.id.backButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onClick(ClickOption clickOption, boolean isChecked) {
        if (clickOption.getName().equalsIgnoreCase(mContext.getString(R.string.all_notifications))) {
            RescribePreferencesManager.putBoolean(mContext.getString(R.string.all_notifications), isChecked, mContext);
            RescribePreferencesManager.putBoolean(mContext.getString(R.string.appointment_alert), isChecked, mContext);
            RescribePreferencesManager.putBoolean(mContext.getString(R.string.investigation_alert), isChecked, mContext);
            RescribePreferencesManager.putBoolean(mContext.getString(R.string.medication_alert), isChecked, mContext);
            RescribePreferencesManager.putBoolean(mContext.getString(R.string.offers_alert), isChecked, mContext);
        } else if (clickOption.getName().equalsIgnoreCase(mContext.getString(R.string.appointment_alert))) {
            RescribePreferencesManager.putBoolean(mContext.getString(R.string.appointment_alert), isChecked, mContext);
            RescribePreferencesManager.putBoolean(mContext.getString(R.string.all_notifications), checkToSetAllNotificationWrongFlow(), mContext);
        } else if (clickOption.getName().equalsIgnoreCase(mContext.getString(R.string.investigation_alert))) {
            RescribePreferencesManager.putBoolean(mContext.getString(R.string.investigation_alert), isChecked, mContext);
            RescribePreferencesManager.putBoolean(mContext.getString(R.string.all_notifications), checkToSetAllNotificationWrongFlow(), mContext);
        } else if (clickOption.getName().equalsIgnoreCase(mContext.getString(R.string.medication_alert))) {
            RescribePreferencesManager.putBoolean(mContext.getString(R.string.medication_alert), isChecked, mContext);
            RescribePreferencesManager.putBoolean(mContext.getString(R.string.all_notifications), checkToSetAllNotificationWrongFlow(), mContext);
        } else if (clickOption.getName().equalsIgnoreCase(mContext.getString(R.string.offers_alert))) {
            RescribePreferencesManager.putBoolean(mContext.getString(R.string.offers_alert), isChecked, mContext);
            RescribePreferencesManager.putBoolean(mContext.getString(R.string.all_notifications), checkToSetAllNotificationWrongFlow(), mContext);
        }

        mNotificationSettingListAdapter.notifyDataSetChanged();
    }

    private boolean checkToSetAllNotificationWrongFlow() {

        boolean appointmentAlert = RescribePreferencesManager.getBoolean(mContext.getString(R.string.appointment_alert), mContext);
        boolean investigationAlert = RescribePreferencesManager.getBoolean(mContext.getString(R.string.investigation_alert), mContext);
        boolean medicationAlert = RescribePreferencesManager.getBoolean(mContext.getString(R.string.medication_alert), mContext);
        boolean offersAlert = RescribePreferencesManager.getBoolean(mContext.getString(R.string.offers_alert), mContext);
        boolean allNotifyAlert = RescribePreferencesManager.getBoolean(mContext.getString(R.string.all_notifications), mContext);

        int statusCount = 0;
        int statusFalseCount = 0;
        if (appointmentAlert)
            statusCount += 1;
        else
            statusFalseCount += 1;
        //------
        if (investigationAlert)
            statusCount += 1;
        else
            statusFalseCount += 1;
        //------
        if (medicationAlert)
            statusCount += 1;
        else
            statusFalseCount += 1;
        //------
        if (offersAlert)
            statusCount += 1;
        else
            statusFalseCount += 1;
        //------

        if (statusCount == 4) {
            return true;
        } else if (statusFalseCount == 4) {
            return false;
        }
        return allNotifyAlert;
    }
}
