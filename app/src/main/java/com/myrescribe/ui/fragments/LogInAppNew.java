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
import com.myrescribe.ui.activities.LoginMainActivity;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 17/8/17.
 */

public class LogInAppNew extends Fragment implements
        HelperResponse {
    private final String TAG = this.getClass().getName();

    Context mContext;

    @BindView(R.id.loginChildScrollView)
    ScrollView mLoginChildScrollView;
    @BindView(R.id.editTextMobileNo)
    EditText mMobileNo;
    /*@BindView(R.id.editTextPassword)
    EditText mPassword;*/

    public LogInAppNew() {
        // Required empty public constructor
    }

    public static LogInAppNew newInstance(String param1, String param2) {
        LogInAppNew fragment = new LogInAppNew();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.log_in_new, container, false);
        ButterKnife.bind(this, inflate);
        mContext = this.getContext();
        init();
        return inflate;
    }

    private void init() {
       // mPassword.setHint(getString(R.string.enter_password).toUpperCase());
        mMobileNo.setHint(getString(R.string.enter_mobile_no).toUpperCase());
    }

    @OnClick({R.id.btn_login})
    public void onButtonClicked(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                String mobileNo = mMobileNo.getText().toString();
              //  String password = mPassword.getText().toString();
               /* if (!validate(mobileNo)) {
                    LoginHelper loginHelper = new LoginHelper(getActivity(), this);
                    loginHelper.doLogin(mobileNo);
                }*/
                Intent intent = new Intent(mContext, AppGlobalContainerActivity.class);
                intent.putExtra(getString(R.string.type), getString(R.string.enter_otp_for_login));
                intent.putExtra(getString(R.string.title), getString(R.string.log_in));
                // intentObj.putExtra(getString(R.string.details), mSignUpRequestModel);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
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
    private boolean validate(String mobileNo) {
        String message = null;
        String enter = getString(R.string.enter);
        if (mobileNo.isEmpty()) {
            message = enter + getString(R.string.enter_mobile_no).toLowerCase(Locale.US);
            mMobileNo.setError(message);
            mMobileNo.requestFocus();
        } else if ((mobileNo.trim().length() < 10) || !(mobileNo.trim().startsWith("7") || mobileNo.trim().startsWith("8") || mobileNo.trim().startsWith("9"))) {
            message = getString(R.string.err_invalid_mobile_no);
            mMobileNo.setError(message);
            mMobileNo.requestFocus();
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
                Intent intent = new Intent(mContext, AppGlobalContainerActivity.class);
                intent.putExtra(getString(R.string.type), getString(R.string.enter_otp_for_login));
                intent.putExtra(getString(R.string.title), getString(R.string.log_in));
               // intentObj.putExtra(getString(R.string.details), mSignUpRequestModel);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
               /* MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.AUTHTOKEN, loginModel.getAuthToken(), mContext);
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, MyRescribeConstants.YES, mContext);
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATEINT_ID, loginModel.getPatientId(), mContext);

                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER, mMobileNo.getText().toString(), mContext);
                Intent intent = new Intent(mContext, HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();*/
            } else {
                Intent intent = new Intent(getActivity(), LoginMainActivity.class);
                intent.putExtra(getString(R.string.type),MyRescribeConstants.TASK_LOGIN);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
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
