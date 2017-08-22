package com.myrescribe.helpers.database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.model.investigation.Image;

import java.util.ArrayList;

/**
 * Created by ganeshshirole on 21/8/17.
 */

public class MyRecordsData {
    private int docId;
    private String visitDate;
    @SerializedName("imageArray")
    @Expose
    private ArrayList<Image> imageArrayList;

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public ArrayList<Image> getImageArrayList() {
        return imageArrayList;
    }

    public void setImageArrayList(ArrayList<Image> imageArrayList) {
        this.imageArrayList = imageArrayList;
    }
}
