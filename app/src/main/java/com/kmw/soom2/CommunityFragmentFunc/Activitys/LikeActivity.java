package com.kmw.soom2.CommunityFragmentFunc.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.CommunityFragmentFunc.Adapters.LikeAdapter;
import com.kmw.soom2.CommunityFragmentFunc.Items.LikeItem;
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

public class LikeActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "LikeActivity";
    TextView txtBack;
    ListView listView;
    LikeAdapter adapter;
    Intent beforeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);

        beforeIntent = getIntent();

        FindViewById();

        new SelectLikeListNetWork().execute(beforeIntent.getStringExtra("communityNo"));

    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_like_back);
        listView = (ListView)findViewById(R.id.list_view_like);

        txtBack.setOnClickListener(this);
    }

    String response = "";

    public class SelectLikeListNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody body = new FormBody.Builder().add("COMMUNITY_NO",strings[0]).build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectCommunityLikeList(), body);

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

            adapter = new LikeAdapter(LikeActivity.this);

            try {
                JSONObject jsonObject = new JSONObject(s);

                JSONArray jsonArray = jsonObject.getJSONArray("list");

                Log.i(TAG,"jsonArray : " + jsonArray.length());

                for (int i = 0; i < jsonArray.length(); i++){

                    JSONObject object = jsonArray.getJSONObject(i);
                    if (JsonIntIsNullCheck(object,"FLAG") == 1){
                        LikeItem likeItem = new LikeItem();
                        likeItem.setImgPath(JsonIsNullCheck(object,"PROFILE_IMG"));
                        likeItem.setNickName(JsonIsNullCheck(object,"NICKNAME"));
                        adapter.addItem(likeItem);
                    }
                }

                listView.setAdapter(adapter);

            }catch (JSONException e){

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_like_back : {
                onBackPressed();
                break;
            }
        }
    }
}
