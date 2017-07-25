package com.myrescribe.model.filter;

/**
 * Created by ganeshshirole on 22/7/17.
 */

public class CaseDetails {
    private int id;
    private String caseDetails;
    private int imageId;
    private boolean isSelected = false;

    public CaseDetails(int id, String caseDetails, int imageId) {
        this.id = id;
        this.caseDetails = caseDetails;
        this.imageId = imageId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCaseDetails() {
        return caseDetails;
    }

    public void setCaseDetails(String caseDetails) {
        this.caseDetails = caseDetails;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
