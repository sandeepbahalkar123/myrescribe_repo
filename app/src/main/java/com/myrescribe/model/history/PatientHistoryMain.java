package com.myrescribe.model.history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;

import java.util.List;

/**
 * Created by riteshpandhurkar on 15/6/17.
 */

public class PatientHistoryMain implements CustomResponse {
    @SerializedName("complaints")
    @Expose
    private List<HistoryCommonDetails> complaints = null;
    @SerializedName("vitals")
    @Expose
    private List<HistoryCommonDetails> vitals = null;
    @SerializedName("findings")
    @Expose
    private String findings;
    @SerializedName("diagnosis")
    @Expose
    private List<HistoryCommonDetails> diagnosis = null;
    @SerializedName("prescriptions")
    @Expose
    private List<HistoryCommonDetails> prescriptions = null;
    @SerializedName("investigations")
    @Expose
    private List<HistoryCommonDetails> investigations = null;
    @SerializedName("advice")
    @Expose
    private String advice;

    public List<HistoryCommonDetails> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<HistoryCommonDetails> complaints) {
        this.complaints = complaints;
    }

    public List<HistoryCommonDetails> getVitals() {
        return vitals;
    }

    public void setVitals(List<HistoryCommonDetails> vitals) {
        this.vitals = vitals;
    }

    public String getFindings() {
        return findings;
    }

    public void setFindings(String findings) {
        this.findings = findings;
    }

    public List<HistoryCommonDetails> getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(List<HistoryCommonDetails> diagnosis) {
        this.diagnosis = diagnosis;
    }

    public List<HistoryCommonDetails> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<HistoryCommonDetails> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public List<HistoryCommonDetails> getInvestigations() {
        return investigations;
    }

    public void setInvestigations(List<HistoryCommonDetails> investigations) {
        this.investigations = investigations;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
}
