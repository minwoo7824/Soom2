package com.kmw.soom2.StaticFunc.Activitys.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.R;
import com.kmw.soom2.StaticFunc.Activitys.Item.HistoryItems;
import com.kmw.soom2.StaticFunc.Activitys.Item.StaticBreathItems;
import com.kmw.soom2.StaticFunc.Activitys.StaticBreathDetailActivity;

import java.util.ArrayList;
import java.util.Collections;


public class StaticBreathListAdapter extends BaseAdapter {
    Context context;

    public ArrayList<StaticBreathItems> itemsArrayList = new ArrayList<>();

    public StaticBreathListAdapter(Context context) {
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
            view = inflater.inflate(R.layout.view_static_breath_list_item,null);

            viewHolder = new ViewHolder();

            viewHolder.txtDate = (TextView)view.findViewById(R.id.txt_statics_breath_date);
            viewHolder.txtMeasureCnt = (TextView)view.findViewById(R.id.txt_statics_breath_measure_cnt);
            viewHolder.txtStatus = (TextView)view.findViewById(R.id.txt_statics_breath_status);

            view.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        if (itemsArrayList.get(i).getHistoryItems().size() > 1) {
            if (itemsArrayList.get(i).getPefRate() < 20) {
                viewHolder.txtStatus.setText("양호");
                viewHolder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.statics_result_good));
            } else if (itemsArrayList.get(i).getPefRate() >= 20 && itemsArrayList.get(i).getPefRate() < 30) {
                viewHolder.txtStatus.setText("주의");
                viewHolder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.statics_result_warning));
            } else {
                viewHolder.txtStatus.setText("위험");
                viewHolder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.statics_result_danger));
            }
        }else {
            viewHolder.txtStatus.setText("-");
            viewHolder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.black));
        }

        viewHolder.txtMeasureCnt.setText(itemsArrayList.get(i).getStMeasureCnt());
        viewHolder.txtDate.setText(itemsArrayList.get(i).getStDate());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StaticBreathDetailActivity.class);
                intent.putExtra("date",itemsArrayList.get(i).getStDate());
                Utils.breathItem = itemsArrayList.get(i);
                context.startActivity(intent);
            }
        });

        return view;
    }
    public void addItem(String status, String date, String cnt){
        StaticBreathItems breathItems = new StaticBreathItems();

        itemsArrayList.add(breathItems);
    }
    public void addItem(ArrayList<HistoryItems> items, String date) {
        Log.i("StaticBreathActivity", items.toString());
        StaticBreathItems breathItems = new StaticBreathItems();

        ArrayList<Integer> scores = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            scores.add(items.get(i).getPefScore());
        }
        float max = Collections.max(scores).floatValue();
        float min = Collections.min(scores).floatValue();

        breathItems.setStDate(date);
        breathItems.setStMeasureCnt(items.size() + "회 측정");

        float pefRate = ((max - min) / ((max + min) / 2)) * 100;
        breathItems.setPefRate(pefRate);
        breathItems.setHistoryItems(items);

        itemsArrayList.add(breathItems);
    }
    private class ViewHolder {
        public TextView txtDate;
        public TextView txtMeasureCnt;
        public TextView txtStatus;

    }
}
