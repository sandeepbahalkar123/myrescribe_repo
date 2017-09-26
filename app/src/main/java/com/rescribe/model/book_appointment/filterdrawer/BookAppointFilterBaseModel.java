
package com.rescribe.model.book_appointment.filterdrawer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;
import com.rescribe.model.book_appointment.ServicesData;

import java.util.ArrayList;

public class BookAppointFilterBaseModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private ServicesData servicesData;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public ServicesData getServicesData() {
        return servicesData;
    }

    public void setServicesData(ServicesData servicesData) {
        this.servicesData = servicesData;
    }

    public class FilterConfigData {
        @SerializedName("clinicFeesRange")
        @Expose
        private String[] clinicFeesRange;
        @SerializedName("distance")
        @Expose
        private String[] distanceRange;
        @SerializedName("locationList")
        @Expose
        private String[] locationList;

        public String[] getClinicFeesRange() {
            return clinicFeesRange;
        }

        public void setClinicFeesRange(String[] clinicFeesRange) {
            this.clinicFeesRange = clinicFeesRange;
        }

        public String[] getDistanceRange() {
            return distanceRange;
        }

        public void setDistanceRange(String[] distanceRange) {
            this.distanceRange = distanceRange;
        }

        public String[] getLocationList() {
            return locationList;
        }

        public void setLocationList(String[] locationList) {
            this.locationList = locationList;
        }
    }
}

