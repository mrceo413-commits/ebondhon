package com.ebondhon.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AppData {
    @SerializedName("college")
    public List<CollegeItem> college;

    @SerializedName("faculties")
    public List<Faculty> faculties;

    @SerializedName("councils")
    public List<Council> councils;

    @SerializedName("teachers")
    public List<Teacher> teachers;

    @SerializedName("prl")
    public List<PrlItem> prl;

    @SerializedName("gallery")
    public List<Photo> gallery;

    @SerializedName("contact")
    public ContactInfo contact;

    @SerializedName("phones")
    public List<PhoneItem> phones;

    public static class CollegeItem {
        @SerializedName("id")
        public String id;

        @SerializedName("title")
        public String title;

        @SerializedName("content")
        public String content;

        @SerializedName("imageUrl")
        public String imageUrl;

        @SerializedName("type")
        public String type;
    }

    public static class Faculty {
        @SerializedName("id")
        public String id;

        @SerializedName("name")
        public String name;

        @SerializedName("departments")
        public List<Department> departments;
    }

    public static class PrlItem {
        @SerializedName("year")
        public String year;

        @SerializedName("yearBn")
        public String yearBn;

        @SerializedName("teachers")
        public List<Teacher> teachers;
    }

    public static class Photo {
        @SerializedName("id")
        public String id;

        @SerializedName("url")
        public String url;

        @SerializedName("caption")
        public String caption;
    }

    public static class PhoneItem {
        @SerializedName("name")
        public String name;

        @SerializedName("phone")
        public String phone;

        @SerializedName("designation")
        public String designation;
    }
}
