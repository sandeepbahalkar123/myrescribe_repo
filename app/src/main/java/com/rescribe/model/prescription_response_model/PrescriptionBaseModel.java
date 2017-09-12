
package com.rescribe.model.prescription_response_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

public class PrescriptionBaseModel implements CustomResponse{

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private PrescriptionData prescriptionData;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public PrescriptionData getPrescriptionData() {
        return prescriptionData;
    }

    public void setPrescriptionData(PrescriptionData prescriptionData) {
        this.prescriptionData = prescriptionData;
    }

}
