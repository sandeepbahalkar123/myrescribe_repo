
package com.rescribe.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class Medication implements CustomResponse {

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

    public boolean isTabWebService() {
        return isTabWebService;
    }

    public void setTabWebService(boolean tabWebService) {
        isTabWebService = tabWebService;
    }

    @SerializedName("medicineTypeId")
    @Expose
    private String medicineTypeId;
    @SerializedName("medicinSlot")
    @Expose
    private String medicinSlot;
    @SerializedName("quantity")
    @Expose
    private String quantity;

    private boolean isSnacksThere = true;
    private boolean isLunchExpanded = false;
    private boolean isDinnerExpanded = false;


    //--This is done to fix in unread notification message update
    // NO USED OF ANYWHERE OTHER THAN THIS--
    @SerializedName("unreadNotificationMessageDataID")
    @Expose
    private String unreadNotificationMessageDataID;
    @SerializedName("unreadNotificationMessageDataTimeStamp")
    @Expose
    private String unreadNotificationMessageDataTimeStamp;
    //----

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

    public boolean isDinnerExpanded() {
        return isDinnerExpanded;
    }

    public void setDinnerExpanded(boolean dinnerExpanded) {
        isDinnerExpanded = dinnerExpanded;
    }

    public boolean isLunchExpanded() {
        return isLunchExpanded;
    }

    public void setLunchExpanded(boolean lunchExpanded) {
        isLunchExpanded = lunchExpanded;
    }

    public boolean isSnacksThere() {
        return isSnacksThere;
    }

    public void setSnacksThere(boolean snacksThere) {
        isSnacksThere = snacksThere;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public boolean isTabSelected() {
        return isTabSelected;
    }

    public void setTabSelected(boolean tabSelected) {
        isTabSelected = tabSelected;
    }

    private boolean isTabWebService = true;
    private boolean isBreakFastExpanded = false;
    private boolean isSnacksExpanded = false;
    private String date;
    private boolean isExpanded = false;
    private boolean isLunchThere = true;
    private boolean isDinnerThere = true;
    private boolean isBreakThere = true;
    private boolean isTabSelected = false;

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


    public String getUnreadNotificationMessageDataID() {
        return unreadNotificationMessageDataID;
    }

    public void setUnreadNotificationMessageDataID(String unreadNotificationMessageDataID) {
        this.unreadNotificationMessageDataID = unreadNotificationMessageDataID;
    }

    public String getUnreadNotificationMessageDataTimeStamp() {
        return unreadNotificationMessageDataTimeStamp;
    }

    public void setUnreadNotificationMessageDataTimeStamp(String unreadNotificationMessageDataTimeStamp) {
        this.unreadNotificationMessageDataTimeStamp = unreadNotificationMessageDataTimeStamp;
    }
}
