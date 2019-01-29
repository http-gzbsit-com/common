package com.baoshen.common.models;

import java.util.Map;

public class SocketResult {
    //状态行
    private String statusline;
    //消息报头
    private Map<String,String> headers;
    //相应正文
    private byte[] body;
    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getStatusline() {
        return statusline;
    }

    public void setStatusline(String statusline) {
        this.statusline = statusline;
    }
}
