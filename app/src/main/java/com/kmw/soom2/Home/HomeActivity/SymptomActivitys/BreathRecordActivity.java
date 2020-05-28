package com.kmw.soom2.Home.HomeActivity.SymptomActivitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker;
import com.kmw.soom2.Common.Item.ForeignKeys;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.R;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.formatHHMM;
import static com.kmw.soom2.Common.Utils.formatHHMMSS;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD2;

public class BreathRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "BreathRecordActivity";
    TextView txtBack;
    TextView txtDate,txtTime;
    EditText edtValue;
    LinearLayout linValueBottom;
    ImageView imgEditClose;
    CheckBox chbCheck;
    LinearLayout linQuestion;
    Button btnFinish;
    ImageView imgRemove;

    Intent beforeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath_record);

        beforeIntent = getIntent();

        FindViewById();

        imgRemove.setVisibility(View.GONE);

        if (beforeIntent != null){
            if (beforeIntent.hasExtra("registerDt")){
                try {
                    txtDate.setText(formatYYYYMMDD2.format(formatYYYYMMDD.parse(beforeIntent.getStringExtra("registerDt").substring(0,10))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    txtTime.setText(Utils.formatHHMM.format(Utils.formatHHMMSS.parse(beforeIntent.getStringExtra("registerDt").substring(11,18))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                edtValue.setText(""+beforeIntent.getIntExtra("pefScore",0));

                if (beforeIntent.getIntExtra("inspiratorFlag",0) == 1){
                    chbCheck.setChecked(true);
                }else{
                    chbCheck.setChecked(false);
                }

                imgRemove.setVisibility(View.VISIBLE);
            }else if (beforeIntent.hasExtra("date")){
                txtDate.setText(beforeIntent.getStringExtra("date"));
                txtTime.setText(beforeIntent.getStringExtra("time"));
            }else{
                txtDate.setText(Utils.formatYYYYMMDD2.format(new Date(System.currentTimeMillis())));
                txtTime.setText(Utils.formatHHMM.format(new Date(System.currentTimeMillis())));
            }
        }else{
            txtDate.setText(Utils.formatYYYYMMDD2.format(new Date(System.currentTimeMillis())));
            txtTime.setText(Utils.formatHHMM.format(new Date(System.currentTimeMillis())));
        }

    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_breath_record_back);
        txtDate = (TextView)findViewById(R.id.txt_breath_record_date);
        txtTime = (TextView)findViewById(R.id.txt_breath_record_time);
        edtValue = (EditText)findViewById(R.id.edt_breath_record_value);
        imgRemove = (ImageView)findViewById(R.id.img_breath_record_remove);
        linValueBottom = (LinearLayout)findViewById(R.id.lin_breath_record_value_bottom);
        imgEditClose = (ImageView)findViewById(R.id.img_breath_record_edit_close);
        chbCheck = (CheckBox)findViewById(R.id.chb_breath_record);
        linQuestion = (LinearLayout)findViewById(R.id.lin_breath_record_question);
        btnFinish = (Button)findViewById(R.id.btn_breath_record_finish);

        txtBack.setOnClickListener(this);
        imgEditClose.setOnClickListener(this);
        linQuestion.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        imgRemove.setOnClickListener(this);
        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);

        edtValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    linValueBottom.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });

        edtValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0){
                    imgEditClose.setVisibility(View.VISIBLE);
                }else{
                    imgEditClose.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    void breathDetailPopup(){
        final Dialog dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_breath_detail,null);

        ImageView imgClose = (ImageView)layout.findViewById(R.id.img_breath_detail_popup_close);
        Button btnMore = (Button)layout.findViewById(R.id.btn_breath_detail_popup_more);

        dialog.setContentView(layout);
        dialog.show();
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.linkKeys != null) {
                    ForeignKeys key = Utils.linkKeys.stream().filter(new Predicate<ForeignKeys>() {
                        @Override
                        public boolean test(ForeignKeys foreignKeys) {
                            if (foreignKeys.getTitle().equals("wrtingPEFmore")) {
                                return true;
                            }
                            return false;
                        }
                    }).collect(Collectors.toList()).get(0);

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(key.getLinkUrl()));
                    startActivity(browserIntent);
                }
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void TwoBtnPopup(int no, String title, String contents, String btnLeftText, String btnRightText){

        final Dialog dateTimeDialog = new Dialog(this);

        LayoutInflater inflater = (LayoutInflater)getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
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
                dateTimeDialog.dismiss();
                imgRemove.setClickable(false);
                new DeletePefHomeFeedHistoryNetWork().execute();
            }
        });
    }

    String response = "";

    public class InsertPefHomeFeedHistoryNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String flag = "2";
            if (chbCheck.isChecked()){
                flag = "1";
            }else{
                flag = "2";
            }

            FormBody.Builder body;
            body = (new FormBody.Builder());

