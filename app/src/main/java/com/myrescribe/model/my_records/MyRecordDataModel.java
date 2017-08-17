
package com.myrescribe.model.my_records;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.model.Common;

import java.util.ArrayList;

public class MyRecordDataModel implements CustomResponse {

    @SerializedName("yearsMonthsData")
    @Expose
    private ArrayList<MyRecordYearMap> receivedYearMap;
    @SerializedName("originalData")
    @Expose
    private MyRecordInfoMonthContainer myRecordInfoMonthContainer;

    public ArrayList<MyRecordYearMap> getReceivedYearMap() {
        return receivedYearMap;
    }

    public void setReceivedYearMap(ArrayList<MyRecordYearMap> receivedYearMap) {
        this.receivedYearMap = receivedYearMap;
    }

    public MyRecordInfoMonthContainer getMyRecordInfoMonthContainer() {
        return myRecordInfoMonthContainer;
    }

    public void setMyRecordInfoMonthContainer(MyRecordInfoMonthContainer myRecordInfoMonthContainer) {
        this.myRecordInfoMonthContainer = myRecordInfoMonthContainer;
    }

    @Override
    public String toString() {
        return "DoctorFilterModel{" +
                "receivedYearMap=" + receivedYearMap +
                ", myRecordInfoMonthContainer=" + myRecordInfoMonthContainer +
                '}';
    }

    private class MyRecordYearMap implements CustomResponse {
        @SerializedName("year")
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
}
