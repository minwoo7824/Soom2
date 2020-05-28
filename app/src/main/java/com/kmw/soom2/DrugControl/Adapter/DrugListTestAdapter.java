package com.kmw.soom2.DrugControl.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.HomeActivity.MedicineInsert.MedicineInsertActivity;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.kmw.soom2.Common.Utils.calDateBetweenAandB;
import static com.kmw.soom2.Common.Utils.itemsArrayList;

public class DrugListTestAdapter extends BaseAdapter {

    private String TAG = "DrugListTestAdapter";
    Context context;
    ArrayList<MedicineTakingItem> medicineTakingItems = new ArrayList<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    SimpleDateFormat dateFormatBefore = new SimpleDateFormat("yyyyMMdd");

    public DrugListTestAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return medicineTakingItems.size();
    }

    @Override
    public Object getItem(int position) {
        return medicineTakingItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_talking_medicine_list_item,null);


        }else{

        }

        TextView txtName = (TextView)convertView.findViewById(R.id.txt_talking_medicine_list_item_name);
        TextView txtDate = (TextView)convertView.findViewById(R.id.txt_talking_medicine_list_item_title_date);
        SeekBar seekBar = (SeekBar)convertView.findViewById(R.id.seekbar_talking_medicine_list_item);
        TextView txtAmount = (TextView)convertView.findViewById(R.id.txt_talking_medicine_list_item_amount);
        TextView txtCnt = (TextView)convertView.findViewById(R.id.txt_talking_medicine_list_item_cnt);
        TextView txtPeriod = (TextView)convertView.findViewById(R.id.txt_talking_medicine_list_item_vice_date);
        ImageView imgIcon = (ImageView)convertView.findViewById(R.id.img_talking_medicine_list_item_icon);

        seekBar.setEnabled(false);
        seekBar.setThumb(getThumb((int) calDateBetweenAandB(medicineTakingItems.get(position).getStartDt(),dateFormatBefore.format(new Date(System.currentTimeMillis())))));
        seekBar.setMax((int) calDateBetweenAandB(medicineTakingItems.get(position).getStartDt(),medicineTakingItems.get(position).getEndDt()));
        seekBar.setProgress((int) calDateBetweenAandB(medicineTakingItems.get(position).getStartDt(),dateFormatBefore.format(new Date(System.currentTimeMillis()))));

        for (int i = 0; i < Utils.MEDICINE_TYPE_LIST.size(); i++){
            if (Utils.MEDICINE_TYPE_LIST.get(i).getMedicineTypeNo() == medicineTakingItems.get(position).getMedicineTypeNo()){
                Log.i(TAG,"type img : " + Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg());
                if (Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg().length() != 0){
                    Glide.with(context).load("http:"+Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg()).into(imgIcon);
                }
            }
        }

        txtName.setText(medicineTakingItems.get(position).getMedicineKo());

        try {
            txtDate.setText("시작일 "+dateFormat.format(dateFormatBefore.parse(medicineTakingItems.get(position).getStartDt())) + " | " + "종료일 "+dateFormat.format(dateFormatBefore.parse(medicineTakingItems.get(position).getEndDt())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (medicineTakingItems.get(position).getFrequency() == 0 || medicineTakingItems.get(position).getFrequency() == -1){
            txtCnt.setText("필요시");
        }else{
            txtCnt.setText("하루 " + medicineTakingItems.get(position).getStartDt() + "번");
        }

        txtAmount.setText(""+medicineTakingItems.get(position).getVolume() + medicineTakingItems.get(position).getUnit());

        txtPeriod.setText(""+ calDateBetweenAandB(medicineTakingItems.get(position).getStartDt(),dateFormatBefore.format(new Date(System.currentTimeMillis()))) + "/" + calDateBetweenAandB(medicineTakingItems.get(position).getStartDt(),medicineTakingItems.get(position).getEndDt()));

//        listView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getActivity(), MedicineInsertActivity.class);
//                i.putExtra("medicineNo",medicineNo);
//                i.putExtra("historyNo",historyNo);
//                i.putExtra("name",name);
//                i.putExtra("startDt",startDt);
//                i.putExtra("endDt",endDt);
//                i.putExtra("frequency",frequency);
//                i.putExtra("volume",volume);
//                i.putExtra("unit",unit);
//                i.putExtra("efficacy",efficacy);
//                i.putExtra("instructions",instruction);
//                i.putExtra("information",information);
//                i.putExtra("stabilityRating",stabilty);
//                i.putExtra("precautions",precaution);
//                i.putExtra("medicineTypeNo",medicineTypeNo);
//                i.putExtra("medicineImg",medicineImg);
//
//                startActivity(i);
//            }
//        });

        return convertView;
    }

    public Drawable getThumb(int progress) {
        View thumbView = LayoutInflater.from(context).inflate(R.layout.view_custom_seekbar_thumb, null, false);
        ((TextView) thumbView.findViewById(R.id.tvProgress)).setText(progress + "");

        thumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        thumbView.layout(0, 0, thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight());
        thumbView.draw(canvas);

        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public void addItem(MedicineTakingItem medicineTakingItem){
        medicineTakingItems.add(medicineTakingItem);
    }
}
