
package com.rescribe.model.book_appointment.doctor_data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ClinicData implements Parcelable {

    @SerializedName("amount")
    @Expose
    private int amount;
    @SerializedName("clinicName")
    @Expose
    private String clinicName = "";
    @SerializedName("clinicAddress")
    @Expose
    private String clinicAddress = "";
    @SerializedName("amt")
    @Expose
    private int amt;
    @SerializedName("apptScheduleLmtDays")
    @Expose
    private int apptScheduleLmtDays;
    @SerializedName("locationId")
    @Expose
    private int locationId;
    @SerializedName("services")
    @Expose
    private ArrayList<String> docServices = new ArrayList<>();
    // not using right now
    private String tokenNo = "";
    @SerializedName("appointmentType")
    @Expose
    private String appointmentType = "";
    @SerializedName("areaName")
    @Expose
    private String areaName = "";
    @SerializedName("cityName")
    @Expose
    private String cityName = "";

    @SerializedName("locationLat")
    @Expose
    private String locationLat;
    @SerializedName("locationLong")
    @Expose
    private String locationLong;

    public final static Creator<ClinicData> CREATOR = new Creator<ClinicData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ClinicData createFromParcel(Parcel in) {
            return new ClinicData(in);
        }

        public ClinicData[] newArray(int size) {
            return (new ClinicData[size]);
        }

    };

    protected ClinicData(Parcel in) {
        ClinicData instance = new ClinicData();
        this.clinicName = ((String) in.readValue((String.class.getClassLoader())));
        this.clinicAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.locationId = ((int) in.readValue((int.class.getClassLoader())));
        this.amt = ((int) in.readValue((int.class.getClassLoader())));
        this.apptScheduleLmtDays = ((int) in.readValue((int.class.getClassLoader())));
        this.amount = ((int) in.readValue((int.class.getClassLoader())));
        this.appointmentType = ((String) in.readValue((String.class.getClassLoader())));

        in.readList(instance.docServices, (String.class.getClassLoader()));
        this.areaName = ((String) in.readValue((String.class.getClassLoader())));
        this.cityName = ((String) in.readValue((String.class.getClassLoader())));

        this.locationLat = ((String) in.readValue((String.class.getClassLoader())));
        this.locationLong = ((String) in.readValue((String.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(clinicName);
        dest.writeValue(clinicAddress);
        dest.writeValue(locationId);
        dest.writeValue(amt);
        dest.writeValue(apptScheduleLmtDays);
        dest.writeValue(amount);
        dest.writeValue(appointmentType);
        dest.writeList(docServices);
        dest.writeValue(areaName);
        dest.writeValue(cityName);

        dest.writeValue(locationLat);
        dest.writeValue(locationLong);
    }


    public ClinicData() {
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

    public ArrayList<String> getDocServices() {
        return docServices;
    }

    public void setDocServices(ArrayList<String> docServices) {
        this.docServices = docServices;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
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

    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return clinicName + ',' + clinicAddress;
    }
}
