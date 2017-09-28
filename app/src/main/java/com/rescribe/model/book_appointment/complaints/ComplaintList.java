
package com.rescribe.model.book_appointment.complaints;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ComplaintList {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("complaint")
    @Expose
    private String complaint;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

}
