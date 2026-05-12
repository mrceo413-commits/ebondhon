package com.ebondhon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ebondhon.R;
import com.ebondhon.models.Teacher;
import com.ebondhon.utils.FontUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherCardAdapter extends RecyclerView.Adapter<TeacherCardAdapter.ViewHolder> {

    public interface OnTeacherClickListener {
        void onTeacherClick(Teacher teacher);
    }

    private final Context context;
    private final List<Teacher> teachers;
    private final OnTeacherClickListener listener;

    public TeacherCardAdapter(Context context, List<Teacher> teachers, OnTeacherClickListener listener) {
        this.context = context;
        this.teachers = teachers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.teacher_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Teacher teacher = teachers.get(position);

        holder.nameText.setText(teacher.name);
        holder.designationText.setText(teacher.designation);
        FontUtils.applyFont(context, holder.nameText);
        FontUtils.applyFont(context, holder.designationText);

        if (teacher.photoUrl != null && !teacher.photoUrl.isEmpty()) {
            Glide.with(context)
                    .load(teacher.photoUrl)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.ic_logo)
                    .into(holder.photoImage);
        } else {
            holder.photoImage.setImageResource(R.drawable.ic_logo);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTeacherClick(teacher);
            }
        });
    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView photoImage;
        TextView nameText;
        TextView designationText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImage = itemView.findViewById(R.id.teacherPhoto);
            nameText = itemView.findViewById(R.id.teacherName);
            designationText = itemView.findViewById(R.id.teacherDesignation);
        }
    }
}
