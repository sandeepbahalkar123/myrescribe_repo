package com.rescribe.model.requestmodel.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

import java.io.Serializable;

public class SignUpRequestModel implements CustomResponse,Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("emailId")
    @Expose
    private String emailId;
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("salutation")
    @Expose
    private int salutation;

    @SerializedName("age")
    @Expose
    private String age;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("authSocialToken")
    @Expose
    private String authSocialToken;

    @SerializedName("authSocialType")
    @Expose
    private String authSocialType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSalutation() {
        return salutation;
    }

    public void setSalutation(int salutation) {
        this.salutation = salutation;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAuthSocialToken() {
        return authSocialToken;
    }

    public void setAuthSocialToken(String authSocialToken) {
        this.authSocialToken = authSocialToken;
    }

    public String getAuthSocialType() {
        return authSocialType;
    }

    public void setAuthSocialType(String authSocialType) {
        this.authSocialType = authSocialType;
    }
}