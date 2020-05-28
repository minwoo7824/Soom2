package com.kmw.soom2.Home.HomeActivity.SymptomActivitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker;
import com.kmw.soom2.Common.PopupDialog.ShowVideoDialog;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.CommunityFragmentFunc.Activitys.GalleryActivity;
import com.kmw.soom2.CommunityFragmentFunc.Activitys.VideoActivity;
import com.kmw.soom2.Home.HomeItem.VideoMenuItem;
import com.kmw.soom2.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.CAMERA_PERMISSION;
import static com.kmw.soom2.Common.Utils.READ_PERMISSION;
import static com.kmw.soom2.Common.Utils.WRITE_PERMISSION;
import static com.kmw.soom2.Common.Utils.formatHHMM;
import static com.kmw.soom2.Common.Utils.formatHHMMSS;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD2;

public class MemoActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "MemoActivity";
    TextView txtBack;
    ImageView imgRemove;
    TextView txtDate,txtTime;
    EditText edtContents;
    LinearLayout linAttachmentParent,linAttachment;
    Button btnFinish;
    TextView txtContentsLength;

    Intent beforeIntent;

    String realUrl = "";

    String[] realUrlList = new String[]{};
    ArrayList<String> testList = new ArrayList<>();
    ProgressDialog progressDialog;
    int selectFlag;

    Vector<VideoMenuItem> menus = new Vector<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        beforeIntent = getIntent();

        FindViewById();

        imgRemove.setVisibility(View.GONE);

        if (beforeIntent != null){
            if (beforeIntent.hasExtra("registerDt")){
                try {
                    txtDate.setText(formatYYYYMMDD2.format(formatYYYYMMDD.parse(beforeIntent.getStringExtra("registerDt").substring(0,10))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    txtTime.setText(Utils.formatHHMM.format(Utils.formatHHMMSS.parse(beforeIntent.getStringExtra("registerDt").substring(11,18))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                edtContents.setText(beforeIntent.getStringExtra("memo"));

                realUrl = beforeIntent.getStringExtra("imgsPath");
                Log.i(TAG,"realUrl : " + realUrl);

                if (beforeIntent.getStringExtra("imgsPath").length() > 0){
                    if (realUrl.contains("mp4")){
                        Log.i(TAG,"1");

                        progressDialog = new ProgressDialog(this);
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();

                        new VideoSearch().execute();

                    }else{
                        realUrlList = realUrl.split(",");
                        testList = new ArrayList<>(Arrays.asList(realUrlList));

                        for (int i = 0; i < testList.size(); i++){
                            selectImg(""+i,testList.get(i),null);
                        }
                    }
                }

                imgRemove.setVisibility(View.VISIBLE);

            }else if (beforeIntent.hasExtra("date")){
                txtDate.setText(beforeIntent.getStringExtra("date"));
                txtTime.setText(beforeIntent.getStringExtra("time"));
            }else{
                txtDate.setText(Utils.formatYYYYMMDD2.format(new Date(System.currentTimeMillis())));
                txtTime.setText(Utils.formatHHMM.format(new Date(System.currentTimeMillis())));
            }
        }else{
            txtDate.setText(Utils.formatYYYYMMDD2.format(new Date(System.currentTimeMillis())));
            txtTime.setText(Utils.formatHHMM.format(new Date(System.currentTimeMillis())));
        }

    }

    class VideoSearch extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {

            String result = "0";

            int size = getVideo().size();

            for (int i = 0; i < size; i++){
                Log.i(TAG,"2");
                if (menus.get(i).getUri().equals(Uri.parse(realUrl))){
                    Log.i(TAG,"3 : " + i);
                    result = ""+i;

                    break;
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            selectImg(""+s,"",menus.get(Integer.parseInt(s)).getImg());
            progressDialog.dismiss();
        }
    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_memo_record_back);
        imgRemove = (ImageView)findViewById(R.id.img_memo_record_remove);
        txtDate = (TextView)findViewById(R.id.txt_memo_record_date);
        txtTime = (TextView)findViewById(R.id.txt_memo_record_time);
        edtContents = (EditText)findViewById(R.id.edt_memo_record_contents);
        linAttachmentParent = (LinearLayout)findViewById(R.id.lin_memo_record_attachment_parent);
        linAttachment = (LinearLayout)findViewById(R.id.lin_memo_record_attachment);
        btnFinish = (Button)findViewById(R.id.btn_memo_record_finish);
        txtContentsLength = (TextView)findViewById(R.id.txt_memo_record_contents_length);

        txtBack.setOnClickListener(this);
        imgRemove.setOnClickListener(this);
        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);
        linAttachment.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);

        edtContents.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtContentsLength.setText("("+s.length()+"/5000)");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtContents.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (edtContents.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });
    }

    void popup(){
        final Dialog dialog = new BottomSheetDialog(MemoActivity.this, R.style.SheetDialog);
        View contentView = getLayoutInflater().inflate(R.layout.dialog_picture_video_layout,null);

        TextView txtPicture = (TextView)contentView.findViewById(R.id.txt_dialog_picture_video_picture);
        TextView txtVideo = (TextView)contentView.findViewById(R.id.txt_dialog_picture_video_video);
        TextView txtCancel = (TextView)contentView.findViewById(R.id.txt_dialog_picture_video_cancel);

        dialog.setContentView(contentView);

        txtPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                selectFlag = 1;

                TedPermission.with(MemoActivity.this)
                        .setPermissionListener(permissionListener)
                        .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                        .check();
            }
        });

        txtVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                selectFlag = 2;

                TedPermission.with(MemoActivity.this)
                        .setPermissionListener(permissionListener)
                        .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                        .check();
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    PermissionListener permissionListener = new PermissionListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionGranted() {
            Log.i(TAG, "허가");

            fileMakedirs();

            if (selectFlag == 1){
                Intent i = new Intent(MemoActivity.this, GalleryActivity.class);
                startActivityForResult(i,1111);
            }else if (selectFlag == 2){
                Intent i = new Intent(MemoActivity.this, VideoActivity.class);
                startActivityForResult(i,2222);
            }
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Log.i(TAG,"거부 : " + deniedPermissions.toString());
            if (deniedPermissions.contains("android.permission.READ_EXTERNAL_STORAGE") || deniedPermissions.contains("android.permission.WRITE_EXTERNAL_STORAGE")){
                WRITE_PERMISSION = false;
                READ_PERMISSION = false;
                Toast.makeText(MemoActivity.this, "권한을 거부하였습니다.", Toast.LENGTH_SHORT).show();
            }else{
                WRITE_PERMISSION = true;
                READ_PERMISSION = true;
                fileMakedirs();
            }

            if (deniedPermissions.contains("android.permission.CAMERA") ){
                CAMERA_PERMISSION = false;
                Toast.makeText(MemoActivity.this, "권한을 거부하였습니다.", Toast.LENGTH_SHORT).show();
            }else{
                CAMERA_PERMISSION = true;
            }
        }
    };

    void fileMakedirs(){
        String str = Environment.getExternalStorageState();
        if (str.equals(Environment.MEDIA_MOUNTED)) {

            String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(dirPath);
            if (!file.exists()) { // 원하는 경로에 폴더가 있는지 확인
                file.mkdirs();
            }
        } else {
            Toast.makeText(MemoActivity.this, "SD Card 인식 실패", Toast.LENGTH_SHORT).show();
        }
    }

    void selectImg(final String idx, final String imagePath, final Bitmap bitmap){
        View listView = new View(this);
        listView = getLayoutInflater().inflate(R.layout.view_select_gallery_list_item,null);

        ImageView img = (ImageView)listView.findViewById(R.id.img_select_gallery_list_item);
        ImageView imgRemove = (ImageView)listView.findViewById(R.id.img_select_gallery_list_item_remove);

//        Glide.with(this).load(file).into(img);

        if (imagePath.length() != 0){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
            Glide.with(this)
                    .load("http:"+imagePath)
                    .apply(requestOptions)
                    .into(img);
        }else if (bitmap != null){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
            Glide.with(this)
                    .load(bitmap)
                    .apply(requestOptions)
                    .into(img);
        }

        final View finalListView = listView;

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagePath.length() != 0){
                    imgDetail(imagePath);
                }else if (bitmap != null){
                    ShowVideoDialog dialog = new ShowVideoDialog(MemoActivity.this, Uri.parse(realUrl), MemoActivity.this);
                    dialog.show();
                }
            }
        });

        imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linAttachmentParent.removeView(finalListView);

                if (imagePath.length() != 0){
                    testList.remove(imagePath);
                    for (int i = 0; i < testList.size(); i++){
                        if (i == 0){
                            realUrl = testList.get(i);
                        }else{
                            realUrl += "," + testList.get(i);
                        }
                    }
                }else if (bitmap != null){
                    realUrl = "";
                }
            }
        });
        linAttachmentParent.addView(listView);
    }

    void imgDetail(String imgPath){
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        View contentView = getLayoutInflater().inflate(R.layout.view_img_detail,null);

        ImageView img = (ImageView)contentView.findViewById(R.id.img_detail);
        TextView txtClose = (TextView)contentView.findViewById(R.id.txt_img_detail);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int height = dm.heightPixels;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
        dialog.addContentView(contentView,params);

        Log.i(TAG,"path : " + imgPath);

        Glide.with(this).load("http:"+imgPath).into(img);

        dialog.show();

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

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
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap;
    }

    private Vector<VideoMenuItem> getVideo() {
        String[] proj = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATA
        };
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        Cursor cursor = getContentResolver().query(uri, proj, null, null, orderBy + " DESC");

        menus = new Vector<>();

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

    String response = "";

    public class InsertHomeFeedHistoryNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            FormBody.Builder body;
            body = (new FormBody.Builder());

