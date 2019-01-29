package com.baoshen.common.event;


import android.support.annotation.Keep;

/**
 * Created by Shute on 2017/11/1.
 */
@Keep
public interface IEventReceiver {
    void onEvent(Object sender, Event event, boolean isAsync);
}