package com.myrescribe.model.records;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.ArrayList;

public class SpinnerDoctor {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("doctorName")
    @Expose
    private String doctorName;
    @SerializedName("specialization")
    @Expose
    private String specialization;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("visitDate")
    @Expose
    private String visitDate;
    @SerializedName("docImg")
    @Expose
    private String docImg;
    @SerializedName("dates")
    @Expose
    private ArrayList<String> dates = new ArrayList<String>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getDocImg() {
        return docImg;
    }

    public void setDocImg(String docImg) {
        this.docImg = docImg;
    }

    public ArrayList<String> getDates() {
        return dates;
    }

    public void setDates(ArrayList<String> dates) {
        this.dates = dates;
    }

}