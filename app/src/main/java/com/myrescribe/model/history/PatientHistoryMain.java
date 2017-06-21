package com.myrescribe.model.history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by riteshpandhurkar on 15/6/17.
 */

public class PatientHistoryMain implements CustomResponse {
    @SerializedName("complaints")
    @Expose
    private ArrayList<HistoryCommonDetails> complaints = null;
    @SerializedName("vitals")
    @Expose
    private ArrayList<HistoryCommonDetails> vitals = null;
    @SerializedName("findings")
    @Expose
    private String findings;
    @SerializedName("diagnosis")
    @Expose
    private ArrayList<HistoryCommonDetails> diagnosis = null;
    @SerializedName("prescriptions")
    @Expose
    private ArrayList<HistoryCommonDetails> prescriptions = null;
    @SerializedName("investigations")
    @Expose
    private ArrayList<HistoryCommonDetails> investigations = null;
    @SerializedName("advice")
    @Expose
    private String advice;

    public ArrayList<HistoryCommonDetails> getComplaints() {
        return complaints;
    }

    public void setComplaints(ArrayList<HistoryCommonDetails> complaints) {
        this.complaints = complaints;
    }

    public ArrayList<HistoryCommonDetails> getVitals() {
        return vitals;
    }

    public void setVitals(ArrayList<HistoryCommonDetails> vitals) {
        this.vitals = vitals;
    }

    public String getFindings() {
        return findings;
    }

    public void setFindings(String findings) {
        this.findings = findings;
    }

    public ArrayList<HistoryCommonDetails> getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(ArrayList<HistoryCommonDetails> diagnosis) {
        this.diagnosis = diagnosis;
    }

    public ArrayList<HistoryCommonDetails> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(ArrayList<HistoryCommonDetails> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public ArrayList<HistoryCommonDetails> getInvestigations() {
        return investigations;
    }

    public void setInvestigations(ArrayList<HistoryCommonDetails> investigations) {
        this.investigations = investigations;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
}
