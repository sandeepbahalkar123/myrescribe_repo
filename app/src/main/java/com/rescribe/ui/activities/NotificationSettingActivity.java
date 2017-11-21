package com.rescribe.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;

import com.rescribe.R;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.customesViews.switch_button.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationSettingActivity extends AppCompatActivity {

    @BindView(R.id.title)
    CustomTextView title;

    @BindView(R.id.all_notifications)
    SwitchButton allNotifications;
    @BindView(R.id.appointment_alert)
    SwitchButton appointmentAlert;
    @BindView(R.id.investigation_alert)
    SwitchButton investigationAlert;
    @BindView(R.id.medication_alert)
    SwitchButton medicationAlert;
    @BindView(R.id.offers_alert)
    SwitchButton offersAlert;
    private Context mContaxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);
        ButterKnife.bind(this);
        mContaxt = this;

        switchListeners();
    }

    private void switchListeners() {

        appointmentAlert.setChecked(RescribePreferencesManager.getBoolean(RescribePreferencesManager.NOTIFICATION_SETTING_KEY.APPOINTMENT_ALERT, mContaxt));
        appointmentAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RescribePreferencesManager.putBoolean(RescribePreferencesManager.NOTIFICATION_SETTING_KEY.APPOINTMENT_ALERT, isChecked, mContaxt);
            }
        });

        investigationAlert.setChecked(RescribePreferencesManager.getBoolean(RescribePreferencesManager.NOTIFICATION_SETTING_KEY.INVESTIGATION_ALERT, mContaxt));
        investigationAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RescribePreferencesManager.putBoolean(RescribePreferencesManager.NOTIFICATION_SETTING_KEY.INVESTIGATION_ALERT, isChecked, mContaxt);
            }
        });

        medicationAlert.setChecked(RescribePreferencesManager.getBoolean(RescribePreferencesManager.NOTIFICATION_SETTING_KEY.MEDICATION_ALERT, mContaxt));
        medicationAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RescribePreferencesManager.putBoolean(RescribePreferencesManager.NOTIFICATION_SETTING_KEY.MEDICATION_ALERT, isChecked, mContaxt);
            }
        });

        offersAlert.setChecked(RescribePreferencesManager.getBoolean(RescribePreferencesManager.NOTIFICATION_SETTING_KEY.OFFERS_ALERT, mContaxt));
        offersAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RescribePreferencesManager.putBoolean(RescribePreferencesManager.NOTIFICATION_SETTING_KEY.OFFERS_ALERT, isChecked, mContaxt);
            }
        });

        allNotifications.setChecked(isAllChecked());
    }

    @OnClick({R.id.backButton, R.id.all_notifications, R.id.appointment_alert, R.id.investigation_alert, R.id.medication_alert, R.id.offers_alert})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                onBackPressed();
                break;
            case R.id.all_notifications:
                appointmentAlert.setChecked(allNotifications.isChecked());
                investigationAlert.setChecked(allNotifications.isChecked());
                medicationAlert.setChecked(allNotifications.isChecked());
                offersAlert.setChecked(allNotifications.isChecked());
                break;
            case R.id.appointment_alert:
                allNotifications.setChecked(isAllChecked());
                break;
            case R.id.investigation_alert:
                allNotifications.setChecked(isAllChecked());
                break;
            case R.id.medication_alert:
                allNotifications.setChecked(isAllChecked());
                break;
            case R.id.offers_alert:
                allNotifications.setChecked(isAllChecked());
                break;
        }
    }

    private boolean isAllChecked() {
        return appointmentAlert.isChecked() && investigationAlert.isChecked() && medicationAlert.isChecked() && offersAlert.isChecked();
    }
}
