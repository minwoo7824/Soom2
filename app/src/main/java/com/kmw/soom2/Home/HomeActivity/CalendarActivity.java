package com.kmw.soom2.Home.HomeActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.HomeActivity.SymptomActivitys.AsthmaControlActivity;
import com.kmw.soom2.Home.HomeActivity.SymptomActivitys.BreathRecordActivity;
import com.kmw.soom2.Home.HomeActivity.SymptomActivitys.MedicinRecordActivity;
import com.kmw.soom2.Home.HomeActivity.SymptomActivitys.MemoActivity;
import com.kmw.soom2.Home.HomeActivity.SymptomActivitys.SymptomRecord;
import com.kmw.soom2.Home.HomeAdapter.CalendarRecyclerViewAdapter;
import com.kmw.soom2.Home.HomeItem.EtcItem;
import com.kmw.soom2.Home.HomeItem.RecyclerViewItemList;
import com.kmw.soom2.Home.HomeItem.RecyclerViewMainStickyItemDecoration;
import com.kmw.soom2.R;
import com.kmw.soom2.StaticFunc.Activitys.Item.HistoryItems;
import com.kmw.soom2.Views.CalendarView;
import com.kmw.soom2.Views.RecordCalendarView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.formatDD;
import static com.kmw.soom2.Common.Utils.formatHHMM;
import static com.kmw.soom2.Common.Utils.formatYYYYMM;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD2;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "CalendarActivity";

    TextView txtBack;
    LinearLayout linLeftBtn, linRightBtn;
    LinearLayout linListParent;
    TextView txtRightBtn;
    public static TextView txtMonthText;
    RecordCalendarView recordCalendarView;
    Button btnRecord;

    RecyclerView recyclerView;
    CalendarRecyclerViewAdapter adapter = null;
    ArrayList<RecyclerViewItemList> mList = new ArrayList<>();
    ProgressDialog progressDialog;
    Date intentDate = null;

    String response;
    ArrayList<HistoryItems> hisItems = new ArrayList<>();

    Calendar currentCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        currentCalendar = Calendar.getInstance();

        FindViewById();

    }

    void FindViewById() {
        progressDialog = new ProgressDialog(this);
        txtBack = (TextView) findViewById(R.id.txt_calendar_record_back);
        linLeftBtn = (LinearLayout) findViewById(R.id.lin_calendar_record_left_btn);
        linRightBtn = (LinearLayout) findViewById(R.id.lin_calendar_record_right_btn);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_fragment_home);
        txtMonthText = (TextView) findViewById(R.id.txt_calendar_record_title);
        recordCalendarView = (RecordCalendarView) findViewById(R.id.calendar_record);
        btnRecord = (Button) findViewById(R.id.btn_calendar_record);
        txtRightBtn = (TextView) findViewById(R.id.txt_calendar_record_right_btn);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));

        intentDate = calendar.getTime();

        txtMonthText.setText(formatYYYYMM.format(calendar.getTime()));

        btnRecord.setText(formatDD.format(calendar.getTime()) + "에 기록하기");

        recordCalendarView.setRobotoCalendarListener(new CalendarView.RobotoCalendarListener() {
            @Override
            public void onDayClick(Calendar daySelectedCalendar) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(System.currentTimeMillis()));
                if (daySelectedCalendar.before(calendar)) {

                }
                if (!(daySelectedCalendar.getTime().getTime() > System.currentTimeMillis())) {
                    btnRecord.setText(formatDD.format(daySelectedCalendar.getTime()) + "에 기록하기");
                    intentDate = daySelectedCalendar.getTime();
                }

                recyclerView.setAdapter(null);

                ArrayList<RecyclerViewItemList> items = new ArrayList<>();

                items.add(new RecyclerViewItemList(formatYYYYMMDD.format(daySelectedCalendar.getTime()), 0, new TreeMap<>()));

                Log.d(TAG, "TITLE : " + items.get(0));

                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getEtcItem() != null) {
                        if (mList.get(i).getEtcItem().getRegisterDt() != null) {
                            if (mList.get(i).getEtcItem().getRegisterDt().contains(formatYYYYMMDD.format(daySelectedCalendar.getTime()))) {
                                items.add(mList.get(i));
                            }
                        }
                    }
                }

                TreeMap<String, ArrayList<HistoryItems>> tempHisItems = new TreeMap<>();
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getHistoryItemList() != null && mList.get(i).getHistoryItemList().size() > 0) {
                        if (mList.get(i).getTitle() != null) {
                            if (mList.get(i).getTitle().contains(formatYYYYMMDD.format(daySelectedCalendar.getTime()).substring(0, 10))) {
                                List<String> tempList = mList.get(i).getHistoryItemList().keySet().stream().filter(s0 -> s0.contains(formatYYYYMMDD.format(daySelectedCalendar.getTime()).substring(0, 10))).collect(Collectors.toList());

                                TreeMap<String, ArrayList<HistoryItems>> savedItem = new TreeMap<>();
                                for (int j = 0; j < tempList.size(); j++) {
                                    savedItem.put(tempList.get(j), mList.get(i).getHistoryItemList().get(tempList.get(j)));
                                }
                                tempHisItems = savedItem;
                            }
                        }
                    }
                }

                if (items.get(0) != null) {
                    items.get(0).setHistoryItemList(tempHisItems);
                }
                if (items.size() == 1) {        // 헤더 밖에 없는 경우 array를 초기화 해줌
                    items = new ArrayList<>();
                }
                adapter = null;
                adapter = new CalendarRecyclerViewAdapter(CalendarActivity.this, items);
                recyclerView.removeAllViewsInLayout();
                recyclerView.addItemDecoration(new RecyclerViewMainStickyItemDecoration());

                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onDayLongClick(Calendar daySelectedCalendar) {

            }

            @Override
            public void onRightButtonClick(Calendar daySelectedCalendar) {

                recyclerView.setAdapter(null);
                hisItems = new ArrayList<>();
                new SelectHomeFeedHistoryListNetWork().execute();
            }

            @Override
            public void onLeftButtonClick(Calendar daySelectedCalendar) {
                txtRightBtn.setTextColor(getResources().getColor(R.color.black));
                recyclerView.setAdapter(null);
                hisItems = new ArrayList<>();
                new SelectHomeFeedHistoryListNetWork().execute();
            }
        });

        txtBack.setOnClickListener(this);
        btnRecord.setOnClickListener(this);
        linLeftBtn.setOnClickListener(this);
        linRightBtn.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        hisItems = new ArrayList<>();
        new SelectHomeFeedHistoryListNetWork().execute();
        adapter = new CalendarRecyclerViewAdapter(this, mList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new RecyclerViewMainStickyItemDecoration());
        recyclerView.setAdapter(adapter);

        Log.i(TAG, "onResumeCalendar");
    }

    private void commentPopup() {

        final Dialog dialog = new BottomSheetDialog(this);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.view_calendar_record_list, null);

        Button btnMedicine = (Button) layout.findViewById(R.id.btn_calendar_record_symptom_list_medicine);
        Button btnSymptom = (Button) layout.findViewById(R.id.btn_calendar_record_symptom_list_symptom);
        Button btnBreath = (Button) layout.findViewById(R.id.btn_calendar_record_symptom_list_breath);
        Button btnInspection = (Button) layout.findViewById(R.id.btn_calendar_record_symptom_list_inspection);
        Button btnDust = (Button) layout.findViewById(R.id.btn_calendar_record_symptom_list_dust);
        Button btnMemo = (Button) layout.findViewById(R.id.btn_calendar_record_symptom_list_memo);

        dialog.setContentView(layout);

        dialog.show();

        btnMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(CalendarActivity.this, MedicinRecordActivity.class);
                i.putExtra("date", formatYYYYMMDD2.format(intentDate));
                i.putExtra("time", formatHHMM.format(intentDate));
                startActivity(i);
            }
        });

        btnSymptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(CalendarActivity.this, SymptomRecord.class);
                i.putExtra("date", formatYYYYMMDD2.format(intentDate));
                i.putExtra("time", formatHHMM.format(intentDate));
                startActivity(i);
            }
        });

        btnBreath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(CalendarActivity.this, BreathRecordActivity.class);
                i.putExtra("date", formatYYYYMMDD2.format(intentDate));
                i.putExtra("time", formatHHMM.format(intentDate));
                startActivity(i);
            }
        });

        btnInspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CalendarActivity.this, AsthmaControlActivity.class);
                i.putExtra("date", formatYYYYMMDD2.format(intentDate));
                i.putExtra("time", formatHHMM.format(intentDate));
                startActivity(i);
                dialog.dismiss();
            }
        });

        btnDust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CalendarActivity.this, DustRecordActivity.class);
                i.putExtra("date", formatYYYYMMDD2.format(intentDate));
                i.putExtra("time", formatHHMM.format(intentDate));
                startActivity(i);
                dialog.dismiss();
            }
        });

        btnMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(CalendarActivity.this, MemoActivity.class);
                i.putExtra("date", formatYYYYMMDD2.format(intentDate));
                i.putExtra("time", formatHHMM.format(intentDate));
                startActivity(i);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.txt_calendar_record_back: {
                onBackPressed();
                break;
            }
            case R.id.lin_calendar_record_left_btn: {
                txtRightBtn.setTextColor(getResources().getColor(R.color.black));
                recyclerView.addItemDecoration(new RecyclerViewMainStickyItemDecoration());
                btnRecord.setText("기록하기");
                recordCalendarView.leftClick();

                break;
            }
            case R.id.lin_calendar_record_right_btn: {
                if (recordCalendarView.currentCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)) {

                } else if (recordCalendarView.currentCalendar.get(Calendar.MONTH) + 1 == currentCalendar.get(Calendar.MONTH)) {
                    Calendar calendar = Calendar.getInstance();
                    txtRightBtn.setTextColor(getResources().getColor(R.color.acacac));
                    recyclerView.addItemDecoration(new RecyclerViewMainStickyItemDecoration());
                    btnRecord.setText(formatDD.format(calendar.getTime()) + "에 기록하기");
                    recordCalendarView.rightClick();
                } else {
                    recyclerView.addItemDecoration(new RecyclerViewMainStickyItemDecoration());
                    btnRecord.setText("기록하기");
                    recordCalendarView.rightClick();
                }
                break;
            }
            case R.id.btn_calendar_record: {
                if(!btnRecord.getText().toString().equals("기록하기")) {
                    commentPopup();
                }
                break;
            }
        }
    }

    public class SelectHomeFeedHistoryListNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            Log.i(TAG, "YEAR : " + String.valueOf(recordCalendarView.currentCalendar.get(Calendar.YEAR)) + " month : " + String.valueOf((recordCalendarView.currentCalendar.get(Calendar.MONTH) + 1)));
            RequestBody body = new FormBody.Builder().add("USER_NO", "" + Utils.userItem.getUserNo())
                    .add("YEAR", String.valueOf(recordCalendarView.currentCalendar.get(Calendar.YEAR)))
                    .add("MONTH", String.valueOf((recordCalendarView.currentCalendar.get(Calendar.MONTH) + 1))).build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectHomeFeedList(), body);
