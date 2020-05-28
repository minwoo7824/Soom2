package com.kmw.soom2.Home.HomeItem;

import android.view.View;

import com.kmw.soom2.Home.Interface.RecyclerViewStickyView;

public class RecyclerViewMainStickyView implements RecyclerViewStickyView {

    @Override
    public boolean isStickyView(View view) {
        return (Boolean) view.getTag();
    }

    @Override
    public int getStickViewType() {
        return RecyclerViewItemList.HEADER_TYPE;
    }
}

