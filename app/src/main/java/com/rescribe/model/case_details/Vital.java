
package com.rescribe.model.case_details;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class Vital implements CustomResponse{

    @SerializedName("unitName")
    @Expose
    private String unitName;
    @SerializedName("unitValue")
    @Expose
    private String unitValue;
    @SerializedName("ranges")
    @Expose
    private List<Range> ranges = null;
    @SerializedName("displayName")
    @Expose
    private String displayName;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @SerializedName("category")
    @Expose

    private String category;

    private String flagName;

    public String getFlagName() {
        return flagName;
    }

    public void setFlagName(String flagName) {
        this.flagName = flagName;
    }

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

    public List<Range> getRanges() {
        return ranges;
    }

    public void setRanges(List<Range> ranges) {
        this.ranges = ranges;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
