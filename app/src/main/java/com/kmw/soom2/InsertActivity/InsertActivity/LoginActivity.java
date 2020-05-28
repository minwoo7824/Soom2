package com.kmw.soom2.InsertActivity.InsertActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.HomeActivity.MainActivity;
import com.kmw.soom2.InsertActivity.Item.UserItem;
import com.kmw.soom2.R;
import com.nhn.android.naverlogin.OAuthLogin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.StartActivity;

public class LoginActivity extends AppCompatActivity {
    final String TAG = "LoginActivity";
    TextView mSearchIdTextView, mSearchPasswordTextView;
    TextView mBackTextView;
    EditText mEmailEditText, mPasswordEditText;
    Button mSuccessButton;
    Context mContext = LoginActivity.this;

    String response;

    private OAuthLogin mOAuthLoginModule;
    private static OAuthLogin mOAuthLoginInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mOAuthLoginModule = OAuthLogin.getInstance();

        mSearchIdTextView = (TextView) findViewById(R.id.search_id_activity_login);
        mSearchPasswordTextView = (TextView) findViewById(R.id.search_password_activity_login);
        mBackTextView = (TextView) findViewById(R.id.back_text_view_activity_login);

        mEmailEditText = (EditText) findViewById(R.id.email_edit_activity_login);
        mPasswordEditText = (EditText) findViewById(R.id.password_edit_activity_login);

        mSuccessButton = (Button) findViewById(R.id.login_success_btn_activity_login);
        mSearchIdTextView.setText("아이디찾기 ");
        mSearchPasswordTextView.setText("비밀번호찾기");

        mSearchPasswordTextView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        mPasswordEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        && mPasswordEditText.getText().toString().length() > 0 && checkPass(mPasswordEditText.getText().toString())
                        && mEmailEditText.getText().toString().length() > 0 && checkEmail(mEmailEditText.getText().toString())){
                    mSuccessButton.performClick();
                }
                    return false;
            }
        });
        mBackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        View.OnClickListener idSearchListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SearchIdActivity.class);
                startActivity(intent);
            }

        };

        mSearchIdTextView.setOnClickListener(idSearchListener);
        mSearchPasswordTextView.setOnClickListener(idSearchListener);

        mSuccessButton.setOnClickListener(new Button.OnClickListener() { // 임시 회원 탈퇴 버튼
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.login_success_btn_activity_login : {
                        if (mEmailEditText.getText().toString().length() > 0 && mPasswordEditText.getText().toString().length() > 0) {
                            new LoginProcessNetWork().execute();
                        }else {
                            Toast.makeText(getApplicationContext(), "아이디 비밀번호를 확인하여주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

//                new DeleteTokenTask().execute();
//                        new AlertDialog.Builder(LoginActivity.this)
//                        .setMessage("탈퇴하시겠습니까?")
//                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
//                                    @Override
//                                    public void onFailure(ErrorResult errorResult) {
//                                        int result = errorResult.getErrorCode();
//
//                                        if (result == ApiErrorCode.CLIENT_ERROR_CODE) {
//                                            Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            Toast.makeText(getApplicationContext(), "회원탈퇴에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onSessionClosed(ErrorResult errorResult) {
//                                        Toast.makeText(getApplicationContext(), "로그인 세션이 닫혔습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//
//                                    @Override
//                                    public void onNotSignedUp() {
//                                        Toast.makeText(getApplicationContext(), "가입되지 않은 계정입니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//
//                                    @Override
//                                    public void onSuccess(Long result) {
//                                        Toast.makeText(getApplicationContext(), "회원탈퇴에 성공했습니다.", Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                });
//
//                                dialog.dismiss();
//                            }
//                        })
//                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }).show();
            }
        });
    }
    private boolean checkEmail(String email) {
        final String mail = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(mail);
        Matcher m = p.matcher(email);
        return m.matches();
    }
    private boolean checkPass(String password) {
        Pattern p = Pattern.compile("^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z\\d$@$!%*#?&]{6,16}$");
        Matcher m = p.matcher(password);

        return m.matches();
    }
    private class DeleteTokenTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            boolean isSuccessDeleteToken = mOAuthLoginInstance.logoutAndDeleteToken(mContext);

            if (!isSuccessDeleteToken) {
                // 서버에서 token 삭제에 실패했어도 클라이언트에 있는 token 은 삭제되어 로그아웃된 상태이다
                // 실패했어도 클라이언트 상에 token 정보가 없기 때문에 추가적으로 해줄 수 있는 것은 없음

            }

            return null;
        }

        protected void onPostExecute(Void v) {
            setContentView(R.layout.activity_home);
        }
    }
    public class LoginProcessNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            @SuppressLint("WrongThread") String stEmail = mEmailEditText.getText().toString();
            @SuppressLint("WrongThread") String stPassword = mPasswordEditText.getText().toString();

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
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                if (jsonArray.length() > 0) {
                    JSONObject object = jsonArray.getJSONObject(0);
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
                }

                if (Utils.userItem != null) {
                    StartActivity(LoginActivity.this, MainActivity.class);
                }
            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }
}
