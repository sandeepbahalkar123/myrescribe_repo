package com.rescribe.model.book_appointment.request_appointment_confirmation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class RequestCancelGetToken implements CustomResponse {

    @SerializedName("docId")
    @Expose
    private Integer docId;
    @SerializedName("locationId")
    @Expose
    private Integer locationId;

    @SerializedName("patientId")
    @Expose
    private Integer patientId;

    @SerializedName("tokenNumber")
    @Expose
    private Integer tokenNumber;

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(Integer tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

}
