
package com.rescribe.model.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HealthBlogData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("healthBlogName")
    @Expose
    private String healthBlogName;
    @SerializedName("healthBlogImgUrl")
    @Expose
    private String healthBlogImgUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHealthBlogName() {
        return healthBlogName;
    }

    public void setHealthBlogName(String healthBlogName) {
        this.healthBlogName = healthBlogName;
    }

    public String getHealthBlogImgUrl() {
        return healthBlogImgUrl;
    }

    public void setHealthBlogImgUrl(String healthBlogImgUrl) {
        this.healthBlogImgUrl = healthBlogImgUrl;
    }

}
