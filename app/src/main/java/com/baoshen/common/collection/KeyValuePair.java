package com.baoshen.common.collection;

import android.support.annotation.Keep;

/**
 * Created by Shute on 2017/2/9.
 */
@Keep
public class KeyValuePair<TKey,TValue> {
    private TKey Key;
    private TValue Value;

    public TKey getKey() {
        return Key;
    }

    public void setKey(TKey key) {
        Key = key;
    }

    public TValue getValue() {
        return Value;
    }

    public void setValue(TValue value) {
        Value = value;
    }
}
