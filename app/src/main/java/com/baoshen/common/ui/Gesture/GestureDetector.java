package com.baoshen.common.ui.Gesture;

import android.content.Context;
import android.view.MotionEvent;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GestureDetector {
    List<AbsGesture> gestures;
    private Context context;
    public GestureDetector(@NotNull Context context,@NotNull IGestureListener listener,@NotNull GestureType... types){
        assert types!=null && types.length>0 : "请设置要识别的手势类型";
        this.context= context;
        gestures = new ArrayList<>(types.length);
        for (GestureType gestureType:types){
            switch (gestureType){
                case Tap:
                    gestures.add(new TapGesture(context,listener));
                    break;
                case Zoom:
                    gestures.add(new ZoomGesture(context,listener));
                    break;
                    default:
                        throw new IllegalArgumentException("未实现的手势");
            }
        }
    }
    public boolean onTouch(MotionEvent event){
        boolean retVal = false;
        for(AbsGesture gesture:gestures){
            retVal |=  gesture.onTouch(event);
        }
        return retVal;
    }
}
