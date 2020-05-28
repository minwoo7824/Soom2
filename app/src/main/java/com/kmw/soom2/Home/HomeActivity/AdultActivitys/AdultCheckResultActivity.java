package com.kmw.soom2.Home.HomeActivity.AdultActivitys;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.HomeActivity.CalendarActivity;
import com.kmw.soom2.Home.HomeActivity.MainActivity;
import com.kmw.soom2.Home.HomeActivity.SymptomActivitys.AsthmaControlActivity;
import com.kmw.soom2.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class AdultCheckResultActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "AdultCheckResultActivity";
    TextView txtTitle1,txtTitle2;
    LinearLayout linScore;
    TextView txtScore,txtContents;
    Button btnRetry,btnFinish;
    TextView txtTitile;

    TextView btnBack;
    LinearLayout linBack;

    Intent beforeIntent;
    int type;
    int status;
    String actSelected = "";
    ArrayList<Activity> activityArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adult_check_result);

        beforeIntent = getIntent();


        type = beforeIntent.getIntExtra("resultType",0);
        actSelected = beforeIntent.getStringExtra("kidsScore");

        FindViewById();

        if (beforeIntent.hasExtra("intoHomeFeed")) {
            linBack.setVisibility(View.VISIBLE);
            txtTitile.setText(beforeIntent.getStringExtra("registerDt").substring(0,10));
        }else {
            linBack.setVisibility(View.GONE);
        }

        if (beforeIntent.hasExtra("score")){
            if (beforeIntent.getIntExtra("score",0) == 25){
                txtTitle1.setText(getResources().getString(R.string.result_title1));
                txtTitle2.setText(getResources().getString(R.string.result_vice_title1));
                txtContents.setText(getResources().getString(R.string.result_vice_contents1));
                linScore.setBackgroundResource(R.drawable.bg_gradient_result01);
                txtScore.setText("25점");
                status = 1;
            }else if (beforeIntent.getIntExtra("score",0) >= 20){
                txtTitle1.setText(getResources().getString(R.string.result_title2));
                txtTitle2.setText(getResources().getString(R.string.result_vice_title2));
                txtContents.setText(getResources().getString(R.string.result_vice_contents2));
                txtScore.setText(""+beforeIntent.getIntExtra("score",0)+"점");
                linScore.setBackgroundResource(R.drawable.bg_gradient_result02);
                status = 2;
            }else{
                txtTitle1.setText(getResources().getString(R.string.result_title3));
                txtTitle2.setText(getResources().getString(R.string.result_vice_title3));
                txtContents.setText(getResources().getString(R.string.result_vice_contents3));
                txtScore.setText(""+beforeIntent.getIntExtra("score",0)+"점");
                linScore.setBackgroundResource(R.drawable.bg_gradient_result03);
                status = 3;
            }
        }
    }

    void FindViewById(){
        txtTitle1 = (TextView)findViewById(R.id.txt_adult_check_result_title);
        txtTitle2 = (TextView)findViewById(R.id.txt_adult_check_result_title2);
        linScore = (LinearLayout)findViewById(R.id.lin_adult_check_result_score);
        txtScore = (TextView)findViewById(R.id.txt_adult_check_result_score);
        txtContents = (TextView)findViewById(R.id.txt_adult_check_result_contents);
        btnRetry = (Button)findViewById(R.id.btn_adult_check_result_retry);
        btnFinish = (Button)findViewById(R.id.btn_adult_check_result_finish);
        txtTitile = (TextView)findViewById(R.id.txt_act_preview_result_date);

        btnBack = findViewById(R.id.txt_act_preview_result_back);
        linBack = findViewById(R.id.lin_act_preview_result_back);

        btnRetry.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    public void TwoBtnPopup(Context context, String title, String contents, String btnLeftText, String btnRightText){

        final Dialog dateTimeDialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.two_btn_popup,null);

        TextView txtTitle = (TextView)layout.findViewById(R.id.txt_two_btn_popup_title);
        TextView txtContents = (TextView)layout.findViewById(R.id.txt_two_btn_popup_contents);
        final Button btnLeft = (Button)layout.findViewById(R.id.btn_two_btn_popup_left);
        Button btnRight = (Button)layout.findViewById(R.id.btn_two_btn_popup_right);

        txtTitle.setText(title);
        txtContents.setText(contents);
        btnLeft.setText(btnLeftText);
        btnRight.setText(btnRightText);

        dateTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dateTimeDialog.getWindow();

        dateTimeDialog.setContentView(layout);
        dateTimeDialog.show();

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialog.dismiss();
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DeleteHomeFeedHistoryNetWork().execute();
                dateTimeDialog.dismiss();
            }
        });
    }

    String response = "";

    public class InsertActHomeFeedHistoryNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            @SuppressLint("WrongThread") RequestBody body = new FormBody.Builder()
                    .add("USER_NO",""+Utils.userItem.getUserNo())
                    .add("CATEGORY","21")
                    .add("REGISTER_DT", Utils.formatYYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis())))
                    .add("ACT_SCORE",""+beforeIntent.getIntExtra("score",0))
                    .add("ACT_TYPE",""+type)
                    .add("ACT_STATE",""+status)
                    .add("ACT_SELECTED",actSelected)
            .add("NICKNAME", Utils.userItem.getNickname())
            .add("GENDER", String.valueOf(Utils.userItem.getGender()))
            .add("BIRTH", String.valueOf(Utils.userItem.getBirth()))
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.insertActHomeFeedHistoryList(), body);
                Log.d("Response", response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i;
                    if (beforeIntent.hasExtra("homeFragment")){
                        Log.i(TAG,"1");
                        i = new Intent(AdultCheckResultActivity.this,MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }else{
                        Log.i(TAG,"2");
                        i = new Intent(AdultCheckResultActivity.this,CalendarActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                    btnFinish.setClickable(false);
                }
            },500);
//            try {
//                JSONObject jsonObject = new JSONObject(s);
//                if (Utils.JsonIsNullCheck(jsonObject, "result").equals("N")) {
//
//                }else {
//                    onBackPressed();
//                    btnFinish.setClickable(true);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }
    }

    public class UpdateActHomeFeedHistoryNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            @SuppressLint("WrongThread") RequestBody body = new FormBody.Builder()
                    .add("USER_HISTORY_NO",beforeIntent.getStringExtra("medicineHistoryNo"))
                    .add("CATEGORY","21")
                    .add("REGISTER_DT",beforeIntent.getStringExtra("registerDt"))
                    .add("ACT_SCORE",""+beforeIntent.getIntExtra("score",0))
                    .add("ACT_TYPE",""+type)
                    .add("ACT_STATE",""+status)
                    .add("ACT_SELECTED",actSelected)
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateActHomeFeedHistoryList(), body);
                Log.d("Response", response);
