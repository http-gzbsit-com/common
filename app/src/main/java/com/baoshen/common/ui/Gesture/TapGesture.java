package com.baoshen.common.ui.Gesture;

import android.content.Context;
import android.util.TypedValue;
import android.view.MotionEvent;

import com.baoshen.common.Log;
import com.baoshen.common.graphics.Size;

import java.util.List;

class TapGesture extends AbsGesture {
    private static final String TAG = TapGesture.class.getSimpleName();

    public TapGesture(Context context, IGestureListener listener){
        super(context,listener);
        moveDistance = unit2Pixel(TypedValue.COMPLEX_UNIT_MM,6f,context);
    }
    boolean isGesture;
    long lastDownTime;//记录按下的那一刻
    long downInterval = 400;//按下太久，则不认为是点击
    float moveDistance;//按下后，如果移动一定距离，则不算点击
    float x;
    float y;

    @Override
    public boolean onTouch(MotionEvent event){
        boolean retVal=false;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                if (event.getPointerCount() == 1) {
                    retVal = beginGesture(event.getX(0), event.getY(0));
                } else {
                    endGesture();
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (event.getPointerCount() == 1) {
                    if(gestureMove(event.getX(0), event.getY(0))){
                        retVal = true;
                    }
                    else {
                        endGesture();
                    }
                } else {
                    endGesture();
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (isGesture) {
                    notify(null);
                    endGesture();
                    retVal = true;
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                if (isGesture) {
                    notify(null);
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
    private boolean beginGesture(float x,float y) {
        if(isGesture) return false;
        this.x = x;
        this.y = y;
        lastDownTime = System.currentTimeMillis();
        isGesture=true;
        return true;
    }
    private void endGesture(){
        isGesture=false;
    }
    private boolean gestureMove(float x,float y){
        float distance = getDistance(x,y,this.x,this.y);
        return distance < moveDistance;
        //如果按下后，移动了手指，则
    }
    private boolean notify(List<Size> pointers) {
        if (System.currentTimeMillis() - lastDownTime < downInterval) {
            Log.i(TAG, "Tap:" + x + "," + y);
            listener.onTap((int) this.x, (int) this.y);
            return true;
        }
        return false;
    }
}
