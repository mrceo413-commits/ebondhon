package com.ebondhon.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebondhon.R;
import com.ebondhon.models.MenuItem;
import com.ebondhon.utils.FontUtils;

import java.util.List;

public class MenuGridAdapter extends BaseAdapter {

    private final Context context;
    private final List<MenuItem> menuItems;

    public MenuGridAdapter(Context context, List<MenuItem> menuItems) {
        this.context = context;
        this.menuItems = menuItems;
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public MenuItem getItem(int position) {
        return menuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false);
            holder = new ViewHolder();
            holder.iconImage = convertView.findViewById(R.id.menuIcon);
            holder.titleText = convertView.findViewById(R.id.menuTitle);
            holder.iconBackground = convertView.findViewById(R.id.iconBackground);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MenuItem item = menuItems.get(position);
        holder.iconImage.setImageResource(item.getIconResId());
        holder.titleText.setText(item.getTitle());
        FontUtils.applyFont(context, holder.titleText);

        try {
            int color = Color.parseColor(item.getColor());
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.OVAL);
            drawable.setColor(color);
            holder.iconBackground.setBackground(drawable);
            holder.iconImage.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView iconImage;
        TextView titleText;
        View iconBackground;
    }
}
