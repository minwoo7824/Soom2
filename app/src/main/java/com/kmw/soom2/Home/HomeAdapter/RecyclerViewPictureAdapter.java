package com.kmw.soom2.Home.HomeAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kmw.soom2.Home.HomeItem.PictureItem;
import com.kmw.soom2.R;

import java.util.ArrayList;

public class RecyclerViewPictureAdapter extends RecyclerView.Adapter<RecyclerViewPictureAdapter.ViewHolder> {
    Context context;
    ArrayList<PictureItem> arrayList;

    public RecyclerViewPictureAdapter(Context context, ArrayList<PictureItem> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,deleteImage;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.picture_image_view);
            this.deleteImage = (ImageView) itemView.findViewById(R.id.picture_delete_button_image);
            deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    PictureItem item =arrayList.get(pos);
                    if(pos != RecyclerView.NO_POSITION){
                        arrayList.remove(pos);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerViewPictureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.recycler_view_picture_list, parent, false);
        RecyclerViewPictureAdapter.ViewHolder vh = new RecyclerViewPictureAdapter.ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewPictureAdapter.ViewHolder holder, int position) {
        PictureItem item = arrayList.get(position);
        Glide.with(context)
                .asBitmap().load(arrayList.get(position));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