//            @SuppressLint("WrongThread") RequestBody body = new FormBody.Builder()
            body.add("USER_NO",""+ Utils.userItem.getUserNo());
            body.add("CATEGORY","22");

            try {
                body.add("REGISTER_DT", formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString()))
                        + " " + formatHHMMSS.format(formatHHMM.parse(txtTime.getText().toString())));
            } catch (ParseException e) {
                e.printStackTrace();
                body.add("REGISTER_DT",Utils.formatYYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis())));
            }
//                    .add("REGISTER_DT", Utils.formatYYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis())))
            body.add("PEF_SCORE",edtValue.getText().toString());
            body.add("INSPIRATOR_FLAG",flag);
            body.add("NICKNAME", Utils.userItem.getNickname());
            body.add("GENDER", String.valueOf(Utils.userItem.getGender()));
            body.add("BIRTH", String.valueOf(Utils.userItem.getBirth()));


            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.insertPefHomeFeedHistoryList(), body.build());
                Log.d("Response", response);
                return response;
            } catch (IOException e) {
                btnFinish.setClickable(true);
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
                    btnFinish.setClickable(true);
                    onBackPressed();
                }
            },500);
        }
    }

    public class UpdatePefHomeFeedHistoryNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String flag = "2";
            if (chbCheck.isChecked()){
                flag = "1";
            }else{
                flag = "2";
            }

            FormBody.Builder body;
            body = (new FormBody.Builder());

            body.add("USER_HISTORY_NO",beforeIntent.getStringExtra("medicineHistoryNo"));
            body.add("CATEGORY","22");
            try {
                body.add("REGISTER_DT", formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString()))
                        + " " + formatHHMMSS.format(formatHHMM.parse(txtTime.getText().toString())));
            } catch (ParseException e) {
                e.printStackTrace();
                body.add("REGISTER_DT",beforeIntent.getStringExtra("registerDt"));
            }
            body.add("PEF_SCORE",edtValue.getText().toString());
            body.add("INSPIRATOR_FLAG",flag);
//            @SuppressLint("WrongThread") RequestBody body = new FormBody.Builder()
//                    .add("USER_HISTORY_NO",beforeIntent.getStringExtra("medicineHistoryNo"))
//                    .add("CATEGORY","22")
//                    .add("REGISTER_DT",beforeIntent.getStringExtra("registerDt"))
//                    .add("PEF_SCORE",edtValue.getText().toString())
//                    .add("INSPIRATOR_FLAG",flag)
//                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updatePefHomeFeedHistoryList(), body.build());
                Log.d("Response", response);
