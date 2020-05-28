package com.kmw.soom2.StaticFunc.Activitys.ActResult;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.R;

public class StaticActResultSecondActivity extends AppCompatActivity implements View.OnClickListener  {
    TextView txtBack;
    RadioGroup rdoGroup;
    Button btnNext;
    RadioButton rdo1,rdo2,rdo3,rdo4,rdo5;
    TextView txtActResultQuestion;

    Intent beforeIntent;

    int idxScore = 0;
    int actType = 0;    // 1 일반, 2 : 아동

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_result_second);

        FindViewById();

        beforeIntent = getIntent();

        actType = beforeIntent.getIntExtra("type",0);
        idxScore = Integer.parseInt(beforeIntent.getStringExtra("selected").split(",")[1]);

        btnNext.setEnabled(false);
        btnNext.setBackgroundTintList(getColorStateList(R.color.f5f5f5));
        btnNext.setTextColor(getResources().getColor(R.color.black));


        if (actType == 1) {
            txtActResultQuestion.setText(R.string.adult_check_second_text);
        }else {
            txtActResultQuestion.setText(R.string.kids_check_second_text);
        }

        setRdoId(idxScore);

    }
    void setRdoId(int idxScore) {
        int idValue = 0;
        if (idxScore == 1) {
            rdo1.setChecked(true);
        }else if (idxScore == 2) {
            rdo2.setChecked(true);
        }else if (idxScore == 3) {
            rdo3.setChecked(true);
        }else if (idxScore == 4) {
            rdo4.setChecked(true);
        }else {
            rdo5.setChecked(true);
        }
        btnNext.setEnabled(true);
        btnNext.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
        btnNext.setTextColor(getResources().getColor(R.color.white));
    }
    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_check_second_back);
        rdoGroup = (RadioGroup)findViewById(R.id.rdo_check_second_group);
        btnNext = (Button)findViewById(R.id.btn_check_second_next);

        txtActResultQuestion = findViewById(R.id.txt_act_result_second_question);

        rdo1 = findViewById(R.id.rdo_check_second1);
        rdo2 = findViewById(R.id.rdo_check_second2);
        rdo3 = findViewById(R.id.rdo_check_second3);
        rdo4 = findViewById(R.id.rdo_check_second4);
        rdo5 = findViewById(R.id.rdo_check_second5);

        rdo1.setEnabled(false);
        rdo2.setEnabled(false);
        rdo3.setEnabled(false);
        rdo4.setEnabled(false);
        rdo5.setEnabled(false);

        rdo1.setText((actType == 1) ? R.string.adult_check_page2_rdo_1 : R.string.kids_check_page2_rdo_1);
        rdo2.setText((actType == 2) ? R.string.adult_check_page2_rdo_1 : R.string.kids_check_page2_rdo_2);
        rdo3.setText((actType == 3) ? R.string.adult_check_page2_rdo_3 : R.string.kids_check_page2_rdo_3);
        rdo4.setText((actType == 4) ? R.string.adult_check_page2_rdo_4 : R.string.kids_check_page2_rdo_4);
        rdo5.setText((actType == 5) ? R.string.adult_check_page2_rdo_5 : R.string.kids_check_page2_rdo_5);

        txtBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_check_second_back : {
                onBackPressed();
                break;
            }
            case R.id.btn_check_second_next : {
                Intent i = new Intent(this, StaticActResultThirdActivity.class);
                i.putExtra("score", beforeIntent.getIntExtra("score", 0));
                i.putExtra("type", beforeIntent.getIntExtra("type",0));
                i.putExtra("selected", beforeIntent.getStringExtra("selected"));
                startActivity(i);
                break;
            }
        }
    }

//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        btnNext.setEnabled(true);
//        btnNext.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
//        btnNext.setTextColor(getResources().getColor(R.color.white));
//
//        int radioButtonID = group.getCheckedRadioButtonId();
//        View radioButton = group.findViewById(radioButtonID);
//        int idx = group.indexOfChild(radioButton);
//
//
//        idxScore = idx;
//    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}