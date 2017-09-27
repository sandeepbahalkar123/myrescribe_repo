package com.rescribe.model.vital_graph.vital_all_list;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

public class VitalGraphBaseModel implements Parcelable, CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private VitalGraphList data;
    public final static Parcelable.Creator<VitalGraphBaseModel> CREATOR = new Creator<VitalGraphBaseModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public VitalGraphBaseModel createFromParcel(Parcel in) {
            VitalGraphBaseModel instance = new VitalGraphBaseModel();
            instance.common = ((Common) in.readValue((Common.class.getClassLoader())));
            instance.data = ((VitalGraphList) in.readValue((VitalGraphList.class.getClassLoader())));
            return instance;
        }

        public VitalGraphBaseModel[] newArray(int size) {
            return (new VitalGraphBaseModel[size]);
        }

    };

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public VitalGraphList getData() {
        return data;
    }

    public void setData(VitalGraphList data) {
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