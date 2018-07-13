package com.rescribe.model.dashboard_api.doctors;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Data implements Parcelable {

    @SerializedName("doctorList")
    @Expose
    private List<DoctorList> doctorList = new ArrayList<DoctorList>();
    public final static Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    };

    protected Data(Parcel in) {
        in.readList(this.doctorList, (com.rescribe.model.dashboard_api.doctors.DoctorList.class.getClassLoader()));
    }

    public Data() {
    }

    public List<DoctorList> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<DoctorList> doctorList) {
        this.doctorList = doctorList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(doctorList);
    }

    public int describeContents() {
        return 0;
    }

}