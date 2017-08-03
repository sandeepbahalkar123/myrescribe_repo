package com.myrescribe.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;

import com.myrescribe.R;
import com.myrescribe.helpers.login.LoginHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.model.login.LoginModel;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.ui.activities.AppGlobalContainerActivity;
import com.myrescribe.ui.activities.HomePageActivity;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogInApp extends Fragment implements
        HelperResponse {
    private final String TAG = this.getClass().getName();

    Context mContext;

    @BindView(R.id.loginChildScrollView)
    ScrollView mLoginChildScrollView;
    @BindView(R.id.editTextMobileNo)
    EditText mMobileNo;
    @BindView(R.id.editTextPassword)
    EditText mPassword;

    public LogInApp() {
        // Required empty public constructor
    }

    public static LogInApp newInstance(String param1, String param2) {
        LogInApp fragment = new LogInApp();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.log_in, container, false);
        ButterKnife.bind(this, inflate);
        mContext = this.getContext();

        return inflate;
    }

    @OnClick({R.id.btn_login})
    public void onButtonClicked(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                String mobileNo = mMobileNo.getText().toString();
                String password = mPassword.getText().toString();
                if (!validate(mobileNo, password)) {
                    LoginHelper loginHelper = new LoginHelper(getActivity(), this);
                    loginHelper.doLogin(mobileNo, password);
                }
                break;
        }
    }

    @OnClick({R.id.forgotPasswordView})
    public void onForgotPassword() {
        Intent intentObj = new Intent(getActivity(), AppGlobalContainerActivity.class);
        intentObj.putExtra(getString(R.string.type), getString(R.string.forgot_password));
        intentObj.putExtra(getString(R.string.title), getString(R.string.forgot_password_header));
        startActivity(intentObj);
    }

    /**
     * Return true if fields empty/validation failed, else false.
     *
     * @return
     */
    private boolean validate(String mobileNo, String password) {
        String message = null;
        String enter = getString(R.string.enter);
        if (mobileNo.isEmpty()) {
            message = enter + getString(R.string.enter_mobile_no).toLowerCase(Locale.US);
            mMobileNo.setError(message);
            mMobileNo.requestFocus();
        } else if (mobileNo.trim().length() < 10) {
            message = getString(R.string.err_invalid_mobile_no);
            mMobileNo.setError(message);
            mMobileNo.requestFocus();

        } else if (password.isEmpty()) {
            message = enter + getString(R.string.enter_password).toLowerCase(Locale.US);
            mPassword.setError(message);
            mPassword.requestFocus();
        } else if (password.trim().length() < 8) {
            message = getString(R.string.error_too_small_password);
            mPassword.setError(message);
            mPassword.requestFocus();

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
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATEINT_ID, loginModel.getPatientId(), mContext);

                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER, mMobileNo.getText().toString(), mContext);
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PASSWORD, mPassword.getText().toString(), mContext);

                Intent intent = new Intent(mContext, HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            } else {
                CommonMethods.showToast(getActivity(), loginModel.getCommon().getStatusMessage());
            }
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        CommonMethods.showToast(getActivity(), errorMessage);
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(getActivity(), serverErrorMessage);
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(getActivity(), serverErrorMessage);
    }
}
