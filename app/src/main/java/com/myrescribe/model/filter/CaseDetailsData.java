package com.myrescribe.model.filter;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CaseDetailsData implements Parcelable {

    @SerializedName("vit_id")
    @Expose
    private int vitId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("selected")
    @Expose
    private boolean selected = false;
    public final static Parcelable.Creator<CaseDetailsData> CREATOR = new Creator<CaseDetailsData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CaseDetailsData createFromParcel(Parcel in) {
            CaseDetailsData instance = new CaseDetailsData();
            instance.vitId = ((int) in.readValue((int.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.selected = ((boolean) in.readValue((boolean.class.getClassLoader())));
            return instance;
        }

        public CaseDetailsData[] newArray(int size) {
            return (new CaseDetailsData[size]);
        }

    };

    public int getVitId() {
        return vitId;
    }

    public void setVitId(int vitId) {
        this.vitId = vitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(vitId);
        dest.writeValue(name);
        dest.writeValue(selected);
    }

    public int describeContents() {
        return 0;
    }

}