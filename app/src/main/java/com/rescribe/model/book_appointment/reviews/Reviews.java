package com.rescribe.model.book_appointment.reviews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Reviews {

@SerializedName("total_review")
@Expose
private Integer totalReview;
@SerializedName("reviews")
@Expose
private ArrayList<Review> reviews = null;

public Integer getTotalReview() {
return totalReview;
}

public void setTotalReview(Integer totalReview) {
this.totalReview = totalReview;
}

public ArrayList<Review> getReviews() {
return reviews;
}

public void setReviews(ArrayList<Review> reviews) {
this.reviews = reviews;
}

}