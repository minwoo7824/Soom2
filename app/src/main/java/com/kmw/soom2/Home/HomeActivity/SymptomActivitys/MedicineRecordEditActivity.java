package com.kmw.soom2.Home.HomeActivity.SymptomActivitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.HomeAdapter.MedicineRecordEditListAdapter;
import com.kmw.soom2.Home.HomeItem.EtcItem;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;

public class MedicineRecordEditActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "MedicineRecordEditActivity";
    TextView txtBack,txtAllRemove;
    TextView txtDate,txtTime;
    ListView listView;

    Intent beforeIntent;

    MedicineRecordEditListAdapter adapter;

    ProgressDialog progressDialog;

    public static ArrayList<EtcItem> etcItemArrayList;

    public static boolean allRemove = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_record_edit);

        beforeIntent = getIntent();

        etcItemArrayList = new ArrayList<>();

        FindViewById();

//        i.putExtra("medicineHistoryNo", finalItem.getEtcItem().getUserHistoryNo());
//        i.putExtra("medicineNo", finalItem.getEtcItem().getMedicineNo());
//        i.putExtra("volume", finalItem.getEtcItem().getVolume());
//        i.putExtra("unit", finalItem.getEtcItem().getUnit());
//        i.putExtra("emergency", finalItem.getEtcItem().getEmergencyFlag());

        if (beforeIntent != null){
            if (beforeIntent.hasExtra("registerDt")) {
                txtDate.setText(beforeIntent.getStringExtra("registerDt").substring(0, 10));
                try {
                    txtTime.setText(Utils.formatHHMM.format(Utils.formatHHMMSS.parse(beforeIntent.getStringExtra("registerDt").substring(11, 18))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            new SelectMedicineListNetWork().execute();

        }else{
            txtDate.setText(Utils.formatYYYYMMDD2.format(new Date(System.currentTimeMillis())));
            txtTime.setText(Utils.formatHHMM.format(new Date(System.currentTimeMillis())));
        }
    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_medicine_record_edit_back);
        txtAllRemove = (TextView)findViewById(R.id.txt_medicine_record_edit_all_remove);
        txtDate = (TextView)findViewById(R.id.txt_medicine_record_edit_date);
        txtTime = (TextView)findViewById(R.id.txt_medicine_record_edit_time);
        listView = (ListView)findViewById(R.id.list_medicine_record_edit);

        txtBack.setOnClickListener(this);
        txtAllRemove.setOnClickListener(this);
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

                allRemove = true;
                Log.i(TAG,"etc size : " + etcItemArrayList.size());
                for (int i = 0; i < etcItemArrayList.size(); i++){
                    if (i == (etcItemArrayList.size()-1)){
                        allRemove = false;
                    }
                    new DeleteHomeFeedHistoryNetWork(MedicineRecordEditActivity.this,etcItemArrayList.get(i).getUserHistoryNo()).execute();
                }
            }
        });
    }

    String response;

    public class SelectMedicineListNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MedicineRecordEditActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody body = new FormBody.Builder().add("USER_NO", ""+ Utils.userItem.getUserNo()).add("CATEGORY","1").build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectHomeFeedList(), body);
                Log.d("Response", response);
