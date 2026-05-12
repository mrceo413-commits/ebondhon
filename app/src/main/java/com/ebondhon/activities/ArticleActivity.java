package com.ebondhon.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ebondhon.R;
import com.ebondhon.utils.FontUtils;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        String title = getIntent().getStringExtra("title");
        String subtitle = getIntent().getStringExtra("subtitle");
        String content = getIntent().getStringExtra("content");
        String imageUrl = getIntent().getStringExtra("imageUrl");

        setupAppBar(title);
        setupContent(title, subtitle, content, imageUrl);
    }

    private void setupAppBar(String title) {
        ImageView backBtn = findViewById(R.id.appbarBack);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(v -> finish());

        TextView titleView = findViewById(R.id.appbarTitle);
        titleView.setText(title != null ? title : "");
        FontUtils.applyBoldFont(this, titleView);
    }

    private void setupContent(String title, String subtitle, String content, String imageUrl) {
        TextView subtitleView = findViewById(R.id.articleSubTitle);
        TextView titleView = findViewById(R.id.articleTitle);
        TextView bodyView = findViewById(R.id.articleBody);
        ImageView imageView = findViewById(R.id.articleImage);

        if (subtitle != null && !subtitle.isEmpty()) {
            subtitleView.setText(subtitle);
            subtitleView.setVisibility(View.VISIBLE);
        } else {
            subtitleView.setVisibility(View.GONE);
        }

        if (title != null) {
            titleView.setText(title);
        }

        if (content != null) {
            bodyView.setText(content);
        }

        FontUtils.applyFont(this, subtitleView);
        FontUtils.applyBoldFont(this, titleView);
        FontUtils.applyFont(this, bodyView);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            imageView.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.loading)
                    .into(imageView);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }
}
