package com.rescribe.model.filter;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

import java.util.ArrayList;

public class CaseDetailsListModel implements Parcelable, CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private ArrayList<CaseDetailsData> caseDetailsDatas = new ArrayList<CaseDetailsData>();
    public final static Parcelable.Creator<CaseDetailsListModel> CREATOR = new Creator<CaseDetailsListModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CaseDetailsListModel createFromParcel(Parcel in) {
            CaseDetailsListModel instance = new CaseDetailsListModel();
            instance.common = ((Common) in.readValue((Common.class.getClassLoader())));
            in.readList(instance.caseDetailsDatas, (CaseDetailsData.class.getClassLoader()));
            return instance;
        }

        public CaseDetailsListModel[] newArray(int size) {
            return (new CaseDetailsListModel[size]);
        }

    };

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public ArrayList<CaseDetailsData> getCaseDetailsDatas() {
        return caseDetailsDatas;
    }

    public void setCaseDetailsDatas(ArrayList<CaseDetailsData> caseDetailsDatas) {
        this.caseDetailsDatas = caseDetailsDatas;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeList(caseDetailsDatas);
    }

    public int describeContents() {
        return 0;
    }

}