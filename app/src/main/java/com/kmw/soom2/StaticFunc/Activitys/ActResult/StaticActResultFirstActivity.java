package com.kmw.soom2.StaticFunc.Activitys.ActResult;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.R;

public class StaticActResultFirstActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private String TAG = "StaticActResultFirstActivity";
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
        setContentView(R.layout.activity_act_result_first);

        FindViewById();

        beforeIntent = getIntent();

        actType = beforeIntent.getIntExtra("type",0);
        idxScore = Integer.parseInt(beforeIntent.getStringExtra("selected").split(",")[0]);

        btnNext.setEnabled(false);
        btnNext.setBackgroundTintList(getColorStateList(R.color.f5f5f5));
        btnNext.setTextColor(getResources().getColor(R.color.black));


        if (actType == 1) {
            txtActResultQuestion.setText(R.string.adult_check_first_text);
        }else {
            txtActResultQuestion.setText(R.string.kids_check_first_text);
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
        txtBack = (TextView)findViewById(R.id.txt_check_first_back);
        rdoGroup = (RadioGroup)findViewById(R.id.rdo_check_first_group);
        btnNext = (Button)findViewById(R.id.btn_check_first_next);

        txtActResultQuestion = findViewById(R.id.txt_act_result_first_question);

        rdo1 = findViewById(R.id.rdo_check_first1);
        rdo2 = findViewById(R.id.rdo_check_first2);
        rdo3 = findViewById(R.id.rdo_check_first3);
        rdo4 = findViewById(R.id.rdo_check_first4);
        rdo5 = findViewById(R.id.rdo_check_first5);

        rdo1.setEnabled(false);
        rdo2.setEnabled(false);
        rdo3.setEnabled(false);
        rdo4.setEnabled(false);
        rdo5.setEnabled(false);

        rdo1.setText((actType == 1) ? R.string.adult_check_page1_rdo_1 : R.string.kids_check_page1_rdo_1);
        rdo2.setText((actType == 2) ? R.string.adult_check_page1_rdo_2 : R.string.kids_check_page1_rdo_2);
        rdo3.setText((actType == 3) ? R.string.adult_check_page1_rdo_3 : R.string.kids_check_page1_rdo_3);
        rdo4.setText((actType == 4) ? R.string.adult_check_page1_rdo_4 : R.string.kids_check_page1_rdo_4);
        rdo5.setText((actType == 5) ? R.string.adult_check_page1_rdo_5 : R.string.kids_check_page1_rdo_5);

        txtBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_check_first_back : {
                onBackPressed();
                break;
            }
            case R.id.btn_check_first_next : {
                Intent i = new Intent(this, StaticActResultSecondActivity.class);
                i.putExtra("score", beforeIntent.getIntExtra("score", 0));
                i.putExtra("type", beforeIntent.getIntExtra("type",0));
                i.putExtra("selected", beforeIntent.getStringExtra("selected"));
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
