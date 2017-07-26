package com.myrescribe.model.requestmodel.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;

import java.io.Serializable;

public class SignUpVerifyOTPRequestModel implements CustomResponse, Serializable {

    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("otp")
    @Expose
    private String OTP;

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}