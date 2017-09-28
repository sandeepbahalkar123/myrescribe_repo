package com.rescribe.model.book_appointment.doctor_data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewList implements Parcelable
{

@SerializedName("userName")
@Expose
private String userName;
@SerializedName("userMessage")
@Expose
private String userMessage;
public final static Parcelable.Creator<ReviewList> CREATOR = new Creator<ReviewList>() {


@SuppressWarnings({
"unchecked"
})
public ReviewList createFromParcel(Parcel in) {
return new ReviewList(in);
}

public ReviewList[] newArray(int size) {
return (new ReviewList[size]);
}

}
;

protected ReviewList(Parcel in) {
this.userName = ((String) in.readValue((String.class.getClassLoader())));
this.userMessage = ((String) in.readValue((String.class.getClassLoader())));
}

public ReviewList() {
}

public String getUserName() {
return userName;
}

public void setUserName(String userName) {
this.userName = userName;
}

public String getUserMessage() {
return userMessage;
}

public void setUserMessage(String userMessage) {
this.userMessage = userMessage;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(userName);
dest.writeValue(userMessage);
}

public int describeContents() {
return 0;
}

}