package com.rescribe.model.doctors.appointments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.io.Serializable;

/**
 * Created by riteshpandhurkar on 19/7/17.
 */

public class AptList implements CustomResponse, Serializable{

    @SerializedName("hospital_pat_id")
    @Expose
    private String hospital_pat_id;
    @SerializedName("doc_id")
    @Expose
    private int doc_id;
    @SerializedName("locationId")
    @Expose
    private String locationId;

    @SerializedName("doctorDegree")

    @Expose
    private String doctorDegree;
    @SerializedName("docPhone")
    @Expose
    private String docPhone;
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

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    @SerializedName("city_name")
    @Expose

    private String city_name;

    @SerializedName("area_name")
    @Expose
    private String area_name;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("aptDate")
    @Expose
    private String aptDate = "";
    @SerializedName("aptStatus")
    @Expose
    private String appointmentType;
    @SerializedName("aptTime")
    @Expose
    private String aptTime = "";
    @SerializedName("clinic_name")
    @Expose
    private String clinic_name;

    @SerializedName("rating")
    @Expose
    private double rating;

    public String getClinic_name() {
        return clinic_name;
    }

    public void setClinic_name(String clinic_name) {
        this.clinic_name = clinic_name;
    }

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
        return aptTime.contains("T") ? CommonMethods.formatDateTime(aptTime, RescribeConstants.DATE_PATTERN.YYYY_MM_DD_hh_mm_a, RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DATE) : aptTime;
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
    public String getHospital_pat_id() {
        return hospital_pat_id;
    }

    public void setHospital_pat_id(String hospital_pat_id) {
        this.hospital_pat_id = hospital_pat_id;
    }

    public int getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(int doc_id) {
        this.doc_id = doc_id;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getDoctorDegree() {
        return doctorDegree;
    }

    public void setDoctorDegree(String doctorDegree) {
        this.doctorDegree = doctorDegree;
    }

    public String getDocPhone() {
        return docPhone;
    }

    public void setDocPhone(String docPhone) {
        this.docPhone = docPhone;
    }
    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

}