//                logLargeString(response);
                return response;
            } catch (IOException e) {
                btnFinish.setClickable(true);
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
                    btnFinish.setClickable(true);
                    onBackPressed();
                }
            },500);
        }
    }

    public class DeletePefHomeFeedHistoryNetWork extends AsyncTask<String, String, String> {

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
                imgRemove.setClickable(true);
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
                    imgRemove.setClickable(true);
                    onBackPressed();
                }
            },500);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_breath_record_back : {
                onBackPressed();
                break;
            }
            case R.id.img_breath_record_edit_close : {
                edtValue.setText("");
                break;
            }
            case R.id.img_breath_record_remove : {
                TwoBtnPopup(0,"폐기능 기록","기록을 삭제하시겠습니까?","취소","확인");
                break;
            }
            case R.id.lin_breath_record_question : {
                breathDetailPopup();
                break;
            }
            case R.id.btn_breath_record_finish : {
                if (edtValue.getText().length() > 0){
                    btnFinish.setClickable(false);
                    if (beforeIntent != null) {
                        if (beforeIntent.hasExtra("registerDt")) {
                            new UpdatePefHomeFeedHistoryNetWork().execute();
                        }else{
                            new InsertPefHomeFeedHistoryNetWork().execute();
                        }
                    }else{
                        new InsertPefHomeFeedHistoryNetWork().execute();
                    }
                }
                break;
            }
            case R.id.txt_breath_record_date: {
                DateTimePicker(1, "날짜를 선택하세요.");
                break;
            }
            case R.id.txt_breath_record_time: {
                DateTimePicker("시간을 선택하세요.");
                break;
            }
        }
    }
    void DateTimePicker(final int flag, String title){
        View dateTimeView = getLayoutInflater().inflate(R.layout.pop_up_date_time_picker,null);
        final Dialog dateDialog = new Dialog(BreathRecordActivity.this);
        TextView txtTitle = (TextView)dateTimeView.findViewById(R.id.txt_calendar_picker_title);

        txtTitle.setText(title);

        dateDialog.setContentView(dateTimeView);
        dateDialog.show();

        final SingleDateAndTimePicker singleDateAndTimePicker = dateTimeView.findViewById(R.id.single_day_picker);

        singleDateAndTimePicker.setDisplayYears(true);
        singleDateAndTimePicker.setDisplayMonths(true);
        singleDateAndTimePicker.setDisplayDaysOfMonth(true);
        singleDateAndTimePicker.setDisplayHours(false);
        singleDateAndTimePicker.setDisplayMinutes(false);

        Date date = null;

        Log.d(TAG,"txtDate : " + txtDate.getText().toString());

        try {
            date = formatYYYYMMDD2.parse(txtDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        singleDateAndTimePicker.selectDate(calendar);

        singleDateAndTimePicker.clickDateChange(new SingleDateAndTimePicker.OnCustomClick() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                Log.d(TAG, "onDateChanged " + displayed + ", date : " + date);
                txtDate.setText(formatYYYYMMDD2.format(date));
                dateDialog.dismiss();
            }
        });

        singleDateAndTimePicker.clickCancelDialog(new SingleDateAndTimePicker.OnCancelClick() {
            @Override
            public void onDialogCancel() {
                dateDialog.dismiss();
            }
        });
    }
    void DateTimePicker(String title) {
        View dateTimeView = getLayoutInflater().inflate(R.layout.pop_up_date_time_picker,null);
        final Dialog dateTimeDialog = new Dialog(BreathRecordActivity.this);
        TextView txtTitle = (TextView)dateTimeView.findViewById(R.id.txt_calendar_picker_title);

        txtTitle.setText(title);

        dateTimeDialog.setContentView(dateTimeView);
        dateTimeDialog.show();

        final SingleDateAndTimePicker singleDateAndTimePicker = (SingleDateAndTimePicker)dateTimeView.findViewById(R.id.single_day_picker);

        singleDateAndTimePicker.setDisplayYears(false);
        singleDateAndTimePicker.setDisplayMonths(false);
        singleDateAndTimePicker.setDisplayDaysOfMonth(false);
        singleDateAndTimePicker.setDisplayHours(true);
        singleDateAndTimePicker.setDisplayMinutes(true);

        Date date = null;

        try {
            date = Utils.formatHHMM.parse(txtTime.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        singleDateAndTimePicker.selectDate(calendar);

        singleDateAndTimePicker.clickDateChange(new SingleDateAndTimePicker.OnCustomClick() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                Log.d(TAG, "onDateChanged " + displayed + ", date : " + date);
                txtTime.setText(Utils.formatHHMM.format(date));
                dateTimeDialog.dismiss();
            }
        });

        singleDateAndTimePicker.clickCancelDialog(new SingleDateAndTimePicker.OnCancelClick() {
            @Override
            public void onDialogCancel() {
                dateTimeDialog.dismiss();
            }
        });
    }
}
