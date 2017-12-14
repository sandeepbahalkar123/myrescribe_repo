package com.rescribe.model.book_appointment.doctor_data.request_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class RequestDoctorListBaseModel implements CustomResponse {

    @SerializedName("cityName")
    @Expose
    private String cityName;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("patientId")
    @Expose
    private Integer patientId;

    @SerializedName("complaint1")
    @Expose
    private String complaint;


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint1) {
        this.complaint = complaint1;
    }

}
