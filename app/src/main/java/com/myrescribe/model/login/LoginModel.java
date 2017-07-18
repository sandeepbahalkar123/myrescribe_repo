package com.myrescribe.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.model.requestmodel.login.Common;

public class LoginModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("authToken")
    @Expose
    private String authToken;
    @SerializedName("patientId")
    @Expose
    private String patientId;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

}