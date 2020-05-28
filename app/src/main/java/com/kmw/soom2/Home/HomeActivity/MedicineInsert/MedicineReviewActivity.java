package com.kmw.soom2.Home.HomeActivity.MedicineInsert;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.R;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class MedicineReviewActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    String TAG = "MedicineReviewActivity";
    ImageView imgBack;
    TextView txtTerm;
    RadioButton btnFirst,btnSecond,btnThird;
    EditText edtContents;
    Button btnSave;
    TextView txtTitle;
    ImageView imgRemove;
    TextView txtContentsLength;

    String[] termStrings = new String[]{"2주 이내","~1달","~3달","6달 이상"};

    Intent beforeIntent;

    int sideEffectFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_review);

        beforeIntent = getIntent();

        FindViewById();

        if (beforeIntent != null){
            if (beforeIntent.hasExtra("reviewNo")){
                txtTitle.setText("리뷰 수정");
                imgRemove.setVisibility(View.VISIBLE);

                txtTerm.setText(beforeIntent.getStringExtra("dosePeriod"));

                if (beforeIntent.getIntExtra("sideEffectFlag",0) == 1){
                    btnFirst.setChecked(true);
                    sideEffectFlag = 1;
                }else if (beforeIntent.getIntExtra("sideEffectFlag",0) == 2){
                    btnSecond.setChecked(true);
                    sideEffectFlag = 2;
                }else if (beforeIntent.getIntExtra("sideEffectFlag",0) == 3){
                    btnThird.setChecked(true);
                    sideEffectFlag = 3;
                }

                edtContents.setHint("");
                edtContents.setText(beforeIntent.getStringExtra("contents"));
            }else{
//                txtTerm.setText("2주 이내");
//                btnFirst.setChecked(true);
//                sideEffectFlag = 1;
            }
        }else{
//            txtTerm.setText("2주 이내");
//            btnFirst.setChecked(true);
//            sideEffectFlag = 1;
        }
    }

    void FindViewById(){
        imgBack = (ImageView)findViewById(R.id.img_medicine_review_insert_back);
        txtTitle = (TextView)findViewById(R.id.txt_review_title);
        imgRemove = (ImageView)findViewById(R.id.img_review_remove);
        txtTerm = (TextView)findViewById(R.id.txt_medicine_review_insert_term);
        btnFirst = (RadioButton)findViewById(R.id.rdo_medicine_review_insert_first);
        btnSecond = (RadioButton)findViewById(R.id.rdo_medicine_review_insert_second);
        btnThird = (RadioButton)findViewById(R.id.rdo_medicine_review_insert_third);
        edtContents = (EditText)findViewById(R.id.edt_medicine_review_insert_contents);
        btnSave = (Button)findViewById(R.id.btn_medicine_review_insert_save);
        txtContentsLength = (TextView)findViewById(R.id.txt_medicine_review_insert_contents_length);

        edtContents.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    if (edtContents.getText().length() == 0){
                        edtContents.setHint("");
                    }
                }
            }
        });

        edtContents.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtContentsLength.setText("("+s.length()+"/5000)");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgBack.setOnClickListener(this);
        txtTerm.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        imgRemove.setOnClickListener(this);
        btnFirst.setOnCheckedChangeListener(this);
        btnSecond.setOnCheckedChangeListener(this);
        btnThird.setOnCheckedChangeListener(this);
    }

    void DayCntPicker(String title){
        View dateTimeView = getLayoutInflater().inflate(R.layout.dialog_number_picker,null);
        TextView txtTitle = (TextView)dateTimeView.findViewById(R.id.txt_dialog_number_picker_title);
        Button btnCancel = (Button)dateTimeView.findViewById(R.id.btn_dialog_number_picker_cancel);
        Button btnDone = (Button)dateTimeView.findViewById(R.id.btn_dialog_number_picker_done);
        final NumberPicker numberPicker = (NumberPicker)dateTimeView.findViewById(R.id.number_picker);
        final Dialog dateTimeDialog = new Dialog(MedicineReviewActivity.this);

        dateTimeDialog.setContentView(dateTimeView);
        dateTimeDialog.show();

        numberPicker.setWrapSelectorWheel(false);

        txtTitle.setText(title);

        numberPicker.setMinValue(0);

        numberPicker.setMaxValue(termStrings.length-1);
        numberPicker.setDisplayedValues(termStrings);

        for (int i = 0; i < termStrings.length; i++){
            if (termStrings[i].equals(txtTerm.getText().toString())){
                numberPicker.setValue(i);
            }
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idx = numberPicker.getValue();
                txtTerm.setText(termStrings[idx]);
                dateTimeDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_medicine_review_insert_back : {
                onBackPressed();
                break;
            }
            case R.id.txt_medicine_review_insert_term : {
                DayCntPicker("복용기간 선택");
                break;
            }
            case R.id.btn_medicine_review_insert_save : {
                btnSave.setClickable(false);
                if (beforeIntent != null) {
                    if (beforeIntent.hasExtra("reviewNo")) {
                        new UpdateReviewNetWork().execute();
                    }else{
                        if (sideEffectFlag != 0 && txtTerm.getText().length() != 0){
                            new InsertReviewNetWork().execute();
                        }
                    }
                }else{
                    if (sideEffectFlag != 0 && txtTerm.getText().length() != 0){
                        new InsertReviewNetWork().execute();
                    }
                }
                break;
            }
            case R.id.img_review_remove : {
                imgRemove.setClickable(false);
                new DeleteReviewNetWork().execute();
                break;
            }
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.rdo_medicine_review_insert_first : {
                if (isChecked){
                    btnFirst.setBackgroundResource(R.drawable.toggle_btn_checked);
                    btnFirst.setTextColor(Color.parseColor("#33d16b"));
                    sideEffectFlag = 1;
                }else{
                    btnFirst.setBackgroundResource(R.drawable.toggle_btn);
                    btnFirst.setTextColor(Color.parseColor("#5c5c5c"));
                }
                break;
            }
            case R.id.rdo_medicine_review_insert_second : {
                if (isChecked){
                    btnSecond.setBackgroundResource(R.drawable.toggle_btn_checked);
                    btnSecond.setTextColor(Color.parseColor("#33d16b"));
                    sideEffectFlag = 2;
                }else{
                    btnSecond.setBackgroundResource(R.drawable.toggle_btn);
                    btnSecond.setTextColor(Color.parseColor("#5c5c5c"));
                }
                break;
            }
            case R.id.rdo_medicine_review_insert_third : {
                if (isChecked){
                    btnThird.setBackgroundResource(R.drawable.toggle_btn_checked);
                    btnThird.setTextColor(Color.parseColor("#33d16b"));
                    sideEffectFlag = 3;
                }else{
                    btnThird.setBackgroundResource(R.drawable.toggle_btn);
                    btnThird.setTextColor(Color.parseColor("#5c5c5c"));
                }
                break;
            }
        }
    }

    String response = "";

    public class InsertReviewNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder()
                    .add("USER_NO", String.valueOf(Utils.userItem.getUserNo()))
                    .add("MEDICINE_NO",""+beforeIntent.getIntExtra("medicineNo",0))
                    .add("CONTENTS",edtContents.getText().toString())
                    .add("DOSE_PERIOD",txtTerm.getText().toString())
                    .add("SIDE_EFFECT_FLAG",""+sideEffectFlag)
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.insertMedicineReview(), body);

                return response;
            } catch (IOException e) {
                btnSave.setClickable(true);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,"insert medicine : " + s);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnSave.setClickable(true);
                    onBackPressed();
                }
            },500);
        }
    }

    public class UpdateReviewNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder()
                    .add("REVIEW_NO", ""+beforeIntent.getIntExtra("reviewNo",0))
                    .add("MEDICINE_NO",""+beforeIntent.getIntExtra("medicineNo",0))
                    .add("CONTENTS",edtContents.getText().toString())
                    .add("DOSE_PERIOD",txtTerm.getText().toString())
                    .add("SIDE_EFFECT_FLAG",""+sideEffectFlag)
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateMedicineReview(), body);

                return response;
            } catch (IOException e) {
                btnSave.setClickable(true);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,"insert medicine : " + s);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnSave.setClickable(true);
                    onBackPressed();
                }
            },500);
        }
    }

    public class DeleteReviewNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder()
                    .add("REVIEW_NO", ""+beforeIntent.getIntExtra("reviewNo",0))
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.deleteMedicineReview(), body);
                Log.i(TAG,"review_response : " + response);
                return response;
            } catch (IOException e) {
                imgRemove.setClickable(true);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,"delete medicine : " + s);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    imgRemove.setClickable(true);
                    onBackPressed();
                }
            },500);
        }
    }
}
