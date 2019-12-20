package com.zhiqing.loadingviewstatemachine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import com.airbnb.lottie.LottieAnimationView;

public class BearLottieView extends LottieAnimationView {
    private static final String TAG = "BearLottieView";
    private boolean needPlay;
    private boolean mHasInit;

    public BearLottieView(Context context) {
        super(context);
        init(null);
    }

    public BearLottieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BearLottieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    public void playAnimation() {
        Log.v(TAG, "xxx:playAnimation: ");
        if (isVisible()) {
            super.playAnimation();
        } else {
            needPlay = true;
        }
    }

    @Override
    public void cancelAnimation() {
        super.cancelAnimation();
        needPlay = false;
    }

    private void init(AttributeSet attrs) {
        // 刚初始化的时候不让自动播放
        if (isAnimating()) {
            Log.v(TAG, "xxx:init: cancel animator");
            super.cancelAnimation();
            needPlay = true;
        }
        mHasInit = true;
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        Log.v(TAG, "xxx: onVisibilityChanged: " + visibility);
        checkAnimation(visibility);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.v(TAG, "xxx: onSizeChanged: ");
        checkAnimation(getVisibility());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 如果刚attach就播放了，暂停它，等显示的时候再播放
        if (isAnimating()) {
            Log.v(TAG, "xxx: onAttachedToWindow: cancel animator");
            super.cancelAnimation();
            needPlay = true;
        }
    }

    private boolean isVisible() {
        return getVisibility() == VISIBLE && getHeight() > 0 && getWidth() > 0;
    }

    private void checkAnimation(int visibility) {
        if (!mHasInit) {
            return;
        }

        if (visibility == VISIBLE && isVisible() && needPlay) {
            Log.v(TAG, "xxx: checkAnimation: play " + hashCode());
            super.playAnimation();
        } else {
            if (isAnimating()) {
                needPlay = true;
            }
            Log.d(TAG, "xxx: checkAnimation: cancel " + hashCode());
            super.cancelAnimation();
        }
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable dr) {
        if (!mHasInit) {
            super.invalidateDrawable(dr);
            return;
        }

        if (!isVisible()) {
            Log.v(TAG, "invalidateDrawable not visible, return");
            if (isAnimating()) {
                needPlay = true;
            }
            super.cancelAnimation();
            return;
        }

        super.invalidateDrawable(dr);
    }
}
