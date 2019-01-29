package com.baoshen.common;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Looper;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Shute on 2016/12/1.
 */
public class Utils {
    private static final int NOTIFICATION_FLAG = 1;
    public static final String TAG = Utils.class.getSimpleName();

    public static boolean isNullOrEmpty(String str) {
        if (str == null) return true;
        return str.length() == 0;
    }

    public static <T> boolean isNullOrEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }

    public static <T> boolean isNullOrEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    //region gson
    public static <T> T jsonParse(String jsonString, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, cls);
        } catch (Exception e) {
            Log.e(TAG, "jsonParse", e);
        }
        return t;
    }

    //region gson
    public static <T> T jsonParse(String jsonString, Type typeOfT) {
        /*example:
        String json = "{'IsSuccess':true,'ErrorMessage':null,'Data':[{'Key':'Version','Value':'{\\\"VersionName\\\":\\\"1.0.4\\\",\\\"VersionCode\\\":\\\"1\\\",\\\"Update\\\":false}'},{'Key':'DeviceType','Value':'QLINE'}]}";
        Type type = new TypeToken<JsonResultVm<List<StringKeyValuePair>>>(){}.getType();
        JsonResultVm<List<StringKeyValuePair>> data = Utils.jsonParse(json, type);
         */
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, typeOfT);
        } catch (Exception e) {
            Log.e(e);
        }
        return t;
    }

    //    public static <T> List<T> jsonParseList(String jsonString, Class<T> cls) {