//                logLargeString(response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setResult(RESULT_OK);
                    onBackPressed();
                }
            },500);
        }
    }

    public class DeleteHomeFeedHistoryNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            @SuppressLint("WrongThread") RequestBody body = new FormBody.Builder()
                    .add("USER_HISTORY_NO",beforeIntent.getStringExtra("medicineHistoryNo"))
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.deleteHomeFeedHistoryList(), body);
                Log.d("Response", response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(AdultCheckResultActivity.this, AsthmaControlActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    if (beforeIntent != null){
                        if (beforeIntent.hasExtra("homeFragment")){
                            i.putExtra("homeFragment",true);
                        }
                    }
                    startActivity(i);
                    onBackPressed();
                }
            },500);
        }
    }

    @Override
    public void onBackPressed() {
        if (beforeIntent.hasExtra("medicineHistoryNo")){
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_adult_check_result_retry : {
                if (beforeIntent.hasExtra("medicineHistoryNo")){
                    TwoBtnPopup(this,"재검사","이전 검사결과가 삭제됩니다.\n다시 하시겠습니까?","취소","다시하기");
                }else{
                    Intent i = new Intent(this, AsthmaControlActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("scoreRetry",true);
                    if (beforeIntent != null){
                        if (beforeIntent.hasExtra("homeFragment")){
                            i.putExtra("homeFragment",true);
                        }
                    }
                    startActivity(i);
                }
                break;
            }
            case R.id.btn_adult_check_result_finish : {
                btnFinish.setClickable(false);
                if (beforeIntent.hasExtra("medicineHistoryNo")){
//                    new UpdateActHomeFeedHistoryNetWork().execute();
                    onBackPressed();
                    btnFinish.setClickable(true);
                }else{
                    new InsertActHomeFeedHistoryNetWork().execute();
                }
                break;
            }
            case R.id.txt_act_preview_result_back : {
                v.setClickable(false);
                onBackPressed();
                break;
            }
        }
    }
}
