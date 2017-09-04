package com.rescribe.model.filter;

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
            instance.isSelected = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
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
    @SerializedName("is_selected")
    @Expose
    private Boolean isSelected = false;

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

    public Boolean isSelected() {
        return isSelected;
    }

    public void setSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(doctorName);
        dest.writeValue(isSelected);
    }

    public int describeContents() {
        return 0;
    }

}