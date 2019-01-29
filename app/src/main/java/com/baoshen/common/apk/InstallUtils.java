package com.baoshen.common.apk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import com.baoshen.common.file.FileUtils;

import java.io.File;

public class InstallUtils {

    //判断是否具有安装权限
    public static boolean checkPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return context.getPackageManager().canRequestPackageInstalls();
        } else {
            return true;
        }
    }

    public static boolean install(File apk, Context context) {
        if (checkPermission(context)) {
            if (FileUtils.view(apk, context, FileUtils.ContentTypeApk)) {
                return true;
            }
        }
        return false;
    }

    /**
     * android 8后，需要去设置允许安装位置应用
     * @author Shute
     * @param resultCode onActivityResult需要用这个编码来识别消息
     * @time 2018/12/13 16:20
     */
//    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void requestInstall(Activity activity,int resultCode ) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            Uri packageUri = Uri.parse("package:" + activity.getPackageName());
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageUri);
            activity.startActivityForResult(intent, resultCode);
        }
    }
}
