package com.baoshen.common;

import android.content.Context;

/**
 * Created by Shute on 2016/12/1.
 */
public class GlobalContext {
    private static boolean gScreenOn;
    private static boolean gIsForeground;

    private static String gStartPackage = null;
    private static long uiThreadId=-1;


    public static String getPackageName(Context context) {
        if (gStartPackage != null) {
            return gStartPackage;
        }
        return loadPackageName(context);
    }

    synchronized private static String loadPackageName(Context context) {
        if (gStartPackage == null) {
            if (context != null) {
                gStartPackage = context.getPackageName();
            }
        }
        return gStartPackage;
    }

    //region 锁屏+前后台

    public static void setIsForeground(boolean value){
        gIsForeground = value;
    }

    /**
     * activity是否显示在前台
     * @throws
     * @author Shute
     * @time 2016/12/7 8:53
     * @return
     */
    public static boolean getIsForeground(){
        return gIsForeground;
    }
    /**
     * 标记当前屏幕状态(逻辑状态，非正真实现锁屏功能)
     * @param  value
     * @throws 
     * @author Shute
     * @time 2016/12/1 11:28
     * @return
     */
    public static void setScreenOn(boolean value) {
        gScreenOn = value;
    }

    public static boolean getScreenOn(){
        return gScreenOn;
    }

    public static boolean getVisible(){
        return gIsForeground && gScreenOn;
    }


    //endregion
}
