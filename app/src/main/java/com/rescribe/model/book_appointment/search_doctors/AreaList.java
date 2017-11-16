
package com.rescribe.model.book_appointment.search_doctors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AreaList {

    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("doctorCount")
    @Expose
    private Integer doctorCount;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getDoctorCount() {
        return doctorCount;
    }

    public void setDoctorCount(Integer doctorCount) {
        this.doctorCount = doctorCount;
    }


}
