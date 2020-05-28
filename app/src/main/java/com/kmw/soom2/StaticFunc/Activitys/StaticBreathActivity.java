package com.kmw.soom2.StaticFunc.Activitys;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.R;
import com.kmw.soom2.StaticFunc.Activitys.Adapter.StaticBreathListAdapter;
import com.kmw.soom2.StaticFunc.Activitys.Item.HistoryItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;

public class StaticBreathActivity extends AppCompatActivity implements View.OnClickListener  {

    final String TAG = "StaticBreathActivity";
    StaticBreathListAdapter adapter;
    ListView listView;
    TextView btnBack;

    String response;
    ArrayList<HistoryItems> historyItems;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_breath);

        findViewByIds();

        progressDialog = new ProgressDialog(StaticBreathActivity.this);
        new SelectPefHistoryListNetWork().execute();
    }

    void findViewByIds() {
        listView = findViewById(R.id.breath_static_listView);
        btnBack = findViewById(R.id.txt_statics_detail_back);

        adapter = new StaticBreathListAdapter(this);

        btnBack.setOnClickListener(this);
    }

    public class SelectPefHistoryListNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {

            RequestBody body = new FormBody.Builder().add("CATEGORY", "22").add("USER_NO", ""+Utils.userItem.getUserNo()).build();

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

            Log.i(TAG,s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    if (JsonIntIsNullCheck(object,"ALIVE_FLAG") == 1) {
                        HistoryItems historyItem = new HistoryItems();

                            historyItem.setUserHistoryNo(JsonIntIsNullCheck(object, "USER_HISTORY_NO"));
                            historyItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                            historyItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));
                            historyItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));

                            historyItem.setPefScore(JsonIntIsNullCheck(object, "PEF_SCORE"));
                            historyItem.setInspiratorFlag(JsonIntIsNullCheck(object, "INSPIRATOR_FLAG"));   // 1 사용, 2 미사용

                            historyItems.add(historyItem);
                    }
                }
                HashMap<String, ArrayList<HistoryItems>> map = setupHistoryData(historyItems);

                TreeMap<String, ArrayList<HistoryItems>> treeMap = new TreeMap<>(map);



                for (Map.Entry<String, ArrayList<HistoryItems>> entry : treeMap.descendingMap().entrySet()) {
                    String key = entry.getKey();
                    ArrayList<HistoryItems> value = entry.getValue();
                    adapter.addItem(value, key);
                }

                listView.setAdapter(adapter);

            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
            }
            progressDialog.dismiss();
        }
    }
    public HashMap<String, ArrayList<HistoryItems>> setupHistoryData(ArrayList<HistoryItems> datas) {
        HashMap<String, ArrayList<HistoryItems>> map = new HashMap<>();

        for (int i = 0; i < datas.size(); i++) {
            if (map.containsKey(datas.get(i).getRegisterDt().substring(0, 10))) {
                map.get(datas.get(i).getRegisterDt().substring(0, 10)).add(datas.get(i));
            }else {
                map.put(datas.get(i).getRegisterDt().substring(0, 10), new ArrayList<HistoryItems>());
                map.get(datas.get(i).getRegisterDt().substring(0, 10)).add(datas.get(i));
            }
        }

        return map;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_statics_detail_back: { // 뒤로 가기
                onBackPressed();
                break;
            }
        }
    }
}
