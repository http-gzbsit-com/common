package com.baoshen.common.ui.Gesture;

import android.content.Context;
import android.util.TypedValue;
import android.view.MotionEvent;

import com.baoshen.common.Log;

public class ZoomGesture extends AbsGesture {
    private static String TAG = ZoomGesture.class.getSimpleName();
    public ZoomGesture(Context context, IGestureListener listener){
        super(context,listener);
        minpointerDistance = Math.round(unit2Pixel(TypedValue.COMPLEX_UNIT_MM,10f,context));
    }
    boolean isGesture;
    float pointer1X;
    float pointer1Y;
    float pointer2X;
    float pointer2Y;
    float pointerDistance;
    float lastZoom = 1.0f;
    int minpointerDistance;//要求两只的最短距离目前假定为1厘米

    @Override
    public boolean onTouch(MotionEvent event){
        boolean retVal=false;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:{
                if (event.getPointerCount() == 2) {
                    if (beginGesture(event.getX(0), event.getY(0),
                            event.getX(1), event.getY(1))) {
                        retVal = true;
                    }
                } else {
                    endGesture();
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (event.getPointerCount() == 2) {
                    gestureMove(event.getX(0), event.getY(0),
                            event.getX(1), event.getY(1));
                    retVal = true;
                } else {
                    endGesture();
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (isGesture) {
                    endGesture();
                    retVal = true;
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                if (isGesture) {
                    endGesture();
                    retVal = true;
                }
                break;
            }
            default:
                break;
        }
        return retVal;
    }

    private boolean beginGesture(float p1x, float p1y, float p2x, float p2y) {
        if (isGesture) return false;
        isGesture = true;
        pointer1X = p1x;
        pointer1Y = p1y;
        pointer2X = p2x;
        pointer2Y = p2y;
        pointerDistance = getDistance(p1x, p1y, p2x, p2y);
        lastZoom=1.0f;
        if (pointerDistance < minpointerDistance) pointerDistance = minpointerDistance;
        Log.v(TAG, "双指按下距离:" + pointerDistance);
        return true;
    }

    public float gestureMove(float p1x,float p1y,float p2x,float p2y) {
        if(!isGesture) return -1;

        float newDistance = getDistance(p1x, p1y, p2x, p2y);
        if (newDistance < minpointerDistance) newDistance = minpointerDistance;
        float newZoom = newDistance / pointerDistance;
        Log.i(TAG, "Zoom:" + newZoom);
        listener.onZoom(newZoom,lastZoom);
        lastZoom = newZoom;
        return newZoom;
    }
    public boolean endGesture(){
        isGesture=false;
        lastZoom=1.0f;
        return true;
    }
}