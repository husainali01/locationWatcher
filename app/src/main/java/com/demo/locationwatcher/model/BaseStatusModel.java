package com.demo.locationwatcher.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 2/12/2018.
 */

public class BaseStatusModel {

    @SerializedName("errcode")
    @Expose
    private Integer errcode;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
