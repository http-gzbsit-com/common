package com.baoshen.common.cache;

public class CacheManager {
    private static BytesCache bytesCache;
    private static IntsCache intsCache;

    public static synchronized BytesCache getBytesCache(){
        if(bytesCache==null) {
            bytesCache = new BytesCache();
        }
        return bytesCache;
    }
    public static synchronized IntsCache getIntsCache(){
        if(intsCache==null) {
            intsCache = new IntsCache();
        }
        return intsCache;
    }
    public static synchronized void clear() {
        if (bytesCache != null) {
            bytesCache.clear();
        }
        if (intsCache != null) {
            intsCache.clear();
        }
    }
}
