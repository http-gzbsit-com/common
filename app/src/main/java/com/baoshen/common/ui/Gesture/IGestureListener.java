package com.baoshen.common.ui.Gesture;

public interface IGestureListener {
    void onTap(int x,int y);
    void onZoom(float newZoom,float oldZoom);
    //todo 继续拓展
}
