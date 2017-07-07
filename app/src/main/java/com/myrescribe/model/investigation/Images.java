package com.myrescribe.model.investigation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ganeshshirole on 30/6/17.
 */

public class Images {
    @SerializedName("images")
    @Expose
    ArrayList<String> imageArray;

    public ArrayList<String> getImageArray() {
        return imageArray;
    }

    public void setImageArray(ArrayList<String> imageArray) {
        this.imageArray = imageArray;
    }
}