//                logLargeString(response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                mList = new ArrayList<>();

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");


                ArrayList<String> registerDtList = new ArrayList<>();
                ArrayList<ArrayList<String>> koList = new ArrayList<>();

                ArrayList<EtcItem> etcItemArrayList = new ArrayList<>();

                int size = jsonArray.length();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {
                        HistoryItems hisItem = new HistoryItems();

                        hisItem.setUserHistoryNo(JsonIntIsNullCheck(object, "USER_HISTORY_NO"));
                        hisItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                        hisItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));
                        hisItem.setNickname(JsonIsNullCheck(object, "NICKNAME"));
                        hisItem.setGender(JsonIntIsNullCheck(object, "GENDER"));
                        hisItem.setBirth(JsonIntIsNullCheck(object, "BIRTH"));
                        hisItem.setAge(JsonIntIsNullCheck(object, "AGE"));
                        hisItem.setCreateDt(JsonIsNullCheck(object, "CREATE_DT"));
                        hisItem.setUpdateDt(JsonIsNullCheck(object, "UPDATE_DT"));
                        hisItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));
                        hisItem.setAliveFlag(JsonIntIsNullCheck(object, "ALIVE_FLAG"));

                        /// 복약 관련
                        hisItem.setMedicineNo(JsonIntIsNullCheck(object, "MEDICINE_NO"));
                        hisItem.setFrequency(JsonIntIsNullCheck(object, "FREQUENCY"));
                        hisItem.setVolume(JsonIsNullCheck(object, "VOLUME"));
                        hisItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                        hisItem.setEmergencyFlag(JsonIntIsNullCheck(object, "EMERGENCY_FLAG"));
                        hisItem.setStartDt(JsonIsNullCheck(object, "START_DT"));
                        hisItem.setEndDt(JsonIsNullCheck(object, "END_DT"));

                        if (JsonIntIsNullCheck(object, "EMERGENCY_FLAG") != 1) {
                            hisItem.setKo(JsonIsNullCheck(object, "KO") + "[응급]");
                        } else {
                            hisItem.setKo(JsonIsNullCheck(object, "KO"));
                        }

                        /// 증상
                        hisItem.setCause(JsonIsNullCheck(object, "CAUSE"));
                        /// 메모
                        hisItem.setMemo(JsonIsNullCheck(object, "MEMO"));
                        /// 미세먼지
                        hisItem.setLatitude(JsonIsNullCheck(object, "LATITUDE"));
                        hisItem.setLongitute(JsonIsNullCheck(object, "LONGITUDE"));
                        hisItem.setLocation(JsonIsNullCheck(object, "LOCATION"));
                        hisItem.setDust(JsonIntIsNullCheck(object, "DUST"));
                        hisItem.setDustState(JsonIntIsNullCheck(object, "DUST_STATE"));
                        hisItem.setUltraDust(JsonIntIsNullCheck(object, "ULTRA_DUST"));
                        hisItem.setUltraDustState(JsonIntIsNullCheck(object, "ULTRA_DUST_STATE"));
                        /// PEF
                        hisItem.setPefScore(JsonIntIsNullCheck(object, "PEF_SCORE"));
                        hisItem.setInspiratorFlag(JsonIntIsNullCheck(object, "INSPIRATOR_FLAG"));
                        /// ACT
                        hisItem.setActScore(JsonIntIsNullCheck(object, "ACT_SCORE"));
                        hisItem.setActType(JsonIntIsNullCheck(object, "ACT_TYPE"));
                        hisItem.setActState(JsonIntIsNullCheck(object, "ACT_STATE"));
                        hisItem.setActSelected(JsonIsNullCheck(object, "ACT_SELECTED"));

                        hisItems.add(hisItem);
                    }
                }
                HashMap<String, ArrayList<HistoryItems>> map = setupHistoryData(hisItems);

                TreeMap<String, ArrayList<HistoryItems>> treeMap = new TreeMap<>(map);

                TreeMap<String, TreeMap<String, ArrayList<HistoryItems>>> finalMap = new TreeMap<>();

                for (Map.Entry<String, ArrayList<HistoryItems>> entry : treeMap.descendingMap().entrySet()) {

                    String key = entry.getKey();
                    ArrayList<HistoryItems> value = entry.getValue();

                    TreeMap<String, ArrayList<HistoryItems>> subMapItems = new TreeMap<>(setupHistoryDataWithRegiDt(value));

                    Log.d(TAG, "subMapItemssubMapItems : " + subMapItems);
                    finalMap.put(key, subMapItems);
                }

                for (int i = 0; i < size; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {

                        ArrayList<String> koChildList = new ArrayList<>();

                        EtcItem etcItem = new EtcItem();

//                        if (filterTextList.contains(JsonIsNullCheck(object,"CATEGORY"))){
                        if (JsonIntIsNullCheck(object, "CATEGORY") == 1) {

                            if (registerDtList.contains(JsonIsNullCheck(object, "REGISTER_DT"))) {
                                if (JsonIntIsNullCheck(object, "EMERGENCY_FLAG") == 1) {
                                    koList.get(registerDtList.indexOf(JsonIsNullCheck(object, "REGISTER_DT"))).add(JsonIsNullCheck(object, "KO"));
                                } else {
                                    koList.get(registerDtList.indexOf(JsonIsNullCheck(object, "REGISTER_DT"))).add(JsonIsNullCheck(object, "KO") + "[응급]");
                                }
                            } else {
                                registerDtList.add(JsonIsNullCheck(object, "REGISTER_DT"));
                                if (JsonIntIsNullCheck(object, "EMERGENCY_FLAG") == 1) {
                                    koChildList.add(JsonIsNullCheck(object, "KO"));
                                } else {
                                    koChildList.add(JsonIsNullCheck(object, "KO") + "[응급]");
                                }
                                koList.add(koChildList);

                                etcItem.setActScore(JsonIntIsNullCheck(object, "ACT_SCORE"));
                                etcItem.setActSelected(JsonIsNullCheck(object, "ACT_SELECTED"));
                                etcItem.setActState(JsonIntIsNullCheck(object, "ACT_STATE"));
                                etcItem.setActType(JsonIntIsNullCheck(object, "ACT_TYPE"));
                                etcItem.setCause(JsonIsNullCheck(object, "CAUSE"));
                                etcItem.setDust(JsonIntIsNullCheck(object, "DUST"));
                                etcItem.setDustStatus(JsonIntIsNullCheck(object, "DUST_STATE"));
                                etcItem.setUltraDust(JsonIntIsNullCheck(object, "ULTRA_DUST"));
                                etcItem.setUltraDustStatus(JsonIntIsNullCheck(object, "ULTRA_DUST_STATE"));
                                etcItem.setEmergencyFlag(JsonIntIsNullCheck(object, "EMERGENCY_FLAG"));
                                etcItem.setEndDt(JsonIsNullCheck(object, "END_DT"));
                                etcItem.setFrequency(JsonIntIsNullCheck(object, "FREQUENCY"));
                                etcItem.setImageFile(JsonIsNullCheck(object, "IMAGE_FILE"));
                                etcItem.setInspiratorFlag(JsonIntIsNullCheck(object, "INSPIRATOR_FLAG"));
                                etcItem.setLat(JsonIsNullCheck(object, "LATITUDE"));
                                etcItem.setLng(JsonIsNullCheck(object, "LONGITUDE"));
                                etcItem.setLocation(JsonIsNullCheck(object, "LOCATION"));
                                etcItem.setMemo(JsonIsNullCheck(object, "MEMO"));
                                etcItem.setPefScore(JsonIntIsNullCheck(object, "PEF_SCORE"));
                                etcItem.setStartDt(JsonIsNullCheck(object, "START_DT"));
                                etcItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                                etcItem.setVolume(JsonIsNullCheck(object, "VOLUME"));
                                etcItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));
                                etcItem.setMedicineNo(JsonIsNullCheck(object, "MEDICINE_NO"));
                                etcItem.setUserHistoryNo(JsonIsNullCheck(object, "USER_HISTORY_NO"));
                                etcItem.setKo(JsonIsNullCheck(object, "KO"));
                                etcItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));

                                etcItemArrayList.add(etcItem);
                            }
                        } else {

                            registerDtList.add(JsonIsNullCheck(object, "REGISTER_DT"));
                            koChildList.add("" + JsonIntIsNullCheck(object, "CATEGORY"));
                            koList.add(koChildList);

                            etcItem.setActScore(JsonIntIsNullCheck(object, "ACT_SCORE"));
                            etcItem.setActSelected(JsonIsNullCheck(object, "ACT_SELECTED"));
                            etcItem.setActState(JsonIntIsNullCheck(object, "ACT_STATE"));
                            etcItem.setActType(JsonIntIsNullCheck(object, "ACT_TYPE"));
                            etcItem.setCause(JsonIsNullCheck(object, "CAUSE"));
                            etcItem.setDust(JsonIntIsNullCheck(object, "DUST"));
                            etcItem.setDustStatus(JsonIntIsNullCheck(object, "DUST_STATE"));
                            etcItem.setUltraDust(JsonIntIsNullCheck(object, "ULTRA_DUST"));
                            etcItem.setUltraDustStatus(JsonIntIsNullCheck(object, "ULTRA_DUST_STATE"));
                            etcItem.setEmergencyFlag(JsonIntIsNullCheck(object, "EMERGENCY_FLAG"));
                            etcItem.setEndDt(JsonIsNullCheck(object, "END_DT"));
                            etcItem.setFrequency(JsonIntIsNullCheck(object, "FREQUENCY"));
                            etcItem.setImageFile(JsonIsNullCheck(object, "IMAGE_FILE"));
                            etcItem.setInspiratorFlag(JsonIntIsNullCheck(object, "INSPIRATOR_FLAG"));
                            etcItem.setLat(JsonIsNullCheck(object, "LATITUDE"));
                            etcItem.setLng(JsonIsNullCheck(object, "LONGITUDE"));
                            etcItem.setLocation(JsonIsNullCheck(object, "LOCATION"));
                            etcItem.setMemo(JsonIsNullCheck(object, "MEMO"));
                            etcItem.setPefScore(JsonIntIsNullCheck(object, "PEF_SCORE"));
                            etcItem.setStartDt(JsonIsNullCheck(object, "START_DT"));
                            etcItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                            etcItem.setVolume(JsonIsNullCheck(object, "VOLUME"));
                            etcItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));
                            etcItem.setMedicineNo(JsonIsNullCheck(object, "MEDICINE_NO"));
                            etcItem.setUserHistoryNo(JsonIsNullCheck(object, "USER_HISTORY_NO"));
                            etcItem.setKo(JsonIsNullCheck(object, "KO"));
                            etcItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));

                            etcItemArrayList.add(etcItem);
                        }
