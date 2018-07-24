package com.rescribe.model.my_records;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.io.Serializable;

/**
 * Created by riteshpandhurkar on 16/6/17.
 */

public class MyRecordDoctorInfo implements CustomResponse, Serializable {

    @SerializedName("docId")
    @Expose
    private String id;
    @SerializedName("doctorName")
    @Expose
    private String doctorName;
    @SerializedName("opdId")
    @Expose
    private String OPDId;
    @SerializedName("opdDate")
    @Expose
    private String date;
    @SerializedName("doctorSpeciality")
    @Expose
    private String specialization;
    @SerializedName("doctorAddress")
    @Expose
    private String address;
    @SerializedName("doctorImageUrl")
    @Expose
    private String docImgURL;
    @SerializedName("areaName")
    @Expose
    private String areaName;
    @SerializedName("cityName")
    @Expose
    private String cityName;

    private Integer bgColor;
    private Integer slideBarColor;

    private boolean isStartElement;
    private boolean isExpanded;

    public Integer getBgColor() {
        return bgColor;
    }

    public void setBgColor(Integer bgColor) {
        this.bgColor = bgColor;
    }

    public Integer getSlideBarColor() {
        return slideBarColor;
    }

    public void setSlideBarColor(Integer slideBarColor) {
        this.slideBarColor = slideBarColor;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        if (date.contains("T")) {
            date = CommonMethods.formatDateTime(date, RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DATE);
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

    public String getDocImgURL() {
        return docImgURL;
    }

    public void setDocImgURL(String docImgURL) {
        this.docImgURL = docImgURL;
    }

    public String getOPDId() {
        return OPDId;
    }

    public void setOPDId(String OPDId) {
        this.OPDId = OPDId;
    }

    @Override
    public String toString() {
        return "DoctorFilteredInfo{" +
                "id='" + id + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", address='" + address + '\'' +
                ", date='" + date + '\'' +
                ", specialization='" + specialization + '\'' +
                ", docImgURL='" + docImgURL + '\'' +
                ", isStartElement=" + isStartElement +
                ", isExpanded=" + isExpanded +
                '}';
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
}
