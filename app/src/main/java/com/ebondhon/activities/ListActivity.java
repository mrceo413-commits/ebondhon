package com.ebondhon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ebondhon.R;
import com.ebondhon.adapters.ListItemAdapter;
import com.ebondhon.adapters.TeacherCardAdapter;
import com.ebondhon.models.AppData;
import com.ebondhon.models.Council;
import com.ebondhon.models.Department;
import com.ebondhon.models.Teacher;
import com.ebondhon.utils.CacheManager;
import com.ebondhon.utils.FontUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyText;
    private String type;
    private String title;
    private String extraId;
    private AppData appData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        type = getIntent().getStringExtra("type");
        title = getIntent().getStringExtra("title");
        extraId = getIntent().getStringExtra("extraId");

        setupAppBar();
        setupViews();
        loadData();
    }

    private void setupAppBar() {
        ImageView backBtn = findViewById(R.id.appbarBack);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(v -> finish());

        TextView titleView = findViewById(R.id.appbarTitle);
        titleView.setText(title != null ? title : "");
        FontUtils.applyBoldFont(this, titleView);
    }

    private void setupViews() {
        recyclerView = findViewById(R.id.listRecyclerView);
        progressBar = findViewById(R.id.listProgressBar);
        emptyText = findViewById(R.id.listEmptyText);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);

        String json = CacheManager.loadData(this);
        appData = CacheManager.parseData(json);

        progressBar.setVisibility(View.GONE);

        if (appData == null) {
            emptyText.setVisibility(View.VISIBLE);
            return;
        }

        switch (type != null ? type : "") {
            case "college":
                loadCollegeItems();
                break;
            case "faculty":
                loadFacultyItems();
                break;
            case "council":
                loadCouncilItems();
                break;
            case "phones":
                loadPhoneItems();
                break;
            case "others":
                loadOtherItems();
                break;
            case "department_teachers":
                loadDepartmentTeachers();
                break;
            case "council_members":
                loadCouncilMembers();
                break;
            case "prl_teachers":
                loadPrlTeachers();
                break;
            default:
                emptyText.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void loadCollegeItems() {
        if (appData.college == null || appData.college.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            return;
        }

        List<ListItemAdapter.ListItem> items = new ArrayList<>();
        for (AppData.CollegeItem item : appData.college) {
            items.add(new ListItemAdapter.ListItem(item.id, item.title, "college_article"));
        }

        ListItemAdapter adapter = new ListItemAdapter(this, items, (position, id, itemTitle) -> {
            AppData.CollegeItem collegeItem = appData.college.get(position);
            Intent intent = new Intent(this, ArticleActivity.class);
            intent.putExtra("title", collegeItem.title);
            intent.putExtra("content", collegeItem.content);
            intent.putExtra("imageUrl", collegeItem.imageUrl);
            intent.putExtra("subtitle", "কুমিল্লা ভিক্টোরিয়া সরকারি কলেজ");
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    private void loadFacultyItems() {
        if (appData.faculties == null || appData.faculties.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            return;
        }

        List<ListItemAdapter.ListItem> items = new ArrayList<>();
        for (AppData.Faculty faculty : appData.faculties) {
            if (faculty.departments != null) {
                for (Department dept : faculty.departments) {
                    items.add(new ListItemAdapter.ListItem(dept.id, dept.name, "department"));
                }
            }
        }

        ListItemAdapter adapter = new ListItemAdapter(this, items, (position, id, itemTitle) -> {
            Intent intent = new Intent(this, ListActivity.class);
            intent.putExtra("type", "department_teachers");
            intent.putExtra("title", itemTitle);
            intent.putExtra("extraId", id);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    private void loadCouncilItems() {
        if (appData.councils == null || appData.councils.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            return;
        }

        List<ListItemAdapter.ListItem> items = new ArrayList<>();
        for (Council council : appData.councils) {
            items.add(new ListItemAdapter.ListItem(council.id, council.name, "council"));
        }

        ListItemAdapter adapter = new ListItemAdapter(this, items, (position, id, itemTitle) -> {
            Intent intent = new Intent(this, ListActivity.class);
            intent.putExtra("type", "council_members");
            intent.putExtra("title", itemTitle);
            intent.putExtra("extraId", id);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    private void loadPhoneItems() {
        if (appData.phones == null || appData.phones.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            return;
        }

        List<ListItemAdapter.ListItem> items = new ArrayList<>();
        for (AppData.PhoneItem phone : appData.phones) {
            String label = phone.name;
            if (phone.designation != null && !phone.designation.isEmpty()) {
                label += " (" + phone.designation + ")";
            }
            label += "\n" + phone.phone;
            items.add(new ListItemAdapter.ListItem(phone.phone, label, "phone"));
        }

        ListItemAdapter adapter = new ListItemAdapter(this, items, (position, id, itemTitle) -> {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(android.net.Uri.parse("tel:" + id));
            startActivity(dialIntent);
        });
        recyclerView.setAdapter(adapter);
    }

    private void loadOtherItems() {
        List<ListItemAdapter.ListItem> items = new ArrayList<>();
        items.add(new ListItemAdapter.ListItem("about", "কলেজ সম্পর্কে", "college_article"));
        items.add(new ListItemAdapter.ListItem("history", "ইতিহাস", "college_article"));
        items.add(new ListItemAdapter.ListItem("mission", "লক্ষ্য ও উদ্দেশ্য", "college_article"));

        ListItemAdapter adapter = new ListItemAdapter(this, items, (position, id, itemTitle) -> {
            if (appData.college != null) {
                for (AppData.CollegeItem collegeItem : appData.college) {
                    if (id.equals(collegeItem.id)) {
                        Intent intent = new Intent(this, ArticleActivity.class);
                        intent.putExtra("title", collegeItem.title);
                        intent.putExtra("content", collegeItem.content);
                        intent.putExtra("imageUrl", collegeItem.imageUrl);
                        startActivity(intent);
                        return;
                    }
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void loadDepartmentTeachers() {
        if (appData.teachers == null || extraId == null) {
            emptyText.setVisibility(View.VISIBLE);
            return;
        }

        List<Teacher> departmentTeachers = new ArrayList<>();
        for (Teacher teacher : appData.teachers) {
            if (extraId.equals(teacher.departmentId)) {
                departmentTeachers.add(teacher);
            }
        }

        if (departmentTeachers.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            return;
        }

        TeacherCardAdapter adapter = new TeacherCardAdapter(this, departmentTeachers, teacher -> {
            Intent intent = new Intent(this, TeacherDetailActivity.class);
            intent.putExtra("teacher_json", new Gson().toJson(teacher));
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    private void loadCouncilMembers() {
        if (appData.councils == null || extraId == null) {
            emptyText.setVisibility(View.VISIBLE);
            return;
        }

        for (Council council : appData.councils) {
            if (extraId.equals(council.id) && council.members != null) {
                List<ListItemAdapter.ListItem> items = new ArrayList<>();
                for (Council.CouncilMember member : council.members) {
                    String label = member.name;
                    if (member.designation != null) {
                        label += "\n" + member.designation;
                    }
                    items.add(new ListItemAdapter.ListItem("", label, "member"));
                }

                ListItemAdapter adapter = new ListItemAdapter(this, items, (position, id, itemTitle) -> {
                    // Council members are display-only
                });
                recyclerView.setAdapter(adapter);
                return;
            }
        }

        emptyText.setVisibility(View.VISIBLE);
    }

    private void loadPrlTeachers() {
        if (appData.prl == null || extraId == null) {
            emptyText.setVisibility(View.VISIBLE);
            return;
        }

        for (AppData.PrlItem prlItem : appData.prl) {
            if (extraId.equals(prlItem.year) && prlItem.teachers != null) {
                TeacherCardAdapter adapter = new TeacherCardAdapter(this, prlItem.teachers, teacher -> {
                    Intent intent = new Intent(this, TeacherDetailActivity.class);
                    intent.putExtra("teacher_json", new Gson().toJson(teacher));
                    startActivity(intent);
                });
                recyclerView.setAdapter(adapter);
                return;
            }
        }

        emptyText.setVisibility(View.VISIBLE);
    }
}
