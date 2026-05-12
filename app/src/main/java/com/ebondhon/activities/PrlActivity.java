package com.ebondhon.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ebondhon.R;
import com.ebondhon.models.AppData;
import com.ebondhon.utils.CacheManager;
import com.ebondhon.utils.FontUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class PrlActivity extends AppCompatActivity {

    private GridView prlGridView;
    private List<AppData.PrlItem> prlItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prl);

        setupAppBar();
        setupGrid();
        loadData();
    }

    private void setupAppBar() {
        ImageView backBtn = findViewById(R.id.appbarBack);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(v -> finish());

        TextView titleView = findViewById(R.id.appbarTitle);
        titleView.setText(R.string.prl_title);
        FontUtils.applyBoldFont(this, titleView);
    }

    private void setupGrid() {
        prlGridView = findViewById(R.id.prlGridView);
    }

    private void loadData() {
        String json = CacheManager.loadData(this);
        AppData appData = CacheManager.parseData(json);

        if (appData != null && appData.prl != null && !appData.prl.isEmpty()) {
            prlItems = appData.prl;
            PrlYearAdapter adapter = new PrlYearAdapter();
            prlGridView.setAdapter(adapter);

            prlGridView.setOnItemClickListener((parent, view, position, id) -> {
                AppData.PrlItem item = prlItems.get(position);
                Intent intent = new Intent(this, ListActivity.class);
                intent.putExtra("type", "prl_teachers");
                intent.putExtra("title", "পি.আর.এল - " + (item.yearBn != null ? item.yearBn : item.year));
                intent.putExtra("extraId", item.year);
                startActivity(intent);
            });
        } else {
            Snackbar.make(prlGridView, R.string.no_data, Snackbar.LENGTH_LONG).show();
        }
    }

    private class PrlYearAdapter extends BaseAdapter {

        private final String[] colors = {"#1A237E", "#009688"};

        @Override
        public int getCount() {
            return prlItems != null ? prlItems.size() : 0;
        }

        @Override
        public AppData.PrlItem getItem(int position) {
            return prlItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(PrlActivity.this)
                        .inflate(R.layout.prl_year_item, parent, false);
            }

            AppData.PrlItem item = prlItems.get(position);
            TextView yearText = convertView.findViewById(R.id.prlYearText);

            String displayYear = item.yearBn != null ? item.yearBn : item.year;
            yearText.setText(displayYear);

            String color = colors[position % colors.length];
            yearText.setBackgroundColor(Color.parseColor(color));

            FontUtils.applyBoldFont(PrlActivity.this, yearText);

            return convertView;
        }
    }
}
