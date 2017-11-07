
package com.rescribe.model.dashboard_api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardClinicList {

    @SerializedName("clinicName")
    @Expose
    private String clinicName;
    @SerializedName("clinicAddress")
    @Expose
    private String clinicAddress;
    @SerializedName("locationId")
    @Expose
    private int locationId;
    @SerializedName("amt")
    @Expose
    private int amt;
    @SerializedName("apptScheduleLmtDays")
    @Expose
    private int apptScheduleLmtDays;

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getAmt() {
        return amt;
    }

    public void setAmt(int amt) {
        this.amt = amt;
    }

    public int getApptScheduleLmtDays() {
        return apptScheduleLmtDays;
    }

    public void setApptScheduleLmtDays(int apptScheduleLmtDays) {
        this.apptScheduleLmtDays = apptScheduleLmtDays;
    }

}
