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
import com.kmw.soom2.StaticFunc.Activitys.Adapter.StaticAsthmaListAdapter;
import com.kmw.soom2.StaticFunc.Activitys.Item.HistoryItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;

public class StaticAsthmaActivity extends AppCompatActivity implements View.OnClickListener   {
    final String TAG = "StaticAsthmaActivity";
    ListView listView;
    TextView btnBack;
    StaticAsthmaListAdapter adapter;
    String response;
    ArrayList<HistoryItems> historyItems;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_asthma);

        findViewByIds();
        progressDialog = new ProgressDialog(StaticAsthmaActivity.this);
        new SelectActHistoryListNetWork().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    void findViewByIds() {
        listView = findViewById(R.id.asthma_static_listview);
        btnBack = findViewById(R.id.txt_statics_detail_back);
        btnBack.setOnClickListener(this);

        adapter = new StaticAsthmaListAdapter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_statics_detail_back: { // 뒤로 가기
                onBackPressed();
                Log.d(TAG, "OnClick~");
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "on BackPressed");
    }

    public class SelectActHistoryListNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {

            RequestBody body = new FormBody.Builder().add("CATEGORY", "21").add("USER_NO", ""+Utils.userItem.getUserNo()).build();

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

                        historyItem.setActScore(JsonIntIsNullCheck(object, "ACT_SCORE"));
                        historyItem.setActSelected(JsonIsNullCheck(object, "ACT_SELECTED"));
                        historyItem.setActState(JsonIntIsNullCheck(object, "ACT_STATE"));
                        historyItem.setActType(JsonIntIsNullCheck(object, "ACT_TYPE"));

                        historyItems.add(historyItem);
                    }
                }
                for (int i = 0; i < historyItems.size(); i++) {
                    adapter.addItem(historyItems.get(i));
                }
                listView.setAdapter(adapter);

            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
            }
            progressDialog.dismiss();
        }
    }
}
