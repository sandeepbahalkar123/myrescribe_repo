package com.myrescribe.model;

import java.util.ArrayList;

public class DataObject {
    private String title;
    private boolean isUploaded;
    private ArrayList<String> photos;

    public DataObject(String title, boolean isUploaded, ArrayList<String> photos) {
        this.title = title;
        this.isUploaded = isUploaded;
        this.photos = photos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return title + " " + isUploaded + " " + photos.toString();
    }
}