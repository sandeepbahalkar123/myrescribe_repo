
package com.rescribe.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.List;

public class NotificationData implements CustomResponse{

    @SerializedName("prescriptionDate")
    @Expose
    private String prescriptionDate;
    @SerializedName("medication")
    @Expose
    private ArrayList<Medication> medication;

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

    private String notifyTime;

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
        if (prescriptionDate.contains("T")) {
            prescriptionDate = CommonMethods.formatDateTime(prescriptionDate, RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DATE);
        }
        return prescriptionDate;
    }

    public void setPrescriptionDate(String prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public ArrayList<Medication> getMedication() {
        return medication;
    }

    public void setMedication(ArrayList<Medication> medication) {
        this.medication = medication;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }
}
