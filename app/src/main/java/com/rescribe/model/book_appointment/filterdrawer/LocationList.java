package com.rescribe.model.book_appointment.filterdrawer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationList {

        @SerializedName("areaName")
        @Expose
        private String areaName;
        @SerializedName("isDoctorAvailable")
        @Expose
        private Boolean isDoctorAvailable;

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public Boolean getIsDoctorAvailable() {
            return isDoctorAvailable;
        }

        public void setIsDoctorAvailable(Boolean isDoctorAvailable) {
            this.isDoctorAvailable = isDoctorAvailable;
        }

    }