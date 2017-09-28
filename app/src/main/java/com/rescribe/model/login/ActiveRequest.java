package com.rescribe.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class ActiveRequest implements CustomResponse {

    @SerializedName("patientId")
    @Expose
    private int userId;

    public int getId() {
        return userId;
    }

    public void setId(int patientId) {
        this.userId = patientId;
    }

}