
package com.rescribe.model.book_appointment.select_slot_book_appointment;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeSlotsInfoList {

    @SerializedName("time")
    @Expose
    private String slotName;
    @SerializedName("description")
    @Expose
    private String slotDescription;
    @SerializedName("timeslot")
    @Expose
    private ArrayList<TimeSlotData> timeSlotList = null;

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public String getSlotDescription() {
        return slotDescription;
    }

    public void setSlotDescription(String slotDescription) {
        this.slotDescription = slotDescription;
    }

    public ArrayList<TimeSlotData> getTimeSlotList() {
        return timeSlotList;
    }

    public void setTimeSlotList(ArrayList<TimeSlotData> timeSlotList) {
        this.timeSlotList = timeSlotList;
    }

    public class TimeSlotData {
        @SerializedName("slotId")
        @Expose
        private String slotId;
        @SerializedName("fromTime")
        @Expose
        private String fromTime;
        @SerializedName("toTime")
        @Expose
        private String toTime;
        @SerializedName("isAvailable")
        @Expose
        private boolean isAvailable;

        public String getSlotId() {
            return slotId;
        }

        public void setSlotId(String slotId) {
            this.slotId = slotId;
        }

        public String getFromTime() {
            return fromTime;
        }

        public void setFromTime(String fromTime) {
            this.fromTime = fromTime;
        }

        public String getToTime() {
            return toTime;
        }

        public void setToTime(String toTime) {
            this.toTime = toTime;
        }

        public boolean isAvailable() {
            return isAvailable;
        }

        public void setAvailable(boolean available) {
            isAvailable = available;
        }
    }
}
