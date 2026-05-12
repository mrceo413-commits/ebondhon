package com.ebondhon.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ebondhon.models.AppData;
import com.ebondhon.network.RetrofitClient;
import com.ebondhon.utils.CacheManager;
import com.ebondhon.utils.VersionChecker;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {

    private final MutableLiveData<AppData> appData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isOffline = new MutableLiveData<>(false);

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<AppData> getAppData() {
        return appData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsOffline() {
        return isOffline;
    }

    public void loadData() {
        isLoading.setValue(true);

        VersionChecker.checkVersion(getApplication(), new VersionChecker.VersionCallback() {
            @Override
            public void onNewVersionAvailable(int newVersion) {
                fetchAndCacheData(newVersion);
            }

            @Override
            public void onUpToDate() {
                loadCachedData(false);
            }

            @Override
            public void onError(String message) {
                loadCachedData(true);
            }
        });
    }

    private void fetchAndCacheData(int newVersion) {
        RetrofitClient.getApiService().getData().enqueue(new Callback<AppData>() {
            @Override
            public void onResponse(Call<AppData> call, Response<AppData> response) {
                isLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    AppData data = response.body();
                    appData.postValue(data);
                    isOffline.postValue(false);

                    String json = new Gson().toJson(data);
                    CacheManager.saveData(getApplication(), json);
                    VersionChecker.setCachedVersion(getApplication(), newVersion);
                } else {
                    loadCachedData(true);
                }
            }

            @Override
            public void onFailure(Call<AppData> call, Throwable t) {
                isLoading.postValue(false);
                loadCachedData(true);
            }
        });
    }

    private void loadCachedData(boolean offline) {
        String json = CacheManager.loadData(getApplication());
        if (json != null) {
            AppData data = CacheManager.parseData(json);
            if (data != null) {
                appData.postValue(data);
                isOffline.postValue(offline);
                isLoading.postValue(false);
                return;
            }
        }
        isLoading.postValue(false);
        errorMessage.postValue("কোনো তথ্য পাওয়া যায়নি");
    }
}
