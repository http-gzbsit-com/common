package com.baoshen.common.web;

import android.preference.PreferenceActivity;
import android.text.TextUtils;

import com.baoshen.common.Log;
import com.baoshen.common.io.StreamUtils;
import com.baoshen.common.models.SocketResult;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Shute on 2018/7/13.
 */

public class SocketHttpRequester {
    public static String TAG = SocketHttpRequester.class.getSimpleName();

    /**
     * 直接通过HTTP协议提交数据到服务器,实现如下面表单提交功能:
     * <FORM METHOD=POST ACTION="http://192.168.1.101:8083/upload/servlet/UploadServlet" enctype="multipart/form-data">
     * <INPUT TYPE="text" NAME="name">
     * <INPUT TYPE="text" NAME="id">
     * <input type="file" name="imagefile"/>
     * <input type="file" name="zip"/>
     * </FORM>
     *
     * @param path   上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.iteye.cn或http://192.168.1.101:8083这样的路径测试)
     * @param params 请求参数 key为参数名,value为参数值
     */
    public static SocketResult post(String path, Map<String, String> params, FormFile[] files) throws Exception {
        SocketResult socketResult=new SocketResult();
        final String BOUNDARY = "---------------------------7da2137580612"; //数据分隔线
        final String endline = "--" + BOUNDARY + "--\r\n";//数据结束标志

        int fileDataLength = 0;
        for (FormFile uploadFile : files) {//得到文件类型数据的总长度
            StringBuilder fileExplain = new StringBuilder();
            fileExplain.append("--");
            fileExplain.append(BOUNDARY);
            fileExplain.append("\r\n");
            fileExplain.append("Content-Disposition: form-data;name=\"" + uploadFile.getParameterName() + "\";filename=\"" + uploadFile.getFilname() + "\"\r\n");
            fileExplain.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
            fileExplain.append("\r\n");
            fileDataLength += fileExplain.length();
            if (uploadFile.getInStream() != null) {
                fileDataLength += uploadFile.getFile().length();
            } else {
                fileDataLength += uploadFile.getData().length;
            }
        }
        StringBuilder textEntity = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {//构造文本类型参数的实体数据
            textEntity.append("--");
            textEntity.append(BOUNDARY);
            textEntity.append("\r\n");
            textEntity.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
            textEntity.append(entry.getValue());
            textEntity.append("\r\n");
        }
        //计算传输给服务器的实体数据总长度
        int dataLength = textEntity.toString().getBytes().length + fileDataLength + endline.getBytes().length;

        URL url = new URL(path);
        int port = url.getPort() == -1 ? 80 : url.getPort();
        Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
        OutputStream outStream = socket.getOutputStream();
        //下面完成HTTP请求头的发送
        String requestmethod = "POST " + url.getPath() + " HTTP/1.1\r\n";
        outStream.write(requestmethod.getBytes());
        String charset = "Accept-Charset: utf-8\n";
        outStream.write(charset.getBytes());
        //临时性改为要求pb格式
        String accept = "Accept: application/pb, */*\r\n";
        outStream.write(accept.getBytes());
        String language = "Accept-Language: zh-CN\r\n";
        outStream.write(language.getBytes());
        String contenttype = "Content-Type: multipart/form-data; boundary=" + BOUNDARY + "\r\n";
        outStream.write(contenttype.getBytes());
        String contentlength = "Content-Length: " + dataLength + "\r\n";
        outStream.write(contentlength.getBytes());
        String alive = "Connection: Keep-Alive\r\n";
        outStream.write(alive.getBytes());
        String host = "Host: " + url.getHost() + ":" + port + "\r\n";
        outStream.write(host.getBytes());
        //写完HTTP请求头后根据HTTP协议再写一个回车换行
        outStream.write("\r\n".getBytes());
        //把所有文本类型的实体数据发送出来
        outStream.write(textEntity.toString().getBytes());
        //把所有文件类型的实体数据发送出来
        for (FormFile uploadFile : files) {
            StringBuilder fileEntity = new StringBuilder();
            fileEntity.append("--");
            fileEntity.append(BOUNDARY);
            fileEntity.append("\r\n");
            fileEntity.append("Content-Disposition: form-data;name=\"" + uploadFile.getParameterName() + "\";filename=\"" + uploadFile.getFilname() + "\"\r\n");
            fileEntity.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
            outStream.write(fileEntity.toString().getBytes());
            byte[] fileBytes = uploadFile.getData();
            int writtenLength = 0;
            int batchLength = 10240;
            int i = 1;
            while (writtenLength < fileBytes.length) {
                if (fileBytes.length - writtenLength < batchLength) {
                    batchLength = fileBytes.length - writtenLength;
                }
                outStream.write(fileBytes, writtenLength, batchLength);
                writtenLength += batchLength;
            }
            if (uploadFile.getInStream() != null) {
                uploadFile.getInStream().close();
            }
            outStream.write("\r\n".getBytes());
        }
        //下面发送数据结束标志，表示数据已经结束
        outStream.write(endline.getBytes());
        outStream.flush();
        InputStream inputStream = socket.getInputStream();
        byte[] data = StreamUtils.readAll(inputStream);
        inputStream.close();
        if (data == null || data.length <= 0) {
            Log.e(TAG, "获取服务器返回的数据失败");
            return null;
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        BufferedReader reader = new BufferedReader(new InputStreamReader(byteArrayInputStream));
        String startLine = reader.readLine();
        socketResult.setStatusline(startLine);
        Map<String,String> heads=new HashMap<String, String>();
        while (true) {
            String line = reader.readLine();
            if (TextUtils.isEmpty(line)) {
                break;
            }
            String[] lineSplit=line.split(":");
            heads.put( lineSplit[0],lineSplit[1].trim());

        }

        byte[] contentBuff=null;
        if (!TextUtils.isEmpty(heads.get("Content-Length"))) {
            int contentLength =Integer.parseInt(Objects.requireNonNull(heads.get("Content-Length")));
            contentBuff = new byte[contentLength];
            System.arraycopy(data, data.length - contentLength, contentBuff, 0, contentLength);
        }
        reader.close();
        byteArrayInputStream.close();
        outStream.close();
        socket.close();
        socketResult.setHeaders(heads);
        socketResult.setBody(contentBuff);
        return socketResult;
    }

    /**
     * 提交数据到服务器
     *
     * @param path   上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.itcast.cn或http://192.168.1.10:8080这样的路径测试)
     * @param params 请求参数 key为参数名,value为参数值
     * @param file   上传文件
     */
    public static SocketResult post(String path, Map<String, String> params, FormFile file) throws Exception {
        return post(path, params, new FormFile[]{file});
    }
}
