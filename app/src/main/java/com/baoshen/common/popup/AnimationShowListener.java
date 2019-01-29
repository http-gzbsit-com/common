package com.baoshen.common.popup;

import android.view.View;
import android.view.animation.Animation;

public class AnimationShowListener implements Animation.AnimationListener {
    View mVm;

    public AnimationShowListener(View vm) {
        mVm = vm;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        mVm.setEnabled(true);
        mVm.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
