package com.rescribe.model.book_appointment.search_doctors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecentlyVisitedAreaList {

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("areaName")
    @Expose
    private String areaName;
    @SerializedName("city")
    @Expose
    private String city;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}