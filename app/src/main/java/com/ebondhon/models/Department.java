package com.ebondhon.models;

import com.google.gson.annotations.SerializedName;

public class Department {
    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("nameEn")
    public String nameEn;

    @SerializedName("facultyId")
    public String facultyId;
}
