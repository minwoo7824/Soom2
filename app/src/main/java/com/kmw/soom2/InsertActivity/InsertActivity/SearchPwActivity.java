package com.kmw.soom2.InsertActivity.InsertActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.InsertActivity.Item.ItemClass;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.OneBtnPopup;

public class SearchPwActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "SearchPwActivity";
    TextView txtBack;
    EditText edtEmail, edtName;
    Button btnFinish;
    InputMethodManager ime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pw);

        FindViewById();
    }

    void FindViewById(){
        ime = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        txtBack = (TextView) findViewById(R.id.back_text_view_activity_search_pw);
        edtEmail = (EditText) findViewById(R.id.name_edit_activity_search_pw);
        edtName = (EditText) findViewById(R.id.birth_edit_activity_search_pw);
        btnFinish = (Button) findViewById(R.id.login_success_btn_activity_search_pw);

        txtBack.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
    }

    String response = "";

    public class SearchPwNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder()
                    .add("EMAIL",edtEmail.getText().toString())
                    .add("NAME",edtName.getText().toString())
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.sendUserPasswordToEmail(), body);

                Log.i(TAG,"searchId type : " + response);
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

                if (jsonObject.get("result").equals("N")){
                    OneBtnPopup(SearchPwActivity.this,"비밀번호 찾기","일치하는 ID가 없습니다.","확인");
                }else{
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

//                    Intent intent = new Intent(SearchPwActivity.this, SearchIdResultActivity.class);
//                    intent.putExtra("email",jsonArray.getJSONObject(0).getString("EMAIL"));
//                    startActivityForResult(intent,1111);
                }

            }catch (JSONException e){

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_text_view_activity_search_pw : {
                onBackPressed();
                break;
            }
            case R.id.login_success_btn_activity_search_pw : {
                if (edtEmail.getText().length() == 0 || !ItemClass.checkEmail(edtEmail.getText().toString())){
                    OneBtnPopup(this,"로그인","아이디 또는 비밀번호를 확인해주세요.","확인");
                }else{
                    new SearchPwNetWork().execute();
                }
                break;
            }
        }
    }
}
