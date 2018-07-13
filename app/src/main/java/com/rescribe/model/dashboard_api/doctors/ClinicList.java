package com.rescribe.model.dashboard_api.doctors;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ClinicList implements Parcelable {

    @SerializedName("clinicName")
    @Expose
    private String clinicName;
    @SerializedName("clinicAddress")
    @Expose
    private String clinicAddress;
    @SerializedName("areaName")
    @Expose
    private String areaName;
    @SerializedName("cityName")
    @Expose
    private String cityName;
    @SerializedName("locationLat")
    @Expose
    private String locationLat;
    @SerializedName("locationLong")
    @Expose
    private String locationLong;
    @SerializedName("amount")
    @Expose
    private int amount;
    @SerializedName("apptScheduleLmtDays")
    @Expose
    private int apptScheduleLmtDays;
    @SerializedName("locationId")
    @Expose
    private int locationId;
    @SerializedName("appointmentType")
    @Expose
    private String appointmentType;
    @SerializedName("services")
    @Expose
    private List<String> services = new ArrayList<String>();
    public final static Creator<ClinicList> CREATOR = new Creator<ClinicList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ClinicList createFromParcel(Parcel in) {
            return new ClinicList(in);
        }

        public ClinicList[] newArray(int size) {
            return (new ClinicList[size]);
        }

    };

    protected ClinicList(Parcel in) {
        this.clinicName = ((String) in.readValue((String.class.getClassLoader())));
        this.clinicAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.areaName = ((String) in.readValue((String.class.getClassLoader())));
        this.cityName = ((String) in.readValue((String.class.getClassLoader())));
        this.locationLat = ((String) in.readValue((String.class.getClassLoader())));
        this.locationLong = ((String) in.readValue((String.class.getClassLoader())));
        this.amount = ((int) in.readValue((int.class.getClassLoader())));
        this.apptScheduleLmtDays = ((int) in.readValue((int.class.getClassLoader())));
        this.locationId = ((int) in.readValue((int.class.getClassLoader())));
        this.appointmentType = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.services, (String.class.getClassLoader()));
    }

    public ClinicList() {
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

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(String locationLat) {
        this.locationLat = locationLat;
    }

    public String getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(String locationLong) {
        this.locationLong = locationLong;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getApptScheduleLmtDays() {
        return apptScheduleLmtDays;
    }

    public void setApptScheduleLmtDays(int apptScheduleLmtDays) {
        this.apptScheduleLmtDays = apptScheduleLmtDays;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(clinicName);
        dest.writeValue(clinicAddress);
        dest.writeValue(areaName);
        dest.writeValue(cityName);
        dest.writeValue(locationLat);
        dest.writeValue(locationLong);
        dest.writeValue(amount);
        dest.writeValue(apptScheduleLmtDays);
        dest.writeValue(locationId);
        dest.writeValue(appointmentType);
        dest.writeList(services);
    }

    public int describeContents() {
        return 0;
    }

}