package com.myrescribe.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;

/**
 * Created by jeetal on 27/7/17.
 */

public class AdapterNotificationModel implements CustomResponse{
    @SerializedName("prescriptionDate")
    @Expose
    private String prescriptionDate;
    @SerializedName("medication")
    @Expose
    private SlotModel slotModel;

    public String getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(String prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public SlotModel getMedication() {
        return slotModel;
    }

    public void setMedication(SlotModel slotModel) {
        this.slotModel = slotModel;
    }

}
