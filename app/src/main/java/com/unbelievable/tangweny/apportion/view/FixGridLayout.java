package com.unbelievable.tangweny.apportion.view;

/**
 * Created by tangweny on 2016/4/29.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FixGridLayout extends ViewGroup {
    private final static int VIEW_MARGIN = 2;
    public FixGridLayout(Context context) {
        super(context);
    }

    public FixGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /**
     * 控制子控件的换行
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();

        int row = 0;// which row lay you view relative to parent

        int lengthX = l; // right position of child relative to parent

        int lengthY = t; // bottom position of child relative to parent

        for (int i = 0; i < count; i++) {

            final View child = this.getChildAt(i);

            int width = child.getMeasuredWidth();

            int height = child.getMeasuredHeight();

            lengthX += width + VIEW_MARGIN;

            lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height
                    + t;

            // if it can't drawing on a same line , skip to next line

            if (lengthX > r) {

                lengthX = width + VIEW_MARGIN + l;

                row++;

                lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height
                        + t;

            }

            child.layout(lengthX - width, lengthY - height, lengthX, lengthY);

        }
    }

    /**
     * 计算控件及子控件所占区域
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int index = 0; index < getChildCount(); index++) {

            final View child = getChildAt(index);

            // measure

            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}

