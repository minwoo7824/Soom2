package com.kmw.soom2.Home.HomeAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kmw.soom2.Common.PopupDialog.ShowVideoDialog;
import com.kmw.soom2.CommunityFragmentFunc.Activitys.VideoActivity;
import com.kmw.soom2.Home.HomeItem.VideoMenuItem;
import com.kmw.soom2.R;

import java.util.ArrayList;
import java.util.Vector;


public class MemoVideoAdapter extends BaseAdapter {

    private Vector<VideoMenuItem> menus = new Vector<>();

    Context context;
    Activity activity;

    public MemoVideoAdapter(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Object getItem(int position) {
        return menus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null){

            viewHolder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_menu,parent,false);

            viewHolder.imgThumbnail = (ImageView)convertView.findViewById(R.id.img_video_thumbnail);
            viewHolder.linNum = (LinearLayout)convertView.findViewById(R.id.lin_video_num);
            viewHolder.txtNum = (TextView)convertView.findViewById(R.id.txt_video_num);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        VideoMenuItem menu  = menus.get(position);
        String title = menu.getTitle();
        Bitmap img = menu.getImg();
        final Uri uri = menu.getUri();

        Display display= ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        Glide.with(activity).load(menus.get(position).getImg()).apply(new RequestOptions().override(display.getWidth()/3,display.getWidth()/3)).into(viewHolder.imgThumbnail);

        //다이얼로그로 동영상의 Uri를 보내며 다이얼로그를 띄우는코드.

        viewHolder.imgThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowVideoDialog dialog = new ShowVideoDialog(activity, uri, activity);
                dialog.show();
            }
        });

        final ViewHolder finalViewHolder = (ViewHolder) convertView.getTag();

        if (VideoActivity.videoPathList.size() > 0){
            if (VideoActivity.videoPathList.contains(menus.get(position).getUri())){
                finalViewHolder.txtNum.setBackgroundResource(R.drawable.radius_50dp);
                finalViewHolder.txtNum.setBackgroundTintList(context.getColorStateList(R.color.colorPrimary));
            }else{
                finalViewHolder.txtNum.setBackgroundResource(0);
            }
        }

        viewHolder.linNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VideoActivity.videoPathList.contains(menus.get(position).getUri())){
                    finalViewHolder.txtNum.setBackgroundResource(0);
                    VideoActivity.videoPathList.remove(menus.get(position).getUri());
                    VideoActivity.bitmapThumbnail = null;
                    notifyDataSetChanged();
                }else{
                    VideoActivity.videoPathList = new ArrayList<>();
                    finalViewHolder.txtNum.setBackgroundResource(R.drawable.radius_50dp);
                    finalViewHolder.txtNum.setBackgroundTintList(context.getColorStateList(R.color.colorPrimary));
                    VideoActivity.videoPathList.add(menus.get(position).getUri());
                    VideoActivity.bitmapThumbnail = menus.get(position).getImg();
                    notifyDataSetChanged();
                }
            }
        });

        return convertView;
    }

    public void setUp(Vector<VideoMenuItem> menus) {
        this.menus = menus;
        notifyDataSetChanged();
    }

    private class ViewHolder{
        public ImageView imgThumbnail;
        public LinearLayout linNum;
        public TextView txtNum;
    }
}

