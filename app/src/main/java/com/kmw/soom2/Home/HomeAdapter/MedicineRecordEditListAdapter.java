package com.kmw.soom2.Home.HomeAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.HomeActivity.SymptomActivitys.MedicineRecordEditActivity;
import com.kmw.soom2.Home.HomeItem.MedicineRecordEditListItem;
import com.kmw.soom2.R;

import java.util.ArrayList;

import static com.kmw.soom2.Home.HomeActivity.SymptomActivitys.MedicineRecordEditActivity.etcItemArrayList;

public class MedicineRecordEditListAdapter extends BaseAdapter {

    private String TAG = "MedicineRecordEditListAdapter";
    Context context;
    LayoutInflater inflater;
    ArrayList<MedicineRecordEditListItem> itemArrayList = new ArrayList<>();

    public MedicineRecordEditListAdapter(LayoutInflater inflater, Context context) {
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

            view = inflater.inflate(R.layout.view_medicine_record_edit_list_item,null);

            viewHolder.imgIcon = (ImageView)view.findViewById(R.id.img_medicine_record_edit_list_item_icon);
            viewHolder.txtName = (TextView)view.findViewById(R.id.txt_medicine_record_edit_list_item_name);
            viewHolder.txtContents = (TextView)view.findViewById(R.id.txt_medicine_record_edit_list_item_contents);
            viewHolder.txtRemove = (TextView)view.findViewById(R.id.txt_medicine_record_edit_list_item_remove);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        if (itemArrayList.get(i).getFlag().equals("1")){
            viewHolder.txtContents.setText(itemArrayList.get(i).getContents() + " 일반복용");
        }else{
            viewHolder.txtContents.setText(itemArrayList.get(i).getContents() + " 응급복용");
        }

        viewHolder.txtName.setText(itemArrayList.get(i).getName());

        for (int j = 0; j < Utils.MEDICINE_LIST.size(); j++){
            if (Utils.MEDICINE_LIST.get(j).getMedicineNo() == itemArrayList.get(i).getMedicineNo()){
                for (int x = 0; x < Utils.MEDICINE_TYPE_LIST.size(); x++){
                    if (Utils.MEDICINE_LIST.get(j).getMedicineTypeNo() == Utils.MEDICINE_TYPE_LIST.get(x).getMedicineTypeNo()){
                        if (Utils.MEDICINE_TYPE_LIST.get(x).getTypeImg().length() != 0){
                            Glide.with(context).load("http:"+Utils.MEDICINE_TYPE_LIST.get(x).getTypeImg()).into(viewHolder.imgIcon);
                            break;
                        }
                    }
                }
            }
        }



        viewHolder.txtRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwoBtnPopup(0, i, context,"복용기록 삭제","선택한 복용기록을\n삭제하시겠습니까?","취소","삭제");
            }
        });

        return view;
    }

    public void TwoBtnPopup(int no , final int position, final Context context, String title, String contents, String btnLeftText, String btnRightText){

        final Dialog dateTimeDialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.two_btn_popup,null);

        TextView txtTitle = (TextView)layout.findViewById(R.id.txt_two_btn_popup_title);
        TextView txtContents = (TextView)layout.findViewById(R.id.txt_two_btn_popup_contents);
        final Button btnLeft = (Button)layout.findViewById(R.id.btn_two_btn_popup_left);
        Button btnRight = (Button)layout.findViewById(R.id.btn_two_btn_popup_right);

        txtTitle.setText(title);
        txtContents.setText(contents);
        btnLeft.setText(btnLeftText);
        btnRight.setText(btnRightText);

        dateTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dateTimeDialog.getWindow();

        dateTimeDialog.setContentView(layout);
        dateTimeDialog.show();

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialog.dismiss();
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MedicineRecordEditActivity.DeleteHomeFeedHistoryNetWork(context,itemArrayList.get(position).getHistoryNo()).execute();
                itemArrayList.remove(position);
                etcItemArrayList.remove(position);
                dateTimeDialog.dismiss();
                notifyDataSetChanged();
            }
        });
    }

    public void addItem(String no, String historyNo, String flag, String name, String contents, int medicineNo){
        MedicineRecordEditListItem medicineRecordEditListItem = new MedicineRecordEditListItem();
        medicineRecordEditListItem.setNo(no);
        medicineRecordEditListItem.setHistoryNo(historyNo);
        medicineRecordEditListItem.setFlag(flag);
        medicineRecordEditListItem.setName(name);
        medicineRecordEditListItem.setContents(contents);
        medicineRecordEditListItem.setMedicineNo(medicineNo);

        itemArrayList.add(medicineRecordEditListItem);
    }

    public class ViewHolder{
        ImageView imgIcon;
        TextView txtName,txtContents;
        TextView txtRemove;
    }
}
