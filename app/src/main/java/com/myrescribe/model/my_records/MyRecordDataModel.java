
package com.myrescribe.model.my_records;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.model.Common;
import com.myrescribe.model.YearsMonthsDataList;
import com.myrescribe.model.login.Year;

import java.util.ArrayList;
import java.util.HashSet;

public class MyRecordDataModel implements CustomResponse {

    @SerializedName("yearsMonthsData")
    @Expose
    private ArrayList<YearsMonthsDataList> receivedYearMap;
    @SerializedName("originalData")
    @Expose
    private MyRecordInfoMonthContainer myRecordInfoMonthContainer;

    public ArrayList<YearsMonthsDataList> getReceivedYearMap() {
        return receivedYearMap;
    }

    public void setReceivedYearMap(ArrayList<YearsMonthsDataList> receivedYearMap) {
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


    public ArrayList<Year> getFormattedYearList() {
        ArrayList<YearsMonthsDataList> yearsMonthsDataList = getReceivedYearMap();
        ArrayList<Year> yearList = new ArrayList<>();
        for (YearsMonthsDataList yearObject :
                yearsMonthsDataList) {
            String[] months = yearObject.getMonths();
            if (months.length > 0) {
                for (int i = 0; i < months.length; i++) {
                    Year year = new Year();
                    year.setYear(yearObject.getYear());
                    year.setMonthName(months[i]);
                    yearList.add(year);
                }
            }
        }
        return yearList;
    }

    public ArrayList<String> getUniqueYears() {
        ArrayList<YearsMonthsDataList> yearsMonthsDataList = getReceivedYearMap();
        HashSet<String> strings = new HashSet<>();
        for (YearsMonthsDataList yearObject :
                yearsMonthsDataList) {
            strings.add(yearObject.getYear());
        }
        return new ArrayList(strings);
    }
}
