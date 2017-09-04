package com.rescribe.model.prescription_response_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrescriptionData {

    @SerializedName("patientId")
    @Expose
    private Integer patientId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("breakfastBefore")
    @Expose
    private String breakfastBefore;
    @SerializedName("breakfastAfter")
    @Expose
    private String breakfastAfter;
    @SerializedName("lunchBefore")
    @Expose
    private String lunchBefore;
    @SerializedName("lunchAfter")
    @Expose
    private String lunchAfter;
    @SerializedName("snacksBefore")
    @Expose
    private String snacksBefore;
    @SerializedName("snacksAfter")
    @Expose
    private String snacksAfter;
    @SerializedName("dinnerBefore")
    @Expose
    private String dinnerBefore;
    @SerializedName("dinnerAfter")
    @Expose
    private String dinnerAfter;
    @SerializedName("instruction")
    @Expose
    private String instruction;
    @SerializedName("days")
    @Expose
    private String days;
    @SerializedName("dosage")
    @Expose
    private String dosage;
    @SerializedName("medicineName")
    @Expose
    private String medicineName;
    @SerializedName("medicineId")
    @Expose
    private Integer medicineId;
    @SerializedName("medicineTypeName")
    @Expose
    private String medicineTypeName;
    @SerializedName("medicineTypeId")
    @Expose
    private String medicineTypeId;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    private String mealTime;

    public String getSlotLabel() {
        return slotLabel;
    }

    public void setSlotLabel(String slotLabel) {
        this.slotLabel = slotLabel;
    }

    private String slotLabel;
    public String getMealTime() {
        return mealTime;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    public String getFreqSchedule() {
        return freqSchedule;
    }

    public void setFreqSchedule(String freqSchedule) {
        this.freqSchedule = freqSchedule;
    }

    @SerializedName("freq")
    @Expose

    private String freq;
    @SerializedName("freqSchedule")
    @Expose
    private String freqSchedule;

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBreakfastBefore() {
        return breakfastBefore;
    }

    public void setBreakfastBefore(String breakfastBefore) {
        this.breakfastBefore = breakfastBefore;
    }

    public String getBreakfastAfter() {
        return breakfastAfter;
    }

    public void setBreakfastAfter(String breakfastAfter) {
        this.breakfastAfter = breakfastAfter;
    }

    public String getLunchBefore() {
        return lunchBefore;
    }

    public void setLunchBefore(String lunchBefore) {
        this.lunchBefore = lunchBefore;
    }

    public String getLunchAfter() {
        return lunchAfter;
    }

    public void setLunchAfter(String lunchAfter) {
        this.lunchAfter = lunchAfter;
    }

    public String getSnacksBefore() {
        return snacksBefore;
    }

    public void setSnacksBefore(String snacksBefore) {
        this.snacksBefore = snacksBefore;
    }

    public String getSnacksAfter() {
        return snacksAfter;
    }

    public void setSnacksAfter(String snacksAfter) {
        this.snacksAfter = snacksAfter;
    }

    public String getDinnerBefore() {
        return dinnerBefore;
    }

    public void setDinnerBefore(String dinnerBefore) {
        this.dinnerBefore = dinnerBefore;
    }

    public String getDinnerAfter() {
        return dinnerAfter;
    }

    public void setDinnerAfter(String dinnerAfter) {
        this.dinnerAfter = dinnerAfter;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public Integer getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }

    public String getMedicineTypeName() {
        return medicineTypeName;
    }

    public void setMedicineTypeName(String medicineTypeName) {
        this.medicineTypeName = medicineTypeName;
    }

    public String getMedicineTypeId() {
        return medicineTypeId;
    }

    public void setMedicineTypeId(String medicineTypeId) {
        this.medicineTypeId = medicineTypeId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public boolean isSnacksThere() {
        return isSnacksThere;
    }





    public void setSnacksThere(boolean snacksThere) {
        isSnacksThere = snacksThere;
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

    private boolean isSnacksThere = true;
    private boolean isLunchExpanded = false;
    private boolean isDinnerExpanded = false;
    private boolean isBreakFastExpanded = false;
    private boolean isSnacksExpanded = false;
    private String date;
    private boolean isExpanded = false;
    private boolean isLunchThere = true;
    private boolean isDinnerThere = true;
    private boolean isBreakThere = true;
    private boolean isTabSelected = false;

    public boolean isTabSelected() {
        return isTabSelected;
    }

    public void setTabSelected(boolean tabSelected) {
        isTabSelected = tabSelected;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isBreakThere() {
        return isBreakThere;
    }

    public void setBreakThere(boolean breakThere) {
        isBreakThere = breakThere;
    }

}