package com.ebondhon.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ebondhon.R;
import com.ebondhon.models.AppData;
import com.ebondhon.models.ContactInfo;
import com.ebondhon.utils.CacheManager;
import com.ebondhon.utils.FontUtils;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        setupAppBar();
        setupContactRows();
        loadImportantLinks();
    }

    private void setupAppBar() {
        ImageView backBtn = findViewById(R.id.appbarBack);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(v -> finish());

        TextView titleView = findViewById(R.id.appbarTitle);
        titleView.setText("যোগাযোগ");
        FontUtils.applyBoldFont(this, titleView);
    }

    private void setupContactRows() {
        LinearLayout container = findViewById(R.id.contactRowsContainer);

        addContactRow(container, "ওয়েবসাইট", getString(R.string.website_url),
                "#1A237E", getString(R.string.website_url));
        addContactRow(container, "ফেসবুক", getString(R.string.facebook_url),
                "#009688", getString(R.string.facebook_url));
        addContactRow(container, "ফোন", getString(R.string.phone_number),
                "#1A237E", "tel:" + getString(R.string.phone_number));
        addContactRow(container, "ইমেইল", getString(R.string.email_address),
                "#2196F3", "mailto:" + getString(R.string.email_address));
    }

    private void addContactRow(LinearLayout container, String label, String value,
                                String bgColor, String actionUri) {
        View row = LayoutInflater.from(this).inflate(R.layout.contact_row, container, false);

        TextView labelView = row.findViewById(R.id.contactRowLabel);
        TextView valueView = row.findViewById(R.id.contactRowValue);

        labelView.setText(label);
        valueView.setText(value);

        FontUtils.applyFont(this, labelView);
        FontUtils.applyFont(this, valueView);

        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadius(12f);
        bg.setColor(Color.parseColor(bgColor));
        row.setBackground(bg);

        row.setOnClickListener(v -> {
            Intent intent;
            if (actionUri.startsWith("tel:")) {
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse(actionUri));
            } else if (actionUri.startsWith("mailto:")) {
                intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(actionUri));
            } else {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(actionUri));
            }
            startActivity(intent);
        });

        container.addView(row);
    }

    private void loadImportantLinks() {
        LinearLayout linksContainer = findViewById(R.id.linksRowsContainer);

        String json = CacheManager.loadData(this);
        AppData appData = CacheManager.parseData(json);

        if (appData != null && appData.contact != null && appData.contact.importantLinks != null) {
            for (ContactInfo.ImportantLink link : appData.contact.importantLinks) {
                if (link.url == null || link.url.isEmpty()) continue;
                String color = link.color != null ? link.color : "#1A237E";
                addContactRow(linksContainer, link.title != null ? link.title : "", link.url, color, link.url);
            }
        } else {
            addContactRow(linksContainer, "শিক্ষা মন্ত্রণালয়",
                    "https://moedu.gov.bd", "#1A237E", "https://moedu.gov.bd");
            addContactRow(linksContainer, "মাধ্যমিক ও উচ্চশিক্ষা অধিদপ্তর",
                    "https://dshe.gov.bd", "#009688", "https://dshe.gov.bd");
            addContactRow(linksContainer, "জাতীয় বিশ্ববিদ্যালয়",
                    "https://www.nu.ac.bd", "#2196F3", "https://www.nu.ac.bd");
        }
    }
}
