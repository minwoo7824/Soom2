package com.kmw.soom2.InsertActivity.InsertActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class EmailSignupActivity extends AppCompatActivity {
    final String TAG = "EmailSignupActivity";
    TextView mEmailWarningTextView, mPasswordWarningTextView, mNickNameWarningTextView, mBackButtonTextView;
    EditText mEmailEditText, mPasswordEditText, mNicknameEditText;
    Button mSuccessButton;
    InputMethodManager mInputMethdManager;
    TextView mEditTextCountTextView;
    ItemClass itemClass = new ItemClass();
    Intent beforeIntent;
    String response;

    boolean enableEmail = false;
    boolean enableNickname = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_signup);

        beforeIntent = getIntent();

        mInputMethdManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        mEditTextCountTextView = (TextView) findViewById(R.id.edit_text_count_text_view_activity_email_signup);
        mEmailWarningTextView = (TextView) findViewById(R.id.email_warning_activity_email_signup);
        mPasswordWarningTextView = (TextView) findViewById(R.id.password_warning_activity_email_signup);
        mNickNameWarningTextView = (TextView) findViewById(R.id.nickname_warning_activity_email_signup);
        mBackButtonTextView = (TextView) findViewById(R.id.back_text_view_activity_email_signup);

        mEmailEditText = (EditText) findViewById(R.id.email_edit_activity_email_signup);
        mPasswordEditText = (EditText) findViewById(R.id.password_edit_activity_email_signup);
        mNicknameEditText = (EditText) findViewById(R.id.nickname_edit_activity_email_signup);

        mSuccessButton = (Button) findViewById(R.id.success_btn_activity_email_signup);
        mSuccessButton.setBackgroundColor(Color.parseColor("#8C8C8C"));
        mEmailEditText.clearFocus();


        mBackButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());


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

        mPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mPasswordEditText.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (mPasswordEditText.length() > 0) {
                            if (keyCode == KeyEvent.KEYCODE_DEL) {
                                mPasswordEditText.setText("");
                            }
                        }
                        return false;
                    }
                });
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mPasswordEditText.getText().toString().length() > 0 && itemClass.checkPass(mPasswordEditText.getText().toString())) {
                    mPasswordWarningTextView.setText("사용가능한 비밀번호입니다.");
                    mPasswordWarningTextView.setTextColor(Color.parseColor("#33d16b"));
                    mPasswordWarningTextView.setVisibility(View.VISIBLE);
                } else if (mPasswordEditText.getText().toString().length() > 0 && !itemClass.checkPass(mPasswordEditText.getText().toString())) {
                    mPasswordWarningTextView.setText("비밀번호 형식이 올바르지 않습니다.");
                    mPasswordWarningTextView.setTextColor(Color.parseColor("#ff0000"));
                    mPasswordWarningTextView.setVisibility(View.VISIBLE);
                } else if (mPasswordEditText.getText().toString().length() == 0) {
                    mPasswordWarningTextView.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (mNicknameEditText.getText().toString().length() > 0
                        && mPasswordEditText.getText().toString().length() > 0 && itemClass.checkPass(mPasswordEditText.getText().toString())
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

        mNicknameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mNickNameWarningTextView.setVisibility(View.INVISIBLE);
                } else if (!hasFocus && mNicknameEditText.getText().toString().length() > 0 && itemClass.checkNick(mNicknameEditText.getText().toString())) {
                    new OverlapUserNickNameNetwork().execute(mNicknameEditText.getText().toString());
                } else if (!hasFocus && mNicknameEditText.getText().toString().length() > 0 && !itemClass.checkNick(mNicknameEditText.getText().toString())) {
                    mNickNameWarningTextView.setText("닉네임형식이 올바르지 않습니다.");
                    mNickNameWarningTextView.setTextColor(Color.parseColor("#ff0000"));
                    mNickNameWarningTextView.setVisibility(View.VISIBLE);
                } else {
                    mNickNameWarningTextView.setVisibility(View.INVISIBLE);
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
                    Log.i(TAG, "length : " + mNicknameEditText.getText().toString().length());
                if (mNicknameEditText.getText().toString().length() > 8) {
                    mNickNameWarningTextView.setText("닉네임은 여덟자까지 가능합니다.");
                    mNickNameWarningTextView.setTextColor(Color.parseColor("#ff0000"));
                    mNickNameWarningTextView.setVisibility(View.VISIBLE);
                    mSuccessButton.setEnabled(false);
                } else if (mNicknameEditText.getText().toString().length() < 2 || !itemClass.checkNick(mNicknameEditText.getText().toString())) {
                    mNickNameWarningTextView.setText("닉네임은 한글자 이상이어야 합니다.");
                    mNickNameWarningTextView.setTextColor(Color.parseColor("#ff0000"));
                    mNickNameWarningTextView.setVisibility(View.VISIBLE);
                    mSuccessButton.setEnabled(false);
                } else if (mNicknameEditText.getText().toString().length() < 9 || mNicknameEditText.getText().toString().length() > 1 || !itemClass.checkNick(mNicknameEditText.getText().toString())) {
                    mNickNameWarningTextView.setText("키보드의 '완료' 눌러 중복체크 해주세요.");
                    mNickNameWarningTextView.setTextColor(Color.parseColor("#477bf4"));
                    mNickNameWarningTextView.setVisibility(View.VISIBLE);
                    mSuccessButton.setEnabled(false);
                } else {
                    mNickNameWarningTextView.setVisibility(View.INVISIBLE);
                }
                result = mNicknameEditText.getText().toString();
                mEditTextCountTextView.setText("" + mNicknameEditText.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mNicknameEditText.getText().toString().length() > 0
                        && mPasswordEditText.getText().toString().length() > 0 && itemClass.checkPass(mPasswordEditText.getText().toString())
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
                        && itemClass.checkNick(mNicknameEditText.getText().toString())
                        && mPasswordEditText.getText().toString().length() > 0 && itemClass.checkPass(mPasswordEditText.getText().toString())
                        && mEmailEditText.getText().toString().length() > 0 && itemClass.checkEmail(mEmailEditText.getText().toString())
                        && mNicknameEditText.getText().toString().length() < 8 && mNicknameEditText.getText().toString().length() > 2) {
                    mNicknameEditText.clearFocus();
                    mSuccessButton.performClick();
                    hideKeyboard();

                } else {
                    mNicknameEditText.clearFocus();
                    hideKeyboard();
                }
                return false;
            }
        });
        mSuccessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enableEmail && enableNickname) {
                    Intent i = new Intent(EmailSignupActivity.this, UserChoiceActivity.class);
                    i.putExtra("LOGIN_TYPE", beforeIntent.getIntExtra("LOGIN_TYPE", 1));    // 2 : sns 가입, 1 :  일반 이메일
                    i.putExtra("EMAIL", mEmailEditText.getText().toString());
                    i.putExtra("NICKNAME", mNicknameEditText.getText().toString());
                    i.putExtra("ID", mEmailEditText.getText().toString());
                    i.putExtra("PASSWORD", mPasswordEditText.getText().toString());
                    startActivity(i);
                } else {

                }
            }
        });
    }

    private void hideKeyboard() {
        mInputMethdManager.hideSoftInputFromWindow(mNicknameEditText.getWindowToken(), 0);

    }

    private void isNicknameCanUsed() {
        mNickNameWarningTextView.setText("사용가능한 닉네임입니다.");
        mNickNameWarningTextView.setTextColor(Color.parseColor("#33d16b"));
        mNickNameWarningTextView.setVisibility(View.VISIBLE);
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
        mNickNameWarningTextView.setText("이미 사용중인 닉네임입니다.");
        mNickNameWarningTextView.setTextColor(Color.parseColor("#ff0000"));
        mNickNameWarningTextView.setVisibility(View.VISIBLE);
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
            Log.i(TAG, s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (JsonIsNullCheck(jsonObject, "result").equals("N")) {
                    // 사용 중
                    isNicknameCanNotUsed();
                    enableNickname = false;
                } else {
                    isNicknameCanUsed();
                    enableNickname = true;
                }
            } catch (JSONException e) {
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
            Log.i(TAG, s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (JsonIsNullCheck(jsonObject, "result").equals("N")) {
                    // 사용 중
                    isEmailCanNotUsed();
                    enableEmail = false;
                } else {
                    isEmailCanUsed();
                    enableEmail = true;
                }
            } catch (JSONException e) {
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }
}