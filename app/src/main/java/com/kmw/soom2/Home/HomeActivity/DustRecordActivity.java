package com.kmw.soom2.Home.HomeActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker;
import com.kmw.soom2.Common.GpsTracker;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gun0912.tedpermission.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.LOCATION_PERMISSION;
import static com.kmw.soom2.Common.Utils.formatHHMM;
import static com.kmw.soom2.Common.Utils.formatHHMMSS;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD2;

public class DustRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "DustRecordActivity";
    TextView txtBack;
    ImageView imgRemove;
    TextView txtDate, txtTime;
    EditText edtLocation, edtDust, edtUltraDust;
    ImageView imgLocationClose, imgDustClose, imgUltraDustClose;
    LinearLayout linLocationBottom, linDustBottom, linUltraDustBottom;
    LinearLayout linDustDetail;
    Button btnRetry, btnFinish;
    Calendar currentCalendar;

    BottomSheetDialog measureBottomSheetDialog;

    Address address;
    int addressPoint = 0;
    Geocoder geocoder;
    List<Address> addressList = null;
    LocationManager locationManager = null;
    ProgressDialog progressDialog;

    private GpsTracker gpsTracker;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    Intent beforeIntent;

    String dustStatus = "";
    String ultraDustStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dust_record);

        beforeIntent = getIntent();

        addressList = new ArrayList<>();

        progressDialog = new ProgressDialog(this);

        FindViewById();

