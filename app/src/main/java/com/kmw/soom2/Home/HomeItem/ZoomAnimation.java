package com.kmw.soom2.Home.HomeItem;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class ZoomAnimation implements ViewPager.PageTransformer {
    private final float MIN_SCALE = 0.7f;
    private final float MIN_ALPHA = 0.5f;
    @Override
    public void transformPage(@NonNull View view, float position) {

        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if(position < -1){
            view.setAlpha(0f);
        }else if (position <=1){
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor)/2;
            float horzMargin = pageWidth * (1 - scaleFactor)/2;
            if(position<0){
                view.setTranslationX(horzMargin - vertMargin/2);
            }else {
                view.setTranslationX(-horzMargin + vertMargin/2);
            }
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            view.setAlpha(MIN_ALPHA + (scaleFactor -MIN_ALPHA)/(1 - MIN_SCALE) * (1 - MIN_ALPHA));
        }else {
            view.setAlpha(0f);
        }
    }
}
