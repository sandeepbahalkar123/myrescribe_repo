
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
    @SerializedName("clinicList")
    @Expose
    private List<DashboardClinicList> clinicList = new ArrayList<DashboardClinicList>();
    @SerializedName("aptDate")
    @Expose
    private String aptDate;
    @SerializedName("aptTime")
    @Expose
    private String aptTime;
    @SerializedName("favorite")
    @Expose
    private String favorite;
    @SerializedName("rating")
    @Expose
    private double rating;
    private int sizeOfList;


    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getSizeOfList() {
        return sizeOfList;
    }

    public void setSizeOfList(int sizeOfList) {
        this.sizeOfList = sizeOfList;
    }



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

    public List<DashboardClinicList> getClinicList() {
        return clinicList;
    }

    public void setClinicList(List<DashboardClinicList> clinicList) {
        this.clinicList = clinicList;
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
