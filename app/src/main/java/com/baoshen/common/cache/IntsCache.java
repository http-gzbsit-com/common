package com.baoshen.common.cache;

import java.util.ArrayList;
import java.util.List;

public class IntsCache extends Cache<int[]> {
    IntsCache() {
        super();
    }
    IntsCache(int maxSize) {
        //20M内存
        super(20000000);
    }
    IntsCache(int maxSize, long expirationTime) {
        super(maxSize, expirationTime);
    }
    @Override
    protected long updateCurrentSize() {
        long size = 0;
        for (Cache.CacheItem item : mMap.values()) {
            size += getItemSize(item);
        }
        mCurrentSize = size;
        return mCurrentSize;
    }
    @Override
    protected long getItemSize(Cache.CacheItem item){
        if(item!=null && item.value!=null){
            return ((int[])item.value).length;
        }
        return 0;
    }
}
