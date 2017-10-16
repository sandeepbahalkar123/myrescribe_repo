package com.rescribe.model.book_appointment.filterdrawer.request_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

/**
 * Created by jeetal on 16/10/17.
 */

public class RequestToGetFilterDataModel implements CustomResponse {

    @SerializedName("cityName")
    @Expose
    private String cityName;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
