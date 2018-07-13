package com.rescribe.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.broadcast_receivers.OtpReader;
import com.rescribe.helpers.login.LoginHelper;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.interfaces.HelperResponse;
import com.rescribe.interfaces.OTPListener;
import com.rescribe.model.login.ForgetPasswordModel;
import com.rescribe.model.login.LoginModel;
import com.rescribe.model.login.LoginWithOtp;
import com.rescribe.model.login.PatientDetail;
import com.rescribe.model.requestmodel.login.SignUpVerifyOTPRequestModel;
import com.rescribe.preference.RescribePreferencesManager;
import com.rescribe.ui.activities.HomePageActivity;
import com.rescribe.ui.activities.ResetPasswordActivity;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rescribe.util.RescribeConstants.FROM;

/**
 * Created by jeetal on 17/8/17.
 */

public class OtpConfirmationForLogin extends Fragment implements HelperResponse, OTPListener {

    private CountDownTimer mCountDownTimer;
    private final long mStartTime = 30 * 1000;
    private final long mInterval = 1000;

    @BindView(R.id.otpEditText)
    EditText mOtpEditText;

    @BindView(R.id.submitBtn)
    Button mSubmitBtn;

    @BindView(R.id.resendOtpBtn)
    TextView mResendOtpBtn;
    @BindView(R.id.progressTime)
    TextView mProgressTime;
    @BindView(R.id.headerMessageForMobileOTP)
    TextView mHeaderMessageForMobileOTP;
    @BindView(R.id.resendOtpBtnLayout)
    LinearLayout mResendOtpBtnLayout;

    private int mResendOTPCount = 0;
    private String from;
//
//    @BindView(R.id.progressBar)
//    LinearLayout mProgressBar;

    public OtpConfirmationForLogin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.enter_generated_otp, container, false);
        ButterKnife.bind(this, inflate);
        // Read sms
        OtpReader.bind(this);
        mCountDownTimer = new OtpConfirmationForLogin.MyCountDownTimer(mStartTime, mInterval);
        mCountDownTimer.start();

        if (getArguments() != null)
            from = getArguments().getString(FROM);

        return inflate;
    }

    @Override
    public void otpReceived(String smsText) {
        //Automate read sms text and navigate to HomepageActivity
        //Do whatever you want to do with the text
        CommonMethods.Log("otpReceived", "otpReceived:" + smsText);
        int value = Integer.parseInt(smsText.replaceAll("[^0-9]", ""));
        CommonMethods.Log("otpReceived", "otpReceived reformatted:" + value);
        mCountDownTimer.onFinish();
        mOtpEditText.setText(String.valueOf(value).substring(0, 4));
        mSubmitBtn.setVisibility(View.VISIBLE);
        onSubmitBtnClicked();
    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            mResendOtpBtnLayout.setVisibility(View.GONE);
            mResendOtpBtn.setVisibility(View.VISIBLE);
            mOtpEditText.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTick(long millisUntilFinished) {
//			mProgressText.setText(" "+millisUntilFinished / 1000 + " secs" );
            if (getActivity() != null) {
                if (!getActivity().isFinishing()) {
                    String format = "" + (millisUntilFinished / 1000);
                    mProgressTime.setText(format);
                }
            }
        }
    }

    @OnClick(R.id.submitBtn)
    public void onSubmitBtnClicked() {
        if (mOtpEditText.getText().toString().trim().length() == 4) {
            LoginHelper loginHelper = new LoginHelper(getActivity(), this);
            SignUpVerifyOTPRequestModel model = new SignUpVerifyOTPRequestModel();
            model.setMobileNumber(RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.MOBILE_NUMBER, getActivity()));
            model.setOtp(mOtpEditText.getText().toString().trim());

            if (from == null)
                loginHelper.doVerifyGeneratedSignUpOTP(model);
            else loginHelper.doVerifyForgetPasswordOTP(model);
        } else {
            CommonMethods.showToast(getActivity(), getString(R.string.err_otp_invalid));
        }
    }

    @OnClick(R.id.resendOtpBtn)
    public void resendOTP() {
        //otp will be resend on click of resend otp
        if (mResendOTPCount == 3) {
            CommonMethods.showToast(getActivity(), getString(R.string.err_maximum_otp_retries));
        } else {
            LoginHelper loginHelper = new LoginHelper(getActivity(), this);
            loginHelper.doLoginByOTP(RescribePreferencesManager.getString(RescribePreferencesManager.PREFERENCES_KEY.MOBILE_NUMBER, getActivity()));
        }

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_LOGIN_WITH_OTP)) {
            LoginWithOtp loginModel = (LoginWithOtp) customResponse;
            if (loginModel.getCommon().isSuccess()) {
                mResendOTPCount = mResendOTPCount + 1;
                mCountDownTimer = new OtpConfirmationForLogin.MyCountDownTimer(mStartTime, mInterval);
                mCountDownTimer.start();
                mResendOtpBtnLayout.setVisibility(View.VISIBLE);
                mSubmitBtn.setVisibility(View.VISIBLE);
                mResendOtpBtn.setVisibility(View.GONE);
                mOtpEditText.setText("");
            } else {
                CommonMethods.showToast(getActivity(), loginModel.getData());
            }
        } else if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_VERIFY_SIGN_UP_OTP)) {
            LoginModel loginModel = (LoginModel) customResponse;
            if (loginModel.getCommon().isSuccess()) {
                PatientDetail patientDetail = loginModel.getLoginData().getPatientDetail();

                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.AUTHTOKEN, loginModel.getLoginData().getAuthToken(), getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.LOGIN_STATUS, RescribeConstants.YES, getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.PATIENT_ID, String.valueOf(patientDetail.getPatientId()), getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.MOBILE_NUMBER, patientDetail.getMobileNumber(), getActivity());

                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.USER_NAME, patientDetail.getPatientName(), getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.PROFILE_PHOTO, patientDetail.getPatientImgUrl(), getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.USER_EMAIL, patientDetail.getPatientEmail(), getActivity());

                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.AGE, patientDetail.getPatientAge(), getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.USER_GENDER, patientDetail.getPatientGender(), getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.PREFERENCES_KEY.SALUTATION, "" + patientDetail.getPatientSalutation(), getActivity());

                Intent intent = new Intent(getActivity(), HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(RescribeConstants.APP_OPENING_FROM_LOGIN, true);
                startActivity(intent);
                getActivity().finish();
            } else {
                CommonMethods.showToast(getActivity(), loginModel.getCommon().getStatusMessage());
            }
        } else if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_VERIFY_FORGET_PASSWORD_OTP)) {
            ForgetPasswordModel forgetPasswordModel = (ForgetPasswordModel) customResponse;
            if (forgetPasswordModel.getCommon().isSuccess()) {
                Intent intentObj = new Intent(getActivity(), ResetPasswordActivity.class);
                startActivity(intentObj);
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