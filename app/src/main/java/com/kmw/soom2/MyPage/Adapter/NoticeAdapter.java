package com.kmw.soom2.MyPage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmw.soom2.MyPage.Activity.NoticeDetailActivity;
import com.kmw.soom2.MyPage.Item.NoticeItem;
import com.kmw.soom2.R;

import java.util.ArrayList;

public class NoticeAdapter extends BaseAdapter {

    Context context;
    ArrayList<NoticeItem> itemArrayList = new ArrayList<>();

    public NoticeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return itemArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = new ViewHolder();

        if (view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.view_notice_list_item,null);
            viewHolder.txtTitle = (TextView)view.findViewById(R.id.txt_notice_list_item_title);
            viewHolder.txtDate = (TextView)view.findViewById(R.id.txt_notice_list_item_date);
            viewHolder.txtContents = (TextView)view.findViewById(R.id.txt_notice_list_item_contents);
            viewHolder.linContents = (LinearLayout)view.findViewById(R.id.lin_notice_list_item_contents);
            viewHolder.imgArrow = (ImageView)view.findViewById(R.id.img_notice_list_item_arrow);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        if (itemArrayList.get(i).getNoticeFlag() == 1){
            viewHolder.txtTitle.setText("[일반] " + itemArrayList.get(i).getTitle());
        }else if (itemArrayList.get(i).getNoticeFlag() == 2){
            viewHolder.txtTitle.setText("[점검] " + itemArrayList.get(i).getTitle());
        }else if (itemArrayList.get(i).getNoticeFlag() == 3){
            viewHolder.txtTitle.setText("[긴급] " + itemArrayList.get(i).getTitle());
        }
        viewHolder.txtDate.setText(itemArrayList.get(i).getDate());
        viewHolder.txtContents.setText(itemArrayList.get(i).getContents());

        final ViewHolder finalViewHolder = viewHolder;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NoticeDetailActivity.class);
                intent.putExtra("noticeNo",itemArrayList.get(i).getNo());
                intent.putExtra("contents", itemArrayList.get(i).getContents());
                context.startActivity(intent);


//                if (finalViewHolder.linContents.getVisibility() == View.GONE){
//                    Utils.Expand(finalViewHolder.linContents);
//                    finalViewHolder.imgArrow.setRotation(180);
//                }else{
//                    Utils.Collapse(finalViewHolder.linContents);
//                    finalViewHolder.imgArrow.setRotation(360);
//                }
            }
        });

        return view;
    }

    public void addItem(NoticeItem noticeItem ){
        itemArrayList.add(noticeItem);
    }

    private class ViewHolder{
        public TextView txtTitle,txtDate,txtContents;
        public LinearLayout linContents;
        public ImageView imgArrow;
    }
}
