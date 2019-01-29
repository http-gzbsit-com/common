package com.baoshen.common.ui.Gesture;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;

//todo AbsGesture可以做深度重构
public abstract class AbsGesture{
    public AbsGesture(Context context,IGestureListener listener){
        this.listener = listener;
        this.context = context;
    }

    protected IGestureListener listener;
    private Context context;

    public abstract boolean onTouch(MotionEvent event);

    protected int dp2px(int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    //其他单位转换为像素
    public static float unit2Pixel(int unit, float value,Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(unit,value,metrics);
    }

    public static float getDistance(float p1x,float p1y,float p2x,float p2y) {
        float distance = Math.round(Math.sqrt(Math.pow(p1x-p2x,2)+Math.pow(p1y-p2y,2)));
        return distance;
    }
}
