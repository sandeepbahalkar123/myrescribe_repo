package com.myrescribe.model.doctors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

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

    @SerializedName("visitDate")
    @Expose
    private String date;
    @SerializedName("specialization")
    @Expose
    private String specialization;
    private int color;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


    private boolean isStartElement;
    private int rowColor;
    private int sideBarViewColor;
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
        if (date.contains("T")) {
            date = CommonMethods.formatDateTime(date, MyRescribeConstants.DATE_PATTERN.YYYY_MM_DD, MyRescribeConstants.DATE_PATTERN.UTC_PATTERN, MyRescribeConstants.DATE);
        }
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getSideBarViewColor() {
        return sideBarViewColor;
    }

    public void setSideBarViewColor(int sideBarViewColor) {
        this.sideBarViewColor = sideBarViewColor;
    }

    @Override
    public String toString() {
        return "DoctorDetail{" +
                "id='" + id + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", address='" + address + '\'' +
                ", date='" + date + '\'' +
                ", specialization='" + specialization + '\'' +
                ", color=" + color +
                ", isStartElement=" + isStartElement +
                ", rowColor=" + rowColor +
                ", sideBarViewColor=" + sideBarViewColor +
                ", isExpanded=" + isExpanded +
                '}';
    }
}
