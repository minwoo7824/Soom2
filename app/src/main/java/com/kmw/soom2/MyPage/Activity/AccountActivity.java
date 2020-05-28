package com.kmw.soom2.MyPage.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kmw.soom2.Common.Crop;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.InsertActivity.InsertActivity.HomeActivity;
import com.kmw.soom2.InsertActivity.Item.UserItem;
import com.kmw.soom2.MyPage.Dialog.LogoutDialog;
import com.kmw.soom2.MyPage.Dialog.SecessionDialog;
import com.kmw.soom2.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.CAMERA_PERMISSION;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.READ_PERMISSION;
import static com.kmw.soom2.Common.Utils.WRITE_PERMISSION;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    final String TAG = "AccountActivity";
    EditText passEditText,nickNameEditText,phoneNumberEditText;
    TextView nickNameCountTextView,nameTextView,accountTextView,secessionTextView, txtSave;
    Button logoutButton;
    LogoutDialog logoutDialog;
    SecessionDialog secessionDialog;

    ImageView profileImageView, cameraImageView;
    TextView btnBack;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    LinearLayout passcontainer;

    String realImageUrl;

    UserItem userItem;
    String response;

    Bitmap bitmap = null;
    Uri selectedImageUri;

    String realUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        preferences = getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);

        txtSave = findViewById(R.id.save_text_view_activity_account);
        btnBack = findViewById(R.id.back_text_view_activity_account);

        profileImageView = findViewById(R.id.account_imageview_profile);
        cameraImageView = findViewById(R.id.account_imageview_profile_camera);

        phoneNumberEditText = (EditText) findViewById(R.id.phone_num_edit_text_activity_account);
        nickNameEditText = (EditText) findViewById(R.id.nickname_edit_activity_account);
        passEditText = (EditText) findViewById(R.id.pass_edit_text_activity_account);
        passEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        nickNameCountTextView = (TextView) findViewById(R.id.edit_text_count_text_view_activity_account);
        secessionTextView = (TextView) findViewById(R.id.here_text_view_activity_account);
        nameTextView = (TextView) findViewById(R.id.name_text_view_activity_account);
        accountTextView = (TextView) findViewById(R.id.account_info_text_view_activity_account);
        logoutButton = (Button) findViewById(R.id.log_out_button_activity_account);
        passcontainer = (LinearLayout) findViewById(R.id.pass_container_activity_account);
        logoutButton.setOnClickListener(this);
        secessionTextView.setOnClickListener(this);
        txtSave.setOnClickListener(this);
        profileImageView.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        userItem = Utils.userItem;
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(400));

        Log.i(TAG,"imgPath : " + userItem.getProfileImg());

        if (userItem.getProfileImg().length() != 0) {
            if (userItem.getProfileImg().contains("http:")) {
                Glide.with(this).asBitmap().load(userItem.getProfileImg()).apply(requestOptions).into(profileImageView);
            }else {
                Glide.with(this).asBitmap().load("http:"+ userItem.getProfileImg()).apply(requestOptions).into(profileImageView);
            }
            cameraImageView.setVisibility(View.INVISIBLE);
        }

        if (userItem.getLoginType() != 1) {
            passcontainer.setVisibility(View.GONE);
            if (userItem.getLoginType() == 2) {
                accountTextView.setText("네이버");
            }else {
                accountTextView.setText("카카오톡");
            }
        } else {
            passEditText.setText(userItem.getPassword());
            accountTextView.setText("일반 이메일");
        }

