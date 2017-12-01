package com.rescribe.model.filter;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

import java.util.ArrayList;

public class DoctorSpecialitiesModel implements Parcelable ,CustomResponse{

    @SerializedName("doctorSpecialities")
    @Expose
    private ArrayList<DoctorSpecialityData> doctorSpecialities = null;
    public final static Parcelable.Creator<DoctorSpecialitiesModel> CREATOR = new Creator<DoctorSpecialitiesModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DoctorSpecialitiesModel createFromParcel(Parcel in) {
            DoctorSpecialitiesModel instance = new DoctorSpecialitiesModel();
            in.readList(instance.doctorSpecialities, (DoctorSpecialityData.class.getClassLoader()));
            return instance;
        }

        public DoctorSpecialitiesModel[] newArray(int size) {
            return (new DoctorSpecialitiesModel[size]);
        }

    };

    public ArrayList<DoctorSpecialityData> getDoctorSpecialities() {
        return doctorSpecialities;
    }

    public void setDoctorSpecialities(ArrayList<DoctorSpecialityData> doctorSpecialities) {
        this.doctorSpecialities = doctorSpecialities;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(doctorSpecialities);
    }

    public int describeContents() {
        return 0;
    }

}