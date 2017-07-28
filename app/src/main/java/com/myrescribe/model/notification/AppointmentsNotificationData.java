package com.myrescribe.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentsNotificationData {

    @SerializedName("aptId")
    @Expose
    private int aptId;
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
    @SerializedName("aptTime")
    @Expose
    private String aptTime;
    @SerializedName("aptStatus")
    @Expose
    private String aptStatus;

    public int getAptId() {
        return aptId;
    }

    public void setAptId(int aptId) {
        this.aptId = aptId;
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

    public String getAptDate() {
        return aptDate;
    }

    public void setAptDate(String aptDate) {
        this.aptDate = aptDate;
    }

    public String getAptTime() {
        return aptTime;
    }

    public void setAptTime(String aptTime) {
        this.aptTime = aptTime;
    }

    public String getAptStatus() {
        return aptStatus;
    }

    public void setAptStatus(String aptStatus) {
        this.aptStatus = aptStatus;
    }

}