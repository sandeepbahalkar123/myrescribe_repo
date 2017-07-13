package com.myrescribe.model.doctors;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.R;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

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
