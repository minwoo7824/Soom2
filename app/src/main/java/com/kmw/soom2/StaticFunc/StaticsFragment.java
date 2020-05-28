package com.kmw.soom2.StaticFunc;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.kmw.soom2.Common.Activity.PushAlarmListActivity;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.R;
import com.kmw.soom2.StaticFunc.Activitys.Item.HistoryItems;
import com.kmw.soom2.StaticFunc.Activitys.StaticAsthmaActivity;
import com.kmw.soom2.StaticFunc.Activitys.StaticBreathActivity;
import com.kmw.soom2.StaticFunc.Activitys.StaticMedicineActivity;
import com.kmw.soom2.StaticFunc.Activitys.StaticSymptomActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;


public class StaticsFragment extends Fragment implements View.OnClickListener {

    private String TAG = "StaticsFragment";
    ImageView imgAlarm;
    LinearLayout linMedicine,linSymptom,linBreath,linAsthma;
    TextView txtMedicine,txtSymptom,txtBreath,txtAsthma;

    String response;

    ProgressDialog progressDialog;

    boolean medicineFlag = false, symptomFlag = false, breathFlag = false, asthmaFlag = false;

    public StaticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_statics, container, false);

        imgAlarm = (ImageView)v.findViewById(R.id.img_statics_alarm);
        linMedicine = (LinearLayout)v.findViewById(R.id.lin_statics_medicine);
        linSymptom = (LinearLayout)v.findViewById(R.id.lin_statics_symptom);
        linBreath = (LinearLayout)v.findViewById(R.id.lin_statics_breath);
        linAsthma = (LinearLayout)v.findViewById(R.id.lin_statics_asthma);
        txtMedicine = (TextView)v.findViewById(R.id.txt_statics_medicine);
        txtSymptom = (TextView)v.findViewById(R.id.txt_statics_symptom);
        txtBreath = (TextView)v.findViewById(R.id.txt_statics_breath);
        txtAsthma = (TextView)v.findViewById(R.id.txt_statics_asthma);

        linMedicine.setOnClickListener(this);
        linSymptom.setOnClickListener(this);
        linBreath.setOnClickListener(this);
        linAsthma.setOnClickListener(this);

        imgAlarm.setOnClickListener(this);


        progressDialog = new ProgressDialog(getActivity());

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_statics_alarm : {
                Intent intent = new Intent(getActivity(), PushAlarmListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.lin_statics_medicine : {
                if (medicineFlag) {
                    Intent i = new Intent(getActivity(), StaticMedicineActivity.class);
                    startActivity(i);
                }else {
                    OneBtnPopup(getActivity(), "약 보고서", "복용 기록이 없어\n" +
                            "보고서를 만들지 못했습니다.\n" +
                            "HOME에서 복용 여부를 기록해주세요.", "확인");
                }
                break;
            }
            case R.id.lin_statics_symptom : {
                if (symptomFlag) {
                    Intent i = new Intent(getActivity(), StaticSymptomActivity.class);
                    startActivity(i);
                }else {
                    OneBtnPopup(getActivity(), "보고서", "증상기록이 없어\n보고서를 만들지 못했습니다.\n홈에서 증상을 기록해주세요.", "확인");
                }
                break;
            }
            case R.id.lin_statics_breath : {
                if(breathFlag) {
                    Intent i = new Intent(getActivity(), StaticBreathActivity.class);
                    startActivity(i);
                }else {
                    OneBtnPopup(getActivity(), "보고서", "폐기능 기록이 없어\n보고서를 만들지 못했습니다.\n홈에서 폐기능을 기록해주세요.", "확인");
                }
                break;
            }
            case R.id.lin_statics_asthma : {
                if (asthmaFlag) {
                    Intent i = new Intent(getActivity(), StaticAsthmaActivity.class);
                    startActivity(i);
                }else {
                    OneBtnPopup(getActivity(), "보고서", "천식조절검사 기록이 없어\n보고서를 만들지 못했습니다.\n홈에서 천식조절검사를 기록해주세요.", "확인");
                }
                break;
            }
        }
    }
    public static void OneBtnPopup(Context context, String title, String contents, String btnText){

        final Dialog dateTimeDialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.one_btn_popup,null);

        TextView txtTitle = (TextView)layout.findViewById(R.id.txt_one_btn_popup_title);
        TextView txtContents = (TextView)layout.findViewById(R.id.txt_one_btn_popup_contents);
        Button btnOk = (Button)layout.findViewById(R.id.btn_one_btn_popup_ok);

        txtTitle.setText(title);
        txtContents.setText(contents);
        btnOk.setText(btnText);

        dateTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dateTimeDialog.getWindow();

        dateTimeDialog.setContentView(layout);
        dateTimeDialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialog.dismiss();
            }
        });

    }
    private String returnDateAfterToday(String stDay) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
