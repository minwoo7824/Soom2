package com.kmw.soom2.Home.HomeActivity.SymptomActivitys;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.HomeAdapter.SymptomGridViewAdapter;
import com.kmw.soom2.Home.HomeItem.SymptomGridViewItem;
import com.kmw.soom2.R;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.formatHHMM;
import static com.kmw.soom2.Common.Utils.formatHHMMSS;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD2;

public class SymptomRecord extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "SymptomRecord";
    TextView txtBack;
    TextView txtDate, txtTime;
    ImageView imgRemove;
    EditText memoEditText;
    ScrollView scrollView;
    GridView gridView;
    SymptomGridViewAdapter adapter;
    ArrayList<SymptomGridViewItem> arrayList;
    RadioGroup mSymptomRadioGroup;
    RadioButton mSymptomOneRadioButton, mSymptomTwoRadioButton,mSymptomThreeRadioButton, mSymptomFourRadioButton;
    Button btnFinish;

    Intent beforeIntent;
    String category = "";

    String[] causes = new String[]{"감기","미세먼지","찬바람","운동","집먼지진드기","애완동물","음식알러지","꽃가루","스트레스","담배연기","요리연기","모르겠어요"};

    public static ArrayList<String> checkedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_symptom_record);

        beforeIntent = getIntent();

        checkedList = new ArrayList<>();

        FindViewById();

