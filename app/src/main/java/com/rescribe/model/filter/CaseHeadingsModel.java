package com.rescribe.model.filter;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CaseHeadingsModel implements Parcelable {

    @SerializedName("caseHeadings")
    @Expose
    private ArrayList<CaseDetailsData> caseHeadings = null;
    public final static Parcelable.Creator<CaseHeadingsModel> CREATOR = new Creator<CaseHeadingsModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CaseHeadingsModel createFromParcel(Parcel in) {
            CaseHeadingsModel instance = new CaseHeadingsModel();
            in.readList(instance.caseHeadings, (CaseDetailsData.class.getClassLoader()));
            return instance;
        }

        public CaseHeadingsModel[] newArray(int size) {
            return (new CaseHeadingsModel[size]);
        }

    };

    public ArrayList<CaseDetailsData> getCaseHeadings() {
        return caseHeadings;
    }

    public void setCaseHeadings(ArrayList<CaseDetailsData> caseHeadings) {
        this.caseHeadings = caseHeadings;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(caseHeadings);
    }

    public int describeContents() {
        return 0;
    }

}