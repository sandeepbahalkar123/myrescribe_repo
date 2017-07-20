package com.myrescribe.model.filter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.model.doctors.doctor_info.DoctorDetail;

import java.util.List;

public class FilterDoctorModel {

    @SerializedName("doctors")
    @Expose
    private List<DoctorDetail> doctors = null;

    public List<DoctorDetail> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<DoctorDetail> doctors) {
        this.doctors = doctors;
    }

}