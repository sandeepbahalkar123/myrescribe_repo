
package com.rescribe.model.doctors.doctor_info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

public class DoctorBaseModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private DoctorDataModel doctorDataModel;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public DoctorDataModel getDoctorDataModel() {
        return doctorDataModel;
    }

    public void setDoctorDataModel(DoctorDataModel doctorDataModel) {
        this.doctorDataModel = doctorDataModel;
    }

    @Override
    public String toString() {
        return "DoctorBaseModel{" +
                "common=" + common +
                ", doctorDataModel=" + doctorDataModel +
                '}';
    }
}
