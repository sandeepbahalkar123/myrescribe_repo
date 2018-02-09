
package com.rescribe.model.book_appointment.select_slot_book_appointment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;

import java.util.ArrayList;

public class TimeSlotListDataModel {

    @SerializedName("isAppointmentTaken")
    @Expose
    private int isAppointmentTaken;
    @SerializedName("isOpen")
    @Expose
    private boolean isOpen;
    @SerializedName("timeSlots")
    @Expose
    private ArrayList<TimeSlotsInfoList> timeSlotsInfoList = new ArrayList<>();
    @SerializedName("docDetail")
    @Expose
    private DoctorList doctorListData;


    public int isAppointmentTaken() {
        return isAppointmentTaken;
    }

    public void setAppointmentTaken(int appointmentTaken) {
        isAppointmentTaken = appointmentTaken;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public ArrayList<TimeSlotsInfoList> getTimeSlotsInfoList() {
        return timeSlotsInfoList;
    }

    public void setTimeSlotsInfoList(ArrayList<TimeSlotsInfoList> timeSlotsInfoList) {
        this.timeSlotsInfoList = timeSlotsInfoList;
    }

    public DoctorList getDoctorListData() {
        return doctorListData;
    }

    public void setDoctorListData(DoctorList doctorListData) {
        this.doctorListData = doctorListData;
    }
}
