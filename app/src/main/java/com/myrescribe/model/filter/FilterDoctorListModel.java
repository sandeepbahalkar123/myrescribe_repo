package com.myrescribe.model.filter;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.model.Common;

import java.util.ArrayList;

public class FilterDoctorListModel implements Parcelable, CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    public final static Parcelable.Creator<FilterDoctorListModel> CREATOR = new Creator<FilterDoctorListModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public FilterDoctorListModel createFromParcel(Parcel in) {
            FilterDoctorListModel instance = new FilterDoctorListModel();
            instance.common = ((Common) in.readValue((Common.class.getClassLoader())));
            in.readList(instance.data, (DoctorData.class.getClassLoader()));
            return instance;
        }

        public FilterDoctorListModel[] newArray(int size) {
            return (new FilterDoctorListModel[size]);
        }

    };
    @SerializedName("data")
    @Expose
    private ArrayList<DoctorData> data = new ArrayList<>();

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public ArrayList<DoctorData> getData() {
        return data;
    }

    public void setData(ArrayList<DoctorData> data) {
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