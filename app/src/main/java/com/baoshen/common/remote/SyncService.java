package com.baoshen.common.remote;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shute on 2016/12/28.
 */
public class SyncService<TResult,TWaitObject> {
    public SyncService() {
        executes = new ArrayList<>(4);
    }

    List<ISynExecute<TResult,TWaitObject>> executes;

    public boolean add(ISynExecute<TResult,TWaitObject> execute) {
//        synchronized(executes) {
            if (executes.contains(execute)) {
                return false;
            }
            executes.add(execute);
//        }
        return true;
    }

    public boolean remove(ISynExecute<TResult,TWaitObject> execute){
        boolean result = false;
//        synchronized(executes) {
            result = executes.remove(execute);
//        }
        return result;
    }

    public ISynExecute<TResult,TWaitObject> receive(@NotNull TWaitObject obj) {
        ISynExecute<TResult, TWaitObject> retVal = null;
        if (executes.size() > 0) {
//            synchronized (executes) {
                if (executes.size() > 0) {
//                    for (ISynExecute<? extends TResult,? extends TWaitObject> item :executes){
                    for (ISynExecute<TResult, TWaitObject> item : executes) {
                        if (item.notify(obj)) {
                            retVal = item;
                            break;
                        }
                    }
                }
//            }
        }
        return retVal;
    }

    public void clear(){
//        synchronized (executes) {
            executes.clear();
//        }
    }
}
