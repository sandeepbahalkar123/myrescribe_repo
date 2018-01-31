package com.rescribe.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgetPasswordData {

    @SerializedName("patientDetail")
    @Expose
    private ForgetPassPatientDetail patientDetail;

    public ForgetPassPatientDetail getPatientDetail() {
        return patientDetail;
    }

    public void setPatientDetail(ForgetPassPatientDetail patientDetail) {
        this.patientDetail = patientDetail;
    }

}