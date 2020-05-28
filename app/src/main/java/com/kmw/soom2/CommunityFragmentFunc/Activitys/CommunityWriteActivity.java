package com.kmw.soom2.CommunityFragmentFunc.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.CAMERA_PERMISSION;
import static com.kmw.soom2.Common.Utils.OneBtnPopup;
import static com.kmw.soom2.Common.Utils.READ_PERMISSION;
import static com.kmw.soom2.Common.Utils.WRITE_PERMISSION;

public class CommunityWriteActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "CommunityWriteActivity";
    TextView txtBack;
    LinearLayout linTagParent;
    EditText edtTag;
    EditText edtContents;
    LinearLayout linContents;
    LinearLayout linPicture,linPictureParent;
    Button btnFinish;
    TextView txtContentsLength;
    ScrollView scrollView;

    String[] tagList = new String[]{"#증상","#흡입기","#관리법","#병원","#코로나","#약","#질문","#일상","#N212332"};
    ArrayList<String> tagSelectList = new ArrayList<>();

    ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    public ArrayList<String> imgPathList = new ArrayList<>();

    String realUrl = "";

    String[] realUrlList = new String[]{};
    ArrayList<String> testList = new ArrayList<>();

    Intent beforeIntent;

    String hashTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_write);

        beforeIntent = getIntent();

        FindViewById();
        if (beforeIntent != null){
            if(beforeIntent.hasExtra("hospitalName")){
                edtContents.setText(beforeIntent.getStringExtra("hospitalName") + "\n");
                edtContents.append(beforeIntent.getStringExtra("hospitalAddress") + "\n");
                edtContents.append(beforeIntent.getStringExtra("hospitalDepartment") + "\n");
                edtContents.append(beforeIntent.getStringExtra("hospitalDoctorName"));
            }else {}
            if (beforeIntent.hasExtra("communityNo")){
                edtContents.setText(beforeIntent.getStringExtra("contents"));

                realUrl = beforeIntent.getStringExtra("imgsPath");

                if (realUrl.length() > 0){
                    realUrlList = realUrl.split(",");
                    testList = new ArrayList<>(Arrays.asList(realUrlList));

                    for (int i = 0; i < testList.size(); i++){
                        selectImg(""+i,testList.get(i),null);
                    }
                }

                if (beforeIntent.getStringExtra("hashTag").length() > 0){
                    hashTag = beforeIntent.getStringExtra("hashTag").replace("##","#");

                    String[] hashTagList = hashTag.split("#");

                    for (int i = hashTagList.length-1; i >= 1; i--){
                        Log.i(TAG,"hasg : " + hashTagList[i]);
                        UnSelectTagList("#"+hashTagList[i], 1);
                    }

                    for (int i = 0; i < tagList.length; i++){
                        if (!hashTag.contains(tagList[i])){
                            UnSelectTagList(tagList[i],0);
                        }
                    }
                }
            }else {
                for (int i = 0; i < tagList.length; i++) {
                    UnSelectTagList(tagList[i], 0);
                }
                if (beforeIntent.hasExtra("sharedData")) {
                    edtContents.setText(beforeIntent.getStringExtra("sharedData"));
                    btnFinish.setText("공유하기");
                }
            }
        }else{
            for (int i = 0; i < tagList.length; i++){
                UnSelectTagList(tagList[i],0);
            }
        }
    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_community_write_back);
        linTagParent = (LinearLayout)findViewById(R.id.lin_community_write_tag_parent);
        edtTag = (EditText)findViewById(R.id.edt_community_writing_tag_insert);
        edtContents = (EditText)findViewById(R.id.edt_community_writing_contents);
        linContents = (LinearLayout)findViewById(R.id.lin_community_writing_contents);
        linPicture = (LinearLayout)findViewById(R.id.lin_community_write_picture);
        linPictureParent = (LinearLayout)findViewById(R.id.lin_community_write_picture_parent);
        btnFinish = (Button)findViewById(R.id.btn_community_write_finish);
        txtContentsLength = (TextView)findViewById(R.id.txt_community_write_contents_length);
        scrollView = (ScrollView)findViewById(R.id.src_community_write);

        txtBack.setOnClickListener(this);
        linPicture.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

        edtTag.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                switch (actionId) {

                    case EditorInfo.IME_ACTION_DONE:
                        if (edtTag.getText().length() != 0) {
                            if (edtTag.getText().toString().contains("#")) {
                                UnSelectTagList(edtTag.getText().toString(), 1);
                            } else {
                                UnSelectTagList("#" + edtTag.getText().toString(), 1);
                            }
                            edtTag.setText("");
                        }
                        break;
                    default:
                        return false;
                }
                return true;
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

        edtContents.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtContentsLength.setText("내용("+s.length()+"/5000)");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    void UnSelectTagList(final String name, final int mode){
        View listView = new View(getApplicationContext());
        listView = getLayoutInflater().inflate(R.layout.view_un_select_tag_list_item,null);
        final TextView txtTag = (TextView)listView.findViewById(R.id.txt_un_select_tag_list_item);
        final LinearLayout linearTag = (LinearLayout)listView.findViewById(R.id.linear_un_select_tag_list_item);

        txtTag.setText(name);

        if (mode == 0){
            linTagParent.addView(listView);
        }else if (mode == 1){
            linTagParent.addView(listView,0);
            tagSelectList.add(name);
            txtTag.setTextColor(getResources().getColor(R.color.white));
            linearTag.setBackgroundResource(R.drawable.radius_50dp);
            linearTag.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
        }

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tagSelectList.contains(name)){
                    tagSelectList.remove(name);
                    txtTag.setTextColor(getResources().getColor(R.color.colorPrimary));
                    linearTag.setBackgroundResource(R.drawable.stroke_radius_50dp);
                    linearTag.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
                }else{
                    tagSelectList.add(name);
                    txtTag.setTextColor(getResources().getColor(R.color.white));
                    linearTag.setBackgroundResource(R.drawable.radius_50dp);
                    linearTag.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
                }
            }
        });
    }

    PermissionListener permissionListener = new PermissionListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionGranted() {
            Log.i(TAG, "허가");

            fileMakedirs();

            Intent i = new Intent(CommunityWriteActivity.this,GalleryActivity.class);
            if (imgPathList.size() > 0){
                i.putExtra("imagePathList",imgPathList);
            }
            startActivityForResult(i,1111);
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Log.i(TAG,"거부 : " + deniedPermissions.toString());
            if (deniedPermissions.contains("android.permission.READ_EXTERNAL_STORAGE") || deniedPermissions.contains("android.permission.WRITE_EXTERNAL_STORAGE")){
                WRITE_PERMISSION = false;
                READ_PERMISSION = false;
                Toast.makeText(CommunityWriteActivity.this, "권한을 거부하였습니다.", Toast.LENGTH_SHORT).show();
            }else{
                WRITE_PERMISSION = true;
                READ_PERMISSION = true;
                fileMakedirs();
            }

            if (deniedPermissions.contains("android.permission.CAMERA") ){
                CAMERA_PERMISSION = false;
                Toast.makeText(CommunityWriteActivity.this, "권한을 거부하였습니다.", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(CommunityWriteActivity.this, "SD Card 인식 실패", Toast.LENGTH_SHORT).show();
        }
    }

    void selectImg(final String idx, final String imagePath, final Bitmap bitmap){
        View listView = new View(this);
        listView = getLayoutInflater().inflate(R.layout.view_select_gallery_list_item,null);

        ImageView img = (ImageView)listView.findViewById(R.id.img_select_gallery_list_item);
        ImageView imgRemove = (ImageView)listView.findViewById(R.id.img_select_gallery_list_item_remove);

//        Glide.with(this).load(file).into(img);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
        Glide.with(this)
                .load("http:"+imagePath)
                .apply(requestOptions)
                .into(img);

        final View finalListView = listView;

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgDetail("http:"+imagePath);
            }
        });

        imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linPictureParent.removeView(finalListView);

                testList.remove(imagePath);
                for (int i = 0; i < testList.size(); i++){
                    if (i == 0){
                        realUrl = testList.get(i);
                    }else{
                        realUrl += "," + testList.get(i);
                    }
                }
            }
        });
        linPictureParent.addView(listView);
    }

    void imgDetail(String imagePath){
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        View contentView = getLayoutInflater().inflate(R.layout.view_img_detail,null);

        ImageView img = (ImageView)contentView.findViewById(R.id.img_detail);
        TextView txtClose = (TextView)contentView.findViewById(R.id.txt_img_detail);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int height = dm.heightPixels;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
        dialog.addContentView(contentView,params);

        Glide.with(this).load(imagePath).into(img);

        dialog.show();

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public void OneBtnPopup(Context context, String title, String contents, String btnText){

        final Dialog dateTimeDialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.one_btn_popup,null);

        TextView txtTitle = (TextView)layout.findViewById(R.id.txt_one_btn_popup_title);
        TextView txtContents = (TextView)layout.findViewById(R.id.txt_one_btn_popup_contents);
        Button btnOk = (Button)layout.findViewById(R.id.btn_one_btn_popup_ok);

        txtTitle.setText(title);
        txtContents.setText(contents);
        btnOk.setText(btnText);

        dateTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dateTimeDialog.getWindow();

        dateTimeDialog.setContentView(layout);
        dateTimeDialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialog.dismiss();
                setResult(RESULT_OK);
                onBackPressed();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1111){
            if (resultCode == RESULT_OK){
                linPictureParent.removeAllViews();

                realUrl = data.getStringExtra("realUrl");
                realUrlList = realUrl.split(",");
                testList = new ArrayList<>(Arrays.asList(realUrlList));

                for (int i = 0; i < testList.size(); i++){
                    selectImg(""+i,testList.get(i),null);
                }
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
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap;
    }

    String response = "";

    public class InsertCommunityNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String hasgTag = "";

            for (int i = 0; i < tagSelectList.size(); i++){
                if (i == 0){
                    hasgTag = tagSelectList.get(i);
                }else{
                    hasgTag += "," + tagSelectList.get(i);
                }
            }

            @SuppressLint("WrongThread") RequestBody body = new FormBody.Builder()
                    .add("USER_NO",""+ Utils.userItem.getUserNo())
                    .add("CONTENTS",edtContents.getText().toString())
                    .add("HASHTAG",hasgTag.replace("#",""))
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.insertCommunity(), body);
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
                        if (beforeIntent.hasExtra("sharedData")){
                            OneBtnPopup(CommunityWriteActivity.this,"공유하기","커뮤니티 공유 완료","확인");
                        }else{
                            setResult(RESULT_OK);
                            onBackPressed();
                        }
                    }
                },500);
            }else{
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        new InsertImageNetWork().execute(String.valueOf(object.get("COMMUNITY_NO")));
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
                    .add("CATEGORY","2")
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
            if (beforeIntent.hasExtra("sharedData")) {
                OneBtnPopup(CommunityWriteActivity.this, "공유하기", "커뮤니티 공유 완료", "확인");
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK);
                        onBackPressed();
                    }
                },500);
            }
        }
    }

    public class UpdateCommunityNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String hasgTag = "";

            for (int i = 0; i < tagSelectList.size(); i++){
                if (i == 0){
                    hasgTag = tagSelectList.get(i);
                }else{
                    hasgTag += "," + tagSelectList.get(i);
                }
            }

            @SuppressLint("WrongThread") RequestBody body = new FormBody.Builder()
                    .add("COMMUNITY_NO",beforeIntent.getStringExtra("communityNo"))
                    .add("CONTENTS", edtContents.getText().toString())
                    .add("HASHTAG",hasgTag.replace("#",""))
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateCommunity(), body);
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
                        if (beforeIntent.getStringExtra("imgsPath").length() == 0){
                            new InsertImageNetWork().execute(String.valueOf(object.get("COMMUNITY_NO")));
                        }else{
                            new UpdateImageNetWork().execute(String.valueOf(object.get("COMMUNITY_NO")));
                        }
                    }
                }catch (JSONException e){

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

            @SuppressLint("WrongThread") RequestBody body = new FormBody.Builder()
                    .add("WRITING_NO",strings[0])
                    .add("CATEGORY","2")
                    .add("IMAGE_FILE", realUrl)
                    .add("IMAGE_ORDER_NO","1")
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateImage(), body);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.lin_community_writing_contents : {
//
//                break;
//            }
            case R.id.txt_community_write_back : {
                onBackPressed();
                break;
            }
            case R.id.lin_community_write_picture : {
                TedPermission.with(this)
                        .setPermissionListener(permissionListener)
                        .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                        .check();
                break;
            }
            case R.id.btn_community_write_finish : {
                if (beforeIntent != null) {
                    if (beforeIntent.hasExtra("communityNo")) {
                        new UpdateCommunityNetWork().execute();
                    }else{
                        new InsertCommunityNetWork().execute();
                    }
                }else{
                    new InsertCommunityNetWork().execute();
                }

                break;
            }
        }
    }
}
