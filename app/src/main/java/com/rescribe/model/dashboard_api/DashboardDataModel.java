
package com.rescribe.model.dashboard_api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;

import java.util.ArrayList;

public class DashboardDataModel implements Parcelable {

    @SerializedName("doctorList")
    @Expose
    private ArrayList<DoctorList> doctorList = new ArrayList<DoctorList>();
    private int index;

    public final static Creator<DashboardDataModel> CREATOR = new Creator<DashboardDataModel>() {
        @SuppressWarnings({
                "unchecked"
        })
        public DashboardDataModel createFromParcel(Parcel in) {
            return new DashboardDataModel(in);
        }

        public DashboardDataModel[] newArray(int size) {
            return (new DashboardDataModel[size]);
        }

    };

    protected DashboardDataModel(Parcel in) {
        in.readList(this.doctorList, (DoctorList.class.getClassLoader()));
    }

    public DashboardDataModel() {
    }

    public ArrayList<DoctorList> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(ArrayList<DoctorList> doctorList) {
        this.doctorList = doctorList;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(doctorList);
    }

    public int describeContents() {
        return 0;
    }

}
