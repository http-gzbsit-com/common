package com.baoshen.common.file;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.baoshen.common.Log;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Shute on 2018/8/29.
 */
public class FileUtils {
    private final static String TAG = FileUtils.class.getSimpleName();

    public final static String ContentTypeImage = "image/*";
    public final static String ContentTypeApk = "application/vnd.android.package-archive";
    public final static String ContentTypePdf = "application/pdf";
    public final static String ContentTypeText = "text/plain";
    public final static String ContentTypeAudio = "audio/*";
    public final static String ContentTypeVideo = "video/*";

    public final static String AppFilesDir = "files";
    public final static String AppCachehDir = "cache";

    /**
     * 获取应用专属缓存目录(优先采用external存储)
     * android 4.4及以上系统不需要申请SD卡读写权限
     * 因此也不用考虑6.0系统动态申请SD卡读写权限问题，切随应用被卸载后自动清空 不会污染用户存储空间
     *
     * @param context 上下文
     * @param type    文件夹类型 可以为空，为空则返回API得到的一级目录.可选(cache/files/...)
     * @return 缓存文件夹 如果没有SD卡或SD卡有问题则返回内存缓存目录，否则优先返回SD卡缓存目录
     */
    public static File getAppDirectory(Context context, String type) {
        File appDir = null;
        appDir = getAppExternalDirectory(context, type);
        if (appDir == null) {
            appDir = getAppInternalDirectory(context, type);
        }
        if (appDir == null) {
            Log.e(TAG, "getCacheDirectory fail ,the reason is mobile phone unknown exception !");
        } else {
            if (!appDir.exists() && !appDir.mkdirs()) {
                Log.e(TAG, "getCacheDirectory fail ,the reason is make directory fail !");
            }
        }
        return appDir;
    }

