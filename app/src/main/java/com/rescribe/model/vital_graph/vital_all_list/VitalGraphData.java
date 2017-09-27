package com.rescribe.model.vital_graph.vital_all_list;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

public class VitalGraphData implements Parcelable {
    @SerializedName("vitalName")
    @Expose
    private String vitalName;
    @SerializedName("vitalValue")
    @Expose
    private String vitalValue;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("date")
    @Expose
    private String vitalDate;
    public final static Parcelable.Creator<VitalGraphData> CREATOR = new Creator<VitalGraphData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public VitalGraphData createFromParcel(Parcel in) {
            VitalGraphData instance = new VitalGraphData();
            instance.vitalName = ((String) in.readValue((String.class.getClassLoader())));
            instance.vitalValue = ((String) in.readValue((String.class.getClassLoader())));
            instance.category = ((String) in.readValue((String.class.getClassLoader())));
            instance.vitalDate = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public VitalGraphData[] newArray(int size) {
            return (new VitalGraphData[size]);
        }

    };

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVitalDate() {
        if (vitalDate != null) {
            if (vitalDate.contains("T")) {
                vitalDate = CommonMethods.formatDateTime(vitalDate, RescribeConstants.DATE_PATTERN.YYYY_MM_DD_hh_mm_ss, RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DATE);
            }
        }
        return vitalDate;
    }

    public void setVitalDate(String vitalDate) {
        this.vitalDate = vitalDate;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(getVitalName());
        dest.writeValue(getVitalValue());
        dest.writeValue(getCategory());
        dest.writeValue(getVitalDate());
    }

    public int describeContents() {
        return 0;
    }

}