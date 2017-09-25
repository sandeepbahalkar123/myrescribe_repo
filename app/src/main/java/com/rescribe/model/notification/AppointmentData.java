package com.rescribe.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ganeshshirole on 22/9/17.
 */

public class AppointmentData {
    @SerializedName("aptList")
    @Expose
    private ArrayList<AppointmentsNotificationData> aptList = new ArrayList<>();

    public ArrayList<AppointmentsNotificationData> getAptList() {
        return aptList;
    }

    public void setAptList(ArrayList<AppointmentsNotificationData> aptList) {
        this.aptList = aptList;
    }
}
