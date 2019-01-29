package com.baoshen.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by Shute on 2016/9/11.
 */
public class HomeListener {
    public static final String TAG = HomeListener.class.getSimpleName();
    public HomeListener(Context context) {
        mContext = context;
        mHomeBtnReceiver = new HomeBtnReceiver( );
        mHomeBtnIntentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    }

    private Context mContext = null;
    private IntentFilter mHomeBtnIntentFilter = null;
    private OnHomeBtnPressLitener mOnHomeBtnPressListener = null;
    private HomeBtnReceiver mHomeBtnReceiver = null;

    public void setOnHomeBtnPressListener( OnHomeBtnPressLitener onHomeBtnPressListener ){
        mOnHomeBtnPressListener = onHomeBtnPressListener;
    }

    public void start( ){
        try {
        mContext.registerReceiver( mHomeBtnReceiver, mHomeBtnIntentFilter );
        }catch (Exception ex){
            Log.e(TAG,"启动失败",ex);
        }
    }

    public void stop( ){
        try {
            mContext.unregisterReceiver(mHomeBtnReceiver);
        }catch (Exception ex){
            Log.e(TAG,"停止失败",ex);
        }
    }

    class HomeBtnReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            receive( context, intent );
        }
    }

    private void receive(Context context, Intent intent){
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra( "reason" );
            if (reason != null) {
                if( null != mOnHomeBtnPressListener ){
                    if( reason.equals( "homekey" ) ){
                        // 按Home按键
                        mOnHomeBtnPressListener.onHomeBtnPress( );
                    }else if( reason.equals( "recentapps" ) ){
                        // 长按Home按键
                        mOnHomeBtnPressListener.onHomeBtnLongPress( );
                    }
//                    else if (reason.equals("lock")) {
//                        // 锁屏
//                        mOnHomeBtnPressListener.onLock( );
//                    }
//                    else if (reason.equals("unlock")) {
//                        // 解锁
//                        mOnHomeBtnPressListener.onUnLock( );
//                    }
                }
            }
        }
    }

    public interface OnHomeBtnPressLitener{
        public void onHomeBtnPress();
        public void onHomeBtnLongPress();
//        public void onLock();
//        public void onUnLock();
    }
}
