package com.kmw.soom2.Home.HomeActivity.MedicineInsert.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.HomeActivity.MedicineInsert.MedicineInsertActivity;
import com.kmw.soom2.Home.HomeItem.RecyclerViewItemList;
import com.kmw.soom2.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Handler;

import static com.kmw.soom2.DrugControl.Fragment.ParentFragment.DrugControlMainFragment.medicineTakingItems;
import static com.kmw.soom2.Home.HomeItem.RecyclerViewItemList.HEADER_TYPE;
import static com.kmw.soom2.Home.HomeItem.RecyclerViewItemList.ITEM_TYPE;

public class MedicineSelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = "RecyclerViewAdapter";
    private ArrayList<RecyclerViewItemList> arrayList;
    Context context;
    SimpleDateFormat dateFormatBefore = new SimpleDateFormat("yyyyMMdd");

    public MedicineSelectAdapter(Context context, ArrayList<RecyclerViewItemList> list) {
        this.context = context;
        this.arrayList = list;
    }

    @Override
    public int getItemViewType(int position) {
        RecyclerViewItemList item = arrayList.get(position);
        if (item != null) {
            return item.getViewType();
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case HEADER_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_medicine_select_parent, parent, false);
                return new ViewHolderHeader(view);
            case ITEM_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_medicine_select_child, parent, false);
                return new ViewHolderItem(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final RecyclerViewItemList item = arrayList.get(position);
        switch (item.getViewType()) {
            case RecyclerViewItemList.HEADER_TYPE:
                ((ViewHolderHeader) holder).title.setText(item.getTitle());

                break;
            case RecyclerViewItemList.ITEM_TYPE:
                ((ViewHolderItem) holder).txtName.setText(item.getMedicineTakingItem().getMedicineKo());
//                Glide.with(context).load(item.getMedicineTakingItem().getMedicineImg()).into(((ViewHolderItem)holder).imgIcon);
//                ((ViewHolderItem)holder).imgIcon.setImageResource(item.getMedicineTakingItem().getMedicineImg());
                for (int i = 0; i < Utils.MEDICINE_TYPE_LIST.size(); i++) {
                    if (Utils.MEDICINE_TYPE_LIST.get(i).getMedicineTypeNo() == item.getMedicineTakingItem().getMedicineTypeNo()) {
                        Log.i(TAG, "type img : " + Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg() + i + "번째");
                        if (Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg().length() != 0) {

                            Glide.with(context).load("http:" + Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg()).into(((ViewHolderItem) holder).imgIcon);
                        } else {
                            Glide.with(context).load("http://soom2.testserver-1.com:8080/img/admin/1585736176275.png").into(((ViewHolderItem) holder).imgIcon);

                        }
                    }
                }

                ((ViewHolderItem) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        boolean check = false;
                        for (int x = 0; x < medicineTakingItems.size(); x++) {
                                if (medicineTakingItems.get(x).getMedicineNo() == item.getMedicineTakingItem().getMedicineNo() && medicineTakingItems.get(x).getAliveFlag() == 1) {

                                        check = true;
                                        break;
                                    } else {
                                        check = false;
                                    }

                        }
                        if (check) {

                            Utils.OneBtnPopup(context, "약 등록", "이미 추가된 약입니다.", "확인");
                        } else {
                            Intent i = new Intent(context, MedicineInsertActivity.class);
                            i.putExtra("medicineNo", item.getMedicineTakingItem().getMedicineNo());
                            i.putExtra("medicineKo", item.getMedicineTakingItem().getMedicineKo());
                            i.putExtra("medicineImg", item.getMedicineTakingItem().getMedicineImg());
                            i.putExtra("manufacturer", item.getMedicineTakingItem().getManufacturer());

                            i.putExtra("instructions", item.getMedicineTakingItem().getInstructions());
                            i.putExtra("ingredient", item.getMedicineTakingItem().getIngredient());
                            i.putExtra("storageMethod", item.getMedicineTakingItem().getStorageMethod());
                            i.putExtra("efficacy", item.getMedicineTakingItem().getEfficacy());
                            i.putExtra("information", item.getMedicineTakingItem().getInformation());
                            i.putExtra("stabilityRating", item.getMedicineTakingItem().getStabiltyRationg());
                            i.putExtra("precautions", item.getMedicineTakingItem().getPrecaution());
                            Log.i(TAG,"끝!"+item.getMedicineTakingItem().getEndDt());
                            context.startActivity(i);
                        }

                    }
                });
                break;
        }
    }

    @NonNull
    @Override
    public int getItemCount() {
        return arrayList.size();

    }

    class ViewHolderHeader extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolderHeader(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_medicine_select_header_title);
            itemView.setTag(true);
        }
    }

    class ViewHolderItem extends RecyclerView.ViewHolder {
        TextView txtName;
        ImageView imgIcon;

        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_medicine_select_child_name);
            imgIcon = itemView.findViewById(R.id.img_medicine_select_child_icon);
            itemView.setTag(false);
        }
    }
}

