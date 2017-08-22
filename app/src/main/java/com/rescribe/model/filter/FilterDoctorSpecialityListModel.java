package com.rescribe.model.filter;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

import java.util.ArrayList;

public class FilterDoctorSpecialityListModel implements Parcelable, CustomResponse {

    public final static Parcelable.Creator<FilterDoctorSpecialityListModel> CREATOR = new Creator<FilterDoctorSpecialityListModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public FilterDoctorSpecialityListModel createFromParcel(Parcel in) {
            FilterDoctorSpecialityListModel instance = new FilterDoctorSpecialityListModel();
            instance.common = ((Common) in.readValue((Common.class.getClassLoader())));
            in.readList(instance.doctorSpecialityData, (DoctorSpecialityData.class.getClassLoader()));
            return instance;
        }

        public FilterDoctorSpecialityListModel[] newArray(int size) {
            return (new FilterDoctorSpecialityListModel[size]);
        }

    };
    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private ArrayList<DoctorSpecialityData> doctorSpecialityData = new ArrayList<>();

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public ArrayList<DoctorSpecialityData> getDoctorSpecialityData() {
        return doctorSpecialityData;
    }

    public void setDoctorSpecialityData(ArrayList<DoctorSpecialityData> doctorSpecialityData) {
        this.doctorSpecialityData = doctorSpecialityData;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeList(doctorSpecialityData);
    }

    public int describeContents() {
        return 0;
    }

}