//            @SuppressLint("WrongThread") RequestBody body = new FormBody.Builder()
//                    .add("USER_NO",""+ Utils.userItem.getUserNo())
//                    .add("CATEGORY","30")
//                    .add("REGISTER_DT", Utils.formatYYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis())))
//                    .add("MEMO",edtContents.getText().toString())
//                    .add("NICKNAME", Utils.userItem.getNickname())
//                    .add("GENDER", String.valueOf(Utils.userItem.getGender()))
//                    .add("BIRTH", String.valueOf(Utils.userItem.getBirth()))
//                    .build();

            body.add("USER_NO",""+ Utils.userItem.getUserNo());
            body.add("CATEGORY","30");
            body.add("MEMO",edtContents.getText().toString());
            body.add("NICKNAME", Utils.userItem.getNickname());
            body.add("GENDER", String.valueOf(Utils.userItem.getGender()));
            body.add("BIRTH", String.valueOf(Utils.userItem.getBirth()));

            try {
                body.add("REGISTER_DT", formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString()))
                        + " " + formatHHMMSS.format(formatHHMM.parse(txtTime.getText().toString())));
            } catch (ParseException e) {
                e.printStackTrace();
                body.add("REGISTER_DT",Utils.formatYYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis())));
            }


            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.insertMemoHomeFeedHistoryList(), body.build());
                Log.d("Response", response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (realUrl.length() == 0){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK);
                        onBackPressed();
                    }
                },500);
            }else{
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        new InsertImageNetWork().execute(String.valueOf(object.get("USER_HISTORY_NO")));
                    }
                }catch (JSONException e){

                }
            }
        }
    }

    public class InsertImageNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            @SuppressLint("WrongThread") RequestBody body = new FormBody.Builder()
                    .add("WRITING_NO",strings[0])
                    .add("CATEGORY","30")
                    .add("IMAGE_FILE", realUrl)
                    .add("IMAGE_ORDER_NO","1")
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.insertImage(), body);
                Log.d("Response", response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setResult(RESULT_OK);
                    onBackPressed();
                }
            },500);
        }
    }

    public class UpdateHomeFeedHistoryNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

