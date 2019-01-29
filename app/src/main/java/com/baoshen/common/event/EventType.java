package com.baoshen.common.event;


import android.support.annotation.Keep;

/**
 * Created by Shute on 2017/11/1.
 */
@Keep
public enum EventType {
    SystemLanguageChanged("SystemLanguageChanged"),//系统语言发生变化

    CommonSettingGetLanguage("CommonSettingGetLanguage"),//获取当前语言
    CommonSettingSetLanguage("CommonSettingSetLanguage"),//(设置面板中)设置了语言
    //打开系统设置
    CommonOpenSetting("CommonOpenSetting"),

    //显示或者隐藏设置按钮
    CommonShowSettingButton("CommonShowSettingButton"),

    //加载网页事件
    LowLoadUrl("LowLoadUrl"),
    LowSettingGetRfidPower("LowSettingGetRfidPower"),
    LowSettingSetRfidPower("LowSettingSetRfidPower");

    private String name;
    EventType(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
}
