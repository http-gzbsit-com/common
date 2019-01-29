package com.baoshen.common;

import android.app.Application;
import android.content.Context;

/**
 * Created by Shute on 2017/2/6.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
    }
}
