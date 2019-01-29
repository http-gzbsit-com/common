package com.baoshen.common.graphics;

import android.text.TextUtils;

/**
 * Created by Shute on 2018/8/24.
 */
public class Size implements Comparable<Size> {
    private static final String SPLIT = "×";
    public Size(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    public void setWidth(int value){
        width = value;
    }
    public void setHeight(int value){
        height = value;
    }
    @Override
    public String toString() {
        return width + SPLIT + height;
    }


    public int compareTo(Size o) {
        return width * height - o.width * o.height;
    }

    public static Size valueOf(String text) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        String[] items = text.split(SPLIT);
        if (items != null && items.length == 2) {
            return new Size(Integer.valueOf(items[0]), Integer.valueOf(items[1]));
        }
        return null;
    }
}
