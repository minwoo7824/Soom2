package com.kmw.soom2.MyPage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kmw.soom2.MyPage.Activity.HospitalSearchActivity;
import com.kmw.soom2.MyPage.Item.HospitalListItem;
import com.kmw.soom2.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class HospitalListAdapter extends BaseAdapter {

    Context context;
    ArrayList<HospitalListItem> itemArrayList = new ArrayList<>();

    public HospitalListAdapter(Context context) {
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
            view = inflater.inflate(R.layout.view_hospital_list_item,null);
            viewHolder.txtTitle = (TextView)view.findViewById(R.id.txt_hospital_list_item_title);
            viewHolder.txtAddress = (TextView)view.findViewById(R.id.txt_hospital_list_item_address);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.txtTitle.setText(itemArrayList.get(i).getHospitalName());
        viewHolder.txtAddress.setText(itemArrayList.get(i).getHospitalAddress());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("hospitalName",itemArrayList.get(i).getHospitalName());
                intent.putExtra("hospitalAddress",itemArrayList.get(i).getHospitalAddress());

                ((HospitalSearchActivity)context).setResult(RESULT_OK,intent);
                ((HospitalSearchActivity)context).onBackPressed();
            }
        });

        return view;
    }

    public void addItem(String name, String address){
        HospitalListItem hospitalListItem = new HospitalListItem();
        hospitalListItem.setHospitalName(name);
        hospitalListItem.setHospitalAddress(address);
        itemArrayList.add(hospitalListItem);
    }

    private class ViewHolder{
        public TextView txtTitle,txtAddress;
    }
}
