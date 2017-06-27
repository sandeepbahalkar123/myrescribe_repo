
package com.myrescribe.model.prescription_response_model;

import android.util.Log;

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

    @SerializedName("EveningB")
    @Expose
    private String eveningB;

    @SerializedName("EveningA")
    @Expose
    private String eveningA;

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

    // ganesh

    private boolean isLunchExpanded = false;
    private boolean isDinnerExpanded = false;
    private boolean isBreakFastExpanded = false;

    public boolean isSnacksExpanded() {
        return isSnacksExpanded;
    }

    public void setSnacksExpanded(boolean snacksExpanded) {
        isSnacksExpanded = snacksExpanded;
    }

    private boolean isSnacksExpanded = false;

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

    private boolean isLunchThere = true;
    private boolean isDinnerThere = true;
    private boolean isBreakThere = true;

    public boolean isSnacksThere() {
        return isSnacksThere;
    }

    public void setSnacksThere(boolean snacksThere) {
        isSnacksThere = snacksThere;
    }

    private boolean isSnacksThere = true;




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

    // for date

    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // for tab selection fun

    private boolean isTabSelected = false;

    public boolean isTabSelected() {
        return isTabSelected;
    }

    public void setTabSelected(boolean tabSelected) {
        isTabSelected = tabSelected;
    }

    //  End

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMorningB() {
        return morningB;
    }
    public String getEveningA() {
        return eveningA;
    }

    public void setEveningA(String eveningA) {
        this.eveningA = eveningA;
    }

    public String getEveningB() {
        return eveningB;
    }

    public void setEveningB(String eveningB) {
        this.eveningB = eveningB;
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

    public static String getMedicineTypeAbbreviation(String medicineTypeName) {
        String abbreviation = medicineTypeName;
        if (medicineTypeName.equalsIgnoreCase("syrup")) {
            abbreviation = "SYP";
        } else if (medicineTypeName.equalsIgnoreCase("Tablet")) {
            abbreviation = "tab";
        } else if (medicineTypeName.equalsIgnoreCase("Capsule")) {
            abbreviation = "cap";
        } else if (medicineTypeName.equalsIgnoreCase("injection")) {
            abbreviation = "inj";
        } else if (medicineTypeName.equalsIgnoreCase("insulin")) {
            abbreviation = "INS";
        } else if (medicineTypeName.equalsIgnoreCase("Inhaler")) {
            abbreviation = "INH";
        } else if (medicineTypeName.equalsIgnoreCase("liquid")) {
            abbreviation = "liq";
        } else if (medicineTypeName.equalsIgnoreCase("tan")) {
            abbreviation = "tan";
        } else if (medicineTypeName.equalsIgnoreCase("cream")) {
            abbreviation = "CRM";
        } else if (medicineTypeName.equalsIgnoreCase("jelly")) {
            abbreviation = "JEL";
        } else if (medicineTypeName.equalsIgnoreCase("local application")) {
            abbreviation = "LAP";
        } else if (medicineTypeName.equalsIgnoreCase("ointment")) {
            abbreviation = "ONT";
        } else if (medicineTypeName.equalsIgnoreCase("lotion")) {
            abbreviation = "LOT";
        } else if (medicineTypeName.equalsIgnoreCase("drops")) {
            abbreviation = "DRP";
        } else if (medicineTypeName.equalsIgnoreCase("eye drops")) {
            abbreviation = "eDRP";
        } else if (medicineTypeName.equalsIgnoreCase("nasal drops")) {
            abbreviation = "nDRP";
        } else if (medicineTypeName.equalsIgnoreCase("nasal spray")) {
            abbreviation = "nSPRY";
        } else if (medicineTypeName.equalsIgnoreCase("ointment/powder")) {
            abbreviation = "pow";
        } else if (medicineTypeName.equalsIgnoreCase("respules")) {
            abbreviation = "RES";
        } else if (medicineTypeName.equalsIgnoreCase("rotacaps")) {
            abbreviation = "ROTA";
        } else if (medicineTypeName.equalsIgnoreCase("sachet")) {
            abbreviation = "sach";
        }
        return abbreviation.toUpperCase();

    }

}
