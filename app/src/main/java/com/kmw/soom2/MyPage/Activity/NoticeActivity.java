package com.kmw.soom2.MyPage.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.HomeActivity.MainActivity;
import com.kmw.soom2.MyPage.Adapter.NoticeAdapter;
import com.kmw.soom2.MyPage.Item.NoticeItem;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;

public class NoticeActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "NoticeActivity";
    TextView txtBack;
    ListView listView;
    NoticeAdapter noticeAdapter;

    String response;

    ProgressDialog progressDialog;

    Intent beforeIntent;

    String noticeNo = "", contents = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        beforeIntent = getIntent();

        FindViewById();

    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_notice_back);
        listView = (ListView)findViewById(R.id.list_view_notice);

        txtBack.setOnClickListener(this);

        noticeAdapter = new NoticeAdapter(this);

        progressDialog = new ProgressDialog(NoticeActivity.this);

        new SelectNoticeListNetWork().execute();
    }

    public class SelectNoticeListNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder().build();
            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectNoticeList(), body);

                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }



            return null;
        }

        @Override
        protected void onPostExecute(String s) {



            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                for (int i = jsonArray.length()-1; i >= 0; i--){
                    JSONObject object = jsonArray.getJSONObject(i);

                    Log.i(TAG,"response : " + object.toString());

                    if (JsonIntIsNullCheck(object,"ALIVE_FLAG") == 1){
                        if (beforeIntent.hasExtra("noticeNo")){
                            if (JsonIsNullCheck(object,"PUSH_MESSAGE").equals(beforeIntent.getStringExtra("body"))){
                                noticeNo = JsonIsNullCheck(object,"NOTICE_NO");
                                contents = JsonIsNullCheck(object,"CONTENTS");
                                Log.i(TAG,"no : " + noticeNo + " contents : " + contents);
                            }
                        }

                        NoticeItem noticeItem = new NoticeItem();
                        noticeItem.setNo(JsonIsNullCheck(object,"NOTICE_NO"));
                        noticeItem.setNoticeFlag(JsonIntIsNullCheck(object,"NOTICE_FLAG"));
                        noticeItem.setTitle(JsonIsNullCheck(object,"TITLE"));
                        noticeItem.setContents(JsonIsNullCheck(object,"CONTENTS"));
                        noticeItem.setDate(JsonIsNullCheck(object,"CREATE_DT"));

                        noticeAdapter.addItem(noticeItem);
                    }
                }

                listView.setAdapter(noticeAdapter);

                if (beforeIntent.hasExtra("push")){
                    Log.i(TAG,"push");
                    Intent intent = new Intent(NoticeActivity.this, NoticeDetailActivity.class);
                    intent.putExtra("noticeNo",noticeNo);
                    intent.putExtra("contents", contents);
                    startActivity(intent);
                }else{
                    Log.i(TAG,"push no");
                }

            }catch (JSONException e){

            }
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (beforeIntent.hasExtra("push")){
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("notice",true);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_notice_back : {
                onBackPressed();
                break;
            }
        }
    }
}