//                logLargeString(response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            adapter = new MedicineRecordEditListAdapter(getLayoutInflater(), MedicineRecordEditActivity.this);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                int size = jsonArray.length();

                for (int i = 0; i < size; i++){
                    JSONObject object = jsonArray.getJSONObject(i);

                    Log.i(TAG,jsonArray.getString(i));

                    if (JsonIntIsNullCheck(object,"ALIVE_FLAG") == 1){

                        ArrayList<String> koChildList = new ArrayList<>();

                        EtcItem etcItem = new EtcItem();

                        if (JsonIntIsNullCheck(object,"CATEGORY") == 1){

                            if (beforeIntent.getStringExtra("registerDt").equals(JsonIsNullCheck(object,"REGISTER_DT"))) {

                                etcItem.setActScore(JsonIntIsNullCheck(object,"ACT_SCORE"));
                                etcItem.setActSelected(JsonIsNullCheck(object,"ACT_SELECTED"));
                                etcItem.setActState(JsonIntIsNullCheck(object,"ACT_STATE"));
                                etcItem.setActType(JsonIntIsNullCheck(object,"ACT_TYPE"));
                                etcItem.setCause(JsonIsNullCheck(object,"CAUSE"));
                                etcItem.setDust(JsonIntIsNullCheck(object,"DUST"));
                                etcItem.setDustStatus(JsonIntIsNullCheck(object,"DUST_STATE"));
                                etcItem.setUltraDust(JsonIntIsNullCheck(object,"ULTRA_DUST"));
                                etcItem.setUltraDustStatus(JsonIntIsNullCheck(object,"ULTRA_DUST_STATE"));
                                etcItem.setEmergencyFlag(JsonIntIsNullCheck(object,"EMERGENCY_FLAG"));
                                etcItem.setEndDt(JsonIsNullCheck(object,"END_DT"));
                                etcItem.setFrequency(JsonIntIsNullCheck(object,"FREQUENCY"));
                                etcItem.setImageFile(JsonIsNullCheck(object,"IMAGE_FILE"));
                                etcItem.setInspiratorFlag(JsonIntIsNullCheck(object,"INSPIRATOR_FLAG"));
                                etcItem.setLat(JsonIsNullCheck(object,"LATITUDE"));
                                etcItem.setLng(JsonIsNullCheck(object,"LONGITUDE"));
                                etcItem.setLocation(JsonIsNullCheck(object,"LOCATION"));
                                etcItem.setMemo(JsonIsNullCheck(object,"MEMO"));
                                etcItem.setPefScore(JsonIntIsNullCheck(object,"PEF_SCORE"));
                                etcItem.setStartDt(JsonIsNullCheck(object,"START_DT"));
                                etcItem.setUnit(JsonIsNullCheck(object,"UNIT"));
                                etcItem.setVolume(JsonIsNullCheck(object,"VOLUME"));
                                etcItem.setRegisterDt(JsonIsNullCheck(object,"REGISTER_DT"));
                                etcItem.setMedicineNo(JsonIsNullCheck(object,"MEDICINE_NO"));
                                etcItem.setUserHistoryNo(JsonIsNullCheck(object,"USER_HISTORY_NO"));
                                etcItem.setKo(JsonIsNullCheck(object,"KO"));
                                etcItem.setMedicineTypeNo(JsonIntIsNullCheck(object,"MEDICINE_TYPE_NO"));

                                etcItemArrayList.add(etcItem);
                            }
                        }
                    }
                }

                for (int i = 0; i < etcItemArrayList.size(); i++){
                    adapter.addItem(etcItemArrayList.get(i).getMedicineNo(), etcItemArrayList.get(i).getUserHistoryNo(),"" + etcItemArrayList.get(i).getEmergencyFlag(),
                            etcItemArrayList.get(i).getKo(),etcItemArrayList.get(i).getVolume() + etcItemArrayList.get(i).getUnit(),Integer.parseInt(etcItemArrayList.get(i).getMedicineNo()));
                }

                listView.setAdapter(adapter);

            }catch (JSONException e){

            }
            progressDialog.dismiss();
        }
    }

    public static class DeleteHomeFeedHistoryNetWork extends AsyncTask<String, String, String> {

        String historyNo = "";
        String response = "";
        Context context;

        public DeleteHomeFeedHistoryNetWork(Context context,String historyNo) {
            this.historyNo = historyNo;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            @SuppressLint("WrongThread") RequestBody body = new FormBody.Builder()
                    .add("USER_HISTORY_NO",historyNo)
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.deleteHomeFeedHistoryList(), body);
                Log.d("Response", response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!allRemove){
                        ((MedicineRecordEditActivity)context).setResult(RESULT_OK);
                        ((MedicineRecordEditActivity)context).onBackPressed();
                    }
                }
            },500);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_medicine_record_edit_back : {
                onBackPressed();
                break;
            }
            case R.id.txt_medicine_record_edit_all_remove : {
                TwoBtnPopup(0,"복용기록 삭제","복용기록을\n전체 삭제하시겠습니까?","취소","확인");
                break;
            }
        }
    }
}
