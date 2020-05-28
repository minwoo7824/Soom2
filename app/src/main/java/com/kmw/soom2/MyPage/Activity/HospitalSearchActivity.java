package com.kmw.soom2.MyPage.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kmw.soom2.MyPage.Adapter.HospitalListAdapter;
import com.kmw.soom2.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class HospitalSearchActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "HospitalSearchActivity";
    TextView txtBack;
    EditText edtSearch;
    ListView listView;
    HospitalListAdapter adapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_search);

        FindViewById();

    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_hospital_search_back);
        edtSearch = (EditText)findViewById(R.id.edt_hospital_search);
        listView = (ListView)findViewById(R.id.list_view_hospital_search);

        txtBack.setOnClickListener(this);

        edtSearch.setImeOptions(EditorInfo.IME_ACTION_GO);


        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                switch (i){
                    case EditorInfo.IME_ACTION_GO : {
                        if (edtSearch.getText().length() > 0){
                            InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow( edtSearch.getWindowToken(), 0);
                            new HospitalListAycnkA().execute(edtSearch.getText().toString());
                        }else{
                            Toast.makeText(HospitalSearchActivity.this, "검색명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
                return true;
            }
        });
    }

    void parseXML(String xml) {
        int itemtype = 0;

        ArrayList<String> hospitalNameList = new ArrayList<>();
        ArrayList<String> hospitalAddressList = new ArrayList<>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));
            // 여기까지 의미있는부분을 짤라준다.
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("dutyAddr")) {
                            itemtype = 1;
                        }
                        if (parser.getName().equals("dutyName")){
                            itemtype = 2;
                        }

                    case XmlPullParser.END_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        if (itemtype == 1) {
                            hospitalAddressList.add(parser.getText());
                            itemtype = 0;
                        }
                        if (itemtype == 2) {
                            hospitalNameList.add(parser.getText());
                            itemtype = 0;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {

        }

        for (int i = 0; i < hospitalNameList.size(); i++){
            adapter.addItem(hospitalNameList.get(i),hospitalAddressList.get(i));
        }
        listView.setAdapter(adapter);
    }


    public class HospitalListAycnkA extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(HospitalSearchActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            adapter = new HospitalListAdapter(HospitalSearchActivity.this);
            BufferedReader br = null;
            String result = "";
            try {
                String encoding = URLEncoder.encode(strings[0],"UTF-8");
                StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncListInfoInqire?QN="+encoding+"&QZ=A&pageNo=1&numOfRows=1000&ServiceKey=ap0uRuiFparH2V6e49%2BO0gNL%2FFsZ1nnH96xTngWb7pxaYn1miMkHNCFpFvAk156fur7sS5nUeNXZ6woNrpAgdA%3D%3D");
                URL url = new URL(urlBuilder.toString());
                HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
                urlconnection.setRequestMethod("GET");
                br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
                String line;

                while ((line = br.readLine()) != null) {
                    result = result + line + "\n";
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            Log.i(TAG,"result : " + result);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            parseXML(s);

            new HospitalListAycnkB().execute(edtSearch.getText().toString());
        }
    }

    public class HospitalListAycnkB extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            BufferedReader br = null;
            String result = "";
            try {

                String encoding = URLEncoder.encode(strings[0],"UTF-8");
                StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncListInfoInqire?QN="+encoding+"&QZ=B&pageNo=1&numOfRows=1000&ServiceKey=ap0uRuiFparH2V6e49%2BO0gNL%2FFsZ1nnH96xTngWb7pxaYn1miMkHNCFpFvAk156fur7sS5nUeNXZ6woNrpAgdA%3D%3D"); /*URL*/

                URL url = new URL(urlBuilder.toString());
                HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
                urlconnection.setRequestMethod("GET");
                br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));

                String line;
                while ((line = br.readLine()) != null) {
                    result = result + line + "\n";
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            parseXML(s);

            new HospitalListAycnkC().execute(edtSearch.getText().toString());

        }
    }

    public class HospitalListAycnkC extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            BufferedReader br = null;
            String result = "";
            try {

                String encoding = URLEncoder.encode(strings[0],"UTF-8");
                StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncListInfoInqire?QN="+encoding+"&QZ=C&pageNo=1&numOfRows=1000&ServiceKey=ap0uRuiFparH2V6e49%2BO0gNL%2FFsZ1nnH96xTngWb7pxaYn1miMkHNCFpFvAk156fur7sS5nUeNXZ6woNrpAgdA%3D%3D"); /*URL*/

                URL url = new URL(urlBuilder.toString());
                HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
                urlconnection.setRequestMethod("GET");
                br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));

                String line;
                while ((line = br.readLine()) != null) {
                    result = result + line + "\n";
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            parseXML(s);

            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_hospital_search_back : {
                onBackPressed();
                break;
            }
        }
    }
}
