package com.rescribe.model.response_model_notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class RequestNotificationModel implements CustomResponse {

    @SerializedName("patientId")
    @Expose
    private Integer patientId;
    @SerializedName("slot")
    @Expose
    private String slot;
    @SerializedName("medicineId")
    @Expose
    private Integer medicineId;
    @SerializedName("takenDate")
    @Expose
    private String takenDate;
    @SerializedName("isBundle")
    @Expose
    private Integer isBundle;
    @SerializedName("isTaken")
    @Expose
    private Integer isTaken;

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public Integer getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }

    public String getTakenDate() {
        return takenDate;
    }

    public void setTakenDate(String takenDate) {
        this.takenDate = takenDate;
    }

    public Integer getIsBundle() {
        return isBundle;
    }

    public void setIsBundle(Integer isBundle) {
        this.isBundle = isBundle;
    }

    public Integer getIsTaken() {
        return isTaken;
    }

    public void setIsTaken(Integer isTaken) {
        this.isTaken = isTaken;
    }
}