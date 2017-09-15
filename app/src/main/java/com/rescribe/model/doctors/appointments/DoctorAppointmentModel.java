
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

    private AppointmentModel appointmentModel;

    public AppointmentModel getAppointmentModel() {
        return appointmentModel;
    }

    public void setAppointmentModel(AppointmentModel appointmentModel) {
        this.appointmentModel = appointmentModel;
    }


    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }


}