//        List<T> list = new ArrayList<T>();
//        try {
//            Gson gson = new Gson();
//            list = gson.fromJson(jsonString, new TypeToken<List<cls>>() {
//            }.getType());
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//        return list;
//    }


    public static String stringify(Object value) {
        Gson gson = new Gson();
        String str = gson.toJson(value);
        return str;
    }
    //endregion


    public static void toast(int toast, Context context, boolean isLong) {
        if (context != null) {
            toast(context.getString(toast), context, isLong);
        }
    }

    public static void toast(String toast, Context context) {
        toast(toast, context, true);
    }

    public static void toast(String toast, boolean longDuration) {
        toast(toast, null, longDuration);
    }

    public static void toast(final String toast, @NotNull Context context, final boolean longDuration) {
        if (isMainThread()) {
            Toast.makeText(context, toast, longDuration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
        } else // 非UI主线程
        {
            final Activity activity = (Activity) context;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, toast, longDuration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
                }
            });
        }
        Log.i("toast", toast);
    }

    /**
     * 退出程序
     *
     * @param activity
     * @return
     * @throws
     * @author Shute
     * @time 2016/12/7 8:37
     */
    public static void exit(Activity activity) {
        try {
            if (activity != null) activity.finish();

            android.os.Process.killProcess(android.os.Process.myPid());    //获取PID
            System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
        } catch (Exception ex) {
            Log.e(TAG, "exit", ex);
        }
    }

    /**
     * 建议采用exit(Activity activity)退出
     *
     * @return
     * @throws
     * @author Shute
     * @time 2016/12/7 8:38
     */
    public static void exit() {
        android.os.Process.killProcess(android.os.Process.myPid());    //获取PID
        System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
    }

    //返回桌面
    public static void goHome(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ResolveInfo homeInfo = pm.resolveActivity(new Intent(Intent.ACTION_MAIN)
                    .addCategory(Intent.CATEGORY_HOME), 0);
            ActivityInfo ai = homeInfo.activityInfo;
            Intent startIntent = new Intent(Intent.ACTION_MAIN);
//            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            startIntent.setComponent(new ComponentName(ai.packageName, ai.name));

            context.startActivity(startIntent);
        } catch (Exception ex) {
            Log.e(TAG, "goHome", ex);
        }
    }

    //显示键盘
    public static void showKeyboard(EditText editText, Context context, boolean isShow) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (isShow) {
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            } else {
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        } catch (Exception ex) {
            Log.e(TAG, "showKeyboard", ex);
        }
    }

    public static String getMD5(String val) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(val.getBytes());
            byte[] m = md5.digest();//加密

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < m.length; i++) {
                sb.append(m[i]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "getMD5", e);
        }
        return null;
    }

    // 计算文件的 MD5 值
    public static String getMD5(File file) {
        if (file == null || !file.isFile() || !file.exists()) {
            return "";
        }
        FileInputStream in = null;
        String result = "";
        byte buffer[] = new byte[8192];
        int len;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                md5.update(buffer, 0, len);
            }
            byte[] bytes = md5.digest();

            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 获取时间(累计毫秒数)
     *
     * @return
     */
    public static long getTime() {
        return System.currentTimeMillis();
    }

    public static String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    public static String substring(String str, int start, int length) {
        return str.substring(start, start + length);
    }

    public static String substring(String str, int start) {
        return str.substring(start);
    }

    public static String toHex(int num) {
        switch (num) {
            case 0:
                return "00000000";
            case 1:
                return "00000001";
        }
        char[] chs = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] arr = new char[8];
        int pos = arr.length;
        while (num != 0) {
            arr[--pos] = chs[num & 15];
            num = num >>> 4;
        }
        return String.valueOf(arr);
    }

    /**
     * [获取cpu型号]
     */
    public static String getCpuArchitecture() {
        String cpu = "";
        try {
            InputStream is = new FileInputStream("/proc/cpuinfo");
            InputStreamReader ir = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(ir);
            try {
                cpu = br.readLine();
            } finally {
                br.close();
                ir.close();
                is.close();
            }
        } catch (Exception e) {
            Log.e(e);
        }

        return cpu;
    }

    //判断当前是不是主线程
    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    public static boolean gotoActivity(Class<? extends Activity> newActivity, Context context) {
        try {
            Intent intent = new Intent();
            intent.setClass(context, newActivity);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(e);
            return false;
        }
        return true;
    }

    //是否处于横屏
    public static boolean isLandscape(@NotNull Activity activity) {
        boolean isLandscape = activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        return isLandscape;
    }

    public static void copyToClipboard(final String text, @NotNull Activity activity) {
        try {
            ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
            // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
            ClipData clipData = ClipData.newPlainText(null, text);
            // 把数据集设置（复制）到剪贴板
            clipboard.setPrimaryClip(clipData);
        } catch (Exception ex) {
            Log.e(ex);
        }
    }

    //对url中的参数进行url编码
    public static String GetRealUrl(String str) {
        try {
            int index = str.indexOf("?");
            if (index < 0) return str;
            String query = str.substring(0, index);
            String params = str.substring(index + 1);
            Map map = GetArgs(params);
            String encodeParams = TransMapToString(map);
            return query + "?" + encodeParams;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return "";
    }

    //将url参数格式转化为map
    public static Map GetArgs(String params) throws Exception {
        Map map = new HashMap();
        String[] pairs = params.split("&");
        for (int i = 0; i < pairs.length; i++) {
            int pos = pairs[i].indexOf("=");
            if (pos == -1) continue;
            String argname = pairs[i].substring(0, pos);
            String value = pairs[i].substring(pos + 1);
            value = URLEncoder.encode(value, "utf-8");
            map.put(argname, value);
        }
        return map;
    }

    //将map转化为指定的String类型
    public static String TransMapToString(Map map) {
        java.util.Map.Entry entry;
        StringBuffer sb = new StringBuffer();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (java.util.Map.Entry) iterator.next();
            sb.append(entry.getKey().toString()).append("=").append(null == entry.getValue() ? "" :
                    entry.getValue().toString()).append(iterator.hasNext() ? "&" : "");
        }
        return sb.toString();
    }

    //将String类型按一定规则转换为Map
    public static Map TransStringToMap(String mapString) {
        Map map = new HashMap();
        java.util.StringTokenizer items;
        for (StringTokenizer entrys = new StringTokenizer(mapString, "&"); entrys.hasMoreTokens();
             map.put(items.nextToken(), items.hasMoreTokens() ? ((Object) (items.nextToken())) : null))
            items = new StringTokenizer(entrys.nextToken(), "=");
        return map;
    }

}

