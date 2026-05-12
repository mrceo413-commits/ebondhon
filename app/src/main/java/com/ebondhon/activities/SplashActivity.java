package com.ebondhon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.ebondhon.R;
import com.ebondhon.models.AppData;
import com.ebondhon.network.RetrofitClient;
import com.ebondhon.utils.CacheManager;
import com.ebondhon.utils.NetworkUtils;
import com.ebondhon.utils.VersionChecker;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 2500;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.splashProgress);

        if (NetworkUtils.isNetworkAvailable(this)) {
            checkVersionAndLoadData();
        } else {
            navigateToMainAfterDelay();
        }
    }

    private void checkVersionAndLoadData() {
        VersionChecker.checkVersion(this, new VersionChecker.VersionCallback() {
            @Override
            public void onNewVersionAvailable(int newVersion) {
                fetchData(newVersion);
            }

            @Override
            public void onUpToDate() {
                navigateToMainAfterDelay();
            }

            @Override
            public void onError(String message) {
                navigateToMainAfterDelay();
            }
        });
    }

    private void fetchData(int newVersion) {
        RetrofitClient.getApiService().getData().enqueue(new Callback<AppData>() {
            @Override
            public void onResponse(Call<AppData> call, Response<AppData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String json = new Gson().toJson(response.body());
                    CacheManager.saveData(SplashActivity.this, json);
                    VersionChecker.setCachedVersion(SplashActivity.this, newVersion);
                }
                navigateToMainAfterDelay();
            }

            @Override
            public void onFailure(Call<AppData> call, Throwable t) {
                navigateToMainAfterDelay();
            }
        });
    }

    private void navigateToMainAfterDelay() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (!isFinishing() && !isDestroyed()) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DELAY);
    }
}
