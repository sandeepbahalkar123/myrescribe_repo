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
import java.util.TreeSet;

/**
 * Created by riteshpandhurkar on 15/6/17.
 */

public class DoctorInfoMonthContainer implements CustomResponse {

    @SerializedName("year")
    private String year;
    @SerializedName("months")
    private HashMap<String, ArrayList<DoctorDetail>> monthWiseSortedDoctorList = null;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public HashMap<String, ArrayList<DoctorDetail>> getMonthWiseSortedDoctorList() {
        return monthWiseSortedDoctorList;
    }

    public ArrayList<DoctorDetail> getFormattedDoctorList(String month, Context context) {

        ArrayList<DoctorDetail> doctorDetails = getMonthWiseSortedDoctorList().get(month);
        ArrayList<DoctorDetail> map = new ArrayList<>();
        //-----------
        TreeSet<String> dateHashSet = new TreeSet<String>(new DateWiseComparator());
        for (DoctorDetail data :
                doctorDetails) {
            dateHashSet.add(data.getDate());
        }
        //-----------
        int color = ContextCompat.getColor(context, R.color.white);
        int previousColor = color;
        for (String dateString :
                dateHashSet) {
            boolean flag = true;
            for (DoctorDetail data :
                    doctorDetails) {
                if (dateString.equalsIgnoreCase(data.getDate())) {
                    if (flag) {
                        data.setRowColor(color);
                        if (color == ContextCompat.getColor(context, R.color.white)) {
                            previousColor = color;
                            color = ContextCompat.getColor(context, R.color.divider);
                        } else if (color == ContextCompat.getColor(context, R.color.divider)) {
                            previousColor = color;
                            color = ContextCompat.getColor(context, R.color.white);
                        }
                        data.setStartElement(true);
                        flag = false;
                    } else if (!flag) {
                        data.setRowColor(previousColor);
                    }
                    map.add(data);
                }
            }
        }
        //----------
        return map;
    }

    //-- Sort date in descending order, copied from SRDaoImplManager.java
    private class DateWiseComparator implements Comparator<String> {

        public int compare(String m1, String m2) {

            //possibly check for nulls to avoid NullPointerException
            Date m1Date = CommonMethods.convertStringToDate(m1, MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY);
            Date m2Date = CommonMethods.convertStringToDate(m2, MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY);

            return m2Date.compareTo(m1Date);
        }
    }

    @Override
    public String toString() {
        return "DoctorInfoMonthContainer{" +
                "year='" + year + '\'' +
                ", doctorList=" + monthWiseSortedDoctorList +
                '}';
    }
}
