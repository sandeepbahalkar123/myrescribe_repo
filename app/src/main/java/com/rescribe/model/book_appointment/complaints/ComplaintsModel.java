
package com.rescribe.model.book_appointment.complaints;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

import java.util.ArrayList;

public class ComplaintsModel implements CustomResponse{

    @SerializedName("complaintList")
    @Expose
    private ArrayList<ComplaintList> complaintList = null;

    public ArrayList<ComplaintList> getComplaintList() {
        return complaintList;
    }

    public void setComplaintList(ArrayList<ComplaintList> complaintList) {
        this.complaintList = complaintList;
    }

}
