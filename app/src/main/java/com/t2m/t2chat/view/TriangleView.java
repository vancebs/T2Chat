package com.t2m.t2chat.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TriangleView extends View {
    private Paint mPaint;
    private Path mTrianglePath;
    private int mLastWidth = -1;
    private int mLastHeight = -1;
    private boolean mInvalidate = true;

    private boolean mMirror = false;

    public TriangleView(Context context) {
        super(context);
        init();
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPaint = new Paint();
    }

    public void setColor(int color) {
        mPaint.setColor(color);
        mInvalidate = true;
    }

    public void setColorRes(int colorRes) {
        mPaint.setColor(getResources().getColor(colorRes, null));
        mInvalidate = true;
    }

    public void setMirror(boolean mirror) {
        mMirror = mirror;
        mInvalidate = true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // update path if necessary
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (mLastWidth != width || mLastHeight != height) {
            mLastWidth = width;
            mLastHeight = height;

            mInvalidate = true;
        }

        if (mInvalidate) {
            mInvalidate = false;

            mTrianglePath = new Path();
            if (mMirror) {
                mTrianglePath.moveTo(width, 0);
                mTrianglePath.lineTo(0, 0);
                mTrianglePath.lineTo(0, width);
                mTrianglePath.lineTo(width, 0);
            } else {
                mTrianglePath.moveTo(0, 0);
                mTrianglePath.lineTo(width, 0);
                mTrianglePath.lineTo(width, width);
                mTrianglePath.lineTo(0, 0);
            }
        }

        canvas.drawPath(mTrianglePath, mPaint);
    }
}
