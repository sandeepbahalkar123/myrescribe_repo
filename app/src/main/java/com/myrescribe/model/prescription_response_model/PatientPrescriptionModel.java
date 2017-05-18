
package com.myrescribe.model.prescription_response_model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;

public class PatientPrescriptionModel implements CustomResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("apistatus")
    @Expose
    private Integer apistatus;
    @SerializedName("authenticated")
    @Expose
    private Integer authenticated;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<PrescriptionData> data = new ArrayList<>();

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getApistatus() {
        return apistatus;
    }

    public void setApistatus(Integer apistatus) {
        this.apistatus = apistatus;
    }

    public Integer getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Integer authenticated) {
        this.authenticated = authenticated;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<PrescriptionData> getData() {
        return data;
    }

    public void setData(ArrayList<PrescriptionData> data) {
        this.data = data;
    }

}
