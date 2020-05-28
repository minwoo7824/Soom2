package com.kmw.soom2.InsertActivity.InsertActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.Item.ForeignKeys;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.CommunityFragmentFunc.Activitys.CommunityDetailActivity;
import com.kmw.soom2.Home.HomeActivity.MainActivity;
import com.kmw.soom2.Home.HomeActivity.MedicineInsert.Items.MedicineTypeItem;
import com.kmw.soom2.Home.HomeActivity.SymptomActivitys.MedicinRecordActivity;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.InsertActivity.Item.UserItem;
import com.kmw.soom2.MyPage.Activity.NoticeActivity;
import com.kmw.soom2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kakao.util.helper.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kakao.util.helper.Utility.getPackageInfo;
import static com.kmw.soom2.Common.Utils.CAMERA_PERMISSION;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.LOCATION_PERMISSION;
import static com.kmw.soom2.Common.Utils.MEDICINE_LIST;
import static com.kmw.soom2.Common.Utils.MEDICINE_TYPE_LIST;
import static com.kmw.soom2.Common.Utils.OneBtnPopup;
import static com.kmw.soom2.Common.Utils.READ_PERMISSION;
import static com.kmw.soom2.Common.Utils.StartActivity;
import static com.kmw.soom2.Common.Utils.WRITE_PERMISSION;

public class SplashActivity extends AppCompatActivity {

    private static final Logger LOG = LoggerFactory.getLogger(SplashActivity.class);
    private String TAG = "SplashActivity";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String response;
    Intent beforeIntent;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        LOG.debug("onRequestPermissionsResult() requestCode:{}", requestCode);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        beforeIntent = getIntent();

        pref = getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);
        editor = pref.edit();

        Log.d(TAG, "TAGTAGTAG : " + getKeyHash(this));


        if(getNetworkState() != null && getNetworkState().isConnected()) {
            try {
                if (new ServerCheck().execute().get()){

                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        Log.w(TAG, "getInstanceId failed", task.getException());
                                        return;
                                    }
                                    Utils.TOKEN = task.getResult().getToken();

                                    Log.d(TAG, "token : " + Utils.TOKEN);
                                }
                            });

                    FirebaseInstanceId.getInstance().getInstanceId().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SplashActivity.this, "Failed to get token Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    });


                    TedPermission.with(this)
                            .setPermissionListener(permissionListener)
                            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                            .check();

                    new SelectMedicineListNetWork().execute();
                    new SelectMedicineImgTypeNetWork().execute();
                    new ForignLinkNetWork().execute();
                }else{
                    OneBtnPopup(SplashActivity.this,"서버연결 실패","서버 상태가 불안정합니다.\n잠시후에 다시 시도해주세요.","확인");
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            OneBtnPopup(SplashActivity.this,"인터넷 체크","인터넷 상태가 불안정합니다.","확인");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null) { return null; }
        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w(TAG, "디버그 keyHash" + signature, e);
            }
        }
        return null;
    }

    PermissionListener permissionListener = new PermissionListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionGranted() {
            Log.i(TAG, "허가 " + pref.getInt("IS_LOGIN_FLAG", 0));

            WRITE_PERMISSION = true;
            READ_PERMISSION = true;
            CAMERA_PERMISSION = true;
            LOCATION_PERMISSION = true;

            fileMakedirs();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (pref.getInt("IS_LOGIN_FLAG", 0) == 1) {
                        new LoginProcessNetWork().execute();
                    }else {
                        StartActivity(SplashActivity.this, HomeActivity.class);
                        onBackPressed();
                    }
                }
            }, 500);
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Log.i(TAG,"거부 : " + deniedPermissions.toString());
            if (deniedPermissions.contains("android.permission.READ_EXTERNAL_STORAGE") || deniedPermissions.contains("android.permission.WRITE_EXTERNAL_STORAGE")){
                WRITE_PERMISSION = false;
                READ_PERMISSION = false;
            }

            if (deniedPermissions.contains("android.permission.CAMERA") ){
                CAMERA_PERMISSION = false;
            }
            if (deniedPermissions.contains("android.permission.ACCESS_FINE_LOCATION") || deniedPermissions.contains("android.permission.ACCESS_COARSE_LOCATION")){
                LOCATION_PERMISSION = false;
            }

            if (pref.getInt("IS_LOGIN_FLAG", 0) == 1) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new LoginProcessNetWork().execute();
                    }
                }, 2000);
            }else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                    StartActivity(SplashActivity.this, MainActivity.class);
                        StartActivity(SplashActivity.this, HomeActivity.class);
                        onBackPressed();
                    }
                },2000);
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
            Toast.makeText(SplashActivity.this, "SD Card 인식 실패", Toast.LENGTH_SHORT).show();
        }
    }
    public class LoginProcessNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            String stEmail = pref.getString("USER_ID","");
            String stPassword = pref.getString("USER_PASSWORD","");
            String stToken = pref.getString("DEVICE_CODE", "");

            FormBody body;

            if (pref.getInt("LOGIN_TYPE", 0) == 1 || pref.getInt("LOGIN_TYPE", 0) == 0) {
                body = (new FormBody.Builder()).add("ID", stEmail).add("PASSWORD", stPassword).add("DEVICE_CODE", Utils.TOKEN).add("OS_TYPE", "1").build();
            }else {
                body = (new FormBody.Builder()).add("ID", stEmail).add("DEVICE_CODE", Utils.TOKEN).add("OS_TYPE", "1").build();
            }
