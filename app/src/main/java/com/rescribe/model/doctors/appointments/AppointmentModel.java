package com.rescribe.model.doctors.appointments;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentModel {

    @SerializedName("aptList")
    @Expose
    private ArrayList<AptList> aptList = null;

    public ArrayList<AptList> getAptList() {
        return aptList;
    }

    public void setAptList(ArrayList<AptList> aptList) {
        this.aptList = aptList;
    }

}