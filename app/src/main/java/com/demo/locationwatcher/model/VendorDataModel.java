package com.demo.locationwatcher.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2/12/2018.
 */

public class VendorDataModel implements Parcelable {
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("address1")
    @Expose
    private String address1;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("pin")
    @Expose
    private String pin;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("server")
    @Expose
    private String server;
    @SerializedName("interval")
    @Expose
    private String interval;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("copyright")
    @Expose
    private String copyright;
    @SerializedName("contactperson")
    @Expose
    private String contactperson;
    @SerializedName("engineerList")
    @Expose
    private List<EngineerListModel> engineerList = null;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getContactperson() {
        return contactperson;
    }

    public void setContactperson(String contactperson) {
        this.contactperson = contactperson;
    }

    public List<EngineerListModel> getEngineerList() {
        return engineerList;
    }

    public void setEngineerList(List<EngineerListModel> engineerList) {
        this.engineerList = engineerList;
    }


    protected VendorDataModel(Parcel in) {
        userid = in.readString();
        company = in.readString();
        address = in.readString();
        address1 = in.readString();
        city = in.readString();
        state = in.readString();
        pin = in.readString();
        contact = in.readString();
        email = in.readString();
        website = in.readString();
        code = in.readString();
        server = in.readString();
        interval = in.readString();
        logo = in.readString();
        copyright = in.readString();
        contactperson = in.readString();
        if (in.readByte() == 0x01) {
            engineerList = new ArrayList<EngineerListModel>();
            in.readList(engineerList, EngineerListModel.class.getClassLoader());
        } else {
            engineerList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userid);
        dest.writeString(company);
        dest.writeString(address);
        dest.writeString(address1);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(pin);
        dest.writeString(contact);
        dest.writeString(email);
        dest.writeString(website);
        dest.writeString(code);
        dest.writeString(server);
        dest.writeString(interval);
        dest.writeString(logo);
        dest.writeString(copyright);
        dest.writeString(contactperson);
        if (engineerList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(engineerList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<VendorDataModel> CREATOR = new Parcelable.Creator<VendorDataModel>() {
        @Override
        public VendorDataModel createFromParcel(Parcel in) {
            return new VendorDataModel(in);
        }

        @Override
        public VendorDataModel[] newArray(int size) {
            return new VendorDataModel[size];
        }
    };
}