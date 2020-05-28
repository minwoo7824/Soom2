package com.kmw.soom2.InsertActivity.InsertActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.HomeActivity.MainActivity;
import com.kmw.soom2.InsertActivity.Item.UserItem;
import com.kmw.soom2.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.StartActivity;


public class HomeActivity extends AppCompatActivity {

    private String TAG = "HomeActivity";
    private SessionCallback mSessionCallback;

    String response;

    String email = "";
    String id;
    int loginType;

    /**
     * client 정보를 넣어준다.
     * */
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private static String OAUTH_CLIENT_ID = "7WKQmVJi1ykWlETPeUzB";
    private static String OAUTH_CLIENT_SECRET = "qeMzoQNmf3";
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";

    BottomSheetDialog mSignupBottomSheetDialog, mLoginBottomSheetDialog;
    Button mSignupButton, mLoginButton;
    Button mFakeNaverSignupButton, mKakaoSignupButton, mEmailSignupButton;
    Button mFakeNaverLoginButton, mKakaoLoginButton, mEmailLoginButton;
    OAuthLoginButton mNaverOAuthLoginButton, mNaverOauthSignupButton;
    TextView mSignupYkTextView1, mSignupYkTextView2;
    TextView mLoginYkTextView1, mLoginYkTextView2;
    private static OAuthLogin mOAuthLoginInstance;
    private static Context mContext;
    static int i = 0;

    //20200325 kmw
    private LoginButton btnKakaoLogin;
    View bottomSheetDialog_Signup_View;
    View bottomSheetDialog_Login_View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext = this;
        initData();

        preferences = getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        editor = preferences.edit();

        mSessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(mSessionCallback);
//        Session.getCurrentSession().checkAndImplicitOpen();

        FindViewById();
        kakaoUnlink();
    }

    void FindViewById(){

        bottomSheetDialog_Signup_View = getLayoutInflater().inflate(R.layout.activity_signup_dialog, null);
        bottomSheetDialog_Login_View = getLayoutInflater().inflate(R.layout.activity_login_dialog, null);

        mLoginBottomSheetDialog = new BottomSheetDialog(HomeActivity.this);
        mSignupBottomSheetDialog = new BottomSheetDialog(HomeActivity.this);

        mSignupBottomSheetDialog.setContentView(bottomSheetDialog_Signup_View);
        mLoginBottomSheetDialog.setContentView(bottomSheetDialog_Login_View);

        btnKakaoLogin = (LoginButton)mSignupBottomSheetDialog.findViewById(R.id.btn_kakao_login);

        mSignupButton = (Button) findViewById(R.id.sign_up_btn_activity_home);
        mLoginButton = (Button) findViewById(R.id.login_btn_activity_home);
        mFakeNaverSignupButton = (Button) bottomSheetDialog_Signup_View.findViewById(R.id.naver_sign_up_btn_activity_signup_dialog);
        mEmailSignupButton = (Button) bottomSheetDialog_Signup_View.findViewById(R.id.email_signup_btn_activity_signup_dialog);
        mKakaoSignupButton = (Button) bottomSheetDialog_Signup_View.findViewById(R.id.kakao_sign_up_btn_activity_signup_dialog);
        mFakeNaverLoginButton = (Button) bottomSheetDialog_Login_View.findViewById(R.id.naver_login_btn_activity_login_dialog);
        mKakaoLoginButton = (Button) bottomSheetDialog_Login_View.findViewById(R.id.kakao_login_btn_activity_login_dialog);
        mEmailLoginButton = (Button) bottomSheetDialog_Login_View.findViewById(R.id.email_login_btn_activity_login_dialog);
        mNaverOauthSignupButton = (OAuthLoginButton) bottomSheetDialog_Signup_View.findViewById(R.id.naver_OAuthlogin_btn_activity_signup_dialog);
        mNaverOAuthLoginButton = (OAuthLoginButton) bottomSheetDialog_Login_View.findViewById(R.id.naver_OAuthlogin_btn_activity_login_dialog);

        mSignupYkTextView1 = (TextView) bottomSheetDialog_Signup_View.findViewById(R.id.yk_text_view1_activity_signup_dialog);
        mSignupYkTextView2 = (TextView) bottomSheetDialog_Signup_View.findViewById(R.id.yk_text_view2_activity_signup_dialog);

        mLoginYkTextView1 = (TextView) bottomSheetDialog_Login_View.findViewById(R.id.yk_text_view1_activity_login_dialog);
        mLoginYkTextView2 = (TextView) bottomSheetDialog_Login_View.findViewById(R.id.yk_text_view2_activity_login_dialog);

        mSignupYkTextView1.setText(Html.fromHtml("<u>" + "이용약관" + "</u>" + " "));
        mSignupYkTextView2.setText(Html.fromHtml("<u>" + "개인정보처리방침" + "</u>"));

        mLoginYkTextView1.setText(Html.fromHtml("<u>" + "이용약관" + "</u>" + " "));
        mLoginYkTextView2.setText(Html.fromHtml("<u>" + "개인정보처리방침" + "</u>"));


        final View.OnClickListener signupYkOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, YKActivity.class);
                startActivity(intent);
                mSignupBottomSheetDialog.dismiss();
            }
        };

        final View.OnClickListener loginYkOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, YKActivity.class);
                startActivity(intent);
                mLoginBottomSheetDialog.dismiss();
            }
        };

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int height = dm.heightPixels;

        bottomSheetDialog_Signup_View.getLayoutParams().height = (int) (height * 0.55f);
        bottomSheetDialog_Login_View.getLayoutParams().height = (int) (height * 0.55f);

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignupBottomSheetDialog.show();
                mEmailSignupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSignupBottomSheetDialog.dismiss();
                        Intent intent = new Intent(HomeActivity.this, EmailSignupActivity.class);

                        intent.putExtra("LOGIN_TYPE",1);
                        startActivity(intent);
                    }
                });
                mFakeNaverSignupButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (preferences.getString("naver_id", null) != null) {
                            // 경고 다이얼로그
                            Log.i("경고", preferences.getString("naver_id", ""));
                            Log.i("경고", "경고");
                        } else {
                            new DeleteTokenTask().execute();
                            mSignupBottomSheetDialog.dismiss();
                            mOAuthLoginInstance.startOauthLoginActivity(HomeActivity.this, mOAuthLoginHandler);
                            mNaverOauthSignupButton.setOAuthLoginHandler(mOAuthLoginHandler);
//                            new RequestApiTask().execute();
                        }
                    }
                });
                mKakaoSignupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG,"preferences.getString(\"nickname\",null) : " +preferences.getString("nickname",null));
