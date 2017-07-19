package com.myrescribe.model.doctors.doctor_info;

import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by riteshpandhurkar on 15/6/17.
 */

public class DoctorInfoMonthContainer implements CustomResponse {

    @SerializedName("year")
    private String year;
    @SerializedName("months")
    private Map<String, ArrayList<DoctorDetail>> monthWiseSortedDoctorList = new TreeMap<String, ArrayList<DoctorDetail>>(String.CASE_INSENSITIVE_ORDER);

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Map<String, ArrayList<DoctorDetail>> getMonthWiseSortedDoctorList() {
        return monthWiseSortedDoctorList;
    }

    @Override
    public String toString() {
        return "DoctorInfoMonthContainer{" +
                "year='" + year + '\'' +
                ", doctorList=" + monthWiseSortedDoctorList +
                '}';
    }
}
