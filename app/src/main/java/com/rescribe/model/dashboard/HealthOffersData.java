
package com.rescribe.model.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HealthOffersData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("diagnosticCentreName")
    @Expose
    private String diagnosticCentreName;
    @SerializedName("actualPrice")
    @Expose
    private String actualPrice;
    @SerializedName("discountedPrice")
    @Expose
    private String discountedPrice;
    @SerializedName("discountPercentage")
    @Expose
    private String discountPercentage;
    @SerializedName("centerType")
    @Expose
    private String centerType;
    @SerializedName("imgUrl")
    @Expose
    private String imgUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDiagnosticCentreName() {
        return diagnosticCentreName;
    }

    public void setDiagnosticCentreName(String diagnosticCentreName) {
        this.diagnosticCentreName = diagnosticCentreName;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getCenterType() {
        return centerType;
    }

    public void setCenterType(String centerType) {
        this.centerType = centerType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
