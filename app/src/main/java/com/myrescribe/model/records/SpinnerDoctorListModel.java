package com.myrescribe.model.records;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SpinnerDoctorListModel {

    @SerializedName("doctors")
    @Expose
    private ArrayList<SpinnerDoctor> doctors = new ArrayList<SpinnerDoctor>();

    public ArrayList<SpinnerDoctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(ArrayList<SpinnerDoctor> doctors) {
        this.doctors = doctors;
    }

}