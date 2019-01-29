package com.baoshen.common.io;


import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class StreamUtils {
    public static byte[] readAll(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while (true) {
            len = inStream.read(buffer);
            if (len > 0) {
                outStream.write(buffer, 0, len);
            } else {
                throw new IOException("stream broken");
            }
            if (inStream.available()<=0)
                break;
        }
        byte[] data=outStream.toByteArray();
        outStream.close();
        return data;
    }

    public static byte[] readAll(String fileName) throws IOException
    {
        InputStream inStream = new FileInputStream(fileName);
        byte[] data=StreamUtils.readAll(inStream);
        inStream.close();
        return data;
    }
}
