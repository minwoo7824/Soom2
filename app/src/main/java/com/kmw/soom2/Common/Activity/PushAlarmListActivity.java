package com.kmw.soom2.Common.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.Adapter.PushAlarmListAdapter;
import com.kmw.soom2.Common.Item.PushItems;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.userItem;

public class PushAlarmListActivity extends AppCompatActivity {
    final String TAG = "PushAlarmListActivity";
    ListView listView;
    String response;
    PushAlarmListAdapter adapter;
    TextView txtBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_alarm_list);

        findViewById();
    }
    void findViewById() {
        listView = findViewById(R.id.push_list_view);
        txtBack = findViewById(R.id.txt_check_first_back);
        adapter = new PushAlarmListAdapter(this);

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        new UpdateUserInfoNetwork().execute();
    }

    public class UpdateUserInfoNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder());

            body.add("USER_NO", String.valueOf(userItem.getUserNo()));

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectPushAlarmList(), body.build());

                Log.d("Response Signup Sns : ", response);

                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (JsonIsNullCheck(jsonObject, "result").equals("N")) {

                }else {
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    if (jsonArray.length() > 0) {
                        for (int i = jsonArray.length()-1; i >= 0; i--) {
                            PushItems item = new PushItems();
                            JSONObject object = jsonArray.getJSONObject(i);

                            item.setTitle(JsonIsNullCheck(object, "TITLE"));
                            item.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                            item.setPushNo(JsonIntIsNullCheck(object, "PUSH_NO"));
                            item.setContents(JsonIsNullCheck(object, "CONTENTS"));
                            item.setCreateDt(JsonIsNullCheck(object, "CREATE_DT"));

                            adapter.addItem(item);
                        }
                        listView.setAdapter(adapter);
                    }
                }
            }catch (JSONException e){
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }
}