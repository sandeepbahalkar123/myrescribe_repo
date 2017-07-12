package com.myrescribe.model.doctors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.myrescribe.interfaces.CustomResponse;

/**
 * Created by riteshpandhurkar on 16/6/17.
 */

public class DoctorDetail implements CustomResponse {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    private boolean isColored = false;

    public boolean isColored() {
        return isColored;
    }

    public void setColored(boolean colored) {
        isColored = colored;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private  int color;
    private String respectiveDate;
    private boolean isStartElement;
    private boolean isExpanded;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRespectiveDate() {
        return respectiveDate;
    }

    public void setRespectiveDate(String respectiveDate) {
        this.respectiveDate = respectiveDate;
    }

    public boolean getIsStartElement() {
        return isStartElement;
    }

    public void setIsStartElement(boolean isStartElement) {
        this.isStartElement = isStartElement;
    }
}
