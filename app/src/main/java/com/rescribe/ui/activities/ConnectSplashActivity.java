package com.rescribe.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.rescribe.R;
import com.rescribe.model.dashboard_api.DashboardBottomMenuList;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import static com.rescribe.util.RescribeConstants.BOTTOM_MENUS;

public class ConnectSplashActivity extends AppCompatActivity {
    public ArrayList<DashboardBottomMenuList> dashboardBottomMenuLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_splash);
        doNext();
    }

    private void doNext() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(ConnectSplashActivity.this, DoctorConnectActivity.class);
                dashboardBottomMenuLists = getIntent().getParcelableArrayListExtra(BOTTOM_MENUS);
                if (dashboardBottomMenuLists != null)
                    intent.putExtra(RescribeConstants.BOTTOM_MENUS, dashboardBottomMenuLists);
                startActivity(intent);
                finish();
            }
        }, RescribeConstants.TIME_STAMPS.TWO_SECONDS);
    }
}
