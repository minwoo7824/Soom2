package com.kmw.soom2.Home.HomeAdapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.kmw.soom2.Home.HomeItem.BannerItem;
import com.kmw.soom2.Home.HomeItem.UIUtils;
import com.kmw.soom2.R;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
    ArrayList<BannerItem> bannerItemArrayList = new ArrayList<>();
    Context context;
    private int pos = 0;


    public ViewPagerAdapter(Context context, ArrayList<BannerItem> bannerItemArrayList) {
        this.bannerItemArrayList = bannerItemArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_pager_item_list, null);
        pos = position % bannerItemArrayList.size();
        ImageView imageView = (ImageView) view.findViewById(R.id.view_pager_image);
        (container).addView(view);
        Glide.with(context)
                .asBitmap().load("http:" + bannerItemArrayList.get(pos).getImageFile())
                .into(UIUtils.getRoundedImageTarget(context, imageView, 6));
        if (pos >= bannerItemArrayList.size() - 1) {
            pos = 0;
        } else {
            ++pos;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bannerItemArrayList.get(pos).getBannerType().equals("1")){
                    Log.i("banner_image_url : " , "" + bannerItemArrayList.get(pos).getBannerLink());
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(bannerItemArrayList.get(pos).getBannerLink()));
                    context.startActivity(i);
                }
            }
        });

        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub

        //ViewPager에서 보이지 않는 View는 제거
        //세번째 파라미터가 View 객체 이지만 데이터 타입이 Object여서 형변환 실시
        container.removeView((View) object);

    }

}
