
package com.rescribe.model.book_appointment.select_slot_book_appointment;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelectSlotList {

    @SerializedName("slotList")
    @Expose
    private ArrayList<SlotList> slotList = null;

    public ArrayList<SlotList> getSlotList() {
        return slotList;
    }

    public void setSlotList(ArrayList<SlotList> slotList) {
        this.slotList = slotList;
    }

}
