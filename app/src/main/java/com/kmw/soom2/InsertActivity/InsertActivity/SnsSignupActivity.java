package com.kmw.soom2.InsertActivity.InsertActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.InsertActivity.Item.ItemClass;
import com.kmw.soom2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;

public class SnsSignupActivity extends AppCompatActivity {
    TextView mEmailWarningTextView, mNnicknameWarningTextView, mBackButtonTextView;
    EditText mEmailEditText, mNicknameEditText;
    Button mSuccessButton;
    InputMethodManager mInputMethdManager;
    TextView mEditTextCountTextView;
    SharedPreferences preferences;
    ItemClass itemClass = new ItemClass();

    final  String TAG = "SnsSignupActivity";
    String response;

    boolean enableEmail = false;
    boolean enableNickname = false;

    Intent beforIntent;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sns_signup);

        beforIntent = getIntent();


        mInputMethdManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        mEditTextCountTextView = (TextView) findViewById(R.id.edit_text_count_text_view_activity_sns_signup);
        mEmailWarningTextView = (TextView) findViewById(R.id.email_warning_activity_sns_signup);
        mNnicknameWarningTextView = (TextView) findViewById(R.id.nickname_warning_activity_sns_signup);
        mBackButtonTextView = (TextView) findViewById(R.id.back_text_view_activity_sns_signup);

        mEmailEditText = (EditText) findViewById(R.id.email_edit_activity_sns_signup);
        mNicknameEditText = (EditText) findViewById(R.id.nickname_edit_activity_sns_signup);

        mSuccessButton = (Button) findViewById(R.id.success_btn_activity_sns_signup);
        mSuccessButton.setBackgroundColor(Color.parseColor("#8C8C8C"));
        mEmailEditText.clearFocus();
        preferences = getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        Intent getIntent = getIntent();

