package com.rescribe.model.vital_graph;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.model.Common;

public class VitalGraphModel implements Parcelable {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private VitalGraphData data;
    public final static Parcelable.Creator<VitalGraphModel> CREATOR = new Creator<VitalGraphModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public VitalGraphModel createFromParcel(Parcel in) {
            VitalGraphModel instance = new VitalGraphModel();
            instance.common = ((Common) in.readValue((Common.class.getClassLoader())));
            instance.data = ((VitalGraphData) in.readValue((VitalGraphData.class.getClassLoader())));
            return instance;
        }

        public VitalGraphModel[] newArray(int size) {
            return (new VitalGraphModel[size]);
        }

    };

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public VitalGraphData getData() {
        return data;
    }

    public void setData(VitalGraphData data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(data);
    }

    public int describeContents() {
        return 0;
    }

}