package com.myrescribe.model.visit_details;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;

public class PatientHistory implements CustomResponse {

    @SerializedName("complaints")
    @Expose
    private ArrayList<Diagnosi> complaints = null;

    @SerializedName("remarks")
    @Expose
    private ArrayList<Diagnosi> remarks = null;

    @SerializedName("vitals")
    @Expose
    private ArrayList<Diagnosi> vitals = null;

    public ArrayList<Diagnosi> getVitals() {
        return vitals;
    }

    public void setVitals(ArrayList<Diagnosi> vitals) {
        this.vitals = vitals;
    }

    @SerializedName("diagnosis")
    @Expose
    private ArrayList<Diagnosi> diagnosis = null;

    @SerializedName("prescriptions")
    @Expose
    private ArrayList<Diagnosi> prescriptions = null;
    @SerializedName("investigations")
    @Expose
    private ArrayList<Diagnosi> investigations = null;

    public ArrayList<Diagnosi> getAdvice() {
        return advice;
    }

    public void setAdvice(ArrayList<Diagnosi> advice) {
        this.advice = advice;
    }

    @SerializedName("advice")
    @Expose

    private ArrayList<Diagnosi> advice = null;

    public ArrayList<Diagnosi> getComplaints() {
        return complaints;
    }

    public void setComplaints(ArrayList<Diagnosi> complaints) {
        this.complaints = complaints;
    }


    public ArrayList<Diagnosi> getRemarks() {
        return remarks;
    }

    public void setRemarks(ArrayList<Diagnosi> remarks) {
        this.remarks = remarks;
    }

    public ArrayList<Diagnosi> getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(ArrayList<Diagnosi> diagnosis) {
        this.diagnosis = diagnosis;
    }

    public ArrayList<Diagnosi> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(ArrayList<Diagnosi> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public ArrayList<Diagnosi> getInvestigations() {
        return investigations;
    }

    public void setInvestigations(ArrayList<Diagnosi> investigations) {
        this.investigations = investigations;
    }


}