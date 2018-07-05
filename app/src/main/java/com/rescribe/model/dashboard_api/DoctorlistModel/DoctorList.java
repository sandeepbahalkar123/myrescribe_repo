package com.rescribe.model.dashboard_api.DoctorlistModel;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoctorList implements Parcelable
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
@SerializedName("docPhone")
@Expose
private String docPhone;
@SerializedName("docName")
@Expose
private String docName;
@SerializedName("doctorImageUrl")
@Expose
private String doctorImageUrl;
@SerializedName("degree")
@Expose
private String degree;
@SerializedName("gender")
@Expose
private String gender;
@SerializedName("specialityId")
@Expose
private int specialityId;
@SerializedName("speciality")
@Expose
private String speciality;
@SerializedName("experience")
@Expose
private int experience;
@SerializedName("clinicList")
@Expose
private List<ClinicList> clinicList = new ArrayList<ClinicList>();
@SerializedName("favorite")
@Expose
private boolean favorite;
@SerializedName("rating")
@Expose
private String rating;
@SerializedName("aboutDoctor")
@Expose
private String aboutDoctor;
@SerializedName("paidStatus")
@Expose
private int paidStatus;
public final static Parcelable.Creator<DoctorList> CREATOR = new Creator<DoctorList>() {


@SuppressWarnings({
"unchecked"
})
public DoctorList createFromParcel(Parcel in) {
return new DoctorList(in);
}

public DoctorList[] newArray(int size) {
return (new DoctorList[size]);
}

}
;

protected DoctorList(Parcel in) {
this.docId = ((int) in.readValue((int.class.getClassLoader())));
this.categoryName = ((String) in.readValue((String.class.getClassLoader())));
this.categorySpeciality = ((String) in.readValue((String.class.getClassLoader())));
this.docPhone = ((String) in.readValue((String.class.getClassLoader())));
this.docName = ((String) in.readValue((String.class.getClassLoader())));
this.doctorImageUrl = ((String) in.readValue((String.class.getClassLoader())));
this.degree = ((String) in.readValue((String.class.getClassLoader())));
this.gender = ((String) in.readValue((String.class.getClassLoader())));
this.specialityId = ((int) in.readValue((int.class.getClassLoader())));
this.speciality = ((String) in.readValue((String.class.getClassLoader())));
this.experience = ((int) in.readValue((int.class.getClassLoader())));
in.readList(this.clinicList, (com.rescribe.model.dashboard_api.DoctorlistModel.ClinicList.class.getClassLoader()));
this.favorite = ((boolean) in.readValue((boolean.class.getClassLoader())));
this.rating = ((String) in.readValue((String.class.getClassLoader())));
this.aboutDoctor = ((String) in.readValue((String.class.getClassLoader())));
this.paidStatus = ((int) in.readValue((int.class.getClassLoader())));
}

public DoctorList() {
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

public String getDocPhone() {
return docPhone;
}

public void setDocPhone(String docPhone) {
this.docPhone = docPhone;
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

public String getGender() {
return gender;
}

public void setGender(String gender) {
this.gender = gender;
}

public int getSpecialityId() {
return specialityId;
}

public void setSpecialityId(int specialityId) {
this.specialityId = specialityId;
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

public List<ClinicList> getClinicList() {
return clinicList;
}

public void setClinicList(List<ClinicList> clinicList) {
this.clinicList = clinicList;
}

public boolean isFavorite() {
return favorite;
}

public void setFavorite(boolean favorite) {
this.favorite = favorite;
}

public String getRating() {
return rating;
}

public void setRating(String rating) {
this.rating = rating;
}

public String getAboutDoctor() {
return aboutDoctor;
}

public void setAboutDoctor(String aboutDoctor) {
this.aboutDoctor = aboutDoctor;
}

public int getPaidStatus() {
return paidStatus;
}

public void setPaidStatus(int paidStatus) {
this.paidStatus = paidStatus;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(docId);
dest.writeValue(categoryName);
dest.writeValue(categorySpeciality);
dest.writeValue(docPhone);
dest.writeValue(docName);
dest.writeValue(doctorImageUrl);
dest.writeValue(degree);
dest.writeValue(gender);
dest.writeValue(specialityId);
dest.writeValue(speciality);
dest.writeValue(experience);
dest.writeList(clinicList);
dest.writeValue(favorite);
dest.writeValue(rating);
dest.writeValue(aboutDoctor);
dest.writeValue(paidStatus);
}

public int describeContents() {
return 0;
}

}