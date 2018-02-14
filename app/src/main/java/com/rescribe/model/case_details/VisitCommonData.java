
package com.rescribe.model.case_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class VisitCommonData implements CustomResponse{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name = "";
    @SerializedName("url")
    @Expose
    private String url = "";
    @SerializedName("medicinename")
    @Expose
    private String medicinename = "";
    @SerializedName("remarks")
    @Expose
    private String remarks = "";

    @SerializedName("dosage")
    @Expose
    private String dosage = "";

    @SerializedName("medicineTypeName")
    @Expose
    private String medicineTypeName = "";

    public String getVitalValue() {
        return vitalValue;
    }

    public void setVitalValue(String vitalValue) {
        this.vitalValue = vitalValue;
    }

    private String vitalValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMedicinename() {
        return medicinename;
    }

    public void setMedicinename(String medicinename) {
        this.medicinename = medicinename;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getMedicineTypeName() {
        return medicineTypeName;
    }

    public void setMedicineTypeName(String medicineTypeName) {
        this.medicineTypeName = medicineTypeName;
    }
}
