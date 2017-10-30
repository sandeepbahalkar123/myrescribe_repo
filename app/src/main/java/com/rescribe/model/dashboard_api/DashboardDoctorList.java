
package com.rescribe.model.dashboard_api;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardDoctorList {

    @SerializedName("docId")
    @Expose
    private int docId;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("categorySpeciality")
    @Expose
    private String categorySpeciality;
    @SerializedName("docName")
    @Expose
    private String docName;
    @SerializedName("doctorImageUrl")
    @Expose
    private String doctorImageUrl;
    @SerializedName("degree")
    @Expose
    private String degree;
    @SerializedName("speciality")
    @Expose
    private String speciality;
    @SerializedName("experience")
    @Expose
    private int experience;
    @SerializedName("doctorAddress")
    @Expose
    private List<String> doctorAddress = new ArrayList<String>();
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("favourite")
    @Expose
    private boolean favourite;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("aptDate")
    @Expose
    private String aptDate;
    @SerializedName("aptTime")
    @Expose
    private String aptTime;

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategorySpeciality() {
        return categorySpeciality;
    }

    public void setCategorySpeciality(String categorySpeciality) {
        this.categorySpeciality = categorySpeciality;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDoctorImageUrl() {
        return doctorImageUrl;
    }

    public void setDoctorImageUrl(String doctorImageUrl) {
        this.doctorImageUrl = doctorImageUrl;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public List<String> getDoctorAddress() {
        return doctorAddress;
    }

    public void setDoctorAddress(List<String> doctorAddress) {
        this.doctorAddress = doctorAddress;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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

}