//        edtLocation.setEnabled(false);
//        edtDust.setEnabled(false);
//        edtUltraDust.setEnabled(false);

        imgRemove.setVisibility(View.GONE);

        if (beforeIntent != null){
            if (beforeIntent.hasExtra("registerDt")){

                try {
                    txtDate.setText(formatYYYYMMDD2.format(formatYYYYMMDD.parse(beforeIntent.getStringExtra("registerDt").substring(0,10))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    txtTime.setText(Utils.formatHHMM.format(Utils.formatHHMMSS.parse(beforeIntent.getStringExtra("registerDt").substring(11,18))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                edtLocation.setEnabled(true);
                edtDust.setEnabled(true);
                edtUltraDust.setEnabled(true);

                Utils.LAT = Float.parseFloat(beforeIntent.getStringExtra("lat"));
                Utils.LNG = Float.parseFloat(beforeIntent.getStringExtra("lng"));
                Utils.LOCATION = beforeIntent.getStringExtra("location");
                Utils.DUST = beforeIntent.getIntExtra("dust",0);
                Utils.ULTRA_DUST = beforeIntent.getIntExtra("ultraDust",0);
                dustStatus = String.valueOf(beforeIntent.getIntExtra("dustStatus",0));
                ultraDustStatus = String.valueOf(beforeIntent.getIntExtra("ultraDustStatus",0));

                edtLocation.setText(Utils.LOCATION);
                edtDust.setText(""+Utils.DUST);
                edtUltraDust.setText(""+Utils.ULTRA_DUST);

                imgRemove.setVisibility(View.VISIBLE);

            }else if (beforeIntent.hasExtra("date")){
                txtDate.setText(beforeIntent.getStringExtra("date"));
                txtTime.setText(beforeIntent.getStringExtra("time"));
            }else{
                txtDate.setText(Utils.formatYYYYMMDD2.format(new Date(System.currentTimeMillis())));
                txtTime.setText(Utils.formatHHMM.format(new Date(System.currentTimeMillis())));
            }
        }else{
            currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(new Date(System.currentTimeMillis()));

            txtDate.setText(Utils.formatYYYYMMDD2.format(new Date(System.currentTimeMillis())));
            txtTime.setText(Utils.formatHHMM.format(new Date(System.currentTimeMillis())));
        }
    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_dust_record_back);
        txtDate = (TextView)findViewById(R.id.txt_dust_record_date);
        txtTime = (TextView)findViewById(R.id.txt_dust_record_time);
        imgRemove = (ImageView)findViewById(R.id.img_dust_record_remove);

        edtLocation = (EditText)findViewById(R.id.edt_dust_record_location);
        edtDust = (EditText)findViewById(R.id.edt_dust_record_dust);
        edtUltraDust = (EditText)findViewById(R.id.edt_dust_record_ultra_dust);

        imgLocationClose = (ImageView)findViewById(R.id.img_dust_record_location_close);
        imgDustClose = (ImageView)findViewById(R.id.img_dust_record_dust_close);
        imgUltraDustClose = (ImageView)findViewById(R.id.img_dust_record_ultra_dust_close);

        linLocationBottom = (LinearLayout)findViewById(R.id.lin_dust_record_location_bottom);
        linDustBottom = (LinearLayout)findViewById(R.id.lin_dust_record_dust_bottom);
        linUltraDustBottom = (LinearLayout)findViewById(R.id.lin_dust_record_ultra_dust_bottom);

        linDustDetail = (LinearLayout)findViewById(R.id.lin_dust_record_detail);

        btnRetry = (Button)findViewById(R.id.btn_dust_record_retry);
        btnFinish = (Button)findViewById(R.id.btn_dust_record_finish);

        txtBack.setOnClickListener(this);
        imgRemove.setOnClickListener(this);

        imgLocationClose.setOnClickListener(this);
        imgDustClose.setOnClickListener(this);
        imgUltraDustClose.setOnClickListener(this);

        linDustDetail.setOnClickListener(this);

        btnRetry.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);
    }

    PermissionListener permissionListener = new PermissionListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionGranted() {
            LOCATION_PERMISSION = true;
            geocoder = new Geocoder(DustRecordActivity.this);
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Log.i(TAG,"거부 : " + deniedPermissions.toString());
            if (deniedPermissions.contains("android.permission.ACCESS_FINE_LOCATION") || deniedPermissions.contains("android.permission.ACCESS_COARSE_LOCATION")){
                LOCATION_PERMISSION = false;
            }
        }
    };

    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        Utils.LAT = (float) latitude;
        Utils.LNG = (float) longitude;

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);

        if (address.getAdminArea() == null && address.getSubLocality() == null){
            address = addressList.get(addressPoint++);
            Log.i(TAG,"address test1");
        }else{
            Utils.LOCATION = address.getLocality();
            if (address.getSubLocality() == null){
                Utils.LOCATION = address.getThoroughfare();
                if (address.getThoroughfare() == null){
                    Utils.LOCATION = address.getSubThoroughfare();
                }else{
                    Utils.LOCATION = address.getLocality();
                }
            }else{
                Utils.LOCATION = address.getSubLocality();
            }

            Utils.SIDO_NAME = address.getAdminArea();

            if (address.getLocality() == null){
                Utils.GUNGU_NAME = address.getSubLocality();
            }else{
                Utils.GUNGU_NAME = address.getLocality();
            }

            if (Utils.SIDO_NAME.contains("서울")) {
                Utils.SIDO_NAME = "서울";
            } else if (Utils.SIDO_NAME.contains("충청남도")) {
                Utils.SIDO_NAME = "충남";
            } else if (Utils.SIDO_NAME.contains("충청북도")) {
                Utils.SIDO_NAME = "충북";
            } else if (Utils.SIDO_NAME.contains("충청")) {
                Utils.SIDO_NAME = "충남";
            } else if (Utils.SIDO_NAME.contains("경상남도")) {
                Utils.SIDO_NAME = "경남";
            } else if (Utils.SIDO_NAME.contains("경상북도")) {
                Utils.SIDO_NAME = "경북";
            } else if (Utils.SIDO_NAME.contains("경상")) {
                Utils.SIDO_NAME = "경남";
            } else if (Utils.SIDO_NAME.contains("전라남도")) {
                Utils.SIDO_NAME = "전남";
            } else if (Utils.SIDO_NAME.contains("전라북도")) {
                Utils.SIDO_NAME = "전북";
            } else if (Utils.SIDO_NAME.contains("전라")) {
                Utils.SIDO_NAME = "전남";
            } else if (Utils.SIDO_NAME.contains("광주")) {
                Utils.SIDO_NAME = "광주";
            } else if (Utils.SIDO_NAME.contains("대구")) {
                Utils.SIDO_NAME = "대구";
            } else if (Utils.SIDO_NAME.contains("부산")) {
                Utils.SIDO_NAME = "부산";
            } else if (Utils.SIDO_NAME.contains("울산")) {
                Utils.SIDO_NAME = "울산";
            } else if (Utils.SIDO_NAME.contains("인천")) {
                Utils.SIDO_NAME = "인천";
            } else if (Utils.SIDO_NAME.contains("대전")) {
                Utils.SIDO_NAME = "대전";
            } else if (Utils.SIDO_NAME.contains("제주")) {
                Utils.SIDO_NAME = "제주";
            }
            new DustAycnk().execute(Utils.SIDO_NAME.substring(0, 2));
        }

        return address.getAddressLine(0).toString()+"\n";

    }

    public void TwoBtnSettingPopup(Context context, String title, String contents, String btnLeftText, String btnRightText){

        final Dialog dateTimeDialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.two_btn_popup,null);

        TextView txtTitle = (TextView)layout.findViewById(R.id.txt_two_btn_popup_title);
        TextView txtContents = (TextView)layout.findViewById(R.id.txt_two_btn_popup_contents);
        final Button btnLeft = (Button)layout.findViewById(R.id.btn_two_btn_popup_left);
        Button btnRight = (Button)layout.findViewById(R.id.btn_two_btn_popup_right);

        dateTimeDialog.setCancelable(false);

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
                onBackPressed();
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+getPackageName()));
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public void TwoBtnPopup(Context context, String title, String contents, String btnLeftText, String btnRightText,int flag){

        final Dialog dateTimeDialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.two_btn_popup,null);

        TextView txtTitle = (TextView)layout.findViewById(R.id.txt_two_btn_popup_title);
        TextView txtContents = (TextView)layout.findViewById(R.id.txt_two_btn_popup_contents);
        final Button btnLeft = (Button)layout.findViewById(R.id.btn_two_btn_popup_left);
        Button btnRight = (Button)layout.findViewById(R.id.btn_two_btn_popup_right);

        dateTimeDialog.setCancelable(false);

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
                if (Utils.DUST > 0){
                    dateTimeDialog.dismiss();
                }else{
                    dateTimeDialog.dismiss();
                    edtLocation.setEnabled(true);
                    edtDust.setEnabled(true);
                    edtUltraDust.setEnabled(true);
                }
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialog.dismiss();
                if(flag == 0) {
                    if (ActivityCompat.checkSelfPermission(DustRecordActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DustRecordActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        TwoBtnSettingPopup(DustRecordActivity.this, "위치 서비스 권장 설정", "현재 위치에 대한 대기질의 정보를 확인하기 위해선 위치 서비스 접근 권한이 필요합니다.\n동의하시는 경우 설정하기 버튼을 눌러주세요.", "취소", "설정하기");
                        return;
                    } else {
                        gpsTracker = new GpsTracker(DustRecordActivity.this);

                        double latitude = gpsTracker.getLatitude();
                        double longitude = gpsTracker.getLongitude();

                        getCurrentAddress(latitude, longitude);
                    }
                }else {
                    new DeleteDustHomeFeedHistoryNetWork().execute();

                }
            }
        });
    }

    void dustDetailPopup(){
        final Dialog dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_dust_detail,null);

        ImageView imgClose = (ImageView)layout.findViewById(R.id.img_dust_detail_popup_close);

        dialog.setContentView(layout);
        dialog.show();

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            if (location != null) {
                Utils.LAT = (float) location.getLatitude();
                Utils.LNG = (float) location.getLongitude();
                Log.i(TAG,"1");
                try {
                    geocoder = new Geocoder(DustRecordActivity.this, Locale.getDefault());
                    addressList = geocoder.getFromLocation(Utils.LAT, Utils.LNG, 5);

                    Log.i(TAG,"2");

                    if (addressList.size() > 0) {
                        address = addressList.get(addressPoint);

                        for (int i = 0; i < addressList.size(); i++){
//                            Toast.makeText(MainActivity.this, "address : " + address, Toast.LENGTH_SHORT).show();
                            Log.i(TAG,"address : " + address);
                        }

                        if (address.getAdminArea() == null && address.getSubLocality() == null){
                            address = addressList.get(addressPoint++);
                            Log.i(TAG,"address test1");
                        }else{
                            Utils.LOCATION = address.getLocality();
                            if (address.getSubLocality() == null){
                                Utils.LOCATION = address.getThoroughfare();
                                if (address.getThoroughfare() == null){
                                    Utils.LOCATION = address.getSubThoroughfare();
                                }else{
                                    Utils.LOCATION = address.getLocality();
                                }
                            }else{
                                Utils.LOCATION = address.getSubLocality();
                            }

                            Utils.SIDO_NAME = address.getAdminArea();

                            if (address.getLocality() == null){
                                Utils.GUNGU_NAME = address.getSubLocality();
                            }else{
                                Utils.GUNGU_NAME = address.getLocality();
                            }

                            if (Utils.SIDO_NAME.contains("서울")) {
                                Utils.SIDO_NAME = "서울";
                            } else if (Utils.SIDO_NAME.contains("충청남도")) {
                                Utils.SIDO_NAME = "충남";
                            } else if (Utils.SIDO_NAME.contains("충청북도")) {
                                Utils.SIDO_NAME = "충북";
                            } else if (Utils.SIDO_NAME.contains("충청")) {
                                Utils.SIDO_NAME = "충남";
                            } else if (Utils.SIDO_NAME.contains("경상남도")) {
                                Utils.SIDO_NAME = "경남";
                            } else if (Utils.SIDO_NAME.contains("경상북도")) {
                                Utils.SIDO_NAME = "경북";
                            } else if (Utils.SIDO_NAME.contains("경상")) {
                                Utils.SIDO_NAME = "경남";
                            } else if (Utils.SIDO_NAME.contains("전라남도")) {
                                Utils.SIDO_NAME = "전남";
                            } else if (Utils.SIDO_NAME.contains("전라북도")) {
                                Utils.SIDO_NAME = "전북";
                            } else if (Utils.SIDO_NAME.contains("전라")) {
                                Utils.SIDO_NAME = "전남";
                            } else if (Utils.SIDO_NAME.contains("광주")) {
                                Utils.SIDO_NAME = "광주";
                            } else if (Utils.SIDO_NAME.contains("대구")) {
                                Utils.SIDO_NAME = "대구";
                            } else if (Utils.SIDO_NAME.contains("부산")) {
                                Utils.SIDO_NAME = "부산";
                            } else if (Utils.SIDO_NAME.contains("울산")) {
                                Utils.SIDO_NAME = "울산";
                            } else if (Utils.SIDO_NAME.contains("인천")) {
                                Utils.SIDO_NAME = "인천";
                            } else if (Utils.SIDO_NAME.contains("대전")) {
                                Utils.SIDO_NAME = "대전";
                            } else if (Utils.SIDO_NAME.contains("제주")) {
                                Utils.SIDO_NAME = "제주";
                            }
                            new DustAycnk().execute(Utils.SIDO_NAME.substring(0, 2));
                        }

                        if (addressPoint == addressList.size()-1){
                            addressPoint = 0;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                Log.i(TAG,"location null");
            }
            // TODO 위도, 경도로 하고 싶은 것
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

    public class DustAycnk extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DustRecordActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            BufferedReader br = null;
            String result = "";
            try {

                StringBuilder urlBuilder = new StringBuilder("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst"); /*URL*/
                urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=ap0uRuiFparH2V6e49%2BO0gNL%2FFsZ1nnH96xTngWb7pxaYn1miMkHNCFpFvAk156fur7sS5nUeNXZ6woNrpAgdA%3D%3D"); /*Service Key*/
                urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8")); /*한 페이지 결과 수*/
                urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
                urlBuilder.append("&" + URLEncoder.encode("sidoName", "UTF-8") + "=" + URLEncoder.encode(strings[0], "UTF-8")); /*시도 이름 (서울, 부산, 대구, 인천, 광주, 대전, 울산, 경기, 강원, 충북, 충남, 전북, 전남, 경북, 경남, 제주, 세종)*/
                urlBuilder.append("&" + URLEncoder.encode("searchCondition", "UTF-8") + "=" + URLEncoder.encode("HOUR", "UTF-8")); /*요청 데이터기간 (시간 : HOUR, 하루 : DAILY)*/
                urlBuilder.append("&" + URLEncoder.encode("_returnType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));

                URL url = new URL(urlBuilder.toString());
                HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
                urlconnection.setRequestMethod("GET");

                br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));

                String line;
                while ((line = br.readLine()) != null) {
                    result = result + line + "\n";
                }
                urlconnection.setConnectTimeout(3000);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i(TAG, "result : " + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if (Utils.GUNGU_NAME.length() == 0){
                        Toast.makeText(DustRecordActivity.this, "위치를 불러오는데 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }else if (JsonIsNullCheck(jsonObject1,"cityName").contains(Utils.GUNGU_NAME)) {
                        Utils.DUST = jsonObject1.getInt("pm10Value");
                        Utils.ULTRA_DUST = jsonObject1.getInt("pm25Value");

                        if (Utils.DUST < 31) {
                            dustStatus = "1";
                        } else if (Utils.DUST > 30 && Utils.DUST < 81) {
                            dustStatus = "2";
                        } else if (Utils.DUST > 80 && Utils.DUST < 151) {
                            dustStatus = "3";
                        } else {
                            dustStatus = "4";
                        }

                        if (Utils.ULTRA_DUST < 16) {
                            ultraDustStatus = "1";
                        } else if (Utils.ULTRA_DUST > 15 && Utils.ULTRA_DUST < 36) {
                            ultraDustStatus = "2";
                        } else if (Utils.ULTRA_DUST > 35 && Utils.ULTRA_DUST < 76) {
                            ultraDustStatus = "3";
                        } else {
                            ultraDustStatus = "4";
                        }

                        edtLocation.setText(Utils.GUNGU_NAME);
                        edtDust.setText(""+Utils.DUST);
                        edtUltraDust.setText(""+Utils.ULTRA_DUST);
                    }
                }
            } catch (JSONException e) {

            }
            progressDialog.dismiss();
        }
    }

    String response = "";

    public class InsertDustHomeFeedHistoryNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            Log.i(TAG,"dt : " + Utils.formatYYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis())) + " lat : " + Utils.LAT + " lng : " + Utils.LNG +
                    " location : " + Utils.LOCATION + " dust : " + Utils.DUST + " dust status : " + dustStatus + " ultra dust : " + Utils.ULTRA_DUST + "ultra dust status : " + ultraDustStatus);


            FormBody.Builder body;
            body = (new FormBody.Builder());
            body.add("USER_NO",""+Utils.userItem.getUserNo());
            body.add("CATEGORY","23");
            try {
                body.add("REGISTER_DT", formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString()))
                        + " " + formatHHMMSS.format(formatHHMM.parse(txtTime.getText().toString())));
            } catch (ParseException e) {
                e.printStackTrace();
                body.add("REGISTER_DT",Utils.formatYYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis())));
            }


            if (Utils.LAT > 0.0) {
                body.add("LATITUDE", "" + Utils.LAT);
            }
            if (Utils.LNG > 0.0) {
                body.add("LONGITUDE",""+Utils.LNG);
            }
            body.add("LOCATION",""+edtLocation.getText().toString());
            body.add("DUST",""+edtDust.getText().toString());
            body.add("ULTRA_DUST",""+edtUltraDust.getText().toString());

            int dust = Integer.parseInt(edtDust.getText().toString());
            int dustState = 0;
            if (dust >= 0 && dust < 31){
                dustState = 1;
            }else if (dust > 30 && dust < 81){
                dustState = 2;
            }else if (dust > 80 && dust < 151){
                dustState = 3;
            }else {
                dustState = 4;
            }

            body.add("DUST_STATE",String.valueOf(dustState));
            int ultraDust = Integer.parseInt(edtUltraDust.getText().toString());
            int ultraDustState = 0;
            if (ultraDust >= 0 && ultraDust < 16) {
                ultraDustState = 1;
            }else if (ultraDust > 15 && ultraDust < 36) {
                ultraDustState = 2;
            }else if (ultraDust > 35 && ultraDust < 76) {
                ultraDustState = 3;
            }else {
                ultraDustState = 4;
            }
            body.add("ULTRA_DUST_STATE",String.valueOf(ultraDustState));
            body.add("NICKNAME", Utils.userItem.getNickname());
            body.add("GENDER", String.valueOf(Utils.userItem.getGender()));
            body.add("BIRTH", String.valueOf(Utils.userItem.getBirth()));


            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.insertDustHomeFeedHistoryList(), body.build());
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
                    setResult(RESULT_OK);
                    onBackPressed();
                }
            },500);
        }
    }

    public class UpdateDustHomeFeedHistoryNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            FormBody.Builder body;
            body = (new FormBody.Builder());
            body.add("USER_HISTORY_NO",beforeIntent.getStringExtra("medicineHistoryNo"));
            body.add("CATEGORY","23");
