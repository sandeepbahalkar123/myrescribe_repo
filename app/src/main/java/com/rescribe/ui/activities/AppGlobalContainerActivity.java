package com.rescribe.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.rescribe.R;
import com.rescribe.ui.fragments.ForgotPassword;
import com.rescribe.ui.fragments.OTPConfirmationForSignUp;
import com.rescribe.ui.fragments.OtpConfirmationForLogin;
import com.rescribe.ui.fragments.SocialSignUpInputFragment;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rescribe.ui.fragments.OTPConfirmationForSignUp.SIGN_UP_DETAILS;
import static com.rescribe.util.RescribeConstants.FROM;

/**
 * Created by jeetal on 19/5/17.
 */

public class AppGlobalContainerActivity extends AppCompatActivity {
    public static final String FORGET_PASSWORD = "forgetPassword";
    private final String TAG = this.getClass().getName();
    @BindView(R.id.blankContainer)
    FrameLayout mBlankContainer;
    private ActionBar mActionBar;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank_layout);
        ButterKnife.bind(this);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        String header = getIntent().getStringExtra(getString(R.string.title));
        from = getIntent().getStringExtra(FROM);
        loadFragment(getIntent().getStringExtra(getString(R.string.type)), getIntent().getSerializableExtra(SIGN_UP_DETAILS), header);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public void loadFragment(String type, Serializable serializableExtra, String header) {
          //When ever this activity will be called respective function fragement will be loaded for eg .Forgotpassword according to type set through intent
        mActionBar.setTitle(header);

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        if (serializableExtra != null)
            bundle.putSerializable(SIGN_UP_DETAILS, serializableExtra);

        if (type.equalsIgnoreCase(getString(R.string.enter_otp))) {
            OTPConfirmationForSignUp otpConfirmationForSignUp = new OTPConfirmationForSignUp();
            otpConfirmationForSignUp.setArguments(bundle);
            fragmentTransaction.replace(R.id.blankContainer, otpConfirmationForSignUp);
        } else if (type.equalsIgnoreCase(getString(R.string.login_with_facebook))) {
            SocialSignUpInputFragment socialLoginInputMobileForConfirmation = new SocialSignUpInputFragment();
            socialLoginInputMobileForConfirmation.setArguments(bundle);
            fragmentTransaction.replace(R.id.blankContainer, socialLoginInputMobileForConfirmation);
        } else if (type.equalsIgnoreCase(getString(R.string.login_with_gmail))) {
            SocialSignUpInputFragment socialLoginInputMobileForConfirmation = new SocialSignUpInputFragment();
            socialLoginInputMobileForConfirmation.setArguments(bundle);
            fragmentTransaction.replace(R.id.blankContainer, socialLoginInputMobileForConfirmation);
        } else if (type.equalsIgnoreCase(getString(R.string.forgot_password))) {
            ForgotPassword forgotPassword = new ForgotPassword();
            forgotPassword.setArguments(bundle);
            fragmentTransaction.replace(R.id.blankContainer, forgotPassword);
        }
        else if(type.equalsIgnoreCase(getString(R.string.enter_otp_for_login))){
            OtpConfirmationForLogin otpConfirmationForLogin = new OtpConfirmationForLogin();
            if (from != null)
                bundle.putString(FROM, from);
            otpConfirmationForLogin.setArguments(bundle);
            fragmentTransaction.replace(R.id.blankContainer, otpConfirmationForLogin);
        }
        fragmentTransaction.commit();
    }
}
