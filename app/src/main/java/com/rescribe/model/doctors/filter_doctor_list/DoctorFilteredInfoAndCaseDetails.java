
package com.rescribe.model.doctors.filter_doctor_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class DoctorFilteredInfoAndCaseDetails implements CustomResponse {
    @SerializedName("doc")
    @Expose
    private DoctorFilteredInfo doctorFilteredInfo;

    @SerializedName("cases")
    @Expose
    private LinkedHashMap<String, String[]> caseDetailList =new LinkedHashMap<>();


    private boolean isCaseDetailHeader = false;


    public boolean isCaseDetailHeader() {
        return isCaseDetailHeader;
    }

    public void setCaseDetailHeader(boolean caseDetailHeader) {
        isCaseDetailHeader = caseDetailHeader;
    }

    public DoctorFilteredInfo getDoctorFilteredInfo() {
        return doctorFilteredInfo;
    }

    public void setDoctorFilteredInfo(DoctorFilteredInfo doctorFilteredInfo) {
        this.doctorFilteredInfo = doctorFilteredInfo;
    }

    public LinkedHashMap<String, String[]> getCaseDetailList() {
        return caseDetailList;
    }

    public void setCaseDetailList(LinkedHashMap<String, String[]> caseDetailList) {
        this.caseDetailList = caseDetailList;
    }
}
