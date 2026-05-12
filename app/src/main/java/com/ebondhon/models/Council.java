package com.ebondhon.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Council {
    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("members")
    public List<CouncilMember> members;

    public static class CouncilMember {
        @SerializedName("name")
        public String name;

        @SerializedName("designation")
        public String designation;

        @SerializedName("photoUrl")
        public String photoUrl;
    }
}
