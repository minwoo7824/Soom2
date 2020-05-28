package com.kmw.soom2.MyPage.Activity;

import android.content.Intent;
import android.graphics.Color;
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

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;

import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.userItem;

public class InquiryActivity extends AppCompatActivity implements View.OnClickListener {
    final String TAG = "InquiryActivity";
    String response;

    RadioGroup inquiryRadioGroup;
    RadioButton inquiryRadioButton, declarationRadioButton;
    EditText inquiryEditText;
    TextView editTextCountTextView;
    EditText editEmail;
    TextView btnBack;
    Button btnSend;
    TextView txtTitle;

    Intent beforeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        beforeIntent = getIntent();

        editEmail = findViewById(R.id.email_edit_text_activity_inquiry);
        btnBack = findViewById(R.id.back_text_view_activity_inquiry);
        btnSend = findViewById(R.id.send_button_activity_inquiry);
        inquiryRadioGroup = (RadioGroup) findViewById(R.id.radio_group_activity_inquiry);
        inquiryRadioButton = (RadioButton) findViewById(R.id.inquiry_radio_btn_activity_inquiry);
        declarationRadioButton =(RadioButton) findViewById(R.id.declaration_radio_btn_activity_inquiry);
        inquiryEditText = (EditText) findViewById(R.id.inquiry_edit_text_activity_inquiry);
        editTextCountTextView = (TextView) findViewById(R.id.edit_text_count_text_view_activity_inquiry);
        txtTitle = (TextView)findViewById(R.id.txt_inquiry_title);

        if (beforeIntent != null){
            if (beforeIntent.hasExtra("inquiry")){
                txtTitle.setVisibility(View.VISIBLE);
                inquiryRadioGroup.setVisibility(View.GONE);
            }
        }
        editEmail.setText(Utils.userItem.getEmail());

        btnSend.setOnClickListener(this);
        btnBack.setOnClickListener(this);


        inquiryRadioButton.setChecked(true);
        inquiryRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (inquiryRadioButton.isChecked()) {
                    inquiryRadioButton.setBackgroundResource(R.drawable.inquiry_radio_button_checked_a);
                    inquiryRadioButton.setTextColor(Color.parseColor("#ffffff"));
                    declarationRadioButton.setBackgroundResource(R.drawable.inquiry_radio_button_b);
                    declarationRadioButton.setTextColor(Color.parseColor("#33d16b"));
                } else if (declarationRadioButton.isChecked()) {
                    declarationRadioButton.setBackgroundResource(R.drawable.inquiry_radio_button_checked_b);
                    declarationRadioButton.setTextColor(Color.parseColor("#ffffff"));
                    inquiryRadioButton.setBackgroundResource(R.drawable.inquiry_radio_button_a);
                    inquiryRadioButton.setTextColor(Color.parseColor("#33d16b"));
                }
            }
        });

        inquiryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextCountTextView.setText("" + inquiryEditText.length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_text_view_activity_inquiry:
                onBackPressed();
                break;
            case R.id.send_button_activity_inquiry:
                btnSend.setClickable(false);
                new SendInquiryNetwork().execute();
                break;
        }
    }

    public class SendInquiryNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder());
            body.add("USER_NO", String.valueOf(userItem.getUserNo()));
            body.add("CONTENTS", inquiryEditText.getText().toString());
            body.add("EMAIL", editEmail.getText().toString());
            body.add("WRITER", userItem.getNickname());

            if (beforeIntent != null){
                if (beforeIntent.hasExtra("inquiry")){
                    body.add("TITLE","신고하기");
                    body.add("INQUIRY_FLAG","2");
                }else{
                    body.add("TITLE", inquiryRadioButton.isChecked() ? "문의하기" : "신고하기");
                    body.add("INQUIRY_FLAG", inquiryRadioButton.isChecked() ? "1" : "2");
                }
            }else{
                body.add("TITLE", inquiryRadioButton.isChecked() ? "문의하기" : "신고하기");
                body.add("INQUIRY_FLAG", inquiryRadioButton.isChecked() ? "1" : "2");
            }

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.insertInquery(), body.build());

                Log.d("Response Signup Sns : ", response);

                return response;
            } catch (IOException e) {
                btnSend.setClickable(true);
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
                }else {
                    onBackPressed();
                }
                btnSend.setClickable(true);
            }catch (JSONException e){
                btnSend.setClickable(true);
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }
}
