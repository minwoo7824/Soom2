package com.kmw.soom2.InsertActivity.InsertActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.HomeActivity.MedicineInsert.Items.MedicineTypeItem;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.MEDICINE_TYPE_LIST;
import static com.kmw.soom2.Common.Utils.OneBtnPopup;

public class SearchIdActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = "SearchIdActivity";
    TextView mBackTextView;
    EditText mEmailEditText, mBirthEdit;
    Button mSuccessButton;
    InputMethodManager mInputMethdManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_id);

        mInputMethdManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        mBackTextView = (TextView) findViewById(R.id.back_text_view_activity_search_id);

        mEmailEditText = (EditText) findViewById(R.id.name_edit_activity_search_id);
        mBirthEdit = (EditText) findViewById(R.id.birth_edit_activity_search_id);

        mSuccessButton = (Button) findViewById(R.id.login_success_btn_activity_search_id);

        mBackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mBirthEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mBirthEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    mBirthEdit.clearFocus();
                    mSuccessButton.performClick();
                    hideKeyboard();
                }
                return false;
            }
        });

        mSuccessButton.setOnClickListener(this);

    }

    private void hideKeyboard() {
        mInputMethdManager.hideSoftInputFromWindow(mBirthEdit.getWindowToken(), 0);

    }

    String response = "";

    public class SearchIdNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder()
                    .add("NAME",mEmailEditText.getText().toString())
                    .add("BIRTH",mBirthEdit.getText().toString())
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.findUserId(), body);

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
                    OneBtnPopup(SearchIdActivity.this,"아이디 찾기","일치하는 ID가 없습니다.","확인");
                }else{
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    Intent intent = new Intent(SearchIdActivity.this, SearchIdResultActivity.class);
                    intent.putExtra("email",jsonArray.getJSONObject(0).getString("EMAIL"));
                    startActivityForResult(intent,1111);
                }

            }catch (JSONException e){

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 1111){
                onBackPressed();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_success_btn_activity_search_id : {
                if (mEmailEditText.getText().length() != 0 && mBirthEdit.getText().length() != 0){
                    new SearchIdNetWork().execute();
                }else{
                    OneBtnPopup(this,"아이디 찾기","정보를 정확히 입력해주세요.","확인");
                }
                break;
            }
        }
    }
}
