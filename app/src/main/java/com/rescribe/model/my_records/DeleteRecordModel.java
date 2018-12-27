package com.rescribe.model.my_records;

import com.rescribe.interfaces.CustomResponse;

import java.util.ArrayList;

public class DeleteRecordModel implements CustomResponse {

        String patientId;
        ArrayList<DeleteRecordDetailsModel> recordDetails;


    public DeleteRecordModel(String patientId, ArrayList<DeleteRecordDetailsModel> recordDetails) {
        this.patientId = patientId;
        this.recordDetails = recordDetails;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public ArrayList<DeleteRecordDetailsModel> getRecordDetails() {
        return recordDetails;
    }

    public void setRecordDetails(ArrayList<DeleteRecordDetailsModel> recordDetails) {
        this.recordDetails = recordDetails;
    }




}
