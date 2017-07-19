
package com.myrescribe.model.doctors.appointments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.model.Common;
import com.myrescribe.model.doctors.doctor_info.DoctorInfoMonthContainer;

public class DoctorAppointmentModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private DoctorAppointment doctorAppointmentList;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public DoctorAppointment getDoctorAppointmentList() {
        return doctorAppointmentList;
    }

    public void setDoctorAppointmentList(DoctorAppointment doctorAppointmentList) {
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
