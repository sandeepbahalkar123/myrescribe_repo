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
import com.myrescribe.model.login.SignUpModel;
import com.myrescribe.model.requestmodel.login.SignUpRequestModel;
import com.myrescribe.ui.customesViews.CustomTextView;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 18/8/17.
 */
public class SignUpNewFlow extends AppCompatActivity implements View.OnClickListener, HelperResponse {
    private final String TAG = this.getClass().getName();
    @BindView(R.id.editTextName)
    EditText editTextName;
    @BindView(R.id.editTextEmailID)
    EditText editTextEmailID;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    @BindView(R.id.editTextMobileNo)
    EditText editTextMobileNo;
    @BindView(R.id.btnSignUp)
    Button btnSignUp;
    @BindView(R.id.login)
    CustomTextView login;
    private Context mContext;
    Intent intent;
    private SignUpRequestModel mSignUpRequestModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_new_flow);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        mContext = SignUpNewFlow.this;
        editTextName.setHint(getString(R.string.enter_full_name).toUpperCase());
        editTextEmailID.setHint(getString(R.string.enter_email_id).toUpperCase());
        editTextPassword.setHint(getString(R.string.enter_password).toUpperCase());
        editTextMobileNo.setHint(getString(R.string.enter_mobile_no).toUpperCase());
    }

    private boolean validate(String name, String email, String password, String mobileNo) {
        String message = null;
        String enter = getString(R.string.enter);
        if (name.isEmpty()) {
            message = enter + getString(R.string.enter_full_name).toLowerCase(Locale.US);
            editTextName.setError(message);
            editTextName.requestFocus();
        } else if (email.isEmpty()) {
            message = enter + getString(R.string.enter_email_id).toLowerCase(Locale.US);
            editTextEmailID.setError(message);
            editTextEmailID.requestFocus();

        } else if (!CommonMethods.isValidEmail(email)) {
            message = getString(R.string.err_email_invalid);
            editTextEmailID.setError(message);
            editTextEmailID.requestFocus();

        } else if (password.isEmpty()) {
            message = enter + getString(R.string.enter_password).toLowerCase(Locale.US);
            editTextPassword.setError(message);
            editTextPassword.requestFocus();

        } else if (password.trim().length() < 8) {
            message = getString(R.string.error_too_small_password);
            editTextPassword.setError(message);
            editTextPassword.requestFocus();

        } else if (mobileNo.isEmpty()) {
            message = enter + getString(R.string.enter_mobile_no).toLowerCase(Locale.US);
            editTextMobileNo.setError(message);
            editTextMobileNo.requestFocus();

        } else if ((mobileNo.trim().length() < 10) || !(mobileNo.trim().startsWith("7") || mobileNo.trim().startsWith("8") || mobileNo.trim().startsWith("9"))) {

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

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(MyRescribeConstants.TASK_SIGN_UP)) {

            SignUpModel loginModel = (SignUpModel) customResponse;

            if (loginModel.getCommon().isSuccess()) {
                if(loginModel.getCommon().getStatusMessage().equalsIgnoreCase(getString(R.string.profile_exists))){
                    CommonMethods.showToast(this, loginModel.getCommon().getStatusMessage());
                }else {
                    Intent intentObj = new Intent(mContext, AppGlobalContainerActivity.class);
                    intentObj.putExtra(getString(R.string.type), getString(R.string.enter_otp));
                    intentObj.putExtra(getString(R.string.title), getString(R.string.sign_up_confirmation));
                    intentObj.putExtra(getString(R.string.details), mSignUpRequestModel);
                    startActivity(intentObj);
                }
            } else {
                CommonMethods.showToast(this, loginModel.getCommon().getStatusMessage());
            }
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        CommonMethods.showToast(this, errorMessage);

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(this, serverErrorMessage);
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(this, serverErrorMessage);
    }

    @OnClick(R.id.btnSignUp)
    public void onViewClicked() {
        String name = editTextName.getText().toString();
        String email = editTextEmailID.getText().toString();
        String password = editTextPassword.getText().toString();
        String mobileNo = editTextMobileNo.getText().toString();
        if (!validate(name, email, password, mobileNo)) {
            LoginHelper loginHelper = new LoginHelper(this, this);
            mSignUpRequestModel = new SignUpRequestModel();
            mSignUpRequestModel.setMobileNumber(mobileNo);
            mSignUpRequestModel.setName(name);
            mSignUpRequestModel.setEmailId(email);
            mSignUpRequestModel.setPassword(password);
            loginHelper.doSignUp(mSignUpRequestModel);
        }
    }

    @OnClick(R.id.login)
    public void onLoginClicked() {
        Intent intent = new Intent(mContext, LoginNewFlowActivity
                .class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
