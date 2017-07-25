
package com.myrescribe.model.notification;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;
import com.myrescribe.util.CommonMethods;
import com.myrescribe.util.MyRescribeConstants;

public class NotificationData implements CustomResponse{

    @SerializedName("prescriptionDate")
    @Expose
    private String prescriptionDate;
    @SerializedName("medication")
    @Expose
    private List<Medication> medication = null;
    private boolean isExpanded = false;
    private boolean isLunchThere = true;
    private boolean isDinnerThere = true;
    private boolean isBreakThere = true;
    private boolean isSnacksThere = true;
    private boolean isLunchExpanded = false;

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

    private boolean isDinnerExpanded = false;
    private boolean isBreakFastExpanded = false;
    private boolean isSnacksExpanded = false;
    private String date;
    private boolean isTabSelected = false;
    public String getPrescriptionDate() {
        if (prescriptionDate.contains("T")) {
            prescriptionDate = CommonMethods.formatDateTime(prescriptionDate, MyRescribeConstants.DATE_PATTERN.DD_MM_YYYY, MyRescribeConstants.DATE_PATTERN.UTC_PATTERN, MyRescribeConstants.DATE);
        }
        return prescriptionDate;
    }

    public void setPrescriptionDate(String prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public List<Medication> getMedication() {
        return medication;
    }

    public void setMedication(List<Medication> medication) {
        this.medication = medication;
    }

}
