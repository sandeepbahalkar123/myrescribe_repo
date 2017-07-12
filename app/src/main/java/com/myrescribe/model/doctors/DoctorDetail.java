package com.myrescribe.model.doctors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;

/**
 * Created by riteshpandhurkar on 16/6/17.
 */

public class DoctorDetail implements CustomResponse {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("doctorName")
    @Expose
    private String doctorName;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("date")
    @Expose
    private String date;

    private boolean isStartElement;
    private int rowColor;
    private boolean isExpanded;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isStartElement() {
        return isStartElement;
    }

    public void setStartElement(boolean startElement) {
        isStartElement = startElement;
    }

    public int getRowColor() {
        return rowColor;
    }

    public void setRowColor(int rowColor) {
        this.rowColor = rowColor;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    @Override
    public String toString() {
        return "DoctorDetail{" +
                "id='" + id + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", address='" + address + '\'' +
                ", date='" + date + '\'' +
                ", isStartElement=" + isStartElement +
                ", isExpanded=" + isExpanded +
                '}';
    }
}
