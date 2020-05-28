package com.kmw.soom2.StaticFunc.Activitys;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.R;
import com.kmw.soom2.StaticFunc.Activitys.Item.HistoryItems;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.formatYYYYMM;

public class StaticSymptomActivity extends AppCompatActivity implements View.OnClickListener {

    final String TAG = "StaticSymptomActivity";
    SimpleDateFormat dataFormatYYYYMM = new SimpleDateFormat("yyyy년 MM월");

    LinearLayout linEnvBtn, linBadBtn, linAllergyBtn;
    LinearLayout linLeftBtn,linRightBtn;
    public TextView txtCalendarTitle;
    Calendar currentCalendar;

    TextView btnBack;
    TextView txtSymptomCough, txtSymptomWheeze, txtSymptombreath, txtSymptomStuffy;
    PieChart pieChart;
    LinearLayout linSelectTimezone;
    TextView btnSelectTimeZone;

    TextView txtCause1, txtCause2, txtCause3, txtCause4, txtCause5, txtCause6, txtCause7, txtCause8, txtCause9, txtCause10, txtCause11, txtCause12;

    BarChart barChart;
//    CustomPopupBasic symptomPickerView;

    ArrayList<HistoryItems> coughItems, breathItems, wheezeItems, stuffyItems;

    Double[] pieValues = {0.0, 0.0, 0.0, 0.0};
    String[] barChartXAxisText = {"새벽", "오전", "오후", "저녁"};
    String[] barChartvaluesTitle = {"전체", "기침", "천명음", "호흡곤란", "가슴답답함"};


    float[] dawns = {0.0f, 0.0f, 0.0f, 0.0f};
    float[] mornings = {0.0f, 0.0f, 0.0f, 0.0f};
    float[] afternoons = {0.0f, 0.0f, 0.0f, 0.0f};
    float[] nights = {0.0f, 0.0f, 0.0f, 0.0f};

    int[] causes = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    String stCause = "";

    String response;

    int selectedIndex = 0;  // 0

    ArrayList<String> causeArr;

