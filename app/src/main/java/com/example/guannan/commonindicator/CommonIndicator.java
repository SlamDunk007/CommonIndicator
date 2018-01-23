package com.example.guannan.commonindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author guannan
 * @date 2018/1/11 11:07
 */

public class CommonIndicator extends HorizontalScrollView {

    private LinearLayout mLinearLayout;
    //tab字体的大小
    private int mTextSize;
    //字体的颜色
    private int mTextColor;
    //tab选中的字体的颜色
    private int mTextColorSelect;
    private Runnable mRunnable;

    public CommonIndicator(Context context) {
        this(context, null);
    }

    public CommonIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CommonIndicator);
        mTextSize = a.getInteger(R.styleable.CommonIndicator_text_size, 15);
        mTextColor = a.getColor(R.styleable.CommonIndicator_text_color, 0x000000);
        mTextColorSelect = a.getColor(R.styleable.CommonIndicator_text_color_select, 0x508cee);
        a.recycle();
        setHorizontalScrollBarEnabled(false);
        initLinearLayout();
    }

    /**
     * 初始化容纳tab的线性容器
     */
    private void initLinearLayout() {
        if (mLinearLayout == null) {
            mLinearLayout = new LinearLayout(getContext());
        }
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(mLinearLayout, new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
    }

    /**
     * 设置tab的标题内容
     *
     * @param titles
     */
    public void setTabTitles(List<String> titles) {
        mLinearLayout.removeAllViews();
        for (int i = 0; i < titles.size(); i++) {
            TextView tv = getTextView(titles, i);
            mLinearLayout.addView(tv);
        }
        setOnItemClick();
        setTabSelect(0);    //默认选中的tab
    }

    /**
     * 设置选中的tab
     *
     * @param position
     */
    public void setTabSelect(int position) {

        if (position >= 0) {
            View childView = mLinearLayout.getChildAt(position);
            changeTab(position, childView);
        } else {
            try {
                throw new IllegalAccessException("position不能为负数");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给每个tab设置点击事件
     */
    private void setOnItemClick() {

        int childCount = mLinearLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View childView = mLinearLayout.getChildAt(i);
            final int j = i;
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeTab(j, childView);
                    if (mOnTabSelectListener != null) {
                        mOnTabSelectListener.onTabSelect(j);
                    }
                }
            });
        }
    }

    /**
     * 切换到选中的tab，并改变tab的颜色字体等
     *
     * @param j
     * @param childView
     */
    private void changeTab(final int j, final View childView) {
        if (mRunnable != null) {
            removeCallbacks(mRunnable);
        }
        mRunnable = new Runnable() {
            @Override
            public void run() {
                resetTextViewColor();
                highLightTextView(j);
                final int scrollPos = childView.getLeft() - (getWidth() - childView.getWidth()) / 2;
                smoothScrollTo(scrollPos, 0);   //改变选中的tab的位置
            }
        };
        post(mRunnable);
    }

    /**
     * 重置字体的颜色
     */
    public void resetTextViewColor() {

        int childCount = mLinearLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            TextView childView = (TextView) mLinearLayout.getChildAt(i);
            childView.setTextColor(mTextColor);
            childView.setBackgroundResource(0);
        }
    }

    /**
     * 改变选中的tab的字体的颜色
     *
     * @param position
     */
    public void highLightTextView(int position) {

        TextView childView = (TextView) mLinearLayout.getChildAt(position);
        childView.setTextColor(mTextColorSelect);
        childView.setBackgroundResource(R.drawable.selector_shape_blue_line_bg);
    }

    /**
     * 生成每个Tab
     *
     * @param titles
     * @param i
     * @return
     */
    @NonNull
    private TextView getTextView(List<String> titles, int i) {
        String text = titles.get(i);
        TextView tv = new TextView(getContext());
        tv.setGravity(Gravity.CENTER);
        tv.setText(text);
        tv.setTextColor(mTextColor);
        tv.setPadding(0, dpTopx(12), 0, dpTopx(12));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layoutParams.setMargins(dpTopx(12), 0, dpTopx(12), 0);
        tv.setLayoutParams(layoutParams);
        return tv;
    }

    private int dpTopx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //暴露给外面的选中tab的接口
    interface OnTabSelectListener {
        void onTabSelect(int position);

    }

    public void setOnTabSelectListener(OnTabSelectListener onTabSelectListener) {
        this.mOnTabSelectListener = onTabSelectListener;
    }

    private OnTabSelectListener mOnTabSelectListener;
}
