
package com.rescribe.model.prescription_response_model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class PrescriptionData implements CustomResponse{

    @SerializedName("prescriptions")
    @Expose
    private List<PrescriptionModel> prescriptionModels = null;

    public List<PrescriptionModel> getPrescriptionModels() {
        return prescriptionModels;
    }

    public void setPrescriptionModels(List<PrescriptionModel> prescriptionModels) {
        this.prescriptionModels = prescriptionModels;
    }

}
