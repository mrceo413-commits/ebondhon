package com.ebondhon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ebondhon.R;
import com.ebondhon.adapters.MenuGridAdapter;
import com.ebondhon.models.MenuItem;
import com.ebondhon.utils.FontUtils;
import com.ebondhon.viewmodels.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private GridView menuGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupAppBar();
        setupMenu();
        setupViewModel();
    }

    private void setupAppBar() {
        ImageView backBtn = findViewById(R.id.appbarBack);
        backBtn.setVisibility(View.GONE);

        TextView title = findViewById(R.id.appbarTitle);
        title.setText(R.string.app_name);
        FontUtils.applyBoldFont(this, title);
    }

    private void setupMenu() {
        menuGridView = findViewById(R.id.menuGridView);

        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("কলেজ", R.drawable.ic_college, "#00BCD4"));
        menuItems.add(new MenuItem("অন্যান্য", R.drawable.ic_others, "#E91E63"));
        menuItems.add(new MenuItem("পরিষদ", R.drawable.ic_council, "#9C27B0"));
        menuItems.add(new MenuItem("গুরুত্বপূর্ণ টেলিফোন নাম্বার", R.drawable.ic_phone, "#9E9E9E"));
        menuItems.add(new MenuItem("অনুষদ", R.drawable.ic_faculty, "#795548"));
        menuItems.add(new MenuItem("ফটো গ্যালারি", R.drawable.ic_gallery, "#4CAF50"));
        menuItems.add(new MenuItem("পি.আর.এল", R.drawable.ic_prl, "#FF9800"));
        menuItems.add(new MenuItem("যোগাযোগ", R.drawable.ic_contact, "#607D8B"));

        MenuGridAdapter adapter = new MenuGridAdapter(this, menuItems);
        menuGridView.setAdapter(adapter);

        menuGridView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent;
            switch (position) {
                case 0: // কলেজ
                    intent = new Intent(this, ListActivity.class);
                    intent.putExtra("type", "college");
                    intent.putExtra("title", "কলেজ");
                    startActivity(intent);
                    break;
                case 1: // অন্যান্য
                    intent = new Intent(this, ListActivity.class);
                    intent.putExtra("type", "others");
                    intent.putExtra("title", "অন্যান্য");
                    startActivity(intent);
                    break;
                case 2: // পরিষদ
                    intent = new Intent(this, ListActivity.class);
                    intent.putExtra("type", "council");
                    intent.putExtra("title", "পরিষদ");
                    startActivity(intent);
                    break;
                case 3: // গুরুত্বপূর্ণ টেলিফোন নাম্বার
                    intent = new Intent(this, ListActivity.class);
                    intent.putExtra("type", "phones");
                    intent.putExtra("title", "গুরুত্বপূর্ণ টেলিফোন নাম্বার");
                    startActivity(intent);
                    break;
                case 4: // অনুষদ
                    intent = new Intent(this, ListActivity.class);
                    intent.putExtra("type", "faculty");
                    intent.putExtra("title", "অনুষদ");
                    startActivity(intent);
                    break;
                case 5: // ফটো গ্যালারি
                    intent = new Intent(this, PhotoGalleryActivity.class);
                    startActivity(intent);
                    break;
                case 6: // পি.আর.এল
                    intent = new Intent(this, PrlActivity.class);
                    startActivity(intent);
                    break;
                case 7: // যোগাযোগ
                    intent = new Intent(this, ContactActivity.class);
                    startActivity(intent);
                    break;
            }
        });
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.getIsOffline().observe(this, isOffline -> {
            if (Boolean.TRUE.equals(isOffline)) {
                Snackbar.make(menuGridView, R.string.offline_mode, Snackbar.LENGTH_LONG).show();
            }
        });

        viewModel.loadData();
    }
}
