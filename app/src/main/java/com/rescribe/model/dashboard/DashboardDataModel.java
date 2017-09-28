
package com.rescribe.model.dashboard;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardDataModel {

    @SerializedName("servicesList")
    @Expose
    private ArrayList<String> servicesList = null;
    @SerializedName("doctorList")
    @Expose
    private ArrayList<DoctorData> doctorList = null;
    @SerializedName("pendingInvestigationList")
    @Expose
    private ArrayList<PendingInvestigationData> pendingInvestigationList = null;
    @SerializedName("latestVitalReading")
    @Expose
    private LatestVitalReading latestVitalReading;
    @SerializedName("healthOffersList")
    @Expose
    private ArrayList<HealthOffersData> healthOffersList = null;
    @SerializedName("healthBlogList")
    @Expose
    private ArrayList<HealthBlogData> healthBlogList = null;
    @SerializedName("tipOfTheDayImageUrl")
    @Expose
    private String tipOfTheDayImageUrl;
    @SerializedName("jokeOfTheDayImageUrl")
    @Expose
    private String jokeOfTheDayImageUrl;

    public ArrayList<String> getServicesList() {
        return servicesList;
    }

    public void setServicesList(ArrayList<String> servicesList) {
        this.servicesList = servicesList;
    }

    public ArrayList<DoctorData> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(ArrayList<DoctorData> doctorList) {
        this.doctorList = doctorList;
    }

    public ArrayList<PendingInvestigationData> getPendingInvestigationList() {
        return pendingInvestigationList;
    }

    public void setPendingInvestigationList(ArrayList<PendingInvestigationData> pendingInvestigationList) {
        this.pendingInvestigationList = pendingInvestigationList;
    }

    public LatestVitalReading getLatestVitalReading() {
        return latestVitalReading;
    }

    public void setLatestVitalReading(LatestVitalReading latestVitalReading) {
        this.latestVitalReading = latestVitalReading;
    }

    public ArrayList<HealthOffersData> getHealthOffersList() {
        return healthOffersList;
    }

    public void setHealthOffersList(ArrayList<HealthOffersData> healthOffersList) {
        this.healthOffersList = healthOffersList;
    }

    public ArrayList<HealthBlogData> getHealthBlogList() {
        return healthBlogList;
    }

    public void setHealthBlogList(ArrayList<HealthBlogData> healthBlogList) {
        this.healthBlogList = healthBlogList;
    }

    public String getTipOfTheDayImageUrl() {
        return tipOfTheDayImageUrl;
    }

    public void setTipOfTheDayImageUrl(String tipOfTheDayImageUrl) {
        this.tipOfTheDayImageUrl = tipOfTheDayImageUrl;
    }

    public String getJokeOfTheDayImageUrl() {
        return jokeOfTheDayImageUrl;
    }

    public void setJokeOfTheDayImageUrl(String jokeOfTheDayImageUrl) {
        this.jokeOfTheDayImageUrl = jokeOfTheDayImageUrl;
    }

}
