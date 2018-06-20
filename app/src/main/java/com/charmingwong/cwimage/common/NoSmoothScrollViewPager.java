package com.charmingwong.cwimage.common;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * @author : Charming Wong
 * @time : 2018/1/31
 * @des : 取消非左右滑动切换 ViewPager 的动画效果
 */
public class NoSmoothScrollViewPager extends ViewPager {

    public NoSmoothScrollViewPager(Context context) {
        super(context);
    }

    public NoSmoothScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }
}
