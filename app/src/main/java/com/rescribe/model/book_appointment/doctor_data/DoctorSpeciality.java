
package com.rescribe.model.book_appointment.doctor_data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoctorSpeciality implements Parcelable {

    private int id;
    private String speciality;
    public final static Creator<DoctorSpeciality> CREATOR = new Creator<DoctorSpeciality>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DoctorSpeciality createFromParcel(Parcel in) {
            DoctorSpeciality instance = new DoctorSpeciality();
            instance.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.speciality = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public DoctorSpeciality[] newArray(int size) {
            return (new DoctorSpeciality[size]);
        }

    };

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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(speciality);
    }

    public int describeContents() {
        return 0;
    }

}
