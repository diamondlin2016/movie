package com.diamond.colamovie.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.diamond.colamovie.R;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/31 下午4:35
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/31      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class RatioFrameLayout extends FrameLayout {

    private float mRatio;

    public RatioFrameLayout(Context context) {
        super(context);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RatioImageView, R.attr.ratio, 0);
        try {
            mRatio = attributes.getFloat(R.styleable.RatioImageView_ratio, 1);
        } finally {
            attributes.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, (int) (width * mRatio));
    }

    public void setRatio(float ratio) {
        mRatio = ratio;
    }


}