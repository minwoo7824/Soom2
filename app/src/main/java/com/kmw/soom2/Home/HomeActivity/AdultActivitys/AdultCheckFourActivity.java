package com.kmw.soom2.Home.HomeActivity.AdultActivitys;

import androidx.appcompat.app.AppCompatActivity;

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

import com.kmw.soom2.R;

public class AdultCheckFourActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener  , CompoundButton.OnCheckedChangeListener{

    private String TAG = "AdultCheckThirdActivity";
    TextView txtBack;
    RadioGroup rdoGroup;
    Button btnNext;

    int idxScore = 0;

    Intent beforeIntent;
    RadioButton rdo1,rdo2,rdo3,rdo4,rdo5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adult_check_four);

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
                Intent i = new Intent(this,AdultCheckFiveActivity.class);
                i.putExtra("score",(idxScore + 1 + beforeIntent.getIntExtra("score",0)));
                i.putExtra("kidsScore",""+beforeIntent.getStringExtra("kidsScore")+","+(idxScore + 1));
                if (beforeIntent != null){
                    if (beforeIntent.hasExtra("homeFragment")){
                        i.putExtra("homeFragment",true);
                    }
                }
                startActivity(i);
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

        idxScore = idx;
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
