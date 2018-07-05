package com.rescribe.model.dashboard_api.card_data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocDetail {

    @SerializedName("docId")
    @Expose
    private int docId;
    @SerializedName("paidStatus")
    @Expose
    private int paidStatus;
    @SerializedName("locationId")
    @Expose
    private int locationId;
    @SerializedName("aptDate")
    @Expose
    private String aptDate;
    @SerializedName("aptTime")
    @Expose
    private String aptTime;
    @SerializedName("bookType")
    @Expose
    private String bookType;
    @SerializedName("bookId")
    @Expose
    private String bookId;
    @SerializedName("tokenNumber")
    @Expose
    private String tokenNumber;
    @SerializedName("waitingPatientCount")
    @Expose
    private String waitingPatientCount;
    @SerializedName("waitingPatientTime")
    @Expose
    private String waitingPatientTime;
    @SerializedName("favorite")
    @Expose
    private boolean favorite;

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public int getPaidStatus() {
        return paidStatus;
    }

    public void setPaidStatus(int paidStatus) {
        this.paidStatus = paidStatus;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
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

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public String getWaitingPatientCount() {
        return waitingPatientCount;
    }

    public void setWaitingPatientCount(String waitingPatientCount) {
        this.waitingPatientCount = waitingPatientCount;
    }

    public String getWaitingPatientTime() {
        return waitingPatientTime;
    }

    public void setWaitingPatientTime(String waitingPatientTime) {
        this.waitingPatientTime = waitingPatientTime;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

}