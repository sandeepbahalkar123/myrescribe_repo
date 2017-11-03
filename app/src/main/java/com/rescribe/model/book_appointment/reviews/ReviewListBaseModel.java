package com.rescribe.model.book_appointment.reviews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;
import com.rescribe.model.Common;

public class ReviewListBaseModel implements CustomResponse{

@SerializedName("common")
@Expose
private Common common;
@SerializedName("data")
@Expose
private ReviewList reviewList;

public Common getCommon() {
return common;
}

public void setCommon(Common common) {
this.common = common;
}

public ReviewList getReviewList() {
return reviewList;
}

public void setReviewList(ReviewList reviewList) {
this.reviewList = reviewList;
}

}