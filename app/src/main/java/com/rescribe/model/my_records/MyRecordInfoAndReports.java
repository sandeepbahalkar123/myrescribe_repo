
package com.rescribe.model.my_records;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

import java.util.ArrayList;

public class MyRecordInfoAndReports implements CustomResponse {
    @SerializedName("doc")
    @Expose
    private MyRecordDoctorInfo myRecordDoctorInfo;

    @SerializedName("reports")
    @Expose
    private ArrayList<MyRecordReports> myRecordReportInfo;


    private boolean isCaseDetailHeader = false;

    public boolean isCaseDetailHeader() {
        return isCaseDetailHeader;
    }

    public void setCaseDetailHeader(boolean caseDetailHeader) {
        isCaseDetailHeader = caseDetailHeader;
    }

    public MyRecordDoctorInfo getMyRecordDoctorInfo() {
        return myRecordDoctorInfo;
    }

    public void setMyRecordDoctorInfo(MyRecordDoctorInfo myRecordDoctorInfo) {
        this.myRecordDoctorInfo = myRecordDoctorInfo;
    }

    public ArrayList<MyRecordReports> getMyRecordReportInfo() {
        return myRecordReportInfo;
    }

    public void setMyRecordReportInfo(ArrayList<MyRecordReports> myRecordReportInfo) {
        this.myRecordReportInfo = myRecordReportInfo;
    }
}
