package com.myrescribe.model.visit_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Vital {
    @SerializedName("unitName")
    @Expose
    private String unitName;
    @SerializedName("unitValue")
    @Expose
    private String unitValue;
    @SerializedName("ranges")
    @Expose
    private List<RangeOfVitals> ranges = null;
    @SerializedName("displayName")
    @Expose
    private String displayName;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(String unitValue) {
        this.unitValue = unitValue;
    }

    public List<RangeOfVitals> getRanges() {
        return ranges;
    }

    public void setRanges(List<RangeOfVitals> ranges) {
        this.ranges = ranges;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}