package com.myrescribe.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.myrescribe.R;
import com.myrescribe.broadcast_receivers.OtpReader;
import com.myrescribe.helpers.login.LoginHelper;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.interfaces.HelperResponse;
import com.myrescribe.interfaces.OTPListener;
import com.myrescribe.model.Common;
import com.myrescribe.model.login.SignUpModel;
import com.myrescribe.model.login.VerifyOTPSignUpResponseModel;
import com.myrescribe.model.requestmodel.login.SignUpRequestModel;
import com.myrescribe.model.requestmodel.login.SignUpVerifyOTPRequestModel;
import com.myrescribe.ui.activities.AppLoginConfirmationActivity;
import com.myrescribe.ui.activities.HomePageActivity;
import com.myrescribe.ui.activities.ShowMedicineDoseListActivity;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link OTPConfirmationForSignUp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OTPConfirmationForSignUp extends Fragment implements HelperResponse, OTPListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String SENDERID = "HP-EMROTP";


    private CountDownTimer mCountDownTimer;
    private final long mStartTime = 30 * 1000;
    private final long mInterval = 1 * 1000;

    private final int mMaxSmsAttempts = 3;
    private int mSmsAttempt = 0;
    private static final int RESPONSE_SUCCESS_MESSAGE = 100;

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

    private SignUpRequestModel mSignUpRequestModel;

    private String mMobileNo;
    private int mResendOTPCount = 0;
//
//    @BindView(R.id.progressBar)
//    LinearLayout mProgressBar;

    public OTPConfirmationForSignUp() {
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
    public static OTPConfirmationForSignUp newInstance(String param1, String param2) {
        OTPConfirmationForSignUp fragment = new OTPConfirmationForSignUp();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.enter_generated_otp, container, false);
        ButterKnife.bind(this, inflate);

        OtpReader.bind(this, SENDERID);
        mCountDownTimer = new MyCountDownTimer(mStartTime, mInterval);
        mCountDownTimer.start();

        if (getArguments() != null) {
            Bundle arguments = getArguments();
            mSignUpRequestModel = (SignUpRequestModel) arguments.getSerializable(getString(R.string.details));
            mHeaderMessageForMobileOTP.setText("" + String.format(getString(R.string.message_for_mobile_otp), mSignUpRequestModel.getMobileNumber()));
        }

        mOtpEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 4) {
                    mResendOtpBtnLayout.setVisibility(View.INVISIBLE);
                    mCountDownTimer.onFinish();
                    mSubmitBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        return inflate;
    }


    private Handler messagehandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case RESPONSE_SUCCESS_MESSAGE:
                    mOtpEditText.setText(msg.getData().getString("message"));
                    mResendOtpBtn.setVisibility(View.GONE);
                    mSubmitBtn.setVisibility(View.VISIBLE);
                    //  verifyOtp(getContext(), msg.getData().getString("message"));
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public void otpReceived(String smsText) {
        //Do whatever you want to do with the text
        Log.e("otpReceived", "otpReceived:" + smsText);
        Message msg = Message.obtain();
        int value = Integer.parseInt(smsText.replaceAll("[^0-9]", ""));
        Log.e("otpReceived", "otpReceived reformatted:" + value);

        msg.what = RESPONSE_SUCCESS_MESSAGE;
        Bundle b = new Bundle();
        b.putString("message", "" + value);
        msg.setData(b);
        messagehandler.sendMessage(msg);

    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            mResendOtpBtnLayout.setVisibility(View.INVISIBLE);
            if (mOtpEditText.getText().toString().trim().length() == 0) {
                mResendOtpBtn.setVisibility(View.VISIBLE);
                mSubmitBtn.setVisibility(View.GONE);
            } else {
                mSubmitBtn.setVisibility(View.VISIBLE);
                mResendOtpBtn.setVisibility(View.GONE);
            }
            mOtpEditText.setVisibility(View.VISIBLE);
            //  mProgressBar.setVisibility(View.GONE);
            mProgressTime.setVisibility(View.VISIBLE);
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
            SignUpVerifyOTPRequestModel model = new SignUpVerifyOTPRequestModel();
            model.setMobileNumber("" + mSignUpRequestModel.getMobileNumber());
            model.setOTP(mOtpEditText.getText().toString().trim());

            LoginHelper loginHelper = new LoginHelper(getActivity(), this);
            loginHelper.doVerifyGeneratedSignUpOTP(model);
        } else {

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
        if (mOldDataTag.equalsIgnoreCase(MyRescribeConstants.TASK_SIGN_UP)) {
            SignUpModel loginModel = (SignUpModel) customResponse;
            if (loginModel.getCommon().isSuccess()) {
                mResendOTPCount = mResendOTPCount + 1;
                mCountDownTimer = new MyCountDownTimer(mStartTime, mInterval);
                mCountDownTimer.start();
                mResendOtpBtnLayout.setVisibility(View.VISIBLE);
                mSubmitBtn.setVisibility(View.GONE);
                mResendOtpBtn.setVisibility(View.GONE);
            } else {
                CommonMethods.showToast(getActivity(), loginModel.getCommon().getStatusMessage());
            }
        } else if (mOldDataTag.equalsIgnoreCase(MyRescribeConstants.TASK_VERIFY_SIGN_UP_OTP)) {
            VerifyOTPSignUpResponseModel receivedModel = (VerifyOTPSignUpResponseModel) customResponse;
            if (receivedModel.getCommon().isSuccess()) {

                //-- TODO , NEED TO ADD QUESTIONNAIRE FRAGMENT: FOR NOW GOING TO DASHBOARD
                Intent intent = new Intent(getActivity(), HomePageActivity.class);
                startActivity(intent);
            } else {
                CommonMethods.showToast(getActivity(), receivedModel.getCommon().getStatusMessage());
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

}
