
package com.rescribe.model.dashboard_api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardClinicList implements Parcelable
{

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
    @SerializedName("amount")
    @Expose
    private int amount;

    public final static Creator<DashboardClinicList> CREATOR = new Creator<DashboardClinicList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DashboardClinicList createFromParcel(Parcel in) {
            return new DashboardClinicList(in);
        }

        public DashboardClinicList[] newArray(int size) {
            return (new DashboardClinicList[size]);
        }

    };

    protected DashboardClinicList(Parcel in) {
        this.clinicName = ((String) in.readValue((String.class.getClassLoader())));
        this.clinicAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.locationId = ((int) in.readValue((int.class.getClassLoader())));
        this.amt = ((int) in.readValue((int.class.getClassLoader())));
        this.apptScheduleLmtDays = ((int) in.readValue((int.class.getClassLoader())));
        this.amount = ((int) in.readValue((int.class.getClassLoader())));
    }

    public DashboardClinicList() {
    }

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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(clinicName);
        dest.writeValue(clinicAddress);
        dest.writeValue(locationId);
        dest.writeValue(amt);
        dest.writeValue(apptScheduleLmtDays);
        dest.writeValue(amount);
    }

    public int describeContents() {
        return  0;
    }

}
