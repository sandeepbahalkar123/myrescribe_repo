
package com.rescribe.model.doctors.filter_doctor_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

import java.util.ArrayList;

public class DoctorFilterModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private ArrayList<DoctorFilteredInfoAndCaseDetails> doctorsInfoAndCaseDetailsList;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public ArrayList<DoctorFilteredInfoAndCaseDetails> getDoctorsInfoAndCaseDetailsList() {
        return doctorsInfoAndCaseDetailsList;
    }

    public void setDoctorsInfoAndCaseDetailsList(ArrayList<DoctorFilteredInfoAndCaseDetails> doctorsInfoAndCaseDetailsList) {
        this.doctorsInfoAndCaseDetailsList = doctorsInfoAndCaseDetailsList;
    }

    @Override
    public String toString() {
        return "DoctorFilterModel{" +
                "common=" + common +
                ", doctorsInfoAndCaseDetailsList=" + doctorsInfoAndCaseDetailsList +
                '}';
    }


}
