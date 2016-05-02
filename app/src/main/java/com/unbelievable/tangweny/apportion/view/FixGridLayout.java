package com.unbelievable.tangweny.apportion.view;

/**
 * Created by tangweny on 2016/4/29.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.Hashtable;

public class FixGridLayout extends ViewGroup {

    private Hashtable map = new Hashtable();

    /*确定子视图位置参数*/
    int mLeft, mRight, mTop, mBottom;
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

        for (int i = 0; i < count; i++) {

            View child = getChildAt(i);
            Position position = (Position)map.get(child);
            if (position != null){
                child.layout(position.left,position.top,position.right,position.bottom);
            }

        }
    }

    /**
     * 计算控件及子控件所占区域
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*获得总宽度*/
        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        /*X坐标位置*/
        int mX = 0;
        /*Y坐标位置*/
        int mY = 0;
        /*确定子视图位置参数*/
        mLeft = 0;
        mRight = 0;
        mTop = 10;
        mBottom = 0;
        /*定义记录视图索引*/
        int index = 0;
        for (int i = 0; i < getChildCount(); i++) {

            final View child = getChildAt(i);
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            int childw = child.getMeasuredWidth();
            int childh = child.getMeasuredHeight();
            mLeft = getPosition(i - index, i);
            /*宽度累加*/
            mX += childw;
            if (mX >= mWidth) {
                mX = childw;
                mY += childh;
                index = i;
                mLeft = 0;
                mTop = mY + 10;
            }
            mRight = mLeft + childw;
            mBottom = mTop + childh;
            mY = mTop;

            Position position = new Position();
            position.left = mLeft;
            position.top = mTop + 8;
            position.right = mRight;
            position.bottom = mBottom;
            map.put(child, position);

        }
        setMeasuredDimension(mWidth, mBottom);
    }
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(0, 0); // default of 1px spacing
    }
    public int getPosition(int IndexInRow, int childIndex) {
        if (IndexInRow > 0) {
            return getPosition(IndexInRow - 1, childIndex - 1) + getChildAt(childIndex - 1).getMeasuredWidth() + 8;
        }
        return getPaddingLeft();
    }

    private class Position {
        int left, top, right, bottom;
    }
}

