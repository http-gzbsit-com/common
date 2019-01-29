package com.baoshen.common;

/**
 * Created by Shute on 2018/9/13.
 */
public enum ActivityState {
    Unknow(0),
    Created(1),
    Started(4),
    Resume(8),
    Running(16),
    Paused(32),
    Stop(64),
    Destroy(128);

    private int code;
    private ActivityState(int value){
        code = value;
    }
    public int getCode(){
        return code;
    }
}
