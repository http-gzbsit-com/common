package com.baoshen.common;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Surface;
import android.view.WindowManager;
import android.util.SizeF;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Shute on 2018/8/23.
 */
public class DisplayUtils {
    public static SizeF getScreenPixelSize(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        return new SizeF(width, height);
    }

    public static SizeF getScreenDipSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int dipWidth = (int) (width / density);  // 屏幕宽度(dp)
        int dipHeight = (int) (height / density);// 屏幕高度(dp)
        return new SizeF(dipWidth, dipHeight);
    }

    public static int getDpi(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.densityDpi;
    }

    //其他单位转换为像素
    public static float unit2Pixel(int unit, float value,Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(unit,value,metrics);
    }

    //获得屏幕方向（0、90、180、270）,0°是正常竖屏，90°是设备向左倾倒；270°是向右倾倒
    public static int getScreenOrientation(@NotNull Activity activity) {
        int screenOrientation = activity.getWindowManager().getDefaultDisplay().getRotation();
        switch (screenOrientation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
            default:
                throw new IllegalArgumentException();
        }
    }
}
