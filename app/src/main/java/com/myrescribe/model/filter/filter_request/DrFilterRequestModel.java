package com.myrescribe.model.filter.filter_request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;

import java.util.ArrayList;

public class DrFilterRequestModel implements CustomResponse{

    @SerializedName("patientId")
    @Expose
    private int patientId;
    @SerializedName("docId")
    @Expose
    private ArrayList<Integer> docId = null;
    @SerializedName("docSpeciality")
    @Expose
    private ArrayList<String> docSpeciality = null;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("cases")
    @Expose
    private ArrayList<String> cases = null;

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public ArrayList<Integer> getDocId() {
        return docId;
    }

    public void setDocId(ArrayList<Integer> docId) {
        this.docId = docId;
    }

    public ArrayList<String> getDocSpeciality() {
        return docSpeciality;
    }

    public void setDocSpeciality(ArrayList<String> docSpeciality) {
        this.docSpeciality = docSpeciality;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ArrayList<String> getCases() {
        return cases;
    }

    public void setCases(ArrayList<String> cases) {
        this.cases = cases;
    }

}