package com.myrescribe.model.filter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.model.doctors.doctor_info.DoctorDetail;

import java.util.ArrayList;
import java.util.List;

public class FilterDoctorModel {

    @SerializedName("doctors")
    @Expose
    private ArrayList<DoctorDetail> doctors = null;

    public ArrayList<DoctorDetail> getDoctors() {
        return doctors;
    }

    public void setDoctors(ArrayList<DoctorDetail> doctors) {
        this.doctors = doctors;
    }

}