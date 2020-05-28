package com.kmw.soom2.Home.HomeActivity.KidsActivitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.R;

public class KidsCheckFourActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener  {

    private String TAG = "KidsCheckFourActivity";
    TextView txtBack;
    RadioGroup rdoGroup;
    Button btnNext;
    RadioButton rdo1,rdo2,rdo3,rdo4,rdo5;

    int idxScore = 0;

    Intent beforeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kids_check_four);

        beforeIntent = getIntent();

        FindViewById();

        btnNext.setEnabled(false);
        btnNext.setBackgroundTintList(getColorStateList(R.color.f5f5f5));
        btnNext.setTextColor(getResources().getColor(R.color.black));

        rdoGroup.setOnCheckedChangeListener(this);
    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_adult_check_four_back);
        rdoGroup = (RadioGroup)findViewById(R.id.rdo_adult_check_four_group);
        btnNext = (Button)findViewById(R.id.btn_adult_check_four_next);

        rdo1 = (RadioButton)findViewById(R.id.rdo_1);
        rdo2 = (RadioButton)findViewById(R.id.rdo_2);
        rdo3 = (RadioButton)findViewById(R.id.rdo_3);
        rdo4 = (RadioButton)findViewById(R.id.rdo_4);
        rdo5 = (RadioButton)findViewById(R.id.rdo_5);

        txtBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        rdo1.setOnCheckedChangeListener(this);
        rdo2.setOnCheckedChangeListener(this);
        rdo3.setOnCheckedChangeListener(this);
        rdo4.setOnCheckedChangeListener(this);
        rdo5.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_adult_check_four_back : {
                onBackPressed();
                break;
            }
            case R.id.btn_adult_check_four_next : {
                if (idxScore == 0){
                    Toast.makeText(this, "항목을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    Intent i = new Intent(this, KidsCheckFiveActivity.class);
                    i.putExtra("score",idxScore + beforeIntent.getIntExtra("score",0));
                    i.putExtra("kidsScore",""+beforeIntent.getStringExtra("kidsScore")+","+idxScore);
                    if (beforeIntent != null){
                        if (beforeIntent.hasExtra("homeFragment")){
                            i.putExtra("homeFragment",true);
                        }
                    }
                    startActivity(i);
                }
                break;
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        btnNext.setEnabled(true);
        btnNext.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
        btnNext.setTextColor(getResources().getColor(R.color.white));

        int radioButtonID = group.getCheckedRadioButtonId();
        View radioButton = group.findViewById(radioButtonID);
        int idx = group.indexOfChild(radioButton);

        Log.i(TAG,"idx : " + idx);

        if (idx == 0){
            idxScore = 5;
        }else if (idx == 1){
            idxScore = 4;
        }else if (idx == 2){
            idxScore = 3;
        }else if (idx == 3){
            idxScore = 2;
        }else if (idx == 4){
            idxScore = 1;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            buttonView.setTextColor(Color.parseColor("#33d36b"));
        }else{
            buttonView.setTextColor(Color.parseColor("#acacac"));
        }
    }
}
