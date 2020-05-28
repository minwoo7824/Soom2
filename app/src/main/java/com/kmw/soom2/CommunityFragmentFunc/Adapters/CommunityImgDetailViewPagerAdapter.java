package com.kmw.soom2.CommunityFragmentFunc.Adapters;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.kmw.soom2.R;

import java.util.ArrayList;

public class CommunityImgDetailViewPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> listViewItems = new ArrayList<>();

    public CommunityImgDetailViewPagerAdapter(Context context, ArrayList<String> listViewItems) {
        this.context = context;
        this.listViewItems.addAll(listViewItems);
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
        view = inflater.inflate(R.layout.view_community_image_detail, null);
        ImageView img= (ImageView) view.findViewById(R.id.img_view_pager_community_detail);

        if (listViewItems.get(position).contains("http:")){
            Glide.with(context).asBitmap().load(listViewItems.get(position)).into(img);
        }else{
            Glide.with(context).asBitmap().load("http:" + listViewItems.get(position)).into(img);
        }
        container.addView(view);

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
}
