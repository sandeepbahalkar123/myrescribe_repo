package com.rescribe.model.dashboard_api.cardviewdatamodel;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocDetail implements Parcelable {

    @SerializedName("docId")
    @Expose
    private int docId;
    @SerializedName("paidStatus")
    @Expose
    private int paidStatus;
    @SerializedName("bookId")
    @Expose
    private String aptId;
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
    private String type;
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
    public final static Parcelable.Creator<DocDetail> CREATOR = new Creator<DocDetail>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DocDetail createFromParcel(Parcel in) {
            return new DocDetail(in);
        }

        public DocDetail[] newArray(int size) {
            return (new DocDetail[size]);
        }

    };

    protected DocDetail(Parcel in) {
        this.docId = ((int) in.readValue((int.class.getClassLoader())));
        this.paidStatus = ((int) in.readValue((int.class.getClassLoader())));
        this.aptId = ((String) in.readValue((String.class.getClassLoader())));
        this.locationId = ((int) in.readValue((int.class.getClassLoader())));
        this.aptDate = ((String) in.readValue((String.class.getClassLoader())));
        this.aptTime = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.tokenNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.waitingPatientCount = ((String) in.readValue((String.class.getClassLoader())));
        this.waitingPatientTime = ((String) in.readValue((String.class.getClassLoader())));
        this.favorite = ((boolean) in.readValue((boolean.class.getClassLoader())));
    }

    public DocDetail() {
    }

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

    public String getAptId() {
        return aptId;
    }

    public void setAptId(String aptId) {
        this.aptId = aptId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(docId);
        dest.writeValue(paidStatus);
        dest.writeValue(aptId);
        dest.writeValue(locationId);
        dest.writeValue(aptDate);
        dest.writeValue(aptTime);
        dest.writeValue(type);
        dest.writeValue(tokenNumber);
        dest.writeValue(waitingPatientCount);
        dest.writeValue(waitingPatientTime);
        dest.writeValue(favorite);
    }

    public int describeContents() {
        return 0;
    }

}