//        i.putExtra("medicineHistoryNo",item.getEtcItem().getUserHistoryNo());
//        i.putExtra("medicineNo",item.getEtcItem().getMedicineNo());
//        i.putExtra("category",item.getMedicineKo().get(0));
//        i.putExtra("registerDt",item.getEtcItem().getRegisterDt());
//        i.putExtra("cause",item.getEtcItem().getCause());
//        i.putExtra("memo",item.getEtcItem().getMemo());

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

                if (beforeIntent.getStringExtra("category").equals("11")){
                    mSymptomOneRadioButton.setChecked(true);
                }else if (beforeIntent.getStringExtra("category").equals("12")){
                    mSymptomTwoRadioButton.setChecked(true);
                }else if (beforeIntent.getStringExtra("category").equals("13")){
                    mSymptomThreeRadioButton.setChecked(true);
                }else if (beforeIntent.getStringExtra("category").equals("14")){
                    mSymptomFourRadioButton.setChecked(true);
                }

                memoEditText.setText(beforeIntent.getStringExtra("memo"));

                String[] causeList = beforeIntent.getStringExtra("cause").split(",");
                for (int i = 0; i < causeList.length; i++){
                    arrayList.get(Integer.parseInt(causeList[i])).setChecked(true);
                    checkedList.add(causeList[i]);
                }
                category = beforeIntent.getStringExtra("category");
                adapter.notifyDataSetChanged();

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
        txtBack = (TextView)findViewById(R.id.txt_symptom_record_back);
        txtDate = (TextView)findViewById(R.id.txt_symptom_record_date);
        txtTime = (TextView)findViewById(R.id.txt_symptom_record_time);
        imgRemove = (ImageView)findViewById(R.id.trash_activity_symptom_record);
        memoEditText = (EditText) findViewById(R.id.memo_edit_text_activity_symptom_record);
        scrollView = (ScrollView) findViewById(R.id.scroll_view_activity_symptom_record);
        gridView = (GridView) findViewById(R.id.grid_view_activity_symptom_record);
        mSymptomRadioGroup = (RadioGroup) findViewById(R.id.symptom_toggle_btn_activity_symptom_record);
        mSymptomOneRadioButton = (RadioButton) findViewById(R.id.symptom_1_activity_symptom_record);
        mSymptomTwoRadioButton = (RadioButton) findViewById(R.id.symptom_2_activity_symptom_record);
        mSymptomThreeRadioButton = (RadioButton) findViewById(R.id.symptom_3_activity_symptom_record);
        mSymptomFourRadioButton = (RadioButton) findViewById(R.id.symptom_4_activity_symptom_record);
        btnFinish = (Button)findViewById(R.id.success_btn_activity_symptom_record);

        arrayList = new ArrayList<>();
        DataSet();

        adapter = new SymptomGridViewAdapter(this, arrayList);

        gridView.setAdapter(adapter);

        mSymptomRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.symptom_1_activity_symptom_record) {
                    mSymptomOneRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                    mSymptomOneRadioButton.setTextColor(Color.parseColor("#33d16b"));
                    mSymptomTwoRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    mSymptomTwoRadioButton.setTextColor(Color.parseColor("#000000"));
                    mSymptomThreeRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    mSymptomThreeRadioButton.setTextColor(Color.parseColor("#000000"));
                    mSymptomFourRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    mSymptomFourRadioButton.setTextColor(Color.parseColor("#000000"));
                    category = "11";
                }  if (checkedId == R.id.symptom_2_activity_symptom_record) {
                    mSymptomTwoRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                    mSymptomTwoRadioButton.setTextColor(Color.parseColor("#33d16b"));
                    mSymptomOneRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    mSymptomOneRadioButton.setTextColor(Color.parseColor("#000000"));
                    mSymptomThreeRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    mSymptomThreeRadioButton.setTextColor(Color.parseColor("#000000"));
                    mSymptomFourRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    mSymptomFourRadioButton.setTextColor(Color.parseColor("#000000"));
                    category = "12";
                } if (checkedId == R.id.symptom_3_activity_symptom_record) {
                    mSymptomThreeRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                    mSymptomThreeRadioButton.setTextColor(Color.parseColor("#33d16b"));
                    mSymptomTwoRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    mSymptomTwoRadioButton.setTextColor(Color.parseColor("#000000"));
                    mSymptomOneRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    mSymptomOneRadioButton.setTextColor(Color.parseColor("#000000"));
                    mSymptomFourRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    mSymptomFourRadioButton.setTextColor(Color.parseColor("#000000"));
                    category = "13";
                } if (checkedId == R.id.symptom_4_activity_symptom_record) {
                    mSymptomFourRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                    mSymptomFourRadioButton.setTextColor(Color.parseColor("#33d16b"));
                    mSymptomTwoRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    mSymptomTwoRadioButton.setTextColor(Color.parseColor("#000000"));
                    mSymptomThreeRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    mSymptomThreeRadioButton.setTextColor(Color.parseColor("#000000"));
                    mSymptomOneRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    mSymptomOneRadioButton.setTextColor(Color.parseColor("#000000"));
                    category = "14";
                }
            }
        });
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);

                memoEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus == true) {
                            scrollView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    scrollView.smoothScrollBy(0, 800);
                                }
                            }, 100);
                        }
                    }
                });
            }
        });

        memoEditText.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (memoEditText.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });

        txtBack.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        imgRemove.setOnClickListener(this);

        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);
    }

    public void DataSet() {
        arrayList.add(new SymptomGridViewItem(R.drawable.gridview_image_1, "감기", false));
        arrayList.add(new SymptomGridViewItem(R.drawable.gridview_image_2, "미세먼지", false));
        arrayList.add(new SymptomGridViewItem(R.drawable.gridview_image_3, "찬바람", false));
        arrayList.add(new SymptomGridViewItem(R.drawable.gridview_image_4, "운동", false));
        arrayList.add(new SymptomGridViewItem(R.drawable.gridview_image_5, "집먼지진드기", false));
        arrayList.add(new SymptomGridViewItem(R.drawable.gridview_image_6, "애완동물", false));
        arrayList.add(new SymptomGridViewItem(R.drawable.gridview_image_7, "음식알러지", false));
        arrayList.add(new SymptomGridViewItem(R.drawable.gridview_image_8, "꽃가루", false));
        arrayList.add(new SymptomGridViewItem(R.drawable.gridview_image_9, "스트레스", false));
        arrayList.add(new SymptomGridViewItem(R.drawable.gridview_image_10, "담배연기", false));
        arrayList.add(new SymptomGridViewItem(R.drawable.gridview_image_11, "요리연기", false));
        arrayList.add(new SymptomGridViewItem(R.drawable.gridview_image_12, "모르겠어요", false));
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
                imgRemove.setClickable(false);
                new DeleteHomeFeedHistoryNetWork().execute();
                dateTimeDialog.dismiss();
            }
        });
    }

    String response;

    public class UpdateHomeFeedHistoryNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String cause = "";
            for (int i = 0; i < checkedList.size(); i++){
                if (i == 0){
                    cause += checkedList.get(i);
                }else{
                    cause += ","+checkedList.get(i);
                }
            }
            FormBody.Builder body;
            body = (new FormBody.Builder());

            body.add("USER_HISTORY_NO",beforeIntent.getStringExtra("medicineHistoryNo"));
            body.add("CATEGORY",category);
            body.add("CAUSE",cause);
            body.add("MEMO",memoEditText.getText().toString());

            try {
                body.add("REGISTER_DT", formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString()))
                        + " " + formatHHMMSS.format(formatHHMM.parse(txtTime.getText().toString())));
            } catch (ParseException e) {
                e.printStackTrace();
                body.add("REGISTER_DT",beforeIntent.getStringExtra("registerDt"));
            }


