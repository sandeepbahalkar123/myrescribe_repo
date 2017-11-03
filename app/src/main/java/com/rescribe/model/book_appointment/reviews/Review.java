package com.rescribe.model.book_appointment.reviews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {

@SerializedName("revier_name")
@Expose
private String revierName;
@SerializedName("review_commment")
@Expose
private String reviewCommment;
@SerializedName("review_date")
@Expose
private String reviewDate;
@SerializedName("rating")
@Expose
private String rating;

public String getRevierName() {
return revierName;
}

public void setRevierName(String revierName) {
this.revierName = revierName;
}

public String getReviewCommment() {
return reviewCommment;
}

public void setReviewCommment(String reviewCommment) {
this.reviewCommment = reviewCommment;
}

public String getReviewDate() {
return reviewDate;
}

public void setReviewDate(String reviewDate) {
this.reviewDate = reviewDate;
}

public String getRating() {
return rating;
}

public void setRating(String rating) {
this.rating = rating;
}

}