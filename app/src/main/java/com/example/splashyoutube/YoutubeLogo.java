package com.example.splashyoutube;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class YoutubeLogo extends View {
    public YoutubeLogo(Context context) {
        super(context);
        init();
    }

    public YoutubeLogo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public YoutubeLogo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public YoutubeLogo(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    Path path;

    Paint red;
    Paint red2;
    Paint white;


    OnFinish onFinish;

    public void setListener(OnFinish onFinish) {
        this.onFinish = onFinish;
    }

    private void init() {
        red = new Paint();
        red.setColor(Color.RED);
        red2 = new Paint();
        red2.setColor(Color.RED);

        white = new Paint();
        white.setColor(Color.WHITE);

        path = new Path();

    }

    public interface OnFinish {
        void onFinish();
    }

    float size = 0f;
    float sizeHeight = 0f;
    float progress = 0f;

    public void startAnimationColor() {
        continueAnimation = false;
        size = 0f;
        sizeHeight = 0f;
        progress = 0f;
        ValueAnimator valueAnimator = ObjectAnimator.ofArgb(Color.RED, Color.parseColor("#9C9C9C"));
        valueAnimator.addUpdateListener(animation -> {
            red.setColor((Integer) animation.getAnimatedValue());
            invalidate();
        });
        valueAnimator.setStartDelay(1000);
        valueAnimator.start();

        ValueAnimator valueAnimator1 = ObjectAnimator.ofFloat(0f, 1f);
        valueAnimator1.addUpdateListener(animation -> {
            size = (float) animation.getAnimatedValue();


            invalidate();
        });

        valueAnimator1.setStartDelay(1200);
        valueAnimator1.setDuration(400);
        valueAnimator1.start();


        ValueAnimator valueAnimator2 = ObjectAnimator.ofFloat(0f, 1f);
        valueAnimator2.addUpdateListener(animation -> {
            sizeHeight = (float) animation.getAnimatedValue();
            invalidate();
        });
        valueAnimator2.setStartDelay(1000);
        valueAnimator2.setDuration(500);
        valueAnimator2.start();
        continueAnimation();
    }

    boolean continueAnimation = false;

    private void continueAnimation() {
        if (continueAnimation)
            return;

        continueAnimation = true;
        ValueAnimator valueAnimator1 = ObjectAnimator.ofFloat(0f, 1f);
        valueAnimator1.addUpdateListener(animation -> {
            progress = (float) animation.getAnimatedValue();
            invalidate();
        });
        valueAnimator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                finishAnimation();
            }
        });

        valueAnimator1.setStartDelay(1000);
        valueAnimator1.setDuration(1300);
        valueAnimator1.start();
    }

    private void finishAnimation() {
        reverse = true;
        ValueAnimator valueAnimator1 = ObjectAnimator.ofFloat(1f, 0f);
        valueAnimator1.addUpdateListener(animation -> {
            size = (float) animation.getAnimatedValue();
            invalidate();
        });
        valueAnimator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (onFinish != null)
                    onFinish.onFinish();
            }
        });
        valueAnimator1.setDuration(400);
        valueAnimator1.start();
    }

    private float dpToPx(float dp) {
        return getResources().getDisplayMetrics().density * dp;
    }

    boolean reverse = false;

    private void createPath() {
        path.reset();
        float x = (getWidth() / 2f);

        x -= size * (x - dpToPx(35));
        if (progress > 0f) {
            x -= progress * dpToPx(18);
        }
        float size = Math.min(getWidth(), getHeight()) - dpToPx(5);
        float sizeTriangle = size / 15f;
        path.moveTo(x - (sizeTriangle / 2), (getHeight() / 2f) - (sizeTriangle / 2));
        path.lineTo(x - (sizeTriangle / 2), (getHeight() / 2f) + (sizeTriangle / 2));
        path.lineTo(x + (sizeTriangle / 2), (getHeight() / 2f));
        path.close();

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        createPath();
        float width = getWidth() / 4f;
        float height = width * 0.65f;
        float left = (getWidth() / 2f) - (width / 2);
        float right = (getWidth() / 2f) + (width / 2);
        float top = (getHeight() / 2f) - (height / 2);
        float bottom = (getHeight() / 2f) + (height / 2);
        float halfHeightSize = (height - dpToPx(5)) / 2f;

        if (size > 0f || sizeHeight > 0f) {
            float halfWidthSize = (getWidth() - dpToPx(110) - width) / 2f;
            left -= (halfWidthSize * size);
            right += (halfWidthSize * size);
            if (reverse) {
                top += halfHeightSize;
                bottom -= halfHeightSize;
            } else {
                top += (halfHeightSize * sizeHeight);
                bottom -= (halfHeightSize * sizeHeight);
            }

        } else {
            if (reverse) {
                top += halfHeightSize;
                bottom -= halfHeightSize;
            }
        }

        if (progress > 0f) {
            left -= progress * dpToPx(18);
            right += progress * dpToPx(18);
        }
        if (reverse) {
            float x = (getWidth() / 2f);

            x -= size * (x - dpToPx(35));
            if (progress > 0f) {
                x -= progress * dpToPx(18);
            }
            right -= x - dpToPx(15);
            left = x + dpToPx(28);
        }
        if (!reverse)
            canvas.drawRoundRect(left, top, right, bottom, dpToPx(20) - (dpToPx(20) * sizeHeight), dpToPx(20) - (dpToPx(20) * sizeHeight), red);

        if (progress > 0f) {
            float p = progress * right;
            if (p < left)
                p = left;
            canvas.drawRoundRect(left, top, p, bottom, dpToPx(20) - (dpToPx(20) * sizeHeight), dpToPx(20) - (dpToPx(20) * sizeHeight), red2);
        }
        canvas.drawPath(path, white);

    }
}