//            body.add("REGISTER_DT",beforeIntent.getStringExtra("registerDt"));

            try {
                body.add("REGISTER_DT", formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString()))
                        + " " + formatHHMMSS.format(formatHHMM.parse(txtTime.getText().toString())));
            } catch (ParseException e) {
                e.printStackTrace();
                body.add("REGISTER_DT",beforeIntent.getStringExtra("registerDt"));
            }

            if (Utils.LAT > 0.0) {
                body.add("LATITUDE", "" + Utils.LAT);
            }
            if (Utils.LNG > 0.0) {
                body.add("LONGITUDE",""+Utils.LNG);
            }
            body.add("LOCATION",""+edtLocation.getText().toString());
            body.add("DUST",""+edtDust.getText().toString());
            body.add("ULTRA_DUST",""+edtUltraDust.getText().toString());

            int dust = Integer.parseInt(edtDust.getText().toString());
            int dustState = 0;
            if (dust >= 0 && dust < 31){
                dustState = 1;
            }else if (dust > 30 && dust < 81){
                dustState = 2;
            }else if (dust > 80 && dust < 151){
                dustState = 3;
            }else {
                dustState = 4;
            }

            body.add("DUST_STATE",String.valueOf(dustState));
            int ultraDust = Integer.parseInt(edtUltraDust.getText().toString());
            int ultraDustState = 0;
            if (ultraDust >= 0 && ultraDust < 16) {
                ultraDustState = 1;
            }else if (ultraDust > 15 && ultraDust < 36) {
                ultraDustState = 2;
            }else if (ultraDust > 35 && ultraDust < 76) {
                ultraDustState = 3;
            }else {
                ultraDustState = 4;
            }
            body.add("ULTRA_DUST_STATE",String.valueOf(ultraDustState));


            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateDustHomeFeedHistoryList(), body.build());
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
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setResult(RESULT_OK);
                    onBackPressed();
                }
            },500);
        }
    }

    public class DeleteDustHomeFeedHistoryNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            @SuppressLint("WrongThread") RequestBody body = new FormBody.Builder()
                    .add("USER_HISTORY_NO",beforeIntent.getStringExtra("medicineHistoryNo"))
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
                    setResult(RESULT_OK);
                    onBackPressed();
                }
            },500);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_dust_record_back : {
                onBackPressed();
                break;
            }
            case R.id.img_dust_record_remove: {
                TwoBtnPopup(this,"미세먼지","미세먼지 기록을 삭제하시겠습니까?","취소","확인",1);
                break;
            }
            case R.id.img_dust_record_location_close : {
                edtLocation.setText("");
                break;
            }
            case R.id.img_dust_record_dust_close : {
                edtLocation.setText("");
                break;
            }
            case R.id.img_dust_record_ultra_dust_close : {
                edtLocation.setText("");
                break;
            }
            case R.id.lin_dust_record_detail : {
                dustDetailPopup();
                break;
            }
            case R.id.btn_dust_record_retry : {
                if (Utils.DUST > 0){
                    TwoBtnPopup(this,"미세먼지 정보","미세먼지 정보를 다시 불러오시겠습니까?","취소","확인",0);
                }else{
                    TwoBtnPopup(this,"미세먼지 정보","미세먼지 수치를 가져오지 못했습니다.\n다시 시도하시겠습니까?","직접입력","재시도",0);
                }
                break;
            }
            case R.id.btn_dust_record_finish : {
                if (beforeIntent != null){
                    if (beforeIntent.hasExtra("registerDt")){
                        new UpdateDustHomeFeedHistoryNetWork().execute();
                    }else{
                        new InsertDustHomeFeedHistoryNetWork().execute();
                    }
                }else{
                    new InsertDustHomeFeedHistoryNetWork().execute();
                }
                break;
            }
            case R.id.txt_dust_record_date: {
//                try {
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                DateTimePicker(1, "날짜를 선택해주세요.");
                break;
            }
            case R.id.txt_dust_record_time: {
                DateTimePicker("시간을 선택해주세요.");
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (beforeIntent != null) {
            if (beforeIntent.hasExtra("registerDt")) {
                btnFinish.setText("수정");

            }else{
                btnFinish.setText("완료");
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    TwoBtnSettingPopup(this,"위치 서비스 권장 설정","현재 위치에 대한 대기질의 정보를 확인하기 위해선 위치 서비스 접근 권한이 필요합니다.\n동의하시는 경우 설정하기 버튼을 눌러주세요.","취소","설정하기");
                    return;
                }else{
                    gpsTracker = new GpsTracker(DustRecordActivity.this);

                    double latitude = gpsTracker.getLatitude();
                    double longitude = gpsTracker.getLongitude();

                    String address = getCurrentAddress(latitude, longitude);

                    Log.i(TAG,"address : " + address);
                }
            }
        }else{
            btnFinish.setText("완료");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                TwoBtnSettingPopup(this,"위치 서비스 권장 설정","현재 위치에 대한 대기질의 정보를 확인하기 위해선 위치 서비스 접근 권한이 필요합니다.\n동의하시는 경우 설정하기 버튼을 눌러주세요.","취소","설정하기");
                return;
            }else{
                gpsTracker = new GpsTracker(DustRecordActivity.this);

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                String address = getCurrentAddress(latitude, longitude);

                Log.i(TAG,"address : " + address);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void DateTimePicker(final int flag, String title){
        View dateTimeView = getLayoutInflater().inflate(R.layout.pop_up_date_time_picker,null);
        final Dialog dateDialog = new Dialog(DustRecordActivity.this);
        TextView txtTitle = (TextView)dateTimeView.findViewById(R.id.txt_calendar_picker_title);

        txtTitle.setText(title);

        dateDialog.setContentView(dateTimeView);
        dateDialog.show();

        final SingleDateAndTimePicker singleDateAndTimePicker = dateTimeView.findViewById(R.id.single_day_picker);

        singleDateAndTimePicker.setDisplayYears(true);
        singleDateAndTimePicker.setDisplayMonths(true);
        singleDateAndTimePicker.setDisplayDaysOfMonth(true);
        singleDateAndTimePicker.setDisplayHours(false);
        singleDateAndTimePicker.setDisplayMinutes(false);

        Date date = null;

        Log.d(TAG,"txtDate : " + txtDate.getText().toString());

        try {
            date = formatYYYYMMDD2.parse(txtDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        singleDateAndTimePicker.selectDate(calendar);

        singleDateAndTimePicker.clickDateChange(new SingleDateAndTimePicker.OnCustomClick() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                Log.d(TAG, "onDateChanged " + displayed + ", date : " + date);
                txtDate.setText(formatYYYYMMDD2.format(date));
                dateDialog.dismiss();
            }
        });

        singleDateAndTimePicker.clickCancelDialog(new SingleDateAndTimePicker.OnCancelClick() {
            @Override
            public void onDialogCancel() {
                dateDialog.dismiss();
            }
        });
    }
    void DateTimePicker(String title) {
        View dateTimeView = getLayoutInflater().inflate(R.layout.pop_up_date_time_picker,null);
        final Dialog dateTimeDialog = new Dialog(DustRecordActivity.this);
        TextView txtTitle = (TextView)dateTimeView.findViewById(R.id.txt_calendar_picker_title);

        txtTitle.setText(title);

        dateTimeDialog.setContentView(dateTimeView);
        dateTimeDialog.show();

        final SingleDateAndTimePicker singleDateAndTimePicker = (SingleDateAndTimePicker)dateTimeView.findViewById(R.id.single_day_picker);

        singleDateAndTimePicker.setDisplayYears(false);
        singleDateAndTimePicker.setDisplayMonths(false);
        singleDateAndTimePicker.setDisplayDaysOfMonth(false);
        singleDateAndTimePicker.setDisplayHours(true);
        singleDateAndTimePicker.setDisplayMinutes(true);

        Date date = null;

        try {
            date = Utils.formatHHMM.parse(txtTime.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        singleDateAndTimePicker.selectDate(calendar);

        singleDateAndTimePicker.clickDateChange(new SingleDateAndTimePicker.OnCustomClick() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                Log.d(TAG, "onDateChanged " + displayed + ", date : " + date);
                txtTime.setText(Utils.formatHHMM.format(date));
                dateTimeDialog.dismiss();
            }
        });

        singleDateAndTimePicker.clickCancelDialog(new SingleDateAndTimePicker.OnCancelClick() {
            @Override
            public void onDialogCancel() {
                dateTimeDialog.dismiss();
            }
        });
    }
}
