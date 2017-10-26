package com.rescribe.model.book_appointment.doctor_data;

import java.util.ArrayList;
import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoctorListByClinic implements Parcelable
{

@SerializedName("docId")
@Expose
private Integer docId;
@SerializedName("doc_location_id")
@Expose
private Integer docLocationId;
@SerializedName("location_id")
@Expose
private Integer locationId;
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
private Integer experience;
@SerializedName("clinicName")
@Expose
private String clinicName;
@SerializedName("doctorAddress")
@Expose
private String doctorAddress;
@SerializedName("amount")
@Expose
private Integer amount;
@SerializedName("distance")
@Expose
private String distance;
@SerializedName("recentlyVisited")
@Expose
private Boolean recentlyVisited;
@SerializedName("favourite")
@Expose
private Boolean favourite;
@SerializedName("aboutDoctor")
@Expose
private String aboutDoctor;
@SerializedName("degree")
@Expose
private String degree;
@SerializedName("rating")
@Expose
private String rating;
@SerializedName("waitingTime")
@Expose
private String waitingTime;
@SerializedName("tokenNo")
@Expose
private String tokenNo;
@SerializedName("morePracticePlaces")
@Expose
private ArrayList<PracticePlaceInfo> morePracticePlaces = new ArrayList<PracticePlaceInfo>();
@SerializedName("openToday")
@Expose
private Boolean openToday;
@SerializedName("openTimeSlots")
@Expose
private ArrayList<String> openTimeSlots = new ArrayList<String>();
@SerializedName("reviewCount")
@Expose
private Integer reviewCount;
public final static Parcelable.Creator<DoctorListByClinic> CREATOR = new Creator<DoctorListByClinic>() {


@SuppressWarnings({
"unchecked"
})
public DoctorListByClinic createFromParcel(Parcel in) {
return new DoctorListByClinic(in);
}

public DoctorListByClinic[] newArray(int size) {
return (new DoctorListByClinic[size]);
}

}
;

protected DoctorListByClinic(Parcel in) {
this.docId = ((Integer) in.readValue((Integer.class.getClassLoader())));
this.docLocationId = ((Integer) in.readValue((Integer.class.getClassLoader())));
this.locationId = ((Integer) in.readValue((Integer.class.getClassLoader())));
this.docName = ((String) in.readValue((String.class.getClassLoader())));
this.doctorImageUrl = ((String) in.readValue((String.class.getClassLoader())));
this.speciality = ((String) in.readValue((String.class.getClassLoader())));
this.experience = ((Integer) in.readValue((Integer.class.getClassLoader())));
this.clinicName = ((String) in.readValue((String.class.getClassLoader())));
this.doctorAddress = ((String) in.readValue((String.class.getClassLoader())));
this.amount = ((Integer) in.readValue((Integer.class.getClassLoader())));
this.distance = ((String) in.readValue((String.class.getClassLoader())));
this.recentlyVisited = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
this.favourite = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
this.aboutDoctor = ((String) in.readValue((String.class.getClassLoader())));
this.degree = ((String) in.readValue((String.class.getClassLoader())));
this.rating = ((String) in.readValue((String.class.getClassLoader())));
this.waitingTime = ((String) in.readValue((String.class.getClassLoader())));
this.tokenNo = ((String) in.readValue((String.class.getClassLoader())));
in.readList(this.morePracticePlaces, (PracticePlaceInfo.class.getClassLoader()));
this.openToday = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
in.readList(this.openTimeSlots, (java.lang.String.class.getClassLoader()));
this.reviewCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
}

public DoctorListByClinic() {
}

public Integer getDocId() {
return docId;
}

public void setDocId(Integer docId) {
this.docId = docId;
}

public Integer getDocLocationId() {
return docLocationId;
}

public void setDocLocationId(Integer docLocationId) {
this.docLocationId = docLocationId;
}

public Integer getLocationId() {
return locationId;
}

public void setLocationId(Integer locationId) {
this.locationId = locationId;
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

public Integer getExperience() {
return experience;
}

public void setExperience(Integer experience) {
this.experience = experience;
}

public String getClinicName() {
return clinicName;
}

public void setClinicName(String clinicName) {
this.clinicName = clinicName;
}

public String getDoctorAddress() {
return doctorAddress;
}

public void setDoctorAddress(String doctorAddress) {
this.doctorAddress = doctorAddress;
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

public Boolean getFavourite() {
return favourite;
}

public void setFavourite(Boolean favourite) {
this.favourite = favourite;
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

public Boolean getOpenToday() {
return openToday;
}

public void setOpenToday(Boolean openToday) {
this.openToday = openToday;
}

public ArrayList<String> getOpenTimeSlots() {
return openTimeSlots;
}

public void setOpenTimeSlots(ArrayList<String> openTimeSlots) {
this.openTimeSlots = openTimeSlots;
}

public Integer getReviewCount() {
return reviewCount;
}

public void setReviewCount(Integer reviewCount) {
this.reviewCount = reviewCount;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(docId);
dest.writeValue(docLocationId);
dest.writeValue(locationId);
dest.writeValue(docName);
dest.writeValue(doctorImageUrl);
dest.writeValue(speciality);
dest.writeValue(experience);
dest.writeValue(clinicName);
dest.writeValue(doctorAddress);
dest.writeValue(amount);
dest.writeValue(distance);
dest.writeValue(recentlyVisited);
dest.writeValue(favourite);
dest.writeValue(aboutDoctor);
dest.writeValue(degree);
dest.writeValue(rating);
dest.writeValue(waitingTime);
dest.writeValue(tokenNo);
dest.writeList(morePracticePlaces);
dest.writeValue(openToday);
dest.writeList(openTimeSlots);
dest.writeValue(reviewCount);
}

public int describeContents() {
return 0;
}

}