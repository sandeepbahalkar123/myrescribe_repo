package com.rescribe.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgetPassPatientDetail {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("patient_name")
    @Expose
    private String patientName;
    @SerializedName("patient_email")
    @Expose
    private String patientEmail;
    @SerializedName("patient_phon")
    @Expose
    private String patientPhon;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("age")
    @Expose
    private int age;
    @SerializedName("patient_gender")
    @Expose
    private String patientGender;
    @SerializedName("salutation")
    @Expose
    private String salutation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPatientPhon() {
        return patientPhon;
    }

    public void setPatientPhon(String patientPhon) {
        this.patientPhon = patientPhon;
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