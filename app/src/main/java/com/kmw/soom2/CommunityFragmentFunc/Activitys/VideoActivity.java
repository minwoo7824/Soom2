package com.kmw.soom2.CommunityFragmentFunc.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.kmw.soom2.Home.HomeAdapter.MemoVideoAdapter;
import com.kmw.soom2.Home.HomeItem.VideoMenuItem;
import com.kmw.soom2.R;

import java.util.ArrayList;
import java.util.Vector;


public class VideoActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "VideoActivity";
    TextView txtBack,txtDone;
    GridView gridView;
    MemoVideoAdapter adapter;
    public static ArrayList<Uri> videoPathList = new ArrayList<>();
    public static Bitmap bitmapThumbnail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        FindViewById();
    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_video_back);
        txtDone = (TextView)findViewById(R.id.txt_video_done);
        gridView = (GridView)findViewById(R.id.grid_video);

        adapter = new MemoVideoAdapter(this);
        gridView.setAdapter(adapter);
        getVideo();
        adapter.setUp(getVideo());

        txtBack.setOnClickListener(this);
        txtDone.setOnClickListener(this);
    }


    private Vector<VideoMenuItem> getVideo() {
        String[] proj = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATA
        };
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        Vector<VideoMenuItem> menus = new Vector<>();
        assert cursor != null;
        while (cursor.moveToNext()) {
            String title = cursor.getString(1);
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID));
            Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(getContentResolver(), id, MediaStore.Video.Thumbnails.MINI_KIND, null);
            // 썸네일 크기 변경할 때.
            //Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, width, height);
            String data = cursor.getString(2);
            menus.add(new VideoMenuItem(title, bitmap, Uri.parse(data)));
        }

        cursor.close();
        return menus;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_video_back : {
                onBackPressed();
                break;
            }
            case R.id.txt_video_done : {

                Bitmap resizedBmp = Bitmap.createScaledBitmap(bitmapThumbnail, (int) 150, (int) 150, true);

                Log.i(TAG,"video : " + resizedBmp);

                if (videoPathList.size() > 0){
                    Intent i = new Intent();
                    i.putExtra("videoThumbnail",resizedBmp);
                    i.putExtra("videoPath",String.valueOf(videoPathList.get(0)));
                    setResult(RESULT_OK,i);
                    onBackPressed();
                }else{
                    onBackPressed();
                }
                break;
            }
        }
    }
}
