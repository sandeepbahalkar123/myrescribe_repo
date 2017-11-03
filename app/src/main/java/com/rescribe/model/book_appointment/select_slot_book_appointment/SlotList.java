
package com.rescribe.model.book_appointment.select_slot_book_appointment;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SlotList {

    @SerializedName("slotName")
    @Expose
    private String slotName;
    @SerializedName("slotTime")
    @Expose
    private String slotTime;
    @SerializedName("slotTimingsList")
    @Expose
    private ArrayList<String> slotTimingsList = null;

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public String getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(String slotTime) {
        this.slotTime = slotTime;
    }

    public ArrayList<String> getSlotTimingsList() {
        return slotTimingsList;
    }

    public void setSlotTimingsList(ArrayList<String> slotTimingsList) {
        this.slotTimingsList = slotTimingsList;
    }

}
