package com.ebondhon.models;

public class MenuItem {
    private String title;
    private int iconResId;
    private String color;

    public MenuItem(String title, int iconResId, String color) {
        this.title = title;
        this.iconResId = iconResId;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getColor() {
        return color;
    }
}
