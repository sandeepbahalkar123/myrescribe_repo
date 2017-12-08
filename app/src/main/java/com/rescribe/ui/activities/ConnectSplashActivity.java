package com.rescribe.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.rescribe.R;
import com.rescribe.util.RescribeConstants;

public class ConnectSplashActivity extends AppCompatActivity {

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
                startActivity(intent);
                finish();
            }
        }, RescribeConstants.TIME_STAMPS.THREE_SECONDS);
    }
}