    /**
     * App 外部存储去
     *
     * @param context 上下文
     * @param type    为空时采用cache,可选参数:cache/files/...
     * @return 缓存目录文件夹 或 null（无SD卡或SD卡挂载失败）
     */
    public static File getAppExternalDirectory(Context context, String type) {
        File appCacheDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (TextUtils.isEmpty(type) || AppCachehDir.equalsIgnoreCase(type)) {
                appCacheDir = context.getExternalCacheDir();
            } else if (AppFilesDir.equalsIgnoreCase(type)) {
                appCacheDir = context.getExternalFilesDir("");
            } else {
                appCacheDir = context.getExternalFilesDir(type);
            }

            if (appCacheDir == null) {// 有些手机需要通过自定义目录
                appCacheDir = new File(Environment.getExternalStorageDirectory(), "Android/data/" + context.getPackageName() + "/cache/" + type);
            }
            if (appCacheDir == null) {
                Log.e(TAG, "getExternalDirectory fail ,the reason is sdCard unknown exception !");
            } else {
                if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
                    Log.e(TAG, "getExternalDirectory fail ,the reason is make directory fail !");
                }
            }
        } else {
            Log.e(TAG, "getExternalDirectory fail ,the reason is sdCard nonexistence or sdCard mount fail !");
        }
        return appCacheDir;
    }

    /**
     * 获取SD卡缓存目录
     *
     * @param folder  根目录的文件夹
     *                {@link android.os.Environment#DIRECTORY_MUSIC},
     *                {@link android.os.Environment#DIRECTORY_PODCASTS},
     *                {@link android.os.Environment#DIRECTORY_RINGTONES},
     *                {@link android.os.Environment#DIRECTORY_ALARMS},
     *                {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
     *                {@link android.os.Environment#DIRECTORY_PICTURES}, or
     *                {@link android.os.Environment#DIRECTORY_MOVIES}.or 自定义文件夹名称
     * @return 缓存目录文件夹 或 null（无SD卡或SD卡挂载失败）
     */
    public static File getExternalDirectory(String folder) {
        File external = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (TextUtils.isEmpty(folder)) {
                external = Environment.getExternalStorageDirectory();
            } else {
                external = new File(Environment.getExternalStorageDirectory(), folder);
            }
            if (external == null) {
                Log.e(TAG, "getExternalDirectory fail ,the reason is sdCard unknown exception !");
            } else {
                if (!external.exists() && !external.mkdirs()) {
                    Log.e(TAG, "getExternalDirectory fail ,the reason is make directory fail !");
                }
            }
        } else {
            Log.e(TAG, "getExternalDirectory fail ,the reason is sdCard nonexistence or sdCard mount fail !");
        }
        return external;
    }

    /**
     * App内部私有目录
     *
     * @param type 子目录(cache/files)
     * @return 缓存目录文件夹 或 null（创建目录文件失败）
     * 注：该方法获取的目录是能供当前应用自己使用，外部应用没有读写权限，如 系统相机应用
     */
    public static File getAppInternalDirectory(Context context, String type) {
        File appDir = null;
        if (TextUtils.isEmpty(type) || AppCachehDir.equalsIgnoreCase(type)) {
            appDir = context.getCacheDir();// /data/data/app_package_name/cache
        } else if (AppFilesDir.equalsIgnoreCase(type)) {
            appDir = context.getFilesDir();// /data/data/app_package_name/files/type
        } else {
            appDir = new File(context.getFilesDir(), type);
        }

        if (!appDir.exists() && !appDir.mkdirs()) {
            Log.e(TAG, "getAppInternalDirectory fail ,the reason is make directory fail !");
        }
        return appDir;
    }

    public static File getDownloadDir() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!dir.exists()) {
            try {
                dir.mkdirs();
            } catch (Exception ex) {
                Log.e(ex);
                dir = null;
                return null;
            }
        }
        return dir;
    }

    public static boolean isExist(String filePath) {
        File file = new File(filePath);
        boolean isExist = file.exists();
        return isExist;
    }

    //如果文件路径重复，则重命名为"文件(1).txt"之类的方式
    public static String getRepeatName(@NotNull String filePath) {
        assert filePath.length() > 0;
        if (!isExist(filePath)) return filePath;

        //出现重名，要重命名
        //先获取拓展名
        String extName = "";
        int index = filePath.lastIndexOf(".");
        if (index > 0 && filePath.length() - index < 6) {
            extName = filePath.substring(index);
        }
        index = filePath.lastIndexOf("/");
        if (index == -1) index = filePath.lastIndexOf("\\");
        if (index == -1) throw new IllegalArgumentException();
        String dirPath = filePath.substring(0, index + 1);
        //不好后缀名的文件名称
        String fileName = filePath.substring(index + 1);
        if (extName.length() > 0) {
            fileName = fileName.substring(0, fileName.length() - extName.length());
        }
        String newPath = null;
        int count = 1;
        while (true) {
            newPath = dirPath + fileName + "(" + Integer.toString(count) + ")" + extName;
            if (!isExist(newPath)) {
                break;
            }
            count++;
        }
        return newPath;
    }

    public static File write(byte[] data, File file, boolean canRename) {

        if (file.exists()) {
            if (canRename) {
                try {
                    String newPath = getRepeatName(file.getCanonicalPath());
                    file = new File(newPath);
                } catch (IOException ex) {
                    Log.e(ex);
                }
            } else {
                //如果要实现覆盖，最好是先删除原文件或者对原文件重命名，不要直接覆盖，可能原先的文件长度比新写入的要长
                throw new UnsupportedOperationException("文件重名");
            }
        }
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (IOException ex) {
            Log.e(ex);
        }
        return file;
    }

    //控制权限
    public static boolean chmod(File file, String limits) {
        String path = file.getAbsolutePath();
        String[] command = {"chmod", limits, path};
        ProcessBuilder builder = new ProcessBuilder(command);
        try {
            builder.start();
            return true;
        } catch (IOException e) {
            Log.e(e);
            return false;
        }
    }

    //控制权限
    public static boolean chmod(File file) {
        return chmod(file, "777");
    }

    //浏览文件夹
    public static boolean viewFolder(File file, Context context) {
        //额外加多一个斜杠
        File dir = file.isDirectory() ? file : file.getParentFile();
        try {
            Uri uri = createContentUri(dir, context);
            if (uri != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, Intent.CATEGORY_DEFAULT);
                context.startActivity(intent);
                return true;
            }
        } catch (Exception ex) {
            Log.e(ex);
        }
        return false;
    }

    //打开指定格式的文件()
    public static boolean view(File file, Context context, String contentType) {
        try {
            Uri uri = createContentUri(file, context);
            if (uri != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, contentType);
                context.startActivity(intent);
                return true;
            }
        } catch (Exception ex) {
            Log.e(ex);
        }
        return false;
    }

    //创建Uri指向文件(兼容FileProvider模式)
    public static Uri createContentUri(File file, Context context) {
        Uri uri = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
            } else {
                uri = Uri.fromFile(file);
            }
        } catch (Exception ex) {
            Log.e(ex);
        }
        return uri;
    }

    /**
     * 获取磁盘可用空间(单位:byte)
     *
     * @param isExternal 是否外部sd卡
     * @author Shute
     * @time 2018/12/14 7:48
     */
    public static long getAvailableSize(boolean isExternal) {
        String state = Environment.getExternalStorageState();
        long size = 0;
        //注:未处理多sd卡的情况
        if (isExternal) {
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File sdcardDir = Environment.getExternalStorageDirectory();
                StatFs sf = new StatFs(sdcardDir.getPath());
                long blockSize = sf.getBlockSizeLong();
                long availCount = sf.getAvailableBlocksLong();
                size = blockSize * availCount;
                Log.d(TAG, "可用的block数目：:" + availCount + ",剩余空间:" + availCount * blockSize / 1024 + "KB");
            }
        } else {
            File root = Environment.getRootDirectory();
            StatFs sf = new StatFs(root.getPath());
            long blockSize = sf.getBlockSizeLong();
            long availCount = sf.getAvailableBlocksLong();
            size = blockSize * availCount;
            Log.d(TAG, "可用的block数目：:" + availCount + ",可用大小:" + availCount * blockSize / 1024 + "KB");
        }
        return size;
    }
}
