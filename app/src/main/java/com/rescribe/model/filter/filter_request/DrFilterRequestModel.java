package com.rescribe.model.filter.filter_request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

import java.util.ArrayList;

public class DrFilterRequestModel implements Parcelable, CustomResponse {

    @SerializedName("patientId")
    @Expose
    private int patientId;
    public final static Parcelable.Creator<DrFilterRequestModel> CREATOR = new Creator<DrFilterRequestModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DrFilterRequestModel createFromParcel(Parcel in) {
            DrFilterRequestModel instance = new DrFilterRequestModel();
            instance.patientId = ((int) in.readValue((int.class.getClassLoader())));
            in.readList(instance.docIds, (java.lang.Integer.class.getClassLoader()));
            in.readList(instance.docSpecialities, (java.lang.String.class.getClassLoader()));
            instance.startDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.endDate = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.cases, (java.lang.String.class.getClassLoader()));
            return instance;
        }

        public DrFilterRequestModel[] newArray(int size) {
            return (new DrFilterRequestModel[size]);
        }

    };
    @SerializedName("docIds")
    @Expose
    private ArrayList<Integer> docIds = new ArrayList<>();
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("docSpecialities")
    @Expose
    private ArrayList<String> docSpecialities = new ArrayList<>();
    @SerializedName("cases")
    @Expose
    private ArrayList<String> cases = new ArrayList<>();

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }


    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ArrayList<String> getCases() {
        return cases;
    }

    public void setCases(ArrayList<String> cases) {
        this.cases = cases;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(patientId);
        dest.writeList(docIds);
        dest.writeList(docSpecialities);
        dest.writeValue(startDate);
        dest.writeValue(endDate);
        dest.writeList(cases);
    }

    public int describeContents() {
        return 0;
    }

    public ArrayList<Integer> getDocIds() {
        return docIds;
    }

    public void setDocIds(ArrayList<Integer> docIds) {
        this.docIds = docIds;
    }

    public ArrayList<String> getDocSpecialities() {
        return docSpecialities;
    }

    public void setDocSpecialities(ArrayList<String> docSpecialities) {
        this.docSpecialities = docSpecialities;
    }
}