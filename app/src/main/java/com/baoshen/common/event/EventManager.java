package com.baoshen.common.event;

import android.os.Handler;
import android.support.annotation.Keep;

import com.baoshen.common.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 消息级别分为：高级消息、普通消息、初级消息
 * 接受者分为：高级：接受所有消息，并优先获得消息处理权；中级：接受普通消息跟初级消息，处理权次于高级；初级：只接受初级消息，优先权最低
 * 高级接受者不允许超过3个
 * 消息可以被中断(stop)
 */
@Keep
public class EventManager {
    private Lock lock;
    private static EventManager instance;
    private EventHandler handler;
    List<IEventReceiver> highReceiverList;
    List<IEventReceiver> commonReceiverList;
    List<IEventReceiver> lowReceiverList;

    private EventManager() {
        lock = new ReentrantLock();
        handler = new EventHandler();
        highReceiverList = new ArrayList(1);
        commonReceiverList = new ArrayList();
        lowReceiverList = new ArrayList();
    }

    public static void init() {
        assert Utils.isMainThread() : "需要在主线程构造";
        if (instance == null) {
            instance = new EventManager();
        }
    }

    public static void attach(IEventReceiver receiver, Level level) {
        assert instance == null : "需要先调用init() ";
        instance.attachPrivate(receiver, level);
    }

    public static void detach(IEventReceiver receiver) {
        assert instance == null : "需要先调用init() ";
        instance.detachPrivate(receiver);
    }

    public static void async(Object sender, Event event) {
        assert instance == null : "需要先调用init() ";
        instance.asyncPrivate(sender, event);
    }

    public static void sync(Object sender, Event event) {
        assert instance == null : "需要先调用init() ";
        instance.syncPrivate(sender, event);
    }

    public static void destroy() {
        assert instance == null : "需要先调用init() ";
        instance.destroyPrivate();
        instance = null;
    }

    private void destroyPrivate() {
        try {
            lock.lock();
            highReceiverList.clear();
            commonReceiverList.clear();
            lowReceiverList.clear();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 添加监听者
     */
    private void attachPrivate(IEventReceiver receiver, Level level) {
        List<IEventReceiver> list = getList(level);
        //高级Receiver不应该超过3个
        if (level == Level.High) {
            if (list.size() > 3) {
                throw new IllegalArgumentException("");
            }
        }
        try {
            lock.lock();
            if (!list.contains(receiver)) {
                list.add(receiver);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 移除监听者
     */
    private void detachPrivate(IEventReceiver receiver) {
        try {
            lock.lock();
            if (highReceiverList.contains(receiver)) {
                highReceiverList.remove(receiver);
            }
            if (commonReceiverList.contains(receiver)) {
                commonReceiverList.remove(receiver);
            }
            if (lowReceiverList.contains(receiver)) {
                lowReceiverList.remove(receiver);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 异步消息
     *
     * @param sender
     * @param event
     * @return
     */
    private void asyncPrivate(final Object sender, final Event event) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                send(sender, event, true);
            }
        });
    }

    private void syncPrivate(Object sender, Event event) {
        send(sender, event, false);
    }

    private void send(Object sender, Event event, boolean isAsync) {
        try {
            lock.lock();
            switch (event.getLevel()) {
                case High:
                    send(sender, event, isAsync, highReceiverList);
                    break;
                case Common:
                    if (send(sender, event, isAsync, highReceiverList)) {
                        send(sender, event, isAsync, commonReceiverList);
                    }
                    break;
                case Low:
                    if (send(sender, event, isAsync, highReceiverList)) {
                        if (send(sender, event, isAsync, commonReceiverList)) {
                            send(sender, event, isAsync, lowReceiverList);
                        }
                    }
                    break;
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * @return 返回false时，表示被中断了
     */
    private boolean send(Object sender, Event event, boolean isAsync, List<IEventReceiver> list) {
        if (list.size() == 0) return true;

        for (IEventReceiver receiver : list) {
            //不捕获接受者的异常
            receiver.onEvent(sender, event, isAsync);
            //消息被中断了
            if (event.isStop()) {
                return false;
            }
        }
        return true;
    }

    private List<IEventReceiver> getList(Level level) {
        switch (level) {
            case High:
                return highReceiverList;
            case Common:
                return commonReceiverList;
            case Low:
                return lowReceiverList;
            default:
                throw new UnsupportedOperationException();
        }
    }

    static class EventHandler extends Handler {
    }
}