//        Date date = new Date(calendar.getTimeInMillis());
//        String toDay = format.format(date);
        long today = calendar.getTimeInMillis();

        try {
            long lastDay = format.parse(stDay).getTime();
            long differ = today - lastDay;

            Log.d(TAG, " (differ/(24*60*60*1000) : " + (differ/(24*60*60*1000)));

            return "" + (differ/(24*60*60*1000));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }

    public class SelectHistoryNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {

            ArrayList<HistoryItems> items = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                for (int i = 0; i < jsonArray.length(); i++) {

                    Log.i(TAG,"기록 : " + jsonArray.getJSONObject(i));

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

                        items.add(historyItem);
                    }
                }

                ArrayList<HistoryItems> medicineHistory = new ArrayList<>();
                ArrayList<HistoryItems> symptomHistory = new ArrayList<>();
                ArrayList<HistoryItems> pefHistory = new ArrayList<>();
                ArrayList<HistoryItems> actHistory = new ArrayList<>();

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).getCategory() == 1) {
                        medicineHistory.add(items.get(i));
                    }else if (items.get(i).getCategory() > 10 && items.get(i).getCategory() < 20) {
                        symptomHistory.add(items.get(i));
                    }else if (items.get(i).getCategory() == 21) {
                        actHistory.add(items.get(i));
                    }else if (items.get(i).getCategory() == 22) {
                        pefHistory.add(items.get(i));
                    }
                }
                Collections.sort(medicineHistory, new Comparator<HistoryItems>() {
                    @Override
                    public int compare(HistoryItems t0, HistoryItems t1) {
                        return t0.getRegisterDt().compareTo(t1.getRegisterDt());
                    }
                });
                Collections.sort(symptomHistory, new Comparator<HistoryItems>() {
                    @Override
                    public int compare(HistoryItems t0, HistoryItems t1) {
                        return t0.getRegisterDt().compareTo(t1.getRegisterDt());
                    }
                });
                Collections.sort(actHistory, new Comparator<HistoryItems>() {
                    @Override
                    public int compare(HistoryItems t0, HistoryItems t1) {
                        return t0.getRegisterDt().compareTo(t1.getRegisterDt());
                    }
                });
                Collections.sort(pefHistory, new Comparator<HistoryItems>() {
                    @Override
                    public int compare(HistoryItems t0, HistoryItems t1) {
                        return t0.getRegisterDt().compareTo(t1.getRegisterDt());
                    }
                });

                Log.i(TAG,"기록 : " + medicineHistory.size());

                if (medicineHistory.size() > 0) {
                    if (returnDateAfterToday(medicineHistory.get(medicineHistory.size() - 1).getRegisterDt()) != "") {
                        if (returnDateAfterToday(medicineHistory.get(medicineHistory.size() - 1).getRegisterDt()).equals("0")){
                            txtMedicine.setText("오늘");
                        }else{
                            txtMedicine.setText(returnDateAfterToday(medicineHistory.get(medicineHistory.size() - 1).getRegisterDt()) + "일 전");
                        }

                        medicineFlag = true;
                    }else {
                        txtMedicine.setText("기록 없음");
                        medicineFlag = false;
                    }
                }else {
                    txtMedicine.setText("기록 없음");
                    medicineFlag = false;
                }
                if (symptomHistory.size() > 0) {
                    if (returnDateAfterToday(symptomHistory.get(symptomHistory.size() - 1).getRegisterDt()) != "") {
                        if (returnDateAfterToday(symptomHistory.get(symptomHistory.size() - 1).getRegisterDt()).equals("0")){
                            txtSymptom.setText("오늘");
                        }else{
                            txtSymptom.setText(returnDateAfterToday(symptomHistory.get(symptomHistory.size() - 1).getRegisterDt()) + "일 전");
                        }

                        symptomFlag = true;
                    }else {
                        txtSymptom.setText("기록 없음");
                        symptomFlag = false;
                    }
                }else {
                    txtSymptom.setText("기록 없음");
                    symptomFlag = false;
                }
                if (pefHistory.size() > 0) {
                    if (returnDateAfterToday(pefHistory.get(pefHistory.size() - 1).getRegisterDt()) != "") {
                        if (returnDateAfterToday(pefHistory.get(pefHistory.size() - 1).getRegisterDt()).equals("0")){
                            txtBreath.setText("오늘");
                        }else{
                            txtBreath.setText(returnDateAfterToday(pefHistory.get(pefHistory.size() - 1).getRegisterDt()) + "일 전");
                        }

                        breathFlag = true;
                    }else {
                        txtBreath.setText("기록 없음");
                        breathFlag = false;
                    }
                }else {
                    txtBreath.setText("기록 없음");
                    breathFlag = false;
                }
                if (actHistory.size() > 0) {
                    if (returnDateAfterToday(actHistory.get(actHistory.size() - 1).getRegisterDt()) != "") {
                        if (returnDateAfterToday(actHistory.get(actHistory.size() - 1).getRegisterDt()).equals("0")){
                            txtAsthma.setText("오늘");
                        }else{
                            txtAsthma.setText(returnDateAfterToday(actHistory.get(actHistory.size() - 1).getRegisterDt()) + "일 전");
                        }

                        asthmaFlag = true;
                    }else {
                        txtAsthma.setText("기록 없음");
                        asthmaFlag = false;
                    }
                }else {
                    txtAsthma.setText("기록 없음");
                    asthmaFlag = false;
                }


            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
//                isNullMedicineListData();

            }
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder().add("USER_NO", ""+Utils.userItem.getUserNo()).build();

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
    }

    @Override
    public void onResume() {
        super.onResume();

        new SelectHistoryNetWork().execute();

    }
}