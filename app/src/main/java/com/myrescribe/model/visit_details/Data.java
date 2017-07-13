package com.myrescribe.model.visit_details;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

@SerializedName("patientHistory")
@Expose
private List<PatientHistory> patientHistory = null;

public List<PatientHistory> getPatientHistory() {
return patientHistory;
}

public void setPatientHistory(List<PatientHistory> patientHistory) {
this.patientHistory = patientHistory;
}

}