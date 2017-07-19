package com.myrescribe.model;

import java.io.Serializable;

public class Medicine implements Serializable {

    private String medicineName;

    private String medicineType;

    private String medicineCount;

    private boolean isSelect = false;

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineType() {
        return medicineType;
    }

    public void setMedicineType(String medicineType) {
        this.medicineType = medicineType;
    }

    public String getMedicineCount() {
        return medicineCount;
    }

    public void setMedicineCount(String medicineCount) {
        this.medicineCount = medicineCount;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}