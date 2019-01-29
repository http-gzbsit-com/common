package com.baoshen.common.file;

import android.content.Context;

import com.baoshen.common.Log;

import java.io.File;
import java.lang.reflect.Method;

/**
 * this class can be seen as mirror of android.os.storage.StorageVolume :
 * Description of a storage volume and its capabilities, including the
 * filesystem path where it may be mounted.
 * ref::https://blog.csdn.net/liuxu0703/article/details/53897702
 */
public class RefStorageVolume {

    private static final int INIT_FLAG_STORAGE_ID = 0x01 << 0;
    private static final int INIT_FLAG_DESCRIPTION_ID = 0x01 << 1;
    private static final int INIT_FLAG_PATH = 0x01 << 2;
    private static final int INIT_FLAG_PRIMARY = 0x01 << 3;
    private static final int INIT_FLAG_REMOVABLE = 0x01 << 4;
    private static final int INIT_FLAG_EMULATED = 0x01 << 5;
    private static final int INIT_FLAG_ALLOW_MASS_STORAGE = 0x01 << 6;
    private static final int INIT_FLAG_MTP_RESERVE_SPACE = 0x01 << 7;
    private static final int INIT_FLAG_MAX_FILE_SIZE = 0x01 << 8;
    private int mInitFlags = 0x00;

    private int mStorageId;
    private int mDescriptionId;
    private File mPath;
    private boolean mPrimary;
    private boolean mRemovable;
    private boolean mEmulated;
    private boolean mAllowMassStorage;
    private int mMtpReserveSpace;
    /** Maximum file size for the storage, or zero for no limit */
    private long mMaxFileSize;

    private Class<?> class_StorageVolume =
            Class.forName("android.os.storage.StorageVolume");
    private Object instance;

    RefStorageVolume(Object obj) throws ClassNotFoundException {
        if (!class_StorageVolume.isInstance(obj)) {
            throw new IllegalArgumentException(
                    "obj not instance of StorageVolume");
        }
        instance = obj;
    }

    public void initAllFields() throws Exception {
        getPathFile();
        getDescriptionId();
        getStorageId();
        isPrimary();
        isRemovable();
        isEmulated();
        allowMassStorage();
        getMaxFileSize();
        getMtpReserveSpace();
    }

    /**
     * Returns the mount path for the volume.
     * @return the mount path
     * @throws Exception
     */
    public String getPath() throws Exception {
        File pathFile = getPathFile();
        if (pathFile != null) {
            return pathFile.toString();
        } else {
            return null;
        }
    }

    public File getPathFile() throws Exception {
        if ((mInitFlags & INIT_FLAG_PATH) == 0) {
            Class<?>[] argTypes = new Class[0];
            Method method = class_StorageVolume.getDeclaredMethod(
                    "getPathFile", argTypes);
            Object[] args = new Object[0];
            Object obj = method.invoke(instance, args);
            mPath = (File) obj;
            mInitFlags &= INIT_FLAG_PATH;
        }
        return mPath;
    }

    /**
     * Returns a user visible description of the volume.
     * @return the volume description
     * @throws Exception
     */
    public String getDescription(Context context) throws Exception {
        int resId = getDescriptionId();
        if (resId != 0) {
            return context.getResources().getString(resId);
        } else {
            return null;
        }
    }

    public int getDescriptionId() throws Exception {
        if ((mInitFlags & INIT_FLAG_DESCRIPTION_ID) == 0) {
            Class<?>[] argTypes = new Class[0];
            Method method = class_StorageVolume.getDeclaredMethod(
                    "getDescriptionId", argTypes);
            Object[] args = new Object[0];
            Object obj = method.invoke(instance, args);
            mDescriptionId = (Integer) obj;
            mInitFlags &= INIT_FLAG_DESCRIPTION_ID;
        }
        return mDescriptionId;
    }

    public boolean isPrimary() throws Exception {
        if ((mInitFlags & INIT_FLAG_PRIMARY) == 0) {
            Class<?>[] argTypes = new Class[0];
            Method method = class_StorageVolume.getDeclaredMethod(
                    "isPrimary", argTypes);
            Object[] args = new Object[0];
            Object obj = method.invoke(instance, args);
            mPrimary = (Boolean) obj;
            mInitFlags &= INIT_FLAG_PRIMARY;
        }
        return mPrimary;
    }

    /**
     * Returns true if the volume is removable.
     * @return is removable
     */
    public boolean isRemovable() throws Exception {
        if ((mInitFlags & INIT_FLAG_REMOVABLE) == 0) {
            Class<?>[] argTypes = new Class[0];
            Method method = class_StorageVolume.getDeclaredMethod(
                    "isRemovable", argTypes);
            Object[] args = new Object[0];
            Object obj = method.invoke(instance, args);
            mRemovable = (Boolean) obj;
            mInitFlags &= INIT_FLAG_REMOVABLE;
        }
        return mRemovable;
    }

