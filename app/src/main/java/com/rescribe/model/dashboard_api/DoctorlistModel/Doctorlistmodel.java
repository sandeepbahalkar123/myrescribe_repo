package com.rescribe.model.dashboard_api.DoctorlistModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.interfaces.CustomResponse;

public class Doctorlistmodel implements Parcelable,CustomResponse
{

@SerializedName("common")
@Expose
private Common common;
@SerializedName("data")
@Expose
private Data data;
public final static Parcelable.Creator<Doctorlistmodel> CREATOR = new Creator<Doctorlistmodel>() {


@SuppressWarnings({
"unchecked"
})
public Doctorlistmodel createFromParcel(Parcel in) {
return new Doctorlistmodel(in);
}

public Doctorlistmodel[] newArray(int size) {
return (new Doctorlistmodel[size]);
}

}
;

protected Doctorlistmodel(Parcel in) {
this.common = ((Common) in.readValue((Common.class.getClassLoader())));
this.data = ((Data) in.readValue((Data.class.getClassLoader())));
}

public Doctorlistmodel() {
}

public Common getCommon() {
return common;
}

public void setCommon(Common common) {
this.common = common;
}

public Data getData() {
return data;
}

public void setData(Data data) {
this.data = data;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(common);
dest.writeValue(data);
}

public int describeContents() {
return 0;
}

}