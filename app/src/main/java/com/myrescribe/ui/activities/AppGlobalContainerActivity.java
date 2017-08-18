package com.myrescribe.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.myrescribe.R;
import com.myrescribe.ui.fragments.ForgotPassword;
import com.myrescribe.ui.fragments.OTPConfirmationForSignUp;
import com.myrescribe.ui.fragments.OtpConfirmationForLogin;
import com.myrescribe.ui.fragments.SocialLoginInputMobileForConfirmation;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 19/5/17.
 */

public class AppGlobalContainerActivity extends AppCompatActivity {
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
        String header = getIntent().getStringExtra(getString(R.string.title));
        loadFragment(getIntent().getStringExtra(getString(R.string.type)), getIntent().getSerializableExtra(getString(R.string.details)), header);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public void loadFragment(String type, Serializable serializableExtra, String header) {

        mActionBar.setTitle(header);

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        Bundle b = new Bundle();
        if (serializableExtra != null) {
            b.putSerializable(getString(R.string.details), serializableExtra);
        }

        if (type.equalsIgnoreCase(getString(R.string.enter_otp))) {
            OTPConfirmationForSignUp otpConfirmationForSignUp = new OTPConfirmationForSignUp();
            otpConfirmationForSignUp.setArguments(b);
            fragmentTransaction.replace(R.id.blankContainer, otpConfirmationForSignUp);
        } else if (type.equalsIgnoreCase(getString(R.string.login_social_media))) {
            SocialLoginInputMobileForConfirmation socialLoginInputMobileForConfirmation = new SocialLoginInputMobileForConfirmation();
            socialLoginInputMobileForConfirmation.setArguments(b);
            fragmentTransaction.replace(R.id.blankContainer, socialLoginInputMobileForConfirmation);
        } else if (type.equalsIgnoreCase(getString(R.string.forgot_password))) {
            ForgotPassword forgotPassword = new ForgotPassword();
            forgotPassword.setArguments(b);
            fragmentTransaction.replace(R.id.blankContainer, forgotPassword);
        }else if(type.equalsIgnoreCase(getString(R.string.enter_otp_for_login))){
            OtpConfirmationForLogin otpConfirmationForLogin = new OtpConfirmationForLogin();
           // otpConfirmationForSignUp.setArguments(b);
            fragmentTransaction.replace(R.id.blankContainer, otpConfirmationForLogin);

        }
        fragmentTransaction.commit();
    }
}
