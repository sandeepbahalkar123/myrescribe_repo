package com.rescribe.model.filter;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class DoctorNameModel implements Parcelable, CustomResponse {

    @SerializedName("doctorNames")
    @Expose
    private ArrayList<DoctorData> doctorNames = null;
    public final static Parcelable.Creator<DoctorNameModel> CREATOR = new Creator<DoctorNameModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DoctorNameModel createFromParcel(Parcel in) {
            DoctorNameModel instance = new DoctorNameModel();
            in.readList(instance.doctorNames, (DoctorData.class.getClassLoader()));
            return instance;
        }

        public DoctorNameModel[] newArray(int size) {
            return (new DoctorNameModel[size]);
        }

    };

    public ArrayList<DoctorData> getDoctorNames() {
        return doctorNames;
    }

    public void setDoctorNames(ArrayList<DoctorData> doctorNames) {
        this.doctorNames = doctorNames;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(doctorNames);
    }

    public int describeContents() {
        return 0;
    }

}