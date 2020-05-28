package com.kmw.soom2.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class BannerImageView extends ImageView {
    public BannerImageView(Context context) {
        super(context);
    }

    public BannerImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BannerImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, (int) (widthMeasureSpec * 0.41));
    }
}
