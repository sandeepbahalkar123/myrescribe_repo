package com.rescribe.model.doctors.doctor_info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.model.login.Year;
import com.rescribe.model.my_records.new_pojo.NewOriginalData;
import com.rescribe.model.my_records.new_pojo.NewYearsMonthsData;

import java.util.ArrayList;
import java.util.HashSet;

public class DoctorDataModel {

    @SerializedName("yearsMonthsData")
    @Expose
    private ArrayList<DoctorListYearsMonthsData> receivedYearMap = new ArrayList<DoctorListYearsMonthsData>();

    @SerializedName("docList")
    @Expose
    private DoctorInfoMonthContainer doctorInfoMonthContainer;

    public DoctorInfoMonthContainer getDoctorInfoMonthContainer() {
        return doctorInfoMonthContainer;
    }

    public void setDoctorInfoMonthContainer(DoctorInfoMonthContainer doctorInfoMonthContainer) {
        this.doctorInfoMonthContainer = doctorInfoMonthContainer;
    }

    public ArrayList<DoctorListYearsMonthsData> getReceivedYearMap() {
        return receivedYearMap;
    }

    public void setReceivedYearMap(ArrayList<DoctorListYearsMonthsData> yearsMonthsData) {
        this.receivedYearMap = yearsMonthsData;
    }


    public ArrayList<Year> getFormattedYearList() {
        ArrayList<DoctorListYearsMonthsData> yearsMonthsDataList = getReceivedYearMap();
        ArrayList<Year> yearList = new ArrayList<>();
        for (DoctorListYearsMonthsData yearObject :
                yearsMonthsDataList) {
            String[] months = yearObject.getMonths().toArray(new String[yearObject.getMonths().size()]);
            if (months.length > 0) {
                for (int i = 0; i < months.length; i++) {
                    Year year = new Year();
                    year.setYear(String.valueOf(yearObject.getYear()));
                    year.setMonthName(months[i]);
                    yearList.add(year);
                }
            }
        }
        return yearList;
    }

    public ArrayList<String> getUniqueYears() {
        ArrayList<DoctorListYearsMonthsData> yearsMonthsDataList = getReceivedYearMap();
        HashSet<String> strings = new HashSet<>();
        for (DoctorListYearsMonthsData yearObject :
                yearsMonthsDataList) {
            strings.add(String.valueOf(yearObject.getYear()));
        }
        return new ArrayList(strings);
    }

    @Override
    public String toString() {
        return "DoctorBaseModel{" +
                ", doctorInfoMonthContainer=" + doctorInfoMonthContainer +
                '}';
    }
}