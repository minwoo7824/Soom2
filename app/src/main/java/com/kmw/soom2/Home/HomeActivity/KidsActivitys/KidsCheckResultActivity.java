package com.kmw.soom2.Home.HomeActivity.KidsActivitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Home.HomeActivity.MainActivity;
import com.kmw.soom2.R;

import java.util.ArrayList;

public class KidsCheckResultActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "KidsCheckResultActivity";
    TextView txtTitle1,txtTitle2;
    LinearLayout linScore;
    TextView txtScore,txtContents;
    Button btnRetry,btnFinish;

    Intent beforeIntent;

    ArrayList<Activity> activityArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adult_check_result);

        beforeIntent = getIntent();

        FindViewById();

        if (beforeIntent.hasExtra("score")){
            if (beforeIntent.getIntExtra("score",0) == 25){
                txtTitle1.setText(getResources().getString(R.string.result_title1));
                txtTitle2.setText(getResources().getString(R.string.result_vice_title1));
                txtContents.setText(getResources().getString(R.string.result_vice_contents1));
                linScore.setBackgroundResource(R.drawable.bg_gradient_result01);
                txtScore.setText("25점");
            }else if (beforeIntent.getIntExtra("score",0) >= 20){
                txtTitle1.setText(getResources().getString(R.string.result_title2));
                txtTitle2.setText(getResources().getString(R.string.result_vice_title2));
                txtContents.setText(getResources().getString(R.string.result_vice_contents2));
                txtScore.setText(""+beforeIntent.getIntExtra("score",0)+"점");
                linScore.setBackgroundResource(R.drawable.bg_gradient_result02);
            }else{
                txtTitle1.setText(getResources().getString(R.string.result_title3));
                txtTitle2.setText(getResources().getString(R.string.result_vice_title3));
                txtContents.setText(getResources().getString(R.string.result_vice_contents3));
                txtScore.setText(""+beforeIntent.getIntExtra("score",0)+"점");
                linScore.setBackgroundResource(R.drawable.bg_gradient_result03);
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

        btnRetry.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_adult_check_result_retry : {
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("scoreRetry",true);
                startActivity(i);
                break;
            }
            case R.id.btn_adult_check_result_finish : {

                break;
            }
        }
    }
}
