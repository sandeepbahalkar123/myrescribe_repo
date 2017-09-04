package com.rescribe.model.investigation.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

import java.util.ArrayList;

public class InvestigationUploadByGmailRequest implements CustomResponse {

    @SerializedName("investigationId")
    @Expose
    private ArrayList<Integer> investigationId = new ArrayList<Integer>();
    @SerializedName("patientId")
    @Expose
    private int patientId;

    public ArrayList<Integer> getInvestigationId() {
        return investigationId;
    }

    public void setInvestigationId(ArrayList<Integer> investigationId) {
        this.investigationId = investigationId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

}