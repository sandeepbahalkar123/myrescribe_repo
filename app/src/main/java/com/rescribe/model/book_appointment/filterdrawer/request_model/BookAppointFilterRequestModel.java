
package com.rescribe.model.book_appointment.filterdrawer.request_model;

import android.os.Parcel;
import android.os.Parcelable;

import com.rescribe.interfaces.CustomResponse;

import java.util.Arrays;

public class BookAppointFilterRequestModel implements CustomResponse, Parcelable {


    private String cityName;

    private String area;

    private Integer patientId;
    private String gender;

    private String[] clinicFeesRange;

    private String[] distance;

    private String[] availability;

    private String[] locationList;

    private String sortBy;
    private String sortOrder;


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String[] getClinicFeesRange() {
        return clinicFeesRange;
    }

    public void setClinicFeesRange(String[] clinicFeesRange) {
        this.clinicFeesRange = clinicFeesRange;
    }

    public String[] getDistance() {
        return distance;
    }

    public void setDistance(String[] distance) {
        this.distance = distance;
    }

    public String[] getAvailability() {
        return availability;
    }

    public void setAvailability(String[] availability) {
        this.availability = availability;
    }

    public String[] getLocationList() {
        return locationList;
    }

    public void setLocationList(String[] locationList) {
        this.locationList = locationList;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public final static Creator<BookAppointFilterRequestModel> CREATOR = new Creator<BookAppointFilterRequestModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BookAppointFilterRequestModel createFromParcel(Parcel in) {
            BookAppointFilterRequestModel instance = new BookAppointFilterRequestModel();
            instance.gender = ((String) in.readValue((Integer.class.getClassLoader())));
            instance.clinicFeesRange = ((String[]) in.readValue((String.class.getClassLoader())));
            instance.distance = ((String[]) in.readValue((String.class.getClassLoader())));
            instance.availability = ((String[]) in.readValue((String.class.getClassLoader())));
            instance.locationList = ((String[]) in.readValue((String.class.getClassLoader())));

            instance.cityName = ((String) in.readValue((String.class.getClassLoader())));
            instance.area = ((String) in.readValue((String.class.getClassLoader())));
            instance.patientId = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.sortBy = ((String) in.readValue((String.class.getClassLoader())));
            instance.sortOrder = ((String) in.readValue((String.class.getClassLoader())));

            return instance;
        }

        public BookAppointFilterRequestModel[] newArray(int size) {
            return (new BookAppointFilterRequestModel[size]);
        }

    };

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(gender);
        dest.writeArray(clinicFeesRange);
        dest.writeArray(distance);
        dest.writeArray(availability);
        dest.writeArray(locationList);

        dest.writeValue(cityName);
        dest.writeValue(area);
        dest.writeValue(patientId);
        dest.writeValue(sortBy);
        dest.writeValue(sortOrder);

    }

    @Override
    public String toString() {
        return "BookAppointFilterRequestModel{" +
                "gender='" + gender + '\'' +
                ", clinicFeesRange=" + Arrays.toString(clinicFeesRange) +
                ", distance=" + Arrays.toString(distance) +
                ", availability=" + Arrays.toString(availability) +
                ", locationList=" + Arrays.toString(locationList) +
                '}';
    }
}

