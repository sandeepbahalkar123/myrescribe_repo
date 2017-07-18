package com.myrescribe.model.visit_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vital {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("unitValue")
@Expose
private String unitValue;
@SerializedName("unitName")
@Expose
private String unitName;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getUnitValue() {
return unitValue;
}

public void setUnitValue(String unitValue) {
this.unitValue = unitValue;
}

public String getUnitName() {
return unitName;
}

public void setUnitName(String unitName) {
this.unitName = unitName;
}

}