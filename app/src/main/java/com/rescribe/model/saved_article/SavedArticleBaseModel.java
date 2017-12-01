
package com.rescribe.model.saved_article;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

public class SavedArticleBaseModel implements Parcelable, CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private SavedArticleDataModel savedArticleDataModel;
    public final static Creator<SavedArticleDataModel> CREATOR = new Creator<SavedArticleDataModel>() {

        @SuppressWarnings({
                "unchecked"
        })
        public SavedArticleDataModel createFromParcel(Parcel in) {
            return new SavedArticleDataModel(in);
        }

        public SavedArticleDataModel[] newArray(int size) {
            return (new SavedArticleDataModel[size]);
        }

    };

    protected SavedArticleBaseModel(Parcel in) {
        this.common = ((Common) in.readValue((Common.class.getClassLoader())));
        this.savedArticleDataModel = ((SavedArticleDataModel) in.readValue((SavedArticleDataModel.class.getClassLoader())));
    }

    public SavedArticleBaseModel() {
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public SavedArticleDataModel getSavedArticleDataModel() {
        return savedArticleDataModel;
    }

    public void setSavedArticleDataModel(SavedArticleDataModel savedArticleDataModel) {
        this.savedArticleDataModel = savedArticleDataModel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(savedArticleDataModel);
    }

    public int describeContents() {
        return 0;
    }

}
