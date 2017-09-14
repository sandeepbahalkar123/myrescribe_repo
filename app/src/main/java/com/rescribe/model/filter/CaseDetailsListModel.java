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
    private CaseHeadingsModel caseHeadingsModel;
    public final static Parcelable.Creator<CaseDetailsListModel> CREATOR = new Creator<CaseDetailsListModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CaseDetailsListModel createFromParcel(Parcel in) {
            CaseDetailsListModel instance = new CaseDetailsListModel();
            instance.common = ((Common) in.readValue((Common.class.getClassLoader())));
            instance.caseHeadingsModel = ((CaseHeadingsModel) in.readValue((CaseHeadingsModel.class.getClassLoader())));
            return instance;
        }

        public CaseDetailsListModel[] newArray(int size) {
            return (new CaseDetailsListModel[size]);
        }

    }
            ;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public CaseHeadingsModel getCaseHeadingsModel() {
        return caseHeadingsModel;
    }

    public void setCaseHeadingsModel(CaseHeadingsModel caseHeadingsModel) {
        this.caseHeadingsModel = caseHeadingsModel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(caseHeadingsModel);
    }

    public int describeContents() {
        return 0;
    }
}