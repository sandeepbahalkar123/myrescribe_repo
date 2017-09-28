
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
    private FilterConfigData filterConfigData;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public FilterConfigData getFilterConfigData() {
        return filterConfigData;
    }

    public void setFilterConfigData(FilterConfigData filterConfigData) {
        this.filterConfigData = filterConfigData;
    }

    public class FilterConfigData {
        @SerializedName("clinicFeesRange")
        @Expose
        private ArrayList<String> clinicFeesRange;
        @SerializedName("distance")
        @Expose
        private ArrayList<String> distanceRange;
        @SerializedName("locationList")
        @Expose
        private ArrayList<String> locationList;

        public ArrayList<String> getClinicFeesRange() {
            return clinicFeesRange;
        }

        public void setClinicFeesRange(ArrayList<String> clinicFeesRange) {
            this.clinicFeesRange = clinicFeesRange;
        }

        public ArrayList<String> getDistanceRange() {
            return distanceRange;
        }

        public void setDistanceRange(ArrayList<String> distanceRange) {
            this.distanceRange = distanceRange;
        }

        public ArrayList<String> getLocationList() {
            return locationList;
        }

        public void setLocationList(ArrayList<String> locationList) {
            this.locationList = locationList;
        }
    }
}

