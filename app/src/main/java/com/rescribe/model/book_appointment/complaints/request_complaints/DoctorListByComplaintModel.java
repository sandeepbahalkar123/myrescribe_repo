package com.rescribe.model.book_appointment.complaints.request_complaints;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class DoctorListByComplaintModel implements CustomResponse {

@SerializedName("cityName")
@Expose
private String cityName;
@SerializedName("area")
@Expose
private String area;
@SerializedName("patientId")
@Expose
private Integer patientId;
@SerializedName("complaint1")
@Expose
private String complaint1;
@SerializedName("complaint2")
@Expose
private String complaint2;

public String getCityName() {
return cityName;
}

public void setCityName(String cityName) {
this.cityName = cityName;
}

public String getArea() {
return area;
}

public void setArea(String area) {
this.area = area;
}

public Integer getPatientId() {
return patientId;
}

public void setPatientId(Integer patientId) {
this.patientId = patientId;
}

public String getComplaint1() {
return complaint1;
}

public void setComplaint1(String complaint1) {
this.complaint1 = complaint1;
}

public String getComplaint2() {
return complaint2;
}

public void setComplaint2(String complaint2) {
this.complaint2 = complaint2;
}

}
