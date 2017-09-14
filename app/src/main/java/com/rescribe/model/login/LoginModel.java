package com.rescribe.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;
import com.rescribe.model.YearsMonthsDataList;

import java.util.ArrayList;

public class LoginModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;

    @SerializedName("data")
    @Expose
    private LoginData loginData;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public LoginData getLoginData() {
        return loginData;
    }

    public void setLoginData(LoginData loginData) {
        this.loginData = loginData;
    }
}