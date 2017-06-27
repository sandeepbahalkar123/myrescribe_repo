
package com.myrescribe.model.doctors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.model.Common;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorsModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private HashMap<String, ArrayList<DoctorDetail>> doctorList = null;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public HashMap<String, ArrayList<DoctorDetail>> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(HashMap<String, ArrayList<DoctorDetail>> doctorList) {
        this.doctorList = doctorList;
    }
}
