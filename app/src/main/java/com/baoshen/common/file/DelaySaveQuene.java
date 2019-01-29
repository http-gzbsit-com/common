package com.baoshen.common.file;

import com.baoshen.common.Log;

import java.util.ArrayList;
import java.util.List;

public class DelaySaveQuene {
    private DelaySaveQuene(){
        list = new ArrayList<>();
        lockObj = new Object();
    }
    static {
        instance = new DelaySaveQuene();
    }
    private static final DelaySaveQuene instance;
    private static final long DELAY = 1000;//延时多久保存
    private List<IDelaySave> list;
    private Thread thread;
    private Object lockObj;

    public static void add(IDelaySave delaySave){
        instance.addAndStart(delaySave);
    }
    public static void clear(){
        instance.clearAll();
    }
    private void addAndStart(IDelaySave delaySave) {
        if (list.contains(delaySave)) {
            return;
        }
        synchronized (lockObj) {
            list.add(delaySave);
        }
        start();
    }
    private void start() {
        if (thread != null && thread.isAlive()) {
            return;
        }
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException ex) {
                    Log.e(ex);
                }
                doSave();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
    private void doSave() {
        if (list.size() == 0) return;
        int length = list.size();
        synchronized (lockObj) {
            for (int i = length - 1; i > -1; i--) {
                IDelaySave item = list.get(i);
                try {
                    item.delaySave();
                } catch (Exception ex) {
                    Log.e(ex);
                }
                list.remove(item);
            }
        }
    }

    private void clearAll(){
        synchronized (lockObj){
            list.clear();
        }
    }
}
