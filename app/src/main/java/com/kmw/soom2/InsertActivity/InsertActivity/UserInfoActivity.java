package com.kmw.soom2.InsertActivity.InsertActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.InsertActivity.Item.UserItem;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "UserInfoActivity";
    RadioGroup mGenderRadioGroup;
    RadioButton mMaleRadioButton, mFemaleRadioButton;
    RadioGroup mConfirmRadioGroup;
    RadioButton mConfirmRadioButton, mSuspicionRadioButton;
    EditText edtName,edtBirth;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView mBackTextView;
    TextView mDateInfo, mCalendarTextView;
    String date;
    Button successButton;
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    DatePickerDialogActivity dp ;

    String response;
    int gender = 0; // 1 : 남, 2 : 여
    int diagnosisFlag = 0; // 2 : 의심, 1 : 확진
    String outBreakDt;
    String birth;
    String[] yyyyList = new String[]{};
    String[] mmList = new String[]{};
    String[] ddList = new String[]{};

    int flag = 0;

    Intent beforeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        beforeIntent = getIntent();


       FindViewById();
    }

    void FindViewById(){
        mBackTextView = (TextView) findViewById(R.id.back_text_view_activity_activity_user_info);

        edtName = (EditText) findViewById(R.id.edt_user_info_name);
        edtBirth = (EditText) findViewById(R.id.edt_user_info_birth);

        mGenderRadioGroup = (RadioGroup) findViewById(R.id.gender_toggle_btn_activity_user_info);
        mMaleRadioButton = (RadioButton) findViewById(R.id.male_btn_activity_user_info);
        mFemaleRadioButton = (RadioButton) findViewById(R.id.feamale_btn_activity_user_info);

        mConfirmRadioGroup = (RadioGroup) findViewById(R.id.confirm_toggle_btn_activity_user_info);
        mConfirmRadioButton = (RadioButton) findViewById(R.id.confirm_btn_activity_user_info);
        mSuspicionRadioButton = (RadioButton) findViewById(R.id.suspicion_btn_activity_user_info);

        mDateInfo = (TextView) findViewById(R.id.date_info_activity_user_info);
        mCalendarTextView = (TextView) findViewById(R.id.calendar_text_view_activity_user_info);



        successButton = (Button) findViewById(R.id.success_btn_activity_user_info);
        successButton.setOnClickListener(this);
//        successButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(UserInfoActivity.this, WorkThroughActivity.class);
//                startActivity(intent);
//            }
//        });

        successButton.setBackgroundColor(getResources().getColor(R.color.acacac));

        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && edtBirth.getText().length() > 0 && gender != 0 && diagnosisFlag != 0){
                    successButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }else{
                    successButton.setBackgroundColor(getResources().getColor(R.color.acacac));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        preferences = getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        Calendar newDate = Calendar.getInstance();
        date = format1.format(newDate.getTime());

        final String confirmDate = preferences.getString("confirm_date", null);

        if(preferences.getString("confirm_date",null) != null) {
            mCalendarTextView.setText(confirmDate);
        }else {
            mCalendarTextView.setText(date);
        }

        mBackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mGenderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.male_btn_activity_user_info) {
                    mMaleRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                    mMaleRadioButton.setTextColor(Color.parseColor("#33d16b"));
                    mFemaleRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    mFemaleRadioButton.setTextColor(Color.parseColor("#5c5c5c"));

                    gender = 1;

                    if (edtName.getText().length() > 0 && edtBirth.getText().length() > 0 && gender != 0 && diagnosisFlag != 0){
                        successButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }else{
                        successButton.setBackgroundColor(getResources().getColor(R.color.acacac));
                    }
                } else if (checkedId == R.id.feamale_btn_activity_user_info) {
                    mFemaleRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                    mFemaleRadioButton.setTextColor(Color.parseColor("#33d16b"));
                    mMaleRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    mMaleRadioButton.setTextColor(Color.parseColor("#5c5c5c"));

                    gender = 2;

                    if (edtName.getText().length() > 0 && edtBirth.getText().length() > 0 && gender != 0 && diagnosisFlag != 0){
                        successButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }else{
                        successButton.setBackgroundColor(getResources().getColor(R.color.acacac));
                    }
                }
            }
        });

        mConfirmRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.confirm_btn_activity_user_info) {
                    mConfirmRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                    mConfirmRadioButton.setTextColor(Color.parseColor("#33d16b"));
                    mSuspicionRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    mSuspicionRadioButton.setTextColor(Color.parseColor("#5c5c5c"));
                    mDateInfo.setVisibility(View.VISIBLE);
                    mCalendarTextView.setVisibility(View.VISIBLE);
                    mCalendarTextView.setEnabled(true);
                    diagnosisFlag = 1;

                    if (edtName.getText().length() > 0 && edtBirth.getText().length() > 0 && gender != 0 && diagnosisFlag != 0){
                        successButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }else{
                        successButton.setBackgroundColor(getResources().getColor(R.color.acacac));
                    }
                } else if (checkedId == R.id.suspicion_btn_activity_user_info) {
                    mSuspicionRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                    mSuspicionRadioButton.setTextColor(Color.parseColor("#33d16b"));
                    mConfirmRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    mConfirmRadioButton.setTextColor(Color.parseColor("#5c5c5c"));
                    mDateInfo.setVisibility(View.INVISIBLE);
                    mCalendarTextView.setVisibility(View.INVISIBLE);
                    mCalendarTextView.setEnabled(false);
                    diagnosisFlag = 2;

                    if (edtName.getText().length() > 0 && edtBirth.getText().length() > 0 && gender != 0 && diagnosisFlag != 0){
                        successButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    }else{
                        successButton.setBackgroundColor(getResources().getColor(R.color.acacac));
                    }
                }
            }
        });



        mCalendarTextView.setOnClickListener(this);
        edtBirth.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edt_user_info_birth : {
                flag = 0;
                FragmentManager fragmentManager = getSupportFragmentManager();
                dp = new DatePickerDialogActivity(flag,this, new DatePickerDialogActivity.ConfirmButtonListener() {

                    @Override
                    public void confirmButton(String data) {
                        edtBirth.setText(data);
                    }
                });

                dp.show(fragmentManager, "test");
                break;
            }
            case R.id.calendar_text_view_activity_user_info : {
                flag = 1;

                dp = new DatePickerDialogActivity(flag,this, new DatePickerDialogActivity.ConfirmButtonListener() {

                    @Override
                    public void confirmButton(String data) {
                        mCalendarTextView.setText(data);
                    }
                });
                FragmentManager fragmentManager = getSupportFragmentManager();
                dp.show(fragmentManager, "test");
                break;
            }
            case R.id.success_btn_activity_user_info : {

                if (edtName.getText().length() > 0 && edtBirth.getText().length() > 0 && gender != 0 && diagnosisFlag != 0){
                    successButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    successButton.setClickable(false);
                    new SignupUserNetwork().execute();
                }else{
                    successButton.setBackgroundColor(getResources().getColor(R.color.acacac));
                }

            }
        }
    }
    public class SignupUserNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            String stBirth = "";
            String stOutBreakDt = "";
            for (int i = 0; i < edtBirth.getText().toString().split("-").length; i++) {
                stBirth += edtBirth.getText().toString().split("-")[i];
            }
            for (int i = 0; i < mCalendarTextView.getText().toString().split("-").length; i++) {
                stOutBreakDt += mCalendarTextView.getText().toString().split("-")[i];
            }


            body = (new FormBody.Builder())
                    .add("EMAIL", beforeIntent.getStringExtra("EMAIL"))
                    .add("ID", beforeIntent.getStringExtra("ID"))
                    .add("NICKNAME", beforeIntent.getStringExtra("NICKNAME"))
                    .add("GENDER", "" + gender)
                    .add("LV", beforeIntent.getStringExtra("LV"))
                    .add("OS_TYPE","1")
                    .add("BIRTH", stBirth)
                    .add("DIAGNOSIS_FLAG", "" + diagnosisFlag)
                    .add("LOGIN_TYPE", "" + beforeIntent.getIntExtra("LOGIN_TYPE", 0))
                    .add("DEVICE_CODE", Utils.TOKEN)
                    .add("NAME", edtName.getText().toString());


            if (diagnosisFlag == 1) {
                body.add("OUTBREAK_DT", stOutBreakDt);
            }
            if (beforeIntent.getIntExtra("LOGIN_TYPE", 0) == 1) {
                if (beforeIntent.hasExtra("PASSWORD")) {
                    body.add("PASSWORD", beforeIntent.getStringExtra("PASSWORD"));
                }
            }

            Log.d("TAGTAGTAG", "SignupUserNetwork + " + beforeIntent.getIntExtra("LOGIN_TYPE", 0) + " param : " + body);

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.signupUser(), body.build());

                Log.d("Response Signup Sns : ", response);

                return response;
            } catch (IOException e) {
                successButton.setClickable(true);
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

                }else {
                    new LoginProcessNetWork().execute();
                }
            }catch (JSONException e){
                successButton.setClickable(true);
                Log.i(TAG, e.getLocalizedMessage());
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
            FormBody.Builder body;

            body = (new FormBody.Builder());

            body.add("ID", beforeIntent.getStringExtra("ID"));

            String stEmail = beforeIntent.getStringExtra("ID");
            if (beforeIntent.getIntExtra("LOGIN_TYPE", 0) == 1) {
                if (beforeIntent.hasExtra("PASSWORD")) {
                    body.add("PASSWORD", beforeIntent.getStringExtra("PASSWORD"));
                }
            }
            Log.d("TAGTAGTAG", "LoginProcessNetWork param : " + body);
            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.loginProcess(), body.build());

                Log.d("Response", response);
                return response;
            } catch (IOException e) {
                successButton.setClickable(true);
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

                        preferences = getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);
                        editor = preferences.edit();
                        editor.putString("DEVICE_CODE",userItem.getDeviceCode());
                        editor.putString("USER_ID", userItem.getId());
                        editor.putString("USER_PASSWORD", userItem.getPassword());
                        editor.putInt("LOGIN_TYPE", userItem.getLoginType());
                        editor.putInt("OS_TYPE", userItem.getOsType());

                        editor.putInt("IS_LOGIN_FLAG", 1);  // 1 : 로그인 됨


