package com.rescribe.model.book_appointment.doctor_data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestFavouriteDoctorModel {

@SerializedName("doctorId")
@Expose
private Integer doctorId;
@SerializedName("patientId")
@Expose
private Integer patientId;
@SerializedName("favouriteflag")
@Expose
private Boolean favouriteflag;

public Integer getDoctorId() {
return doctorId;
}

public void setDoctorId(Integer doctorId) {
this.doctorId = doctorId;
}

public Integer getPatientId() {
return patientId;
}

public void setPatientId(Integer patientId) {
this.patientId = patientId;
}

public Boolean getFavouriteflag() {
return favouriteflag;
}

public void setFavouriteflag(Boolean favouriteflag) {
this.favouriteflag = favouriteflag;
}

}