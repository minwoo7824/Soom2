package com.kmw.soom2.CommunityFragmentFunc.Adapters;

import android.content.Context;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.kmw.soom2.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class CommunityViewPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> listViewItems = new ArrayList<>();
    CommunityImgDetailViewPagerAdapter adapter;
    Display display;
    WindowManager wm;

    public CommunityViewPagerAdapter(Context context) {
        this.context = context;
        wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listViewItems.size();
    }

    @Override
    public void startUpdate(View view) {

    }

    @Override
    public Object instantiateItem(View view, int i) {
        return null;
    }

    @Override
    public void destroyItem(View view, int i, Object o) {

    }

    @Override
    public void finishUpdate(View view) {

    }

    public Object instantiateItem(ViewGroup container, final int position) {
        // TODO Auto-generated method stub
        View view=null;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_community_view_pager_list_item, null);
        ImageView img= (ImageView) view.findViewById(R.id.img_community_view_pager_list_item_main);

        if (listViewItems.get(position).length() > 0){
            if (listViewItems.get(position).contains("http:")){
                Glide.with(context).load(listViewItems.get(position)).override(display.getWidth(),display.getWidth()).into(img);
            }else{
                Glide.with(context).load("http:" + listViewItems.get(position)).override(display.getWidth(),display.getWidth()).into(img);
            }
        }

        container.addView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter = new CommunityImgDetailViewPagerAdapter(context,listViewItems);

                final BottomSheetDialog dialog = new BottomSheetDialog(context);
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View contentView = inflater.inflate(R.layout.view_img_detail_viewpager,null);

                ViewPager viewPagerDetail = (ViewPager) contentView.findViewById(R.id.view_pager_img_detail);
                TextView txtClose = (TextView)contentView.findViewById(R.id.txt_img_detail);

                DisplayMetrics dm = context.getResources().getDisplayMetrics();
                int height = dm.heightPixels;

                WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
                dialog.addContentView(contentView,params);

                viewPagerDetail.setAdapter(adapter);

                dialog.show();

                txtClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        return view;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v==obj;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {

    }

    public void addItem(String imagePath){
        listViewItems.add(imagePath);
    }

//    @Override
//
//    public int getItemPosition(Object object) {
//
//        return POSITION_NONE;
//
//    }
}
