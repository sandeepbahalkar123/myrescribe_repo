
package com.myrescribe.model.doctors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.model.Common;

public class DoctorsModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private DoctorListMain doctorListMainInstance;

    public Common getCommon() {
        return common;
    }

    public DoctorListMain getDoctorListMainInstance() {
        return doctorListMainInstance;
    }

    public void setDoctorListMainInstance(DoctorListMain doctorListMainInstance) {
        this.doctorListMainInstance = doctorListMainInstance;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

}
