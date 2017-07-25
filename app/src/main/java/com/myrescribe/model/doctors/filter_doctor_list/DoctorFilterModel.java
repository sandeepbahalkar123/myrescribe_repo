
package com.myrescribe.model.doctors.filter_doctor_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.model.Common;
import com.myrescribe.model.doctors.doctor_info.DoctorInfoMonthContainer;

import java.util.ArrayList;

public class DoctorFilterModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private ArrayList<DoctorFilteredInfo> doctorFilteredInfoList;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public ArrayList<DoctorFilteredInfo> getDoctorFilteredInfoList() {
        return doctorFilteredInfoList;
    }

    public void setDoctorFilteredInfoList(ArrayList<DoctorFilteredInfo> doctorFilteredInfoList) {
        this.doctorFilteredInfoList = doctorFilteredInfoList;
    }

    @Override
    public String toString() {
        return "DoctorFilterModel{" +
                "common=" + common +
                ", doctorFilteredInfoList=" + doctorFilteredInfoList +
                '}';
    }
}
