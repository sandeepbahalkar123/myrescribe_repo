package com.myrescribe.model.doctors.appointments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

/**
 * Created by riteshpandhurkar on 19/7/17.
 */

public class DoctorAppointment implements CustomResponse{
    @SerializedName("aptId")
    @Expose
    private String id;
    @SerializedName("doctorName")
    @Expose
    private String doctorName;

    @SerializedName("specialization")
    @Expose
    private String specialization;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("aptDate")
    @Expose
    private String aptDate;
    @SerializedName("aptStatus")
    @Expose
    private String appointmentType;
    @SerializedName("aptTime")
    @Expose
    private String aptTime;

    public String getAptTime() {
        return aptTime;
    }

    public void setAptTime(String aptTime) {
        this.aptTime = aptTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
     //the time which is coming in aptTime is time of appointment
    public String getAptDate() {
        if (aptDate.contains("T")) {
            String date[] = aptDate.split("T");
            String dateBeforeTime = date[0];
            aptDate = CommonMethods.formatDateTime(dateBeforeTime+"T"+aptTime+".000Z", MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD_hh_mm_a, MyRescribeConstants.DATE_PATTERN.UTC_PATTERN, MyRescribeConstants.DATE);
        }
        return aptDate;
    }

    public void setAptDate(String aptDate) {
        this.aptDate = aptDate;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }
}
