package com.rescribe.model.dashboard_api.doctors;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

public class DoctorListModel implements Parcelable, CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private Data data;
    public final static Creator<DoctorListModel> CREATOR = new Creator<DoctorListModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DoctorListModel createFromParcel(Parcel in) {
            return new DoctorListModel(in);
        }

        public DoctorListModel[] newArray(int size) {
            return (new DoctorListModel[size]);
        }

    };

    protected DoctorListModel(Parcel in) {
        this.common = ((Common) in.readValue((Common.class.getClassLoader())));
        this.data = ((Data) in.readValue((Data.class.getClassLoader())));
    }

    public DoctorListModel() {
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
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