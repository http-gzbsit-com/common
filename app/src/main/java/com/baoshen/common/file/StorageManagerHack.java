package com.baoshen.common.file;


import android.content.Context;
import android.os.storage.StorageManager;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 *
 * author: liuxu
 * date: 2014-10-27
 *
 * There are some useful methods in StorageManager, like:
 * StorageManager.getVolumeList()
 * StorageManager.getVolumeState()
 * StorageManager.getVolumePaths()
 * But for now these methods are not visible in SDK (marked as \@hide).
 * one requirement for these methods is to get secondary storage or
 * OTG disk info.
 *
 * here we use java reflect mechanism to retrieve these methods and data.
 *
 * ref:https://blog.csdn.net/liuxu0703/article/details/53897702
 */
public final class StorageManagerHack  {

    public static StorageManager getStorageManager(Context cxt) {
        StorageManager sm = (StorageManager)
                cxt.getSystemService(Context.STORAGE_SERVICE);
        return sm;
    }

    /**
     * Returns list of all mountable volumes.
     * list elements are RefStorageVolume, which can be seen as
     * mirror of android.os.storage.StorageVolume
     * return null on error.
     *
     * @param cxt
     * @return
     */
    public static RefStorageVolume[] getVolumeList(Context cxt) {
        if (!isSupportApi()) {
            return null;
        }
        StorageManager sm = getStorageManager(cxt);
        if (sm == null) {
            return null;
        }

        try {
            Class<?>[] argTypes = new Class[0];
            Method method_getVolumeList =
                    StorageManager.class.getMethod("getVolumeList", argTypes);
            Object[] args = new Object[0];
            Object array = method_getVolumeList.invoke(sm, args);
            int arrLength = Array.getLength(array);
            RefStorageVolume[] volumes = new
                    RefStorageVolume[arrLength];
            for (int i = 0; i < arrLength; i++) {
                volumes[i] = new RefStorageVolume(Array.get(array, i));
            }
            return volumes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns list of paths for all mountable volumes.
     * return null on error.
     */
    public static String[] getVolumePaths(Context cxt) {
        if (!isSupportApi()) {
            return null;
        }
        StorageManager sm = getStorageManager(cxt);
        if (sm == null) {
            return null;
        }

        try {
            Class<?>[] argTypes = new Class[0];
            Method method_getVolumeList =
                    StorageManager.class.getMethod("getVolumePaths", argTypes);
            Object[] args = new Object[0];
            Object array = method_getVolumeList.invoke(sm, args);
            int arrLength = Array.getLength(array);
            String[] paths = new
                    String[arrLength];
            for (int i = 0; i < arrLength; i++) {
                paths[i] = (String) Array.get(array, i);
            }
            return paths;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the state of a volume via its mountpoint.
     * return null on error.
     */
    public static String getVolumeState(Context cxt, String mountPoint) {
        if (!isSupportApi()) {
            return null;
        }
        StorageManager sm = getStorageManager(cxt);
        if (sm == null) {
            return null;
        }

        try {
            Class<?>[] argTypes = new Class[1];
            argTypes[0] = String.class;
            Method method_getVolumeList =
                    StorageManager.class.getMethod("getVolumeState", argTypes);
            Object[] args = new Object[1];
            args[0] = mountPoint;
            Object obj = method_getVolumeList.invoke(sm, args);
            String state = (String) obj;
            return state;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get primary volume of the device.
     *
     * @param cxt
     * @return RefStorageVolume can be seen as mirror of
     * android.os.storage.StorageVolume
     */
    public static RefStorageVolume getPrimaryVolume(Context cxt) {
        RefStorageVolume[] volumes = getVolumeList(cxt);
        if (volumes == null) {
            return null;
        }
        for (RefStorageVolume volume : volumes) {
            try {
                if (volume.isPrimary()) {
                    return volume;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * see if SDK version of current device is greater
     * than 14 (IceCreamSandwich, 4.0).
     */
    private static boolean isSupportApi() {
        int osVersion = android.os.Build.VERSION.SDK_INT;
        boolean avail = osVersion >= 14;
        return avail;
    }
}