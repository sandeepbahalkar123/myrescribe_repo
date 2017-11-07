
package com.rescribe.model.dashboard_api;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardDoctorList implements Parcelable
{

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
    @SerializedName("favorite")
    @Expose
    private String favorite;
    @SerializedName("rating")
    @Expose
    private double rating;
    @SerializedName("aptDate")
    @Expose
    private String aptDate;
    @SerializedName("aptTime")
    @Expose
    private String aptTime;
    private int sizeOfList;

    public final static Creator<DashboardDoctorList> CREATOR = new Creator<DashboardDoctorList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DashboardDoctorList createFromParcel(Parcel in) {
            return new DashboardDoctorList(in);
        }

        public DashboardDoctorList[] newArray(int size) {
            return (new DashboardDoctorList[size]);
        }

    }
    ;

    protected DashboardDoctorList(Parcel in) {
        this.docId = ((int) in.readValue((int.class.getClassLoader())));
        this.categoryName = ((String) in.readValue((String.class.getClassLoader())));
        this.categorySpeciality = ((String) in.readValue((String.class.getClassLoader())));
        this.docName = ((String) in.readValue((String.class.getClassLoader())));
        this.doctorImageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.degree = ((String) in.readValue((String.class.getClassLoader())));
        this.speciality = ((String) in.readValue((String.class.getClassLoader())));
        this.experience = ((int) in.readValue((int.class.getClassLoader())));
        in.readList(this.clinicList, (DashboardClinicList.class.getClassLoader()));
        this.favorite = ((String) in.readValue((String.class.getClassLoader())));
        this.rating = ((double) in.readValue((double.class.getClassLoader())));
        this.aptDate = ((String) in.readValue((String.class.getClassLoader())));
        this.aptTime = ((String) in.readValue((String.class.getClassLoader())));
        this.sizeOfList = ((int) in.readValue((int.class.getClassLoader())));

    }

    public DashboardDoctorList() {
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(docId);
        dest.writeValue(categoryName);
        dest.writeValue(categorySpeciality);
        dest.writeValue(docName);
        dest.writeValue(doctorImageUrl);
        dest.writeValue(degree);
        dest.writeValue(speciality);
        dest.writeValue(experience);
        dest.writeList(clinicList);
        dest.writeValue(favorite);
        dest.writeValue(rating);
        dest.writeValue(aptDate);
        dest.writeValue(aptTime);
        dest.writeValue(sizeOfList);
    }

    public int describeContents() {
        return  0;
    }

}
