package com.kmw.soom2.MyPage.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.kmw.soom2.MyPage.Item.ViewPagerItemList;
import com.kmw.soom2.R;

import java.util.ArrayList;

public class ViewPagerImageAdapter extends PagerAdapter {
    ArrayList<ViewPagerItemList> arrayList;
    Context context;
    ImageView imageView;
    public ViewPagerImageAdapter(ArrayList<ViewPagerItemList>arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewPagerItemList item = arrayList.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_pager_item_list_my_page, null);
        (container).addView(view);
        imageView = (ImageView) view.findViewById(R.id.view_pager_image_my_page);
        imageView.setImageResource(item.getImage());
        return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub

        //ViewPager에서 보이지 않는 View는 제거
        //세번째 파라미터가 View 객체 이지만 데이터 타입이 Object여서 형변환 실시
        container.removeView((View) object);

    }

}
