package com.rescribe.model.vital_graph.vital_tracker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class VitalGraphTracker implements CustomResponse {
    @SerializedName("vitalName")
    @Expose
    private String vitalName;

    @SerializedName("vitalKey")
    @Expose
    private String vitalKey;

    public String getVitalName() {
        return vitalName;
    }

    public void setVitalName(String vitalName) {
        this.vitalName = vitalName;
    }

    public String getVitalKey() {
        return vitalKey;
    }

    public void setVitalKey(String vitalKey) {
        this.vitalKey = vitalKey;
    }
}