package com.rescribe.model.book_appointment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenDetail {

    @SerializedName("tokenNumber")
    @Expose
    private int tokenNumber;

    public int getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(int tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

}