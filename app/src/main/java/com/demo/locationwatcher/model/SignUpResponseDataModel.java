package com.demo.locationwatcher.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 2/12/2018.
 */

public class SignUpResponseDataModel {
    @SerializedName("vendordata")
    @Expose
    private VendorDataModel vendordata;
    @SerializedName("status")
    @Expose
    private BaseStatusModel status;

    public VendorDataModel getVendordata() {
        return vendordata;
    }

    public void setVendordata(VendorDataModel vendordata) {
        this.vendordata = vendordata;
    }

    public BaseStatusModel getStatus() {
        return status;
    }

    public void setStatus(BaseStatusModel status) {
        this.status = status;
    }
}
