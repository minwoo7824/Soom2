package com.kmw.soom2.StaticFunc.Activitys;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.HttpClient;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.R;
import com.kmw.soom2.StaticFunc.Activitys.Item.HistoryItems;
import com.kmw.soom2.Views.CalendarView;
import com.kmw.soom2.Views.StaticMedicineCalendarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.formatYYYYMM;

public class StaticMedicineActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "StaticMedicineActivity";
    TextView txtBack;
    LinearLayout linLeftBtn,linRightBtn;
    public static TextView txtCalendarTitle;
    StaticMedicineCalendarView staticMedicineCalendarView;
    LinearLayout linListParent;
    ArrayList<CheckBox> checkBoxArrayList = new ArrayList<>();
    ArrayList<TextView> nameTextList = new ArrayList<>();
    ArrayList<TextView> basicTextList = new ArrayList<>();
    ArrayList<TextView> emergencyTextList = new ArrayList<>();

    ArrayList<HistoryItems> historyItems;
    String response;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_medicine);

        FindViewById();
        progressDialog = new ProgressDialog(StaticMedicineActivity.this);
        new SelectMedicineHistoryNetWork().execute();
    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_statics_medicine_back);
        linLeftBtn = (LinearLayout)findViewById(R.id.lin_statics_medicine_left_btn);
        linRightBtn = (LinearLayout)findViewById(R.id.lin_statics_medicine_right_btn);
        txtCalendarTitle = (TextView)findViewById(R.id.txt_statics_medicine_title);
        staticMedicineCalendarView = (StaticMedicineCalendarView)findViewById(R.id.calendar_statics_medicine);
        linListParent = (LinearLayout)findViewById(R.id.lin_statics_medicine_list_parent);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));

        txtCalendarTitle.setText(formatYYYYMM.format(calendar.getTime()));


        staticMedicineCalendarView.setRobotoCalendarListener(new CalendarView.RobotoCalendarListener() {
            @Override
            public void onDayClick(Calendar daySelectedCalendar) {

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(System.currentTimeMillis()));
                if (daySelectedCalendar.before(calendar)){

                }
            }

            @Override
            public void onDayLongClick(Calendar daySelectedCalendar) {

            }

            @Override
            public void onRightButtonClick(Calendar daySelectedCalendar) {
                Log.i(TAG, "");
            }

            @Override
            public void onLeftButtonClick(Calendar daySelectedCalendar) {

            }
        });

        txtBack.setOnClickListener(this);
        linLeftBtn.setOnClickListener(this);
        linRightBtn.setOnClickListener(this);

