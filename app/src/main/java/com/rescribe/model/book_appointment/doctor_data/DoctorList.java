
package com.rescribe.model.book_appointment.doctor_data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


//TODO : RESPONSE JSON IS GOING TO CHANGE, ONCE RESPONSE FINAL REMOVE UNWANTED VARIABLES.
public class DoctorList implements Parcelable, Cloneable, Comparable<DoctorList> {

    @SerializedName("docId")
    @Expose
    private int docId;
    @SerializedName("docPhone")
    @Expose
    private String docPhone;
    @SerializedName("doc_location_id")
    @Expose
    private int docLocationId;

    /*@SerializedName("aptId")
    @Expose
    private int aptId;*/

    @SerializedName("aptId")
    @Expose
    private String aptId;
    @SerializedName("locationId")
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

    @SerializedName("favorite")
    @Expose
    private boolean favourite;
    @SerializedName("degree")
    @Expose
    private String degree = "";

    @SerializedName("rating")
    @Expose
    private double rating = 0.0d;

    @SerializedName("waitingTime")
    @Expose
    private String waitingTime = "";
    @SerializedName("aboutDoctor")
    @Expose
    private String aboutDoctor = "";
    @SerializedName("services")
    @Expose
    private ArrayList<String> docServices = new ArrayList<>();
    @SerializedName("aptDate")
    @Expose
    private String aptDate = "";
    @SerializedName("aptTime")
    @Expose
    private String aptTime = "";

    @SerializedName("paidStatus")
    @Expose
    private int paidStatus;

    @SerializedName("type")
    @Expose
    private String type; // token/appointment etc
    @SerializedName("tokenNumber")
    @Expose
    private String tokenNumber;

    //------
    private int sizeOfList = 0;
    private double latitude = 0.0;
    private double longitude = 0.0;

    //this parameters are used to sort list by clinicName and doctorName as per functionality
    private String nameOfClinicString = "";
    private String addressOfDoctorString = "";

    private String spannable;
    private boolean doctorSearch;
    private boolean isTypedashboard;
    private boolean isAppointmentTypeMixed;


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
            instance.aptId = ((String) in.readValue((String.class.getClassLoader())));
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
            instance.aboutDoctor = ((String) in.readValue((String.class.getClassLoader())));
            instance.docPhone = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.docServices, (String.class.getClassLoader()));
            instance.aptDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.aptTime = ((String) in.readValue((String.class.getClassLoader())));
            instance.sizeOfList = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.addressOfDoctorString = ((String) in.readValue((String.class.getClassLoader())));
            instance.nameOfClinicString = ((String) in.readValue((String.class.getClassLoader())));
            instance.spannable = ((String) in.readValue((String.class.getClassLoader())));
            instance.doctorSearch = ((boolean) in.readValue((String.class.getClassLoader())));
            instance.isTypedashboard = ((boolean) in.readValue((String.class.getClassLoader())));
            instance.paidStatus = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.isAppointmentTypeMixed = ((boolean) in.readValue((Boolean.class.getClassLoader())));
            instance.type = ((String) in.readValue((String.class.getClassLoader())));
            instance.tokenNumber = ((String) in.readValue((String.class.getClassLoader())));
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

    public String getSpannable() {
        return spannable;
    }

    public void setSpannable(String spannable) {
        this.spannable = spannable;
    }

    public boolean isDoctorSearch() {
        return doctorSearch;
    }

    public void setDoctorSearch(boolean doctorSearch) {
        this.doctorSearch = doctorSearch;
    }

    public boolean isTypedashboard() {
        return isTypedashboard;
    }

    public void setTypedashboard(boolean typedashboard) {
        isTypedashboard = typedashboard;
    }

    public int getPaidStatus() {
        return paidStatus;
    }

    public void setPaidStatus(int paidStatus) {
        this.paidStatus = paidStatus;
    }

    public boolean isAppointmentTypeMixed() {
        return isAppointmentTypeMixed;
    }

    public void setAppointmentTypeMixed(boolean appointmentTypeMixed) {
        isAppointmentTypeMixed = appointmentTypeMixed;
    }
/* public ArrayList<ReviewList> getReviewList() {
=======
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

    /* public ArrayList<ReviewList> getReviewList() {
>>>>>>> ganesh
        return reviewList;
    }

    public void setReviewList(ArrayList<ReviewList> reviewList) {
        this.reviewList = reviewList;
    }*/


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(docId);
        dest.writeValue(docLocationId);
        dest.writeValue(locationId);
        dest.writeValue(aptId);
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
        dest.writeValue(aboutDoctor);
        dest.writeValue(docPhone);
        dest.writeList(docServices);
        dest.writeValue(aptDate);
        dest.writeValue(aptTime);
        dest.writeValue(sizeOfList);
        dest.writeValue(addressOfDoctorString);
        dest.writeValue(nameOfClinicString);
        dest.writeValue(spannable);
        dest.writeValue(doctorSearch);
        dest.writeValue(isTypedashboard);
        dest.writeValue(paidStatus);
        dest.writeValue(isAppointmentTypeMixed);
        dest.writeValue(type);
        dest.writeValue(tokenNumber);
    }


    public int describeContents() {
        return 0;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int compareTo(@NonNull DoctorList doctorList) {
        if (this.docId == doctorList.getDocId()) {
            return 0;
        } else if (this.docId < doctorList.getDocId()) {
            return -1;
        }
        return 1;
    }

    public String getDocPhone() {
        return docPhone;
    }

    public void setDocPhone(String docPhone) {
        this.docPhone = docPhone;
    }

    public String getAptId() {
        return aptId;
    }

    public void setAptId(String aptId) {
        this.aptId = aptId;
    }

    //------- THIS IS DONE FOR APPOINTMENT, TO SHOW CONFIRMATION DIALOG BASED ON type="token/appointment" :START
    public String getType() {
        return type;
    }

    public String getTokenNumber() {
        return tokenNumber;
    }

    public int getLocationId() {
        return locationId;
    }
    //------- THIS IS DONE FOR APPOINTMENT, TO SHOW CONFIRMATION DIALOG BASED ON type="token/appointment" :END

}
