package com.rescribe.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgetPassPatientDetail {

    @SerializedName("patientId")
    @Expose
    private int patientId;
    @SerializedName("patientName")
    @Expose
    private String patientName;
    @SerializedName("patientEmail")
    @Expose
    private String patientEmail;
    @SerializedName("patientPhone")
    @Expose
    private String patientPhone;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("age")
    @Expose
    private int age;
    @SerializedName("patientGender")
    @Expose
    private String patientGender;
    @SerializedName("salutation")
    @Expose
    private String salutation;

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

}