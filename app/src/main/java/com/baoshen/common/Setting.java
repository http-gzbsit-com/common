package com.baoshen.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 设置(适合存储少量的配置数据)
 * Created by Shute on 2016/9/8.
 */
public abstract class Setting {
    public static final String TAG = Setting.class.getSimpleName();
    private SharedPreferences preferences = null;
    SharedPreferences.Editor editor = null;
    protected Context context;

    public Setting(@NotNull Context context) {
        this.context = context;
        preferences = createPreferences(context);
        editor = preferences.edit();
    }

    @NotNull
    protected abstract SharedPreferences createPreferences(Context context);

    protected String getString(String key,String defValue) {
        String value = preferences.getString(key, defValue);
        return value;
    }
    protected void setString(String key,String value){
        try {
            editor.putString(key, value);
        }
        catch (Exception ex){
            Log.e(TAG,"SetString",ex);
        }
    }

    protected int getInt(String key,int defValue) {
        int value = preferences.getInt(key, defValue);
        return value;
    }
    protected void setInt(String key,int value){
        try {
            editor.putInt(key, value);
        }
        catch (Exception ex){
            Log.e(TAG,"SetInt",ex);
        }
    }

    protected float getFloat(String key,float defValue) {
        float value = preferences.getFloat(key, defValue);
        return value;
    }
    protected void setFloat(String key,float value){
        try {
            editor.putFloat(key, value);
        }
        catch (Exception ex){
            Log.e(TAG,"SetFloat",ex);
        }
    }

    protected boolean getBoolean(String key,boolean defValue){
        boolean value = preferences.getBoolean(key,defValue);
        return value;
    }
    protected void setBoolean(String key,boolean value){
        try {
            editor.putBoolean(key, value);
        }
        catch (Exception ex){
            Log.e(TAG,"setBoolean",ex);
        }
    }

    protected Long getLong(String key,Long defValue){
        Long value = preferences.getLong(key,defValue);
        return value;
    }
    protected void setLong(String key,Long value){
        try {
            editor.putLong(key, value);
        }
        catch (Exception ex){
            Log.e(TAG,"setLong",ex);
        }
    }

    protected <T> List<T> getList(String key){
        String stringify = preferences.getString(key, "");
        if(!TextUtils.isEmpty(stringify)){
            Type type = new TypeToken<List<T>>(){}.getType();
            List<T> value = Utils.jsonParse(stringify, type);
            return value;
        }
        return null;
    }

    protected <T> void setList(String key,List<T> list){
        try {
            String stringify = Utils.stringify(list);
            editor.putString(key, stringify);
        }
        catch (Exception ex){
            Log.e(TAG,"setList",ex);
        }
    }
    protected boolean contains(String key){
        return preferences.contains(key);
    }
    protected boolean commitChanges(){
        return editor.commit();
    }
}
