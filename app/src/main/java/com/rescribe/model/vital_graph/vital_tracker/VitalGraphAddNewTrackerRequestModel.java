package com.rescribe.model.vital_graph.vital_tracker;

import com.rescribe.interfaces.CustomResponse;

public class VitalGraphAddNewTrackerRequestModel implements CustomResponse {

    private String vitalName;
    private String vitalValue;
    private String patientId;
    private String checkDate;

    public String getVitalName() {
        return vitalName;
    }

    public void setVitalName(String vitalName) {
        this.vitalName = vitalName;
    }

    public String getVitalValue() {
        return vitalValue;
    }

    public void setVitalValue(String vitalValue) {
        this.vitalValue = vitalValue;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }
}