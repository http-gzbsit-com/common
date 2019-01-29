package com.baoshen.common.popup;

import android.view.View;
import android.view.animation.Animation;

public class AnimationHideListener implements Animation.AnimationListener  {
    View mVm;
    public AnimationHideListener(View vm) {
        mVm = vm;
    }
    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        mVm.setVisibility(View.INVISIBLE);
        mVm.setEnabled(false);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
