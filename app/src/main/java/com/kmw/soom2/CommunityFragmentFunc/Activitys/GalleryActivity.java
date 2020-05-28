package com.kmw.soom2.CommunityFragmentFunc.Activitys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.CommunityFragmentFunc.Adapters.GalleryAdapter;
import com.kmw.soom2.CommunityFragmentFunc.Items.GalleryItem;
import com.kmw.soom2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.ImageResizeUtils.exifOrientationToDegrees;
import static com.kmw.soom2.Common.Utils.FILE_PATH;
import static com.kmw.soom2.Common.Utils.PICK_URI;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "GalleryActivity";
    TextView txtBack,txtDone;
    GridView gridView;
    GalleryAdapter galleryAdapter;
    public static ArrayList<String> galleryImagePathList = new ArrayList<>();
    public static ArrayList<File> bitmapArrayList = new ArrayList<>();
    ArrayList<GalleryItem> itemArrayLists = new ArrayList<>();

    Intent beforeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        beforeIntent = getIntent();
        galleryImagePathList.clear();
        galleryImagePathList = new ArrayList<>();
        itemArrayLists = new ArrayList<>();

        FindViewById();

        checkPermission();

    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_gallery_back);
        txtDone = (TextView)findViewById(R.id.txt_gallery_done);
        gridView = (GridView)findViewById(R.id.grid_gallery);

        txtBack.setOnClickListener(this);
        txtDone.setOnClickListener(this);
    }

    public void checkPermission(){
        int permission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
// If we don't have permissions, ask user for permissions
        if (permission != PackageManager.PERMISSION_GRANTED) {
            String[] PERMISSIONS_STORAGE = {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            };
            int REQUEST_EXTERNAL_STORAGE = 1;

            ActivityCompat.requestPermissions(
                    GalleryActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }else{
            getThumbInfo();
            Collections.sort(itemArrayLists,compare);

            GalleryItem test = new GalleryItem();
            test.setDataTest("");
            test.setIdTest("");
            test.setSizeTest("");
            test.setDate(0);
            itemArrayLists.add(0,test);

            galleryAdapter = new GalleryAdapter(this, itemArrayLists);
            gridView.setAdapter(galleryAdapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1 : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermission();
                } else {
                    Toast.makeText(GalleryActivity.this, "권한요청을 거부했습니다.", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                break;
            }
        }
    }

    private void getThumbInfo(){
        String[] proj = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_ADDED};

        Cursor imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj, null, null, null);

        if (imageCursor != null && imageCursor.moveToFirst()){
            String title;
            String thumbsID;
            String thumbsImageID;
            String thumbsData;
            String date1;
            String imgSize;

            int thumbsIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
            int thumbsDataCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int date = imageCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
            int thumbsImageIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
            int thumbsSizeCol = imageCursor.getColumnIndex(MediaStore.Images.Media.SIZE);
            int num = 0;
            do {
                thumbsID = imageCursor.getString(thumbsIDCol);
                thumbsData = imageCursor.getString(thumbsDataCol);
                thumbsImageID = imageCursor.getString(thumbsImageIDCol);
                imgSize = imageCursor.getString(thumbsSizeCol);
                date1 = imageCursor.getString(date);

                num++;

                Log.d(TAG,"thumbsID : " + thumbsID);
                Log.d(TAG,"thumbsData : " + thumbsData);
                Log.d(TAG,"thumbsImageID : " + thumbsImageID);
                Log.d(TAG,"imgSize : " + imgSize);
                Log.d(TAG,"date1 : " + imageCursor.getInt(thumbsSizeCol));


                Log.d(TAG,"======================");


                if (thumbsImageID != null){
                    GalleryItem test = new GalleryItem();
                    test.setDataTest(thumbsData);
                    test.setIdTest(thumbsID);
                    if (imgSize == null) {
                        test.setSizeTest("");
                    }else{
                        test.setSizeTest(imgSize);
                    }
                    test.setDate(Long.parseLong(date1));
                    Log.i(TAG,"setDate : " + date1 + " Integer : " + Integer.parseInt(date1));
                    itemArrayLists.add(test);
                }
            }while (imageCursor.moveToNext());
        }
        imageCursor.close();
        return;
    }

    Comparator<GalleryItem> compare = new Comparator<GalleryItem>()
    {
        @Override
        public int compare(GalleryItem test, GalleryItem t1) {
            int position = 0;
            try{
                if (test == null){
                    position = 0;
                }else if (t1 == null){
                    position = 0;
                }else if (test.getDate() > t1.getDate()){
                    position = -1;
                }else if (test.getDate() == t1.getDate()){
                    position = 0;
                }else if (test.getDate() < t1.getDate()){
                    position = 1;
                }
            }catch (Exception e){

            }
            return position;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK){
            if (requestCode == 2222){

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), PICK_URI);
                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(FILE_PATH);
                        int exifOrientation = exif.getAttributeInt(
                                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int exifDegree = exifOrientationToDegrees(exifOrientation);
                        bitmap = rotate(bitmap, exifDegree);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap ,"nameofimage" , "description");

                itemArrayLists = new ArrayList<>();

                getThumbInfo();
                Collections.sort(itemArrayLists,compare);

                GalleryItem test = new GalleryItem();
                test.setDataTest("");
                test.setIdTest("");
                test.setSizeTest("");
                test.setDate(0);
                itemArrayLists.add(0,test);

                galleryAdapter = new GalleryAdapter(this,itemArrayLists);
                gridView.setAdapter(galleryAdapter);
                galleryAdapter.notifyDataSetChanged();
            }
        }
    }

    public Bitmap rotate(Bitmap bitmap, int degrees)
    {
        if(degrees != 0 && bitmap != null)
        {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try
            {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if(bitmap != converted)
                {
                    bitmap.recycle();
                    bitmap = converted;
                }
            }
            catch(OutOfMemoryError ex)
            {
                Log.i(TAG,"eg : " + ex);
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap;
    }

    String response = "";

    public class UploadImgNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            MultipartBody.Builder body;

            body = (new MultipartBody.Builder());

            body.setType(MultipartBody.FORM);

            for (int i = 0; i < bitmapArrayList.size(); i++){
                Log.i(TAG,"i : " + i);
                body.addFormDataPart("profileImage"+i, "profileImage"+i, RequestBody.create(MediaType.parse("image/*"), bitmapArrayList.get(i)));
            }

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.imageUpload(), body.build());

                Log.d("Response", response);
//                logLargeString(response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            try{
                JSONObject object = new JSONObject(s);

                if (object.get("result").equals("Y")){
                    Intent intent = new Intent();
                    intent.putExtra("imagePathList",galleryImagePathList);
                    intent.putExtra("realUrl", String.valueOf(object.get("RealURL")));
                    setResult(RESULT_OK,intent);
                    onBackPressed();
                }else{
                    Toast.makeText(GalleryActivity.this, "파일 업로드 실패", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }catch (JSONException e){

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_gallery_back : {
                onBackPressed();
                break;
            }
            case R.id.txt_gallery_done : {

                bitmapArrayList = new ArrayList<>();

                for (int i = 0; i < galleryImagePathList.size(); i++){
                    File imgFile = new  File(galleryImagePathList.get(i));
                    bitmapArrayList.add(imgFile);
                    if (i == galleryImagePathList.size()-1){
                        new UploadImgNetWork().execute();
                    }
                }
                break;
            }
        }
    }
}