//        for (int i = 0; i < 5; i++){
//            medicineListMake("name"+i,i,i);
//        }

    }
    void isNullMedicineListData() {
        linListParent.removeAllViews();
    }

    void medicineListMake(int index, String name, final ArrayList<HistoryItems> items){
        View listView = new View(this);
        listView = getLayoutInflater().inflate(R.layout.view_statics_medicine_list_item,null);

        final CheckBox chbCheck = (CheckBox)listView.findViewById(R.id.chb_statics_medicine_list_item);
        final TextView txtName = (TextView)listView.findViewById(R.id.txt_statics_medicine_list_item_name);
        final TextView txtBasic = (TextView)listView.findViewById(R.id.txt_statics_medicine_list_item_basic);
        final TextView txtEmergency = (TextView)listView.findViewById(R.id.txt_statics_medicine_list_item_emergency);

        linListParent.addView(listView);

        int basicCnt = items.stream().filter(new Predicate<HistoryItems>() {
            @Override
            public boolean test(HistoryItems f) {
                return (f.getEmergencyFlag() == 1);
            }
        }).collect(Collectors.toList()).size();
        int emergencyCnt = items.stream().filter(new Predicate<HistoryItems>() {
            @Override
            public boolean test(HistoryItems f) {
                return (f.getEmergencyFlag() == 2);
            }
        }).collect(Collectors.toList()).size();

        txtName.setText(name);
        txtBasic.setText("일반복용 " + basicCnt + " 회");
        txtEmergency.setText("응급복용 " + emergencyCnt + " 회");

        checkBoxArrayList.add(chbCheck);
        nameTextList.add(txtName);
        basicTextList.add(txtBasic);
        emergencyTextList.add(txtEmergency);

        if (index == 0) {
            chbCheck.setChecked(true);
            txtName.setTextColor(getResources().getColor(R.color.black));
            txtBasic.setTextColor(getResources().getColor(R.color.colorPrimary));
            txtEmergency.setTextColor(getResources().getColor(R.color.ff6767));

            staticMedicineCalendarView.setCalendarItem(items);
        }

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"click");
                for (int i = 0; i < checkBoxArrayList.size(); i++){
                    checkBoxArrayList.get(i).setChecked(false);
                    nameTextList.get(i).setTextColor(getResources().getColor(R.color.acacac));
                    basicTextList.get(i).setTextColor(getResources().getColor(R.color.acacac));
                    emergencyTextList.get(i).setTextColor(getResources().getColor(R.color.acacac));
                }
                chbCheck.setChecked(true);
                txtName.setTextColor(getResources().getColor(R.color.black));
                txtBasic.setTextColor(getResources().getColor(R.color.colorPrimary));
                txtEmergency.setTextColor(getResources().getColor(R.color.ff6767));

                staticMedicineCalendarView.setCalendarItem(items);

            }
        });
    }
    void medicineListMake(String name, int basicCnt, int emergencyCnt){
        View listView = new View(this);
        listView = getLayoutInflater().inflate(R.layout.view_statics_medicine_list_item,null);

        final CheckBox chbCheck = (CheckBox)listView.findViewById(R.id.chb_statics_medicine_list_item);
        final TextView txtName = (TextView)listView.findViewById(R.id.txt_statics_medicine_list_item_name);
        final TextView txtBasic = (TextView)listView.findViewById(R.id.txt_statics_medicine_list_item_basic);
        final TextView txtEmergency = (TextView)listView.findViewById(R.id.txt_statics_medicine_list_item_emergency);

        linListParent.addView(listView);

        txtName.setText(name);
        txtBasic.setText("일반복용 " + basicCnt + " 회");
        txtEmergency.setText("응급복용 " + emergencyCnt + " 회");

        checkBoxArrayList.add(chbCheck);
        nameTextList.add(txtName);
        basicTextList.add(txtBasic);
        emergencyTextList.add(txtEmergency);

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < checkBoxArrayList.size(); i++){
                    checkBoxArrayList.get(i).setChecked(false);
                    nameTextList.get(i).setTextColor(getResources().getColor(R.color.acacac));
                    basicTextList.get(i).setTextColor(getResources().getColor(R.color.acacac));
                    emergencyTextList.get(i).setTextColor(getResources().getColor(R.color.acacac));
                }
                chbCheck.setChecked(true);
                txtName.setTextColor(getResources().getColor(R.color.black));
                txtBasic.setTextColor(getResources().getColor(R.color.colorPrimary));
                txtEmergency.setTextColor(getResources().getColor(R.color.ff6767));


            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_statics_medicine_back : {
                onBackPressed();
                break;
            }
            case R.id.lin_statics_medicine_left_btn : {
                staticMedicineCalendarView.leftClick();
                new SelectMedicineHistoryNetWork().execute();
                break;
            }
            case R.id.lin_statics_medicine_right_btn : {
                staticMedicineCalendarView.rightClick();
                new SelectMedicineHistoryNetWork().execute();
                break;
            }
        }
    }

    public HashMap<String, ArrayList<HistoryItems>> setupHistoryData(ArrayList<HistoryItems> datas) {
        HashMap<String, ArrayList<HistoryItems>> map = new HashMap<>();

        for (int i = 0; i < datas.size(); i++) {
            if (map.containsKey(datas.get(i).getKo())) {
                map.get(datas.get(i).getKo()).add(datas.get(i));
            }else {
                map.put(datas.get(i).getKo(), new ArrayList<HistoryItems>());
                map.get(datas.get(i).getKo()).add(datas.get(i));
            }
        }

        return map;
    }

    public class SelectMedicineHistoryNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpClient.Builder http = new HttpClient.Builder("POST", Utils.Server.selectHomeFeedList());

            String month = "";
            String year = "";

            if (staticMedicineCalendarView.currentCalendar.get(Calendar.MONTH) < 9) {
                month = "0" + (staticMedicineCalendarView.currentCalendar.get(Calendar.MONTH) + 1);
            }else {
                month = "" + (staticMedicineCalendarView.currentCalendar.get(Calendar.MONTH) + 1);
            }

            year = "" + staticMedicineCalendarView.currentCalendar.get(Calendar.YEAR);

            RequestBody body = new FormBody.Builder().add("MONTH", month).add("YEAR", year).add("USER_NO", ""+Utils.userItem.getUserNo()).add("CATEGORY", "1").build();

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
            historyItems = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    if (JsonIntIsNullCheck(object,"ALIVE_FLAG") == 1) {
                        HistoryItems historyItem = new HistoryItems();

                        historyItem.setUserHistoryNo(JsonIntIsNullCheck(object,"USER_HISTORY_NO"));
                        historyItem.setUserNo(JsonIntIsNullCheck(object,"USER_NO"));
                        historyItem.setCategory(JsonIntIsNullCheck(object,"CATEGORY"));
                        historyItem.setRegisterDt(JsonIsNullCheck(object,"REGISTER_DT"));

                        historyItem.setMedicineNo(JsonIntIsNullCheck(object,"MEDICINE_NO"));
                        historyItem.setFrequency(JsonIntIsNullCheck(object,"FREQUENCY"));
                        historyItem.setVolume(JsonIsNullCheck(object,"VOLUME"));
                        historyItem.setKo(JsonIsNullCheck(object,"KO"));
                        historyItem.setEmergencyFlag(JsonIntIsNullCheck(object,"EMERGENCY_FLAG"));
                        historyItem.setUnit(JsonIsNullCheck(object,"UNIT"));

                        historyItems.add(historyItem);
                    }
                }
                HashMap<String, ArrayList<HistoryItems>> map = setupHistoryData(historyItems);

                Log.i(TAG, map.size() + "");
                linListParent.removeAllViews();
                int i = 0;
                for (Map.Entry<String, ArrayList<HistoryItems>> entry : map.entrySet()) {
                    String key = entry.getKey();
                    ArrayList<HistoryItems> value = entry.getValue();
                    //TODO: other cool stuff
                    medicineListMake(i, key, value);
                    i++;
                }

            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
                isNullMedicineListData();

            }
            progressDialog.dismiss();
        }
    }
}
