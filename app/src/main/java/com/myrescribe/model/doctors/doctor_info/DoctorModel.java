
package com.myrescribe.model.doctors.doctor_info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.model.Common;

public class DoctorModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private DoctorInfoMonthContainer doctorInfoMonthContainer;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public DoctorInfoMonthContainer getDoctorInfoMonthContainer() {
        return doctorInfoMonthContainer;
    }

    public void setDoctorInfoMonthContainer(DoctorInfoMonthContainer doctorInfoMonthContainer) {
        this.doctorInfoMonthContainer = doctorInfoMonthContainer;
    }

    @Override
    public String toString() {
        return "DoctorModel{" +
                "common=" + common +
                ", doctorInfoMonthContainer=" + doctorInfoMonthContainer +
                '}';
    }
}
