package com.rescribe.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class SocialLoginRequestModel implements CustomResponse{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("authSocialType")
    @Expose
    private String authSocialType;
    @SerializedName("authSocialToken")
    @Expose
    private String authSocialToken;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthSocialType() {
        return authSocialType;
    }

    public void setAuthSocialType(String authSocialType) {
        this.authSocialType = authSocialType;
    }

    public String getAuthSocialToken() {
        return authSocialToken;
    }

    public void setAuthSocialToken(String authSocialToken) {
        this.authSocialToken = authSocialToken;
    }

}