//            RequestBody body = new FormBody.Builder().add("ID", stEmail).add("PASSWORD", stPassword).add("DEVICE_CODE", Utils.TOKEN).add("OS_TYPE", "1").build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.loginProcess(), body);

                Log.d("Response", response);
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
                        userItem.setPhone(JsonIsNullCheck(object,"PHONE"));

                        Utils.userItem = userItem;

                        pref = getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);
                        editor = pref.edit();
                        editor.putString("DEVICE_CODE",JsonIsNullCheck(object, "DEVICE_CODE"));
                        editor.putString("USER_ID", JsonIsNullCheck(object, "ID"));
                        editor.putString("USER_PASSWORD", JsonIsNullCheck(object, "PASSWORD"));
                        editor.putInt("LOGIN_TYPE", JsonIntIsNullCheck(object, "LOGIN_TYPE"));
                        editor.putInt("OS_TYPE", JsonIntIsNullCheck(object, "OS_TYPE"));

                        editor.putInt("IS_LOGIN_FLAG", 1);  // 1 : 로그인 됨
                        if (Utils.userItem != null) {
                            Intent i = null;

                            if (beforeIntent.hasExtra("medicineAlarm")){
                                i = new Intent(SplashActivity.this, MedicinRecordActivity.class);
                                i.putExtra("medicineAlarm",true);
                            }else if (beforeIntent.hasExtra("android_channel_id")){

                                if (beforeIntent.getStringExtra("android_channel_id").equals("community")){
                                    i = new Intent(SplashActivity.this, CommunityDetailActivity.class);
                                    i.putExtra("communityNo",beforeIntent.getStringExtra("no"));
                                    i.putExtra("push",true);
                                }else if (beforeIntent.getStringExtra("android_channel_id").equals("notice")){
                                    i = new Intent(SplashActivity.this, NoticeActivity.class);
                                    i.putExtra("noticeNo",beforeIntent.getStringExtra("no"));
                                    i.putExtra("body",beforeIntent.getStringExtra("body"));
                                    i.putExtra("push",true);
                                }
                            }else{
                                i = new Intent(SplashActivity.this,MainActivity.class);
                            }
                            startActivity(i);
                            onBackPressed();
                        }
                    }else {
                        Log.d(TAG, "aliveFlag != 1");
                        StartActivity(SplashActivity.this, HomeActivity.class);
                        onBackPressed();
                    }
                }else {
                    StartActivity(SplashActivity.this, HomeActivity.class);
                    onBackPressed();
                }
            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
                StartActivity(SplashActivity.this, HomeActivity.class);
                onBackPressed();
            }
        }
    }
    public class ForignLinkNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody body;
            body = (new FormBody.Builder()).build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectForeignKeyList(), body);

                Log.d("Response", response);
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
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                if (jsonArray.length() > 0) {
                    ArrayList<ForeignKeys> items = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        ForeignKeys item = new ForeignKeys();

                        item.setAliveFlag(JsonIntIsNullCheck(object, "ALIVE_FLAG"));
                        item.setCreateDt(JsonIsNullCheck(object, "CREATE_DT"));
                        item.setLinkNo(JsonIntIsNullCheck(object, "LINK_NO"));
                        item.setLinkUrl(JsonIsNullCheck(object, "LINK_URL"));
                        item.setTitle(JsonIsNullCheck(object, "TITLE"));

                        items.add(item);
                    }
                    Utils.linkKeys = items;
                }else {
                }
            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }

    public class SelectMedicineListNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder().build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectMedicineList(), body);

                Log.i(TAG,"medicine list : " + response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            MEDICINE_LIST = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);

                    Log.i(TAG,"medicine : " + object.toString());

                    //notice_list 테이블의 PUSH_FLAG 1인 경우 push_alarm_list 테이블에 저장 (PUSH_MESSAGE 값을 push_alarm_list의 CONTENTS로 저장)

                    MedicineTakingItem medicineTakingItem = new MedicineTakingItem();

                    medicineTakingItem.setMedicineNo(JsonIntIsNullCheck(object,"MEDICINE_NO"));
                    medicineTakingItem.setMedicineKo(JsonIsNullCheck(object,"KO"));
                    medicineTakingItem.setMedicineEn(JsonIsNullCheck(object,"EN"));
                    medicineTakingItem.setManufacturer(JsonIsNullCheck(object,"MANUFACTURER"));
                    medicineTakingItem.setIngredient(JsonIsNullCheck(object,"INGREDIENT"));
                    medicineTakingItem.setForm(JsonIsNullCheck(object,"FORM"));
                    medicineTakingItem.setMedicineTypeNo(JsonIntIsNullCheck(object,"MEDICINE_TYPE_NO"));
                    medicineTakingItem.setStorageMethod(JsonIsNullCheck(object,"STORAGE_METHOD"));
                    medicineTakingItem.setUnit(JsonIsNullCheck(object,"UNIT"));
                    medicineTakingItem.setEfficacy(JsonIsNullCheck(object,"EFFICACY"));
                    medicineTakingItem.setInformation(JsonIsNullCheck(object,"INFORMATION"));
                    medicineTakingItem.setInstructions(JsonIsNullCheck(object,"INSTRUCTIONS"));
                    medicineTakingItem.setStabiltyRationg(JsonIsNullCheck(object,"STABILITY_RATING"));
                    medicineTakingItem.setPrecaution(JsonIsNullCheck(object,"PRECAUTIONS"));
                    medicineTakingItem.setBookMark(JsonIntIsNullCheck(object,"BOOKMARK"));
                    medicineTakingItem.setMedicineImg(JsonIsNullCheck(object,"MEDICINE_IMG"));
                    medicineTakingItem.setCreateDt(JsonIsNullCheck(object,"CREATE_DT"));
                    medicineTakingItem.setUpdateDt(JsonIsNullCheck(object,"UPDATE_DT"));

                    MEDICINE_LIST.add(medicineTakingItem);
                }
            }catch (JSONException e){

            }
        }
    }

    public class SelectMedicineImgTypeNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder().build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectMedicineType(), body);

                Log.i(TAG,"medicine type : " + response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            MEDICINE_TYPE_LIST = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);

                    MedicineTypeItem medicineTypeItem = new MedicineTypeItem();

                    medicineTypeItem.setMedicineTypeNo(JsonIntIsNullCheck(object,"MEDICINE_TYPE_NO"));
                    medicineTypeItem.setTypeImg(JsonIsNullCheck(object,"TYPE_IMG"));

                    MEDICINE_TYPE_LIST.add(medicineTypeItem);
                }
            }catch (JSONException e){

            }
        }
    }

    public NetworkInfo getNetworkState() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    class ServerCheck extends AsyncTask<Boolean,Boolean,Boolean>{
        @Override
        protected Boolean doInBackground(Boolean... booleans) {
            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress("103.55.190.193",8080);
            try{
                socket.connect(socketAddress,1000);

                if (socket.isConnected()){
                    return true;
                }
            }catch (IOException e){
                return false;
            }
            return false;
        }
    }

}
