
package com.rescribe.model.book_appointment.select_slot_book_appointment;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeSlotListDataModel {

    @SerializedName("isOpen")
    @Expose
    private boolean isOpen;
    @SerializedName("timeSlots")
    @Expose
    private ArrayList<TimeSlotsInfoList> timeSlotsInfoList = new ArrayList<>();

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public ArrayList<TimeSlotsInfoList> getTimeSlotsInfoList() {
        return timeSlotsInfoList;
    }

    public void setTimeSlotsInfoList(ArrayList<TimeSlotsInfoList> timeSlotsInfoList) {
        this.timeSlotsInfoList = timeSlotsInfoList;
    }
}
