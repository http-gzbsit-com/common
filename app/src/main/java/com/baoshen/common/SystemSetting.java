package com.baoshen.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Locale;

/**
 * 系统设置(预留接口)
 * Created by Shute on 2016/9/8.
 */
public class SystemSetting extends Setting{
    protected static final String KEY_THEME = "Theme";
    protected static final String KEY_FULLSCREEN = "Fullscreen";
    protected static final String KEY_LANGUAGE = "Language";

    protected static final String KEY_USE_COUNT = "UseCount";

    public static final String LanguageChinese = Locale.CHINESE.toString();
    public static final String LanguageEnglish = Locale.ENGLISH.toString();
    public static final String LanguageFrench = Locale.FRENCH.toString();//法语
    public static final String LanguageGerman = Locale.GERMAN.toString();//德语
    public static final String LanguageSpanish = "es";//西班牙语
    public static final String LanguagePortuguese = "pt";//葡萄牙语
    public static final String[] Languages = new String[]{LanguageChinese,LanguageEnglish,LanguageFrench,
            LanguageGerman,LanguageSpanish,LanguagePortuguese};


    public SystemSetting(Context context) {
        super(context);
    }

    @Override
    protected SharedPreferences createPreferences(Context context){
        return context.getSharedPreferences("SystemSetting", Context.MODE_PRIVATE);
    }

    public String getTheme() {
        return getString(KEY_THEME, "");
    }

    public boolean setTheme(String value){
        assert !TextUtils.isEmpty(value);
        setString(KEY_THEME,value);
        return commitChanges();
    }

    public boolean isFullscreen() {
        return getBoolean(KEY_FULLSCREEN, false);
    }

    public boolean setFullscreen(boolean value) {
        setBoolean(KEY_FULLSCREEN, value);
        return commitChanges();
    }

    public String getLanguage(){
        String language = getString(KEY_LANGUAGE,null);
        if(TextUtils.isEmpty(language)){
            Locale systemLanguage =  Locale.getDefault();
            if(systemLanguage!=null){
                language = systemLanguage.toString();
                if(language.length()>2){
                    language = language.substring(0,2);
                }
            }
        }
        boolean supported=false;
        if(language!=null) {
            for (String item : Languages) {
                if (item.equals(language)) {
                    supported = true;
                    break;
                }
            }
        }
        if(!supported){
            language = LanguageEnglish;//默认英文
        }
        return language;
    }

    public boolean setLanguage(String value) {
        if(TextUtils.isEmpty(value)) return false;
        setString(KEY_LANGUAGE, value);
        return commitChanges();
    }

    public int  getUseCount(){
        return getInt(KEY_USE_COUNT,0);
    }

    public boolean setUseCount(int value){
        assert value>0;
        setInt(KEY_USE_COUNT,value);
        return commitChanges();
    }
    public void reset() {
        if (BuildConfig.DEBUG) {
            setInt(KEY_USE_COUNT, 0);
            commitChanges();
        }
    }
}
