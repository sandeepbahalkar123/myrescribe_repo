package com.myrescribe.ui.fragments;

import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.myrescribe.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.format;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EnterGeneratedOTP.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EnterGeneratedOTP#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnterGeneratedOTP extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private SmsReceiver mSmsListener = new SmsReceiver();
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

    @BindView(R.id.progressBar)
    LinearLayout mProgressBar;

    public EnterGeneratedOTP() {
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
    public static EnterGeneratedOTP newInstance(String param1, String param2) {
        EnterGeneratedOTP fragment = new EnterGeneratedOTP();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.enter_generated_otp, container, false);
        ButterKnife.bind(this, inflate);

        getActivity().registerReceiver(mSmsListener, new IntentFilter(
                "android.provider.Telephony.SMS_RECEIVED"));

        mCountDownTimer = new MyCountDownTimer(mStartTime, mInterval);
        mCountDownTimer.start();

        return inflate;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class SmsReceiver extends BroadcastReceiver {
        private final String TAG = SmsReceiver.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent) {

            final Bundle bundle = intent.getExtras();
            try {
                if (bundle != null) {
                    Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (Object aPdusObj : pdusObj) {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                        String senderAddress = currentMessage.getDisplayOriginatingAddress();

                        SmsMessage[] msgs = null;
                        String msg_from;
                        if (bundle != null) {
                            // ---retrieve the SMS message received---
                            try {
                                Message msg = Message.obtain();
                                String message = "";
                                message = currentMessage.getDisplayMessageBody();

                                if (senderAddress.contains("GLMIKA")) {

                                    Log.e(TAG, "Received SMS: " + message + ", Sender: " + senderAddress);
                                    String arr[] = message.split(" ", 2);

                                    String OTP = arr[0];   //the
                                    String theRest = arr[1];
                                    if (OTP.length() == 6) {
                                        msg.what = RESPONSE_SUCCESS_MESSAGE;
                                        Bundle b = new Bundle();
                                        b.putString("message", OTP);
                                        msg.setData(b);
                                        messagehandler.sendMessage(msg);
                                    }
                                }

                            } catch (Exception e) {
                                // Log.d("Exception caught",e.getMessage());
                            }
                        }

                    }

                }
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private Handler messagehandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case RESPONSE_SUCCESS_MESSAGE:
                    mOtpEditText.setText(msg.getData().getString("message"));
                    mResendOtpBtn.setVisibility(View.GONE);
                    verifyOtp(getContext(), msg.getData().getString("message"));
                    break;

                default:
                    break;
            }
        }
    };

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            mResendOtpBtn.setVisibility(View.VISIBLE);
            mOtpEditText.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mProgressTime.setVisibility(View.GONE);
        }

        @Override
        public void onTick(long millisUntilFinished) {
//			mProgressText.setText(" "+millisUntilFinished / 1000 + " secs" );
            String format = String.format(getString(R.string.waiting_for_sms), (millisUntilFinished / 1000));
            mProgressTime.setText(format);
        }
    }


    public void verifyOtp(Context mContext, String Otp) {

    }

    public void resendOtp() {
//        if (NetworkCheck.getConnectivityStatusString(context)) {
//
//            if (mSmsAttempt >= mMaxSmsAttempts) {
//                resendOtpBtn.setVisibility(View.GONE);
//                Toast.makeText(OtpActivity.this, "A few deep breaths and you shall recieve your OTP via SMS", Toast.LENGTH_SHORT).show();
//
//                return;
//            }
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setMessage("Resend Otp...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//            //fname=Dheeraj&playerid=rdwerwerw&emailid=dheeraj@myblackbean.com&mobile=9765482155&otp=6ce62
//            RequestQueue queue = Volley.newRequestQueue(context);
//            final Map<String, String> params = new HashMap<String, String>();
//            params.put("mobile", mobile);
//            CustomRequest jsCustomRequest = new CustomRequest(Request.Method.POST, URLS.BASEURL + URLS.RESEND_OTP, params, createMyReqSuccessListener("resend"), createMyReqErrorListener());
//            Log.d("URLs", jsCustomRequest.toString());
//            queue.add(jsCustomRequest);
//
//        } else {
//            Toast.makeText(context, "Please check your Internet Connection and try again  ", Toast.LENGTH_SHORT).show();
//        }
    }
}