//            @SuppressLint("WrongThread") RequestBody body = new FormBody.Builder()
//                    .add("USER_HISTORY_NO",beforeIntent.getStringExtra("medicineHistoryNo"))
//                    .add("CATEGORY","30")
//                    .add("REGISTER_DT",beforeIntent.getStringExtra("registerDt"))
//                    .add("MEMO",edtContents.getText().toString())
//                    .build();
            FormBody.Builder body;
            body = (new FormBody.Builder());

            body.add("USER_HISTORY_NO",beforeIntent.getStringExtra("medicineHistoryNo"));
            body.add("CATEGORY","30");
            body.add("MEMO",edtContents.getText().toString());
            try {
                body.add("REGISTER_DT", formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString()))
                        + " " + formatHHMMSS.format(formatHHMM.parse(txtTime.getText().toString())));
            } catch (ParseException e) {
                e.printStackTrace();
                body.add("REGISTER_DT",beforeIntent.getStringExtra("registerDt"));
            }

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateMemoHomeFeedHistoryList(), body.build());
                Log.d("Response", response);
//                logLargeString(response);
                return response;
            } catch (IOException e) {
                btnFinish.setClickable(true);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (realUrl.length() == 0){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK);
                        btnFinish.setClickable(true);
                        onBackPressed();
                    }
                },500);
            }else{
                if (beforeIntent.getStringExtra("imgsPath").length() == 0){
                    new InsertImageNetWork().execute(beforeIntent.getStringExtra("medicineHistoryNo"));
                }else{
                    new UpdateImageNetWork().execute();
                }
            }
        }
    }

    public class UpdateImageNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            Log.i(TAG,"update : " + realUrl);

            @SuppressLint("WrongThread") RequestBody body = new FormBody.Builder()
                    .add("WRITING_NO",beforeIntent.getStringExtra("medicineHistoryNo"))
                    .add("CATEGORY","30")
                    .add("IMAGE_FILE", realUrl)
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateImage(), body);
                Log.d("Response", response);
