package com.rescribe.model.saved_article.request_model;

import com.rescribe.interfaces.CustomResponse;

/**
 * Created by riteshpandhurkar on 30/11/17.
 */

public class ArticleToSaveReqModel implements CustomResponse {
    private String articleUrl;
    private int patientId;
    private int isRemove;

    public String getUrl() {
        return articleUrl;
    }

    public void setUrl(String url) {
        this.articleUrl = url;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int isRemove() {
        return isRemove;
    }

    public void setRemove(int bookMarked) {
        isRemove = bookMarked;
    }
}