//            @SuppressLint("WrongThread") RequestBody body = new FormBody.Builder()
//                    .add("USER_HISTORY_NO",beforeIntent.getStringExtra("medicineHistoryNo"))
//                    .add("CATEGORY",category)
//                    .add("REGISTER_DT",beforeIntent.getStringExtra("registerDt"))
//                    .add("CAUSE",cause)
//                    .add("MEMO",memoEditText.getText().toString())
//                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateSymptomHomeFeedHistory(), body.build());
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

    public class InsertHomeFeedHistoryNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String cause = "";
            for (int i = 0; i < checkedList.size(); i++){
                if (i == 0){
                    cause += checkedList.get(i);
                }else{
                    cause += ","+checkedList.get(i);
                }
            }
            FormBody.Builder body;
            body = (new FormBody.Builder());
            body.add("USER_NO",""+ Utils.userItem.getUserNo());
            body.add("CATEGORY",category);
            body.add("CAUSE",cause);
            body.add("MEMO",memoEditText.getText().toString());
            body.add("NICKNAME", Utils.userItem.getNickname());
            body.add("GENDER", String.valueOf(Utils.userItem.getGender()));
            body.add("BIRTH", String.valueOf(Utils.userItem.getBirth()));

            try {
                body.add("REGISTER_DT", formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString()))
                        + " " + formatHHMMSS.format(formatHHMM.parse(txtTime.getText().toString())));
            } catch (ParseException e) {
                e.printStackTrace();
                body.add("REGISTER_DT",Utils.formatYYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis())));
            }

//            @SuppressLint("WrongThread") RequestBody body = new FormBody.Builder()
//                    .add("USER_NO",""+ Utils.userItem.getUserNo())
//                    .add("CATEGORY",category)
//                    .add("REGISTER_DT", Utils.formatYYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis())))
//                    .add("CAUSE",cause)
//                    .add("MEMO",memoEditText.getText().toString())
//                    .add("NICKNAME", Utils.userItem.getNickname())
//                    .add("GENDER", String.valueOf(Utils.userItem.getGender()))
//                    .add("BIRTH", String.valueOf(Utils.userItem.getBirth()))
//                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.insertSymptomHomeFeedHistory(), body.build());
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
            case R.id.txt_symptom_record_back : {
                onBackPressed();
                break;
            }
            case R.id.success_btn_activity_symptom_record : {
                if (category.length() == 0 || checkedList.size() == 0){
                    Toast.makeText(this, "항목을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    btnFinish.setClickable(false);
                    if (beforeIntent != null){
                        if (beforeIntent.hasExtra("registerDt")){
                            new UpdateHomeFeedHistoryNetWork().execute();
                        }else{
                            new InsertHomeFeedHistoryNetWork().execute();
                        }
                    }else{
                        new InsertHomeFeedHistoryNetWork().execute();
                    }
                }
                break;
            }
            case R.id.trash_activity_symptom_record : {
                TwoBtnPopup(this,"증상기록","기록을 삭제하시겠습니까?","취소","확인");
                break;
            }
            case R.id.txt_symptom_record_date: {
                DateTimePicker(1, "날짜를 선택하세요.");
                break;
            }
            case R.id.txt_symptom_record_time: {
                DateTimePicker("시간을 선택하세요.");
                break;
            }
        }
    }
    void DateTimePicker(final int flag, String title){
        View dateTimeView = getLayoutInflater().inflate(R.layout.pop_up_date_time_picker,null);
        final Dialog dateDialog = new Dialog(SymptomRecord.this);
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
        final Dialog dateTimeDialog = new Dialog(SymptomRecord.this);
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