
package com.rescribe.model.book_appointment;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class ServicesData implements CustomResponse{

    @SerializedName("servicesList")
    @Expose
    private ArrayList<ServicesList> servicesList = null;

    public ArrayList<ServicesList> getServicesList() {
        return servicesList;
    }

    public void setServicesList(ArrayList<ServicesList> servicesList) {
        this.servicesList = servicesList;
    }

}
