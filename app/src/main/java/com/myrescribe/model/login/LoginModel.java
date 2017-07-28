package com.myrescribe.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.model.Common;

import java.util.ArrayList;

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

    //TODO: Need to parsed this, once done from server side.
    private ArrayList<String> yearList = new ArrayList<>();

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

    public ArrayList<String> getYearList() {
        return yearList;
    }

    public void setYearList(ArrayList<String> yearList) {
        this.yearList = yearList;
    }
}