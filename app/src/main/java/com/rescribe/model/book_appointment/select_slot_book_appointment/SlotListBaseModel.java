
package com.rescribe.model.book_appointment.select_slot_book_appointment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

public class SlotListBaseModel implements CustomResponse{

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private SelectSlotList selectSlotList;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public SelectSlotList getSelectSlotList() {
        return selectSlotList;
    }

    public void setSelectSlotList(SelectSlotList selectSlotList) {
        this.selectSlotList = selectSlotList;
    }

}
