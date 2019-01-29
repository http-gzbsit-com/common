package com.baoshen.common.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//注:保持为abstract,在CacheManager中统一做内存回收
public abstract class Cache <T> {
    protected Cache() {
        this(10,3600000);//一个小时
    }
    protected Cache(int maxSize) {
        this(maxSize,3600000);//一个小时
    }
    //expirationTime 单位毫秒
    protected Cache(int maxSize,long expirationTime) {
        mMaxSize = maxSize;
        mExpirationTime = expirationTime;
        mMap = new HashMap();
    }

    protected int mMaxSize;
    protected Map<String,CacheItem> mMap;
    protected long mExpirationTime;
    protected Comparator comparator;
    protected long mCurrentSize;

    protected long recycle(long releaseSize) {
        assert releaseSize < mMaxSize;
        long collectedSize = 0;
        Set<Map.Entry<String, CacheItem>> entrySet = mMap.entrySet();
        List<String> toReleaseList = new ArrayList<>();
        for (Map.Entry<String, CacheItem> entry : entrySet) {
            if (isOverdue(entry.getValue())) {
                toReleaseList.add(entry.getKey());
            }
        }
        if (toReleaseList.size() > 0) {
            for (String key : toReleaseList) {
                CacheItem item = mMap.get(key);
                collectedSize+=getItemSize(item);
                mMap.remove(key);
            }
        }
        if (collectedSize < releaseSize) {
            List<CacheItem> list;
            Collection<CacheItem> coll = mMap.values();
            if (coll instanceof List) {
                list = (List) coll;
            } else {
                list = new ArrayList(coll);
            }
            if (comparator == null) {
                comparator = new Comparator<CacheItem>() {
                    public int compare(CacheItem lhs, CacheItem rhs) {//作升序排序
                        if (lhs.extendedTime == rhs.extendedTime) {
                            return 0;
                        } else if (lhs.extendedTime > rhs.extendedTime) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                };
            }
            Collections.sort(list, comparator);
            for (int i = 0; i < list.size() && collectedSize >= releaseSize; i++) {
                CacheItem item = list.get(i);
                collectedSize+=getItemSize(item);
                mMap.remove(list.get(i).key);
            }
        }
        updateCurrentSize();
        return collectedSize;
    }
    public synchronized boolean put(String key,T value) {
        if (isFull()) {
            recycle(mMaxSize / 3);
        }
        CacheItem item;
        if (mMap.containsKey(key)) {
            item = mMap.get(key);
        } else {
            item = new CacheItem();
            item.key = key;
            mMap.put(key, item);
        }
        item.extendedTime = System.currentTimeMillis() + mExpirationTime;
        item.value = value;
        updateCurrentSize();
        return true;
    }
    public synchronized T get(String key) {
        if (mMap.containsKey(key)) {
            CacheItem item = mMap.get(key);
            if (item != null) {
                if (isOverdue(item)) {
                    mMap.remove(key);
                    return null;
                } else {
                    return (T)item.value;
                }
            }
        }
        return null;
    }

    public synchronized int clear(){
        int size = mMap.size();
        mMap.clear();
        updateCurrentSize();
        return size;
    }

    public class CacheItem{
        long  extendedTime;
        Object value;
        String key;
    }
    protected boolean isOverdue(CacheItem item) {
        assert item != null;
        return item.extendedTime < System.currentTimeMillis();
    }
    protected boolean isFull() {
        return mCurrentSize >= mMaxSize;
    }
    protected long updateCurrentSize() {
        mCurrentSize = mMap.size();
        return mCurrentSize;
    }
    protected long getItemSize(CacheItem item){
        return 1;
    }
}
