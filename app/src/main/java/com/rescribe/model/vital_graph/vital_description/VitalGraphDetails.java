package com.rescribe.model.vital_graph.vital_description;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

public class VitalGraphDetails implements CustomResponse {
    @SerializedName("vitalValue")
    @Expose
    private String vitalValue;
    @SerializedName("creation_date")
    @Expose
    private String creation_date;
    @SerializedName("selfTrackerFlag")
    @Expose
    private String selfTrackerFlag;

    public String getVitalValue() {
        return vitalValue;
    }

    public void setVitalValue(String vitalValue) {
        this.vitalValue = vitalValue;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getSelfTrackerFlag() {
        return selfTrackerFlag;
    }

    public void setSelfTrackerFlag(String selfTrackerFlag) {
        this.selfTrackerFlag = selfTrackerFlag;
    }
}