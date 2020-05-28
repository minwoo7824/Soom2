package com.kmw.soom2.Home.HomeItem;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class CustomViewPager extends ViewPager {
    public CustomViewPager(@NonNull Context context) {
        super(context);
    }

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        postInitViewPager();
    }
    private ScrollerCustomDuration mScroller = null;
    private void postInitViewPager() {

        try {

            Class<?> viewpager = ViewPager.class;

            Field scroller = viewpager.getDeclaredField("mScroller");

            scroller.setAccessible(true);

            Field interpolator = viewpager.getDeclaredField("sInterpolator");

            interpolator.setAccessible(true);



            mScroller = new ScrollerCustomDuration(getContext(),

                    (Interpolator) interpolator.get(null));

            scroller.set(this, mScroller);

        } catch (Exception e) {

        }

    }
}
