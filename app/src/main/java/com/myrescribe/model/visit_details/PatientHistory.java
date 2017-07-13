package com.myrescribe.model.visit_details;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientHistory {

@SerializedName("complaints")
@Expose
private List<Object> complaints = null;
@SerializedName("vitals")
@Expose
private List<Object> vitals = null;
@SerializedName("remarks")
@Expose
private List<Object> remarks = null;
@SerializedName("diagnosis")
@Expose
private List<Diagnosi> diagnosis = null;
@SerializedName("prescriptions")
@Expose
private List<Prescription> prescriptions = null;
@SerializedName("investigations")
@Expose
private List<Object> investigations = null;
@SerializedName("advice")
@Expose
private String advice;

public List<Object> getComplaints() {
return complaints;
}

public void setComplaints(List<Object> complaints) {
this.complaints = complaints;
}

public List<Object> getVitals() {
return vitals;
}

public void setVitals(List<Object> vitals) {
this.vitals = vitals;
}

public List<Object> getRemarks() {
return remarks;
}

public void setRemarks(List<Object> remarks) {
this.remarks = remarks;
}

public List<Diagnosi> getDiagnosis() {
return diagnosis;
}

public void setDiagnosis(List<Diagnosi> diagnosis) {
this.diagnosis = diagnosis;
}

public List<Prescription> getPrescriptions() {
return prescriptions;
}

public void setPrescriptions(List<Prescription> prescriptions) {
this.prescriptions = prescriptions;
}

public List<Object> getInvestigations() {
return investigations;
}

public void setInvestigations(List<Object> investigations) {
this.investigations = investigations;
}

public String getAdvice() {
return advice;
}

public void setAdvice(String advice) {
this.advice = advice;
}

}