//                logLargeString(response);
                return response;
            } catch (IOException e) {
                btnFinish.setClickable(true);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setResult(RESULT_OK);
                    btnFinish.setClickable(true);
                    onBackPressed();
                }
            },500);
        }
    }

    public class DeleteHomeFeedHistoryNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            @SuppressLint("WrongThread") RequestBody body = new FormBody.Builder()
                    .add("USER_HISTORY_NO",beforeIntent.getStringExtra("medicineHistoryNo"))
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.deleteHomeFeedHistoryList(), body);
                Log.d("Response", response);
                return response;
            } catch (IOException e) {
                imgRemove.setClickable(true);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setResult(RESULT_OK);
                    imgRemove.setClickable(true);
                    onBackPressed();
                }
            },500);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1111){
            if (resultCode == RESULT_OK){

                linAttachmentParent.removeAllViews();

                realUrl = data.getStringExtra("realUrl");
                realUrlList = realUrl.split(",");
                testList = new ArrayList<>(Arrays.asList(realUrlList));

                for (int i = 0; i < testList.size(); i++){
                    selectImg(""+i,testList.get(i),null);
                }
            }
        }else if (requestCode == 2222){
            if (resultCode == RESULT_OK){
                linAttachmentParent.removeAllViews();
                realUrl = data.getStringExtra("videoPath");
                selectImg("","",(Bitmap)data.getParcelableExtra("videoThumbnail"));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_memo_record_back : {
                onBackPressed();
                break;
            }
            case R.id.img_memo_record_remove : {
                TwoBtnPopup(this,"메모","메모를 삭제하시겠습니까?","취소","확인");

                break;
            }
            case R.id.txt_memo_record_date : {
                DateTimePicker(1, "날짜를 선택하세요.");
                break;
            }
            case R.id.txt_memo_record_time : {
                DateTimePicker("시간을 선택하세요.");
                break;
            }
            case R.id.lin_memo_record_attachment : {
                popup();
                break;
            }
            case R.id.btn_memo_record_finish : {
                btnFinish.setClickable(false);
                if (beforeIntent != null){
                    if (beforeIntent.hasExtra("registerDt")){
                        new UpdateHomeFeedHistoryNetWork().execute();
                    }else{
                        new InsertHomeFeedHistoryNetWork().execute();
                    }
                }else{
                    new InsertHomeFeedHistoryNetWork().execute();
                }
                break;
            }
        }
    }
    void DateTimePicker(final int flag, String title){
        View dateTimeView = getLayoutInflater().inflate(R.layout.pop_up_date_time_picker,null);
        final Dialog dateDialog = new Dialog(MemoActivity.this);
        TextView txtTitle = (TextView)dateTimeView.findViewById(R.id.txt_calendar_picker_title);

        txtTitle.setText(title);

        dateDialog.setContentView(dateTimeView);
        dateDialog.show();

        final SingleDateAndTimePicker singleDateAndTimePicker = dateTimeView.findViewById(R.id.single_day_picker);

        singleDateAndTimePicker.setDisplayYears(true);
        singleDateAndTimePicker.setDisplayMonths(true);
        singleDateAndTimePicker.setDisplayDaysOfMonth(true);
        singleDateAndTimePicker.setDisplayHours(false);
        singleDateAndTimePicker.setDisplayMinutes(false);

        Date date = null;

        Log.d(TAG,"txtDate : " + txtDate.getText().toString());

        try {
            date = formatYYYYMMDD2.parse(txtDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        singleDateAndTimePicker.selectDate(calendar);

        singleDateAndTimePicker.clickDateChange(new SingleDateAndTimePicker.OnCustomClick() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                Log.d(TAG, "onDateChanged " + displayed + ", date : " + date);
                txtDate.setText(formatYYYYMMDD2.format(date));
                dateDialog.dismiss();
            }
        });

        singleDateAndTimePicker.clickCancelDialog(new SingleDateAndTimePicker.OnCancelClick() {
            @Override
            public void onDialogCancel() {
                dateDialog.dismiss();
            }
        });
    }
    public void TwoBtnPopup(Context context, String title, String contents, String btnLeftText, String btnRightText){

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
                new DeleteHomeFeedHistoryNetWork().execute();
                dateTimeDialog.dismiss();
            }
        });
    }

    void DateTimePicker(String title) {
        View dateTimeView = getLayoutInflater().inflate(R.layout.pop_up_date_time_picker,null);
        final Dialog dateTimeDialog = new Dialog(MemoActivity.this);
        TextView txtTitle = (TextView)dateTimeView.findViewById(R.id.txt_calendar_picker_title);

        txtTitle.setText(title);

        dateTimeDialog.setContentView(dateTimeView);
        dateTimeDialog.show();

        final SingleDateAndTimePicker singleDateAndTimePicker = (SingleDateAndTimePicker)dateTimeView.findViewById(R.id.single_day_picker);

        singleDateAndTimePicker.setDisplayYears(false);
        singleDateAndTimePicker.setDisplayMonths(false);
        singleDateAndTimePicker.setDisplayDaysOfMonth(false);
        singleDateAndTimePicker.setDisplayHours(true);
        singleDateAndTimePicker.setDisplayMinutes(true);

        Date date = null;

        try {
            date = Utils.formatHHMM.parse(txtTime.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        singleDateAndTimePicker.selectDate(calendar);

        singleDateAndTimePicker.clickDateChange(new SingleDateAndTimePicker.OnCustomClick() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                Log.d(TAG, "onDateChanged " + displayed + ", date : " + date);
                txtTime.setText(Utils.formatHHMM.format(date));
                dateTimeDialog.dismiss();
            }
        });

        singleDateAndTimePicker.clickCancelDialog(new SingleDateAndTimePicker.OnCancelClick() {
            @Override
            public void onDialogCancel() {
                dateTimeDialog.dismiss();
            }
        });
    }
}
