package com.rescribe.model.book_appointment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenData {

    @SerializedName("tokenDetails")
    @Expose
    private TokenDetail tokenDetail;

    public TokenDetail getTokenDetail() {
        return tokenDetail;
    }

    public void setTokenDetail(TokenDetail tokenDetail) {
        this.tokenDetail = tokenDetail;
    }

}