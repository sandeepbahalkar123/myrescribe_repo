package com.rescribe.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

import java.util.List;

/**
 * Created by jeetal on 27/7/17.
 */

public class AdapterNotificationModel implements CustomResponse{
    @SerializedName("prescriptionDate")
    @Expose
    private String prescriptionDate;
    @SerializedName("medication")
    @Expose
    private SlotModel slotModel;
    private List<Medication> medication = null;
    private boolean isExpanded = false;

    private boolean isLunchThere = true;
    private boolean isDinnerThere = true;
    private boolean isBreakThere = true;
    private boolean isSnacksThere = true;

    private boolean isLunchExpanded = false;
    private boolean isDinnerExpanded = false;
    private boolean isBreakFastExpanded = false;
    private boolean isSnacksExpanded = false;
    private String date;
    private boolean isTabSelected = false;

    public SlotModel getSlotModel() {
        return slotModel;
    }

    public void setSlotModel(SlotModel slotModel) {
        this.slotModel = slotModel;
    }

    public void setMedication(List<Medication> medication) {
        this.medication = medication;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public boolean isLunchThere() {
        return isLunchThere;
    }

    public void setLunchThere(boolean lunchThere) {
        isLunchThere = lunchThere;
    }

    public boolean isDinnerThere() {
        return isDinnerThere;
    }

    public void setDinnerThere(boolean dinnerThere) {
        isDinnerThere = dinnerThere;
    }

    public boolean isBreakThere() {
        return isBreakThere;
    }

    public void setBreakThere(boolean breakThere) {
        isBreakThere = breakThere;
    }

    public boolean isSnacksThere() {
        return isSnacksThere;
    }

    public void setSnacksThere(boolean snacksThere) {
        isSnacksThere = snacksThere;
    }

    public boolean isLunchExpanded() {
        return isLunchExpanded;
    }

    public void setLunchExpanded(boolean lunchExpanded) {
        isLunchExpanded = lunchExpanded;
    }

    public boolean isDinnerExpanded() {
        return isDinnerExpanded;
    }

    public void setDinnerExpanded(boolean dinnerExpanded) {
        isDinnerExpanded = dinnerExpanded;
    }

    public boolean isBreakFastExpanded() {
        return isBreakFastExpanded;
    }

    public void setBreakFastExpanded(boolean breakFastExpanded) {
        isBreakFastExpanded = breakFastExpanded;
    }

    public boolean isSnacksExpanded() {
        return isSnacksExpanded;
    }

    public void setSnacksExpanded(boolean snacksExpanded) {
        isSnacksExpanded = snacksExpanded;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isTabSelected() {
        return isTabSelected;
    }

    public void setTabSelected(boolean tabSelected) {
        isTabSelected = tabSelected;
    }

    public String getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(String prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public SlotModel getMedication() {
        return slotModel;
    }

    public void setMedication(SlotModel slotModel) {
        this.slotModel = slotModel;
    }

}