//        String email = preferences.getString("email", "");

        String email = beforIntent.getStringExtra("EMAIL");
        Log.i("Snssignup", email);

        mEmailEditText.setText(email);

        if (!email.equals("")) {
            new OverlapEmailNetwork().execute(email);
        }
        mBackButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mEmailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mEmailWarningTextView.setVisibility(View.INVISIBLE);
                } else if (!hasFocus && mEmailEditText.getText().toString().length() > 0 && itemClass.checkEmail(mEmailEditText.getText().toString())) {
                    new OverlapEmailNetwork().execute(mEmailEditText.getText().toString());
                } else if (!hasFocus && mEmailEditText.getText().toString().length() > 0 && !itemClass.checkEmail(mEmailEditText.getText().toString())) {
                    mEmailWarningTextView.setText("이메일형식이 올바르지 않습니다.");
                    mEmailWarningTextView.setTextColor(Color.parseColor("#ff0000"));
                    mEmailWarningTextView.setVisibility(View.VISIBLE);
                } else if (mEmailEditText.getText().toString().length() == 0) {
                    mEmailWarningTextView.setVisibility(View.INVISIBLE);
                }
            }
        });


        mNicknameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                if (hasFocus) {
                    mNnicknameWarningTextView.setVisibility(View.INVISIBLE);
                } else if (!hasFocus && mNicknameEditText.getText().toString().length() > 0 && itemClass.checkNick(mNicknameEditText.getText().toString())) {
                    new OverlapUserNickNameNetwork().execute(mNicknameEditText.getText().toString());
                } else if (!hasFocus && mNicknameEditText.getText().toString().length() > 0 && !itemClass.checkNick(mNicknameEditText.getText().toString())) {
                    mNnicknameWarningTextView.setText("닉네임형식이 올바르지 않습니다.");
                    mNnicknameWarningTextView.setTextColor(Color.parseColor("#ff0000"));
                    mNnicknameWarningTextView.setVisibility(View.VISIBLE);
                } else if (mNicknameEditText.getText().toString().length() == 0) {
                    mNnicknameWarningTextView.setVisibility(View.INVISIBLE);
                }
            }

        });



        mNicknameEditText.addTextChangedListener(new TextWatcher() {
            String result;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(result))
                    if (mNicknameEditText.getText().toString().length() < 2 || !itemClass.checkNick(mNicknameEditText.getText().toString())) {
                        mNnicknameWarningTextView.setText("닉네임은 한글자 이상이어야 합니다.");
                        mNnicknameWarningTextView.setTextColor(Color.parseColor("#ff0000"));
                        mNnicknameWarningTextView.setVisibility(View.VISIBLE);
                        mSuccessButton.setEnabled(false);
                    }else if (mNicknameEditText.getText().toString().length() < 8 || mNicknameEditText.getText().toString().length() > 1 || !itemClass.checkNick(mNicknameEditText.getText().toString())){
                        mNnicknameWarningTextView.setText("키보드의 '완료'를 눌러 중복체크 해주세요.");
                        mNnicknameWarningTextView.setTextColor(Color.parseColor("#477bf4"));
                        mNnicknameWarningTextView.setVisibility(View.VISIBLE);
                        mSuccessButton.setEnabled(false);
                    }else if (mNicknameEditText.getText().toString().length() > 8){
                        mNnicknameWarningTextView.setText("닉네임은 여덟자까지 가능합니다.");
                        mNnicknameWarningTextView.setTextColor(Color.parseColor("#ff0000"));
                        mNnicknameWarningTextView.setVisibility(View.VISIBLE);
                        mSuccessButton.setEnabled(false);
                    }else {
                        mNnicknameWarningTextView.setVisibility(View.INVISIBLE);
                    }
                result = mNicknameEditText.getText().toString();
                mEditTextCountTextView.setText("" + mNicknameEditText.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mNicknameEditText.getText().toString().length() > 0
                        && mEmailEditText.getText().toString().length() > 0 && itemClass.checkEmail(mEmailEditText.getText().toString())
                        && itemClass.checkNick(mNicknameEditText.getText().toString())) {
                    mSuccessButton.setEnabled(true);
                    mSuccessButton.setBackgroundColor(Color.parseColor("#33d16b"));
                } else {
                    mSuccessButton.setEnabled(false);
                    mSuccessButton.setBackgroundColor(Color.parseColor("#acacac"));
                }
            }
        });

        mNicknameEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mNicknameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        && mNicknameEditText.getText().toString().length() > 0
                        && mEmailEditText.getText().toString().length() > 0 && itemClass.checkEmail(mEmailEditText.getText().toString())
                        && itemClass.checkNick(mNicknameEditText.getText().toString())
                        && mNicknameEditText.getText().toString().length() <= 8 && mNicknameEditText.getText().toString().length() >= 2) {
                    mNicknameEditText.clearFocus();
                    hideKeyboard();
//                    mSuccessButton.performClick();


                } else {
                    mNicknameEditText.clearFocus();
                    hideKeyboard();
                }
                return true;
            }
        });
        mSuccessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enableEmail && enableNickname) {
                    Intent i = new Intent(SnsSignupActivity.this, UserChoiceActivity.class);
                    i.putExtra("LOGIN_TYPE", beforIntent.getIntExtra("LOGIN_TYPE", 2));    // 2 : sns 가입, 1 :  일반 이메일
                    i.putExtra("EMAIL", mEmailEditText.getText().toString());
                    i.putExtra("NICKNAME", mNicknameEditText.getText().toString());
                    i.putExtra("ID", beforIntent.getStringExtra("ID"));

                    startActivity(i);
                }else {

                }
            }
        });


    }
    private void isNicknameCanUsed() {
        mNnicknameWarningTextView.setText("사용가능한 닉네임입니다.");
        mNnicknameWarningTextView.setTextColor(Color.parseColor("#33d16b"));
        mNnicknameWarningTextView.setVisibility(View.VISIBLE);
        if (mNicknameEditText.getText().toString().length() > 0
                && mEmailEditText.getText().toString().length() > 0 && itemClass.checkEmail(mEmailEditText.getText().toString())
                && itemClass.checkNick(mNicknameEditText.getText().toString())) {
            mSuccessButton.setEnabled(true);
            mSuccessButton.setBackgroundColor(Color.parseColor("#33d16b"));
        } else {
            mSuccessButton.setEnabled(false);
            mSuccessButton.setBackgroundColor(Color.parseColor("#acacac"));
        }
    }
    private void isNicknameCanNotUsed() {
        mNnicknameWarningTextView.setText("이미 사용중인 닉네임입니다.");
        mNnicknameWarningTextView.setTextColor(Color.parseColor("#ff0000"));
        mNnicknameWarningTextView.setVisibility(View.VISIBLE);
    }
    private void isEmailCanNotUsed() {
        mEmailWarningTextView.setText("이미 사용중인 이메일입니다.");
        mEmailWarningTextView.setTextColor(Color.parseColor("#ff0000"));
        mEmailWarningTextView.setVisibility(View.VISIBLE);
    }
    private void isEmailCanUsed() {
        mEmailWarningTextView.setText("사용가능한 이메일입니다.");
        mEmailWarningTextView.setTextColor(Color.parseColor("#33d16b"));
        mEmailWarningTextView.setVisibility(View.VISIBLE);
        if (mNicknameEditText.getText().toString().length() > 0
                && mEmailEditText.getText().toString().length() > 0 && itemClass.checkEmail(mEmailEditText.getText().toString())
                && itemClass.checkNick(mNicknameEditText.getText().toString())) {
            mSuccessButton.setEnabled(true);
            mSuccessButton.setBackgroundColor(Color.parseColor("#33d16b"));
        } else {
            mSuccessButton.setEnabled(false);
            mSuccessButton.setBackgroundColor(Color.parseColor("#acacac"));
        }
    }

    private void hideKeyboard() {
        mInputMethdManager.hideSoftInputFromWindow(mNicknameEditText.getWindowToken(), 0);

    }

    public class OverlapUserNickNameNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            String stEmail = strings[0];

            RequestBody body = new FormBody.Builder().add("NICKNAME", stEmail).build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.overlapUserNickname(), body);

                Log.d("Response overlap : ", response);


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
                if (JsonIsNullCheck(jsonObject, "result").equals("N")) {
                    // 사용 중
                    isNicknameCanNotUsed();
                    enableNickname = false;
                }else {
                    isNicknameCanUsed();
                    enableNickname = true;
                }
            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }
    public class OverlapEmailNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            String stEmail = strings[0];

            RequestBody body = new FormBody.Builder().add("EMAIL", strings[0]).build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.overlapUserEmail(), body);

                Log.d("Response overlap : ", response);


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
                if (JsonIsNullCheck(jsonObject, "result").equals("N")) {
                    // 사용 중
                    isEmailCanNotUsed();
                    enableEmail = false;
                }else {
                    isEmailCanUsed();
                    enableEmail = true;
                }
            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }
}