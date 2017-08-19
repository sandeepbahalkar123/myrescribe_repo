package com.myrescribe.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.myrescribe.R;
import com.myrescribe.helpers.login.LoginHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.login.LoginModel;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.ui.customesViews.CustomTextView;
import com.myrescribe.ui.fragments.SignUp;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 18/8/17.
 */

public class LoginNewFlowActivity extends AppCompatActivity implements View.OnClickListener, HelperResponse {
    private final String TAG = this.getClass().getName();
    @BindView(R.id.editTextMobileNo)
    EditText editTextMobileNo;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    @BindView(R.id.btnOtp)
    CustomTextView btnOtp;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.forgotPasswordView)
    CustomTextView forgotPasswordView;
    @BindView(R.id.signup)
    CustomTextView signup;
    private Context mContext;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_new_flow_layout);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        mContext = LoginNewFlowActivity.this;
        intent = getIntent();
        btnLogin.setOnClickListener(this);
        btnOtp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                String mobileNo = editTextMobileNo.getText().toString();
                String password = editTextPassword.getText().toString();
                if (!validate(mobileNo, password)) {
                    LoginHelper loginHelper = new LoginHelper(mContext, this);
                    loginHelper.doLogin(mobileNo, password);
                }
                break;
            case R.id.btnOtp:
                String mobile = editTextMobileNo.getText().toString();
                if (!validatePhoneNo(mobile)) {
                    LoginHelper loginHelper = new LoginHelper(this, this);
                    loginHelper.doLoginByOTP(mobile);
                }
                break;

        }
    }

    @OnClick({R.id.forgotPasswordView})
    public void onForgotPassword() {
        Intent intentObj = new Intent(mContext, AppGlobalContainerActivity.class);
        intent.putExtra(getString(R.string.type), getString(R.string.enter_otp_for_login));
        intentObj.putExtra(getString(R.string.type), getString(R.string.forgot_password));
        intentObj.putExtra(getString(R.string.title), getString(R.string.forgot_password_header));
        startActivity(intentObj);
    }

    private boolean validatePhoneNo(String mobile) {
        String message = null;
        String enter = getString(R.string.enter);
        if (mobile.isEmpty()) {
            message = enter + getString(R.string.enter_mobile_no).toLowerCase(Locale.US);
            editTextMobileNo.setError(message);
            editTextMobileNo.requestFocus();
        } else if ((mobile.trim().length() < 10) || !(mobile.trim().startsWith("7") || mobile.trim().startsWith("8") || mobile.trim().startsWith("9"))) {
            message = getString(R.string.err_invalid_mobile_no);
            editTextMobileNo.setError(message);
            editTextMobileNo.requestFocus();
        }
        if (message != null) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validate(String mobileNo, String password) {
        String message = null;
        String enter = getString(R.string.enter);
        if (mobileNo.isEmpty()) {
            message = enter + getString(R.string.enter_mobile_no).toLowerCase(Locale.US);
            editTextMobileNo.setError(message);
            editTextMobileNo.requestFocus();
        } else if ((mobileNo.trim().length() < 10) || !(mobileNo.trim().startsWith("7") || mobileNo.trim().startsWith("8") || mobileNo.trim().startsWith("9"))) {
            message = getString(R.string.err_invalid_mobile_no);
            editTextMobileNo.setError(message);
            editTextMobileNo.requestFocus();
        } else if (password.isEmpty()) {
            message = enter + getString(R.string.enter_password).toLowerCase(Locale.US);
            editTextPassword.setError(message);
            editTextPassword.requestFocus();
        } else if (password.trim().length() < 8) {
            message = getString(R.string.error_too_small_password);
            editTextPassword.setError(message);
            editTextPassword.requestFocus();

        }
        if (message != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(MyRescribeConstants.TASK_LOGIN)) {

            LoginModel loginModel = (LoginModel) customResponse;
            if (loginModel.getCommon().isSuccess()) {
                CommonMethods.Log(TAG + " Token", loginModel.getAuthToken());
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.AUTHTOKEN, loginModel.getAuthToken(), mContext);
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, MyRescribeConstants.YES, mContext);
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATIENT_ID, loginModel.getPatientId(), mContext);
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER, editTextMobileNo.getText().toString(), mContext);
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PASSWORD, editTextPassword.getText().toString(), mContext);
                Intent intent = new Intent(mContext, HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                CommonMethods.showToast(mContext, loginModel.getCommon().getStatusMessage());
                Intent intent = new Intent(mContext, SignUpNewFlow.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        } else if (mOldDataTag.equalsIgnoreCase(MyRescribeConstants.TASK_LOGIN_WITH_OTP)) {

            LoginModel loginModel = (LoginModel) customResponse;
            if (loginModel.getCommon().isSuccess()) {
                CommonMethods.Log(TAG + " Token", loginModel.getAuthToken());
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER, editTextMobileNo.getText().toString(), this);
                Intent intent = new Intent(this, AppGlobalContainerActivity.class);
                intent.putExtra(getString(R.string.type), getString(R.string.enter_otp_for_login));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {

                Intent intent = new Intent(this, SignUpNewFlow.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }


        }

    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        CommonMethods.showToast(mContext, errorMessage);
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(mContext, serverErrorMessage);

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(mContext, serverErrorMessage);

    }

    @OnClick(R.id.signup)
    public void onViewClicked() {
        Intent intent = new Intent(mContext, SignUpNewFlow.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
