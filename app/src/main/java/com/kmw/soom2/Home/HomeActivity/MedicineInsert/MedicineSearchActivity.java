package com.kmw.soom2.Home.HomeActivity.MedicineInsert;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.HomeActivity.MainActivity;
import com.kmw.soom2.Home.HomeActivity.MedicineInsert.Adapters.MedicineSearchAdapter;
import com.kmw.soom2.Home.HomeAdapter.ViewPagerAdapter;
import com.kmw.soom2.Home.HomeItem.BannerItem;
import com.kmw.soom2.Home.HomeItem.ZoomAnimation;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.MEDICINE_LIST;

public class MedicineSearchActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "MedicineSearchActivity";
    TextView txtBack;
    EditText edtSearch;
    ListView listView;
    LinearLayout linNoList;
    Button btnRequest;
    MedicineSearchAdapter adapter;

    boolean newbieCheck = false;
    Intent beforIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_search);

        beforIntent = getIntent();

        if (beforIntent != null){
            if (beforIntent.hasExtra("newbie")){
                newbieCheck = true;
            }
        }

        FindViewById();

    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_medicine_search_back);
        edtSearch = (EditText)findViewById(R.id.edt_medicine_search);
        listView = (ListView)findViewById(R.id.list_medicine_search);
        linNoList = (LinearLayout)findViewById(R.id.lin_medicine_search_no_list);
        btnRequest = (Button)findViewById(R.id.btn_medicine_search_request);

        adapter = new MedicineSearchAdapter(this,newbieCheck);

        for (int i = 0; i < MEDICINE_LIST.size(); i++){
            adapter.addItem(MEDICINE_LIST.get(i));
        }

        listView.setAdapter(adapter);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.searchName(String.valueOf(s));

                if (adapter.getCount() == 0){
                    listView.setVisibility(View.GONE);
                    linNoList.setVisibility(View.VISIBLE);
                }else{
                    listView.setVisibility(View.VISIBLE);
                    linNoList.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtBack.setOnClickListener(this);
        btnRequest.setOnClickListener(this);
    }

    public void OneBtnPopup(Context context, String title, String contents, String btnText){

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

    String response = "";

    public class InsertMedicineApplicationNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody body = new FormBody.Builder()
                    .add("USER_NO",""+Utils.userItem.getUserNo())
                    .add("MEDICINE_NAME",edtSearch.getText().toString())
                    .add("STATUS","1")
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.insertMedicineApplication(), body);

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
            try{
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.has("result")){
                    if (jsonObject.get("result").equals("Y")){
                        OneBtnPopup(MedicineSearchActivity.this,"약 등록 요청완료!","약을 정확히 등록하기 위해\n최대 이틀이 소요될 수 있습니다.\n등록 완료 후 알림을 보내드립니다.","확인");
                    }
                }
            }catch (JSONException e){

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_medicine_search_back : {
                if (newbieCheck) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("newbieBack", true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    onBackPressed();
                }
                break;
            }
            case R.id.btn_medicine_search_request : {
                new InsertMedicineApplicationNetWork().execute();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (newbieCheck) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("newbieBack", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        super.onBackPressed();
    }
}
