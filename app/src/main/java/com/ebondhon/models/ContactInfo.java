package com.ebondhon.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ContactInfo {
    @SerializedName("website")
    public String website;

    @SerializedName("facebook")
    public String facebook;

    @SerializedName("phone")
    public String phone;

    @SerializedName("email")
    public String email;

    @SerializedName("importantLinks")
    public List<ImportantLink> importantLinks;

    public static class ImportantLink {
        @SerializedName("title")
        public String title;

        @SerializedName("url")
        public String url;

        @SerializedName("color")
        public String color;
    }
}
