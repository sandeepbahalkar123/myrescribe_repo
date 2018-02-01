package com.rescribe.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.rescribe.interfaces.OTPListener;
import com.rescribe.util.CommonMethods;

import static com.rescribe.util.RescribeConstants.SENDERID;
import static com.rescribe.util.RescribeConstants.SENDERID_2;


/**
 * Created by swarajpal on 13-12-2015.
 * BroadcastReceiver OtpReader for receiving and processing the SMS messages.
 */
public class OtpReader extends BroadcastReceiver {

    /**
     * Constant TAG for logging key.
     */
    private static final String TAG = "OtpReader";

    /**
     * The bound OTP Listener that will be trigerred on receiving message.
     */
    private static OTPListener otpListener;

    /**
     * Binds the sender string and listener for callback.
     *
     * @param listener
     */
    public static void bind(OTPListener listener) {
        otpListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        if (bundle != null) {

            final Object[] pdusArr = (Object[]) bundle.get("pdus");

            if (pdusArr != null) {
                for (Object aPdusArr : pdusArr) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusArr);
                    String senderNum = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();
                    CommonMethods.Log(TAG, "senderNum: " + senderNum + " message: " + message);
                    if (senderNum.contains(SENDERID) || senderNum.contains(SENDERID_2)) { //If message received is from required number.
                        //If bound a listener interface, callback the overriden method.
                        if (otpListener != null) {
                            otpListener.otpReceived(message);
                        }
                    }
                }
            }
        }
    }
}
