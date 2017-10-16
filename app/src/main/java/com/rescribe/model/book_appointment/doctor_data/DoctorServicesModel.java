
package com.rescribe.model.book_appointment.doctor_data;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class DoctorServicesModel implements Parcelable, CustomResponse {

    @SerializedName("docDetails")
    @Expose
    private ArrayList<DoctorList> doctorList = new ArrayList<>();
    @SerializedName("doctorSpecialities")
    @Expose
    private ArrayList<String> doctorSpecialities = new ArrayList<>();
    public final static Creator<DoctorServicesModel> CREATOR = new Creator<DoctorServicesModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DoctorServicesModel createFromParcel(Parcel in) {
            DoctorServicesModel instance = new DoctorServicesModel();
            in.readList(instance.doctorList, (DoctorList.class.getClassLoader()));
            in.readList(instance.doctorSpecialities, (String.class.getClassLoader()));
            return instance;
        }

        public DoctorServicesModel[] newArray(int size) {
            return (new DoctorServicesModel[size]);
        }

    };

    public ArrayList<DoctorList> getDoctorList() {
        return doctorList;
    }

    public ArrayList<DoctorList> getRecentlyVisitedAndFavoriteDoctorList() {
        ArrayList<DoctorList> temp = new ArrayList<>();
        for (DoctorList docObject :
                doctorList) {
            if (docObject.getRecentlyVisited() || docObject.getFavourite()) {
                temp.add(docObject);
            }
        }
        return temp;
    }

    public void setDoctorList(ArrayList<DoctorList> doctorList) {
        this.doctorList = doctorList;
    }

    public ArrayList<DoctorSpeciality> getDoctorSpecialities() {
        ArrayList<DoctorSpeciality> temp = new ArrayList<>();
        ArrayList<String> doctorSpecialities = this.doctorSpecialities;
        for (int i = 0; i < doctorSpecialities.size(); i++) {
            DoctorSpeciality object = new DoctorSpeciality();
            object.setId(i);
            object.setSpeciality(doctorSpecialities.get(i));
            temp.add(object);
        }
        return temp;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(doctorList);
        dest.writeList(doctorSpecialities);
    }

    public int describeContents() {
        return 0;
    }


}
