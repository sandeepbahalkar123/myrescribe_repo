package com.rescribe.model.book_appointment.request_appointment_confirmation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;
import com.rescribe.model.doctors.appointments.AptList;

public class ResponseAppointmentConfirmationModel implements CustomResponse{

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private AptList aptList;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public AptList getAptList() {
        return aptList;
    }

    public void setAptList(AptList aptList) {
        this.aptList = aptList;
    }

}