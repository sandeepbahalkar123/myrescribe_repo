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
    @SerializedName("patientDetail")
    @Expose
    private PatientDetail patientDetail;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public PatientDetail getPatientDetail() {
        return patientDetail;
    }

    public void setPatientDetail(PatientDetail patientDetail) {
        this.patientDetail = patientDetail;
    }
}
