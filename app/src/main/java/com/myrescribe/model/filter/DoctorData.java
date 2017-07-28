package com.myrescribe.model.filter;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoctorData implements Parcelable {

    public final static Parcelable.Creator<DoctorData> CREATOR = new Creator<DoctorData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DoctorData createFromParcel(Parcel in) {
            DoctorData instance = new DoctorData();
            instance.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.doctorName = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public DoctorData[] newArray(int size) {
            return (new DoctorData[size]);
        }

    };
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("doctor_name")
    @Expose
    private String doctorName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(doctorName);
    }

    public int describeContents() {
        return 0;
    }

}