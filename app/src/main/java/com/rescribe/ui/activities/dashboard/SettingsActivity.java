package com.rescribe.ui.activities.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rescribe.R;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.ui.customesViews.NonSwipeableViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 3/11/17.
 */

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    NonSwipeableViewPager viewpager;
    @BindView(R.id.viewPagerDoctorItem)
    ViewPager viewPagerDoctorItem;
    @BindView(R.id.sliderView)
    FrameLayout sliderView;
    @BindView(R.id.dividerView)
    View dividerView;
    @BindView(R.id.menuOptionsListView)
    RecyclerView menuOptionsListView;
    @BindView(R.id.alertsIcon)
    ImageView alertsIcon;
    @BindView(R.id.alerts)
    CustomTextView alerts;
    @BindView(R.id.alertTab)
    ImageView alertTab;
    @BindView(R.id.alertsLayout)
    LinearLayout alertsLayout;
    @BindView(R.id.profileImage)
    ImageView profileImage;
    @BindView(R.id.profile)
    CustomTextView profile;
    @BindView(R.id.profileTab)
    ImageView profileTab;
    @BindView(R.id.profileLayout)
    LinearLayout profileLayout;
    @BindView(R.id.appLogo)
    ImageView appLogo;
    @BindView(R.id.logoLayout)
    LinearLayout logoLayout;
    @BindView(R.id.settingIcon)
    ImageView settingIcon;
    @BindView(R.id.setting)
    CustomTextView setting;
    @BindView(R.id.settingTab)
    ImageView settingTab;
    @BindView(R.id.settingLayout)
    LinearLayout settingLayout;
    @BindView(R.id.supportIcon)
    ImageView supportIcon;
    @BindView(R.id.support)
    CustomTextView support;
    @BindView(R.id.supportTab)
    ImageView supportTab;
    @BindView(R.id.supportLayout)
    LinearLayout supportLayout;
    @BindView(R.id.doctorOptionsView)
    LinearLayout doctorOptionsView;
    @BindView(R.id.menuIcon)
    ImageView menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        ButterKnife.bind(this);
        initialize();

    }

    private void initialize() {
        settingTab.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.settings));
    }

    @OnClick({R.id.alertsLayout, R.id.profileLayout, R.id.logoLayout, R.id.settingLayout, R.id.supportLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.alertsLayout:
                break;
            case R.id.profileLayout:
                Intent intent = new Intent(SettingsActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.logoLayout:
               /* Intent intent = new Intent(HomePageActivity.this, ProfileActivity.class);
                startActivity(intent);*/
                break;
            case R.id.supportLayout:
                Intent intentSupport = new Intent(SettingsActivity.this, SupportActivity.class);
                startActivity(intentSupport);
                finish();
                break;
        }
    }
}