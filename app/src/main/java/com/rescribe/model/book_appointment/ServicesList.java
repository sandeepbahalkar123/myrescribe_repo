
package com.rescribe.model.book_appointment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServicesList {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("serviceName")
    @Expose
    private String serviceName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

}
