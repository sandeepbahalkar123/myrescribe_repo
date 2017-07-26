package com.myrescribe.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.myrescribe.R;
import com.myrescribe.model.login.SignUpModel;
import com.myrescribe.ui.fragments.LoginMainTabFragment;
import com.myrescribe.ui.fragments.OTPConfirmationForSignUp;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 19/5/17.
 */

public class AppLoginConfirmationActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getName();
    @BindView(R.id.blankContainer)
    FrameLayout mBlankContainer;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank_layout);
        ButterKnife.bind(this);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(getString(R.string.doctor_details) + getString(R.string.details));
        loadFragment(getIntent().getStringExtra(getString(R.string.type)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(String type) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();

        if (type.equalsIgnoreCase(getString(R.string.enter_otp))) {
            OTPConfirmationForSignUp otpConfirmationForSignUp = new OTPConfirmationForSignUp();
            Bundle b = new Bundle();
            b.putSerializable(getString(R.string.details), getIntent().getSerializableExtra(getString(R.string.details)));
            otpConfirmationForSignUp.setArguments(b);
            fragmentTransaction.replace(R.id.blankContainer, otpConfirmationForSignUp);
        }
        fragmentTransaction.commit();
    }
}