//                        Intent intent = new Intent(UserInfoActivity.this, WorkThroughActivity.class);

                        new InsertUserAlarmInitNetwork().execute(""+userItem.getUserNo());

//                        if (Utils.userItem != null) {
//                            StartActivity(UserInfoActivity.this, MainActivity.class);
//                        }
                    }else {
                        Log.d(TAG, "aliveFlag != 1");
//                        StartActivity(SplashActivity.this, HomeActivity.class);
                        successButton.setClickable(true);
                    }
                }else {
//                    StartActivity(SplashActivity.this, HomeActivity.class);
                    successButton.setClickable(true);
                }
            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
                successButton.setClickable(true);
//                StartActivity(SplashActivity.this, HomeActivity.class);
            }
        }
    }
    public class InsertUserAlarmInitNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder())
                    .add("USER_NO", strings[0])
                    .add("SYMPTOM_FLAG", "1")
                    .add("DOSING_FLAG", "1")
                    .add("NOTICE_FLAG", "1")
                    .add("COMMUNITY_COMMENT_FLAG", "1")
                    .add("COMMUNITY_COMMENT_REPLY_FLAG", "1")
                    .add("COMMUNITY_LIKE_FLAG", "1");

            Log.d("TAGTAGTAG", "InsertUserAlarmInitNetwork param : " + body.build().toString());
            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.insertAlarmSetting(), body.build());

                Log.d("Response Signup Sns : ", response);

                return response;
            } catch (IOException e) {
                successButton.setClickable(true);
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

                }else {
                    Intent intent = new Intent(UserInfoActivity.this, WorkThroughActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    successButton.setClickable(true);
                    finish();
                }
            }catch (JSONException e){
                successButton.setClickable(true);
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }
}