    RelativeLayout rllEnv, rllAllergy, rllBad;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_symptom);

        findViewByIds();

        callAllDatasReload();
    }

    private void findViewByIds() {
        rllAllergy = findViewById(R.id.rll_static_result_allergy);
        rllEnv = findViewById(R.id.rll_static_result_env);
        rllBad = findViewById(R.id.rll_static_result_bad);

        rllAllergy.setOnClickListener(this);
        rllEnv.setOnClickListener(this);
        rllBad.setOnClickListener(this);

        txtCalendarTitle = findViewById(R.id.txt_statics_symptom_title);
        btnBack = findViewById(R.id.txt_statics_detail_back);

        /// 증상 빈도
        txtSymptomStuffy = findViewById(R.id.symptom_static_result_frequency_stuffy_cnt);
        txtSymptomCough = findViewById(R.id.symptom_static_result_frequency_cough_cnt);
        txtSymptomWheeze = findViewById(R.id.symptom_static_result_frequency_wheeze_cnt);
        txtSymptombreath = findViewById(R.id.symptom_static_result_frequency_breath_cnt);
        pieChart = findViewById(R.id.symptom_static_result_pie_chart);


        /// 시간대별 증상
        linSelectTimezone = findViewById(R.id.lin_symptom_static_result_select_cause);
        barChart = findViewById(R.id.symptom_static_result_bar_chart);
        btnSelectTimeZone = findViewById(R.id.symptom_static_result_select_cause);

        currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(new Date(System.currentTimeMillis()));

        txtCalendarTitle.setText(formatYYYYMM.format(currentCalendar.getTime()));

        linLeftBtn = findViewById(R.id.lin_statics_symptom_left_btn);
        linRightBtn = findViewById(R.id.lin_statics_symptom_right_btn);

        txtCause1 = findViewById(R.id.symptom_static_cause_1);
        txtCause2 = findViewById(R.id.symptom_static_cause_2);
        txtCause3 = findViewById(R.id.symptom_static_cause_3);
        txtCause4 = findViewById(R.id.symptom_static_cause_4);
        txtCause5 = findViewById(R.id.symptom_static_cause_5);
        txtCause6 = findViewById(R.id.symptom_static_cause_6);
        txtCause7 = findViewById(R.id.symptom_static_cause_7);
        txtCause8 = findViewById(R.id.symptom_static_cause_8);
        txtCause9 = findViewById(R.id.symptom_static_cause_9);
        txtCause10 = findViewById(R.id.symptom_static_cause_10);
        txtCause11 = findViewById(R.id.symptom_static_cause_11);
        txtCause12 = findViewById(R.id.symptom_static_cause_12);

        linAllergyBtn = findViewById(R.id.lin_allergy_cause);
        linEnvBtn = findViewById(R.id.lin_env_cause);
        linBadBtn = findViewById(R.id.lin_bad_cause);

        linAllergyBtn.setOnClickListener(this);
        linEnvBtn.setOnClickListener(this);
        linBadBtn.setOnClickListener(this);

        btnBack.setOnClickListener(this);
        linLeftBtn.setOnClickListener(this);
        linRightBtn.setOnClickListener(this);
        linSelectTimezone.setOnClickListener(this);
    }
    void callAllDatasReload() {
        progressDialog = new ProgressDialog(StaticSymptomActivity.this);
        new SelectSymptomHistoryNetWork().execute();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_allergy_cause : {
                if (Utils.linkKeys != null) {
                    for (int i = 0; i < Utils.linkKeys.size(); i++) {
                        if (Utils.linkKeys.get(i).getTitle().equals("symptomreport2")) {
                            if (Utils.linkKeys.get(i).getLinkUrl() != null) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Utils.linkKeys.get(i).getLinkUrl()));
                                startActivity(intent);
                                break;
                            }
                        }
                    }
                }
                break;
            }
            case R.id.lin_env_cause : {
                if (Utils.linkKeys != null) {
                    for (int i = 0; i < Utils.linkKeys.size(); i++) {
                        if (Utils.linkKeys.get(i).getTitle().equals("symptomreport1")) {
                            if (Utils.linkKeys.get(i).getLinkUrl() != null) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Utils.linkKeys.get(i).getLinkUrl()));
                                startActivity(intent);
                                break;
                            }
                        }
                    }
                }
                break;
            }
            case R.id.lin_bad_cause : {
                if (Utils.linkKeys != null) {
                    for (int i = 0; i < Utils.linkKeys.size(); i++) {
                        if (Utils.linkKeys.get(i).getTitle().equals("symptomreport3")) {
                            if (Utils.linkKeys.get(i).getLinkUrl() != null) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Utils.linkKeys.get(i).getLinkUrl()));
                                startActivity(intent);
                                break;
                            }
                        }
                    }
                }
                break;
            }
            case R.id.txt_statics_detail_back : {
                onBackPressed();
                break;
            }
            case R.id.lin_statics_symptom_left_btn : {
                currentCalendar.add(Calendar.MONTH, -1);
                txtCalendarTitle.setText(dataFormatYYYYMM.format(currentCalendar.getTime()));

                callAllDatasReload();
                break;
            }
            case R.id.lin_statics_symptom_right_btn : {
                currentCalendar.add(Calendar.MONTH, 1);
                txtCalendarTitle.setText(dataFormatYYYYMM.format(currentCalendar.getTime()));

                callAllDatasReload();
                break;
            }
            case R.id.lin_symptom_static_result_select_cause: {
                StaticPicker("시간대별 증상");
                break;
            }
            case R.id.rll_static_result_allergy: {
                for (int i = 0 ; i < Utils.linkKeys.size() ; i++) {
                    if (Utils.linkKeys.get(i).getTitle().equals("symptomreport1")) {
                        if (Utils.linkKeys.get(i).getLinkUrl() != null) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Utils.linkKeys.get(i).getLinkUrl())); startActivity(intent);
                            break;
                        }
                    }
                }
                break;
            }
            case R.id.rll_static_result_env: {
                for (int i = 0 ; i < Utils.linkKeys.size() ; i++) {
                    if (Utils.linkKeys.get(i).getTitle().equals("symptomreport2")) {
                        if (Utils.linkKeys.get(i).getLinkUrl() != null) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Utils.linkKeys.get(i).getLinkUrl())); startActivity(intent);
                            break;
                        }
                    }
                }
                break;
            }
            case R.id.rll_static_result_bad: {
                for (int i = 0 ; i < Utils.linkKeys.size() ; i++) {
                    if (Utils.linkKeys.get(i).getTitle().equals("symptomreport3")) {
                        if (Utils.linkKeys.get(i).getLinkUrl() != null) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Utils.linkKeys.get(i).getLinkUrl())); startActivity(intent);
                            break;
                        }
                    }
                }
                break;
            }
        }
    }

    void isNullHistoryData(int index, Double value){
//        pieValues[index] = value;
//        dawns[index] = value.floatValue();
//        mornings[index] = value.floatValue();
//        afternoons[index] = value.floatValue();
//        nights[index] = value.floatValue();
//
//        pieValues[0] = Double.valueOf(coughItems.size());
//        txtSymptomCough.setText(coughItems.size()+"회");
//        pieValues[2] = Double.valueOf(wheezeItems.size());
//        txtSymptomWheeze.setText(wheezeItems.size()+"회");
//        pieValues[1] = Double.valueOf(breathItems.size());
//        txtSymptombreath.setText(breathItems.size()+"회");
//        pieValues[3] = Double.valueOf(stuffyItems.size());
//        txtSymptomStuffy.setText(stuffyItems.size()+"회");

        stCause = "";

        coughItems = new ArrayList<>();
        wheezeItems = new ArrayList<>();
        breathItems = new ArrayList<>();
        stuffyItems = new ArrayList<>();

        pieValues = new Double[]{0.0, 0.0, 0.0, 0.0};
        dawns = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
        mornings = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
        afternoons = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
        nights = new float[]{0.0f, 0.0f, 0.0f, 0.0f};


        txtSymptomCough.setText("0회");
        txtSymptomWheeze.setText("0회");
        txtSymptombreath.setText("0회");
        txtSymptomStuffy.setText("0회");

        setupBarChart();
        setupPieChart();
        setCausesTexts();
    }



    public class SelectSymptomHistoryNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {

            String month = "";
            String year = "";

            if (currentCalendar.get(Calendar.MONTH) < 9) {
                month = "0" + (currentCalendar.get(Calendar.MONTH) + 1);
            }else {
                month = "" + (currentCalendar.get(Calendar.MONTH) + 1);
            }

            year = "" + currentCalendar.get(Calendar.YEAR);

            RequestBody body = new FormBody.Builder().add("MONTH", month).add("YEAR", year).add("USER_NO", ""+Utils.userItem.getUserNo()).build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectHomeFeedList(), body);

                Log.d("Response", response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            coughItems = new ArrayList<>();
            wheezeItems = new ArrayList<>();
            breathItems = new ArrayList<>();
            stuffyItems = new ArrayList<>();

            causeArr = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    if (JsonIntIsNullCheck(object,"ALIVE_FLAG") == 1) {
                        HistoryItems historyItem = new HistoryItems();

                        if (JsonIntIsNullCheck(object, "CATEGORY") == 11) {
                            historyItem.setUserHistoryNo(JsonIntIsNullCheck(object, "USER_HISTORY_NO"));
                            historyItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                            historyItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));
                            historyItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));

                            historyItem.setMedicineNo(JsonIntIsNullCheck(object, "MEDICINE_NO"));
                            historyItem.setFrequency(JsonIntIsNullCheck(object, "FREQUENCY"));
                            historyItem.setVolume(JsonIsNullCheck(object, "VOLUME"));
                            historyItem.setKo(JsonIsNullCheck(object, "KO"));
                            historyItem.setEmergencyFlag(JsonIntIsNullCheck(object, "EMERGENCY_FLAG"));
                            historyItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                            historyItem.setCause(JsonIsNullCheck(object, "CAUSE"));

                            coughItems.add(historyItem);

                            int hour = Integer.parseInt(historyItem.getRegisterDt().substring(11, 13));

                            if (hour >= 0 && hour < 6) {
                                dawns[0] = dawns[0] + 1;
                            }else if (hour >= 6 && hour < 12) {
                                mornings[0] = mornings[0] + 1;
                            }else if (hour >= 12 && hour < 18) {
                                afternoons[0] = afternoons[0] + 1;
                            }else {
                                nights[0] = nights[0] + 1;
                            }
                            if (historyItem.getCause().length() > 0) {
                                if (!stCause.startsWith(",")) {
//                                    if (stCause.endsWith(",")) {
//                                        stCause += historyItem.getCause();
//                                    }else {
//                                        stCause += "," + historyItem.getCause();
//                                    }
                                    if (stCause.length() > 0) {
                                        stCause += "," + historyItem.getCause();
                                    }else {
                                        stCause += historyItem.getCause();
                                    }
                                } else {
                                    stCause += "," + historyItem.getCause();
                                }
//                                causeArr.addAll(Arrays.asList(stCause.split(",")));
                            }
                        }else if (JsonIntIsNullCheck(object, "CATEGORY") == 13) {
                            historyItem.setUserHistoryNo(JsonIntIsNullCheck(object, "USER_HISTORY_NO"));
                            historyItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                            historyItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));
                            historyItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));

                            historyItem.setMedicineNo(JsonIntIsNullCheck(object, "MEDICINE_NO"));
                            historyItem.setFrequency(JsonIntIsNullCheck(object, "FREQUENCY"));
                            historyItem.setVolume(JsonIsNullCheck(object, "VOLUME"));
                            historyItem.setKo(JsonIsNullCheck(object, "KO"));
                            historyItem.setEmergencyFlag(JsonIntIsNullCheck(object, "EMERGENCY_FLAG"));
                            historyItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                            historyItem.setCause(JsonIsNullCheck(object, "CAUSE"));

                            wheezeItems.add(historyItem);

                            int hour = Integer.parseInt(historyItem.getRegisterDt().substring(11, 13));

                            if (hour >= 0 && hour < 6) {
                                dawns[2] = dawns[2] + 1;
                            }else if (hour >= 6 && hour < 12) {
                                mornings[2] = mornings[2] + 1;
                            }else if (hour >= 12 && hour < 18) {
                                afternoons[2] = afternoons[2] + 1;
                            }else {
                                nights[2] = nights[2] + 1;
                            }
                            if (historyItem.getCause().length() > 0) {
                                if (!stCause.startsWith(",")) {
                                    if (stCause.endsWith(",")) {
                                        stCause += historyItem.getCause();
                                    }else {
                                        stCause += "," + historyItem.getCause();
                                    }
                                } else {
                                    stCause += "," + historyItem.getCause();
                                }
//                                causeArr.addAll(Arrays.asList(stCause.split(",")));
                            }
                        }else if (JsonIntIsNullCheck(object, "CATEGORY") == 12) {
                            historyItem.setUserHistoryNo(JsonIntIsNullCheck(object, "USER_HISTORY_NO"));
                            historyItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                            historyItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));
                            historyItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));

                            historyItem.setMedicineNo(JsonIntIsNullCheck(object, "MEDICINE_NO"));
                            historyItem.setFrequency(JsonIntIsNullCheck(object, "FREQUENCY"));
                            historyItem.setVolume(JsonIsNullCheck(object, "VOLUME"));
                            historyItem.setKo(JsonIsNullCheck(object, "KO"));
                            historyItem.setEmergencyFlag(JsonIntIsNullCheck(object, "EMERGENCY_FLAG"));
                            historyItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                            historyItem.setCause(JsonIsNullCheck(object, "CAUSE"));

                            breathItems.add(historyItem);

                            int hour = Integer.parseInt(historyItem.getRegisterDt().substring(11, 13));

                            if (hour >= 0 && hour < 6) {
                                dawns[1] = dawns[1] + 1;
                            }else if (hour >= 6 && hour < 12) {
                                mornings[1] = mornings[1] + 1;
                            }else if (hour >= 12 && hour < 18) {
                                afternoons[1] = afternoons[1] + 1;
                            }else {
                                nights[1] = nights[1] + 1;
                            }
                            if (historyItem.getCause().length() > 0) {
                                if (!stCause.startsWith(",")) {
//                                    if (stCause.endsWith(",")) {
//                                        stCause += historyItem.getCause();
//                                    }else {
//                                        stCause += "," + historyItem.getCause();
//                                    }
                                    if (stCause.length() > 0) {
                                        stCause += "," + historyItem.getCause();
                                    }else {
                                        stCause += historyItem.getCause();
                                    }
                                } else {
                                    stCause += "," + historyItem.getCause();
                                }
//                                causeArr.addAll(Arrays.asList(stCause.split(",")));
                            }
                        }else if (JsonIntIsNullCheck(object, "CATEGORY") == 14) {
                            historyItem.setUserHistoryNo(JsonIntIsNullCheck(object, "USER_HISTORY_NO"));
                            historyItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                            historyItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));
                            historyItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));

                            historyItem.setMedicineNo(JsonIntIsNullCheck(object, "MEDICINE_NO"));
                            historyItem.setFrequency(JsonIntIsNullCheck(object, "FREQUENCY"));
                            historyItem.setVolume(JsonIsNullCheck(object, "VOLUME"));
                            historyItem.setKo(JsonIsNullCheck(object, "KO"));
                            historyItem.setEmergencyFlag(JsonIntIsNullCheck(object, "EMERGENCY_FLAG"));
                            historyItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                            historyItem.setCause(JsonIsNullCheck(object, "CAUSE"));

                            stuffyItems.add(historyItem);

                            int hour = Integer.parseInt(historyItem.getRegisterDt().substring(11, 13));

                            if (hour >= 0 && hour < 6) {
                                dawns[3] = dawns[3] + 1;
                            }else if (hour >= 6 && hour < 12) {
                                mornings[3] = mornings[3] + 1;
                            }else if (hour >= 12 && hour < 18) {
                                afternoons[3] = afternoons[3] + 1;
                            }else {
                                nights[3] = nights[3] + 1;
                            }
                            if (historyItem.getCause().length() > 0) {
                                if (!stCause.startsWith(",")) {
//                                    if (stCause.endsWith(",")) {
//                                        stCause += historyItem.getCause();
//                                    }else {
//                                        stCause += "," + historyItem.getCause();
//                                    }
                                    if (stCause.length() > 0) {
                                        stCause += "," + historyItem.getCause();
                                    }else {
                                        stCause += historyItem.getCause();
                                    }
                                } else {
                                    stCause += "," + historyItem.getCause();
                                }
//                                causeArr.addAll(Arrays.asList(stCause.split(",")));
                            }
                        }
                    }
                }

                pieValues[0] = Double.valueOf(coughItems.size());
                txtSymptomCough.setText(coughItems.size()+"회");
                pieValues[1] = Double.valueOf(wheezeItems.size());
                txtSymptomWheeze.setText(wheezeItems.size()+"회");
                pieValues[2] = Double.valueOf(breathItems.size());
                txtSymptombreath.setText(breathItems.size()+"회");
                pieValues[3] = Double.valueOf(stuffyItems.size());
                txtSymptomStuffy.setText(stuffyItems.size()+"회");

                Log.i(TAG,stCause);

                setupPieChart();
                setupBarChart();
                setCausesTexts();
            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
