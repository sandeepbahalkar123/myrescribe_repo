package com.myrescribe.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.broadcast_receivers.OtpReader;
import com.myrescribe.helpers.login.LoginHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.interfaces.OTPListener;
import com.myrescribe.model.login.LoginModel;
import com.myrescribe.model.login.SignUpModel;
import com.myrescribe.model.requestmodel.login.SignUpRequestModel;
import com.myrescribe.model.requestmodel.login.SignUpVerifyOTPRequestModel;
import com.myrescribe.preference.MyRescribePreferencesManager;
import com.myrescribe.ui.activities.HomePageActivity;
import com.myrescribe.ui.activities.LoginMainActivity;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 17/8/17.
 */

public class OtpConfirmationForLogin extends Fragment implements HelperResponse, OTPListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String SENDERID = "HP-EMROTP";
    private CountDownTimer mCountDownTimer;
    private final long mStartTime = 30 * 1000;
    private final long mInterval = 1 * 1000;

    @BindView(R.id.otpEditText)
    EditText mOtpEditText;
    @BindView(R.id.doneBtn)
    Button mDoneBtn;
    @BindView(R.id.resendOtpBtn)
    TextView mResendOtpBtn;
    @BindView(R.id.progressTime)
    TextView mProgressTime;
    @BindView(R.id.headerMessageForMobileOTP)
    TextView mHeaderMessageForMobileOTP;
    @BindView(R.id.resendOtpBtnLayout)
    LinearLayout mResendOtpBtnLayout;
    @BindView(R.id.passwordEditText)
    EditText mPasswordEditText;
    private SignUpRequestModel mSignUpRequestModel;
    private int mResendOTPCount = 0;
