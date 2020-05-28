package com.kmw.soom2.InsertActivity.InsertActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.HomeActivity.MainActivity;
import com.kmw.soom2.InsertActivity.Item.UserItem;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.OneBtnPopup;
import static com.kmw.soom2.Common.Utils.StartActivity;

public class EmailLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "EmailLoginActivity";
    TextView txtBack;
    EditText edtId,edtPw;
    TextView txtFindId,txtFindPw;
    Button btnFinish;
    LinearLayout linParent;

    String response;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        FindViewById();

    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_email_login_back);
        edtId = (EditText)findViewById(R.id.edt_email_login_id);
        edtPw = (EditText)findViewById(R.id.edt_email_login_pw);
        txtFindId = (TextView)findViewById(R.id.txt_email_login_id_search);
        txtFindPw = (TextView)findViewById(R.id.txt_email_login_pw_search);
        btnFinish = (Button)findViewById(R.id.btn_email_login_finish);
        linParent = (LinearLayout)findViewById(R.id.lin_email_login_parent);

        txtBack.setOnClickListener(this);
        txtFindId.setOnClickListener(this);
        txtFindPw.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        linParent.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_email_login_back : {
                onBackPressed();
                break;
            }
            case R.id.txt_email_login_id_search : {
                StartActivity(this, SearchIdActivity.class);
                break;
            }
            case R.id.txt_email_login_pw_search : {
                StartActivity(this, SearchPwActivity.class);
                break;
            }
            case R.id.btn_email_login_finish : {
                if (edtId.getText().toString().length() > 0 && edtPw.getText().toString().length() > 0) {
                    new LoginProcessNetWork().execute();
                }else {
                    OneBtnPopup(this,"로그인 오류","아이디와 비밀번호를 확인해주세요.","확인");
                }
                break;
            }
            case R.id.lin_email_login_parent : {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(edtPw.getWindowToken(),0);
                break;
            }
        }
    }
    public class LoginProcessNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            String stEmail = edtId.getText().toString();
            String stPassword = edtPw.getText().toString();


            RequestBody body = new FormBody.Builder().add("ID", stEmail).add("PASSWORD", stPassword).add("DEVICE_CODE", Utils.TOKEN).add("OS_TYPE", "1").build();

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

                if (jsonObject.has("list")){
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

                            Log.i(TAG,"type : " + pref.getInt("LOGIN_TYPE",0));
                            Log.i(TAG,"password : " + pref.getString("USER_PASSWORD",""));

                            editor.apply();

                            if (Utils.userItem != null) {
                                Intent i = new Intent(EmailLoginActivity.this,MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                onBackPressed();
                            }
                        }else {
                            Log.d(TAG, "aliveFlag != 1");
                        }
                    }
                }else{
                    OneBtnPopup(EmailLoginActivity.this,"로그인 오류","아이디와 비밀번호를 확인해주세요.","확인");
                }

            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }
}