//                isNullMedicineListData();

                isNullHistoryData(0, 0.0);
            }
            progressDialog.dismiss();
        }
    }
    void setupBarChart() {

        ArrayList barEndtry = new ArrayList();
        int[] colors = new int[]{getColor(R.color.static_symptom_cough), getColor(R.color.static_symptom_wheeze), getColor(R.color.static_symptom_breath), getColor(R.color.static_symptom_stuffy)};;

        if (selectedIndex == 0) {
            barEndtry.add(new BarEntry(0, dawns));
            barEndtry.add(new BarEntry(1, mornings));
            barEndtry.add(new BarEntry(2, afternoons));
            barEndtry.add(new BarEntry(3, nights));
        }else {
            barEndtry.add(new BarEntry(0, dawns[selectedIndex-1]));
            barEndtry.add(new BarEntry(1, mornings[selectedIndex-1]));
            barEndtry.add(new BarEntry(2, afternoons[selectedIndex-1]));
            barEndtry.add(new BarEntry(3, nights[selectedIndex-1]));
        }

        Log.i(TAG, " dawns : " + dawns.toString());

        BarDataSet dataSet = new BarDataSet(barEndtry, "");
        if (selectedIndex == 0 ) {
            dataSet.setColors(colors);
        }else {
            dataSet.setColor(colors[selectedIndex - 1]);
        }
        BarData barData = new BarData(dataSet);

        Legend l = barChart.getLegend();
        l.setEnabled(false);
        barData.setBarWidth(0.5f);

        barChart.getXAxis().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        YAxis rightYAxis = barChart.getAxisRight();
        rightYAxis.setEnabled(false);
        barChart.setData(barData);

        String[] labels = {"새벽", "오전", "오후", "저녁"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
//        xAxis.setCenterAxisLabels(true);
        xAxis.setEnabled(true);
        Typeface tf = ResourcesCompat.getFont(this, R.font.notosanscjkkr_medium);
        xAxis.setTypeface(tf);
        xAxis.setTextSize(14f);
        xAxis.setTextColor(Color.parseColor("#7e7e7e"));
        xAxis.setYOffset(5);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.setExtraBottomOffset(20);
        barChart.setScaleEnabled(false);
        barChart.setPinchZoom(false);
        barChart.notifyDataSetChanged();
        barChart.invalidate();
    }

    void setupPieChart() {
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);


        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);


        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        yValues.add(new PieEntry(pieValues[0].floatValue()));
        yValues.add(new PieEntry(pieValues[1].floatValue()));
        yValues.add(new PieEntry(pieValues[2].floatValue()));
        yValues.add(new PieEntry(pieValues[3].floatValue()));


