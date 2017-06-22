package com.myrescribe.model.history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;

import java.util.List;

/**
 * Created by riteshpandhurkar on 15/6/17.
 */

public class PatientHistoryData implements CustomResponse {
    @SerializedName("patientHistory")
    @Expose
    private List<PatientHistoryMain> patientHistoryList = null;

    public List<PatientHistoryMain> getPatientHistoryList() {
        return patientHistoryList;
    }

    public void setPatientHistoryList(List<PatientHistoryMain> patientHistoryList) {
        this.patientHistoryList = patientHistoryList;
    }
}
