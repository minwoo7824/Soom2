package com.kmw.soom2.DrugControl.Item;

import android.view.View;

import com.kmw.soom2.DrugControl.Interface.StickyView;


public class ExampleStickyView implements StickyView {

    @Override
    public boolean isStickyView(View view) {
        return (Boolean) view.getTag();
    }

    @Override
    public int getStickViewType() {
        return DrugListItemList.HEADER;
    }
}
