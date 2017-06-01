
package com.myrescribe.model.prescription_response_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrescriptionData {

    @SerializedName("Id")
    @Expose
    private String id;

    @SerializedName("MorningB")
    @Expose
    private String morningB;

    @SerializedName("MorningA")
    @Expose
    private String morningA;

    @SerializedName("LunchB")
    @Expose
    private String lunchB;

    @SerializedName("LunchA")
    @Expose
    private String lunchA;

    @SerializedName("DinnerB")
    @Expose
    private String dinnerB;

    @SerializedName("DinnerA")
    @Expose
    private String dinnerA;

    @SerializedName("Instruction")
    @Expose
    private String instruction;

    @SerializedName("medicineName")
    @Expose
    private String medicineName;

    @SerializedName("medicineId")
    @Expose
    private String medicineId;

    @SerializedName("medicineTypeName")
    @Expose
    private String medicineTypeName;

    @SerializedName("medicineTypeId")
    @Expose
    private String medicineTypeId;

    @SerializedName("Days")
    @Expose
    private String days;

    @SerializedName("Dosage")
    @Expose
    private String dosage;

    private boolean isExpanded = false;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMorningB() {
        return morningB;
    }

    public void setMorningB(String morningB) {
        this.morningB = morningB;
    }

    public String getMorningA() {
        return morningA;
    }

    public void setMorningA(String morningA) {
        this.morningA = morningA;
    }

    public String getLunchB() {
        return lunchB;
    }

    public void setLunchB(String lunchB) {
        this.lunchB = lunchB;
    }

    public String getLunchA() {
        return lunchA;
    }

    public void setLunchA(String lunchA) {
        this.lunchA = lunchA;
    }

    public String getDinnerB() {
        return dinnerB;
    }

    public void setDinnerB(String dinnerB) {
        this.dinnerB = dinnerB;
    }

    public String getDinnerA() {
        return dinnerA;
    }

    public void setDinnerA(String dinnerA) {
        this.dinnerA = dinnerA;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
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

    public Boolean getExpanded() {
        return isExpanded;
    }

    public void setExpanded(Boolean expanded) {
        isExpanded = expanded;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }
}
