package com.rescribe.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.rescribe.R;
import com.rescribe.preference.RescribePreferencesManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {
    @BindView(R.id.radioNotification)
    RadioButton radioNotification;
    @BindView(R.id.radioAlarm)
    RadioButton radioAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        String string = RescribePreferencesManager.getString(getString(R.string.notificationAlarmTypeSetting), this);

        if (getString(R.string.alarm).equalsIgnoreCase(string)) {
            radioAlarm.setChecked(true);
            radioNotification.setChecked(false);
        } else {
            radioAlarm.setChecked(false);
            radioNotification.setChecked(true);
        }
    }

    @OnClick({R.id.radioNotification, R.id.radioAlarm})
    public void saveNotificationAlarmTypeSetting(RadioButton radioButton) {
        // Is the button now checked?
        boolean checked = radioButton.isChecked();

        // Check which radio button was clicked
        switch (radioButton.getId()) {
            case R.id.radioNotification:
                if (checked) {
                    // 1 clicked
                    RescribePreferencesManager.putString(getString(R.string.notificationAlarmTypeSetting), getString(R.string.notification), this);
                }
                break;
            case R.id.radioAlarm:
                if (checked) {
                    // 2 clicked
                    RescribePreferencesManager.putString(getString(R.string.notificationAlarmTypeSetting), getString(R.string.alarm), this);
                }
                break;
        }
    }

    @OnClick(R.id.backButton)
    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                finish();
                break;
        }
    }

}