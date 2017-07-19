package com.myrescribe.model.visit_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;

import java.util.List;

/**
 * Created by jeetal on 18/7/17.
 */

public class VitalsList implements CustomResponse{
    @SerializedName("vitals")
    @Expose
    private List<Vital> vitals = null;

    public List<Vital> getVitals() {
        return vitals;
    }

    public void setVitals(List<Vital> vitals) {
        this.vitals = vitals;
    }
}