//        String email = preferences.getString("email", "");
//        String nickName = preferences.getString("nickname","");

        nameTextView.setText(userItem.getEmail());
        nickNameEditText.setText(userItem.getNickname());
        nickNameCountTextView.setText("" + nickNameEditText.length());

        phoneNumberEditText.setText((userItem.getPhone() != null) ? userItem.getPhone() : "");

        logoutDialog = new LogoutDialog(this, new LogoutDialog.LogoutButtonListener() {
            @Override
            public void logoutButton(boolean data) {
                new LogoutProcessNetwork().execute();
            }
        });

        secessionDialog = new SecessionDialog(this, new SecessionDialog.SecessionButtonListener() {
            @Override
            public void secessionButton(boolean data) {
                new DeleteUserInfoNetwork().execute();
            }
        });




        nickNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                nickNameCountTextView.setText("" + nickNameEditText.length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        phoneNumberEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    @Override
    public void onClick(View v) {
        SharedPreferences preferences = getSharedPreferences("preferences",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        switch (v.getId()){
            case R.id.log_out_button_activity_account:
//                logoutDialog.show();
                logoutButton.setClickable(false);
                editor.clear();
                editor.apply();

//                logoutDialog.show();
                    new LogoutProcessNetwork().execute();


                break;
            case R.id.here_text_view_activity_account:
                secessionTextView.setClickable(false);
                editor.clear();
                editor.apply();
                TwoBtnPopup(AccountActivity.this,"회원탈퇴", "탈퇴하시겠습니까?","취소", "확인",1);

//                secessionDialog.show();

                break;
            case R.id.save_text_view_activity_account:
                txtSave.setClickable(false);
                new UpdateUserInfoNetwork().execute();
                break;
            case R.id.account_imageview_profile:
//                goToAlbum();
                TedPermission.with(AccountActivity.this)
                        .setPermissionListener(permissionListener)
                        .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                        .check();
                break;
            case R.id.back_text_view_activity_account:
                onBackPressed();
                break;
        }
    }
    PermissionListener permissionListener = new PermissionListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionGranted() {
            Log.i(TAG, "허가");
            goToAlbum();

        }

        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Log.i(TAG,"거부 : " + deniedPermissions.toString());
            if (deniedPermissions.contains("android.permission.READ_EXTERNAL_STORAGE") || deniedPermissions.contains("android.permission.WRITE_EXTERNAL_STORAGE")){
                WRITE_PERMISSION = false;
                READ_PERMISSION = false;
                Toast.makeText(AccountActivity.this, "권한을 거부하였습니다.", Toast.LENGTH_SHORT).show();
            }else{
                WRITE_PERMISSION = true;
                READ_PERMISSION = true;
            }

            if (deniedPermissions.contains("android.permission.CAMERA") ){
                CAMERA_PERMISSION = false;
                Toast.makeText(AccountActivity.this, "권한을 거부하였습니다.", Toast.LENGTH_SHORT).show();
            }else{
                CAMERA_PERMISSION = true;
            }
        }
    };
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
                txtSave.setClickable(true);
                onBackPressed();
            }
        });

    }
    public class UpdateUserInfoNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder());

            if (realUrl != null){
                body.add("PROFILE_IMG", realUrl);
            }
            if (Utils.userItem.getLoginType() == 1) {
                body.add("PASSWORD",passEditText.getText().toString());
            }
            if (phoneNumberEditText.getText().toString() != "") {
                body.add("PHONE", phoneNumberEditText.getText().toString());
            }
            body.add("NICKNAME", nickNameEditText.getText().toString());
            body.add("USER_NO", String.valueOf(userItem.getUserNo()));

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateUserInfo(), body.build());

                Log.d("Response Signup Sns : ", response);

                return response;
            } catch (IOException e) {
                txtSave.setClickable(true);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (JsonIsNullCheck(jsonObject, "result").equals("N")) {
                    // 가입 실패
                    Log.d(TAG, "가입 에러");
                    txtSave.setClickable(true);
                }else {
                    new GetReloadUserDataNetwork().execute();
                }
            }catch (JSONException e){
                txtSave.setClickable(true);
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }
    public class GetReloadUserDataNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder())
                    .add("ID", userItem.getId());

            if (userItem.getLoginType() == 1) {
                body.add("PASSWORD", passEditText.getText().toString());
            }
            body.add("DEVICE_CODE", Utils.TOKEN);
            body.add("OS_TYPE", "1");

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.loginProcess(), body.build());

                Log.d("Response Signup Sns : ", response);

                return response;
            } catch (IOException e) {
                txtSave.setClickable(true);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                if (jsonArray.length() > 0) {

                    JSONObject object = jsonArray.getJSONObject(0);
                    if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {
                        UserItem userItem = new UserItem();

                        userItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                        userItem.setLv(JsonIntIsNullCheck(object, "LV"));
                        userItem.setId(JsonIsNullCheck(object, "ID"));
                        userItem.setEmail(JsonIsNullCheck(object, "EMAIL"));
                        userItem.setPassword(JsonIsNullCheck(object, "PASSWORD"));
                        userItem.setNickname(JsonIsNullCheck(object, "NICKNAME"));
                        userItem.setName(JsonIsNullCheck(object, "NAME"));
                        userItem.setBirth(JsonIntIsNullCheck(object, "BIRTH"));
                        userItem.setGender(JsonIntIsNullCheck(object, "GENDER"));
                        userItem.setDiagnosisFlag(JsonIntIsNullCheck(object, "DIAGNOSIS_FLAG"));  // 확진 여부
                        userItem.setOutbreakDt(JsonIsNullCheck(object, "OUTBREAK_DT")); // 발병일
                        userItem.setProfileImg(JsonIsNullCheck(object, "PROFILE_IMG"));  // 프로필 사진
                        userItem.setDeviceCode(JsonIsNullCheck(object, "DEVICE_CODE"));  // fcm토큰
                        userItem.setLoginType(JsonIntIsNullCheck(object, "LOGIN_TYPE"));  //  1: 일반 이메일, 2 : 네이버, 3 : 카카오, 4 : 애플
                        userItem.setOsType(JsonIntIsNullCheck(object, "OS_TYPE")); // 1 : android, 2 : ios
                        userItem.setCreateDt(JsonIsNullCheck(object, "CREATE_DT"));
                        userItem.setUpdateDt(JsonIsNullCheck(object, "UPDATE_DT"));
                        userItem.setPhone(JsonIsNullCheck(object, "PHONE"));

                        Utils.userItem = userItem;

                        editor = preferences.edit();
                        editor.putString("DEVICE_CODE",userItem.getDeviceCode());
                        editor.putString("USER_ID", userItem.getId());
                        editor.putString("USER_PASSWORD", userItem.getPassword());
                        editor.putInt("LOGIN_TYPE", userItem.getLoginType());
                        editor.putInt("OS_TYPE", userItem.getOsType());

                        editor.apply();
                        OneBtnPopup(AccountActivity.this,"회원정보 수정","저장되었습니다.","확인");

                    }else {
                        Log.d(TAG, "aliveFlag != 1");
                    }
                }else {

                    Log.d(TAG, "JSON array null");
                }
                txtSave.setClickable(true);
            }catch (JSONException e){
                txtSave.setClickable(true);
                Log.i(TAG, e.getLocalizedMessage());
    //                StartActivity(SplashActivity.this, HomeActivity.class);
            }
        }
    }
    public class imageUploadNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
