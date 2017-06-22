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
}
