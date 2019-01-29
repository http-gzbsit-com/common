package com.baoshen.common.event;

/**
 * Created by Shute on 2017/11/1.
 */
public class Event {
    protected boolean isStop;
    protected Object data;
    protected Object result;
    protected EventType type;
    protected Level level;

    public Event(EventType type,Level level, Object data) {
        this.data = data;
        this.type = type;
        this.level = level;
    }

    public Event(EventType type,Level level){
        this(type, level,null);
    }
    public Event(EventType type){
        this(type,Level.Common,null);
    }

    /**
     * 中断事件传播
     * @param  result 事件返回值
     * @return
     */
    public void stop(Object result) {
        isStop = true;
        this.result = result;
    }
    public void stop(){
        this.stop(null);
    }

    public boolean isStop() {
        return isStop;
    }

    public Level getLevel(){
        return level;
    }

    /**
     * 消息类型
     */
    public EventType getType(){return type;}

    public Object getData() {
        return data;
    }

    public Object getResult() {
        return result;
    }
}
