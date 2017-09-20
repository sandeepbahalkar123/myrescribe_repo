
package com.rescribe.model.book_appointment.doctor_data;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class DoctorList implements Parcelable {

    @SerializedName("docId")
    @Expose
    private Integer docId;
    @SerializedName("docName")
    @Expose
    private String docName;
    @SerializedName("doctorImageUrl")
    @Expose
    private String doctorImageUrl;
    @SerializedName("speciality")
    @Expose
    private String speciality;
    @SerializedName("experience")
    @Expose
    private int experience;
    @SerializedName("doctorAddress")
    @Expose
    private String doctorAddress;
    @SerializedName("paidStatus")
    @Expose
    private Integer paidStatus;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("recentlyVisited")
    @Expose
    private Boolean recentlyVisited;
    @SerializedName("aboutDoctor")
    @Expose
    private String aboutDoctor;
    @SerializedName("degree")
    @Expose
    private String degree;
    @SerializedName("rating")
    @Expose
    private Float rating;
    @SerializedName("waitingTime")
    @Expose
    private String waitingTime;
    @SerializedName("tokenNo")
    @Expose
    private Integer tokenNo;
    @SerializedName("morePracticePlaces")
    @Expose
    private List<String> morePracticePlaces = null;
    @SerializedName("openToday")
    @Expose
    private String openToday;
    @SerializedName("availableTimeSlots")
    @Expose
    private List<String> availableTimeSlots = null;
    public final static Creator<DoctorList> CREATOR = new Creator<DoctorList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DoctorList createFromParcel(Parcel in) {
            DoctorList instance = new DoctorList();
            instance.docId = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.docName = ((String) in.readValue((String.class.getClassLoader())));
            instance.doctorImageUrl = ((String) in.readValue((String.class.getClassLoader())));
            instance.speciality = ((String) in.readValue((String.class.getClassLoader())));
            instance.experience = ((int) in.readValue((Integer.class.getClassLoader())));
            instance.doctorAddress = ((String) in.readValue((String.class.getClassLoader())));
            instance.paidStatus = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.amount = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.distance = ((String) in.readValue((String.class.getClassLoader())));
            instance.recentlyVisited = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            instance.aboutDoctor = ((String) in.readValue((String.class.getClassLoader())));
            instance.degree = ((String) in.readValue((String.class.getClassLoader())));
            instance.rating = ((Float) in.readValue((Float.class.getClassLoader())));
            instance.waitingTime = ((String) in.readValue((String.class.getClassLoader())));
            instance.tokenNo = ((Integer) in.readValue((Integer.class.getClassLoader())));
            in.readList(instance.morePracticePlaces, (String.class.getClassLoader()));
            instance.openToday = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.availableTimeSlots, (String.class.getClassLoader()));
            return instance;
        }

        public DoctorList[] newArray(int size) {
            return (new DoctorList[size]);
        }

    };

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
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

    public String getDoctorAddress() {
        return doctorAddress;
    }

    public void setDoctorAddress(String doctorAddress) {
        this.doctorAddress = doctorAddress;
    }

    public Integer getPaidStatus() {
        return paidStatus;
    }

    public void setPaidStatus(Integer paidStatus) {
        this.paidStatus = paidStatus;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Boolean getRecentlyVisited() {
        return recentlyVisited;
    }

    public void setRecentlyVisited(Boolean recentlyVisited) {
        this.recentlyVisited = recentlyVisited;
    }

    public String getAboutDoctor() {
        return aboutDoctor;
    }

    public void setAboutDoctor(String aboutDoctor) {
        this.aboutDoctor = aboutDoctor;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(String waitingTime) {
        this.waitingTime = waitingTime;
    }

    public Integer getTokenNo() {
        return tokenNo;
    }

    public void setTokenNo(Integer tokenNo) {
        this.tokenNo = tokenNo;
    }

    public List<String> getMorePracticePlaces() {
        return morePracticePlaces;
    }

    public void setMorePracticePlaces(List<String> morePracticePlaces) {
        this.morePracticePlaces = morePracticePlaces;
    }

    public String getOpenToday() {
        return openToday;
    }

    public void setOpenToday(String openToday) {
        this.openToday = openToday;
    }

    public List<String> getAvailableTimeSlots() {
        return availableTimeSlots;
    }

    public void setAvailableTimeSlots(List<String> availableTimeSlots) {
        this.availableTimeSlots = availableTimeSlots;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(docId);
        dest.writeValue(docName);
        dest.writeValue(doctorImageUrl);
        dest.writeValue(speciality);
        dest.writeValue(experience);
        dest.writeValue(doctorAddress);
        dest.writeValue(paidStatus);
        dest.writeValue(amount);
        dest.writeValue(distance);
        dest.writeValue(recentlyVisited);
        dest.writeValue(aboutDoctor);
        dest.writeValue(degree);
        dest.writeValue(rating);
        dest.writeValue(waitingTime);
        dest.writeValue(tokenNo);
        dest.writeList(morePracticePlaces);
        dest.writeValue(openToday);
        dest.writeList(availableTimeSlots);
    }

    public int describeContents() {
        return 0;
    }

}
