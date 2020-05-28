package com.kmw.soom2.DrugControl.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kmw.soom2.Common.DbOpenHelper;
import com.kmw.soom2.DrugControl.Activity.DrugAlarmInsertActivity;
import com.kmw.soom2.DrugControl.Item.DrugAlarmItemList;
import com.kmw.soom2.Home.HomeActivity.MainActivity;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DrugAlarmRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<DrugAlarmItemList> arrayList = new ArrayList<>();
    Context context;
    SimpleDateFormat dateTimeStatusFormat = new SimpleDateFormat("a");
    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("hh:mm");
    DbOpenHelper mDbOpenHelper;

    public DrugAlarmRecyclerViewAdapter(Context context, DbOpenHelper mDbOpenHelper) {
        this.context = context;
        this.mDbOpenHelper = mDbOpenHelper;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.view_medicine_alarm_list_item, parent, false);
        return new ViewHolderHeader(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DrugAlarmItemList item = arrayList.get(position);

        ((ViewHolderHeader)holder).txtTimeStatus.setText(dateTimeStatusFormat.format(new Date(item.getSelectTime())));
        if (dateTimeFormat.format(new Date(item.getSelectTime())).indexOf("0") == 0){
            ((ViewHolderHeader)holder).txtTime.setText(dateTimeFormat.format(new Date(item.getSelectTime())).substring(1,5));
        }else{
            ((ViewHolderHeader)holder).txtTime.setText(dateTimeFormat.format(new Date(item.getSelectTime())));
        }

        ((ViewHolderHeader)holder).swhPush.setChecked(item.getPushCheck()  == 1 ? true : false);

        String[] days = item.getSelectDay().split(",");

        for (int i = 0; i < days.length; i++){
            if (days[i].equals("1")){
                ((ViewHolderHeader)holder).txtSun.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }else if (days[i].equals("2")){
                ((ViewHolderHeader)holder).txtMon.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }else if (days[i].equals("3")){
                ((ViewHolderHeader)holder).txtTue.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }else if (days[i].equals("4")){
                ((ViewHolderHeader)holder).txtWed.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }else if (days[i].equals("5")){
                ((ViewHolderHeader)holder).txtThur.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }else if (days[i].equals("6")){
                ((ViewHolderHeader)holder).txtFir.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }else if (days[i].equals("7")){
                ((ViewHolderHeader)holder).txtSat.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
        }

        ((ViewHolderHeader)holder).linParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DrugAlarmInsertActivity.class);
                i.putExtra("alarmId",item.getIdx());
                i.putExtra("alarmTime",item.getSelectTime());
                i.putExtra("alarmDay",item.getSelectDay());
                ((MainActivity)context).startActivityForResult(i,3333);
            }
        });

        ((ViewHolderHeader)holder).swhPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDbOpenHelper.updateColumn(item.getIdx(),"drugAlarm",item.getSelectDay(),item.getSelectTime(),isChecked == true ? 1 : 2);
            }
        });


    }

    class ViewHolderBanner extends RecyclerView.ViewHolder {
        ImageView bannerImage;

        public ViewHolderBanner(@NonNull View itemView) {
            super(itemView);
            bannerImage = (ImageView) itemView.findViewById(R.id.drug_alarm_banner);
        }
    }

    class ViewHolderHeader extends RecyclerView.ViewHolder {
        TextView txtTimeStatus;
        TextView txtTime;
        TextView txtSun;
        TextView txtMon;
        TextView txtTue;
        TextView txtWed;
        TextView txtThur;
        TextView txtFir ;
        TextView txtSat ;
        Switch   swhPush;
        LinearLayout linParent;
        public ViewHolderHeader(@NonNull View itemView) {
            super(itemView);
            txtTimeStatus = (TextView)itemView.findViewById(R.id.txt_medicine_alarm_list_item_time_status);
            txtTime = (TextView)itemView.findViewById(R.id.txt_medicine_alarm_list_item_time);
            txtSun = (TextView)itemView.findViewById(R.id.txt_medicine_alarm_list_item_sun);
            txtMon = (TextView)itemView.findViewById(R.id.txt_medicine_alarm_list_item_mon);
            txtTue = (TextView)itemView.findViewById(R.id.txt_medicine_alarm_list_item_tue);
            txtWed = (TextView)itemView.findViewById(R.id.txt_medicine_alarm_list_item_wed);
            txtThur = (TextView)itemView.findViewById(R.id.txt_medicine_alarm_list_item_thur);
            txtFir = (TextView)itemView.findViewById(R.id.txt_medicine_alarm_list_item_fri);
            txtSat = (TextView)itemView.findViewById(R.id.txt_medicine_alarm_list_item_sat);
            swhPush = (Switch)itemView.findViewById(R.id.swh_medicine_alarm_list_item);
            linParent = (LinearLayout)itemView.findViewById(R.id.lin_medicine_alarm_list_item_parent);

            itemView.setTag(false);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addItem(int idx, String selectDay, long selectTime, int pushCheck){
        DrugAlarmItemList drugAlarmItemList = new DrugAlarmItemList();
        drugAlarmItemList.setIdx(idx);
        drugAlarmItemList.setSelectDay(selectDay);
        drugAlarmItemList.setSelectTime(selectTime);
        drugAlarmItemList.setPushCheck(pushCheck);

        arrayList.add(drugAlarmItemList);
    }

}
