package com.kmw.soom2.Home.HomeAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.R;

import java.util.ArrayList;

import static com.kmw.soom2.Home.HomeActivity.SymptomActivitys.MedicinRecordActivity.medicineTakingItems1;

public class MedicineRecordListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<MedicineTakingItem> itemArrayList = new ArrayList<>();

    public MedicineRecordListAdapter(LayoutInflater inflater, Context context) {
        this.context = context;
        this.inflater = inflater;
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

        ViewHolder viewHolder = null;

        if (view == null){

            viewHolder = new ViewHolder();

            view = inflater.inflate(R.layout.view_medicine_record_list_item,null);

            viewHolder.imgIcon = (ImageView)view.findViewById(R.id.img_medicine_record_list_item_icon);
            viewHolder.txtName = (TextView)view.findViewById(R.id.txt_medicine_record_list_item_name);
            viewHolder.linStatus = (LinearLayout)view.findViewById(R.id.lin_medicine_record_list_item_status);
            viewHolder.txtStatus = (TextView)view.findViewById(R.id.txt_medicine_record_list_item_status);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        for (int j = 0; j < Utils.MEDICINE_TYPE_LIST.size(); j++){
            if (Utils.MEDICINE_TYPE_LIST.get(j).getMedicineTypeNo() == itemArrayList.get(i).getMedicineTypeNo()){
                if (Utils.MEDICINE_TYPE_LIST.get(j).getTypeImg().length() != 0){
                    Glide.with(context).load("http:"+Utils.MEDICINE_TYPE_LIST.get(j).getTypeImg()).into(viewHolder.imgIcon);
                }
            }
        }

        viewHolder.txtName.setText(itemArrayList.get(i).getMedicineKo());
        viewHolder.linStatus.setBackgroundTintList(context.getColorStateList(R.color.d2d6d9));
        viewHolder.txtStatus.setText("먹음");

        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.linStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemArrayList.get(i).getEmergencyFlag().equals("1")){
                    finalViewHolder.linStatus.setBackgroundTintList(context.getColorStateList(R.color.ff6767));
                    finalViewHolder.txtStatus.setText("먹음");

                    medicineTakingItems1.get(i).setEmergencyFlag("2");

                }else if (itemArrayList.get(i).getEmergencyFlag().equals("2")){
                    finalViewHolder.linStatus.setBackgroundTintList(context.getColorStateList(R.color.d2d6d9));
                    finalViewHolder.txtStatus.setText("먹음");

                    medicineTakingItems1.get(i).setEmergencyFlag("0");

                }else{
                    finalViewHolder.linStatus.setBackgroundTintList(context.getColorStateList(R.color.colorPrimary));
                    finalViewHolder.txtStatus.setText("먹음");

                    medicineTakingItems1.get(i).setEmergencyFlag("1");
                }
            }
        });

        return view;
    }

    public void addItem(MedicineTakingItem medicineTakingItem){
        itemArrayList.add(medicineTakingItem);
    }

    public class ViewHolder{
        ImageView imgIcon;
        TextView txtName;
        LinearLayout linStatus;
        TextView txtStatus;
    }
}
