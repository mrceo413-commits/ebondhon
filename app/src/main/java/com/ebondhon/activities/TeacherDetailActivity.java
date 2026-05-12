package com.ebondhon.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ebondhon.R;
import com.ebondhon.models.Teacher;
import com.ebondhon.utils.FontUtils;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherDetailActivity extends AppCompatActivity {

    private Teacher teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_detail);

        String teacherJson = getIntent().getStringExtra("teacher_json");
        if (teacherJson != null) {
            teacher = new Gson().fromJson(teacherJson, Teacher.class);
        }

        setupAppBar();

        if (teacher != null) {
            setupTeacherInfo();
            setupDetailRows();
            setupActionButtons();
        }
    }

    private void setupAppBar() {
        ImageView backBtn = findViewById(R.id.appbarBack);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(v -> finish());

        TextView titleView = findViewById(R.id.appbarTitle);
        titleView.setText(teacher != null ? teacher.name : "");
        FontUtils.applyBoldFont(this, titleView);
    }

    private void setupTeacherInfo() {
        CircleImageView photo = findViewById(R.id.detailPhoto);
        TextView nameBn = findViewById(R.id.detailNameBn);
        TextView nameEn = findViewById(R.id.detailNameEn);
        TextView designation = findViewById(R.id.detailDesignation);

        if (teacher.photoUrl != null && !teacher.photoUrl.isEmpty()) {
            Glide.with(this)
                    .load(teacher.photoUrl)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.ic_logo)
                    .into(photo);
        }

        nameBn.setText(teacher.name != null ? teacher.name : "");
        nameEn.setText(teacher.nameEn != null ? teacher.nameEn : "");
        designation.setText(teacher.designation != null ? teacher.designation : "");

        FontUtils.applyBoldFont(this, nameBn);
        FontUtils.applyFont(this, designation);
    }

    private void setupDetailRows() {
        LinearLayout container = findViewById(R.id.detailRowsContainer);

        addDetailRow(container, getString(R.string.label_id), teacher.idNo);
        addDetailRow(container, getString(R.string.label_this_college_joining), teacher.thisCollegeJoining);
        addDetailRow(container, getString(R.string.label_govt_joining), teacher.govtJoining);
        addDetailRow(container, getString(R.string.label_current_designation_joining), teacher.thisDesignationJoining);
        addDetailRow(container, getString(R.string.label_birth_date), teacher.birthDate);
        addDetailRow(container, getString(R.string.label_blood_group), teacher.bloodGroup);
        addDetailRow(container, getString(R.string.label_current_address), teacher.addressCurrent);
        addDetailRow(container, getString(R.string.label_permanent_address), teacher.addressPermanent);
    }

    private void addDetailRow(LinearLayout container, String label, String value) {
        if (value == null || value.isEmpty()) return;

        View row = LayoutInflater.from(this).inflate(R.layout.detail_row, container, false);
        TextView labelView = row.findViewById(R.id.rowLabel);
        TextView valueView = row.findViewById(R.id.rowValue);

        labelView.setText(label);
        valueView.setText(value);

        FontUtils.applyFont(this, labelView);
        FontUtils.applyFont(this, valueView);

        container.addView(row);
    }

    private void setupActionButtons() {
        MaterialButton btnCall = findViewById(R.id.btnCall);
        MaterialButton btnEmail = findViewById(R.id.btnEmail);
        MaterialButton btnFacebook = findViewById(R.id.btnFacebook);

        btnCall.setOnClickListener(v -> {
            if (teacher.phone != null && !teacher.phone.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + teacher.phone));
                startActivity(intent);
            }
        });

        btnEmail.setOnClickListener(v -> {
            if (teacher.email != null && !teacher.email.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + teacher.email));
                startActivity(intent);
            }
        });

        btnFacebook.setOnClickListener(v -> {
            if (teacher.facebookLink != null && !teacher.facebookLink.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(teacher.facebookLink));
                startActivity(intent);
            }
        });
    }
}
