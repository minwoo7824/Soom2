package com.kmw.soom2.CommunityFragmentFunc.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kmw.soom2.CommunityFragmentFunc.Activitys.GalleryActivity;
import com.kmw.soom2.CommunityFragmentFunc.Items.GalleryItem;
import com.kmw.soom2.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.kmw.soom2.Common.Utils.FILE_PATH;
import static com.kmw.soom2.Common.Utils.PICK_URI;

public class GalleryAdapter extends BaseAdapter {

    private String TAG = "GalleryAdapter";

    Context mContext;
    ArrayList<GalleryItem> itemArrayLists = new ArrayList<>();

    public GalleryAdapter(Context mContext, ArrayList<GalleryItem> itemArrayLists) {
        this.mContext = mContext;
        this.itemArrayLists.addAll(itemArrayLists);
    }

    @Override
    public int getCount() {
        return itemArrayLists.size();
    }

    @Override
    public Object getItem(int i) {
        return itemArrayLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = new ViewHolder();

        if (view == null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.view_gallery_list_item,viewGroup,false);

            viewHolder.imageView = (ImageView)view.findViewById(R.id.img_gallery_list_item);
            viewHolder.txtCheck = (TextView) view.findViewById(R.id.txt_gallery_list_item_num);
            viewHolder.linCheck = (LinearLayout)view.findViewById(R.id.lin_gallery_list_item_num);

            view.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        final ViewHolder finalViewHolder = viewHolder;

        Display display= ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        if (i == 0){
            Glide.with(mContext).load(R.drawable.ic_community_camera).apply(new RequestOptions().override(display.getWidth()/3,display.getWidth()/3)).into(viewHolder.imageView);
            viewHolder.imageView.setBackgroundColor(mContext.getResources().getColor(R.color.f5f5f5));
            viewHolder.linCheck.setVisibility(View.GONE);
        }else{
            viewHolder.linCheck.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(itemArrayLists.get(i).getDataTest()).apply(new RequestOptions().override(display.getWidth()/3,display.getWidth()/3)).into(viewHolder.imageView);
        }

//        final ViewHolder finalViewHolder = (ViewHolder) view.getTag();

        if (GalleryActivity.galleryImagePathList.size() > 0){
            if (GalleryActivity.galleryImagePathList.contains(getImageInfo(itemArrayLists.get(i).getIdTest()))){
                finalViewHolder.txtCheck.setBackgroundResource(R.drawable.radius_50dp);
                finalViewHolder.txtCheck.setBackgroundTintList(mContext.getColorStateList(R.color.colorPrimary));
                finalViewHolder.txtCheck.setText(""+(GalleryActivity.galleryImagePathList.indexOf(getImageInfo(itemArrayLists.get(i).getIdTest()))+1));
            }else{
                finalViewHolder.txtCheck.setBackgroundResource(0);
                finalViewHolder.txtCheck.setText("");
            }
        }

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0){

                    File dir = null;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Soom");
                    }else{
                        dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Soom");
                    }

                    if(!dir.exists()) {
                        dir.mkdir();
                    }

                    File filePath = null;
                    try {
                        filePath = File.createTempFile("IMG", ".jpg", dir);
                        if(!filePath.exists()) {
                            filePath.createNewFile();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    FILE_PATH = String.valueOf(filePath);
                    Log.i(TAG,"filePath : " + FILE_PATH);
                    Uri providerURI = FileProvider.getUriForFile( mContext ,mContext.getPackageName()+".fileprovider" , filePath);
                    PICK_URI = providerURI;
                    intent.putExtra(MediaStore.EXTRA_OUTPUT , providerURI);
                    ((GalleryActivity)mContext).startActivityForResult(intent,2222);
                }else {

                }
            }
        });

        viewHolder.txtCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GalleryActivity.galleryImagePathList.contains(getImageInfo(itemArrayLists.get(i).getIdTest()))){
                    finalViewHolder.txtCheck.setBackgroundResource(0);
                    GalleryActivity.galleryImagePathList.remove(getImageInfo(itemArrayLists.get(i).getIdTest()));
                    finalViewHolder.txtCheck.setText("");
                }else{
                    if (GalleryActivity.galleryImagePathList.size() < 5){
                        finalViewHolder.txtCheck.setBackgroundResource(R.drawable.radius_50dp);
                        finalViewHolder.txtCheck.setBackgroundTintList(mContext.getColorStateList(R.color.colorPrimary));
                        GalleryActivity.galleryImagePathList.add(getImageInfo(itemArrayLists.get(i).getIdTest()));
                        finalViewHolder.txtCheck.setText(""+ GalleryActivity.galleryImagePathList.size());
                    }else{
                        Toast.makeText(mContext, "사진은 첨부는 5장까지 가능합니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

    private class ViewHolder{
        public ImageView imageView;
        public TextView txtCheck;
        public LinearLayout linCheck;
    }

    private String getImageInfo(String thumbID){
        String imageDataPath = null;
        String[] proj = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor imageCursor = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj, "_ID='"+ thumbID +"'", null, null);

        if (imageCursor != null && imageCursor.moveToFirst()){
            if (imageCursor.getCount() > 0){
                int imgData = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                imageDataPath = imageCursor.getString(imgData);
            }
        }
        imageCursor.close();
        return imageDataPath;
    }
}