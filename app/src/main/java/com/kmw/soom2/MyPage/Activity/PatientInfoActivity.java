package com.kmw.soom2.MyPage.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.InsertActivity.InsertActivity.DatePickerDialogActivity;
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

public class PatientInfoActivity extends AppCompatActivity implements View.OnClickListener {
    final String TAG = "PatientInfo";
    RadioGroup genderRadioGroup;
    RadioButton maleRadioButton, femaleRadioButton;
    RadioGroup confirmRadioGroup;
    RadioButton confirmRadioButton, suspicionRadioButton;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView backTextView, btnSave;
    TextView dateInfo, calendarTextView;
    String date;
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    DatePickerDialogActivity dp;

    LinearLayout linCalendar;

    EditText editName, editBirth;

    String response;
    UserItem userItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);

        btnSave = findViewById(R.id.txt_patient_info_save);
        userItem = Utils.userItem;
        editName = findViewById(R.id.name_edit_text_activity_patient_info);
        editBirth = findViewById(R.id.birth_edit_text_activity_patient_info);
        linCalendar = findViewById(R.id.lin_patient_info_calendar);

        backTextView = (TextView) findViewById(R.id.back_text_view_activity_patient_info);

        genderRadioGroup = (RadioGroup) findViewById(R.id.gender_toggle_btn_activity_patient_info);
        maleRadioButton = (RadioButton) findViewById(R.id.male_btn_activity_patient_info);
        femaleRadioButton = (RadioButton) findViewById(R.id.feamale_btn_activity_patient_info);

        confirmRadioGroup = (RadioGroup) findViewById(R.id.confirm_toggle_btn_activity_patient_info);
        confirmRadioButton = (RadioButton) findViewById(R.id.confirm_btn_activity_patient_info);
        suspicionRadioButton = (RadioButton) findViewById(R.id.suspicion_btn_activity_patient_info);

        dateInfo = (TextView) findViewById(R.id.date_info_activity_patient_info);
        calendarTextView = (TextView) findViewById(R.id.calendar_text_view_activity_patient_info);

        preferences = getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        Calendar newDate = Calendar.getInstance();
        date = format1.format(newDate.getTime());

        btnSave.setOnClickListener(this);

        backTextView.setOnClickListener(this);

        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.male_btn_activity_patient_info) {
                    maleRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                    maleRadioButton.setTextColor(Color.parseColor("#33d16b"));
                    femaleRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    femaleRadioButton.setTextColor(Color.parseColor("#5c5c5c"));
                } else if (checkedId == R.id.feamale_btn_activity_patient_info) {
                    femaleRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                    femaleRadioButton.setTextColor(Color.parseColor("#33d16b"));
                    maleRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    maleRadioButton.setTextColor(Color.parseColor("#5c5c5c"));
                }
            }
        });

        confirmRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.confirm_btn_activity_patient_info) {
                    confirmRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                    confirmRadioButton.setTextColor(Color.parseColor("#33d16b"));
                    suspicionRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    suspicionRadioButton.setTextColor(Color.parseColor("#5c5c5c"));
                    dateInfo.setVisibility(View.VISIBLE);
                    linCalendar.setVisibility(View.VISIBLE);
                    calendarTextView.setVisibility(View.VISIBLE);
                    calendarTextView.setEnabled(true);

                } else if (checkedId == R.id.suspicion_btn_activity_patient_info) {
                    suspicionRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                    suspicionRadioButton.setTextColor(Color.parseColor("#33d16b"));
                    confirmRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    confirmRadioButton.setTextColor(Color.parseColor("#5c5c5c"));
                    dateInfo.setVisibility(View.INVISIBLE);
                    linCalendar.setVisibility(View.GONE);
                    calendarTextView.setVisibility(View.INVISIBLE);
                    calendarTextView.setEnabled(false);
                }
            }
        });

        calendarTextView.setOnClickListener(this);
        dp = new DatePickerDialogActivity(this, new DatePickerDialogActivity.ConfirmButtonListener() {

            @Override
            public void confirmButton(String data) {
                calendarTextView.setText(data);
            }

        });


        editBirth.setText(userItem.getBirth() + "");
        editName.setText(userItem.getName());
        if (userItem.getGender() == 1) {
            maleRadioButton.setChecked(true);
            femaleRadioButton.setChecked(false);
        }else {
            maleRadioButton.setChecked(false);
            femaleRadioButton.setChecked(true);
        }
        if (userItem.getDiagnosisFlag() == 1) {
            confirmRadioButton.setChecked(true);
            suspicionRadioButton.setChecked(false);
            linCalendar.setVisibility(View.VISIBLE);
            calendarTextView.setVisibility(View.VISIBLE);
            calendarTextView.setText(userItem.getOutbreakDt());
        }else {
            confirmRadioButton.setChecked(false);
            suspicionRadioButton.setChecked(true);
            linCalendar.setVisibility(View.GONE);
            calendarTextView.setVisibility(View.GONE);
            calendarTextView.setText(Utils.formatYYYYMMDD.format(System.currentTimeMillis()));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_patient_info_save:
                btnSave.setClickable(false);
                new UpdateUserInfoNetwork().execute();
                break;
            case R.id.back_text_view_activity_patient_info:
                onBackPressed();
                break;
            case R.id.calendar_text_view_activity_patient_info:
                FragmentManager fragmentManager = getSupportFragmentManager();
                dp.show(fragmentManager, "test");
                break;
        }
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
            body.add("USER_NO", String.valueOf(userItem.getUserNo()));
            body.add("GENDER", maleRadioButton.isChecked() ? "1" : "2");
            body.add("NAME", editName.getText().toString());
            body.add("BIRTH", editBirth.getText().toString());
            body.add("DIAGNOSIS_FLAG", confirmRadioButton.isChecked() ? "1" : "2");
            if (confirmRadioButton.isChecked()) {
                body.add("OUTBREAK_DT", calendarTextView.getText().toString());
            }


            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateUserInfo(), body.build());

                Log.d("Response Signup Sns : ", response);

                return response;
            } catch (IOException e) {
                e.printStackTrace();
                btnSave.setClickable(true);
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
                    btnSave.setClickable(true);
                }else {
                    new GetReloadUserDataNetwork().execute();
                }
            }catch (JSONException e){
                btnSave.setClickable(true);
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
                body.add("PASSWORD", userItem.getPassword());
            }
            body.add("DEVICE_CODE", Utils.TOKEN);
            body.add("OS_TYPE", "1");

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.loginProcess(), body.build());

                Log.d("Response Signup Sns : ", response);

                return response;
            } catch (IOException e) {
                btnSave.setClickable(true);
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

                        OneBtnPopup(PatientInfoActivity.this,"회원정보 수정","저장되었습니다.","확인");
//                        customOneButtonView.show();

                        onBackPressed();
                    }else {
                        Log.d(TAG, "aliveFlag != 1");
                    }
                }else {
                    Log.d(TAG, "JSON array null");
                }
            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
                //                StartActivity(SplashActivity.this, HomeActivity.class);
            }
            btnSave.setClickable(true);
        }
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
                onBackPressed();
            }
        });

    }
}

