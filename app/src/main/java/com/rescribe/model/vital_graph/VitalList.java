package com.rescribe.model.vital_graph;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VitalList implements Parcelable {

    @SerializedName("vitalId")
    @Expose
    private Integer vitalId;
    @SerializedName("vitalName")
    @Expose
    private String vitalName;
    @SerializedName("vitalWeight")
    @Expose
    private String vitalWeight;
    @SerializedName("vitalDate")
    @Expose
    private String vitalDate;
    public final static Parcelable.Creator<VitalList> CREATOR = new Creator<VitalList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public VitalList createFromParcel(Parcel in) {
            VitalList instance = new VitalList();
            instance.vitalId = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.vitalName = ((String) in.readValue((String.class.getClassLoader())));
            instance.vitalWeight = ((String) in.readValue((String.class.getClassLoader())));
            instance.vitalDate = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public VitalList[] newArray(int size) {
            return (new VitalList[size]);
        }

    };

    public Integer getVitalId() {
        return vitalId;
    }

    public void setVitalId(Integer vitalId) {
        this.vitalId = vitalId;
    }

    public String getVitalName() {
        return vitalName;
    }

    public void setVitalName(String vitalName) {
        this.vitalName = vitalName;
    }

    public String getVitalWeight() {
        return vitalWeight;
    }

    public void setVitalWeight(String vitalWeight) {
        this.vitalWeight = vitalWeight;
    }

    public String getVitalDate() {
        return vitalDate;
    }

    public void setVitalDate(String vitalDate) {
        this.vitalDate = vitalDate;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(vitalId);
        dest.writeValue(vitalName);
        dest.writeValue(vitalWeight);
        dest.writeValue(vitalDate);
    }

    public int describeContents() {
        return 0;
    }

}