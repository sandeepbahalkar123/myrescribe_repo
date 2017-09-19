
package com.rescribe.model.book_appointment.doctor_data;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class DoctorServicesModel implements Parcelable, CustomResponse {

    @SerializedName("doctorList")
    @Expose
    private ArrayList<DoctorList> doctorList = null;
    @SerializedName("doctorSpecialities")
    @Expose
    private ArrayList<DoctorSpeciality> doctorSpecialities = null;
    public final static Creator<DoctorServicesModel> CREATOR = new Creator<DoctorServicesModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DoctorServicesModel createFromParcel(Parcel in) {
            DoctorServicesModel instance = new DoctorServicesModel();
            in.readList(instance.doctorList, (DoctorList.class.getClassLoader()));
            in.readList(instance.doctorSpecialities, (DoctorSpeciality.class.getClassLoader()));
            return instance;
        }

        public DoctorServicesModel[] newArray(int size) {
            return (new DoctorServicesModel[size]);
        }

    };

    public ArrayList<DoctorList> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(ArrayList<DoctorList> doctorList) {
        this.doctorList = doctorList;
    }

    public ArrayList<DoctorSpeciality> getDoctorSpecialities() {
        return doctorSpecialities;
    }

    public void setDoctorSpecialities(ArrayList<DoctorSpeciality> doctorSpecialities) {
        this.doctorSpecialities = doctorSpecialities;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(doctorList);
        dest.writeList(doctorSpecialities);
    }

    public int describeContents() {
        return 0;
    }

}
