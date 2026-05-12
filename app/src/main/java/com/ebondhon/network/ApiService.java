package com.ebondhon.network;

import com.ebondhon.models.AppData;
import com.ebondhon.models.ContactInfo;
import com.ebondhon.models.Council;
import com.ebondhon.models.Teacher;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("api/version")
    Call<JsonObject> getVersion();

    @GET("api/data")
    Call<AppData> getData();

    @GET("api/college")
    Call<List<AppData.CollegeItem>> getCollegeItems();

    @GET("api/departments")
    Call<List<AppData.Faculty>> getDepartments();

    @GET("api/teachers/{deptId}")
    Call<List<Teacher>> getTeachersByDepartment(@Path("deptId") String deptId);

    @GET("api/council")
    Call<List<Council>> getCouncilData();

    @GET("api/prl")
    Call<List<AppData.PrlItem>> getPrlData();

    @GET("api/gallery")
    Call<List<AppData.Photo>> getGallery();

    @GET("api/contact")
    Call<ContactInfo> getContactInfo();
}
