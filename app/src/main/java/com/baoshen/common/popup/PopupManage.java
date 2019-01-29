package com.baoshen.common.popup;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.AnimRes;
import android.support.annotation.AnimatorRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;

import com.baoshen.common.R;

import java.util.LinkedList;

public class PopupManage {
    private static final String TAG = PopupManage.class.getSimpleName();
    Context mContext;
    LinkedList<View> mOpenedFloats;
    Type mType = Type.LIFO;
    View backgroundMask;

    public enum Type {
        // First in, First out.先进先出。
        FIFO,
        //Last in, First out.后进先出。
        LIFO
    }

    private synchronized View getOpenedFloat() {
        View openedFloat = null;
        if (mOpenedFloats.size() > 0) {
            if (mType == Type.FIFO) {
                openedFloat = mOpenedFloats.removeFirst();
            } else {
                openedFloat = mOpenedFloats.removeLast();
            }
        }
        return openedFloat;
    }

    public PopupManage(Context context) {
        this(context, Type.FIFO);
    }

    public PopupManage(Context context, Type type) {
        this.mContext = context;
        mOpenedFloats = new LinkedList<View>();
        mType = type == null ? Type.LIFO : type;
    }

    public void show(View vm, @AnimRes int id) {
        AnimationShowListener showListener = new AnimationShowListener(vm);
        show(vm, id, showListener);
    }

    public void show(View vm, @AnimRes int id, AnimationShowListener showListener) {
        Animation animation = AnimationUtils.loadAnimation(mContext, id);
        show(vm, animation, showListener);
    }

    public void show(View vm, Animation animation) {
        AnimationShowListener showListener = new AnimationShowListener(vm);
        show(vm, animation, showListener);
    }

    public void show(View vm, Animation animation, AnimationShowListener showListener) {
        animation.setAnimationListener(showListener);
        vm.startAnimation(animation);
        addOpenedFloat(vm);

    }


    public void hide(View vm, @AnimRes int id) {
        AnimationHideListener hideListener = new AnimationHideListener(vm);
        hide(vm, id, hideListener);
    }

    public void hide(View vm, @AnimRes int id, AnimationHideListener hideListener) {
        Animation animation = AnimationUtils.loadAnimation(mContext, id);
        hide(vm, animation, hideListener);
    }

    public void hide(View vm, Animation animation) {
        AnimationHideListener hideListener = new AnimationHideListener(vm);
        hide(vm, animation, hideListener);
    }

    public void hide(View vm, Animation animation, AnimationHideListener hideListener) {
        if (mOpenedFloats.contains(vm)) {
            animation.setAnimationListener(hideListener);
            vm.startAnimation(animation);
            removeOpenedFloat(vm);
        }
    }

    public boolean hide() {
        boolean isHide = false;
        View openedFloat = getOpenedFloat();
        if (openedFloat != null) {
            openedFloat.setVisibility(View.INVISIBLE);
            removeOpenedFloat(openedFloat);
            isHide = true;
        }
        return isHide;
    }

    public void hideAll() {
        boolean isContinue = true;
        while (isContinue) {
            isContinue = hide();
        }
    }

    public int getShowSize() {
        return mOpenedFloats.size();
    }

    private void addOpenedFloat(View vm) {
        int index = mOpenedFloats.indexOf(vm);
        if (index != -1)
            mOpenedFloats.remove(index);
        mOpenedFloats.add(vm);
    }

    private void removeOpenedFloat(View vm) {
        int index = mOpenedFloats.indexOf(vm);
        if (index != -1) {
            mOpenedFloats.remove(index);
        }
    }

    private void backgroundMask(boolean isShow) {
        Window window = ((Activity) mContext).getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (isShow) {
            lp.alpha = 0.3f;
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            lp.alpha = 1.0f;
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        window.setAttributes(lp);
    }
    public void setBackgroundMask(View backgroundMask){
        this.backgroundMask=backgroundMask;
    }


}
