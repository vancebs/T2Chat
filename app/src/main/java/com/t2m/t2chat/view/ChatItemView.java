package com.t2m.t2chat.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.t2m.t2chat.R;

import io.noties.markwon.Markwon;

public class ChatItemView extends androidx.appcompat.widget.AppCompatTextView {
    private static final String TAG = ChatItemView.class.getSimpleName();

    private int mTriangleSize = 0;

    private final Paint mBackgroundPaint = new Paint();
    private int mBackgroundColor = 0xFFFFFFFF;

    private Path mBackgroundPath = null;
    private int mLastLayoutDirection = LAYOUT_DIRECTION_LTR;
    private int mLastWidth = -1;
    private int mLastHeight = -1;

    private Markwon mMarkwon = null;

    public ChatItemView(@NonNull Context context) {
        super(context, null, 0);
    }

    public ChatItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // set TextView background to transparent to avoid new background being covered
        setBackgroundColor(Color.TRANSPARENT);

        // set new background color
        mBackgroundPaint.setColor(mBackgroundColor);
        mBackgroundPaint.setPathEffect(
                new CornerPathEffect(
                        getResources().getDimension(R.dimen.conversation_item_message_bg_corner)));

        // triangle size
        mTriangleSize = getContext().getResources()
                .getDimensionPixelSize(R.dimen.conversation_item_triangle_size);
    }

    public void setItemBackgroundColor(int color) {
        mBackgroundColor = color;
        mBackgroundPaint.setColor(mBackgroundColor);
    }

    public void setItemBackgroundColorResource(int resId) {
        setItemBackgroundColor(getResources().getColor(resId, getContext().getTheme()));
    }

    public void setMarkdownText(String text) {
        if (mMarkwon == null) {
            mMarkwon = Markwon.create(getContext());
        }
        mMarkwon.setMarkdown(this, text);
    }

    @Override
    public int getCompoundPaddingLeft() {
        if (getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
            return super.getCompoundPaddingLeft();
        } else {
            return super.getCompoundPaddingLeft() + mTriangleSize;
        }
    }

    @Override
    public int getCompoundPaddingRight() {
        if (getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
            return super.getCompoundPaddingRight() + mTriangleSize;
        } else {
            return super.getCompoundPaddingRight();
        }
    }

    @Override
    public int getCompoundPaddingStart() {
        return super.getCompoundPaddingStart() + mTriangleSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int layoutDirection = getLayoutDirection();
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (mBackgroundPath == null
            || layoutDirection != mLastLayoutDirection
            || width != mLastWidth
            || height != mLastHeight) {
            mLastLayoutDirection = layoutDirection;
            mLastWidth = width;
            mLastHeight = height;

            mBackgroundPath = new Path();
            if (layoutDirection == LAYOUT_DIRECTION_RTL) {
                mBackgroundPath.moveTo(width, 0);
                mBackgroundPath.lineTo(0, 0);
                mBackgroundPath.lineTo(0, height);
                mBackgroundPath.lineTo(width - mTriangleSize, height);
                mBackgroundPath.lineTo(width - mTriangleSize, mTriangleSize);
                mBackgroundPath.lineTo(width, 0);
            } else {
                mBackgroundPath.moveTo(0, 0);
                mBackgroundPath.lineTo(width, 0);
                mBackgroundPath.lineTo(width, height);
                mBackgroundPath.lineTo(mTriangleSize, height);
                mBackgroundPath.lineTo(mTriangleSize, mTriangleSize);
                mBackgroundPath.lineTo(0, 0);
            }
        }
        canvas.drawPath(mBackgroundPath, mBackgroundPaint);

        super.onDraw(canvas);
    }
}
