package com.kmw.soom2.Common.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kmw.soom2.Common.Item.PushItems;
import com.kmw.soom2.R;

import java.util.ArrayList;

public class PushAlarmListAdapter extends BaseAdapter {
    ArrayList<PushItems> itemsArrayList = new ArrayList<>();
    Context context;

    public PushAlarmListAdapter(Context context) {
        this.context = context;
    }

        @Override
    public int getCount() {
        return itemsArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemsArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;

        if (view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.view_push_list_item,null);

            viewHolder = new ViewHolder();

            viewHolder.txtTitle = view.findViewById(R.id.txt_push_list_title);
            viewHolder.txtContents = view.findViewById(R.id.txt_push_list_contents);

            view.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.txtTitle.setText(itemsArrayList.get(i).getTitle());
        viewHolder.txtContents.setText(itemsArrayList.get(i).getContents());
        return view;
    }
    public void addItem(PushItems item) {
        PushItems pushitem = new PushItems();

        pushitem.setContents(item.getContents());
        pushitem.setCreateDt(item.getCreateDt());
        pushitem.setPushNo(item.getPushNo());
        pushitem.setUserNo(item.getUserNo());
        pushitem.setTitle(item.getTitle());

        itemsArrayList.add(pushitem);
    }
    private class ViewHolder {
        public TextView txtTitle;
        public TextView txtContents;

    }
}