//
//    @BindView(R.id.progressBar)
//    LinearLayout mProgressBar;

    public OtpConfirmationForLogin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUp.
     */
    // TODO: Rename and change types and number of parameters
    public static OtpConfirmationForLogin newInstance(String param1, String param2) {
        OtpConfirmationForLogin fragment = new OtpConfirmationForLogin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.enter_otp_password_login, container, false);
        ButterKnife.bind(this, inflate);

        OtpReader.bind(this, SENDERID);
        mCountDownTimer = new OtpConfirmationForLogin.MyCountDownTimer(mStartTime, mInterval);
        mCountDownTimer.start();

        mOtpEditText.addTextChangedListener(new CustomTextWatcher(mOtpEditText, getString(R.string.enter_otp_for_login)));
        mPasswordEditText.addTextChangedListener(new CustomTextWatcher(mPasswordEditText, getString(R.string.enter_password)));
       /* if (getArguments() != null) {
            Bundle arguments = getArguments();
            mSignUpRequestModel = (SignUpRequestModel) arguments.getSerializable(getString(R.string.details));
            mHeaderMessageForMobileOTP.setText("" + String.format(getString(R.string.message_for_mobile_otp), mSignUpRequestModel.getMobileNumber()));
        }*/


        return inflate;
    }

    @Override
    public void otpReceived(String smsText) {
        //Do whatever you want to do with the text
        CommonMethods.Log("otpReceived", "otpReceived:" + smsText);
        int value = Integer.parseInt(smsText.replaceAll("[^0-9]", ""));
        CommonMethods.Log("otpReceived", "otpReceived reformatted:" + value);
        mCountDownTimer.onFinish();
        mOtpEditText.setText(String.valueOf(value).substring(0, 4));
        mDoneBtn.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.doneBtn)
    public void onpasswordClicked() {
        if (mPasswordEditText.getText().toString().trim().length() < 8 && mOtpEditText.getText().length() == 0) {
            String message = getString(R.string.error_too_small_password);
            mPasswordEditText.setError(message);
            mPasswordEditText.requestFocus();

        } else if (mOtpEditText.getText().length() == 0 && mPasswordEditText.getText().toString().trim().length() > 8) {
            LoginHelper loginHelperForPassword = new LoginHelper(getActivity(), this);
            loginHelperForPassword.doLoginByPassword(mPasswordEditText.getText().toString());
        } else if (mOtpEditText.getText().toString().trim().length() == 4 && mPasswordEditText.getText().toString().trim().length() == 0) {
            onSubmitBtnClicked();
        }


    }

    public void onSubmitBtnClicked() {
        if (mOtpEditText.getText().toString().trim().length() == 4) {
            LoginHelper loginHelper = new LoginHelper(getActivity(), this);
            loginHelper.doLoginByOTP(mOtpEditText.getText().toString());
        } else {
            CommonMethods.showToast(getActivity(), getString(R.string.err_otp_invalid));
        }
    }

    @OnClick(R.id.resendOtpBtn)
    public void resendOTP() {
        if (mResendOTPCount == 3) {
            CommonMethods.showToast(getActivity(), getString(R.string.err_maximum_otp_retries));
        } else {
            LoginHelper loginHelper = new LoginHelper(getActivity(), this);
            loginHelper.doSignUp(mSignUpRequestModel);
        }

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(MyRescribeConstants.TASK_LOGIN_WITH_PASSWORD)) {
            LoginModel receivedModel = (LoginModel) customResponse;
            if (receivedModel.getCommon().isSuccess()) {
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.AUTHTOKEN, receivedModel.getAuthToken(), getActivity());
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATEINT_ID, receivedModel.getPatientId(), getActivity());
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, MyRescribeConstants.YES, getActivity());
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER, mSignUpRequestModel.getMobileNumber().toString(), getActivity());
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PASSWORD, mSignUpRequestModel.getPassword().toString(), getActivity());
                Intent intent = new Intent(getActivity(), HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            } else {
                Intent intent = new Intent(getActivity(), LoginMainActivity.class);
                intent.putExtra(getString(R.string.type),MyRescribeConstants.TASK_LOGIN_WITH_PASSWORD);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        } else if (mOldDataTag.equalsIgnoreCase(MyRescribeConstants.TASK_LOGIN_WITH_OTP)) {

            LoginModel receivedModel = (LoginModel) customResponse;
            if (receivedModel.getCommon().isSuccess()) {
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.AUTHTOKEN, receivedModel.getAuthToken(), getActivity());
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PATEINT_ID, receivedModel.getPatientId(), getActivity());
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, MyRescribeConstants.YES, getActivity());
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER, mSignUpRequestModel.getMobileNumber().toString(), getActivity());
                MyRescribePreferencesManager.putString(MyRescribePreferencesManager.MYRESCRIBE_PREFERENCES_KEY.PASSWORD, mSignUpRequestModel.getPassword().toString(), getActivity());

                Intent intent = new Intent(getActivity(), HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            } else {
                Intent intent = new Intent(getActivity(), LoginMainActivity.class);
                intent.putExtra(getString(R.string.type),MyRescribeConstants.TASK_LOGIN_WITH_OTP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
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

    private class CustomTextWatcher implements TextWatcher {
        private EditText mEditText;
        String checkStringForOtpOrPassword;

        public CustomTextWatcher(EditText e, String textForOtpOrPassword) {
            mEditText = e;
            checkStringForOtpOrPassword = textForOtpOrPassword;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (checkStringForOtpOrPassword.equalsIgnoreCase(getString(R.string.enter_otp_for_login))) {
                mPasswordEditText.setEnabled(false);
                mPasswordEditText.setFocusable(false);
            } else if (checkStringForOtpOrPassword.equalsIgnoreCase(getString(R.string.enter_password))) {
                mOtpEditText.setFocusable(false);
                mOtpEditText.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (checkStringForOtpOrPassword.equalsIgnoreCase(getString(R.string.enter_otp_for_login))) {
                if (mOtpEditText.getText().length() == 0) {
                    mPasswordEditText.setFocusableInTouchMode(true);
                    mPasswordEditText.setEnabled(true);
                } else {
                    mPasswordEditText.setEnabled(false);
                    mPasswordEditText.setFocusable(false);
                }
            } else if (checkStringForOtpOrPassword.equalsIgnoreCase(getString(R.string.enter_password))) {
                if (mPasswordEditText.getText().length() == 0) {
                    mOtpEditText.setFocusableInTouchMode(true);
                    mOtpEditText.setEnabled(true);
                } else {
                    mOtpEditText.setEnabled(false);
                    mOtpEditText.setFocusable(false);
                }
            }
        }
    }
}
