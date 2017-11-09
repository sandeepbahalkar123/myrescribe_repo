
package com.rescribe.model.book_appointment.doctor_data;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


//TODO : RESPONSE JSON IS GOING TO CHANGE, ONCE RESPONSE FINAL REMOVE UNWANTED VARIABLES.
public class DoctorList implements Parcelable {

    @SerializedName("docId")
    @Expose
    private int docId;
    @SerializedName("doc_location_id")
    @Expose
    private int docLocationId;
    @SerializedName("location_id")
    @Expose
    private int locationId;
    @SerializedName("docName")
    @Expose
    private String docName = "";
    @SerializedName("categoryName")
    @Expose
    private String categoryName = "";
    @SerializedName("categorySpeciality")
    @Expose
    private String categorySpeciality = "";

    @SerializedName("doctorImageUrl")
    @Expose
    private String doctorImageUrl = "";
    @SerializedName("speciality")
    @Expose
    private String docSpeciality = "";
    @SerializedName("experience")
    @Expose
    private int experience;

    @SerializedName("clinicList")
    @Expose
    private ArrayList<ClinicData> clinicDataList = new ArrayList<>();

    @SerializedName("favourite")
    @Expose
    private boolean favourite;
    @SerializedName("degree")
    @Expose
    private String degree = "";

    @SerializedName("rating")
    @Expose
    private double rating;

    @SerializedName("waitingTime")
    @Expose
    private String waitingTime = "";
    @SerializedName("tokenNo")
    @Expose
    private String tokenNo;
    @SerializedName("aboutDoctor")
    @Expose
    private String aboutDoctor = "";

    //------
    @SerializedName("docServices")
    @Expose
    private ArrayList<String> docServices = new ArrayList<>();
    @SerializedName("aptDate")
    @Expose
    private String aptDate = "";
    @SerializedName("aptTime")
    @Expose
    private String aptTime = "";
    //------
    private int sizeOfList = 0;
    private double latitude = 0.0;
    private double longitude = 0.0;

    //this parameters are used to sort list by clinicName and doctorName as per functionality
    private String nameOfClinicString = "";
    private String addressOfDoctorString = "";

    /*  @SerializedName("reviewList")
    @Expose
    private ArrayList<ReviewList> reviewList = null;*/

    public final static Creator<DoctorList> CREATOR = new Creator<DoctorList>() {

        @SuppressWarnings({
                "unchecked"
        })
        public DoctorList createFromParcel(Parcel in) {
            DoctorList instance = new DoctorList();
            instance.docId = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.docLocationId = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.locationId = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.docName = ((String) in.readValue((String.class.getClassLoader())));
            instance.categoryName = ((String) in.readValue((String.class.getClassLoader())));
            instance.categorySpeciality = ((String) in.readValue((String.class.getClassLoader())));
            instance.doctorImageUrl = ((String) in.readValue((String.class.getClassLoader())));
            instance.docSpeciality = ((String) in.readValue((String.class.getClassLoader())));
            instance.experience = ((Integer) in.readValue((Integer.class.getClassLoader())));
            in.readList(instance.clinicDataList, (ClinicData.class.getClassLoader()));
            instance.favourite = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            instance.degree = ((String) in.readValue((String.class.getClassLoader())));
            instance.rating = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.waitingTime = ((String) in.readValue((String.class.getClassLoader())));
            instance.tokenNo = ((String) in.readValue((String.class.getClassLoader())));
            instance.aboutDoctor = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.docServices, (String.class.getClassLoader()));
            instance.aptDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.aptTime = ((String) in.readValue((String.class.getClassLoader())));
            instance.sizeOfList = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.addressOfDoctorString = ((String) in.readValue((String.class.getClassLoader())));
            instance.nameOfClinicString = ((String) in.readValue((String.class.getClassLoader())));
            //in.readList(instance.reviewList, (ReviewList.class.getClassLoader()));
            return instance;
        }

        public DoctorList[] newArray(int size) {
            return (new DoctorList[size]);
        }

    };

    public String getNameOfClinicString() {
        return nameOfClinicString;
    }

    public void setNameOfClinicString(String nameOfClinicString) {
        this.nameOfClinicString = nameOfClinicString;
    }

    public String getAddressOfDoctorString() {
        return addressOfDoctorString;
    }

    public void setAddressOfDoctorString(String addressOfDoctorString) {
        this.addressOfDoctorString = addressOfDoctorString;
    }

    public int getSizeOfList() {
        return sizeOfList;
    }

    public void setSizeOfList(int sizeOfList) {
        this.sizeOfList = sizeOfList;
    }

    public boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
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

    public String getDocSpeciality() {
        return docSpeciality;
    }

    public void setDocSpeciality(String speciality) {
        this.docSpeciality = speciality;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(String waitingTime) {
        this.waitingTime = waitingTime;
    }

    public String getTokenNo() {
        return tokenNo;
    }

    public void setTokenNo(String tokenNo) {
        this.tokenNo = tokenNo;
    }

    public ArrayList<ClinicData> getClinicDataList() {
        return clinicDataList;
    }

    public void setClinicDataList(ArrayList<ClinicData> clinicDataList) {
        this.clinicDataList = clinicDataList;
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

    public ArrayList<String> getDocServices() {
        return docServices;
    }

    public void setDocServices(ArrayList<String> docServices) {
        this.docServices = docServices;
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
/* public ArrayList<ReviewList> getReviewList() {
        return reviewList;
    }

    public void setReviewList(ArrayList<ReviewList> reviewList) {
        this.reviewList = reviewList;
    }*/


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(docId);
        dest.writeValue(docLocationId);
        dest.writeValue(locationId);
        dest.writeValue(docName);
        dest.writeValue(categoryName);
        dest.writeValue(categorySpeciality);
        dest.writeValue(doctorImageUrl);
        dest.writeValue(docSpeciality);
        dest.writeValue(experience);
        dest.writeList(clinicDataList);
        dest.writeValue(favourite);
        dest.writeValue(degree);
        dest.writeValue(rating);
        dest.writeValue(waitingTime);
        dest.writeValue(tokenNo);
        dest.writeValue(aboutDoctor);
        dest.writeList(docServices);
        dest.writeValue(aptDate);
        dest.writeValue(aptTime);
        dest.writeValue(sizeOfList);
        dest.writeValue(addressOfDoctorString);
        dest.writeValue(nameOfClinicString);
    }


    public int describeContents() {
        return 0;
    }

}