//        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues,"");
        dataSet.setSliceSpace(3f);
//        dataSet.setSelectionShift(5f);
        dataSet.setColors(new int[]{getColor(R.color.static_symptom_cough), getColor(R.color.static_symptom_wheeze), getColor(R.color.static_symptom_breath), getColor(R.color.static_symptom_stuffy)});

        Legend l = pieChart.getLegend();
        l.setEnabled(false);

        PieData data = new PieData((dataSet));

        pieChart.setData(data);
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
    }
    public void StaticPicker(String title){
        View dateTimeView = getLayoutInflater().inflate(R.layout.dialog_number_picker,null);
        TextView txtTitle = (TextView)dateTimeView.findViewById(R.id.txt_dialog_number_picker_title);
        Button btnCancel = (Button)dateTimeView.findViewById(R.id.btn_dialog_number_picker_cancel);
        Button btnDone = (Button)dateTimeView.findViewById(R.id.btn_dialog_number_picker_done);
        final NumberPicker numberPicker = (NumberPicker)dateTimeView.findViewById(R.id.number_picker);
        final Dialog dateTimeDialog = new Dialog(StaticSymptomActivity.this);

        dateTimeDialog.setContentView(dateTimeView);
        dateTimeDialog.show();

        numberPicker.setWrapSelectorWheel(false);

        txtTitle.setText(title);

        numberPicker.setMinValue(0);

        numberPicker.setMaxValue(barChartvaluesTitle.length-1);
        numberPicker.setDisplayedValues(barChartvaluesTitle);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idx = numberPicker.getValue();

                btnSelectTimeZone.setText(barChartvaluesTitle[idx]);
                selectedIndex = idx;

                setupBarChart();
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
    void setCausesTexts() {
        causes = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        if (stCause.length() == 0) {

            txtCause1.setText("" + causes[0] + "회");
            txtCause2.setText("" + causes[1] + "회");
            txtCause3.setText("" + causes[2] + "회");
            txtCause4.setText("" + causes[3] + "회");
            txtCause5.setText("" + causes[4] + "회");
            txtCause6.setText("" + causes[5] + "회");
            txtCause7.setText("" + causes[6] + "회");
            txtCause8.setText("" + causes[7] + "회");
            txtCause9.setText("" + causes[8] + "회");
            txtCause10.setText("" + causes[9] + "회");
            txtCause11.setText("" + causes[10] + "회");
            txtCause12.setText("" + causes[11] + "회");

            return;
        }

        for (int i = 0; i < stCause.split(",").length; i++) {
            Log.d(TAG, " stCause " + i + " : " + stCause);
            if (stCause.startsWith(",")) {
                stCause = stCause.substring(1, stCause.length() - 1);
            }else if (stCause.endsWith(",")) {
                stCause = stCause.substring(0, stCause.length() - 2);
            }
            Log.d(TAG,"i : " + i + ", stCause : " + stCause);
            if (stCause.contains(",")) {
                causes[Integer.parseInt(stCause.split(",")[i])] += 1;
            }else {
                if (!stCause.equals("")) {
                    causes[Integer.parseInt(stCause)] += 1;
                }
            }
        }

        txtCause1.setText("" + causes[0] + "회");
        txtCause2.setText("" + causes[1] + "회");
        txtCause3.setText("" + causes[2] + "회");
        txtCause4.setText("" + causes[3] + "회");
        txtCause5.setText("" + causes[4] + "회");
        txtCause6.setText("" + causes[5] + "회");
        txtCause7.setText("" + causes[6] + "회");
        txtCause8.setText("" + causes[7] + "회");
        txtCause9.setText("" + causes[8] + "회");
        txtCause10.setText("" + causes[9] + "회");
        txtCause11.setText("" + causes[10] + "회");
        txtCause12.setText("" + causes[11] + "회");
    }
}