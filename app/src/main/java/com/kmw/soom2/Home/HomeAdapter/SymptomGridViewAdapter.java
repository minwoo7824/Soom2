package com.kmw.soom2.Home.HomeAdapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmw.soom2.Home.HomeItem.SymptomGridViewItem;
import com.kmw.soom2.R;

import java.util.ArrayList;

import static com.kmw.soom2.Home.HomeActivity.SymptomActivitys.SymptomRecord.checkedList;

public class SymptomGridViewAdapter extends BaseAdapter {

    private String TAG = "SymptomGridViewAdapter";
    Context context;
    ArrayList<SymptomGridViewItem> arrayList = new ArrayList<>();
    LayoutInflater inflater;

    public SymptomGridViewAdapter(Context context, ArrayList<SymptomGridViewItem> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if(convertView == null){
            viewHolder = new ViewHolder();

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_grid,null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_tab_grid);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.txt_tab_grid);

            viewHolder.imageView.setImageResource(arrayList.get(position).getImage());
            viewHolder.textView.setText(arrayList.get(position).getTitle());

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        if(arrayList.get(position).isChecked()) {
            viewHolder.imageView.setColorFilter(Color.parseColor("#33d16b"));
            viewHolder.textView.setTextColor(Color.parseColor("#33d16b"));
        }

        final ViewHolder finalViewHolder = viewHolder;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"click");
                if (!arrayList.get(position).isChecked()) {
                    finalViewHolder.imageView.setColorFilter(Color.parseColor("#33d16b"));
                    finalViewHolder.textView.setTextColor(Color.parseColor("#33d16b"));
                    arrayList.get(position).setChecked(true);

                    checkedList.add(""+position);
                }else if(arrayList.get(position).isChecked()) {
                    finalViewHolder.imageView.setColorFilter(Color.parseColor("#707070"));
                    finalViewHolder.textView.setTextColor(Color.parseColor("#000000"));
                    arrayList.get(position).setChecked(false);

                    checkedList.remove(""+position);
                }
            }
        });

        return convertView;
    }

    private class ViewHolder{
        public ImageView imageView;
        public TextView textView;
    }
}
