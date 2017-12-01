package com.rescribe.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.adapters.SnoozeAlarmTimeSlotAdapter;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.RescribeConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.utils.GridSpacingItemDecoration;

public class SnoozeAlarmNotifyActivity extends Activity {


    @BindView(R.id.messageTitle)
    TextView mMessageTitle;
    @BindView(R.id.showMedicineName)
    TextView mShowMedicineName;
    @BindView(R.id.timeText)
    TextView mTimeText;
    @BindView(R.id.dialogMessageText)
    TextView mDialogMessageText;
    @BindView(R.id.dialogButtonSetSnooze)
    Button mDialogButtonSetSnooze;
    @BindView(R.id.dialogButtonOK)
    Button mDialogButtonOK;
    @BindView(R.id.mainNotificationInfoLayout)
    LinearLayout mMainNotificationInfoLayout;
    @BindView(R.id.snoozeTitle)
    TextView mSnoozeTitle;
    @BindView(R.id.snoozeTimeSlotListView)
    RecyclerView mSnoozeTimeSlotListView;
    @BindView(R.id.snoozeDialogButtonCancel)
    Button mSnoozeDialogButtonCancel;
    @BindView(R.id.snoozeDialogButtonOK)
    Button mSnoozeDialogButtonOK;
    @BindView(R.id.mainSetSnoozeTimeLayout)
    LinearLayout mMainSetSnoozeTimeLayout;

    private ArrayList<String> mTimeSlots;
    private SnoozeAlarmTimeSlotAdapter mSnoozeAlarmTimeSlotAdapter;
    private String mNotificationTime, mTitle, mMedicinSlot;
    private String mNotificationID;
    private MediaPlayer mMediaPlayer;
    private Vibrator mVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.custom_mesage_box);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            mNotificationID = intent.getStringExtra(RescribeConstants.NOTIFICATION_ID);
            mTitle = intent.getStringExtra(RescribeConstants.TITLE);
            mNotificationTime = intent.getStringExtra(RescribeConstants.NOTIFICATION_TIME);
            mMedicinSlot = intent.getStringExtra(RescribeConstants.MEDICINE_SLOT);
            mDialogMessageText.setText("" + mTitle);
            mTimeText.setText("" + mNotificationTime);
            mShowMedicineName.setText("" + mMedicinSlot);
        }

        //----Play sound
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        mMediaPlayer.start();
        mVibrator.vibrate(mMediaPlayer.getDuration());
        mMainNotificationInfoLayout.setVisibility(View.VISIBLE);
        mMainSetSnoozeTimeLayout.setVisibility(View.GONE);

        //--------------
        definedTimeSlots();
        int spanCount = 2; // 3 columns
        int spacing = 30; // 50px
        boolean includeEdge = false;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        mSnoozeTimeSlotListView.setLayoutManager(layoutManager);
        mSnoozeTimeSlotListView.setItemAnimator(new DefaultItemAnimator());
        mSnoozeTimeSlotListView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        mSnoozeAlarmTimeSlotAdapter = new SnoozeAlarmTimeSlotAdapter(this, mTimeSlots);
        mSnoozeTimeSlotListView.setAdapter(mSnoozeAlarmTimeSlotAdapter);
        //--------------

    }

    private void definedTimeSlots() {
        mTimeSlots = new ArrayList<>();
        mTimeSlots.add("15");
        mTimeSlots.add("30");
        mTimeSlots.add("45");
        mTimeSlots.add("60");
    }

    @OnClick({R.id.dialogButtonSetSnooze, R.id.snoozeDialogButtonCancel, R.id.dialogButtonOK, R.id.snoozeDialogButtonOK})
    public void onClick(View view) {
        mMediaPlayer.stop();
        mVibrator.cancel();

        switch (view.getId()) {

            case R.id.snoozeDialogButtonCancel:
            case R.id.dialogButtonOK:
                mMediaPlayer.stop();
                mVibrator.cancel();
                SharedPreferences sharedPreference = RescribePreferencesManager.getSharedPreference(this);
                Map<String, ?> keys = sharedPreference.getAll();
                for (Map.Entry<String, ?> entry : keys.entrySet()) {
                    String key = entry.getKey();
                    //  if (key.startsWith(getString(R.string.snooze_interval)) && !(key.endsWith("null"))) {
                    if (key.startsWith(getString(R.string.snooze_interval))) {
                        String value = entry.getValue().toString();
                        String keyData = getString(R.string.snooze_interval) + "|" + mNotificationID + "|" + mTitle + "|" + mNotificationTime + "|" + mMedicinSlot;
                        if (key.equalsIgnoreCase(keyData)) {
                            RescribePreferencesManager.removeSharedPrefKey(this, keyData);
                        }
                    }
                }
                finish();
                break;
            case R.id.dialogButtonSetSnooze:     // for snooze
                mSnoozeTitle.setText(getString(R.string.snooze_interval));
                mMainNotificationInfoLayout.setVisibility(View.GONE);
                mMainSetSnoozeTimeLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.snoozeDialogButtonOK:
                String selectedTimeSlot = mSnoozeAlarmTimeSlotAdapter.getSelectedTimeSlot();
                if (!RescribeConstants.BLANK.equalsIgnoreCase(selectedTimeSlot)) {
                    SimpleDateFormat df = new SimpleDateFormat(RescribeConstants.DATE_PATTERN.DD_MM_YYYY + " " + RescribeConstants.DATE_PATTERN.HH_mm_ss);
                    Date currentDate = new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(currentDate);
                    cal.add(Calendar.MINUTE, Integer.parseInt(selectedTimeSlot));
                    String newTime = df.format(cal.getTime());

                    String key = getString(R.string.snooze_interval) + "|" + mNotificationID + "|" + mTitle + "|" + mNotificationTime + "|" + mMedicinSlot;
                    RescribePreferencesManager.putString(key, newTime, this);
                    finish();
                }

                break;
        }
    }
}
