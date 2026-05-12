package com.ebondhon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ebondhon.R;
import com.ebondhon.utils.FontUtils;

import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position, String id, String title);
    }

    private final Context context;
    private final List<ListItem> items;
    private final OnItemClickListener listener;

    public ListItemAdapter(Context context, List<ListItem> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItem item = items.get(position);
        holder.titleText.setText(item.title);
        FontUtils.applyFont(context, holder.titleText);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position, item.id, item.title);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        ImageView chevronIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.listItemTitle);
            chevronIcon = itemView.findViewById(R.id.listItemChevron);
        }
    }

    public static class ListItem {
        public String id;
        public String title;
        public String type;

        public ListItem(String id, String title, String type) {
            this.id = id;
            this.title = title;
            this.type = type;
        }
    }
}
