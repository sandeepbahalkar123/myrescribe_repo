package com.rescribe.model.book_appointment.reviews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class ReviewList  implements CustomResponse {

@SerializedName("reviews")
@Expose
private Reviews reviews;

public Reviews getReviews() {
return reviews;
}

public void setReviews(Reviews reviews) {
this.reviews = reviews;
}

}