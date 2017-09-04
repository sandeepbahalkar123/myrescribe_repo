
package com.rescribe.model.doctors.appointments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

import java.util.ArrayList;

public class DoctorAppointmentModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private ArrayList<DoctorAppointment> doctorAppointmentList;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public ArrayList<DoctorAppointment> getDoctorAppointmentList() {
        return doctorAppointmentList;
    }

    public void setDoctorAppointmentList(ArrayList<DoctorAppointment> doctorAppointmentList) {
        this.doctorAppointmentList = doctorAppointmentList;
    }

    @Override
    public String toString() {
        return "DoctorAppointmentModel{" +
                "common=" + common +
                ", doctorAppointmentList=" + doctorAppointmentList +
                '}';
    }
}
