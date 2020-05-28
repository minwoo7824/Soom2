package com.kmw.soom2.Home.HomeAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kmw.soom2.Home.HomeItem.TakingRecordItemList;
import com.kmw.soom2.R;

import java.util.ArrayList;

import static com.kmw.soom2.Home.HomeItem.TakingRecordItemList.BODY_TYPE;
import static com.kmw.soom2.Home.HomeItem.TakingRecordItemList.HEADER_TYPE;

public class TakingRecordRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<TakingRecordItemList> arrayList;


    public TakingRecordRecyclerViewAdapter(ArrayList<TakingRecordItemList> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public int getItemViewType(int position) {
        TakingRecordItemList takingRecordItemList = arrayList.get(position);
        if (takingRecordItemList != null) {
            return takingRecordItemList.getViewType();
        }
        return 0;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case HEADER_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.taking_record_header, parent, false);
                return new ViewHolderHeader(view);
            case BODY_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.taking_record_item_list, parent, false);
                return new ViewHolderItem(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TakingRecordItemList takingRecordItemList = arrayList.get(position);
        switch (takingRecordItemList.getViewType()) {
            case HEADER_TYPE:
                ((ViewHolderHeader) holder).title.setText(takingRecordItemList.getTitle());
                break;
            case BODY_TYPE:
                ((ViewHolderItem) holder).title.setText(takingRecordItemList.getTitle());
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
            title = itemView.findViewById(R.id.mention_header);

        }
    }

    class ViewHolderItem extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.mention_item);
        }
    }

}
