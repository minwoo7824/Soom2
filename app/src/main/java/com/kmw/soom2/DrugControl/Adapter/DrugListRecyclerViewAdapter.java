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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.DrugControl.Item.DrugListItemList;
import com.kmw.soom2.Home.HomeActivity.MedicineInsert.MedicineInsertActivity;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.kmw.soom2.Common.Utils.calDateBetweenAandB;
import static com.kmw.soom2.DrugControl.Item.DrugListItemList.BANNER;
import static com.kmw.soom2.DrugControl.Item.DrugListItemList.COMPLETE;
import static com.kmw.soom2.DrugControl.Item.DrugListItemList.HEADER;
import static com.kmw.soom2.DrugControl.Item.DrugListItemList.ITEM;

public class DrugListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = "DrugListTestAdapter";
    ArrayList<MedicineTakingItem> medicineTakingItems = new ArrayList<>();
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    SimpleDateFormat dateFormatBefore = new SimpleDateFormat("yyyyMMdd");

    public DrugListRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.view_talking_medicine_list_item, parent, false);
        return new ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MedicineTakingItem item = medicineTakingItems.get(position);

        ((ViewHolderItem)holder).seekBar.setEnabled(false);
        ((ViewHolderItem)holder).seekBar.setThumb(getThumb((int) calDateBetweenAandB(medicineTakingItems.get(position).getStartDt(),dateFormatBefore.format(new Date(System.currentTimeMillis())))));
        ((ViewHolderItem)holder).seekBar.setMax((int) calDateBetweenAandB(medicineTakingItems.get(position).getStartDt(),medicineTakingItems.get(position).getEndDt()));
        ((ViewHolderItem)holder).seekBar.setProgress((int) calDateBetweenAandB(medicineTakingItems.get(position).getStartDt(),dateFormatBefore.format(new Date(System.currentTimeMillis()))));

        for (int i = 0; i < Utils.MEDICINE_TYPE_LIST.size(); i++){
            if (Utils.MEDICINE_TYPE_LIST.get(i).getMedicineTypeNo() == medicineTakingItems.get(position).getMedicineTypeNo()){
                Log.i(TAG,"type img : " + Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg());
                if (Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg().length() != 0){
                    Glide.with(context).load("http:"+Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg()).into(((ViewHolderItem)holder).imgIcon);
                }
            }
        }

        ((ViewHolderItem)holder).txtName.setText(medicineTakingItems.get(position).getMedicineKo());

        try {
            ((ViewHolderItem)holder).txtDate.setText("시작일 "+dateFormat.format(dateFormatBefore.parse(medicineTakingItems.get(position).getStartDt())) + " | " + "종료일 "+dateFormat.format(dateFormatBefore.parse(medicineTakingItems.get(position).getEndDt())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (medicineTakingItems.get(position).getFrequency() == 0 || medicineTakingItems.get(position).getFrequency() == -1){
            ((ViewHolderItem)holder).txtCnt.setText("필요시");
        }else{
            ((ViewHolderItem)holder).txtCnt.setText("하루 " + medicineTakingItems.get(position).getFrequency() + "번");
        }

        ((ViewHolderItem)holder).txtAmount.setText(""+medicineTakingItems.get(position).getVolume() + medicineTakingItems.get(position).getUnit());

        ((ViewHolderItem)holder).txtPeriod.setText(""+ calDateBetweenAandB(medicineTakingItems.get(position).getStartDt(),dateFormatBefore.format(new Date(System.currentTimeMillis()))) + "/" + calDateBetweenAandB(medicineTakingItems.get(position).getStartDt(),medicineTakingItems.get(position).getEndDt()));

        ((ViewHolderItem)holder).linParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MedicineInsertActivity.class);
                i.putExtra("medicineNo",medicineTakingItems.get(position).getMedicineNo());
                i.putExtra("historyNo",medicineTakingItems.get(position).getMedicineHistoryNo());
                i.putExtra("name",medicineTakingItems.get(position).getMedicineKo());
                i.putExtra("startDt",medicineTakingItems.get(position).getStartDt());
                i.putExtra("endDt",medicineTakingItems.get(position).getEndDt());
                i.putExtra("frequency",medicineTakingItems.get(position).getFrequency());
                i.putExtra("volume",medicineTakingItems.get(position).getVolume());
                i.putExtra("unit",medicineTakingItems.get(position).getUnit());
                i.putExtra("efficacy",medicineTakingItems.get(position).getEfficacy());
                i.putExtra("instructions",medicineTakingItems.get(position).getInstructions());
                i.putExtra("information",medicineTakingItems.get(position).getInformation());
                i.putExtra("stabilityRating",medicineTakingItems.get(position).getStabiltyRationg());
                i.putExtra("precautions",medicineTakingItems.get(position).getPrecaution());
                i.putExtra("medicineTypeNo",medicineTakingItems.get(position).getMedicineTypeNo());
                i.putExtra("medicineImg",medicineTakingItems.get(position).getMedicineImg());

                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicineTakingItems.size();
    }

    @Override
    public int getItemViewType(int position) {

        return 0;
    }


    class ViewHolderBanner extends RecyclerView.ViewHolder {
        ImageView bannerImage;

        public ViewHolderBanner(@NonNull View itemView) {
            super(itemView);
            bannerImage = (ImageView) itemView.findViewById(R.id.drug_list_banner);
            itemView.setTag(false);

        }
    }

    class ViewHolderComplete extends RecyclerView.ViewHolder {
        TextView textView;


        public ViewHolderComplete(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.taking_complete);
            itemView.setTag(false);

        }
    }

    class ViewHolderHeader extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolderHeader(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.taking_drug_text_view);
            itemView.setTag(true);

        }
    }


    class ViewHolderItem extends RecyclerView.ViewHolder {
        TextView txtName,txtDate,txtAmount,txtCnt,txtPeriod;
        SeekBar seekBar;
        ImageView imgIcon;
        LinearLayout linParent;

        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView)itemView.findViewById(R.id.txt_talking_medicine_list_item_name);
            txtDate = (TextView)itemView.findViewById(R.id.txt_talking_medicine_list_item_title_date);
            seekBar = (SeekBar)itemView.findViewById(R.id.seekbar_talking_medicine_list_item);
            txtAmount = (TextView)itemView.findViewById(R.id.txt_talking_medicine_list_item_amount);
            txtCnt = (TextView)itemView.findViewById(R.id.txt_talking_medicine_list_item_cnt);
            txtPeriod = (TextView)itemView.findViewById(R.id.txt_talking_medicine_list_item_vice_date);
            imgIcon = (ImageView)itemView.findViewById(R.id.img_talking_medicine_list_item_icon);
            linParent = (LinearLayout)itemView.findViewById(R.id.lin_talking_medicine_list_item_parent);
            itemView.setTag(false);

        }
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
