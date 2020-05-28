package com.kmw.soom2.MyPage.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.R;
import com.kmw.soom2.StaticFunc.Activitys.Item.HistoryItems;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.READ_PERMISSION;
import static com.kmw.soom2.Common.Utils.WRITE_PERMISSION;
import static com.kmw.soom2.Common.Utils.formatYYYYMM;
import static com.kmw.soom2.Common.Utils.userItem;

public class MypageCreateCsvActivity extends AppCompatActivity implements View.OnClickListener  {

    final String TAG = "MypageCreateCsvActivity";
    RelativeLayout relMedicine, relSymptom, relPef, relAct, relDust;
    TextView txtTitle;
    String response;
    Calendar currentCalendar;
    int selectedCategory;
    ArrayList<HistoryItems> historyItems;
    TextView txtRightBtn;
    LinearLayout linBtnPrev, linBtnNext;
    TextView btnBack;

    ProgressDialog progressDialog;

    Calendar calendarCurrent;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_create_csv_activity);

        progressDialog = new ProgressDialog(MypageCreateCsvActivity.this);

        txtTitle = findViewById(R.id.txt_create_csv_title);

        btnBack = findViewById(R.id.txt_statics_detail_back);
        linBtnPrev = findViewById(R.id.lin_create_csv_left_btn);
        linBtnNext = findViewById(R.id.lin_create_csv_right_btn);
        txtRightBtn = findViewById(R.id.txt_create_csv_right_btn);


        relMedicine = findViewById(R.id.rll_mypage_download_medicine_csv);
        relSymptom = findViewById(R.id.rll_mypage_download_symptom_csv);
        relPef = findViewById(R.id.rll_mypage_download_pef_csv);
        relAct = findViewById(R.id.rll_mypage_download_act_csv);
        relDust = findViewById(R.id.rll_mypage_download_dust_csv);

        calendarCurrent = Calendar.getInstance();

        relMedicine.setOnClickListener(this);
        relSymptom.setOnClickListener(this);
        relPef.setOnClickListener(this);
        relAct.setOnClickListener(this);
        relDust.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        linBtnNext.setOnClickListener(this);
        linBtnPrev.setOnClickListener(this);

        currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(new Date(System.currentTimeMillis()));

        txtTitle.setText(formatYYYYMM.format(currentCalendar.getTime()));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rll_mypage_download_medicine_csv:
                selectedCategory = 1;
                TedPermission.with(MypageCreateCsvActivity.this)
                        .setPermissionListener(permissionListener)
                        .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
                break;
            case R.id.rll_mypage_download_symptom_csv:
                selectedCategory = 10;
                TedPermission.with(MypageCreateCsvActivity.this)
                        .setPermissionListener(permissionListener)
                        .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
                break;
            case R.id.rll_mypage_download_pef_csv:
                selectedCategory = 22;
                TedPermission.with(MypageCreateCsvActivity.this)
                        .setPermissionListener(permissionListener)
                        .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
                break;
            case R.id.rll_mypage_download_act_csv:
                selectedCategory = 21;
                TedPermission.with(MypageCreateCsvActivity.this)
                        .setPermissionListener(permissionListener)
                        .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
                break;
            case R.id.rll_mypage_download_dust_csv:
                selectedCategory = 23;
                TedPermission.with(MypageCreateCsvActivity.this)
                        .setPermissionListener(permissionListener)
                        .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
                break;

            case R.id.lin_create_csv_left_btn:
                currentCalendar.add(Calendar.MONTH, -1);
                txtRightBtn.setTextColor(getResources().getColor(R.color.black));
                txtTitle.setText(formatYYYYMM.format(currentCalendar.getTime()));
                break;
            case R.id.lin_create_csv_right_btn:

                if (currentCalendar.get(Calendar.MONTH) == calendarCurrent.get(Calendar.MONTH)){

                }else if (currentCalendar.get(Calendar.MONTH)+1 == calendarCurrent.get(Calendar.MONTH)){
                    currentCalendar.add(Calendar.MONTH, 1);
                    txtRightBtn.setTextColor(getResources().getColor(R.color.acacac));
                    txtTitle.setText(formatYYYYMM.format(currentCalendar.getTime()));
                }else{
                    currentCalendar.add(Calendar.MONTH, 1);
                    txtTitle.setText(formatYYYYMM.format(currentCalendar.getTime()));
                }

                break;
            case R.id.txt_statics_detail_back:
                onBackPressed();
                break;
        }
    }
    public class SelectHomeFeedListNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            String month = "";
            String year = "";

            if (currentCalendar.get(Calendar.MONTH) < 9) {
                month = "0" + (currentCalendar.get(Calendar.MONTH) + 1);
            }else {
                month = "" + (currentCalendar.get(Calendar.MONTH) + 1);
            }

            year = "" + currentCalendar.get(Calendar.YEAR);

            body = (new FormBody.Builder());
            body.add("USER_NO", String.valueOf(userItem.getUserNo()));
            body.add("YEAR", year);
            body.add("MONTH", month);

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectHomeFeedList(), body.build());

                Log.d("Response Signup Sns : ", response);

                return response;
            } catch (IOException e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,s);
            historyItems = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {
                        HistoryItems historyItem = new HistoryItems();


                        historyItem.setUserHistoryNo(JsonIntIsNullCheck(object, "USER_HISTORY_NO"));
                        historyItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                        historyItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));
                        historyItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));
                        historyItem.setNickname(JsonIsNullCheck(object, "NICKNAME"));
                        historyItem.setGender(JsonIntIsNullCheck(object, "GENDER"));
                        historyItem.setBirth(JsonIntIsNullCheck(object, "BIRTH"));

                        historyItem.setMedicineNo(JsonIntIsNullCheck(object,"MEDICINE_NO"));
                        historyItem.setFrequency(JsonIntIsNullCheck(object,"FREQUENCY"));
                        historyItem.setVolume(JsonIsNullCheck(object,"VOLUME"));
                        historyItem.setKo(JsonIsNullCheck(object,"KO"));
                        historyItem.setEmergencyFlag(JsonIntIsNullCheck(object,"EMERGENCY_FLAG"));
                        historyItem.setUnit(JsonIsNullCheck(object,"UNIT"));
                        historyItem.setStartDt(JsonIsNullCheck(object, "START_DT"));
                        historyItem.setEndDt(JsonIsNullCheck(object, "END_DT"));


                        historyItem.setLatitude(JsonIsNullCheck(object, "LATITUDE"));
                        historyItem.setLongitute(JsonIsNullCheck(object, "LONGITUDE"));
                        historyItem.setLocation(JsonIsNullCheck(object, "LOCATION"));
                        historyItem.setDust(JsonIntIsNullCheck(object, "DUST"));
                        historyItem.setDustState(JsonIntIsNullCheck(object, "DUST_STATE"));
                        historyItem.setUltraDust(JsonIntIsNullCheck(object, "ULTRA_DUST"));
                        historyItem.setUltraDustState(JsonIntIsNullCheck(object, "ULTRA_DUST_STATE"));

                        historyItem.setPefScore(JsonIntIsNullCheck(object,"PEF_SCORE"));
                        historyItem.setInspiratorFlag(JsonIntIsNullCheck(object, "INSPIRATOR_FLAG"));

                        historyItem.setActType(JsonIntIsNullCheck(object, "ACT_TYPE"));
                        historyItem.setActScore(JsonIntIsNullCheck(object, "ACT_SCORE"));
                        historyItem.setActState(JsonIntIsNullCheck(object, "ACT_STATE"));
                        historyItem.setActSelected(JsonIsNullCheck(object, "ACT_SELECTED"));

                        historyItem.setCause(JsonIsNullCheck(object, "CAUSE"));

                        historyItems.add(historyItem);
                    }
                }
                createCsvFile();
                progressDialog.dismiss();
            }

            catch (JSONException e){
                progressDialog.dismiss();
                Log.i(TAG, e.getLocalizedMessage());
            }
        }
    }
    void createCsvFile() {


        String fileName = "";
        String firstLine = "";
        if (selectedCategory == 1) {
            fileName = "medicine ";
            firstLine = "카테고리,닉네임,성별,생년월일,날짜,국문명,응급약 여부,투약량,단위,시작일,종료일";
        }else if (selectedCategory == 10) {
            fileName = "symptom ";
            firstLine = "카테고리,닉네임,성별,생년월일,날짜,증상원인";
        }else if (selectedCategory == 22) {
            fileName = "pef ";
            firstLine = "카테고리,닉네임,성별,생년월일,날짜,PEF값,흡입기 사용여부";
        }else if (selectedCategory == 21) {
            fileName = "act ";
            firstLine = "카테고리,닉네임,성별,생년월일,날짜,ACT값,측정 타입,ACT상태,선택한 값";
        }else if (selectedCategory == 23) {
            fileName = "dust ";
            firstLine = "카테고리,닉네임,성별,생년월일,날짜,위치,미세먼지,미세먼지 상태,초미세먼지,초미세먼지 상태,위치,위도,경도";
        }


        fileName = fileName + formatYYYYMM.format(currentCalendar.getTime());

        String baseDir = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        }else{
            baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        try {
//            BufferedWriter fw = new BufferedWriter(new FileWriter(baseDir+"/"+fileName+".csv", true));

            BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(baseDir+"/"+fileName+".csv"), "MS949"));

            fw.write(firstLine);
            fw.newLine();

            for (int i = 0; i < historyItems.size(); i++) {
                HistoryItems item = historyItems.get(i);
                if (selectedCategory == 1) {
                    if (item.getCategory() == selectedCategory) {
                        fw.write("복약" + "," + item.getNickname() + "," + (item.getGender() == 1 ? "남" : "여") + "," + String.valueOf(item.getBirth())
                                + "," + item.getRegisterDt() + "," + item.getKo() + "," + (item.getEmergencyFlag() == 1 ? "일반" : "응급") + "," + item.getFrequency()
                                + "," + item.getUnit() + "," + item.getStartDt() + "," + item.getEndDt());
                        fw.newLine();
                    }
                }else if (selectedCategory == 10) {
                    if (item.getCategory() == 11 || item.getCategory() == 12 || item.getCategory() == 13 || item.getCategory() == 14) {
                        String stCategiryTitle = "";
                        if (item.getCategory() == 11)
                            stCategiryTitle = "기침";
                        else if (item.getCategory() == 12)
                            stCategiryTitle = "호흡곤란";
                        else if (item.getCategory() == 13)
                            stCategiryTitle = "천명음";
                        else if (item.getCategory() == 14)
                            stCategiryTitle = "가슴답답함";

                        String[] causes = new String[]{"감기", "미세먼지", "찬바람", "운동", "집먼지진드기", "애완동물", "음식알러지", "꽃가루", "스트레스", "담배연기", "요리연기", "모르겠어요"};
                        String cause = "";
                        if (item.getCause().length() > 0) {
                            if (item.getCause().contains(",")) {
                                for (String c : item.getCause().split(",")) {
                                    cause += causes[Integer.parseInt(c)] + " ";
                                }
                            }else {
                                cause += causes[Integer.parseInt(item.getCause())];
                            }
                        }
//                        "카테고리,닉네임,성별,생년월일,날짜,증상원인";
                        fw.write(stCategiryTitle + "," + item.getNickname() + "," + (item.getGender() == 1 ? "남" : "여") + "," + item.getBirth() + "," + item.getRegisterDt() + "," + causes);
                        fw.newLine();
                    }
                }else if (selectedCategory == 22) {
//                    "카테고리,닉네임,성별,생년월일,날짜,PEF값,흡입기 사용여부";

                    fw.write("폐기능" + "," + item.getNickname() + "," + (item.getGender() == 1 ? "남" : "여") + "," + item.getBirth()
                            + "," + item.getRegisterDt() + "," + item.getPefScore() + "," + (item.getInspiratorFlag() == 1 ? "사용" : "미사용"));
                    fw.newLine();
                }else if (selectedCategory == 21) {
//                    "카테고리,닉네임,성별,생년월일,날짜,ACT값,측정 타입,ACT상태,선택한 값";
                    String state = "";
                    if (item.getActState() == 1)
                        state = "양호";
                    else if (item.getActState() == 2)
                        state = "주의";
                    else
                        state = "위험";

                    fw.write("ACT" + "," + item.getNickname() + "," + (item.getGender() == 1 ? "남" : "여") + "," + item.getBirth()
                            + "," + item.getRegisterDt() + "," + item.getActScore() + "," + (item.getActType() == 1 ? "일반" : "아동") + "," + state + "," + item.getActSelected());
                    fw.newLine();
                }else if (selectedCategory == 23) {
//                    "카테고리,닉네임,성별,생년월일,날짜,위치,미세먼지,미세먼지 상태,초미세먼지,초미세먼지 상태,위도,경도";
                    String dustState = "";
                    String ultraDustState = "";
                    if (item.getUltraDust() == 1)
                        ultraDustState = "좋음";
                    else if (item.getUltraDust() == 2)
                        ultraDustState = "보통";
                    else if (item.getUltraDust() == 3)
                        ultraDustState = "나쁨";
                    else
                        ultraDustState = "매우나쁨";
                    if (item.getDust() == 1)
                        dustState = "좋음";
                    else if (item.getDust() == 2)
                        dustState = "보통";
                    else if (item.getDust() == 3)
                        dustState = "나쁨";
                    else
                        dustState = "매우나쁨";
                    fw.write("미세먼지" + "," + item.getNickname() + "," + (item.getGender() == 1 ? "남" : "여") + "," + item.getBirth()
                            + "," + item.getRegisterDt() + "," + item.getDust() + "," + dustState + "," + item.getUltraDust() + "," + ultraDustState
                            + "," + item.getLocation() + "," + item.getLatitude() + "," + item.getLongitute());
                    fw.newLine();
                }
            }
            fw.flush();
            fw.close();

            if (historyItems != null) {
                if (historyItems.size() > 0) {

//                    File file = new File()
                    Intent intent = new Intent(Intent.ACTION_SEND);

                    intent.setType("text/csv");
                    intent.putExtra(Intent.EXTRA_EMAIL, "");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "");
                    intent.putExtra(Intent.EXTRA_TEXT, "");

                    String fileUriString = baseDir + "/" + fileName+".csv";
                    Log.d(TAG,"fileUriString : " + fileUriString);
                    Log.d(TAG,"getPackageName()+\".fileprovider\" : " + getPackageName()+".fileprovider");
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri providerURI = FileProvider.getUriForFile( getApplicationContext() ,getPackageName()+".fileprovider" , new File(fileUriString));
                    intent.putExtra(Intent.EXTRA_STREAM, providerURI);
                    intent.setDataAndType(providerURI, "text/*");
                    startActivity(Intent.createChooser(intent, "Send CSV"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    PermissionListener permissionListener = new PermissionListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionGranted() {
            Log.i(TAG, "허가");

            new SelectHomeFeedListNetwork().execute();
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Log.i(TAG,"거부 : " + deniedPermissions.toString());
            if (deniedPermissions.contains("android.permission.READ_EXTERNAL_STORAGE") || deniedPermissions.contains("android.permission.WRITE_EXTERNAL_STORAGE")){
                WRITE_PERMISSION = false;
                READ_PERMISSION = false;
                Toast.makeText(MypageCreateCsvActivity.this, "권한을 거부하였습니다.", Toast.LENGTH_SHORT).show();
            }else{
                WRITE_PERMISSION = true;
                READ_PERMISSION = true;

            }
        }
    };
}