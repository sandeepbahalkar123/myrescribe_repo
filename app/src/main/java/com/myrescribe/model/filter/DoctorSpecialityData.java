package com.myrescribe.model.filter;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoctorSpecialityData implements Parcelable {

    public final static Parcelable.Creator<DoctorSpecialityData> CREATOR = new Creator<DoctorSpecialityData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DoctorSpecialityData createFromParcel(Parcel in) {
            DoctorSpecialityData instance = new DoctorSpecialityData();
            instance.id = ((int) in.readValue((int.class.getClassLoader())));
            instance.speciality = ((String) in.readValue((String.class.getClassLoader())));
            instance.selected = ((boolean) in.readValue((boolean.class.getClassLoader())));
            return instance;
        }

        public DoctorSpecialityData[] newArray(int size) {
            return (new DoctorSpecialityData[size]);
        }

    };
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("speciality")
    @Expose
    private String speciality;
    @SerializedName("selected")
    @Expose
    private boolean selected = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(speciality);
        dest.writeValue(selected);
    }

    public int describeContents() {
        return 0;
    }

}