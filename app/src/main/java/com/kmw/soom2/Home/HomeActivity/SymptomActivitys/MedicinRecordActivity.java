package com.kmw.soom2.Home.HomeActivity.SymptomActivitys;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker;
import com.kmw.soom2.Common.HttpClient;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.HomeActivity.MainActivity;
import com.kmw.soom2.Home.HomeAdapter.MedicineRecordListAdapter;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.formatHHMMSS;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD2;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDDHHMMSS;
import static com.kmw.soom2.Common.Utils.setListViewHeightBasedOnChildren;

public class MedicinRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "MedicinRecordActivity";
    TextView txtBack;
    TextView txtBefore,txtDate,txtAfter;
    LinearLayout linNoList;
    ScrollView scrList;
    ListView listView;
    Button btnDateSelect,btnFinish;

    String registerDt = "";

    Calendar calendar = Calendar.getInstance();;
    Date date = new Date(System.currentTimeMillis());

    ArrayList<MedicineTakingItem> medicineTakingItems = new ArrayList<>();
    public static ArrayList<MedicineTakingItem> medicineTakingItems1 = new ArrayList<>();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    MedicineRecordListAdapter adapter;

//    public static SimpleDateFormat formatYYYYMMDDHHMMSS = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");

    ProgressDialog progressDialog;

    Intent beforeIntent;

    Calendar currentCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicin_record);
        progressDialog = new ProgressDialog(this);

        beforeIntent = getIntent();

        currentCalendar = Calendar.getInstance();

        FindViewById();

        if (beforeIntent.hasExtra("date")){
            txtDate.setText(beforeIntent.getStringExtra("date"));
            try {
                registerDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString())) + " " + formatHHMMSS.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            registerDt = formatYYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis()));
        }

        new SelectMedicineHomeFeedHistoryListNetWork().execute();
    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_medicine_record_back);
        txtBefore = (TextView)findViewById(R.id.txt_medicine_record_before);
        txtDate = (TextView)findViewById(R.id.txt_medicine_record_date);
        txtAfter = (TextView)findViewById(R.id.txt_medicine_record_after);
        linNoList = (LinearLayout)findViewById(R.id.lin_medicine_record_no_list);
        scrList = (ScrollView)findViewById(R.id.scr_medicine_record_list);
        listView = (ListView)findViewById(R.id.list_medicine_record);
        btnDateSelect = (Button)findViewById(R.id.btn_medicine_record_date_select);
        btnFinish = (Button)findViewById(R.id.btn_medicine_record_finish);

        txtDate.setText(Utils.formatYYYYMMDD2.format(date));

        calendar.setTime(date);

        txtBack.setOnClickListener(this);
        txtBefore.setOnClickListener(this);
        txtAfter.setOnClickListener(this);
        btnDateSelect.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
    }

    void DateTimePicker(){
        View dateTimeView = getLayoutInflater().inflate(R.layout.pop_up_date_time_picker,null);
        final Dialog dateTimeDialog = new Dialog(MedicinRecordActivity.this);
        dateTimeDialog.setContentView(dateTimeView);
        dateTimeDialog.show();

        final SingleDateAndTimePicker singleDateAndTimePicker = (SingleDateAndTimePicker)dateTimeView.findViewById(R.id.single_day_picker);

            singleDateAndTimePicker.setDisplayYears(false);
            singleDateAndTimePicker.setDisplayMonths(false);
            singleDateAndTimePicker.setDisplayDaysOfMonth(false);
            singleDateAndTimePicker.setDisplayHours(true);
            singleDateAndTimePicker.setDisplayMinutes(true);

            singleDateAndTimePicker.selectDate(calendar);

        singleDateAndTimePicker.clickDateChange(new SingleDateAndTimePicker.OnCustomClick() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                try {
                    registerDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString())) + " " + formatHHMMSS.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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

    //건너뜀0, 일반1, 응급2

    public class SelectMedicineHomeFeedHistoryListNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", Utils.Server.selectMedicineHistoryList());

            http.addOrReplace("USER_NO",""+ Utils.userItem.getUserNo());

            HttpClient post = http.create();
            post.request();
            String body = post.getBody();

            return body;
        }

        @Override
        protected void onPostExecute(String s) {

            Log.i(TAG,"복용중인 약 : " + s);

            adapter = new MedicineRecordListAdapter(getLayoutInflater(), MedicinRecordActivity.this);
            medicineTakingItems = new ArrayList<>();
            medicineTakingItems1 = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.has("list")){
                    linNoList.setVisibility(View.GONE);
                    scrList.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (JsonIntIsNullCheck(object,"ALIVE_FLAG") == 1 &&
                            Integer.parseInt(simpleDateFormat.format(formatYYYYMMDD2.parse(txtDate.getText().toString()))) >= Integer.parseInt(JsonIsNullCheck(object,"START_DT")) &&
                                Integer.parseInt(simpleDateFormat.format(formatYYYYMMDD2.parse(txtDate.getText().toString()))) <= Integer.parseInt(JsonIsNullCheck(object,"END_DT"))){
                            MedicineTakingItem medicineTakingItem = new MedicineTakingItem();
                            medicineTakingItem.setMedicineNo(JsonIntIsNullCheck(object,"MEDICINE_NO"));
                            medicineTakingItem.setFrequency(JsonIntIsNullCheck(object,"FREQUENCY"));
                            medicineTakingItem.setVolume(JsonIntIsNullCheck(object,"VOLUME"));
                            medicineTakingItem.setUnit(JsonIsNullCheck(object,"UNIT"));
                            medicineTakingItem.setStartDt(JsonIsNullCheck(object,"START_DT"));
                            medicineTakingItem.setEndDt(JsonIsNullCheck(object,"END_DT"));
                            medicineTakingItem.setAliveFlag(JsonIntIsNullCheck(object,"ALIVE_FLAG"));
                            medicineTakingItem.setEmergencyFlag("0");

                            //clsMedicineBean
                            medicineTakingItem.setMedicineKo(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"KO"));
                            medicineTakingItem.setMedicineTypeNo(JsonIntIsNullCheck(object.getJSONObject("clsMedicineBean"),"MEDICINE_TYPE_NO"));

                            medicineTakingItems.add(medicineTakingItem);
                            medicineTakingItems1.add(medicineTakingItem);

                            adapter.addItem(medicineTakingItem);

                        }
                    }
                    if (adapter.getCount() == 0){
                        linNoList.setVisibility(View.VISIBLE);
                        scrList.setVisibility(View.GONE);
                        btnDateSelect.setEnabled(false);
                        btnFinish.setEnabled(false);
                        btnDateSelect.setAlpha(0.5f);
                        btnFinish.setAlpha(0.5f);
                    }else{
                        linNoList.setVisibility(View.GONE);
                        scrList.setVisibility(View.VISIBLE);
                        btnDateSelect.setEnabled(true);
                        btnFinish.setEnabled(true);
                        btnDateSelect.setAlpha(1f);
                        btnFinish.setAlpha(1f);
                        listView.setAdapter(adapter);
                        setListViewHeightBasedOnChildren(listView);
                    }
                }else{
                    linNoList.setVisibility(View.VISIBLE);
                    scrList.setVisibility(View.GONE);
                    btnDateSelect.setEnabled(false);
                    btnFinish.setEnabled(false);
                    btnDateSelect.setAlpha(0.5f);
                    btnFinish.setAlpha(0.5f);
                }

            }catch (JSONException e){

            } catch (ParseException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }

    String response = "";

    public class InsertMedicineHomeFeedHistoryNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            Log.i(TAG,"registerDt : " + registerDt);

            RequestBody body = new FormBody.Builder().add("USER_NO", String.valueOf(Utils.userItem.getUserNo()))
                    .add("MEDICINE_NO",strings[0])
                    .add("FREQUENCY",strings[1])
                    .add("VOLUME",strings[2])
                    .add("UNIT",strings[3])
                    .add("EMERGENCY_FLAG",strings[4])
                    .add("START_DT",strings[5])
                    .add("END_DT",strings[6])
                    .add("KO",strings[7])
                    .add("NICKNAME", Utils.userItem.getNickname())
                    .add("GENDER",""+ Utils.userItem.getGender())
                    .add("BIRTH",""+ Utils.userItem.getBirth())
                    .add("CATEGORY","1")
                    .add("REGISTER_DT",registerDt)
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.insertMedicineHomeFeedHistoryList(), body);

                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,"insert medicine : " + s);

        }
    }

    @Override
    public void onBackPressed() {

        if (beforeIntent.hasExtra("medicineAlarm")){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            super.onBackPressed();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_medicine_record_back : {
                onBackPressed();
                break;
            }
            case R.id.txt_medicine_record_before : {
                calendar.add(Calendar.DAY_OF_MONTH,-1);
                txtDate.setText(Utils.formatYYYYMMDD2.format(calendar.getTime()));
                txtAfter.setTextColor(getResources().getColor(R.color.black));
                new SelectMedicineHomeFeedHistoryListNetWork().execute();
                try {
                    registerDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString())) + " " + formatHHMMSS.format(new Date(System.currentTimeMillis()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            }
            case R.id.txt_medicine_record_after : {
                if (calendar.get(Calendar.DAY_OF_MONTH) == currentCalendar.get(Calendar.DAY_OF_MONTH)){

                }else if (calendar.get(Calendar.DAY_OF_MONTH) + 1 == currentCalendar.get(Calendar.DAY_OF_MONTH)){
                    txtAfter.setTextColor(getResources().getColor(R.color.acacac));
                    calendar.add(Calendar.DAY_OF_MONTH,+1);
                    txtDate.setText(Utils.formatYYYYMMDD2.format(calendar.getTime()));
                    new SelectMedicineHomeFeedHistoryListNetWork().execute();
                    try {
                        registerDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString())) + " " + formatHHMMSS.format(new Date(System.currentTimeMillis()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    calendar.add(Calendar.DAY_OF_MONTH,+1);
                    txtDate.setText(Utils.formatYYYYMMDD2.format(calendar.getTime()));
                    new SelectMedicineHomeFeedHistoryListNetWork().execute();
                    try {
                        registerDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString())) + " " + formatHHMMSS.format(new Date(System.currentTimeMillis()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                break;
            }
            case R.id.btn_medicine_record_date_select : {
                DateTimePicker();
                break;
            }
            case R.id.btn_medicine_record_finish : {
                btnFinish.setClickable(false);
                if (medicineTakingItems1.size() > 0){
                    for (int i = 0; i < medicineTakingItems1.size(); i++){
                        if (medicineTakingItems1.get(i).getEmergencyFlag().equals("1") || medicineTakingItems1.get(i).getEmergencyFlag().equals("2")){
                            new InsertMedicineHomeFeedHistoryNetWork().execute(""+medicineTakingItems1.get(i).getMedicineNo(),""+medicineTakingItems1.get(i).getFrequency(),
                                    ""+medicineTakingItems1.get(i).getVolume(),medicineTakingItems1.get(i).getUnit(),medicineTakingItems1.get(i).getEmergencyFlag(),
                                    medicineTakingItems1.get(i).getStartDt(),medicineTakingItems1.get(i).getEndDt(),medicineTakingItems1.get(i).getMedicineKo());
                        }
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnFinish.setClickable(true);
                            setResult(RESULT_OK);
                            onBackPressed();
                        }
                    },500);

                }else{
                    btnFinish.setClickable(true);
                    onBackPressed();
                }
                break;
            }
        }
    }
}