    /**
     * Returns true if the volume is emulated.
     * @return is removable
     */
    public boolean isEmulated() throws Exception {
        if ((mInitFlags & INIT_FLAG_EMULATED) == 0) {
            Class<?>[] argTypes = new Class[0];
            Method method = class_StorageVolume.getDeclaredMethod(
                    "isEmulated", argTypes);
            Object[] args = new Object[0];
            Object obj = method.invoke(instance, args);
            mEmulated = (Boolean) obj;
            mInitFlags &= INIT_FLAG_EMULATED;
        }
        return mEmulated;
    }

    /**
     * Returns the MTP storage ID for the volume.
     * this is also used for the storage_id column in the media provider.
     * @return MTP storage ID
     */
    public int getStorageId() throws Exception {
        if ((mInitFlags & INIT_FLAG_STORAGE_ID) == 0) {
            Class<?>[] argTypes = new Class[0];
            Method method = class_StorageVolume.getDeclaredMethod(
                    "getStorageId", argTypes);
            Object[] args = new Object[0];
            Object obj = method.invoke(instance, args);
            mStorageId = (Integer) obj;
            mInitFlags &= INIT_FLAG_STORAGE_ID;
        }
        return mStorageId;
    }

    /**
     * Returns true if this volume can be shared via USB mass storage.
     * @return whether mass storage is allowed
     */
    public boolean allowMassStorage() throws Exception {
        if ((mInitFlags & INIT_FLAG_ALLOW_MASS_STORAGE) == 0) {
            Class<?>[] argTypes = new Class[0];
            Method method = class_StorageVolume.getDeclaredMethod(
                    "allowMassStorage", argTypes);
            Object[] args = new Object[0];
            Object obj = method.invoke(instance, args);
            mAllowMassStorage = (Boolean) obj;
            mInitFlags &= INIT_FLAG_ALLOW_MASS_STORAGE;
        }
        return mAllowMassStorage;
    }

    /**
     * Returns maximum file size for the volume, or zero if it is unbounded.
     * @return maximum file size
     */
    public long getMaxFileSize() throws Exception {
        if ((mInitFlags & INIT_FLAG_MAX_FILE_SIZE) == 0) {
            Class<?>[] argTypes = new Class[0];
            Method method = class_StorageVolume.getDeclaredMethod(
                    "getMaxFileSize", argTypes);
            Object[] args = new Object[0];
            Object obj = method.invoke(instance, args);
            mMaxFileSize = (Long) obj;
            mInitFlags &= INIT_FLAG_MAX_FILE_SIZE;
        }
        return mMaxFileSize;
    }

    /**
     * Number of megabytes of space to leave unallocated by MTP.
     * MTP will subtract this value from the free space it reports back
     * to the host via GetStorageInfo, and will not allow new files to
     * be added via MTP if there is less than this amount left free in the
     * storage.
     * If MTP has dedicated storage this value should be zero, but if MTP is
     * sharing storage with the rest of the system, set this to a positive
     * value
     * to ensure that MTP activity does not result in the storage being
     * too close to full.
     * @return MTP reserve space
     */
    public int getMtpReserveSpace() throws Exception {
        if ((mInitFlags & INIT_FLAG_MTP_RESERVE_SPACE) == 0) {
            Class<?>[] argTypes = new Class[0];
            Method method = class_StorageVolume.getDeclaredMethod(
                    "getMtpReserveSpace", argTypes);
            Object[] args = new Object[0];
            Object obj = method.invoke(instance, args);
            mMtpReserveSpace = (Integer) obj;
            mInitFlags &= INIT_FLAG_MTP_RESERVE_SPACE;
        }
        return mMtpReserveSpace;
    }

    @Override
    public String toString() {
        try {
            final StringBuilder builder = new StringBuilder("RefStorageVolume [");
            builder.append("mStorageId=").append(getStorageId());
            builder.append(" mPath=").append(getPath());
            int descriptionId = -1;
            try{
                descriptionId = getDescriptionId();
            }
            catch (Exception ex){
                Log.e(ex);
            }
            builder.append(" mDescriptionId=").append(descriptionId);
            builder.append(" mPrimary=").append(isPrimary());
            builder.append(" mRemovable=").append(isRemovable());
            builder.append(" mEmulated=").append(isEmulated());
            builder.append(" mMtpReserveSpace=").append(getMtpReserveSpace());
            builder.append(" mAllowMassStorage=").append(allowMassStorage());
            builder.append(" mMaxFileSize=").append(getMaxFileSize());
            builder.append("]");
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
