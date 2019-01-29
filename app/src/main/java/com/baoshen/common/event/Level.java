package com.baoshen.common.event;


import android.support.annotation.Keep;

/**
 * Created by Shute on 2017/11/1.
 */
@Keep
public enum Level {
    /**
     * 系统级事件，轻易不要使用
     */
    High(1),
    /*
     * 普通事件
     */
    Common(2),
    /*
     * 琐碎事件
     */
    Low(3);

    private int level;
    private Level(int level){
        this.level = level;
    }
    public int getLevel(){
        return level;
    }
}
