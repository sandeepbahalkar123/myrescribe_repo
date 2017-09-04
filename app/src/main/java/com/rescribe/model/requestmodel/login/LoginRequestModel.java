package com.rescribe.model.requestmodel.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class LoginRequestModel implements CustomResponse {

    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("password")
    @Expose
    private String password;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}