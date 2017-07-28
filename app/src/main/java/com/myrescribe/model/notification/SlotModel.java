package com.myrescribe.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;

import java.util.List;

/**
 * Created by jeetal on 27/7/17.
 */

public class SlotModel implements CustomResponse {
    @SerializedName("breakfast")
    @Expose
    private List<Breakfast> breakfast = null;
    @SerializedName("dinner")
    @Expose
    private List<Dinner> dinner = null;
    @SerializedName("snacks")
    @Expose
    private List<Snack> snacks = null;
    @SerializedName("lunch")
    @Expose
    private List<Lunch> lunch = null;

    public List<Breakfast> getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(List<Breakfast> breakfast) {
        this.breakfast = breakfast;
    }

    public List<Dinner> getDinner() {
        return dinner;
    }

    public void setDinner(List<Dinner> dinner) {
        this.dinner = dinner;
    }

    public List<Snack> getSnacks() {
        return snacks;
    }

    public void setSnacks(List<Snack> snacks) {
        this.snacks = snacks;
    }

    public List<Lunch> getLunch() {
        return lunch;
    }

    public void setLunch(List<Lunch> lunch) {
        this.lunch = lunch;
    }

}

