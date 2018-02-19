package com.rescribe.model.token;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FCMData implements Parcelable {

    @SerializedName("identifier")
    @Expose
    private String identifier = "";
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("clinicName")
    @Expose
    private String clinicName;
    @SerializedName("notificationId")
    @Expose
    private Integer notificationId;
    @SerializedName("docId")
    @Expose
    private Integer docId;
    @SerializedName("degree")
    @Expose
    private String degree;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("waitingTime")
    @Expose
    private Integer waitingTime;
    @SerializedName("docSpeciality")
    @Expose
    private String docSpeciality;
    @SerializedName("docName")
    @Expose
    private String docName;
    @SerializedName("profilePicUrl")
    @Expose
    private String profilePicUrl;
    @SerializedName("appointmentTime")
    @Expose
    private String appointmentTime;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("locationId")
    @Expose
    private Integer locationId;
    @SerializedName("favorite")
    @Expose
    private Boolean favorite;

    public final static Parcelable.Creator<FCMData> CREATOR = new Creator<FCMData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public FCMData createFromParcel(Parcel in) {
            return new FCMData(in);
        }

        public FCMData[] newArray(int size) {
            return (new FCMData[size]);
        }

    };

    protected FCMData(Parcel in) {
        this.identifier = ((String) in.readValue((String.class.getClassLoader())));
        this.msg = ((String) in.readValue((String.class.getClassLoader())));
        this.clinicName = ((String) in.readValue((String.class.getClassLoader())));
        this.notificationId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.docId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.degree = ((String) in.readValue((String.class.getClassLoader())));
        this.rating = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.waitingTime = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.docSpeciality = ((String) in.readValue((String.class.getClassLoader())));
        this.docName = ((String) in.readValue((String.class.getClassLoader())));
        this.profilePicUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.appointmentTime = ((String) in.readValue((String.class.getClassLoader())));
        this.price = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.locationId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.favorite = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    public FCMData() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer NotificationId) {
        this.notificationId = NotificationId;
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(Integer waitingTime) {
        this.waitingTime = waitingTime;
    }

    public String getDocSpeciality() {
        return docSpeciality;
    }

    public void setDocSpeciality(String docSpeciality) {
        this.docSpeciality = docSpeciality;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(identifier);
        dest.writeValue(msg);
        dest.writeValue(clinicName);
        dest.writeValue(notificationId);
        dest.writeValue(docId);
        dest.writeValue(degree);
        dest.writeValue(rating);
        dest.writeValue(waitingTime);
        dest.writeValue(docSpeciality);
        dest.writeValue(docName);
        dest.writeValue(profilePicUrl);
        dest.writeValue(appointmentTime);
        dest.writeValue(price);
        dest.writeValue(locationId);
        dest.writeValue(favorite);
    }

    public int describeContents() {
        return 0;
    }

}