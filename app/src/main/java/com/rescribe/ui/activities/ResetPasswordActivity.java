package com.rescribe.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.helpers.login.LoginHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.model.login.ForgetPasswordModel;
import com.rescribe.model.login.ResetPasswordRequestModel;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPasswordActivity extends AppCompatActivity implements HelperResponse {

    @BindView(R.id.passwordText)
    EditText passwordText;
    @BindView(R.id.passwordAgainText)
    EditText passwordAgainText;
    @BindView(R.id.okButton)
    Button okButton;
    @BindView(R.id.titleText)
    TextView titleText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        titleText.setText(getResources().getString(R.string.title_activity_reset_password));
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_RESET_PASS_WITH_OTP)) {
            ForgetPasswordModel forgetPasswordModel = (ForgetPasswordModel) customResponse;
            if (forgetPasswordModel.getCommon().isSuccess()) {
                Intent intentObj = new Intent(ResetPasswordActivity.this, LoginSignUpActivity.class);
                startActivity(intentObj);
                finishAffinity();
            }
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {

    }

    @OnClick(R.id.okButton)
    public void onViewClicked() {

        String password = passwordText.getText().toString();
        String passwordAgain = passwordAgainText.getText().toString();

        if (password.isEmpty() && passwordAgain.isEmpty()) {
            CommonMethods.showToast(this, "Please enter " + getResources().getString(R.string.enter_password));
            return;
        }

        if (password.equals(passwordAgain)) {
            if (password.length() > 7 && passwordAgain.length() > 7) {
                LoginHelper loginHelper = new LoginHelper(this, this);
                ResetPasswordRequestModel resetPasswordRequestModel = new ResetPasswordRequestModel();
                resetPasswordRequestModel.setMobileNumber(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER, this));
                resetPasswordRequestModel.setPassword(password);
                resetPasswordRequestModel.setPatientId(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, this));
                loginHelper.resetPassword(resetPasswordRequestModel);
            } else
                CommonMethods.showToast(this, getResources().getString(R.string.error_too_small_password));
        } else
            CommonMethods.showToast(this, getResources().getString(R.string.password_not_matched));
    }
}
