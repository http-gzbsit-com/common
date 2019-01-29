package com.baoshen.common;

/**
 * Created by Shute on 2017/1/6.
    尽量不修改此类的方法，跟系统Log的接口保持一致
 */
public class Log {

    //0级，不输出任何Log
    public static final int LEVEL_NONE = 10;
    //1级，危险信息(异常)
    public static final int LEVEL_ERROR =  android.util.Log.ERROR;
    //2级，重要信息
    public static final int LEVEL_HIGH =  android.util.Log.WARN;
    //3级，普通信息
    public static final int LEVEL_COMMON = android.util.Log.INFO;
    //4级，所有信息
    public static final int LEVEL_LOW = android.util.Log.VERBOSE;

    //配置Log输出级别
    public static int level = LEVEL_LOW;

    public static void i(String tag,String msg){
        i(tag,msg,LEVEL_COMMON);
    }
    public static void i(String tag,String msg,int level){
        if(check(level)) {
            android.util.Log.i(tag, msg);
        }
    }
    public static void i(Class classOfT,String msg){
        i(classOfT,msg,LEVEL_COMMON);
    }
    public static void i(Class classOfT,String msg,int level){
        if(check(level)){
            i(classOfT.getSimpleName(),msg);
        }
    }
    public static void v(String tag,String msg){
        v(tag,msg,LEVEL_COMMON);
    }
    public static void v(Class classOfT,String msg) {
        v(classOfT,msg,LEVEL_LOW);
    }
    public static void v(String tag,String msg,int level){
        if(check(level)) {
            android.util.Log.v(tag, msg);
        }
    }
    public static void v(Class classOfT,String msg,int level) {
        v(classOfT.getSimpleName(), msg, level);
    }

    public static void d(Class classOfT,String msg) {
        d(classOfT,msg,LEVEL_COMMON);
    }
    public static void d(String tag,String msg){
        d(tag,msg,LEVEL_COMMON);
    }
    public static void d(Class classOfT,String msg,int level) {
        d(classOfT.getSimpleName(), msg, level);
    }
    public static void d(String tag,String msg,int level){
        if(check(level)) {
            android.util.Log.d(tag, msg);
        }
    }
    public static void e(String tag,String msg) {
        e(tag, msg, LEVEL_ERROR);
    }
    public static void e(Class classOfT,String msg,Throwable throwable) {
        e(classOfT, msg, throwable);
    }
    public static void e(Throwable throwable) {
        e(throwable,LEVEL_ERROR);
    }
    public static void e(String tag,String msg,Throwable throwable){
        e(tag,msg,throwable,LEVEL_ERROR);
    }
    public static void e(String tag,String msg,int level){
        if(check(level)) {
            android.util.Log.e(tag, msg);
        }
    }
    public static void e(Class classOfT,String msg,Throwable throwable,int level) {
        e(classOfT.getSimpleName(), msg, throwable, level);
    }
    public static void e(Throwable throwable,int level) {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        if (stacktrace != null && stacktrace.length > 3) {
            StackTraceElement e = stacktrace[3];
            e(e.getClassName(), e.getMethodName(), throwable, level);
        } else {
            e("[Error]", "[Message]", throwable, level);
        }
    }
    public static void e(String tag,String msg,Throwable throwable,int level){
        if(check(level)) {
            if(throwable==null) {
                android.util.Log.e(tag, msg);
            }
            else{
                android.util.Log.e(tag, msg,throwable);
            }
        }
    }
    public static void w(Class classOfT,String msg) {
        w(classOfT, msg, LEVEL_HIGH);
    }
    public static void w(String tag,String msg){
        w(tag,msg,LEVEL_HIGH);
    }
    public static void w(Class classOfT,String msg,int level) {
        w(classOfT.getSimpleName(), msg, level);
    }
    public static void w(String tag,String msg,int level){
        if(check(level)) {
            android.util.Log.w(tag, msg);
        }
    }

    private static boolean check(int level) {
        return BuildConfig.DEBUG && level >= Log.level;
    }
}