//                        }
                    }
                }

                Log.i(TAG, "koList size : " + koList.size());
                for (int i = 0; i < koList.size(); i++) {
                    if (i != 0) {
                        if (registerDtList.get(i - 1).substring(0, 10).equals(registerDtList.get(i).substring(0, 10))) {
                            mList.add(new RecyclerViewItemList(koList.get(i), etcItemArrayList.get(i), RecyclerViewItemList.ITEM_TYPE));
                        } else {

//                            mList.add(new RecyclerViewItemList(registerDtList.get(i), RecyclerViewItemList.HEADER_TYPE, finalMap.get(registerDtList.get(i).substring(0, 10)).get(registerDtList.get(i).substring(0, 10))));
                            mList.add(new RecyclerViewItemList(registerDtList.get(i), RecyclerViewItemList.HEADER_TYPE, finalMap.get(registerDtList.get(i).substring(0, 10))));
//                            mList.add(new RecyclerViewItemList(registerDtList.get(i), RecyclerViewItemList.HEADER_TYPE));
                            mList.add(new RecyclerViewItemList(koList.get(i), etcItemArrayList.get(i), RecyclerViewItemList.ITEM_TYPE));

                        }
                    } else {
                        if (mList.size() > 0) {
                            if (mList.get(mList.size() - 1).getEtcItem() != null) {
                                if (mList.get(mList.size() - 1).getEtcItem().getRegisterDt().contains(registerDtList.get(i).substring(0, 10))) {
                                    mList.add(new RecyclerViewItemList(koList.get(i), etcItemArrayList.get(i), RecyclerViewItemList.ITEM_TYPE));
                                }
                            }
                        } else {
                            mList.add(new RecyclerViewItemList(registerDtList.get(i), RecyclerViewItemList.HEADER_TYPE, finalMap.get(registerDtList.get(i).substring(0, 10))));
//                            mList.add(new RecyclerViewItemList(registerDtList.get(i), RecyclerViewItemList.HEADER_TYPE));
                            mList.add(new RecyclerViewItemList(koList.get(i), etcItemArrayList.get(i), RecyclerViewItemList.ITEM_TYPE));
                        }
                    }
                }
