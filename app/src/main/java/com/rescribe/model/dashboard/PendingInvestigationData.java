
package com.rescribe.model.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PendingInvestigationData {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("investigationName")
    @Expose
    private String investigationName;
    @SerializedName("speciality")
    @Expose
    private String speciality;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvestigationName() {
        return investigationName;
    }

    public void setInvestigationName(String investigationName) {
        this.investigationName = investigationName;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}
