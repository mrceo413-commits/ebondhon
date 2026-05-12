package com.ebondhon.models;

import com.google.gson.annotations.SerializedName;

public class Teacher {
    @SerializedName("serial")
    public int serial;

    @SerializedName("idNo")
    public String idNo;

    @SerializedName("name")
    public String name;

    @SerializedName("nameEn")
    public String nameEn;

    @SerializedName("designation")
    public String designation;

    @SerializedName("govtJoining")
    public String govtJoining;

    @SerializedName("thisCollegeJoining")
    public String thisCollegeJoining;

    @SerializedName("thisDesignationJoining")
    public String thisDesignationJoining;

    @SerializedName("birthDate")
    public String birthDate;

    @SerializedName("bloodGroup")
    public String bloodGroup;

    @SerializedName("addressCurrent")
    public String addressCurrent;

    @SerializedName("addressPermanent")
    public String addressPermanent;

    @SerializedName("phone")
    public String phone;

    @SerializedName("email")
    public String email;

    @SerializedName("facebookLink")
    public String facebookLink;

    @SerializedName("photoUrl")
    public String photoUrl;

    @SerializedName("departmentId")
    public String departmentId;
}
