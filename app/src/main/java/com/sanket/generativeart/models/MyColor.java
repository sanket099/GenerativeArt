package com.sanket.generativeart.models;

public class MyColor {
    private int color;
    private boolean selected;

    public MyColor(int color, boolean selected) {
        this.color = color;
        this.selected = selected;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