//            FormBody.Builder body;
//            body = (new FormBody.Builder())
//                    .add("USER_NO", String.valueOf(Utils.userItem.getUserNo()));

            File file = new File(strings[0]);
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("profileImage", "profileImage", RequestBody.create(MediaType.parse("image/*"), file))
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.imageUpload(), body);

                Log.d("Response Signup Sns : ", response);

                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                realUrl = JsonIsNullCheck(jsonObject, "RealURL");
                Log.d(TAG, realUrl);
            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }

    public class LogoutProcessNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder())
                    .add("USER_NO", String.valueOf(Utils.userItem.getUserNo()));

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.logoutProcess(), body.build());

                Log.d("Response Signup Sns : ", response);

                return response;
            } catch (IOException e) {
                e.printStackTrace();
                logoutButton.setClickable(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,s);
            try {
                JSONObject jsonObject = new JSONObject(s);


                editor = preferences.edit();

                editor.putInt("IS_LOGIN_FLAG",0);
                editor.apply();

                TwoBtnPopup(AccountActivity.this,"로그아웃", "로그아웃 하시겠습니까?","취소", "확인",0);
//                Intent i = new Intent(AccountActivity.this, HomeActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);
//                onBackPressed();
            }catch (JSONException e){
                logoutButton.setClickable(true);
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }
    public class DeleteUserInfoNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder())
                    .add("USER_NO", String.valueOf(Utils.userItem.getUserNo()));

            Log.d("TAGTAGTAG", "InsertUserAlarmInitNetwork param : " + body.build().toString());
            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.deleteUserInfo(), body.build());

                Log.d("Response Signup Sns : ", response);

                return response;
            } catch (IOException e) {
                secessionTextView.setClickable(false);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                editor = preferences.edit();

                editor.putInt("IS_LOGIN_FLAG",0);
                editor.commit();



            }catch (JSONException e){
                secessionTextView.setClickable(true);
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }
    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, 9162);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }else if (requestCode == 1111 && resultCode == RESULT_OK){
            /// 기존 데이터???
        }
    }
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asPng(true).start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(result));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            new imageUploadNetwork().execute(Crop.getOutput(result).getPath());

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(400));
//            profileImageView.setImageURI(Crop.getOutput(result));
            Glide.with(this).asBitmap().load(Crop.getOutput(result)).apply(requestOptions).into(profileImageView);

            cameraImageView.setVisibility(View.VISIBLE);

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public Button TwoBtnPopup(Context context, String title, String contents, String btnLeftText, String btnRightText, int flag){

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
                logoutButton.setClickable(true);
                secessionTextView.setClickable(true);
                dateTimeDialog.dismiss();
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialog.dismiss();
                if(flag == 1) {
                    new DeleteUserInfoNetwork().execute();
                }else {
                    if(accountTextView.getText().toString().equals("카카오톡")) {
                       new HomeActivity().onClickLogout();
                    }else {

                    }
                }
                Intent i = new Intent(AccountActivity.this, HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                logoutButton.setClickable(true);
                secessionTextView.setClickable(true);
                onBackPressed();
            }
        });

        return btnRight;
    }


}
