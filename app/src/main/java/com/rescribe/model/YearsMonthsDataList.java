package com.rescribe.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YearsMonthsDataList {

    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("months")
    @Expose
    private String[] months;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String[] getMonths() {
        return months;
    }

    public void setMonths(String[] months) {
        this.months = months;
    }
}