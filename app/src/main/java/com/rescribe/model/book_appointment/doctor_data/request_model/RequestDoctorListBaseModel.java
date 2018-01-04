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

    @SerializedName("complaint")
    @Expose
    private String complaint;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("time")
    @Expose
    private String time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

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
