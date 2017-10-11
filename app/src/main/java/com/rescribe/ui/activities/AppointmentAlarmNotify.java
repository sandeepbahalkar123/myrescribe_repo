package com.rescribe.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
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
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.utils.GridSpacingItemDecoration;

public class AppointmentAlarmNotify extends Activity {


    @BindView(R.id.messageTitle)
    TextView mMessageTitle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.custom_mesage_box);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            mDialogMessageText.setText("" + intent.getStringExtra(RescribeConstants.APPOINTMENT_MESSAGE));
            mMessageTitle.setText("" + intent.getStringExtra(RescribeConstants.APPOINTMENT_TIME));
        }

        //----Play sound
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), notification);
        mp.start();
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
        mTimeSlots.add("15 minutes");
        mTimeSlots.add("30 minutes");
        mTimeSlots.add("45 minutes");
        mTimeSlots.add("60 minutes");
    }

    @OnClick({R.id.dialogButtonSetSnooze, R.id.snoozeDialogButtonCancel, R.id.dialogButtonOK, R.id.snoozeDialogButtonOK})
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.snoozeDialogButtonCancel:
            case R.id.dialogButtonOK:
                finish();
                break;
            case R.id.dialogButtonSetSnooze:     // for snooze
                mSnoozeTitle.setText(getString(R.string.snooze_interval));
                mMainNotificationInfoLayout.setVisibility(View.GONE);
                mMainSetSnoozeTimeLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.snoozeDialogButtonOK:
                String selectedTimeSlot = mSnoozeAlarmTimeSlotAdapter.getSelectedTimeSlot();


                CommonMethods.showToast(this, selectedTimeSlot);

                break;
        }
    }
}
