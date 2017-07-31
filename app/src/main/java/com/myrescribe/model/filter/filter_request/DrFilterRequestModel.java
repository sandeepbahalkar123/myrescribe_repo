package com.myrescribe.model.filter.filter_request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DrFilterRequestModel implements Parcelable {

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
            in.readList(instance.docId, (java.lang.Integer.class.getClassLoader()));
            in.readList(instance.docSpeciality, (java.lang.String.class.getClassLoader()));
            instance.startDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.endDate = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.cases, (java.lang.String.class.getClassLoader()));
            return instance;
        }

        public DrFilterRequestModel[] newArray(int size) {
            return (new DrFilterRequestModel[size]);
        }

    };
    @SerializedName("docId")
    @Expose
    private List<Integer> docId = null;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("docSpeciality")
    @Expose
    private List<String> docSpeciality = null;
    @SerializedName("cases")
    @Expose
    private List<String> cases = null;

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public List<Integer> getDocId() {
        return docId;
    }

    public void setDocId(List<Integer> docId) {
        this.docId = docId;
    }

    public List<String> getDocSpeciality() {
        return docSpeciality;
    }

    public void setDocSpeciality(List<String> docSpeciality) {
        this.docSpeciality = docSpeciality;
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

    public List<String> getCases() {
        return cases;
    }

    public void setCases(List<String> cases) {
        this.cases = cases;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(patientId);
        dest.writeList(docId);
        dest.writeList(docSpeciality);
        dest.writeValue(startDate);
        dest.writeValue(endDate);
        dest.writeList(cases);
    }

    public int describeContents() {
        return 0;
    }

}