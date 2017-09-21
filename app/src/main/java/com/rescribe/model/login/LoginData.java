package com.rescribe.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ganeshshirole on 7/9/17.
 */

public class LoginData {
    @SerializedName("authToken")
    @Expose
    private String authToken;
    @SerializedName("patientId")
    @Expose
    private String patientId;
    @SerializedName("patientName")
    @Expose
    private String patientName;
    @SerializedName("patientImgUrl")
    @Expose
    private String patientImgUrl;
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("patientEmail")
    @Expose
    private String patientEmail;

    public String getPatientId() {
        return patientId;
    }
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    public String getAuthToken() {
        return authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientImgUrl() {
        return patientImgUrl;
    }

    public void setPatientImgUrl(String patientImgUrl) {
        this.patientImgUrl = patientImgUrl;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }
}
