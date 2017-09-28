
package com.rescribe.model.dashboard;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardDataModel {

    @SerializedName("servicesList")
    @Expose
    private List<String> servicesList = null;
    @SerializedName("doctorList")
    @Expose
    private List<DoctorData> doctorList = null;
    @SerializedName("pendingInvestigationList")
    @Expose
    private List<PendingInvestigationData> pendingInvestigationList = null;
    @SerializedName("latestVitalReading")
    @Expose
    private LatestVitalReading latestVitalReading;
    @SerializedName("healthOffersList")
    @Expose
    private List<HealthOffersData> healthOffersList = null;
    @SerializedName("healthBlogList")
    @Expose
    private List<HealthBlogData> healthBlogList = null;
    @SerializedName("tipOfTheDayImageUrl")
    @Expose
    private String tipOfTheDayImageUrl;
    @SerializedName("jokeOfTheDayImageUrl")
    @Expose
    private String jokeOfTheDayImageUrl;

    public List<String> getServicesList() {
        return servicesList;
    }

    public void setServicesList(List<String> servicesList) {
        this.servicesList = servicesList;
    }

    public List<DoctorData> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<DoctorData> doctorList) {
        this.doctorList = doctorList;
    }

    public List<PendingInvestigationData> getPendingInvestigationList() {
        return pendingInvestigationList;
    }

    public void setPendingInvestigationList(List<PendingInvestigationData> pendingInvestigationList) {
        this.pendingInvestigationList = pendingInvestigationList;
    }

    public LatestVitalReading getLatestVitalReading() {
        return latestVitalReading;
    }

    public void setLatestVitalReading(LatestVitalReading latestVitalReading) {
        this.latestVitalReading = latestVitalReading;
    }

    public List<HealthOffersData> getHealthOffersList() {
        return healthOffersList;
    }

    public void setHealthOffersList(List<HealthOffersData> healthOffersList) {
        this.healthOffersList = healthOffersList;
    }

    public List<HealthBlogData> getHealthBlogList() {
        return healthBlogList;
    }

    public void setHealthBlogList(List<HealthBlogData> healthBlogList) {
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
