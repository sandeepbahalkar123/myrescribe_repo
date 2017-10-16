
package com.rescribe.model.book_appointment.doctor_data;

import java.util.ArrayList;
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
    @SerializedName("doctorImageUrl")
    @Expose
    private String doctorImageUrl = "";
    @SerializedName("speciality")
    @Expose
    private String speciality = "";
    @SerializedName("experience")
    @Expose
    private int experience;
    @SerializedName("doctorAddress")
    @Expose
    private String doctorAddress = "";
    @SerializedName("amount")
    @Expose
    private int amount;
    @SerializedName("distance")
    @Expose
    private String distance = "";
    @SerializedName("recentlyVisited")
    @Expose
    private boolean recentlyVisited;
    @SerializedName("favourite")
    @Expose
    private boolean favourite;
    @SerializedName("aboutDoctor")
    @Expose
    private String aboutDoctor = "";
    @SerializedName("degree")
    @Expose
    private String degree = "";
    @SerializedName("rating")
    @Expose
    private String rating = "";
    @SerializedName("waitingTime")
    @Expose
    private String waitingTime = "";
    @SerializedName("tokenNo")
    @Expose
    private String tokenNo;
    @SerializedName("morePracticePlaces")
    @Expose
    private ArrayList<DoctorList.PracticePlaceInfo> morePracticePlaces = new ArrayList<>();
    @SerializedName("openToday")
    @Expose
    private boolean openToday;
    @SerializedName("openTimeSlots")
    @Expose
    private ArrayList<String> availableTimeSlots = new ArrayList<>();
    @SerializedName("reviewCount")
    @Expose
    private int totalReview;
    private double latitude = 0.0;
    private double longitude = 0.0;

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
            instance.doctorImageUrl = ((String) in.readValue((String.class.getClassLoader())));
            instance.speciality = ((String) in.readValue((String.class.getClassLoader())));
            instance.experience = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.doctorAddress = ((String) in.readValue((String.class.getClassLoader())));
            instance.amount = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.distance = ((String) in.readValue((String.class.getClassLoader())));
            instance.recentlyVisited = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            instance.aboutDoctor = ((String) in.readValue((String.class.getClassLoader())));
            instance.degree = ((String) in.readValue((String.class.getClassLoader())));
            instance.rating = ((String) in.readValue((String.class.getClassLoader())));
            instance.waitingTime = ((String) in.readValue((String.class.getClassLoader())));
            instance.tokenNo = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.morePracticePlaces, (DoctorList.PracticePlaceInfo.class.getClassLoader()));
            instance.openToday = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            instance.favourite = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            in.readList(instance.availableTimeSlots, (String.class.getClassLoader()));
            instance.totalReview = ((Integer) in.readValue((Integer.class.getClassLoader())));
            //in.readList(instance.reviewList, (ReviewList.class.getClassLoader()));
            return instance;
        }

        public DoctorList[] newArray(int size) {
            return (new DoctorList[size]);
        }

    };

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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public boolean getRecentlyVisited() {
        return recentlyVisited;
    }

    public void setRecentlyVisited(boolean recentlyVisited) {
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
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


    public boolean getOpenToday() {
        return openToday;
    }

    public void setOpenToday(boolean openToday) {
        this.openToday = openToday;
    }

    public ArrayList<String> getAvailableTimeSlots() {
        return availableTimeSlots;
    }

    public void setAvailableTimeSlots(ArrayList<String> availableTimeSlots) {
        this.availableTimeSlots = availableTimeSlots;
    }

    public int getTotalReview() {
        return totalReview;
    }

    public void setTotalReview(int totalReview) {
        this.totalReview = totalReview;
    }

   /* public ArrayList<ReviewList> getReviewList() {
        return reviewList;
    }

    public void setReviewList(ArrayList<ReviewList> reviewList) {
        this.reviewList = reviewList;
    }*/


    public ArrayList<String> getMorePracticePlaces() {
        ArrayList<String> temp = new ArrayList<>();
        for (PracticePlaceInfo data :
                morePracticePlaces) {
            temp.add(data.toString());
        }
        return temp;
    }

    public void setMorePracticePlaces(ArrayList<String> morePracticePlaces) {
        ArrayList<PracticePlaceInfo> d = new ArrayList<>();
        for (String data :
                morePracticePlaces) {
            d.add(new PracticePlaceInfo(data, "", ""));
        }
        this.morePracticePlaces = d;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(docId);
        dest.writeValue(docLocationId);
        dest.writeValue(locationId);
        dest.writeValue(docName);
        dest.writeValue(doctorImageUrl);
        dest.writeValue(speciality);
        dest.writeValue(experience);
        dest.writeValue(doctorAddress);
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
        dest.writeValue(favourite);
        dest.writeList(availableTimeSlots);
        dest.writeValue(totalReview);
       /*  dest.writeList(reviewList);*/
    }


    public int describeContents() {
        return 0;
    }

/*

    @Override
    public String toString() {
        return "DoctorList{" +
                "docId=" + docId +
                ", docLocationId=" + docLocationId +
                ", locationId=" + locationId +
                ", docName='" + docName + '\'' +
                ", doctorImageUrl='" + doctorImageUrl + '\'' +
                ", speciality='" + speciality + '\'' +
                ", experience=" + experience +
                ", doctorAddress='" + doctorAddress + '\'' +
                ", amount=" + amount +
                ", distance='" + distance + '\'' +
                ", recentlyVisited=" + recentlyVisited +
                ", aboutDoctor='" + aboutDoctor + '\'' +
                ", degree='" + degree + '\'' +
                ", rating='" + rating + '\'' +
                ", waitingTime='" + waitingTime + '\'' +
                ", tokenNo=" + tokenNo +
                ", morePracticePlaces=" + morePracticePlaces +
                ", openToday='" + openToday + '\'' +
                ", availableTimeSlots=" + availableTimeSlots +
                ", favourite=" + favourite +
                ", totalReview=" + totalReview +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
*/

    public class PracticePlaceInfo {
        @SerializedName("location_name")
        @Expose
        private String locationName = "";
        @SerializedName("city_name")
        @Expose
        private String cityName = "";
        @SerializedName("area_name")
        @Expose
        private String areaName = "";

        public PracticePlaceInfo(String locationName, String cityName, String areaName) {
            this.locationName = locationName;
            this.cityName = cityName;
            this.areaName = areaName;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        @Override
        public String toString() {
            return locationName + ", " + areaName + ", " + cityName;
        }
    }
}
