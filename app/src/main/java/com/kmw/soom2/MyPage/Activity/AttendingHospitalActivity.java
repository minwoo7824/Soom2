package com.kmw.soom2.MyPage.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.CommunityFragmentFunc.Activitys.CommunityWriteActivity;
import com.kmw.soom2.MyPage.Item.HospitalItem;
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
import static com.kmw.soom2.Common.Utils.hospitalItem;
import static com.kmw.soom2.Common.Utils.userItem;

public class AttendingHospitalActivity extends AppCompatActivity implements View.OnClickListener {
    final String TAG = "AttendingHospital";
    EditText hospitalNameEditText, departmentNameEditText, doctorNameEditText;
    Button saveButton, shareButton;
    String address = "";

    HospitalItem hosItem = new HospitalItem();

    Intent beforeIntent;
    String response;
    TextView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attending_hospital);

        btnBack = findViewById(R.id.back_text_view_activity_attending_hospital);
        hospitalNameEditText = (EditText) findViewById(R.id.hospital_name_edit_text_activity_attending_hospital);
        departmentNameEditText = (EditText) findViewById(R.id.department_edit_text_activity_attending_hospital);
        doctorNameEditText = (EditText) findViewById(R.id.doctor_name_edit_text_activity_attending_hospital);

        saveButton = (Button) findViewById(R.id.save_button_activity_attending_hospital);
        shareButton = (Button) findViewById(R.id.share_button_activity_attending_hospital);

        btnBack.setOnClickListener(this);
        hospitalNameEditText.setFocusable(false);
        hospitalNameEditText.setClickable(false);
        hospitalNameEditText.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);

        beforeIntent = getIntent();
        hosItem = Utils.hospitalItem;
        if (hosItem != null) {
            hospitalNameEditText.setText(hosItem.getName());
            departmentNameEditText.setText(hosItem.getDepartment());
            doctorNameEditText.setText(hosItem.getDoctor());
            address = hosItem.getAddr();
            saveButton.setBackgroundTintList(getColorStateList(R.color.color467ff4));
            saveButton.setTextColor(getResources().getColor(R.color.white));
        }else {
            Log.d(TAG, "병원 정보 없음");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1111){
            if (resultCode == RESULT_OK){

                saveButton.setBackgroundTintList(getColorStateList(R.color.color467ff4));
                saveButton.setTextColor(getResources().getColor(R.color.white));

                hospitalNameEditText.setText(data.getStringExtra("hospitalName"));
                address = data.getStringExtra("hospitalAddress");

                Log.i(TAG,"name : " + data.getStringExtra("hospitalName"));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hospital_name_edit_text_activity_attending_hospital:
                hospitalNameEditText.setClickable(false);
                Intent intent = new Intent(AttendingHospitalActivity.this, HospitalSearchActivity.class);
                startActivityForResult(intent,1111);
                hospitalNameEditText.setClickable(true);
                break;

            case R.id.save_button_activity_attending_hospital:
                saveButton.setClickable(false);
                if (hospitalItem != null) {
                    new UpdateHospitalInfoNetwork().execute();
                }else {
                    new InsertHospitalInfoNetwork().execute();
                }
                break;

            case R.id.share_button_activity_attending_hospital:
                Intent i = new Intent(AttendingHospitalActivity.this, CommunityWriteActivity.class);
                i.putExtra("hospitalName",hosItem.getName());
                i.putExtra("hospitalAddress",address);
                i.putExtra("hospitalDoctorName",hosItem.getDoctor());
                i.putExtra("hospitalDepartment",hosItem.getDepartment());
                startActivity(i);
                break;
            case R.id.back_text_view_activity_attending_hospital:
                onBackPressed();
                break;
        }
    }
    public class InsertHospitalInfoNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder())
                    .add("USER_NO", ""+userItem.getUserNo());

            body.add("NAME", hospitalNameEditText.getText().toString());
            body.add("ADDR", address);
            if (departmentNameEditText.getText().toString() != "") {
                body.add("DEPARTMENT", departmentNameEditText.getText().toString());
            }
            if (doctorNameEditText.getText().toString() != "") {
                body.add("DOCTOR", doctorNameEditText.getText().toString());
            }

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.insertHospital(), body.build());

                Log.d("Response Signup Sns : ", response);

                return response;
            } catch (IOException e) {
                saveButton.setClickable(true);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (JsonIsNullCheck(jsonObject, "result") == "N") {
                    Log.d(TAG, " insert Error");
                }else {
                    Log.d(TAG, "insert Success");
                    new SelectHospitalInfoNetWork().execute();
                }
                saveButton.setClickable(true);
            }catch (JSONException e){
                saveButton.setClickable(true);
                Log.i(TAG, e.getLocalizedMessage());
                //                StartActivity(SplashActivity.this, HomeActivity.class);
            }
        }
    }
    public class UpdateHospitalInfoNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder())
                    .add("HOSPITAL_NO", String.valueOf(hosItem.getHospitalNo()));

            body.add("NAME", hospitalNameEditText.getText().toString());
            body.add("ADDR", address);
            if (departmentNameEditText.getText().toString() != "") {
                body.add("DEPARTMENT", departmentNameEditText.getText().toString());
            }
            if (doctorNameEditText.getText().toString() != "") {
                body.add("DOCTOR", doctorNameEditText.getText().toString());
            }

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateHospital(), body.build());

                Log.d("Response Signup Sns : ", response);

                return response;
            } catch (IOException e) {
                e.printStackTrace();
                saveButton.setClickable(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (JsonIsNullCheck(jsonObject, "result") == "N") {
                    Log.d(TAG, " insert Error");
                }else {
                    Log.d(TAG, "insert Success");
                    new SelectHospitalInfoNetWork().execute();
                }
                saveButton.setClickable(true);
            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
                saveButton.setClickable(true);
                //                StartActivity(SplashActivity.this, HomeActivity.class);
            }

        }
    }
    public class SelectHospitalInfoNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            Log.i(TAG,"select ");

            @SuppressLint("WrongThread")
            RequestBody body = new FormBody.Builder()
                    .add("USER_NO",""+ Utils.userItem.getUserNo()).build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectHospitalInfo(), body);

                Log.d(TAG , " Response " + response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                if (jsonArray.length() > 0) {
                    JSONObject object = jsonArray.getJSONObject(0);
                    HospitalItem hospitalItem = new HospitalItem();

                    hospitalItem.setHospitalNo(JsonIntIsNullCheck(object, "HOSPITAL_NO"));
                    hospitalItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                    hospitalItem.setName(JsonIsNullCheck(object, "NAME"));
                    hospitalItem.setAddr(JsonIsNullCheck(object, "ADDR"));
                    hospitalItem.setDepartment(JsonIsNullCheck(object, "DEPARTMENT"));
                    hospitalItem.setDoctor(JsonIsNullCheck(object, "DOCTOR"));
                    hospitalItem.setCreateDt(JsonIsNullCheck(object, "CREATE_DT"));
                    hospitalItem.setUpdateDt(JsonIsNullCheck(object, "UPDATE_DT"));

                    Utils.hospitalItem = hospitalItem;
                }
                OneBtnPopup(AttendingHospitalActivity.this,"병원정보 저장","병원정보가 저장되었습니다.","확인");
            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
            }
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
