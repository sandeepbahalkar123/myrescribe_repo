package com.rescribe.model.doctors.doctor_info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class DoctorListYearsMonthsData implements Serializable{

    @SerializedName("year")
    @Expose
    private int year;
    @SerializedName("months")
    @Expose
    private ArrayList<String> months = new ArrayList<String>();

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ArrayList<String> getMonths() {
        return months;
    }

    public void setMonths(ArrayList<String> months) {
        this.months = months;
    }

}