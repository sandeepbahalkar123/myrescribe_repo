package com.myrescribe.model.my_records.new_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NewMyRecordDataModel {

    @SerializedName("yearsMonthsData")
    @Expose
    private ArrayList<NewYearsMonthsData> yearsMonthsData = new ArrayList<NewYearsMonthsData>();
    @SerializedName("originalData")
    @Expose
    private ArrayList<NewOriginalData> originalData = new ArrayList<NewOriginalData>();

    public ArrayList<NewYearsMonthsData> getYearsMonthsData() {
        return yearsMonthsData;
    }

    public void setYearsMonthsData(ArrayList<NewYearsMonthsData> yearsMonthsData) {
        this.yearsMonthsData = yearsMonthsData;
    }

    public ArrayList<NewOriginalData> getOriginalData() {
        return originalData;
    }

    public void setOriginalData(ArrayList<NewOriginalData> originalData) {
        this.originalData = originalData;
    }

}