//                        if(preferences.getString("nickname",null) != null){
//
//                        }else {
                            mSignupBottomSheetDialog.dismiss();
                        onClickLogout();
                            Session session = Session.getCurrentSession();
                            session.addCallback(new SessionCallback());
                            session.open(AuthType.KAKAO_LOGIN_ALL, HomeActivity.this);
//                            btnKakaoLogin.performClick();
//                        }
                    }
                });
                mSignupYkTextView1.setOnClickListener(signupYkOnClickListener);
                mSignupYkTextView2.setOnClickListener(signupYkOnClickListener);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginBottomSheetDialog.show();
                mFakeNaverLoginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLoginBottomSheetDialog.dismiss();
                        mOAuthLoginInstance.startOauthLoginActivity(HomeActivity.this, mOAuthLoginHandler);
//                        mNaverOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
//                        new RequestApiTask().execute();
                    }
                });
                mKakaoLoginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLoginBottomSheetDialog.dismiss();
                        Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, HomeActivity.this);

                    }
                });

                mEmailLoginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLoginBottomSheetDialog.dismiss();
                        Intent intent = new Intent(HomeActivity.this, EmailLoginActivity.class);
                        startActivity(intent);


                    }
                });
                mLoginYkTextView1.setOnClickListener(loginYkOnClickListener);
                mLoginYkTextView2.setOnClickListener(loginYkOnClickListener);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(mSessionCallback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {

            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    //로그인에 실패했을 때. 인터넷 연결이 불안정한 경우도 여기에 해당한다.
                    int result = errorResult.getErrorCode();

                    if (result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
//                        finish();
//                        redirectSignupActivity();
                    } else {
                        Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다: " + errorResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
//                        redirectSignupActivity();
                    }

                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    //로그인 도중 세션이 비정상적인 이유로 닫혔을 때
                    Toast.makeText(getApplicationContext(), "세션이 닫혔습니다. 다시 시도해 주세요: " + errorResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
//                    redirectSignupActivity();
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    Log.d(TAG, "TAG " + result.toString());
                    //로그인에 성공했을 때
                    Intent intent = new Intent(getApplicationContext(), SnsSignupActivity.class);
                    Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                    String kakaoNickname = result.getNickname();


                    id = String.valueOf(result.getId());
                    Log.d(TAG, "result.getKakaoAccount().emailNeedsAgreement() : " +result.getKakaoAccount().emailNeedsAgreement());
//                    if (result.getKakaoAccount().emailNeedsAgreement() == OptionalBoolean.TRUE) //이메일 값이 존재하면
//                    {
//                        Log.d(TAG,"result.getKakaoAccount().getEmail() : " + result.getKakaoAccount().getEmail());
//                        if (result.getKakaoAccount().getEmail() != null) {
//                            email = result.getKakaoAccount().getEmail();
//                        }else {
//                            email = "";
//                        }
//
//                        editor.putString("email", email);
//
//                    }
                    Log.d(TAG,"result.getKakaoAccount().getEmail() : " + result.getKakaoAccount().getEmail());
                    if (result.getKakaoAccount().getEmail() != null) {
                        email = result.getKakaoAccount().getEmail();
                    }else {
                        email = "";
                    }

                    editor.putString("email", email);

                    editor.putString("nickname", kakaoNickname);
                    editor.apply();

                    loginType = 3;

                    new OverlapUserIdNetwork().execute(id);
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.i(TAG,"exception : " + exception);
            if (exception != null) {
                Log.i("세션", "세션 오픈을 실패했습니다.");
            }
//            redirectSignupActivity();
        }
    }
    public void onClickLogout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Log.d(TAG, "kakao logout");
            }
        });
    }
    private void kakaoUnlink() {
        UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d(TAG, " errorResult : " + errorResult.getErrorMessage());
            }

            @Override
            public void onSuccess(Long result) {
                Log.d(TAG, "kakao Unlink");
            }
        });
    }
    protected void redirectSignupActivity() {
        final Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void initData() {
        mOAuthLoginInstance = OAuthLogin.getInstance();

        mOAuthLoginInstance.showDevelopersLog(true);
        mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);
    }

    private class DeleteTokenTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            boolean isSuccessDeleteToken = mOAuthLoginInstance.logoutAndDeleteToken(mContext);

            if (!isSuccessDeleteToken) {
                // 서버에서 token 삭제에 실패했어도 클라이언트에 있는 token 은 삭제되어 로그아웃된 상태이다
                // 실패했어도 클라이언트 상에 token 정보가 없기 때문에 추가적으로 해줄 수 있는 것은 없음
                Log.d(TAG, "errorCode:" + mOAuthLoginInstance.getLastErrorCode(mContext));
                Log.d(TAG, "errorDesc:" + mOAuthLoginInstance.getLastErrorDesc(mContext));
            }

            return null;
        }

        protected void onPostExecute(Void v) {

        }
    }

    public class OverlapUserIdNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            String stEmail = strings[0];

            RequestBody body = new FormBody.Builder().add("ID", stEmail).build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.overlapUserId(), body);

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
                    new LoginProcessNetwork().execute();
                }else {
                    Intent i = new Intent(HomeActivity.this, SnsSignupActivity.class);
                    i.putExtra("ID", id);
                    i.putExtra("EMAIL", email);
                    i.putExtra("LOGIN_TYPE",loginType);

                    startActivity(i);
                }
            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }

    public class LoginProcessNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Nullable
        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder().add("ID", id).add("DEVICE_CODE", Utils.TOKEN).add("OS_TYPE", "1").build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.loginProcess(), body);

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

                        editor.putString("DEVICE_CODE",userItem.getDeviceCode());
                        editor.putString("USER_ID", userItem.getId());
                        editor.putString("USER_PASSWORD", userItem.getPassword());
                        editor.putInt("LOGIN_TYPE", userItem.getLoginType());
                        editor.putInt("OS_TYPE", userItem.getOsType());
                        editor.putInt("IS_LOGIN_FLAG", 1);
                        editor.apply();
                        if (Utils.userItem != null) {
                            StartActivity(HomeActivity.this, MainActivity.class);
                            finish();

                        }
                    }else {
                        Toast.makeText(HomeActivity.this,"탈퇴된 아이디 입니다.",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "로그인 실패1");
                    }
                }else {
                    Log.d(TAG, "로그인 실패2");
                }
            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }

    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {

        @Override
        public void run(boolean success) {

            if (success) {
               new Thread(){
                   public void run(){
                       String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
                       String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
                       String data = mOAuthLoginInstance.requestApi(mContext,accessToken,"https://openapi.naver.com/v1/nid/me");
                       Log.d(TAG, "mOAuthLoginHandler : " + data);
                       try {
                           JSONObject result = new JSONObject(data);
                           JSONObject jsonObject2 = (JSONObject) result.get("response");

                           email = jsonObject2.has("email") ? jsonObject2.getString("email") : "";
                           id = jsonObject2.getString("id");
                           loginType = 2;
                           new OverlapUserIdNetwork().execute(id);
                       } catch (JSONException e) {
                           Log.e(TAG,"e : " + e.getLocalizedMessage());
                           e.printStackTrace();
                       }
                   }
               }.start();
            }
        }

    };

    private class RefreshTokenTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            return mOAuthLoginInstance.refreshAccessToken(mContext);
        }

        protected void onPostExecute(String res) {

            setContentView(R.layout.activity_home);
        }
    }


}


