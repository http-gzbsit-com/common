package com.baoshen.common.cache;

public class BytesCache extends Cache<byte[]> {
    BytesCache() {
        super();
    }
    BytesCache(int maxSize) {
        //20M内存
        super(20000000);
    }
    BytesCache(int maxSize, long expirationTime) {
        super(maxSize, expirationTime);
    }
    @Override
    protected long updateCurrentSize() {
        long size = 0;
        for (CacheItem item : mMap.values()) {
            size += getItemSize(item);
        }
        mCurrentSize = size;
        return mCurrentSize;
    }
    @Override
    protected long getItemSize(CacheItem item){
        if(item!=null && item.value!=null){
            return ((byte[])item.value).length;
        }
        return 0;
    }
}
