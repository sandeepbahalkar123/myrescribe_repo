package com.myrescribe.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;

public class Lunch implements CustomResponse {

@SerializedName("id")
@Expose
private Integer id;
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
@SerializedName("medicinSlot")
@Expose
private String medicinSlot;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @SerializedName("quantity")
@Expose
private String quantity;
    private String date;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
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

public String getMedicinSlot() {
return medicinSlot;
}

public void setMedicinSlot(String medicinSlot) {
this.medicinSlot = medicinSlot;
}

public String getQuantity() {
return quantity;
}

public void setQuantity(String quantity) {
this.quantity = quantity;
}

}