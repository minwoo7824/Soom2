package com.kmw.soom2.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FooterFrameLayout extends FrameLayout {

    TextView tvTitle;

    public FooterFrameLayout(@NonNull Context context) {
        super(context);
    }

    public FooterFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FooterFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /* SKIP CODE... */

    /**
     * TitleView 를 상단에 고정시키기 위한 method
     */
    public void fixTitle(int scrollY) {
        tvTitle.setY(-scrollY);
    }
}
