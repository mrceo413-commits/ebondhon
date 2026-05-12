package com.ebondhon.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ebondhon.R;
import com.ebondhon.adapters.PhotoGalleryAdapter;
import com.ebondhon.models.AppData;
import com.ebondhon.utils.CacheManager;
import com.ebondhon.utils.FontUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class PhotoGalleryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);

        setupAppBar();
        setupViews();
        loadGallery();
    }

    private void setupAppBar() {
        ImageView backBtn = findViewById(R.id.appbarBack);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(v -> finish());

        TextView titleView = findViewById(R.id.appbarTitle);
        titleView.setText(R.string.photo_gallery);
        FontUtils.applyBoldFont(this, titleView);
    }

    private void setupViews() {
        recyclerView = findViewById(R.id.galleryRecyclerView);
        progressBar = findViewById(R.id.galleryProgressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadGallery() {
        progressBar.setVisibility(View.VISIBLE);

        String json = CacheManager.loadData(this);
        AppData appData = CacheManager.parseData(json);

        progressBar.setVisibility(View.GONE);

        if (appData != null && appData.gallery != null && !appData.gallery.isEmpty()) {
            PhotoGalleryAdapter adapter = new PhotoGalleryAdapter(this, appData.gallery);
            recyclerView.setAdapter(adapter);
        } else {
            Snackbar.make(recyclerView, R.string.no_data, Snackbar.LENGTH_LONG).show();
        }
    }
}
