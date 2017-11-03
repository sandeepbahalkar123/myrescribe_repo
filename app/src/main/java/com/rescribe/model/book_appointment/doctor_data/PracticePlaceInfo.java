package com.rescribe.model.book_appointment.doctor_data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PracticePlaceInfo implements Parcelable {
    @SerializedName("location_name")
    @Expose
    private String locationName;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("area_name")
    @Expose
    private String areaName;

    public PracticePlaceInfo(String locationName, String cityName, String areaName) {
        this.locationName = locationName;
        this.cityName = cityName;
        this.areaName = areaName;
    }

    public final static Parcelable.Creator<PracticePlaceInfo> CREATOR = new Creator<PracticePlaceInfo>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PracticePlaceInfo createFromParcel(Parcel in) {
            return new PracticePlaceInfo(in);
        }

        public PracticePlaceInfo[] newArray(int size) {
            return (new PracticePlaceInfo[size]);
        }

    };

    protected PracticePlaceInfo(Parcel in) {
        this.locationName = ((String) in.readValue((String.class.getClassLoader())));
        this.cityName = ((String) in.readValue((String.class.getClassLoader())));
        this.areaName = ((String) in.readValue((String.class.getClassLoader())));
    }

    public PracticePlaceInfo() {
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(locationName);
        dest.writeValue(cityName);
        dest.writeValue(areaName);
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return locationName + ", " + areaName + ", " + cityName;
    }
}