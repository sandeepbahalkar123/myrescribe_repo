package com.myrescribe.model.investigation;

import java.io.Serializable;
import java.util.ArrayList;

public class DataObject implements Serializable{
    private int id;
    private String title;
    private boolean isSelected = false;
    private boolean isUploaded = false;
    private ArrayList<Image> photos;

    public DataObject(int id, String title, boolean isSelected, boolean isUploaded, ArrayList<Image> photos) {
        this.id = id;
        this.title = title;
        this.isSelected = isSelected;
        this.isUploaded = isUploaded;
        this.photos = photos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public ArrayList<Image> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Image> photos) {
        this.photos = photos;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    @Override
    public String toString() {
        return title + " " + isSelected + " " + photos.toString();
    }
}