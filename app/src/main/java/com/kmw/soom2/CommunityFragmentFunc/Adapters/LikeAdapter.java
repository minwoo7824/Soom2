package com.kmw.soom2.CommunityFragmentFunc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kmw.soom2.CommunityFragmentFunc.Items.LikeItem;
import com.kmw.soom2.R;

import java.util.ArrayList;

public class LikeAdapter extends BaseAdapter {

    Context context;
    ArrayList<LikeItem> itemArrayList = new ArrayList<>();

    public LikeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return itemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_like_list_item,null);

            viewHolder.txtNickname = (TextView)convertView.findViewById(R.id.txt_like_list_item_nickname);
            viewHolder.imgProfile = (ImageView)convertView.findViewById(R.id.img_like_list_item_profile);

            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        RequestOptions requestOptions = new RequestOptions();

        viewHolder.txtNickname.setText(itemArrayList.get(position).getNickName());

        if (itemArrayList.get(position).getImgPath().length() > 0){
            if (itemArrayList.get(position).getImgPath().contains("http:")){
                Glide.with(context).load(itemArrayList.get(position).getImgPath()).apply(requestOptions.circleCrop()).into(viewHolder.imgProfile);
            }else{
                Glide.with(context).load("http:" + itemArrayList.get(position).getImgPath()).apply(requestOptions.circleCrop()).into(viewHolder.imgProfile);
            }
        }else{
            viewHolder.imgProfile.setImageResource(R.drawable.ic_user_profile);
        }


        return convertView;
    }

    private class ViewHolder{
        TextView txtNickname;
        ImageView imgProfile;
    }

    public void addItem(LikeItem likeItem){
        itemArrayList.add(likeItem);
    }
}
