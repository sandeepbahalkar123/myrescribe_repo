
package com.rescribe.model.saved_article;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SavedArticleDataModel implements Parcelable {

    @SerializedName("articleList")
    @Expose
    private ArrayList<SavedArticleInfo> savedArticleList = new ArrayList<SavedArticleInfo>();
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

    protected SavedArticleDataModel(Parcel in) {
        in.readList(this.savedArticleList, (SavedArticleInfo.class.getClassLoader()));
    }

    public SavedArticleDataModel() {
    }

    public ArrayList<SavedArticleInfo> getSavedArticleList() {
        return savedArticleList;
    }

    public void setSavedArticleList(ArrayList<SavedArticleInfo> savedArticleList) {
        this.savedArticleList = savedArticleList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(savedArticleList);

    }

    public int describeContents() {
        return 0;
    }


}
