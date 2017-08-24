package com.rescribe.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

import java.util.List;

/**
 * Created by jeetal on 27/7/17.
 */

public class SlotModel implements CustomResponse {
    @SerializedName("breakfast")
    @Expose
    private List<Medication> breakfast = null;
    @SerializedName("dinner")
    @Expose
    private List<Medication> dinner = null;
    @SerializedName("snacks")
    @Expose
    private List<Medication> snacks = null;
    @SerializedName("lunch")
    @Expose
    private List<Medication> lunch = null;

    public List<Medication> getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(List<Medication> breakfast) {
        this.breakfast = breakfast;
    }

    public List<Medication> getDinner() {
        return dinner;
    }

    public void setDinner(List<Medication> dinner) {
        this.dinner = dinner;
    }

    public List<Medication> getSnacks() {
        return snacks;
    }

    public void setSnacks(List<Medication> snacks) {
        this.snacks = snacks;
    }

    public List<Medication> getLunch() {
        return lunch;
    }

    public void setLunch(List<Medication> lunch) {
        this.lunch = lunch;
    }

}

