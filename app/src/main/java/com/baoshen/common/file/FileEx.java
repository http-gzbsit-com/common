package com.baoshen.common.file;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.baoshen.common.Log;
import com.baoshen.common.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Shute on 2017/2/5.
 */
public final class FileEx {
    /**
     * read byte up to length
     * @param length read up to length（use -1 to read all）
     * @throws IOException if the stream is closed or another IOException occurs.
     * @return return null if failed
     */
    public static byte[] read(@NotNull InputStream stream, int length)throws IOException {
        if(length==-1) {
            length = stream.available();
        }
        byte[] buff = new byte[length];
        int readCount = 0;
        while (readCount<length) {
            int count = stream.read(buff,readCount,length-readCount);
            if(count>0){
                readCount+=count;
            }
            //流结束
            else if(count ==-1) {
                break;
            }
        }
        return buff;
    }

    /**
     * read byte up to length
     * @param buff
     * @param offset start position of buff to write
     * @param length read up to length
     * @throws IOException if the stream is closed or another IOException occurs.
     */
    public static void read(@NotNull InputStream stream,@NotNull byte[] buff,int offset,int length)throws IOException{
        assert(offset > 0);
        assert(length > 0);
        assert(buff.length>=offset+length);
        int readCount = 0;
        while (readCount<length) {
            int count = stream.read(buff,offset+readCount,length-readCount);
            if(count>0){
                readCount+=count;
            }

            //流结束
            else if(count ==-1) {
                break;
            }
        }
    }

    public static boolean write(@NotNull byte[]data,String filePath){
        return write(data,new File(filePath));
    }
    public static boolean write(@NotNull InputStream inputStream,@NotNull File file) {
        boolean isOk=false;
        FileOutputStream outpuStream = null;
        try{
            outpuStream = new FileOutputStream(file,false);
            byte[] buff = new byte[4096];//4k缓冲
            int buffLength;
            int finishLength=0;
            int total = inputStream.available();
            while (finishLength<total) {
                int readLength = buff.length;
                if (readLength + finishLength > total) {
                    readLength = total - finishLength;
                }
                readLength = inputStream.read(buff, finishLength, readLength);
                finishLength += readLength;
                outpuStream.write(buff,0,readLength);
            }
            outpuStream.flush();
            outpuStream.getFD().sync();
            isOk=true;
        }
        catch (IOException ex){
            Log.e(ex);
        }
        finally {
            if(outpuStream !=null){
                try {
                    outpuStream.close();
                }
                catch (IOException ex){
                    Log.e(ex);
                }
            }
        }
        return isOk;
    }

    public static boolean write(@NotNull byte[] buff,@NotNull File file) {
        FileOutputStream stream = null;
        boolean isOk = false;
        try {
            stream = new FileOutputStream(file, false);
            stream.write(buff);
            stream.flush();
            stream.getFD().sync();
            isOk = true;
        } catch (IOException ex) {
            Log.e(ex);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    Log.e(ex);
                }
            }
        }
        return isOk;
    }

    public static File getExternalDirectory() {
//        <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
        File dir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dir = Environment.getExternalStorageDirectory();
        }
        if (dir == null) {
            try {
                Runtime rt = Runtime.getRuntime();
                Process proc = rt.exec("mount");
                InputStream is = proc.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.contains("extSdCard")) {
                        String[] arr = line.split(" ");
                        String path = arr[1];
                        File file = new File(path);
                        if (file.isDirectory()) {
                            dir = file;
                        }
                    }
                }
                isr.close();
            } catch (Exception e) {
            }
        }
        return dir;
    }

    public static String combinePath(String root,String path,boolean endWithSeparator) {
        String p = combinePath(root, path);
        if (!Utils.isNullOrEmpty(p)) {
            if (endWithSeparator) {
                if(!p.endsWith(File.separator)) {
                    p += File.separator;
                }
            }
            else{
                if(p.endsWith(File.separator)) {
                    p = p.substring(0,p.length()- File.separator.length());
                }
            }
        }
        return p;
    }

    public static String combinePath(String root,String path){
        if(Utils.isNullOrEmpty(root)) return path;
        if(Utils.isNullOrEmpty(path)) return root;
        if(root.endsWith(File.separator)){
            if(!path.startsWith(File.separator)){
                return root + path;
            }
            return root+path.substring(File.separator.length());
        }
        else{
            if(path.startsWith(File.separator)){
                return root + path;
            }
            return root + File.separator + path;
        }
    }
}
