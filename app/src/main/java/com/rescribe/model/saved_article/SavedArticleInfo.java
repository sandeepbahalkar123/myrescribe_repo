
package com.rescribe.model.saved_article;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.dashboard_api.DashboardBottomMenuList;
import com.rescribe.model.dashboard_api.DashboardLeftSideDrawerMenuList;
import com.rescribe.model.dashboard_api.DashboardMenuList;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

public class SavedArticleInfo implements Parcelable {

    @SerializedName("authorName")
    @Expose
    private String authorName;
    @SerializedName("articleTitle")
    @Expose
    private String articleTitle;
    @SerializedName("authorSpecialization")
    @Expose
    private String specialization;
    @SerializedName("authorAddress")
    @Expose
    private String address;
    @SerializedName("authorImageURL")
    @Expose
    private String authorImageURL;
    @SerializedName("articleUpdatedDate")
    @Expose
    private String articleUpdatedDate;
    @SerializedName("articleFeatureImageURL")
    @Expose
    private String articleImageURL;
    @SerializedName("articleExcerpt")
    @Expose
    private String articleExcerpt;
    @SerializedName("articleUrl")
    @Expose
    private String articleUrl;
    public final static Creator<SavedArticleInfo> CREATOR = new Creator<SavedArticleInfo>() {


        @SuppressWarnings({
                "unchecked"
        })
        public SavedArticleInfo createFromParcel(Parcel in) {
            SavedArticleInfo instance = new SavedArticleInfo();
            instance.authorName = ((String) in.readValue((String.class.getClassLoader())));
            instance.articleTitle = ((String) in.readValue((String.class.getClassLoader())));
            instance.specialization = ((String) in.readValue((String.class.getClassLoader())));
            instance.address = ((String) in.readValue((String.class.getClassLoader())));
            instance.authorImageURL = ((String) in.readValue((String.class.getClassLoader())));
            instance.articleUpdatedDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.articleImageURL = ((String) in.readValue((String.class.getClassLoader())));
            instance.articleExcerpt = ((String) in.readValue((String.class.getClassLoader())));
            instance.articleUrl = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public SavedArticleInfo[] newArray(int size) {
            return (new SavedArticleInfo[size]);
        }

    };


    public SavedArticleInfo() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(authorName);
        dest.writeValue(articleTitle);
        dest.writeValue(specialization);
        dest.writeValue(address);
        dest.writeValue(authorImageURL);
        dest.writeValue(articleUpdatedDate);
        dest.writeValue(articleImageURL);
        dest.writeValue(articleExcerpt);
        dest.writeValue(articleUrl);

    }

    public int describeContents() {
        return 0;
    }


    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAuthorImageURL() {
        return authorImageURL;
    }

    public void setAuthorImageURL(String authorImageURL) {
        this.authorImageURL = authorImageURL;
    }

    public String getArticleUpdatedDate() {
        if (articleUpdatedDate.contains("T")) {
            articleUpdatedDate = CommonMethods.formatDateTime(articleUpdatedDate, RescribeConstants.DATE_PATTERN.MMM_DD_YYYY, RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DATE);
        }
        return articleUpdatedDate;
    }

    public void setArticleUpdatedDate(String articleUpdatedDate) {
        this.articleUpdatedDate = articleUpdatedDate;
    }

    public String getArticleImageURL() {
        return articleImageURL;
    }

    public void setArticleImageURL(String articleImageURL) {
        this.articleImageURL = articleImageURL;
    }

    public String getArticleExcerpt() {
        return articleExcerpt;
    }

    public void setArticleExcerpt(String articleExcerpt) {
        this.articleExcerpt = articleExcerpt;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

}
