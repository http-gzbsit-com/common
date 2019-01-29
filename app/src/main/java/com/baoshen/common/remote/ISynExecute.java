package com.baoshen.common.remote;

/**
 * Created by Shute on 2016/12/28.
 */
public interface ISynExecute<TResult,TWaitObject> {

    /**
     * 等待，直到超时，或者其他线程唤醒它
     */
    void waitOne() throws IllegalMonitorStateException ,IllegalArgumentException,InterruptedException;

    /**
     * 获取超时时间
     * @return 超时时间
     */
    int getTimeout();
    
    /**
     * 唤醒当前对象的线程（如果满足唤醒条件）,要判断是否已经超时
     * @return
     */
    boolean notify(TWaitObject obj);

    /**
     * 执行结果
     * @return
     */
    TResult getResult();
}
