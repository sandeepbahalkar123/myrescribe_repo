package com.rescribe.model.my_records;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

import java.util.ArrayList;
import java.util.List;

public class MyRecordsDoctorListModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private ArrayList<SpinnerDoctor> doctors = new ArrayList<SpinnerDoctor>();

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public ArrayList<SpinnerDoctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(ArrayList<SpinnerDoctor> doctors) {
        this.doctors = doctors;
    }

}