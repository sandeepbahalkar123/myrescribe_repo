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
    private String creationDate;
    @SerializedName("selfTrackerFlag")
    @Expose
    private String selfTrackerFlag;

    public String getVitalValue() {
        return vitalValue;
    }

    public void setVitalValue(String vitalValue) {
        this.vitalValue = vitalValue;
    }

    public String getCreationDate() {

        if (creationDate != null) {
            if (creationDate.contains("T")) {
                creationDate = CommonMethods.formatDateTime(creationDate, RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DATE);
            }
        }
        return creationDate;
    }

    public void setCreationDate(String creation_date) {
        this.creationDate = creation_date;
    }

    public String getSelfTrackerFlag() {
        return selfTrackerFlag;
    }

    public void setSelfTrackerFlag(String selfTrackerFlag) {
        this.selfTrackerFlag = selfTrackerFlag;
    }
}