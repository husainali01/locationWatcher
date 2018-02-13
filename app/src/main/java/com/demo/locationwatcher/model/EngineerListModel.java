package com.demo.locationwatcher.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 2/12/2018.
 */

public class EngineerListModel implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    protected EngineerListModel(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<EngineerListModel> CREATOR = new Parcelable.Creator<EngineerListModel>() {
        @Override
        public EngineerListModel createFromParcel(Parcel in) {
            return new EngineerListModel(in);
        }

        @Override
        public EngineerListModel[] newArray(int size) {
            return new EngineerListModel[size];
        }
    };
}
