package com.demo.locationwatcher.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 2/12/2018.
 */

public class SignUpBaseResponseModel {
    @SerializedName("vendordata")
    @Expose
    private SignUpResponseDataModel data;

    public SignUpResponseDataModel getData() {
        return data;
    }

    public void setData(SignUpResponseDataModel data) {
        this.data = data;
    }
}
