
package com.rescribe.model.book_appointment.filterdrawer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

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
        private ArrayList<Integer> clinicFeesRange = null;
        @SerializedName("locationList")
        @Expose
        private ArrayList<LocationList> locationList = null;

        public ArrayList<Integer> getClinicFeesRange() {
            return clinicFeesRange;
        }

        public void setClinicFeesRange(ArrayList<Integer> clinicFeesRange) {
            this.clinicFeesRange = clinicFeesRange;
        }

        public ArrayList<LocationList> getLocationList() {
            return locationList;
        }

        public void setLocationList(ArrayList<LocationList> locationList) {
            this.locationList = locationList;
        }

    }

}

