package com.ebondhon.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ebondhon.network.RetrofitClient;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VersionChecker {

    private static final String PREFS_NAME = "ebondhon_prefs";
    private static final String KEY_CACHED_VERSION = "cached_version";

    public interface VersionCallback {
        void onNewVersionAvailable(int newVersion);
        void onUpToDate();
        void onError(String message);
    }

    public static void checkVersion(Context context, VersionCallback callback) {
        RetrofitClient.getApiService().getVersion().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int serverVersion = response.body().get("version").getAsInt();
                    int cachedVersion = getCachedVersion(context);

                    if (serverVersion > cachedVersion) {
                        callback.onNewVersionAvailable(serverVersion);
                    } else {
                        callback.onUpToDate();
                    }
                } else {
                    callback.onError("সার্ভার থেকে তথ্য পাওয়া যায়নি");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public static int getCachedVersion(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_CACHED_VERSION, 0);
    }

    public static void setCachedVersion(Context context, int version) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_CACHED_VERSION, version).apply();
    }
}
