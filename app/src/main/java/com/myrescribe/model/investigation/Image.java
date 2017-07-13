package com.myrescribe.model.investigation;

import java.io.Serializable;

/**
 * Created by ganeshshirole on 11/7/17.
 */

public class Image implements Serializable{
    private String imageId;
    private String imagePath;
    private boolean isSelected = false;

    public Image(String imageId, String imagePath, boolean isSelected) {
        this.imageId = imageId;
        this.imagePath = imagePath;
        this.isSelected = isSelected;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
