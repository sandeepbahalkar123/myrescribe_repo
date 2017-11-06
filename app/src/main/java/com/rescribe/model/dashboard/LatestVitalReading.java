
package com.rescribe.model.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

public class LatestVitalReading {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("vitalName")
    @Expose
    private String vitalName;
    @SerializedName("vitalValue")
    @Expose
    private String vitalValue;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("date")
    @Expose
    private String date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVitalName() {
        return vitalName;
    }

    public void setVitalName(String vitalName) {
        this.vitalName = vitalName;
    }

    public String getVitalValue() {
        return vitalValue;
    }

    public void setVitalValue(String vitalValue) {
        this.vitalValue = vitalValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDate() {
        if (date != null) {
            if (date.contains("T")) {
                date = CommonMethods.formatDateTime(date, RescribeConstants.DATE_PATTERN.YYYY_MM_DD_HH_mm_ss, RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DATE);
            }
        }
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
