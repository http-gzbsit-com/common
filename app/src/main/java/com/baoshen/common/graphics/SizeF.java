package com.baoshen.common.graphics;

/**
 * Created by Shute on 2018/9/11.
 */
public class SizeF {
    public SizeF(final float width, final float height) {
        this.width = width;
        this.height = height;
    }

    private float width;
    private float height;

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
    public void setWidth(float value){
        width = value;
    }
    public void setHeight(float value){
        height = value;
    }
}