//                adapter = new CalendarRecyclerViewAdapter(CalendarActivity.this, mList);

                Log.d(TAG, TAG + " mList : " + mList);
                recordCalendarView.setCalendarDataSet(mList);

                recyclerView.setAdapter(null);

                ArrayList<RecyclerViewItemList> items = new ArrayList<>();

                items.add(new RecyclerViewItemList(formatYYYYMMDD.format(new Date(System.currentTimeMillis())), 0, new TreeMap<>()));

                Log.d(TAG, "TITLE : " + items.get(0));

                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getEtcItem() != null) {
                        if (mList.get(i).getEtcItem().getRegisterDt() != null) {
                            if (mList.get(i).getEtcItem().getRegisterDt().contains(formatYYYYMMDD.format(new Date(System.currentTimeMillis())))) {
                                items.add(mList.get(i));
                            }
                        }
                    }
                }

                TreeMap<String, ArrayList<HistoryItems>> tempHisItems = new TreeMap<>();
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getHistoryItemList() != null && mList.get(i).getHistoryItemList().size() > 0) {
                        if (mList.get(i).getTitle() != null) {
                            if (mList.get(i).getTitle().contains(formatYYYYMMDD.format(new Date(System.currentTimeMillis())).substring(0, 10))) {
                                List<String> tempList = mList.get(i).getHistoryItemList().keySet().stream().filter(s0 -> s0.contains(formatYYYYMMDD.format(new Date(System.currentTimeMillis())).substring(0, 10))).collect(Collectors.toList());

                                TreeMap<String, ArrayList<HistoryItems>> savedItem = new TreeMap<>();
                                for (int j = 0; j < tempList.size(); j++) {
                                    savedItem.put(tempList.get(j), mList.get(i).getHistoryItemList().get(tempList.get(j)));
                                }


                                tempHisItems = savedItem;
//                                }
                            }
                        }
                    }
                }
                Log.d(TAG, "tempHisItems : " + tempHisItems);
                if (items.get(0) != null) {
                    items.get(0).setHistoryItemList(tempHisItems);
                }
                if (items.size() == 1) {        // 헤더 밖에 없는 경우 array를 초기화 해줌
                    items = new ArrayList<>();
                }
                adapter = null;
                adapter = new CalendarRecyclerViewAdapter(CalendarActivity.this, items);
                recyclerView.removeAllViewsInLayout();
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            } catch (JSONException e) {

            }

            progressDialog.dismiss();
        }
    }

    public HashMap<String, ArrayList<HistoryItems>> setupHistoryDataWithRegiDt(ArrayList<HistoryItems> datas) {
        HashMap<String, ArrayList<HistoryItems>> map = new HashMap<>();

        for (int i = 0; i < datas.size(); i++) {
            if (map.containsKey(datas.get(i).getRegisterDt())) {
                map.get(datas.get(i).getRegisterDt()).add(datas.get(i));
            } else {
                map.put(datas.get(i).getRegisterDt(), new ArrayList<HistoryItems>());
                map.get(datas.get(i).getRegisterDt()).add(datas.get(i));
            }
        }

        return map;
    }

    public HashMap<String, ArrayList<HistoryItems>> setupHistoryData(ArrayList<HistoryItems> datas) {
        HashMap<String, ArrayList<HistoryItems>> map = new HashMap<>();

        for (int i = 0; i < datas.size(); i++) {
            if (map.containsKey(datas.get(i).getRegisterDt().substring(0, 10))) {
                map.get(datas.get(i).getRegisterDt().substring(0, 10)).add(datas.get(i));
            } else {
                map.put(datas.get(i).getRegisterDt().substring(0, 10), new ArrayList<HistoryItems>());
                map.get(datas.get(i).getRegisterDt().substring(0, 10)).add(datas.get(i));
            }
        }

        return map;
    }
}
