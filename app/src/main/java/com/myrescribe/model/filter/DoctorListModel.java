package com.myrescribe.model.filter;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.model.Common;

import java.util.List;

public class DoctorListModel implements Parcelable {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private List<DoctorData> data = null;
    public final static Parcelable.Creator<DoctorListModel> CREATOR = new Creator<DoctorListModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DoctorListModel createFromParcel(Parcel in) {
            DoctorListModel instance = new DoctorListModel();
            instance.common = ((Common) in.readValue((Common.class.getClassLoader())));
            in.readList(instance.data, (DoctorData.class.getClassLoader()));
            return instance;
        }

        public DoctorListModel[] newArray(int size) {
            return (new DoctorListModel[size]);
        }

    };

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public List<DoctorData> getData() {
        return data;
    }

    public void setData(List<DoctorData> data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeList(data);
    }

    public int describeContents() {
        return 0;
    }

}